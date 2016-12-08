package com.thoersch.examples;

import com.thoersch.examples.init.filters.ApiOriginFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.AuthenticationManagerConfiguration;
import org.springframework.boot.autoconfigure.security.FallbackWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SpringBootWebSecurityConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

import java.util.Properties;

@Configuration
@EnableAutoConfiguration(exclude = {AuthenticationManagerConfiguration.class, FallbackWebSecurityAutoConfiguration.class, SecurityAutoConfiguration.class, SpringBootWebSecurityConfiguration.class})
@ComponentScan
public class Application {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("org.apache.tomcat.util.http. ServerCookie.STRICT_NAMING", "false");
        properties.setProperty("org.apache.tomcat.util.http.ServerCookie.ALLOW_HTTP_SEPARATORS_IN_V0", "true");

        new SpringApplicationBuilder(Application.class).properties(properties).run(args);
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
        return new TomcatEmbeddedServletContainerFactory();
    }

    @Bean
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
        return new OpenEntityManagerInViewFilter();
    }

    @Bean
    public ApiOriginFilter apiOriginFilter() {
        return new ApiOriginFilter();
    }
}
