package org.activiti.cloud.services.audit.jpa.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;
import org.activiti.cloud.services.audit.jpa.events.AuditEventEntity;
import org.activiti.cloud.services.audit.jpa.repository.EventSpecificationsBuilder;
import org.activiti.cloud.services.audit.jpa.repository.SearchOperation;
import org.springframework.data.jpa.domain.Specification;

public class SearchUtils {
    public Specification<AuditEventEntity> createSearchSpec(String search) {
        EventSpecificationsBuilder builder = new EventSpecificationsBuilder();
        if (search != null && !search.isEmpty()) {
            String operationSetExper = Joiner.on("|")
                    .join(SearchOperation.SIMPLE_OPERATION_SET);
            Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)([a-zA-Z0-9-_]+?)(\\p{Punct}?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1),
                             matcher.group(2),
                             matcher.group(4),
                             matcher.group(3),
                             matcher.group(5));
            }
        }

        return builder.build();
    }

}
