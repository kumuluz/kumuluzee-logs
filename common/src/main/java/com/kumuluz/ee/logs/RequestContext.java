package com.kumuluz.ee.logs;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;


public interface RequestContext {

    HashMap<String, String> getContext();

}
