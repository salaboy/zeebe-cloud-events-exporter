package io.zeebe.exporter.cloud.events;

public class ExporterConfiguration {

    private String enabledValueTypes = "JOB,WORKFLOW_INSTANCE,DEPLOYMENT,INCIDENT";

    private String enabledRecordTypes = "EVENT";

    private boolean updatePosition = true;


    public ExporterConfiguration() {
    }

    public ExporterConfiguration(String enabledValueTypes, String enabledRecordTypes, boolean updatePosition) {
        this.enabledValueTypes = enabledValueTypes;
        this.enabledRecordTypes = enabledRecordTypes;
        this.updatePosition = updatePosition;
    }

    public String getEnabledValueTypes() {
        return enabledValueTypes;
    }

    public String getEnabledRecordTypes() {
        return enabledRecordTypes;
    }

    public boolean isUpdatePosition() {
        return updatePosition;
    }
}
