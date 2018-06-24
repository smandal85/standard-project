package com.backbase.recruitment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by andresc on 04/07/2017.
 * This file replaces the dispatcher-servlet.xml and complements applicationContext to configure the Spring Context
 */
@Configuration
@ComponentScan
@PropertySources(value = {@PropertySource("classpath:application.properties")})
//@ImportResource("classpath:/META-INF/spring/camel-context.xml")
public class ApplicationConfiguration {
}
