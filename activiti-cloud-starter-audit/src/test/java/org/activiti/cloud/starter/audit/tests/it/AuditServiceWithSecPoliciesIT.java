/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.cloud.starter.audit.tests.it;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.api.process.model.events.BPMNActivityEvent;
import org.activiti.api.runtime.model.impl.BPMNActivityImpl;
import org.activiti.api.runtime.model.impl.ProcessDefinitionImpl;
import org.activiti.api.runtime.model.impl.ProcessInstanceImpl;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.events.TaskRuntimeEvent;
import org.activiti.api.task.model.impl.TaskImpl;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.model.shared.impl.conf.IgnoredRuntimeEvent;
import org.activiti.cloud.api.model.shared.impl.events.CloudRuntimeEventImpl;
import org.activiti.cloud.api.process.model.events.CloudBPMNActivityEvent;
import org.activiti.cloud.api.process.model.events.CloudBPMNActivityStartedEvent;
import org.activiti.cloud.api.process.model.impl.events.CloudBPMNActivityCancelledEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudBPMNActivityCompletedEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudBPMNActivityStartedEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessCancelledEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessCompletedEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessDeployedEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessStartedEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessSuspendedEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessUpdatedEventImpl;
import org.activiti.cloud.api.task.model.events.CloudTaskAssignedEvent;
import org.activiti.cloud.api.task.model.events.CloudTaskCancelledEvent;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskAssignedEventImpl;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskCancelledEventImpl;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskCompletedEventImpl;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskCreatedEventImpl;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskUpdatedEventImpl;
import org.activiti.cloud.services.audit.api.converters.APIEventToEntityConverters;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.jpa.events.AuditEventEntity;
import org.activiti.cloud.services.audit.jpa.repository.EventsRepository;
import org.activiti.cloud.starters.test.MyProducer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@TestPropertySource({"classpath:application.properties", "classpath:access-control.properties"})
public class AuditServiceWithSecPoliciesIT {

    @Autowired
    private EventsRestTemplate eventsRestTemplate;

    @Autowired
    private EventsRepository repository;

    @Autowired
    private MyProducer producer;
    
    @Autowired
    private APIEventToEntityConverters eventConverters;

    @Before
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void shouldGetEventsForACancelledTask() {
        //given
        List<CloudRuntimeEvent> coveredEvents = getTaskCancelledEvents();
        producer.send(coveredEvents.toArray(new CloudRuntimeEvent[coveredEvents.size()]));

        await().untilAsserted(() -> {

            //when
            Map<String, Object> filters = new HashMap<>();
            filters.put("entityId",
                        "1234-abc-5678-def");

            ResponseEntity<PagedResources<CloudRuntimeEvent>> eventsPagedResources = eventsRestTemplate.executeFind(filters);

            //then
            Collection<CloudRuntimeEvent> retrievedEvents = eventsPagedResources.getBody().getContent();
            assertThat(retrievedEvents).hasSize(3);
            for (CloudRuntimeEvent e : retrievedEvents) {
                assertThat(e.getEntityId()).isEqualTo("1234-abc-5678-def");
            }
        });
    }


    @Test
    public void shouldBeAbleToFilterOnEventTypeTaskCancelled() {
        //given
        List<CloudRuntimeEvent> coveredEvents = getTaskCancelledEvents();
        producer.send(coveredEvents.toArray(new CloudRuntimeEvent[coveredEvents.size()]));

        await().untilAsserted(() -> {

            //when
            ResponseEntity<PagedResources<CloudRuntimeEvent>> eventsPagedResources = eventsRestTemplate.executeFind(Collections.singletonMap("eventType",
                                                                                                                                             TaskRuntimeEvent.TaskEvents.TASK_CANCELLED.name()));

            //then
            Collection<CloudRuntimeEvent> retrievedEvents = eventsPagedResources.getBody().getContent();
            assertThat(retrievedEvents).hasSize(1);
            CloudTaskCancelledEvent cloudBPMNTaskCancelled = (CloudTaskCancelledEvent) retrievedEvents.iterator().next();
            assertThat(cloudBPMNTaskCancelled.getEventType()).isEqualTo(TaskRuntimeEvent.TaskEvents.TASK_CANCELLED);
            assertThat(cloudBPMNTaskCancelled.getEntityId()).isEqualTo("1234-abc-5678-def");
            assertThat(cloudBPMNTaskCancelled.getEntity()).isInstanceOf(Task.class);
            assertThat(cloudBPMNTaskCancelled.getEntity().getId()).isEqualTo("1234-abc-5678-def");
        });
    }

    
    private List<CloudRuntimeEvent> getTaskCancelledEvents() {
        List<CloudRuntimeEvent> testEvents = new ArrayList<>();
        TaskImpl taskCreated = new TaskImpl("1234-abc-5678-def",
                                            "my task",
                                            Task.TaskStatus.CREATED);

        CloudTaskCreatedEventImpl cloudTaskCreatedEvent = new CloudTaskCreatedEventImpl("TaskCreatedEventId",
                                                                                        System.currentTimeMillis(),
                                                                                        taskCreated);
        cloudTaskCreatedEvent.setServiceName("test-app");

        testEvents.add(cloudTaskCreatedEvent);

        TaskImpl taskAssigned = new TaskImpl("1234-abc-5678-def",
                                             "my task",
                                             Task.TaskStatus.ASSIGNED);

        CloudTaskAssignedEventImpl cloudTaskAssignedEvent = new CloudTaskAssignedEventImpl("TaskAssignedEventId",
                                                                                           System.currentTimeMillis(),
                                                                                           taskAssigned);
        cloudTaskAssignedEvent.setServiceName("test-app");
        testEvents.add(cloudTaskAssignedEvent);

        TaskImpl taskCancelled = new TaskImpl("1234-abc-5678-def",
                                              "my task",
                                              Task.TaskStatus.CANCELLED);

        CloudTaskCancelledEventImpl cloudTaskCancelledEvent = new CloudTaskCancelledEventImpl("TaskCancelledEventId",
                                                                                              System.currentTimeMillis(),
                                                                                              taskCancelled);
        cloudTaskCancelledEvent.setServiceName("test-app");
        testEvents.add(cloudTaskCancelledEvent);
        return testEvents;
    }
}
