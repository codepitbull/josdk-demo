package de.codepitbull.josdk.demo.resource.v1.dependent;

import de.codepitbull.josdk.demo.resource.v1.DemoResource;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.dependent.ReconcileResult;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;

import static de.codepitbull.josdk.demo.resource.v1.Constants.*;
import static de.codepitbull.josdk.demo.resource.v1.DemoReconciler.isIgnoreReconcile;

@KubernetesDependent(labelSelector = MANAGED_BY_OPERATOR_SELECTOR)
public class DependentDeployment extends CRUDKubernetesDependentResource<Deployment, DemoResource> {

  private static final Logger log = Logger.getLogger(DependentDeployment.class);

  public DependentDeployment() {
    super(Deployment.class);
  }

  @Override
  public ReconcileResult<Deployment> reconcile(DemoResource primary, Context<DemoResource> context) {
    if (isIgnoreReconcile(primary)) {
      log.infov("Ignored reconcile for {0} in namespace {1}", primary.getMetadata().getName(), primary.getMetadata().getNamespace());
      return ReconcileResult.noOperation(null);
    }
    return super.reconcile(primary, context);
  }

  @Override
  protected Deployment desired(DemoResource primary, Context<DemoResource> context) {
    // @formatter:off
    return new DeploymentBuilder()
      .withNewMetadata()
        .withName(primary.getMetadata().getName())
        .withNamespace(primary.getMetadata().getNamespace())
        .withLabels(Map.of(LABEL_MANAGED_BY, LABEL_DEMO_OPERATOR,
          "app.kubernetes.io/name", primary.getMetadata().getName()))
      .endMetadata()
      .withNewSpec()
        .withReplicas(1)
        .withSelector(new LabelSelectorBuilder()
          .withMatchLabels(Map.of(
            LABEL_MANAGED_BY,
            LABEL_DEMO_OPERATOR,
            "app.kubernetes.io/name", primary.getMetadata().getName()))
          .build())
        .withNewTemplate()
          .withNewMetadata()
            .withName(primary.getMetadata().getName())
            .withNamespace(primary.getMetadata().getNamespace())
            .withLabels(Map.of(
              LABEL_MANAGED_BY, LABEL_DEMO_OPERATOR,
              "app.kubernetes.io/name", primary.getMetadata().getName()
            ))
          .endMetadata()
          .withNewSpec()
            .addNewContainer()
              .withPorts(List.of(new ContainerPortBuilder()
                .withName("service")
                .withContainerPort(8888)
                .build()))
              .withName(primary.getMetadata().getName())
              .withImage("josdk-demo/starter")
              .withImagePullPolicy("IfNotPresent")
              .withResources(new ResourceRequirementsBuilder()
                .withLimits(Map.of(
                  "cpu", new Quantity("250m"),
                  "memory", new Quantity("128M")
                ))
                .build())
              .withNewSecurityContext()
                .withRunAsUser(1000L)
                .withRunAsNonRoot(true)
                .withPrivileged(false)
                .withAllowPrivilegeEscalation(false)
                  .withNewCapabilities()
                  .withDrop("ALL")
                .endCapabilities()
                .withReadOnlyRootFilesystem(false)
                .withRunAsGroup(2000L)
              .endSecurityContext()
              .withVolumeMounts(new VolumeMountBuilder()
                .withName("config")
                .withMountPath("/mapped")
                .build())
            .endContainer()
            .withVolumes(
              new VolumeBuilder()
                .withName("config")
                .withConfigMap(new ConfigMapVolumeSourceBuilder()
                  .withName(primary.getMetadata().getName())
                  .build()).build()
            )
          .endSpec()
        .endTemplate()
      .endSpec()
      .build();
    // @formatter:on
  }
}
