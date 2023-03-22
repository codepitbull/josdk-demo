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
public class DependentConfigMap extends CRUDKubernetesDependentResource<ConfigMap, DemoResource> {

  private static final Logger log = Logger.getLogger(DependentConfigMap.class);

  public DependentConfigMap() {
    super(ConfigMap.class);
  }

  @Override
  public ReconcileResult<ConfigMap> reconcile(DemoResource primary, Context<DemoResource> context) {
    if (isIgnoreReconcile(primary)) {
      log.infov("Ignored reconcile for {0} in namespace {1}", primary.getMetadata().getName(), primary.getMetadata().getNamespace());
      return ReconcileResult.noOperation(null);
    }
    return super.reconcile(primary, context);
  }

  @Override
  protected ConfigMap desired(DemoResource primary, Context<DemoResource> context) {
    // @formatter:off
    return new ConfigMapBuilder()
      .withNewMetadata()
        .withName(primary.getMetadata().getName())
        .withNamespace(primary.getMetadata().getNamespace())
        .withLabels(Map.of(LABEL_MANAGED_BY, LABEL_DEMO_OPERATOR))
      .endMetadata()
      .withData(Map.of("message",primary.getSpec().getDescription()))
      .build();
    // @formatter:on
  }
}
