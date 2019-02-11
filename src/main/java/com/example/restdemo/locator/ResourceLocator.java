package com.example.restdemo.locator;

import java.net.URI;

public interface ResourceLocator {

    URI getLocator(String id);
}
