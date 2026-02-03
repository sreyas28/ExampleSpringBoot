package com.sreyas.examplebackend;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class reply {

    @RequestMapping("/reply")
    public String reply() {

        return "Hello World! Form the Backend";
    }

}
