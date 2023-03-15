package de.codepitbull.josdk.demo.resource;

import io.javaoperatorsdk.operator.api.ObservedGenerationAwareStatus;

public class DemoStatus extends ObservedGenerationAwareStatus {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
