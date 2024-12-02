package com.zero.milvus.controller;

import com.zero.milvus.service.HelloZillizVectorDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/milvus")
public class MilvusController {

    HelloZillizVectorDBService helloZillizVectorDBService;

    @Autowired
    public MilvusController(HelloZillizVectorDBService helloZillizVectorDBService) {
        this.helloZillizVectorDBService = helloZillizVectorDBService;
    }

    @RequestMapping("/hello")
    public Map<String, Object> showHelloWorld() {
        Map<String, Object> map = new HashMap<String, Object>();
        helloZillizVectorDBService.search();
        map.put("msg", "HelloWorld");
        return map;
    }

}