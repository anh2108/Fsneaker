package com.example.fsneaker.controller;

import com.example.fsneaker.entity.ThuongHieu;
import com.example.fsneaker.entity.XuatXu;
import com.example.fsneaker.repositories.ThuongHieuRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ThuongHieuController {

    @Autowired
    private ThuongHieuRepo thuongHieuRepo;

    @GetMapping("/qlthuonghieu")
    public String index( Model model,
                         @RequestParam(name = "page", defaultValue = "0")Integer pageNo,
                         @RequestParam(name = "limit", defaultValue = "5")Integer pageSize,
                         Pageable pageable){


        Page<ThuongHieu> thuongHieuList = thuongHieuRepo.findAll(pageable);

//        List<ThuongHieu> thuongHieuList = thuongHieuRepo.findAll();
        model.addAttribute("thieu", thuongHieuList);
        return "/templateadmin/qlthuonghieu.html";

    }

    @PostMapping("/qlthuonghieu/store")
    public String store(@RequestParam String maThuongHieu,
                        @RequestParam String tenThuongHieu,
                        @RequestParam int trangThai){

       ThuongHieu thuongHieu = new ThuongHieu();
        thuongHieu.setMaThuongHieu(maThuongHieu);
        thuongHieu.setTenThuongHieu(tenThuongHieu);
        thuongHieu.setTrangThai(trangThai == 0? 0 : 1);
        thuongHieuRepo.save(thuongHieu);
        return "redirect:/qlthuonghieu";
    }

    @GetMapping("/qlthuonghieu/edit/{id}")
    public String editXuatXu(@PathVariable int id, Model model) {
        ThuongHieu th = thuongHieuRepo.getThuongHieuById(id);
        model.addAttribute("th",th);
        List<ThuongHieu> list = thuongHieuRepo.findAll();
        model.addAttribute("thieu",list);
        return "templateadmin/qlthuonghieu.html";
    }

    @PostMapping("/qlthuonghieu/update")
    public String update(
            @RequestParam int id,
            @RequestParam String maThuongHieu,
            @RequestParam String tenThuongHieu,
            @RequestParam int trangThai) {
        ThuongHieu existingThuongHieu = thuongHieuRepo.getThuongHieuById(id);
        if (existingThuongHieu != null) {
            existingThuongHieu.setMaThuongHieu(maThuongHieu);
            existingThuongHieu.setTenThuongHieu(tenThuongHieu);
            existingThuongHieu.setTrangThai(trangThai);
            thuongHieuRepo.save(existingThuongHieu);
        }

        return "redirect:/qlthuonghieu";
    }

}