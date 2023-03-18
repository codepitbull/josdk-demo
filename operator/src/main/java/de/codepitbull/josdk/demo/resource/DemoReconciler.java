package de.codepitbull.josdk.demo.resource;

import io.javaoperatorsdk.operator.api.reconciler.*;

@ControllerConfiguration()
public class DemoReconciler implements Reconciler<DemoResource>, ErrorStatusHandler<DemoResource> {

    @Override
    public ErrorStatusUpdateControl<DemoResource> updateErrorStatus(DemoResource resource, Context<DemoResource> context, Exception e) {
        return ErrorStatusUpdateControl.noStatusUpdate();
    }

    @Override
    public UpdateControl<DemoResource> reconcile(DemoResource resource, Context<DemoResource> context) throws Exception {
        return UpdateControl.noUpdate();
    }
}
