package de.codepitbull.josdk.demo.resource;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("de.codepitbull.josdk.demo")
@Version("v1")
public class DemoResource extends CustomResource<DemoSpec, DemoStatus> implements Namespaced {
}
