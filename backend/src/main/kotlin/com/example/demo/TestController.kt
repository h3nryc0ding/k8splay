package com.example.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.*

@RestController
class TestController {
    @GetMapping("/")
    fun index(@RequestParam params: Map<String, String>): Map<String, String> {
        return params
    }
}
