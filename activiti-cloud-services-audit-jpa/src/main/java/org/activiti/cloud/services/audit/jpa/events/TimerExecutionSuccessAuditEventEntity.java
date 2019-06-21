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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.activiti.api.process.model.BPMNTimer;
import org.activiti.api.process.model.events.BPMNTimerEvent;

@Entity
@DiscriminatorValue(value = TimerExecutionSuccessAuditEventEntity.TIMER_EXECUTION_SUCCESS_EVENT)
public class TimerExecutionSuccessAuditEventEntity extends TimerAuditEventEntity {

    protected static final String TIMER_EXECUTION_SUCCESS_EVENT = "TimerExecutionSuccessEvent";
    
    public TimerExecutionSuccessAuditEventEntity() {
    }

    public TimerExecutionSuccessAuditEventEntity(String eventId,
                                      Long timestamp) {
        super(eventId,
              timestamp,
              BPMNTimerEvent.TimerEvents.JOB_EXECUTION_SUCCESS.name());
    }

    public TimerExecutionSuccessAuditEventEntity(String eventId,
                                          Long timestamp,
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
              BPMNTimerEvent.TimerEvents.JOB_EXECUTION_SUCCESS.name(),
              appName,
              appVersion,
              serviceName,
              serviceFullName,
              serviceType,
              serviceVersion,
              messageId,
              sequenceNumber,
              timer);
        
    }
    
}
