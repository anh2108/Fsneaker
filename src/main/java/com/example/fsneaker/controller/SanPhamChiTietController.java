package com.example.fsneaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SanPhamChiTietController {
    @GetMapping("/qlsanphamchitiet")
    public String SanPhamChiTiet(){
        return "templateadmin/qlsanphamchitiet.html";
    }
}
