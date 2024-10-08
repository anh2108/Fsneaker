package com.example.fsneaker.controller;

import com.example.fsneaker.entity.MauSac;
import com.example.fsneaker.entity.XuatXu;
import com.example.fsneaker.service.XuatXuService;
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
public class XuatXuController {

    @Autowired
    private XuatXuService xuatXuService;

    @GetMapping("/qlxuatxu")
    public String XuatXu(){
        return "templateadmin/qlxuatxu.html";
    }

    @GetMapping("/qlmausac")
    public String getALlXuatXu(Model model){
        List<XuatXu> listXuatXu = xuatXuService.getAllXuatXu();
        model.addAttribute("listXuatXu", listXuatXu);
        return "templateadmin/qlxuatxu.html";
    }

    @RequestMapping("/add-xuatXu")
    public String add(Model model){
        XuatXu xuatXu = new XuatXu();
        model.addAttribute("xuatXu", xuatXu);
        return "templateadmin/qlxuatxu.html/add-xuatXu";
    }

    @PostMapping("/add-xuatXu")
    public String save(@ModelAttribute("xuatXu") XuatXu xuatXu ){
        xuatXuService.add(xuatXu);
        return "templateadmin/qlxuatxu.html/add-xuatXu";
    }

    @GetMapping("/edit-mauSac")
    public String edit(Model model, @PathVariable("id") Integer id){
        XuatXu xuatXu = xuatXuService.findById(id);
        model.addAttribute("xuatXu", xuatXu);
        return "templateadmin/qlxuatxu.html/edit-xuatXu";
    }
}
