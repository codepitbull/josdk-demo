package de.codepitbull.josdk.demo.resource.v1;

public class Constants {
  public static final String LABEL_MANAGED_BY = "app.kubernetes.io/managed-by";
  public static final String LABEL_DEMO_OPERATOR = "demo-operator";
  public static final String MANAGED_BY_OPERATOR_SELECTOR = LABEL_MANAGED_BY + "=" + LABEL_DEMO_OPERATOR;
  public static final String ANNOTATION_IGNORE_FOR_RECONCILE = "demo/ignore-for-reconcile";

  private Constants() {
    throw new RuntimeException("Go away, this class shouldn't be instantiated");
  }
}
