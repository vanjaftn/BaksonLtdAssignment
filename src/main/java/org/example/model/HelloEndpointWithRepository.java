package org.example.model;

import org.example.interfaces.HelloEndpoint;
import org.example.repository.HelloWorldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

//@Component
public class HelloEndpointWithRepository implements HelloEndpoint {

    @Autowired
    private HelloWorldRepository repository;

    @Override
    public String hello(String language, Model model) {
        String string = repository.findStringByLanguage(language);
        model.addAttribute("string", string);
        return "repository_template";
    }
}

