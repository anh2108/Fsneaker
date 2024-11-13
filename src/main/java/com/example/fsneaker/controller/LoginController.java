package com.example.fsneaker.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String trangChu(){
        return "login";
    }
    @GetMapping("/redirectByRole")
    public String redirectByRole(Authentication authentication){
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        if("ROLE_ADMIN".equals(role)){
            return "redirect:/trangchuadmin";
        }else if("ROLE_STAFF".equals(role)){
            return "redirect:/trangchuadmin";
        }else if("ROLE_CUSTOMER".equals(role)){
            return "redirect:/trang-chu";
        }
        return "redirect:/login?error";
    }
}
