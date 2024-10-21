package com.example.fsneaker.controller;

import com.example.fsneaker.entity.MauSac;
import com.example.fsneaker.entity.XuatXu;
import com.example.fsneaker.repositories.XuatXuRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class XuatXuController {

    @Autowired
    private XuatXuRepo xuatXuRepo;

    @GetMapping("/qlxuatxu")
    public String index(Model model){
        List<XuatXu> xuatXuList = xuatXuRepo.findAll();
        model.addAttribute("xuatXuList", xuatXuList);
        return "templateadmin/qlxuatxu.html";
    }

    @PostMapping("/qlxuatxu/store")
    public String store(@RequestParam String maXuatXu,
                        @RequestParam String tenXuatXu,
                        @RequestParam int trangThai){

        XuatXu xuatXu1 = new XuatXu();
        xuatXu1.setMaXuatXu(maXuatXu);
        xuatXu1.setTenXuatXu(tenXuatXu);
        xuatXu1.setTrangThai(trangThai == 0? 0 : 1);
        xuatXuRepo.save(xuatXu1);
        return "redirect:/qlxuatxu";
    }

    @GetMapping("/qlxuatxu/edit/{id}")
    public String editXuatXu(@PathVariable int id, Model model) {
        XuatXu ms = xuatXuRepo.getXuatXuById(id);
        model.addAttribute("editXuatXu",ms);
        List<XuatXu> list = xuatXuRepo.findAll();
        model.addAttribute("xxu",list);
        return "templateadmin/qlxuatxu.html";
    }

    @PostMapping("/qlxuatxu/update")
    public String update(
            @RequestParam int id,
            @RequestParam String maXuatXu,
            @RequestParam String tenXuatXu,
            @RequestParam int trangThai) {
        XuatXu existingXuatXu = xuatXuRepo.getXuatXuById(id);
        if (existingXuatXu != null) {
            existingXuatXu.setMaXuatXu(maXuatXu);
            existingXuatXu.setTenXuatXu(tenXuatXu);
            existingXuatXu.setTrangThai(trangThai);
            xuatXuRepo.save(existingXuatXu);
        }

        return "redirect:/qlxuatxu";
    }


}
