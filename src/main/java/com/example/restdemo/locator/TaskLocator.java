package com.example.restdemo.locator;

import com.example.restdemo.controller.TaskController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;

public class TaskLocator implements ResourceLocator {

    @Override
    public URI getLocator(String id) {

        return MvcUriComponentsBuilder.fromMethodCall(
                MvcUriComponentsBuilder.on(TaskController.class).getTask(id))
                .buildAndExpand().toUri();
    }

}
