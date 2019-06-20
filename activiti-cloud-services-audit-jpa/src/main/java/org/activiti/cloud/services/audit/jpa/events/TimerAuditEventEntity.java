package org.activiti.cloud.services.audit.jpa.events;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;

import org.activiti.api.process.model.BPMNTimer;
import org.activiti.cloud.services.audit.jpa.converters.json.TimerJpaJsonConverter;

@Entity
public abstract class TimerAuditEventEntity extends AuditEventEntity {

    @Convert(converter = TimerJpaJsonConverter.class)
    @Column(columnDefinition="text")
    private BPMNTimer timer;

    public TimerAuditEventEntity() {
    }

    public TimerAuditEventEntity(String eventId,
                                 Long timestamp,
                                 String eventType) {
        super(eventId,
              timestamp,
              eventType);
    }

    public TimerAuditEventEntity(String eventId,
                                 Long timestamp,
                                 String eventType,
                                 String appName,
                                 String appVersion,
                                 String serviceName,
                                 String serviceFullName,
                                 String serviceType,
                                 String serviceVersion,
                                 String messageId,
                                 Integer sequenceNumber,
                                 BPMNTimer timer) {
        super(eventId,
              timestamp,
              eventType);
        setAppName(appName);
        setAppVersion(appVersion);
        setServiceName(serviceName);
        setServiceFullName(serviceFullName);
        setServiceType(serviceType);
        setServiceVersion(serviceVersion);
        setMessageId(messageId);
        setSequenceNumber(sequenceNumber);
        if (timer != null) {
            setProcessDefinitionId(timer.getProcessDefinitionId());
            setProcessInstanceId(timer.getProcessInstanceId());
        }

        this.timer = timer;
        if (timer != null) {
            setEntityId(timer.getElementId());
        }

    }

    public BPMNTimer getTimer() {
        return timer;
    }

    public void setTimer(BPMNTimer timer) {
        this.timer = timer;
    }

 
}
