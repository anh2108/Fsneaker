package com.example.fsneaker.controller;

import com.example.fsneaker.entity.KichThuoc;
import com.example.fsneaker.entity.XuatXu;
import com.example.fsneaker.service.KichThuocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class KichThuocController {
    @Autowired
    private KichThuocService kichThuocService;

    @GetMapping("/qlkichthuoc")
    public String KichThuoc(){
        return "templateadmin/qlkichthuoc.html";
    }

    @GetMapping("/qlkichthuoc")
    public String getAllKichTHuoc(Model model){
        List<KichThuoc> listKichThuoc = kichThuocService.getAllkichThuoc();
        model.addAttribute("listKichThuoc", listKichThuoc);
        return "templateadmin/qlkichthuoc.html";
    }

    @RequestMapping("/add-kichThuoc")
    public String add(Model model){
        KichThuoc kichThuoc = new KichThuoc();
        model.addAttribute("kichTHuoc", kichThuoc);
        return "templateadmin/qlkichthuoc.html/add-kichThuoc";
    }

    @PostMapping("/add-kichThuoc")
    public String save(@ModelAttribute("kichThuoc") KichThuoc kichThuoc ){
        kichThuocService.add(kichThuoc);
        return "templateadmin/qlkichthuoc.html/add-kichThuoc";
    }

    @GetMapping("/edit-kichThuoc")
    public String edit(Model model, @PathVariable("id") Integer id){
        KichThuoc kichThuoc = kichThuocService.findById(id);
        model.addAttribute("kichThuoc", kichThuoc);
        return "templateadmin/qlkichthuoc.html/edit-kichThuoc";
    }
}
