package com.example.fsneaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TrangAdminController {
    @GetMapping("/trangchuadmin")
    public String TrangChu(){
        return "templateadmin/trangadmin.html";
    }
}