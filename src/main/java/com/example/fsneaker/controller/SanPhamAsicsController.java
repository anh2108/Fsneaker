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
public class SanPhamAsicsController {
    @Autowired
    private MauSacService mauSacService;
    @Autowired
    private KichThuocService kichThuocService;
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;
    @GetMapping("/san-pham-asics")
    public String hienThiAsics(@RequestParam(value = "page",defaultValue = "0")int page, Model model){
        int pageSize = 12;
        List<Object[]> mauSacVoiSanPham = mauSacService.getMauSacWithSanPham();
        model.addAttribute("mauSacVoiSanPham",mauSacVoiSanPham);
        List<Object[]> kichThuocVoiSanPham = kichThuocService.getKichThuocVoiSanPham();
        model.addAttribute("kichThuocVoiSanPham",kichThuocVoiSanPham);
        Page<Object[]> tatCaSanPhamAsics = sanPhamChiTietService.getThuongHieuTenThuongHieu(5,page, pageSize);
        model.addAttribute("tatCaSanPhamAsics",tatCaSanPhamAsics);
        return "templatekhachhang/san-pham-asics";
    }
}
