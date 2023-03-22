package de.codepitbull.josdk.demo.resource.v1;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@QuarkusTestResource(K3sTestResourceLifecycleManager.class)
public class DemoReconcilerTest {

  KubernetesClient kubernetesClient;

  @Test
  public void test_customResourceApplied() {
    assertThat(kubernetesClient.apiextensions().v1().customResourceDefinitions().list().getItems())
      .extracting("metadata.name")
      .contains("demoresources.de.codepitbull.josdk.demo");
  }

}
