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
public class DependentService extends CRUDKubernetesDependentResource<Service, DemoResource> {

  private static final Logger log = Logger.getLogger(DependentService.class);

  public DependentService() {
    super(Service.class);
  }

  @Override
  public ReconcileResult<Service> reconcile(DemoResource primary, Context<DemoResource> context) {
    if (isIgnoreReconcile(primary)) {
      log.infov("Ignored reconcile for {0} in namespace {1}", primary.getMetadata().getName(), primary.getMetadata().getNamespace());
      return ReconcileResult.noOperation(null);
    }
    return super.reconcile(primary, context);
  }

  @Override
  protected Service desired(DemoResource primary, Context<DemoResource> context) {
    // @formatter:off
    return new ServiceBuilder()
      .withNewMetadata()
        .withName(primary.getMetadata().getName())
        .withNamespace(primary.getMetadata().getNamespace())
        .withLabels(Map.of(LABEL_MANAGED_BY, LABEL_DEMO_OPERATOR,
          "app.kubernetes.io/name", primary.getMetadata().getName()))
      .endMetadata()
      .withNewSpec()
        .withSelector(Map.of("app.kubernetes.io/name", primary.getMetadata().getName()))
        .withPorts(new ServicePortBuilder()
          .withName(primary.getMetadata().getName())
          .withProtocol("TCP")
          .withPort(8888)
          .withTargetPort(new IntOrString("service"))
          .build()
        )
      .endSpec()
      .build();
    // @formatter:on
  }
}
