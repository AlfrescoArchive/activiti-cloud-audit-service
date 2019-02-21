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

package org.activiti.cloud.services.audit.jpa.events;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.activiti.api.process.model.BPMNSignal;
import org.activiti.api.process.model.events.BPMNSignalEvent;
import org.activiti.cloud.services.audit.jpa.converters.json.SignalJpaJsonConverter;

@Entity
@DiscriminatorValue(value = SignalReceivedAuditEventEntity.SIGNAL_RECEIVED_EVENT)
public class SignalReceivedAuditEventEntity extends AuditEventEntity {

    protected static final String SIGNAL_RECEIVED_EVENT = "SignalReceivedEvent";
    
    @Convert(converter = SignalJpaJsonConverter.class)
    @Lob
    @Column
    private BPMNSignal signal;

    public SignalReceivedAuditEventEntity() {
    }

    public SignalReceivedAuditEventEntity(String eventId,
                                          Long timestamp) {
        super(eventId,
              timestamp,
              BPMNSignalEvent.SignalEvents.SIGNAL_RECEIVED.name());
    }

    public SignalReceivedAuditEventEntity(String eventId,
                                          Long timestamp,
                                          String appName,
                                          String appVersion,
                                          String serviceName,
                                          String serviceFullName,
                                          String serviceType,
                                          String serviceVersion,
                                          String messageId,
                                          Integer sequenceNumber,
                                          BPMNSignal signal) {
    	 super(eventId,
               timestamp,
               BPMNSignalEvent.SignalEvents.SIGNAL_RECEIVED.name());
           setAppName(appName);
           setAppVersion(appVersion);
           setServiceName(serviceName);
           setServiceFullName(serviceFullName);
           setServiceType(serviceType);
           setServiceVersion(serviceVersion);
           setMessageId(messageId);
           setSequenceNumber(sequenceNumber);
           setSignal(signal) ;
    }
    
    public BPMNSignal getSignal() {
        return signal;
    }

    public void setSignal(BPMNSignal signal) {
        this.signal = signal;
    }
}
