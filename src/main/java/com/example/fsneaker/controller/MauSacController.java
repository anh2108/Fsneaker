package com.example.fsneaker.controller;

import com.example.fsneaker.entity.MauSac;
import com.example.fsneaker.repositories.MauSacRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller

public class MauSacController {

    @Autowired
    private MauSacRepo mauSacRepo;

    @GetMapping("/qlmausac")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") Integer pageNo,
                        @RequestParam(name = "limit", defaultValue = "5") Integer pageSize
                        ){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<MauSac> ms = mauSacRepo.findAll(pageable);
        MauSac mauSac = new MauSac();
        model.addAttribute("mauSac", mauSac);
        model.addAttribute("ms",ms);
        return "templateadmin/qlmausac";
    }

    @PostMapping("/qlmausac/store")
    public String storeMauSac(@ModelAttribute("mauSac")MauSac mauSac, RedirectAttributes redirectAttributes) {
        Map<String,String > errors = new HashMap<>();
        if(mauSac.getMaMauSac() == null || mauSac.getMaMauSac().isBlank()){
            errors.put("maMauSac", "Mã màu sắc không được để trống!");
        }else if(mauSac.getMaMauSac().length() < 5 || mauSac.getMaMauSac().length() > 20){
            errors.put("maMauSac","Mã màu sắc từ 5 đến 20 ký tự!");
        }else if(mauSacRepo.existsByMaMauSac(mauSac.getMaMauSac())){
            redirectAttributes.addFlashAttribute("error", "Mã màu sắc đã tồn tại!");
            return "redirect:/qlmausac";
        }
        if(mauSac.getTenMauSac() == null || mauSac.getTenMauSac().isBlank()){
            errors.put("tenMauSac","Tên màu sắc không được để trống!");
        }else if(mauSac.getTenMauSac().length() < 2 || mauSac.getTenMauSac().length() > 30){
            errors.put("tenMauSac","Tên màu sắc từ 2 đến 30 ký tự!");
        }
        if(!errors.isEmpty()){
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:/qlmausac";
        }
        mauSacRepo.save(mauSac);
        return "redirect:/qlmausac"; // Redirect after successful submission
    }

    @GetMapping("/qlmausac/edit/{id}")
    public String editMauSac(@PathVariable int id, Model model) {
        MauSac ms = mauSacRepo.getMauSacById(id);
        model.addAttribute("editMauSac",ms);
        List<MauSac> list = mauSacRepo.findAll();
        model.addAttribute("msac",list);
        return "templateadmin/qlmausac";
    }

    @PostMapping("/qlmausac/update")
    public String update(
            @RequestParam int id,
            @RequestParam String maMauSac,
            @RequestParam String tenMauSac,
            @RequestParam int trangThai) {
        MauSac existingMauSac = mauSacRepo.getMauSacById(id);
        if (existingMauSac != null) {
            existingMauSac.setMaMauSac(maMauSac);
            existingMauSac.setTenMauSac(tenMauSac);
            existingMauSac.setTrangThai(trangThai);
            mauSacRepo.save(existingMauSac);
        }
        return "redirect:/qlmausac";
    }

}
