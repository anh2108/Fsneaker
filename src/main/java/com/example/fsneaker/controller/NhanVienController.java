package com.example.fsneaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NhanVienController {
    @GetMapping("/qlnhanvien")
    public String NhanVien(){
        return "templateadmin/qlnhanvien.html";
    }
}
