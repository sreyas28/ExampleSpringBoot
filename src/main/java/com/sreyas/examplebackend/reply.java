package com.sreyas.examplebackend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class reply {

    @GetMapping("/reply")
    public Map<String, String> replying() {
        return Map.of("message", "Hello World! From the Backend");
    }
}
