package org.activiti.cloud.services.audit.jpa.converters;

import org.activiti.api.process.model.events.SequenceFlowEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.process.model.events.CloudSequenceFlowTakenEvent;
import org.activiti.cloud.api.process.model.impl.events.CloudSequenceFlowTakenImpl;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.jpa.events.AuditEventEntity;
import org.activiti.cloud.services.audit.jpa.events.SequenceFlowAuditEventEntity;
import org.springframework.stereotype.Component;

@Component
public class SequenceFlowTakenEventConverter implements EventToEntityConverter<AuditEventEntity> {

    @Override
    public String getSupportedEvent() {
        return SequenceFlowEvent.SequenceFlowEvents.SEQUENCE_FLOW_TAKEN.name();
    }

    @Override
    public AuditEventEntity convertToEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudSequenceFlowTakenEvent cloudSequenceFlowTaken = (CloudSequenceFlowTakenEvent) cloudRuntimeEvent;

        SequenceFlowAuditEventEntity eventEntity = new SequenceFlowAuditEventEntity(cloudSequenceFlowTaken.getId(),
                                                                                    cloudSequenceFlowTaken.getTimestamp(),
                                                                                    cloudSequenceFlowTaken.getAppName(),
                                                                                    cloudSequenceFlowTaken.getAppVersion(),
                                                                                    cloudSequenceFlowTaken.getServiceName(),
                                                                                    cloudSequenceFlowTaken.getServiceFullName(),
                                                                                    cloudSequenceFlowTaken.getServiceType(),
                                                                                    cloudSequenceFlowTaken.getServiceVersion(),
                                                                                    cloudSequenceFlowTaken.getEntity());
        eventEntity.setBaseProcessData(cloudSequenceFlowTaken.getEntityId(),
                                       cloudSequenceFlowTaken.getProcessInstanceId(),
                                       cloudSequenceFlowTaken.getProcessDefinitionId(),
                                       cloudSequenceFlowTaken.getProcessDefinitionKey(),
                                       cloudSequenceFlowTaken.getBusinessKey(),
                                       cloudSequenceFlowTaken.getParentProcessInstanceId());

        return eventEntity;    
    }

    @Override
    public CloudRuntimeEvent convertToAPI(AuditEventEntity auditEventEntity) {
        SequenceFlowAuditEventEntity sequenceFlowTakenAuditEventEntity = (SequenceFlowAuditEventEntity) auditEventEntity;
        CloudSequenceFlowTakenImpl cloudSequenceFlowTakenEvent = new CloudSequenceFlowTakenImpl(sequenceFlowTakenAuditEventEntity.getEventId(),
                                                                                                sequenceFlowTakenAuditEventEntity.getTimestamp(),
                                                                                                sequenceFlowTakenAuditEventEntity.getSequenceFlow());
        cloudSequenceFlowTakenEvent.setAppName(sequenceFlowTakenAuditEventEntity.getAppName());
        cloudSequenceFlowTakenEvent.setAppVersion(sequenceFlowTakenAuditEventEntity.getAppVersion());
        cloudSequenceFlowTakenEvent.setServiceFullName(sequenceFlowTakenAuditEventEntity.getServiceFullName());
        cloudSequenceFlowTakenEvent.setServiceName(sequenceFlowTakenAuditEventEntity.getServiceName());
        cloudSequenceFlowTakenEvent.setServiceType(sequenceFlowTakenAuditEventEntity.getServiceType());
        cloudSequenceFlowTakenEvent.setServiceVersion(sequenceFlowTakenAuditEventEntity.getServiceVersion());
        
        cloudSequenceFlowTakenEvent.setEntityId(sequenceFlowTakenAuditEventEntity.getProcessInstanceId());
        cloudSequenceFlowTakenEvent.setProcessDefinitionId(sequenceFlowTakenAuditEventEntity.getProcessDefinitionId());
        cloudSequenceFlowTakenEvent.setProcessInstanceId(sequenceFlowTakenAuditEventEntity.getProcessInstanceId());

        return cloudSequenceFlowTakenEvent;
    }
}
