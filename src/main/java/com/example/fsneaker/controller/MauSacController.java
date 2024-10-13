package com.example.fsneaker.controller;

import com.example.fsneaker.entity.MauSac;
import com.example.fsneaker.repositories.MauSacRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller

public class MauSacController {

    @Autowired
    private MauSacRepo mauSacRepo;

    @GetMapping("/qlmausac")
    public String index(Model model){
        List<MauSac > ms = mauSacRepo.findAll();
        model.addAttribute("ms",ms);
        return "templateadmin/qlmausac.html";
    }

    @PostMapping("/qlmausac/store")
    public String storeMauSac(@RequestParam String maMauSac,
                              @RequestParam String mauSac,
                              @RequestParam int trangThai) {
        MauSac newMauSac = new MauSac();
        newMauSac.setMaMauSac(maMauSac);
        newMauSac.setMauSac(mauSac);
        newMauSac.setTrangThai(trangThai == 0 ? 0 : 1);

        mauSacRepo.save(newMauSac);

        return "redirect:/qlmausac"; // Redirect after successful submission

    }

    @GetMapping("/qlmausac/edit/{id}")
    public String editMauSac(@PathVariable int id, Model model) {
        MauSac ms = mauSacRepo.getMauSacById(id);
        model.addAttribute("editMauSac",ms);
        List<MauSac> list = mauSacRepo.findAll();
        model.addAttribute("msac",list);
        return "templateadmin/qlmausac.html";
    }

    @PostMapping("/qlmausac/update")
    public String update(
            @RequestParam int id,
            @RequestParam String maMauSac,
            @RequestParam String mauSac,
            @RequestParam int trangThai) {
        MauSac existingMauSac = mauSacRepo.getMauSacById(id);
        if (existingMauSac != null) {
            existingMauSac.setMaMauSac(maMauSac);
            existingMauSac.setMauSac(mauSac);
            existingMauSac.setTrangThai(trangThai);
            mauSacRepo.save(existingMauSac);
        }

        return "redirect:/qlmausac";
    }

}
