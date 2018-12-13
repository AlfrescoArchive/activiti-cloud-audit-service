package org.activiti.cloud.services.audit.jpa.converters;

import org.activiti.api.process.model.events.ProcessRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.process.model.events.CloudProcessUpdatedEvent;
import org.activiti.cloud.api.process.model.impl.events.CloudProcessUpdatedEventImpl;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.jpa.events.AuditEventEntity;
import org.activiti.cloud.services.audit.jpa.events.ProcessUpdatedAuditEventEntity;
import org.springframework.stereotype.Component;

@Component
public class ProcessUpdatedEventConverter implements EventToEntityConverter<AuditEventEntity> {

    @Override
    public String getSupportedEvent() {
        return ProcessRuntimeEvent.ProcessEvents.PROCESS_UPDATED.name();
    }

    @Override
    public AuditEventEntity convertToEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudProcessUpdatedEvent cloudProcessUpdated = (CloudProcessUpdatedEvent) cloudRuntimeEvent;
         
        ProcessUpdatedAuditEventEntity eventEntity = new ProcessUpdatedAuditEventEntity(cloudProcessUpdated.getId(),
                                                                                        cloudProcessUpdated.getTimestamp(),
                                                                                        cloudProcessUpdated.getAppName(),
                                                                                        cloudProcessUpdated.getAppVersion(),
                                                                                        cloudProcessUpdated.getServiceName(),
                                                                                        cloudProcessUpdated.getServiceFullName(),
                                                                                        cloudProcessUpdated.getServiceType(),
                                                                                        cloudProcessUpdated.getServiceVersion(),
                                                                                        cloudProcessUpdated.getEntity());
        eventEntity.setBaseProcessData(cloudProcessUpdated.getEntityId(),
                                       cloudProcessUpdated.getProcessInstanceId(),
                                       cloudProcessUpdated.getProcessDefinitionId(),
                                       cloudProcessUpdated.getProcessDefinitionKey(),
                                       cloudProcessUpdated.getBusinessKey(),
                                       cloudProcessUpdated.getParentProcessInstanceId());

        return eventEntity;
    }

    @Override
    public CloudRuntimeEvent convertToAPI(AuditEventEntity auditEventEntity) {
        ProcessUpdatedAuditEventEntity processUpdatedAuditEventEntity = (ProcessUpdatedAuditEventEntity) auditEventEntity;
        CloudProcessUpdatedEventImpl cloudProcessUpdatedEvent = new CloudProcessUpdatedEventImpl(processUpdatedAuditEventEntity.getEventId(),
                                                                                                 processUpdatedAuditEventEntity.getTimestamp(),
                                                                                                 processUpdatedAuditEventEntity.getProcessInstance());
        cloudProcessUpdatedEvent.setAppName(processUpdatedAuditEventEntity.getAppName());
        cloudProcessUpdatedEvent.setAppVersion(processUpdatedAuditEventEntity.getAppVersion());
        cloudProcessUpdatedEvent.setServiceFullName(processUpdatedAuditEventEntity.getServiceFullName());
        cloudProcessUpdatedEvent.setServiceName(processUpdatedAuditEventEntity.getServiceName());
        cloudProcessUpdatedEvent.setServiceType(processUpdatedAuditEventEntity.getServiceType());
        cloudProcessUpdatedEvent.setServiceVersion(processUpdatedAuditEventEntity.getServiceVersion());

        return cloudProcessUpdatedEvent;
    }
}
