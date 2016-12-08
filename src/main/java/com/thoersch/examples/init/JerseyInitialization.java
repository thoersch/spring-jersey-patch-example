package com.thoersch.examples.init;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.thoersch.examples.persistence.members.MemberRepository;
import com.thoersch.examples.resources.members.MemberResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.stereotype.Component;

@Component
public class JerseyInitialization extends ResourceConfig {
    public JerseyInitialization() {
        this.register(RequestContextFilter.class);
        this.register(ObjectMapperResolver.class);
        this.register(new JacksonJsonProvider(ObjectMapperFactory.create()));

        this.packages("com.thoersch.examples.resources", "com.thoersch.examples.init.interceptors");
    }
}
