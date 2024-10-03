package com.example.fsneaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VoucherController {
    @GetMapping("/qlvoucher")
    public String Voucher(){
        return "templateadmin/qlvoucher.html";
    }
}
