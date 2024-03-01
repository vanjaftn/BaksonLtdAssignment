package org.example.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.interfaces.HelloEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

//@Component
public class HelloEndpointWithMyMemory implements HelloEndpoint {

    @Autowired
    private RestTemplate restTemplate;

    private static final String MYMEMORY_TRANSLATE_API_URL = "http://mymemory.translated.net/api/get";

    @Override
    public String hello(String language, Model model) {
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

        // Return the translated text
//        return translatedText;
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
}

