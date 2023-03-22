package de.codepitbull.josdk.demo.resource.v1;

import de.codepitbull.josdk.demo.resource.v1.dependent.DependentConfigMap;
import de.codepitbull.josdk.demo.resource.v1.dependent.DependentDeployment;
import de.codepitbull.josdk.demo.resource.v1.dependent.DependentService;
import io.javaoperatorsdk.operator.api.reconciler.*;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import org.jboss.logging.Logger;

import static de.codepitbull.josdk.demo.resource.v1.Constants.ANNOTATION_IGNORE_FOR_RECONCILE;

@ControllerConfiguration(dependents = {
  @Dependent(
    type = DependentDeployment.class
  ),
  @Dependent(
    type = DependentConfigMap.class
  ),
  @Dependent(
    type = DependentService.class
  )
})
public class DemoReconciler implements Reconciler<DemoResource>, ErrorStatusHandler<DemoResource> {

  private static final Logger log = Logger.getLogger(DemoReconciler.class);

  @Override
  public ErrorStatusUpdateControl<DemoResource> updateErrorStatus(DemoResource resource, Context<DemoResource> context, Exception e) {
    return ErrorStatusUpdateControl.noStatusUpdate();
  }

  @Override
  public UpdateControl<DemoResource> reconcile(DemoResource resource, Context<DemoResource> context) throws Exception {
    log.infov("Reconciling: {0}", resource.getMetadata().getName());
    return UpdateControl.noUpdate();
  }

  public static boolean isIgnoreReconcile(DemoResource resource) {
    return resource.getMetadata().getAnnotations() != null && "true".equals(resource.getMetadata().getAnnotations().get(ANNOTATION_IGNORE_FOR_RECONCILE));
  }
}
