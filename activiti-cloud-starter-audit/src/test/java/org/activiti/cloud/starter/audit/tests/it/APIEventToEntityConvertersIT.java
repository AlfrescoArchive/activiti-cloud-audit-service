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

import static org.assertj.core.api.Assertions.assertThat;

import org.activiti.cloud.api.model.shared.impl.events.CloudVariableCreatedEventImpl;
import org.activiti.cloud.api.model.shared.impl.events.CloudVariableDeletedEventImpl;
import org.activiti.cloud.api.model.shared.impl.events.CloudVariableUpdatedEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudBPMNActivityCancelledEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudBPMNActivityCompletedEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudBPMNActivityStartedEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudBPMNSignalReceivedEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessCancelledEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessCompletedEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessCreatedEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessDeployedEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessResumedEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessStartedEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessSuspendedEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessUpdatedEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudSequenceFlowTakenImpl;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskAssignedEventImpl;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskCancelledEventImpl;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskCandidateGroupAddedEventImpl;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskCandidateGroupRemovedEventImpl;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskCandidateUserAddedEventImpl;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskCandidateUserRemovedEventImpl;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskCompletedEventImpl;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskCreatedEventImpl;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskSuspendedEventImpl;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskUpdatedEventImpl;
import org.activiti.cloud.services.audit.api.converters.APIEventToEntityConverters;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.jpa.converters.ActivityCancelledEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.ActivityCompletedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.ActivityStartedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.ProcessCancelledEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.ProcessCompletedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.ProcessCreatedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.ProcessDeployedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.ProcessResumedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.ProcessStartedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.ProcessSuspendedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.ProcessUpdatedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.SequenceFlowTakenEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.SignalReceivedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.TaskAssignedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.TaskCancelledEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.TaskCandidateGroupAddedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.TaskCandidateGroupRemovedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.TaskCandidateUserAddedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.TaskCandidateUserRemovedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.TaskCompletedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.TaskCreatedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.TaskSuspendedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.TaskUpdatedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.VariableCreatedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.VariableDeletedEventConverter;
import org.activiti.cloud.services.audit.jpa.converters.VariableUpdatedEventConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@TestPropertySource("classpath:application.properties")
public class APIEventToEntityConvertersIT {

    @Autowired
    private EventsRestTemplate eventsRestTemplate;
    
    @Autowired
    private APIEventToEntityConverters eventConverters;

    @Test
    public void checkConverterForEvents() {
        
        EventToEntityConverter converter;
        
        converter = eventConverters.getConverterByEventTypeName(new CloudBPMNActivityCancelledEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(ActivityCancelledEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudBPMNActivityCompletedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(ActivityCompletedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudBPMNActivityStartedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(ActivityStartedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudProcessCancelledEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(ProcessCancelledEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudProcessCompletedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(ProcessCompletedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudProcessCreatedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(ProcessCreatedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudProcessDeployedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(ProcessDeployedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudProcessResumedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(ProcessResumedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudProcessStartedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(ProcessStartedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudProcessSuspendedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(ProcessSuspendedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudProcessUpdatedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(ProcessUpdatedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudBPMNSignalReceivedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(SignalReceivedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudSequenceFlowTakenImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(SequenceFlowTakenEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudTaskAssignedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(TaskAssignedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudTaskCancelledEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(TaskCancelledEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudTaskCandidateGroupAddedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(TaskCandidateGroupAddedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudTaskCandidateGroupRemovedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(TaskCandidateGroupRemovedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudTaskCandidateUserAddedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(TaskCandidateUserAddedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudTaskCandidateUserRemovedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(TaskCandidateUserRemovedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudTaskCompletedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(TaskCompletedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudTaskCreatedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(TaskCreatedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudTaskSuspendedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(TaskSuspendedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudTaskUpdatedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(TaskUpdatedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudVariableCreatedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(VariableCreatedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudVariableDeletedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(VariableDeletedEventConverter.class);
        
        converter = eventConverters.getConverterByEventTypeName(new CloudVariableUpdatedEventImpl().getEventType().name());
        assertThat(converter).isNotNull().isInstanceOf(VariableUpdatedEventConverter.class);
        
    }
 
    
}
