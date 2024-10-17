package com.example.fsneaker.controller;

import com.example.fsneaker.entity.SanPham;
import com.example.fsneaker.entity.SanPhamChiTiet;
import com.example.fsneaker.repositories.SanPhamChiTietRepo;
import com.example.fsneaker.repositories.SanPhamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SanPhamController {

    @Autowired
    private SanPhamRepo sanPhamRepo;

    @Autowired
    private SanPhamChiTietRepo sanPhamChiTietRepo;

    @GetMapping("/qlsanpham")
   public String index(Model model){

        List<SanPham> list = sanPhamRepo.findAll();
        model.addAttribute("listSanPham", list);

        List<SanPhamChiTiet> list2 = sanPhamChiTietRepo.findAll();
        model.addAttribute("listSanPhamChiTiet", list2);

        return "templateadmin/qlsanpham.html";

    }



}
