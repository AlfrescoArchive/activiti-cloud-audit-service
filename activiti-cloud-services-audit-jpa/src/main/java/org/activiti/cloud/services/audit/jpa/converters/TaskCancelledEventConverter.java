package org.activiti.cloud.services.audit.jpa.converters;

import org.activiti.api.task.model.events.TaskRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.task.model.events.CloudTaskCancelledEvent;
import org.activiti.cloud.api.task.model.impl.events.CloudTaskCancelledEventImpl;
import org.activiti.cloud.services.audit.api.converters.EventToEntityConverter;
import org.activiti.cloud.services.audit.jpa.events.AuditEventEntity;
import org.activiti.cloud.services.audit.jpa.events.TaskCancelledEventEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskCancelledEventConverter implements EventToEntityConverter<AuditEventEntity> {

    @Override
    public String getSupportedEvent() {
        return TaskRuntimeEvent.TaskEvents.TASK_CANCELLED.name();
    }

    @Override
    public AuditEventEntity convertToEntity(CloudRuntimeEvent cloudRuntimeEvent) {
        CloudTaskCancelledEvent cloudTaskCancelledEvent = (CloudTaskCancelledEvent) cloudRuntimeEvent;
        TaskCancelledEventEntity eventEntity = new TaskCancelledEventEntity(cloudTaskCancelledEvent.getId(),
                                                                            cloudTaskCancelledEvent.getTimestamp(),
                                                                            cloudTaskCancelledEvent.getAppName(),
                                                                            cloudTaskCancelledEvent.getAppVersion(),
                                                                            cloudTaskCancelledEvent.getServiceName(),
                                                                            cloudTaskCancelledEvent.getServiceFullName(),
                                                                            cloudTaskCancelledEvent.getServiceType(),
                                                                            cloudTaskCancelledEvent.getServiceVersion(),
                                                                            cloudTaskCancelledEvent.getEntity(),
                                                                            cloudTaskCancelledEvent.getCause());
        
        eventEntity.setBaseProcessData(cloudTaskCancelledEvent.getEntityId(),
                                       cloudTaskCancelledEvent.getProcessInstanceId(),
                                       cloudTaskCancelledEvent.getProcessDefinitionId(),
                                       cloudTaskCancelledEvent.getProcessDefinitionKey(),
                                       cloudTaskCancelledEvent.getBusinessKey(),
                                       cloudTaskCancelledEvent.getParentProcessInstanceId());
        
        return eventEntity;
    }

    @Override
    public CloudRuntimeEvent convertToAPI(AuditEventEntity auditEventEntity) {
        TaskCancelledEventEntity taskCancelledEventEntity = (TaskCancelledEventEntity) auditEventEntity;

        CloudTaskCancelledEventImpl cloudTaskCancelledEvent = new CloudTaskCancelledEventImpl(taskCancelledEventEntity.getEventId(),
                                                                                              taskCancelledEventEntity.getTimestamp(),
                                                                                              taskCancelledEventEntity.getTask());
        cloudTaskCancelledEvent.setAppName(taskCancelledEventEntity.getAppName());
        cloudTaskCancelledEvent.setAppVersion(taskCancelledEventEntity.getAppVersion());
        cloudTaskCancelledEvent.setServiceFullName(taskCancelledEventEntity.getServiceFullName());
        cloudTaskCancelledEvent.setServiceName(taskCancelledEventEntity.getServiceName());
        cloudTaskCancelledEvent.setServiceType(taskCancelledEventEntity.getServiceType());
        cloudTaskCancelledEvent.setServiceVersion(taskCancelledEventEntity.getServiceVersion());
        cloudTaskCancelledEvent.setCause(taskCancelledEventEntity.getCause());

        return cloudTaskCancelledEvent;
    }
}
