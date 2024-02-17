package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

@RestController
@RequestMapping("info")
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

    @GetMapping(path = "sum")
    public ResponseEntity<?> getSum(
            @RequestParam(defaultValue = "1000000") Integer max
    ) {
        try {
            long sum = IntStream
                    .range(1, max + 1)
                    .reduce(0, Integer::sum);;
            return ResponseEntity.ok(
                    sum
            );
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
