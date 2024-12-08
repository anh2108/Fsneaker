package com.example.fsneaker.controller;

import com.example.fsneaker.entity.MauSac;
import com.example.fsneaker.entity.XuatXu;
import com.example.fsneaker.repositories.XuatXuRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class XuatXuController {

    @Autowired
    private XuatXuRepo xuatXuRepo;

    @GetMapping("/qlxuatxu")
    public String index(Model model){
        List<XuatXu> xuatXuList = xuatXuRepo.findAll();
        model.addAttribute("xuatXuList", xuatXuList);
        XuatXu xuatXu = new XuatXu();
        model.addAttribute("xuatXu",xuatXu);
        return "templateadmin/qlxuatxu";
    }

    @PostMapping("/qlxuatxu/store")
    public String store(@ModelAttribute("xuatXu")XuatXu xuatXu, RedirectAttributes redirectAttributes){
        Map<String , String > errors = new HashMap<>();
        if(xuatXu.getMaXuatXu() == null || xuatXu.getMaXuatXu().isBlank()){
            errors.put("maXuatXu","Mã xuất xứ không được để trống!");
        }else if(xuatXu.getMaXuatXu().length() <5 || xuatXu.getMaXuatXu().length() > 20){
            errors.put("maXuatXu", "Mã xuất xứ từ 5 đến 20 ký tự!");
        }else if(xuatXuRepo.existsByMaXuatXu(xuatXu.getMaXuatXu())){
            redirectAttributes.addFlashAttribute("error","Mã xuất xứ đã tồn tại!");
            return "redirect:/qlxuatxu";
        }
        if(xuatXu.getTenXuatXu() == null || xuatXu.getTenXuatXu().isBlank()){
            errors.put("tenXuatXu","Tên xuất xứ không được để trống!");
        }else if(xuatXu.getTenXuatXu().length() > 30){
            errors.put("tenXuatXu","Tên xuất xứ dưới 30 ký tự!");
        }
        if(!errors.isEmpty()){
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:/qlxuatxu";
        }
        xuatXuRepo.save(xuatXu);
        return "redirect:/qlxuatxu";
    }

    @GetMapping("/qlxuatxu/edit/{id}")
    public String editXuatXu(@PathVariable int id, Model model) {
        XuatXu ms = xuatXuRepo.getXuatXuById(id);
        model.addAttribute("editXuatXu",ms);
        List<XuatXu> list = xuatXuRepo.findAll();
        model.addAttribute("xxu",list);
        return "templateadmin/qlxuatxu";
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
