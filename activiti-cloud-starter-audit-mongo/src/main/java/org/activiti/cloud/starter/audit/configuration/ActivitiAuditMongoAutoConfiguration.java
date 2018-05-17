package org.activiti.cloud.starter.audit.configuration;

import org.activiti.cloud.services.security.SecurityPoliciesApplicationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"org.activiti.cloud.services.audit"})
public class ActivitiAuditMongoAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public SecurityPoliciesApplicationService securityPoliciesApplicationService(){
        return new SecurityPoliciesApplicationService();
    }
}
