package io.zeebe.exporter.cloud.events;

import static org.assertj.core.api.Assertions.assertThat;


import io.zeebe.client.ZeebeClient;
import io.zeebe.model.bpmn.Bpmn;
import io.zeebe.model.bpmn.BpmnModelInstance;
import io.zeebe.protocol.record.ValueType;
import io.zeebe.test.ZeebeTestRule;
import io.zeebe.test.util.TestUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ExporterTest {

    private static final BpmnModelInstance WORKFLOW =
            Bpmn.createExecutableProcess("process")
                    .startEvent("start")
                    .sequenceFlowId("to-task")
                    .serviceTask("task", s -> s.zeebeTaskType("test"))
                    .sequenceFlowId("to-end")
                    .endEvent("end")
                    .done();

    private static final ExporterConfiguration CONFIGURATION = new ExporterConfiguration();

    @Rule
    public final ZeebeTestRule testRule = new ZeebeTestRule("zeebe.test.cfg.toml", Properties::new);

    private ZeebeClient client;

    @Before
    public void init() {
        client = testRule.getClient();

    }

    @After
    public void cleanUp() {

    }

    @Test
    public void shouldExportEventsAsProtobuf() throws Exception {
        final List<byte[]> messages = new ArrayList<>();

//        final ITopic<byte[]> topic = hz.getTopic(CONFIGURATION.getTopicName(ValueType.DEPLOYMENT));
//        topic.addMessageListener(m -> messages.add(m.getMessageObject()));
// Listen to stream messages

        client.newDeployCommand().addWorkflowModel(WORKFLOW, "process.bpmn").send().join();
// see the events in the streams

        //TestUtil.waitUntil(() -> messages.size() > 0);

//        byte[] message = messages.get(0);


//        final Schema.DeploymentRecord deploymentRecord = Schema.DeploymentRecord.parseFrom(message);
//        final Schema.DeploymentRecord.Resource resource = deploymentRecord.getResources(0);
//        assertThat(resource.getResourceName()).isEqualTo("process.bpmn");
    }
}