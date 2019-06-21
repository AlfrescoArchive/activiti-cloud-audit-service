package org.activiti.cloud.services.audit.jpa.converters;

import org.activiti.api.process.model.events.BPMNTimerEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.model.shared.impl.events.CloudRuntimeEventImpl;
import org.activiti.cloud.api.process.model.events.CloudBPMNTimerCanceledEvent;
import org.activiti.cloud.api.process.model.impl.events.CloudBPMNTimerCanceledEventImpl;
import org.activiti.cloud.services.audit.jpa.events.AuditEventEntity;
import org.activiti.cloud.services.audit.jpa.events.TimerCanceledAuditEventEntity;

public class TimerCanceledEventConverter extends BaseEventToEntityConverter {

    public TimerCanceledEventConverter(EventContextInfoAppender eventContextInfoAppender) {
        super(eventContextInfoAppender);
    }

    @Override
    public String getSupportedEvent() {
        return BPMNTimerEvent.TimerEvents.JOB_CANCELED.name();
    }

    @Override
    protected TimerCanceledAuditEventEntity createEventEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudBPMNTimerCanceledEvent cloudEvent = (CloudBPMNTimerCanceledEvent) cloudRuntimeEvent;
        return new TimerCanceledAuditEventEntity(cloudEvent.getId(),
                                                 cloudEvent.getTimestamp(),
                                                 cloudEvent.getAppName(),
                                                 cloudEvent.getAppVersion(),
                                                 cloudEvent.getServiceFullName(),
                                                 cloudEvent.getServiceName(),
                                                 cloudEvent.getServiceType(),
                                                 cloudEvent.getServiceVersion(),
                                                 cloudEvent.getMessageId(),
                                                 cloudEvent.getSequenceNumber(),
                                                 cloudEvent.getEntity());
    }

    @Override
    protected CloudRuntimeEventImpl<?, ?> createAPIEvent(AuditEventEntity auditEventEntity) {
        TimerCanceledAuditEventEntity timerEventEntity = (TimerCanceledAuditEventEntity) auditEventEntity;

        CloudBPMNTimerCanceledEventImpl cloudEvent = new CloudBPMNTimerCanceledEventImpl(timerEventEntity.getEventId(),
                                                                                         timerEventEntity.getTimestamp(),
                                                                                         timerEventEntity.getTimer(),
                                                                                         timerEventEntity.getProcessDefinitionId(),
                                                                                         timerEventEntity.getProcessInstanceId());

        cloudEvent.setAppName(timerEventEntity.getAppName());
        cloudEvent.setAppVersion(timerEventEntity.getAppVersion());
        cloudEvent.setServiceFullName(timerEventEntity.getServiceFullName());
        cloudEvent.setServiceName(timerEventEntity.getServiceName());
        cloudEvent.setServiceType(timerEventEntity.getServiceType());
        cloudEvent.setServiceVersion(timerEventEntity.getServiceVersion());
        cloudEvent.setMessageId(timerEventEntity.getMessageId());
        cloudEvent.setSequenceNumber(timerEventEntity.getSequenceNumber());

        return cloudEvent;
    }
}
