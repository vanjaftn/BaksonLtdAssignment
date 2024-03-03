package org.example.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.model.HelloWorld;
import org.example.repository.HelloWorldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class HelloWorldService {

    private final String MYMEMORY_TRANSLATE_API_URL = "https://api.mymemory.translated.net/get";

    private final RestTemplate restTemplate;

    @Autowired
    private Environment environment;
    private final HelloWorldRepository repository;

    public HelloWorldService(HelloWorldRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public HelloWorld create(@ModelAttribute HelloWorld helloWorld) {
        return repository.save(helloWorld);
    }

    public List<HelloWorld> getAll() {
        return repository.findAll();
    }

    public String switchHello(@PathVariable("language") String language, Model model) {
        if (environment.acceptsProfiles("myMemory")) {
            // Call the myMemoryHello endpoint
            return myMemoryHello(language, model);
        } else {
            // Call the repository-based hello endpoint
            return repositoryHello(language, model);
        }
    }

    public String repositoryHello(@PathVariable("language") String language, Model model) {
        String string = repository.findStringByLanguage(language);
        model.addAttribute("string", string);
        return "repository_template";
    }

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

        return translatedText;
    }

    public String secureHello(@PathVariable("language") String language, Model model) {
            String string = repository.findStringByLanguage(language);
            model.addAttribute("string", string);
            return "repository_template";
    }
}
