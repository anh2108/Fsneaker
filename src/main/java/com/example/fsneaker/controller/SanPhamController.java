package com.example.fsneaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SanPhamController {
    @GetMapping("/qlsanpham")
    public String SanPham(){
        return "templateadmin/qlsanpham.html";
    }
}
