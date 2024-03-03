package org.example.controller;

import org.example.model.HelloWorld;
import org.example.service.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
public class WelcomeController {

    private final HelloWorldService service;

    @Autowired
    private Environment environment;

    @Autowired
    private ConfigurableEnvironment configurableEnvironment;

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

    @GetMapping("/choose-profile")
    public String chooseProfilePage() {
        return "choose_profile";
    }

    @PostMapping("/choose-profile")
    @ResponseBody
    public String chooseProfile(@RequestParam("buttonType") String buttonType) {
        if ("repository".equals(buttonType)) {
            configurableEnvironment.setActiveProfiles("repository");
            return "Active profiles set to: repository";
        } else if ("myMemory".equals(buttonType)) {
            configurableEnvironment.setActiveProfiles("myMemory");
            return "Active profiles set to: myMemory";
        } else {
            return "error_page";
        }
    }

    @PostMapping("/profiles")
    public String setActiveProfiles(@RequestBody String[] profiles) {
        configurableEnvironment.setActiveProfiles(profiles);
        return "Active profiles set to: " + Arrays.toString(profiles);
    }

    @GetMapping("/switch-hello/language")
    public String switchHello(@RequestParam("language") String language, Model model) {
        return service.switchHello(language, model);
    }

    @GetMapping("/hello/language")
    public String repositoryHello(@RequestParam String language, Model model) {
        return service.repositoryHello(language, model);
    }

    @GetMapping("/my-memory-hello/language")
//    @ResponseBody
    public String myMemoryHello(@RequestParam String language, Model model) {
        return service.myMemoryHello(language, model);
    }

    @GetMapping("/secure/hello/language")
    public String secureHello(@RequestParam String language, Model model) {
        return service.secureHello(language, model);
    }

//    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/hello-world")
    public String adminPage() {
        return "new_hello_world";
    }

//    @PreAuthorize("hasRole('ROLE_Admin')")
    @PostMapping("/hello-world")
    @ResponseBody
    public HelloWorld create(@ModelAttribute HelloWorld helloWorld) {
        return service.create(helloWorld);
    }
}
