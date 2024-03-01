package org.example.interfaces;
import org.springframework.ui.Model;

public interface HelloEndpoint {
    String hello(String language, Model model);
}
