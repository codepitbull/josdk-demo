package de.codepitbull.josdk.demo.resource;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(K3sTestResourceLifecycleManager.class)
public class DemoReconcilerTest {

    KubernetesClient kubernetesClient;

    @Test
    public void test() {
        kubernetesClient.apps().deployments().list().getItems().forEach(d -> System.out.println(d.getMetadata().getName()));
    }

}
