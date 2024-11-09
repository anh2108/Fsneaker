package com.example.fsneaker.controller;

import com.example.fsneaker.entity.SanPhamChiTiet;
import com.example.fsneaker.repositories.DonHangChiTietRepo;
import com.example.fsneaker.repositories.SanPhamChiTietRepo;
import com.example.fsneaker.service.DonHangChiTietService;
import com.example.fsneaker.service.KichThuocService;
import com.example.fsneaker.service.MauSacService;
import com.example.fsneaker.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class SanPhamAsicsController {
    @Autowired
    private MauSacService mauSacService;
    @Autowired
    private KichThuocService kichThuocService;
    @Autowired
    private SanPhamChiTietRepo sanPhamChiTietRepo;
    @Autowired
    DonHangChiTietRepo donHangChiTietRepo;
    @GetMapping("/san-pham-asics")
    public String hienThiAsicsTimKiem(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "12") int pageSize,
            @RequestParam Optional<String> tenMauSac,
            @RequestParam Optional<String> tenKichThuoc,
            @RequestParam Optional<Double> fromGiaBan,
            @RequestParam Optional<Double> toGiaBan,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<Object[]> mauSacVoiSanPham = mauSacService.getMauSacWithSanPham(3);
        model.addAttribute("mauSacVoiSanPham", mauSacVoiSanPham);
        List<Object[]> kichThuocVoiSanPham = kichThuocService.getKichThuocVoiSanPham(3);
        model.addAttribute("kichThuocVoiSanPham", kichThuocVoiSanPham);

//        Page<Object[]> tatCaSanPhamPuma = sanPhamChiTietService.getThuongHieuTenThuongHieu(4, page, pageSize);
        Page<SanPhamChiTiet> tatCaSanPhamAsics = sanPhamChiTietRepo.findByThuongHieuPuma(
                3,
                tenMauSac.orElse(null),
                tenKichThuoc.orElse(null),
                fromGiaBan.orElse(null),
                toGiaBan.orElse(null),
                pageRequest) ;
        model.addAttribute("tatCaSanPhamAsics", tatCaSanPhamAsics);
        return "templatekhachhang/san-pham-asics";
    }

    @GetMapping("/san-pham-asics-sap-xep")
    public String hienThiSPumaSapXep(
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "12") int pageSize,
            @RequestParam(value = "sortBy",required = false, defaultValue = "0") String sortBy,
            Model model
    ){
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<Object[]> mauSacVoiSanPham = mauSacService.getMauSacWithSanPham(3);
        model.addAttribute("mauSacVoiSanPham", mauSacVoiSanPham);
        List<Object[]> kichThuocVoiSanPham = kichThuocService.getKichThuocVoiSanPham(3);
        model.addAttribute("kichThuocVoiSanPham", kichThuocVoiSanPham);

        Page<SanPhamChiTiet> sanPhamChiTietAsics = null;
        Page<Object[]> sanPhamAcicsTotalQuantity = null;
        switch (sortBy){
            case "1" -> sanPhamAcicsTotalQuantity = donHangChiTietRepo.findNikeByPopularity(3,pageRequest);
            case "2" -> sanPhamChiTietAsics = sanPhamChiTietRepo.findByPumaSortProductNew(3, pageRequest);
            case "3" -> sanPhamChiTietAsics = sanPhamChiTietRepo.findByPumaSortAsc(3, pageRequest);
            case "4" -> sanPhamChiTietAsics = sanPhamChiTietRepo.findByPumaSortDesc(3, pageRequest);
            case "5" -> sanPhamChiTietAsics = sanPhamChiTietRepo.findByPumaSortName(3,pageRequest);
            default -> sanPhamChiTietAsics = sanPhamChiTietRepo.findByPumaThuongHieu(3,pageRequest);
        }
        model.addAttribute("tatCaSanPhamAsics",sanPhamChiTietAsics);
        model.addAttribute("sortBy",sortBy);
        model.addAttribute("sanPhamAsicsTotalQuantity",sanPhamAcicsTotalQuantity);
        return "templatekhachhang/san-pham-asics";
    }
}
