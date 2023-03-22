package de.codepitbull.josdk.demo.resource.v1;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.k3s.K3sContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;

public class K3sTestResourceLifecycleManager implements QuarkusTestResourceLifecycleManager {

  K3sContainer k3s;
  KubernetesClient kubernetesClient;

  @Override
  public Map<String, String> start() {
    k3s = new K3sContainer(DockerImageName.parse("rancher/k3s:v1.22.13-k3s1"));
    k3s.start();
    try {
      File tempFile = File.createTempFile("kubeconfig-", ".yaml");
      tempFile.deleteOnExit();
      Files.write(tempFile.toPath(), k3s.getKubeConfigYaml().getBytes());
      kubernetesClient = new KubernetesClientBuilder().withConfig(Serialization.unmarshal(k3s.getKubeConfigYaml(), Config.class)).build();
      System.setProperty("kubeconfig", tempFile.getAbsolutePath());
      return Collections.emptyMap();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  @Override
  public void inject(TestInjector testInjector) {
    testInjector.injectIntoFields(kubernetesClient, new TestInjector.MatchesType(KubernetesClient.class));
  }

  @Override
  public void stop() {
    k3s.stop();
  }
}
