package com.example.fsneaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class XuatXuController {
    @GetMapping("/qlxuatxu")
    public String XuatXu(){
        return "templateadmin/qlxuatxu.html";
    }
}
