package org.example.controller;
import org.example.model.HelloWorld;
import org.example.repository.HelloWorldRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WelcomeController {

    private final HelloWorldRepository repository;

    public WelcomeController(HelloWorldRepository repository) {
        this.repository = repository;
    }
    @GetMapping("/hello-rest")
    @ResponseBody
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("/getAllStrings")
    @ResponseBody
    public List<HelloWorld> getAll() {
        return repository.findAll();
    }

//    @RequestMapping("/hello")
//    public String helloWorldHTML() {
//        return "index";
//    }

//    @GetMapping("/hello/{language}")
//    @ResponseBody
//    public String findStringByLanguage(@PathVariable("language") String language){
//        HelloWorld helloWorld = repository.findStringByLanguage(language);
//        return helloWorld.getString();
//    }

//    @GetMapping("/hello/{language}")
//    public String helloWorld(@PathVariable("language") String language, Model model) {
//        String helloWorldString = repository.findStringByLanguage(language);
//        model.addAttribute(helloWorldString);
//        return "index";
//    }

    @GetMapping("/hello/{language}")
    public String hello(@PathVariable("language") String language, Model model) {
        String string = repository.findStringByLanguage(language);
        model.addAttribute("string", string);
        return "index";
    }

    @GetMapping("/secure/hello/{language}")
    public String secureHello(@PathVariable("language") String language, Model model) {
        String string = repository.findStringByLanguage(language);
        model.addAttribute("string", string);
        return "index";
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
        System.out.println(helloWorld);
        return repository.save(helloWorld);
    }
}
