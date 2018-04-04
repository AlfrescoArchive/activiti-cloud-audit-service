package org.activiti.cloud.starter.audit.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.activiti.cloud.services.audit.mongo")
public class ActivitiAuditMongoAutoConfiguration {

}
