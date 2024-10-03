package com.example.fsneaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KhuyenMaiController {
    @GetMapping("/qlkhuyenmai")
    public String KhuyenMai(){
        return "templateadmin/qlkhuyenmai.html";
    }
}
