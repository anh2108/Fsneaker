package com.example.fsneaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThuongHieuController {
    @GetMapping("/qlthuonghieu")
    public String ThuongHieu(){
        return "templateadmin/qlthuonghieu.html";
    }
}
