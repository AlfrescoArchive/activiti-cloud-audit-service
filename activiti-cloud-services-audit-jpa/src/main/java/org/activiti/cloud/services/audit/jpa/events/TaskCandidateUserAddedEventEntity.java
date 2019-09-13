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

import org.activiti.api.task.model.TaskCandidateUser;
import org.activiti.api.task.model.impl.TaskCandidateUserImpl;
import org.activiti.cloud.api.task.model.events.CloudTaskCandidateUserAddedEvent;
import org.activiti.cloud.services.audit.jpa.converters.json.TaskCandidateUserJpaJsonConverter;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity(name = TaskCandidateUserAddedEventEntity.TASK_CANDIDATE_USER_ADDED_EVENT)
@DiscriminatorValue(value = TaskCandidateUserAddedEventEntity.TASK_CANDIDATE_USER_ADDED_EVENT)
public class TaskCandidateUserAddedEventEntity extends AuditEventEntity {

    protected static final String TASK_CANDIDATE_USER_ADDED_EVENT = "TaskCandidateUserAddedEvent";

    @Convert(converter = TaskCandidateUserJpaJsonConverter.class)
    @Lob
    @Column
    private TaskCandidateUserImpl candidateUser;
    
    public TaskCandidateUserAddedEventEntity() {
    }

    public TaskCandidateUserAddedEventEntity(CloudTaskCandidateUserAddedEvent cloudEvent) {
        super(cloudEvent);
        setCandidateUser(cloudEvent.getEntity());
    }
    
    public TaskCandidateUser getCandidateUser() {
        return candidateUser;
    }

    public void setCandidateUser(TaskCandidateUser candidateUser) {
        this.candidateUser = new TaskCandidateUserImpl(candidateUser.getUserId(),candidateUser.getTaskId());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TaskCandidateUserAddedEventEntity [candidateUser=")
               .append(candidateUser)
               .append(", toString()=")
               .append(super.toString())
               .append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(candidateUser);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TaskCandidateUserAddedEventEntity other = (TaskCandidateUserAddedEventEntity) obj;
        return Objects.equals(candidateUser, other.candidateUser);
    }
}
