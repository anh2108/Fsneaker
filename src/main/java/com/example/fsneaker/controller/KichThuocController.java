package com.example.fsneaker.controller;

import com.example.fsneaker.entity.KichThuoc;
import com.example.fsneaker.entity.MauSac;
import com.example.fsneaker.repositories.KichThuocRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class KichThuocController {
    @Autowired
    KichThuocRepo kichThuocRepo;
    @GetMapping("/qlkichthuoc")
    public String index(Model model){
        List<KichThuoc> list = kichThuocRepo.findAll();
        model.addAttribute("kichthuoc", list);
        return "templateadmin/qlkichthuoc.html";
    }

    @PostMapping("qlkichthuoc/store")
    public String store(@RequestParam String maKichThuoc,
                        @RequestParam String kichThuoc,
                        @RequestParam int trangThai){

        KichThuoc kt = new KichThuoc();
        kt.setMakichThuoc(maKichThuoc);
        kt.setKichThuoc(kichThuoc);
        kt.setTrangThai(trangThai == 0 ? 0 : 1);
        kichThuocRepo.save(kt);

        return "redirect:/qlkichthuoc";
    }

    @GetMapping("/qlkichthuoc/edit/{id}")
    public String editKichThuoc(@PathVariable int id, Model model) {
        KichThuoc kt = kichThuocRepo.getKichThuocById(id);
        model.addAttribute("editKichThuoc",kt);
        List<KichThuoc> list = kichThuocRepo.findAll();
        model.addAttribute("kthuoc",list);
        return "templateadmin/qlkichthuoc.html";
    }

    @PostMapping("/qlkichthuoc/update")
    public String update(
            @RequestParam int id,
            @RequestParam String makichThuoc,
            @RequestParam String kichThuoc,
            @RequestParam int trangThai) {
        KichThuoc existingKichThuoc = kichThuocRepo.getKichThuocById(id);
        if (existingKichThuoc != null) {
            existingKichThuoc.setMakichThuoc(makichThuoc);
            existingKichThuoc.setKichThuoc(kichThuoc);
            existingKichThuoc.setTrangThai(trangThai);
            kichThuocRepo.save(existingKichThuoc);
        }

        return "redirect:/qlkichthuoc";
    }

}
