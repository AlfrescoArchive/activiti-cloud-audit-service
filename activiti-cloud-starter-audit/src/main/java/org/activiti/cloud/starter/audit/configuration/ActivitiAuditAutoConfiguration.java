package org.activiti.cloud.starter.audit.configuration;

import org.activiti.cloud.services.security.SecurityPoliciesApplicationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan({"org.activiti.cloud.services.audit"})
public class ActivitiAuditAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SecurityPoliciesApplicationService securityPoliciesApplicationService(){
        return new SecurityPoliciesApplicationService();
    }

}
