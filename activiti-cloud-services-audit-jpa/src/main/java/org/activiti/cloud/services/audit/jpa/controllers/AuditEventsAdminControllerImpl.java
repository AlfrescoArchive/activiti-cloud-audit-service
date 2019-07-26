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

package org.activiti.cloud.services.audit.jpa.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.activiti.api.runtime.shared.NotFoundException;
import org.activiti.cloud.alfresco.data.domain.AlfrescoPagedResourcesAssembler;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.services.audit.api.assembler.EventResourceAssembler;
import org.activiti.cloud.services.audit.api.controllers.AuditEventsAdminController;
import org.activiti.cloud.services.audit.api.converters.APIEventToEntityConverters;
import org.activiti.cloud.services.audit.api.resources.EventsRelProvider;
import org.activiti.cloud.services.audit.jpa.events.AuditEventEntity;
import org.activiti.cloud.services.audit.jpa.repository.EventsRepository;
import org.activiti.cloud.services.audit.jpa.security.SecurityPoliciesApplicationServiceImpl;
import org.activiti.core.common.spring.security.policies.ActivitiForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/v1/" + EventsRelProvider.COLLECTION_RESOURCE_REL, produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class AuditEventsAdminControllerImpl implements AuditEventsAdminController {

    private final EventsRepository eventsRepository;

    private final EventResourceAssembler eventResourceAssembler;

    private final AlfrescoPagedResourcesAssembler<CloudRuntimeEvent> pagedResourcesAssembler;

    private final APIEventToEntityConverters eventConverters;

    private SecurityPoliciesApplicationServiceImpl securityPoliciesApplicationService;

    @Autowired
    public AuditEventsAdminControllerImpl(EventsRepository eventsRepository,
                                          EventResourceAssembler eventResourceAssembler,
                                          APIEventToEntityConverters eventConverters,
                                          AlfrescoPagedResourcesAssembler<CloudRuntimeEvent> pagedResourcesAssembler,
                                          SecurityPoliciesApplicationServiceImpl securityPoliciesApplicationService
                                          ) {
        this.eventsRepository = eventsRepository;
        this.eventResourceAssembler = eventResourceAssembler;
        this.eventConverters = eventConverters;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.securityPoliciesApplicationService = securityPoliciesApplicationService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public PagedResources<Resource<CloudRuntimeEvent>> findAll(Pageable pageable) {
        Page<AuditEventEntity> allAuditInPage = eventsRepository.findAll(pageable);

        List<CloudRuntimeEvent> events = new ArrayList<>();

        for (AuditEventEntity aee : allAuditInPage.getContent()) {
            events.add(eventConverters.getConverterByEventTypeName(aee.getEventType()).convertToAPI(aee));
        }

        return pagedResourcesAssembler.toResource(pageable,
                                                  new PageImpl<>(events,
                                                                 pageable,
                                                                 allAuditInPage.getTotalElements()),
                                                  eventResourceAssembler);
    }

    @RequestMapping(value = "/{eventId}", method = RequestMethod.GET)
    public Resource<CloudRuntimeEvent> findById(@PathVariable String eventId) {
        Optional<AuditEventEntity> findResult = eventsRepository.findByEventId(eventId);
        if (!findResult.isPresent()) {
            throw new NotFoundException("Unable to find event for the given id:'" + eventId + "'");
        }
        AuditEventEntity auditEventEntity = findResult.get();
        if (!securityPoliciesApplicationService.canRead(auditEventEntity.getProcessDefinitionId(),
                                                        auditEventEntity.getServiceFullName())) {
            throw new ActivitiForbiddenException("Operation not permitted for " + auditEventEntity.getProcessDefinitionId());
        }

        CloudRuntimeEvent cloudRuntimeEvent = eventConverters.getConverterByEventTypeName(auditEventEntity.getEventType()).convertToAPI(auditEventEntity);
        return eventResourceAssembler.toResource(cloudRuntimeEvent);
    }
}