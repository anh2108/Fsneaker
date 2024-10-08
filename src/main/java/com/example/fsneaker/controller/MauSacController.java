package com.example.fsneaker.controller;

import com.example.fsneaker.entity.MauSac;
import com.example.fsneaker.service.MauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MauSacController {

    @Autowired
    private MauSacService mauSacService;

    @GetMapping("/qlmausac")
    public String MauSac(){
        return "templateadmin/qlmausac.html";
    }

    @GetMapping("/qlmausac")
    public String getAllMauSac(Model model){
        List<MauSac> listMauSac = mauSacService.getAllMauSac();
        model.addAttribute("listMauSac", listMauSac);
        return "templateadmin/qlmausac.html";
    }

    @RequestMapping("/add-mauSac")
    public String add(Model model){
        MauSac mauSac = new MauSac();
        model.addAttribute("mauSac", mauSac);
        return "templateadmin/qlmausac.html/add-mauSac";
    }

    @PostMapping("/add-mauSac")
    public String save(@ModelAttribute("mauSac") MauSac mauSac ){
        mauSacService.add(mauSac);
        return "templateadmin/qlmausac.html/add-mauSac";
    }

    @GetMapping("/edit-mauSac")
    public String edit(Model model, @PathVariable("id") Integer id){
        MauSac mauSac = mauSacService.findById(id);
        model.addAttribute("mauSac", mauSac);
        return "templateadmin/qlmausac.html/edit-mauSac";
    }


}
