package org.activiti.cloud.services.audit.jpa.converters;

import org.activiti.api.process.model.events.ProcessRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.process.model.events.CloudProcessResumedEvent;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessResumedEventImpl;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.jpa.events.AuditEventEntity;
import org.activiti.cloud.services.audit.jpa.events.ProcessResumedAuditEventEntity;
import org.springframework.stereotype.Component;

@Component
public class ProcessResumedEventConverter implements EventToEntityConverter<AuditEventEntity> {

    @Override
    public String getSupportedEvent() {
        return ProcessRuntimeEvent.ProcessEvents.PROCESS_RESUMED.name();
    }

    @Override
    public AuditEventEntity convertToEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudProcessResumedEvent cloudProcessResumed = (CloudProcessResumedEvent) cloudRuntimeEvent;
       
        ProcessResumedAuditEventEntity eventEntity = new ProcessResumedAuditEventEntity(cloudProcessResumed.getId(),
                                                                                        cloudProcessResumed.getTimestamp(),
                                                                                        cloudProcessResumed.getAppName(),
                                                                                        cloudProcessResumed.getAppVersion(),
                                                                                        cloudProcessResumed.getServiceName(),
                                                                                        cloudProcessResumed.getServiceFullName(),
                                                                                        cloudProcessResumed.getServiceType(),
                                                                                        cloudProcessResumed.getServiceVersion(),
                                                                                        cloudProcessResumed.getEntity());
        
        eventEntity.setBaseProcessData(cloudProcessResumed.getEntityId(),
                                       cloudProcessResumed.getProcessInstanceId(),
                                       cloudProcessResumed.getProcessDefinitionId(),
                                       cloudProcessResumed.getProcessDefinitionKey(),
                                       cloudProcessResumed.getBusinessKey(),
                                       cloudProcessResumed.getParentProcessInstanceId());
        

        return eventEntity;
    }

    @Override
    public CloudRuntimeEvent convertToAPI(AuditEventEntity auditEventEntity) {
        ProcessResumedAuditEventEntity processResumedAuditEventEntity = (ProcessResumedAuditEventEntity) auditEventEntity;
        CloudProcessResumedEventImpl cloudProcessResumedEvent = new CloudProcessResumedEventImpl(processResumedAuditEventEntity.getEventId(),
                                                                                                 processResumedAuditEventEntity.getTimestamp(),
                                                                                                 processResumedAuditEventEntity.getProcessInstance());
        cloudProcessResumedEvent.setAppName(processResumedAuditEventEntity.getAppName());
        cloudProcessResumedEvent.setAppVersion(processResumedAuditEventEntity.getAppVersion());
        cloudProcessResumedEvent.setServiceFullName(processResumedAuditEventEntity.getServiceFullName());
        cloudProcessResumedEvent.setServiceName(processResumedAuditEventEntity.getServiceName());
        cloudProcessResumedEvent.setServiceType(processResumedAuditEventEntity.getServiceType());
        cloudProcessResumedEvent.setServiceVersion(processResumedAuditEventEntity.getServiceVersion());

        return cloudProcessResumedEvent;
    }
}
