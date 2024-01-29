package com.nva.server.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
public class AdminController {
    @GetMapping
    public String index() {
        return "admin";
    }
}
