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

import org.activiti.api.process.model.BPMNActivity;
import org.activiti.api.process.model.events.BPMNActivityEvent;

@Entity
@DiscriminatorValue(value = ActivitySignaledAuditEventEntity.ACTIVITY_SIGNALED_EVENT)
public class ActivitySignaledAuditEventEntity extends BPMNActivityAuditEventEntity {

    protected static final String ACTIVITY_SIGNALED_EVENT = "ActivitySignaledEvent";

    public ActivitySignaledAuditEventEntity() {
    }

    public ActivitySignaledAuditEventEntity(String eventId,
                                           Long timestamp) {
        super(eventId,
              timestamp,
              BPMNActivityEvent.ActivityEvents.ACTIVITY_SIGNALED.name());
    }

    public ActivitySignaledAuditEventEntity(String eventId,
                                           Long timestamp,
                                           String appName,
                                           String appVersion,
                                           String serviceName,
                                           String serviceFullName,
                                           String serviceType,
                                           String serviceVersion,
                                           String messageId,
                                           Integer sequenceNumber,
                                           BPMNActivity bpmnActivity) {
        super(eventId,
              timestamp,
              BPMNActivityEvent.ActivityEvents.ACTIVITY_SIGNALED.name(),
              appName,
              appVersion,
              serviceName,
              serviceFullName,
              serviceType,
              serviceVersion,
              messageId,
              sequenceNumber,
              bpmnActivity);
    }
}
