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

import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessStartedEventImpl;
import org.activiti.cloud.services.audit.jpa.events.AuditEventEntity;
import org.activiti.api.runtime.model.impl.ProcessInstanceImpl;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ProcessStartedEventConverterTest {
    
    @Test
    public void checkConvertToEntityProcessStartedEvent() {
        //given
        CloudProcessStartedEventImpl event = createProcessStartedEvent();
           
        //when
        AuditEventEntity auditEventEntity = convertToEntity(event);   
     
        //then
        assertThat(auditEventEntity).isNotNull();
        assertThat(auditEventEntity.getEntityId()).isEqualTo(event.getEntityId());
        assertThat(auditEventEntity.getProcessInstanceId()).isEqualTo(event.getProcessInstanceId());
        assertThat(auditEventEntity.getProcessDefinitionId()).isEqualTo(event.getProcessDefinitionId());
        assertThat(auditEventEntity.getProcessDefinitionKey()).isEqualTo(event.getProcessDefinitionKey());
        assertThat(auditEventEntity.getBusinessKey()).isEqualTo(event.getBusinessKey());
        assertThat(auditEventEntity.getParentProcessInstanceId()).isEqualTo(event.getParentProcessInstanceId());
    }
    
    @Test
    public void checkConvertToAPIProcessStartedEvent() {
        //given
        AuditEventEntity auditEventEntity = convertToEntity(createProcessStartedEvent());   
        
        //when
        CloudRuntimeEvent cloudEvent= convertToAPI(auditEventEntity);
        assertThat(cloudEvent).isNotNull();
        
        assertThat(auditEventEntity.getEntityId()).isEqualTo(cloudEvent.getEntityId());
        assertThat(auditEventEntity.getProcessInstanceId()).isEqualTo(cloudEvent.getProcessInstanceId());
        assertThat(auditEventEntity.getProcessDefinitionId()).isEqualTo(cloudEvent.getProcessDefinitionId());
        assertThat(auditEventEntity.getProcessDefinitionKey()).isEqualTo(cloudEvent.getProcessDefinitionKey());
        assertThat(auditEventEntity.getBusinessKey()).isEqualTo(cloudEvent.getBusinessKey());
        assertThat(auditEventEntity.getParentProcessInstanceId()).isEqualTo(cloudEvent.getParentProcessInstanceId());
        
    }
    
    public CloudProcessStartedEventImpl createProcessStartedEvent() {
        //given
        ProcessInstanceImpl processInstanceStarted = new ProcessInstanceImpl();
        processInstanceStarted.setId("processInstanceId");
        processInstanceStarted.setProcessDefinitionId("processDefinitionId");
        processInstanceStarted.setProcessDefinitionKey("processDefinitionKey");
        processInstanceStarted.setBusinessKey("businessKey");
        processInstanceStarted.setParentId("parentId");
               
        CloudProcessStartedEventImpl event = new CloudProcessStartedEventImpl("ProcessStartedEventId",
                                                                              System.currentTimeMillis(),
                                                                              processInstanceStarted);
        event.setEntityId("entityId");
        event.setProcessInstanceId(processInstanceStarted.getId());
        event.setProcessDefinitionId(processInstanceStarted.getProcessDefinitionId());
        event.setProcessDefinitionKey(processInstanceStarted.getProcessDefinitionKey());
        event.setBusinessKey(processInstanceStarted.getBusinessKey());
        event.setParentProcessInstanceId(processInstanceStarted.getParentId());
        event.setMessageId("message-id");
        event.setSequenceNumber(0);
        
        return event;
    }
    
    public AuditEventEntity convertToEntity(CloudRuntimeEvent cloudEvent) {
        ProcessStartedEventConverter eventConverter = new ProcessStartedEventConverter(new EventContextInfoAppender());
        return (AuditEventEntity)eventConverter.convertToEntity(cloudEvent);
    }
    
    public CloudRuntimeEvent convertToAPI(AuditEventEntity auditEventEntity) {
        ProcessStartedEventConverter eventConverter = new ProcessStartedEventConverter(new EventContextInfoAppender());
        return (CloudRuntimeEvent)eventConverter.convertToAPI(auditEventEntity);
    }
    
}