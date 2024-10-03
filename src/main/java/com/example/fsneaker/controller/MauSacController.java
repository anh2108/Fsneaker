package com.example.fsneaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MauSacController {
    @GetMapping("/qlmausac")
    public String MauSac(){
        return "templateadmin/qlmausac.html";
    }
}
