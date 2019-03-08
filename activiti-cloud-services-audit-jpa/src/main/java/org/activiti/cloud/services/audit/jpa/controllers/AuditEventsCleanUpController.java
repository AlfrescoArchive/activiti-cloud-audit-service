package org.activiti.cloud.services.audit.jpa.controllers;

import org.activiti.cloud.services.audit.api.assembler.EventResourceAssembler;
import org.activiti.cloud.services.audit.api.converters.APIEventToEntityConverters;
import org.activiti.cloud.services.audit.api.resources.EventResource;
import org.activiti.cloud.services.audit.api.resources.EventsRelProvider;
import org.activiti.cloud.services.audit.jpa.events.AuditEventEntity;
import org.activiti.cloud.services.audit.jpa.repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

@ConditionalOnProperty(name = "activiti.rest.enable-clean-up", havingValue = "true")
@RestController
@RequestMapping(
        value = "/admin/clean-up/v1/" + EventsRelProvider.COLLECTION_RESOURCE_REL,
        produces = {
                MediaTypes.HAL_JSON_VALUE,
                MediaType.APPLICATION_JSON_VALUE
        })
public class AuditEventsCleanUpController {

    private final EventsRepository eventsRepository;

    private final EventResourceAssembler eventResourceAssembler;

    private final APIEventToEntityConverters eventConverters;

    @Autowired
    public AuditEventsCleanUpController(EventsRepository eventsRepository,
                                          EventResourceAssembler eventResourceAssembler,
                                          APIEventToEntityConverters eventConverters) {
        this.eventsRepository = eventsRepository;
        this.eventResourceAssembler = eventResourceAssembler;
        this.eventConverters = eventConverters;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public Resources<EventResource> deleteEvents (){

        Collection<EventResource> result = new ArrayList<>();
        Iterable <AuditEventEntity> iterable = eventsRepository.findAll();

        for(AuditEventEntity entity : iterable){
            result.add(eventResourceAssembler.toResource(
                    eventConverters.getConverterByEventTypeName(entity.getEventType()).convertToAPI(entity)
            ));
        }

        eventsRepository.deleteAll(iterable);

        return new Resources<>(result);
    }


}
