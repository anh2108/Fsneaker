package com.example.fsneaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KichThuocController {
    @GetMapping("/qlkichthuoc")
    public String KichThuoc(){
        return "templateadmin/qlkichthuoc.html";
    }
}
