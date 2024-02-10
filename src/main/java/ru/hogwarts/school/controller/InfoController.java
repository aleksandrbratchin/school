package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("student")
public class InfoController {

    @Value("${server.port}")
    private String port;

    @GetMapping(path = "port")
    public ResponseEntity<?> getPort() {
        try {
            return ResponseEntity.ok(
                    port
            );
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


}
