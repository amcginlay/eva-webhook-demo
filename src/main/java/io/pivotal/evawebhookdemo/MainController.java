package io.pivotal.evawebhookdemo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @PostMapping
    public void webhook(@RequestBody String values) {
        System.out.println("PAYLOAD: " + values);
    }
}
