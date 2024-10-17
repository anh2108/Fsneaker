package com.example.fsneaker.controller;

import com.example.fsneaker.entity.SanPhamChiTiet;
import com.example.fsneaker.repositories.SanPhamChiTietRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SanPhamChiTietController {

    @Autowired
    private SanPhamChiTietRepo sanPhamChiTietRepo;

    @GetMapping("/qlsanphamchitiet")
   public String index(Model model) {

        return "templateadmin/qlsanpham.html";
    }


}
