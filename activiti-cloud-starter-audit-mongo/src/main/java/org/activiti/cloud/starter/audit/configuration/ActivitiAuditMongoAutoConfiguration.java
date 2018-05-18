package org.activiti.cloud.starter.audit.configuration;

import org.activiti.cloud.services.audit.mongo.EventsRelProvider;
import org.activiti.cloud.services.audit.mongo.ProcessEngineEventsAdminController;
import org.activiti.cloud.services.audit.mongo.ProcessEngineEventsController;
import org.activiti.cloud.services.audit.mongo.assembler.EventResourceAssembler;
import org.activiti.cloud.services.audit.mongo.channel.AuditConsumerChannelHandler;
import org.activiti.cloud.services.audit.mongo.config.RepositoryConfig;
import org.activiti.cloud.services.audit.mongo.repository.EventsRepository;
import org.activiti.cloud.services.security.SecurityPoliciesApplicationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({RepositoryConfig.class, AuditConsumerChannelHandler.class,
        ProcessEngineEventsController.class,ProcessEngineEventsAdminController.class})
@ComponentScan(basePackageClasses = {EventsRepository.class})
public class ActivitiAuditMongoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SecurityPoliciesApplicationService securityPoliciesApplicationService(){
        return new SecurityPoliciesApplicationService();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventResourceAssembler eventResourceAssembler(){
        return new EventResourceAssembler();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventsRelProvider eventsRelProvider(){
        return new EventsRelProvider();
    }
}
