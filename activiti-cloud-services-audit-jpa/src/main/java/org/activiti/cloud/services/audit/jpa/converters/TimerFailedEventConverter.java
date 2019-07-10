package org.activiti.cloud.services.audit.jpa.converters;

import org.activiti.api.process.model.events.BPMNTimerEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.model.shared.impl.events.CloudRuntimeEventImpl;
import org.activiti.cloud.api.process.model.events.CloudBPMNTimerFailedEvent;
import org.activiti.cloud.api.process.model.impl.events.CloudBPMNTimerFailedEventImpl;
import org.activiti.cloud.services.audit.jpa.events.AuditEventEntity;
import org.activiti.cloud.services.audit.jpa.events.TimerFailedAuditEventEntity;

public class TimerFailedEventConverter extends BaseEventToEntityConverter {

    public TimerFailedEventConverter(EventContextInfoAppender eventContextInfoAppender) {
        super(eventContextInfoAppender);
    }

    @Override
    public String getSupportedEvent() {
        return BPMNTimerEvent.TimerEvents.TIMER_FAILED.name();
    }

    @Override
    protected TimerFailedAuditEventEntity createEventEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudBPMNTimerFailedEvent cloudEvent = (CloudBPMNTimerFailedEvent) cloudRuntimeEvent;
        return new TimerFailedAuditEventEntity(cloudEvent.getId(),
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
        TimerFailedAuditEventEntity timerEventEntity = (TimerFailedAuditEventEntity) auditEventEntity;

        CloudBPMNTimerFailedEventImpl cloudEvent = new CloudBPMNTimerFailedEventImpl(timerEventEntity.getEventId(),
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
