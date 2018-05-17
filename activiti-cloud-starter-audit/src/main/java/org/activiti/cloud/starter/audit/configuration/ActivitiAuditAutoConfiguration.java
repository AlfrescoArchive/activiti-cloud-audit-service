package org.activiti.cloud.starter.audit.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan({"org.activiti.cloud.services.audit"})
public class ActivitiAuditAutoConfiguration {

}
