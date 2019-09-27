package io.zeebe.exporter.cloud.events;


import io.cloudevents.json.Json;
import io.cloudevents.v03.CloudEventBuilder;
import io.cloudevents.v03.CloudEventImpl;
import io.zeebe.exporter.api.Exporter;
import io.zeebe.exporter.api.context.Context;
import io.zeebe.exporter.api.context.Controller;
import io.zeebe.protocol.record.Record;
import io.zeebe.protocol.record.RecordType;
import io.zeebe.protocol.record.ValueType;
import org.slf4j.Logger;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CloudEventsExporter implements Exporter {

    private final List<ValueType> enabledValueTypes = new ArrayList<>();

    private ExporterConfiguration config;
    private Logger logger;
    private Controller controller;

    public void configure(Context context) throws Exception {
        logger = context.getLogger();
        config = context.getConfiguration().instantiate(ExporterConfiguration.class);

        logger.debug("Starting exporter with configuration: {}", config);

        final List<RecordType> enabledRecordTypes =
                parseList(config.getEnabledRecordTypes()).map(RecordType::valueOf).collect(Collectors.toList());

        parseList(config.getEnabledValueTypes()).map(ValueType::valueOf).forEach(enabledValueTypes::add);

        context.setFilter(
                new Context.RecordFilter() {

                    @Override
                    public boolean acceptType(RecordType recordType) {
                        return enabledRecordTypes.contains(recordType);
                    }

                    @Override
                    public boolean acceptValue(ValueType valueType) {
                        return enabledValueTypes.contains(valueType);
                    }
                });

        if (!config.isUpdatePosition()) {
            logger.warn(
                    "The exporter is configured to not update its position. "
                            + "Because of this, the broker can't delete data and may run out of disk space!");
        }

    }

    private Stream<String> parseList(String list) {
        return Arrays.stream(list.split(",")).map(String::trim).map(String::toUpperCase);
    }

    public void open(Controller controller) {
        this.controller = controller;
        //Initialize Streams
    }

    public void close() {
        //shutdown streams
    }

    public void export(Record record) {

        // Define where to send the record

        final String eventId = UUID.randomUUID().toString();
        final URI src = URI.create("/trigger");
        final String eventType = "My.Cloud.Event.Type";

        //Transform to cloud event

        final CloudEventImpl<ZeebeCloudEvent> cloudEvent = CloudEventBuilder.<ZeebeCloudEvent>builder()
                .withType(eventType)
                .withId(eventId)
                .withSource(src)
                .withData(new ZeebeCloudEvent(record))
                .build();

        final String json = Json.encode(cloudEvent);
        System.out.println("> Cloud Event: " + json);
        //push


    }
}
