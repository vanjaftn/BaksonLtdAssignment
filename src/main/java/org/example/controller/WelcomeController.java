package org.example.controller;

import org.example.model.HelloWorld;
import org.example.service.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class WelcomeController {

    private final HelloWorldService service;


    public WelcomeController(HelloWorldService service) {
        this.service = service;
    }

    @GetMapping("/hello-rest")
    @ResponseBody
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("/getAllStrings")
    @ResponseBody
    public List<HelloWorld> getAll() {
        return service.getAll();
    }

    @RequestMapping("/hello")
    public String helloWorldHTML() {
        return "index";
    }

//    @GetMapping("/hello/{language}")
//    @ResponseBody
//    public String findStringByLanguage(@PathVariable("language") String language){
//        HelloWorld helloWorld = repository.findStringByLanguage(language);
//        return helloWorld.getString();
//    }

    @GetMapping("/switchHello/{language}")
    public String switchHello(@PathVariable("language") String language, Model model) {
        return service.switchHello(language, model);
    }

    @GetMapping("/hello/{language}")
    public String repositoryHello(@PathVariable("language") String language, Model model) {
        return service.repositoryHello(language, model);
    }

    @GetMapping("/myMemoryHello/{language}")
//    @ResponseBody
    public String myMemoryHello(@PathVariable("language") String language, Model model) {
        return service.myMemoryHello(language, model);
    }

    @GetMapping("/secure/hello/{language}")
    public String secureHello(@PathVariable("language") String language, Model model) {
        return service.secureHello(language, model);
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/hello-world")
    public String adminPage() {
        return "new_hello_world";
    }

    @PreAuthorize("hasRole('ROLE_Admin')")
    @PostMapping("/hello-world")
    @ResponseBody
    public HelloWorld create(@ModelAttribute HelloWorld helloWorld) {
        return service.create(helloWorld);
    }
}
