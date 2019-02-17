package org.activiti.cloud.services.audit.jpa.converters;

import org.activiti.api.process.model.events.BPMNActivityEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.model.shared.impl.events.CloudRuntimeEventImpl;
import org.activiti.cloud.api.process.model.events.CloudBPMNActivitySignaledEvent;
import org.activiti.cloud.api.process.model.impl.events.CloudBPMNActivitySignaledEventImpl;
import org.activiti.cloud.services.audit.jpa.events.ActivitySignaledAuditEventEntity;
import org.activiti.cloud.services.audit.jpa.events.AuditEventEntity;

public class ActivitySignaledEventConverter extends BaseEventToEntityConverter {

    public ActivitySignaledEventConverter(EventContextInfoAppender eventContextInfoAppender) {
        super(eventContextInfoAppender);
    }
    
    @Override
    public String getSupportedEvent() {
        return BPMNActivityEvent.ActivityEvents.ACTIVITY_SIGNALED.name();
    }

    @Override
    protected ActivitySignaledAuditEventEntity createEventEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudBPMNActivitySignaledEvent cloudActivitySignaledEvent = (CloudBPMNActivitySignaledEvent) cloudRuntimeEvent;
        return new ActivitySignaledAuditEventEntity(cloudActivitySignaledEvent.getId(),
                                                   cloudActivitySignaledEvent.getTimestamp(),
                                                   cloudActivitySignaledEvent.getAppName(),
                                                   cloudActivitySignaledEvent.getAppVersion(),
                                                   cloudActivitySignaledEvent.getServiceFullName(),
                                                   cloudActivitySignaledEvent.getServiceName(),
                                                   cloudActivitySignaledEvent.getServiceType(),
                                                   cloudActivitySignaledEvent.getServiceVersion(),
                                                   cloudActivitySignaledEvent.getMessageId(),
                                                   cloudActivitySignaledEvent.getSequenceNumber(),
                                                   cloudActivitySignaledEvent.getEntity());
               
    }

    @Override
    protected CloudRuntimeEventImpl<?, ?> createAPIEvent(AuditEventEntity auditEventEntity) {
        ActivitySignaledAuditEventEntity activitySignaledAuditEventEntity = (ActivitySignaledAuditEventEntity) auditEventEntity;

        CloudBPMNActivitySignaledEventImpl bpmnActivitySignaledEvent = new CloudBPMNActivitySignaledEventImpl(activitySignaledAuditEventEntity.getEventId(),
                                                                                                              activitySignaledAuditEventEntity.getTimestamp(),
                                                                                                              activitySignaledAuditEventEntity.getBpmnActivity(),
                                                                                                              activitySignaledAuditEventEntity.getProcessDefinitionId(),
                                                                                                              activitySignaledAuditEventEntity.getProcessInstanceId());
        bpmnActivitySignaledEvent.setAppName(activitySignaledAuditEventEntity.getAppName());
        bpmnActivitySignaledEvent.setAppVersion(activitySignaledAuditEventEntity.getAppVersion());
        bpmnActivitySignaledEvent.setServiceFullName(activitySignaledAuditEventEntity.getServiceFullName());
        bpmnActivitySignaledEvent.setServiceName(activitySignaledAuditEventEntity.getServiceName());
        bpmnActivitySignaledEvent.setServiceType(activitySignaledAuditEventEntity.getServiceType());
        bpmnActivitySignaledEvent.setServiceVersion(activitySignaledAuditEventEntity.getServiceVersion());
        bpmnActivitySignaledEvent.setMessageId(activitySignaledAuditEventEntity.getMessageId());
        bpmnActivitySignaledEvent.setSequenceNumber(activitySignaledAuditEventEntity.getSequenceNumber());

        return bpmnActivitySignaledEvent;
    }
}
