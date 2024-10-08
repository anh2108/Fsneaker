package com.example.fsneaker.controller;

import com.example.fsneaker.entity.ThuongHieu;
import com.example.fsneaker.entity.XuatXu;
import com.example.fsneaker.service.ThuongHieuService;
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
public class ThuongHieuController {

    @Autowired
    private ThuongHieuService thuongHieuService;

    @GetMapping("/qlthuonghieu")
    public String ThuongHieu(){
        return "templateadmin/qlthuonghieu.html";
    }

    @GetMapping("/qlthuonghieu")
    public String getALlXuatXu(Model model){
        List<ThuongHieu> listThuongHieu = thuongHieuService.getAllThuongHieu();
        model.addAttribute("listTHuongHieu", listThuongHieu);
        return "templateadmin/qlthuonghieu.html";
    }

    @RequestMapping("/add-thuongHieu")
    public String add(Model model){
        ThuongHieu thuongHieu = new ThuongHieu();
        model.addAttribute("thuongHieu", thuongHieu);
        return "templateadmin/qlthuonghieu.html/add-thuongHieu";
    }

    @PostMapping("/add-thuongHieu")
    public String save(@ModelAttribute("thuongHieu") ThuongHieu thuongHieu ){
        thuongHieuService.add(thuongHieu);
        return "templateadmin/qlthuonghieu.html/add-thuongHieu";
    }

    @GetMapping("/edit-thuongHieu")
    public String edit(Model model, @PathVariable("id") Integer id){
        ThuongHieu thuongHieu = thuongHieuService.findById(id);
        model.addAttribute("thuongHieu", thuongHieu);
        return "templateadmin/qlthuonghieu.html/edit-thuongHieu";
    }
}
