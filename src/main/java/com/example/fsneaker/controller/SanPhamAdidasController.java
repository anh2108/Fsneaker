package com.example.fsneaker.controller;

import com.example.fsneaker.service.KichThuocService;
import com.example.fsneaker.service.MauSacService;
import com.example.fsneaker.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SanPhamAdidasController {
    @Autowired
    private MauSacService mauSacService;
    @Autowired
    private KichThuocService kichThuocService;
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;
    @GetMapping("/san-pham-adidas")
    public String hienThiAdidas(@RequestParam(value="page",defaultValue = "0")int page, Model model){
        int pageSize = 12;
        List<Object[]> mauSacVoiSanPham = mauSacService.getMauSacWithSanPham();
        model.addAttribute("mauSacVoiSanPham",mauSacVoiSanPham);
        List<Object[]> kichThuocVoiSanPham = kichThuocService.getKichThuocVoiSanPham();
        model.addAttribute("kichThuocVoiSanPham",kichThuocVoiSanPham);
        Page<Object[]> tatCaSanPhamAdidas = sanPhamChiTietService.getThuongHieuTenThuongHieu(2, page, pageSize);
        model.addAttribute("tatCaSanPhamAdidas",tatCaSanPhamAdidas);
        return "templatekhachhang/san-pham-adidas";
    }
}
