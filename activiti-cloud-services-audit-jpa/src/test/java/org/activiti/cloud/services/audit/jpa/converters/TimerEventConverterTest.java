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

package org.activiti.cloud.services.audit.jpa.converters;

import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.model.payloads.TimerPayload;
import org.activiti.api.runtime.model.impl.BPMNTimerImpl;
import org.activiti.api.runtime.model.impl.ProcessInstanceImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudBPMNTimerFiredEventImpl;
import org.activiti.cloud.api.process.model.impl.events.CloudBPMNTimerScheduledEventImpl;
import org.activiti.cloud.services.audit.jpa.events.TimerFiredAuditEventEntity;
import org.activiti.cloud.services.audit.jpa.events.TimerScheduledAuditEventEntity;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TimerEventConverterTest {

    private TimerFiredEventConverter eventConverterTimerFired = new TimerFiredEventConverter(new EventContextInfoAppender());
    private TimerScheduledEventConverter eventConverterTimerScheduled = new TimerScheduledEventConverter(new EventContextInfoAppender());

    @Test
    public void checkConvertToEntityTimerFiredEvent() {
        //given
        CloudBPMNTimerFiredEventImpl event = createTimerFiredEvent();
           
        //when
        TimerFiredAuditEventEntity auditEventEntity = (TimerFiredAuditEventEntity) eventConverterTimerFired.convertToEntity(event);
     
        //then
        assertThat(auditEventEntity).isNotNull();
        assertThat(auditEventEntity.getEntityId()).isEqualTo(event.getEntityId());
        assertThat(auditEventEntity.getProcessInstanceId()).isEqualTo(event.getProcessInstanceId());
        assertThat(auditEventEntity.getProcessDefinitionId()).isEqualTo(event.getProcessDefinitionId());
        assertThat(auditEventEntity.getProcessDefinitionKey()).isEqualTo(event.getProcessDefinitionKey());
        assertThat(auditEventEntity.getBusinessKey()).isEqualTo(event.getBusinessKey());
        assertThat(auditEventEntity.getParentProcessInstanceId()).isEqualTo(event.getParentProcessInstanceId());
        assertThat(auditEventEntity.getTimer().getProcessInstanceId()).isEqualTo(event.getEntity().getProcessInstanceId());
        assertThat(auditEventEntity.getTimer().getProcessDefinitionId()).isEqualTo(event.getEntity().getProcessDefinitionId());
        assertThat(auditEventEntity.getTimer().getTimerPayload()).isEqualTo(event.getEntity().getTimerPayload());
    }
    
    @Test
    public void checkConvertToEntityTimerScheduledEvent() {
        //given
        CloudBPMNTimerScheduledEventImpl event = createTimerScheduledEvent();
           
        //when
        TimerScheduledAuditEventEntity auditEventEntity = (TimerScheduledAuditEventEntity) eventConverterTimerScheduled.convertToEntity(event);
     
        //then
        assertThat(auditEventEntity).isNotNull();
        assertThat(auditEventEntity.getEntityId()).isEqualTo(event.getEntityId());
        assertThat(auditEventEntity.getProcessInstanceId()).isEqualTo(event.getProcessInstanceId());
        assertThat(auditEventEntity.getProcessDefinitionId()).isEqualTo(event.getProcessDefinitionId());
        assertThat(auditEventEntity.getProcessDefinitionKey()).isEqualTo(event.getProcessDefinitionKey());
        assertThat(auditEventEntity.getBusinessKey()).isEqualTo(event.getBusinessKey());
        assertThat(auditEventEntity.getParentProcessInstanceId()).isEqualTo(event.getParentProcessInstanceId());
        assertThat(auditEventEntity.getTimer().getProcessInstanceId()).isEqualTo(event.getEntity().getProcessInstanceId());
        assertThat(auditEventEntity.getTimer().getProcessDefinitionId()).isEqualTo(event.getEntity().getProcessDefinitionId());
        assertThat(auditEventEntity.getTimer().getTimerPayload()).isEqualTo(event.getEntity().getTimerPayload());
    }
    
    @Test
    public void checkConvertToAPITimerFiredEvent() {
        //given
        TimerFiredAuditEventEntity auditEventEntity = (TimerFiredAuditEventEntity) eventConverterTimerFired.convertToEntity(createTimerFiredEvent());
        
        //when
        CloudBPMNTimerFiredEventImpl cloudEvent= (CloudBPMNTimerFiredEventImpl) eventConverterTimerFired.convertToAPI(auditEventEntity);
        assertThat(cloudEvent).isNotNull();
        assertThat(auditEventEntity.getEntityId()).isEqualTo(cloudEvent.getEntityId());
        assertThat(auditEventEntity.getProcessInstanceId()).isEqualTo(cloudEvent.getProcessInstanceId());
        assertThat(auditEventEntity.getProcessDefinitionId()).isEqualTo(cloudEvent.getProcessDefinitionId());
        assertThat(auditEventEntity.getProcessDefinitionKey()).isEqualTo(cloudEvent.getProcessDefinitionKey());
        assertThat(auditEventEntity.getBusinessKey()).isEqualTo(cloudEvent.getBusinessKey());
        assertThat(auditEventEntity.getParentProcessInstanceId()).isEqualTo(cloudEvent.getParentProcessInstanceId());
        assertThat(auditEventEntity.getTimer().getProcessInstanceId()).isEqualTo(cloudEvent.getEntity().getProcessInstanceId());
        assertThat(auditEventEntity.getTimer().getProcessDefinitionId()).isEqualTo(cloudEvent.getEntity().getProcessDefinitionId());
        assertThat(auditEventEntity.getTimer().getTimerPayload()).isEqualTo(cloudEvent.getEntity().getTimerPayload());       
    }
    
    @Test
    public void checkConvertToAPITimerScheduledEvent() {
        //given
        TimerScheduledAuditEventEntity auditEventEntity = (TimerScheduledAuditEventEntity) eventConverterTimerScheduled.convertToEntity(createTimerScheduledEvent());
        
        //when
        CloudBPMNTimerScheduledEventImpl cloudEvent= (CloudBPMNTimerScheduledEventImpl) eventConverterTimerScheduled.convertToAPI(auditEventEntity);
        assertThat(cloudEvent).isNotNull();
        assertThat(auditEventEntity.getEntityId()).isEqualTo(cloudEvent.getEntityId());
        assertThat(auditEventEntity.getProcessInstanceId()).isEqualTo(cloudEvent.getProcessInstanceId());
        assertThat(auditEventEntity.getProcessDefinitionId()).isEqualTo(cloudEvent.getProcessDefinitionId());
        assertThat(auditEventEntity.getProcessDefinitionKey()).isEqualTo(cloudEvent.getProcessDefinitionKey());
        assertThat(auditEventEntity.getBusinessKey()).isEqualTo(cloudEvent.getBusinessKey());
        assertThat(auditEventEntity.getParentProcessInstanceId()).isEqualTo(cloudEvent.getParentProcessInstanceId());
        assertThat(auditEventEntity.getTimer().getProcessInstanceId()).isEqualTo(cloudEvent.getEntity().getProcessInstanceId());
        assertThat(auditEventEntity.getTimer().getProcessDefinitionId()).isEqualTo(cloudEvent.getEntity().getProcessDefinitionId());
        assertThat(auditEventEntity.getTimer().getTimerPayload()).isEqualTo(cloudEvent.getEntity().getTimerPayload());       
    }
    
    private CloudBPMNTimerFiredEventImpl createTimerFiredEvent() {
        //given
        ProcessInstanceImpl processInstance = new ProcessInstanceImpl();
        processInstance.setId("processInstanceId");
        processInstance.setProcessDefinitionId("processDefinitionId");
        processInstance.setProcessDefinitionKey("processDefinitionKey");
        processInstance.setBusinessKey("businessKey");
        processInstance.setParentId("parentId");
            
        BPMNTimerImpl timer = createBPMNTimer(processInstance);

        CloudBPMNTimerFiredEventImpl event = new CloudBPMNTimerFiredEventImpl("eventId",
                                                                              System.currentTimeMillis(),
                                                                              timer,
                                                                              timer.getProcessDefinitionId(),
                                                                              timer.getProcessInstanceId());

        event.setEntityId("entityId");
        event.setProcessInstanceId(processInstance.getId());
        event.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        event.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
        event.setBusinessKey(processInstance.getBusinessKey());
        event.setParentProcessInstanceId(processInstance.getParentId());
        event.setMessageId("message-id");
        event.setSequenceNumber(0);
        
        return event;
    }
    
    private CloudBPMNTimerScheduledEventImpl createTimerScheduledEvent() {
        //given
        ProcessInstanceImpl processInstance = new ProcessInstanceImpl();
        processInstance.setId("processInstanceId");
        processInstance.setProcessDefinitionId("processDefinitionId");
        processInstance.setProcessDefinitionKey("processDefinitionKey");
        processInstance.setBusinessKey("businessKey");
        processInstance.setParentId("parentId");
            
        BPMNTimerImpl timer = createBPMNTimer(processInstance);

        CloudBPMNTimerScheduledEventImpl event = new CloudBPMNTimerScheduledEventImpl("eventId",
                                                                                      System.currentTimeMillis(),
                                                                                      timer,
                                                                                      timer.getProcessDefinitionId(),
                                                                                      timer.getProcessInstanceId());

        event.setEntityId("entityId");
        event.setProcessInstanceId(processInstance.getId());
        event.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        event.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
        event.setBusinessKey(processInstance.getBusinessKey());
        event.setParentProcessInstanceId(processInstance.getParentId());
        event.setMessageId("message-id");
        event.setSequenceNumber(0);
        
        return event;
    }
    
    private BPMNTimerImpl createBPMNTimer(ProcessInstanceImpl processInstance) {
        BPMNTimerImpl timer = new BPMNTimerImpl("entityId");
        timer.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        timer.setProcessInstanceId(processInstance.getId());
        timer.setTimerPayload(createTimerPayload());
        return timer;
    }
    
    private TimerPayload createTimerPayload() {
        return ProcessPayloadBuilder.timer()
                .withExecutionId("ExecutionId")
                .withIsExclusive(true)
                .withRetries(5)
                .withMaxIterations(2)
                .withJobHandlerType("jobHandlerType")
                .withJobHandlerConfiguration("jobHandlerConfiguration")
                .withTenantId("tetantId")
                .withJobType("jobType")
                .build();
    }
 
}