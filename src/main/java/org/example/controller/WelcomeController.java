package org.example.controller;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.model.HelloWorld;
import org.example.repository.HelloWorldRepository;
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

    private final String MYMEMORY_TRANSLATE_API_URL = "https://api.mymemory.translated.net/get";

    private final RestTemplate restTemplate;

    @Autowired
    private Environment environment;
    private final HelloWorldRepository repository;


    public WelcomeController(HelloWorldRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
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

    @GetMapping("/switchHello/{language}")
    public String switchHello(@PathVariable("language") String language, Model model) {
        if (environment.acceptsProfiles("myMemory")) {
            // Call the myMemoryHello endpoint
            return myMemoryHello(language, model);
        } else {
            // Call the repository-based hello endpoint
            return repositoryHello(language, model);
        }
    }

    @GetMapping("/hello/{language}")
    public String repositoryHello(@PathVariable("language") String language, Model model) {
        String string = repository.findStringByLanguage(language);
        model.addAttribute("string", string);
        return "repository_template";
    }

    @GetMapping("/myMemoryHello/{language}")
    @ResponseBody
    public String myMemoryHello(@PathVariable("language") String language, Model model) {
        // Text to translate
        String textToTranslate = "Hello World";

        // Build the API URL with query parameters
        String apiUrl = MYMEMORY_TRANSLATE_API_URL +
                "?q=" + textToTranslate +
                "&langpair=en|" + language;

        // Send GET request to MyMemory Translation API and retrieve response as String
        String response = restTemplate.getForObject(apiUrl, String.class);

        // Extract translated text from the response
        String translatedText = extractTranslatedText(response);

        model.addAttribute("string", translatedText);
        return "my_memory_template";
    }

    private String extractTranslatedText(String response) {
        // Parse the JSON response
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonObject responseData = jsonObject.getAsJsonObject("responseData");

        // Extract translated text from responseData
        String translatedText = responseData.get("translatedText").getAsString();

        // Decode Unicode escape sequences if needed
        translatedText = decodeUnicodeEscapeSequences(translatedText);

        return translatedText;
    }

    private String decodeUnicodeEscapeSequences(String input) {
        StringBuilder builder = new StringBuilder();

        int i = 0;
        while (i < input.length()) {
            char currentChar = input.charAt(i);
            if (currentChar == '\\' && i + 1 < input.length() && input.charAt(i + 1) == 'u') {
                // Decode Unicode escape sequence
                String unicodeSequence = input.substring(i + 2, i + 6);
                char decodedChar = (char) Integer.parseInt(unicodeSequence, 16);
                builder.append(decodedChar);
                i += 6;
            } else if (currentChar == '_') {
                // Skip special sequences like "_BAR_/" and "_BAR_$[" until we find a ";"
                int end = input.indexOf(";", i);
                if (end != -1) {
                    i = end + 1;
                } else {
                    // If no ";" found, just append "_"
                    builder.append(currentChar);
                    i++;
                }
            } else if (currentChar == '&' && i + 5 < input.length() && input.substring(i, i + 6).equals("&apos;")) {
                // Skip "&apos;" sequence
                i += 5;
            } else {
                builder.append(currentChar);
                i++;
            }
        }

        return builder.toString();
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
