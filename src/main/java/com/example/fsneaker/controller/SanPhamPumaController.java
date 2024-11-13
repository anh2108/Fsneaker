package com.example.fsneaker.controller;

import com.example.fsneaker.entity.SanPhamChiTiet;
import com.example.fsneaker.repositories.DonHangChiTietRepo;
import com.example.fsneaker.repositories.SanPhamChiTietRepo;
import com.example.fsneaker.service.KichThuocService;
import com.example.fsneaker.service.MauSacService;
import com.example.fsneaker.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class SanPhamPumaController {
    @Autowired
    private MauSacService mauSacService;
    @Autowired
    private KichThuocService kichThuocService;
    @Autowired
    private SanPhamChiTietRepo sanPhamChiTietRepo;
    @Autowired
    DonHangChiTietRepo donHangChiTietRepo;
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @GetMapping("/san-pham-puma")
    public String hienThiPumaTimKiem(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "12") int pageSize,
            @RequestParam Optional<String> tenMauSac,
            @RequestParam Optional<String> tenKichThuoc,
            @RequestParam Optional<Double> fromGiaBan,
            @RequestParam Optional<Double> toGiaBan,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<Object[]> tenSanPhamVoiSanPham = sanPhamChiTietService.getNiekByTenSanPham(1);
        model.addAttribute("tenSanPhamVoiSanPham", tenSanPhamVoiSanPham);
        List<Object[]> tenSanPhamPumaVoiSanPham = sanPhamChiTietService.getNiekByTenSanPham(3);
        model.addAttribute("tenSanPhamPumaVoiSanPham", tenSanPhamPumaVoiSanPham);
        List<Object[]> tenSanPhamAdidasVoiSanPham = sanPhamChiTietService.getNiekByTenSanPham(2);
        model.addAttribute("tenSanPhamAdidasVoiSanPham",tenSanPhamAdidasVoiSanPham);
        List<Object[]> tenSanPhamNewBalanceVoiSanPham = sanPhamChiTietService.getNiekByTenSanPham(4);
        model.addAttribute("tenSanPhamNewBalanceVoiSanPham",tenSanPhamNewBalanceVoiSanPham);
        List<Object[]> tenSanPhamAsicsVoiSanPham = sanPhamChiTietService.getNiekByTenSanPham(5);
        model.addAttribute("tenSanPhamAsicsVoiSanPham", tenSanPhamAsicsVoiSanPham);
        List<Object[]> mauSacVoiSanPham = mauSacService.getMauSacWithSanPham(4);
        model.addAttribute("mauSacVoiSanPham", mauSacVoiSanPham);
        List<Object[]> kichThuocVoiSanPham = kichThuocService.getKichThuocVoiSanPham(4);
        model.addAttribute("kichThuocVoiSanPham", kichThuocVoiSanPham);

//        Page<Object[]> tatCaSanPhamPuma = sanPhamChiTietService.getThuongHieuTenThuongHieu(4, page, pageSize);
        Page<SanPhamChiTiet> tatCaSanPhamPuma = sanPhamChiTietRepo.findByThuongHieuPuma(
                4,
                tenMauSac.orElse(null),
                tenKichThuoc.orElse(null),
                fromGiaBan.orElse(null),
                toGiaBan.orElse(null),
                pageRequest) ;
        model.addAttribute("tatCaSanPhamPuma", tatCaSanPhamPuma);
        return "templatekhachhang/san-pham-puma";
    }

    @GetMapping("/san-pham-puma-sap-xep")
    public String hienThiSPumaSapXep(
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "12") int pageSize,
            @RequestParam(value = "sortBy",required = false, defaultValue = "0") String sortBy,
            Model model
    ){
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<Object[]> mauSacVoiSanPham = mauSacService.getMauSacWithSanPham(4);
        model.addAttribute("mauSacVoiSanPham", mauSacVoiSanPham);
        List<Object[]> kichThuocVoiSanPham = kichThuocService.getKichThuocVoiSanPham(4);
        model.addAttribute("kichThuocVoiSanPham", kichThuocVoiSanPham);

        Page<SanPhamChiTiet> sanPhamChiTietPuma = null;
        Page<Object[]> sanPhamPumaTotalQuantity = null;
        switch (sortBy){
            case "1" -> sanPhamPumaTotalQuantity = donHangChiTietRepo.findNikeByPopularity(4,pageRequest);
            case "2" -> sanPhamChiTietPuma = sanPhamChiTietRepo.findByPumaSortProductNew(4, pageRequest);
            case "3" -> sanPhamChiTietPuma = sanPhamChiTietRepo.findByPumaSortAsc(4, pageRequest);
            case "4" -> sanPhamChiTietPuma = sanPhamChiTietRepo.findByPumaSortDesc(4, pageRequest);
            case "5" -> sanPhamChiTietPuma = sanPhamChiTietRepo.findByPumaSortName(4,pageRequest);
            default -> sanPhamChiTietPuma = sanPhamChiTietRepo.findByPumaThuongHieu(4,pageRequest);
        }
        model.addAttribute("tatCaSanPhamPuma",sanPhamChiTietPuma);
        model.addAttribute("sortBy",sortBy);
        model.addAttribute("sanPhamPumaTotalQuantity",sanPhamPumaTotalQuantity);
        return "templatekhachhang/san-pham-puma";
    }
}
