package com.example.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @GetMapping("/")
    fun index(
        @RequestParam params: Map<String, String>,
    ): Map<String, String> {
        return params
    }
}
