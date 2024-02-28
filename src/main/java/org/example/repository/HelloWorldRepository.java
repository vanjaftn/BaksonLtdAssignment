package org.example.repository;

import org.example.model.HelloWorld;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HelloWorldRepository extends JpaRepository<HelloWorld, Integer> {
    @Query("SELECT hw.string FROM hello_world hw WHERE hw.language = ?1")
    String findStringByLanguage(String language);
}
