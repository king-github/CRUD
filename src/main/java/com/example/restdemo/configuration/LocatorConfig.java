package com.example.restdemo.configuration;

import com.example.restdemo.locator.ResourceLocator;
import com.example.restdemo.locator.TaskLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocatorConfig {

    @Bean
    public ResourceLocator getTaskLocator() {

        return new TaskLocator();
    }

}
