package io.zeebe.exporter.cloud.events;

import io.zeebe.protocol.record.Record;

public class ZeebeCloudEvent {
    private Record record;

    public ZeebeCloudEvent() {
    }

    public ZeebeCloudEvent(Record record) {
        this.record = record;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }
}
