package com.example.fsneaker.controller;


import com.example.fsneaker.entity.SanPham;
import com.example.fsneaker.entity.SanPhamChiTiet;
import com.example.fsneaker.service.DonHangChiTietService;
import com.example.fsneaker.service.KichThuocService;
import com.example.fsneaker.service.MauSacService;
import com.example.fsneaker.service.SanPhamChiTietService;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SanPhamNikeController {
    @Autowired
    private MauSacService mauSacService;
    @Autowired
    private KichThuocService kichThuocService;
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;
    @Autowired
    private DonHangChiTietService donHangChiTietService;
    @GetMapping("/san-pham-nike")
    public String hienThiSanPhamNike(@RequestParam(value = "page",defaultValue = "0")int page,@RequestParam(value = "color", required = false)String color,@RequestParam(value = "kichThuoc",required = false)String kichThuoc,@RequestParam(value = "minGia",required = false)Integer minGia,@RequestParam(value = "maxGia",required = false)Integer maxGia, Model model){
        int pageSize = 12;
        List<Object[]> mauSacVoiSanPham = mauSacService.getMauSacWithSanPham(1);
        model.addAttribute("mauSacVoiSanPham",mauSacVoiSanPham);
        List<Object[]> kichThuocVoiSanPham = kichThuocService.getKichThuocVoiSanPham(1);
        model.addAttribute("kichThuocVoiSanPham",kichThuocVoiSanPham);
         if(color != null && !color.isEmpty()){
            Page<Object[]> sanPhamTheoMauSac = sanPhamChiTietService.getThuongHieuAndMauSac(1,color,page,pageSize);
            model.addAttribute("sanPhamTheoMauSac",sanPhamTheoMauSac);
        }else if(kichThuoc != null && !kichThuoc.isEmpty()){
            Page<Object[]> sanPhamTheoKichThuoc = sanPhamChiTietService.getThuongHieuAndKichThuoc(1,kichThuoc,page,pageSize);
            model.addAttribute("sanPhamTheoKichThuoc",sanPhamTheoKichThuoc);
        }else if(minGia != null && maxGia != null) {
            Page<Object[]> sanPhamTheoGia = sanPhamChiTietService.getSanPhamTheoThuongHieuVaGia(1,minGia, maxGia, page, pageSize);
            model.addAttribute("sanPhamTheoGia", sanPhamTheoGia);
        }else{
            Page<Object[]> tatCaSanPhamNike = sanPhamChiTietService.getThuongHieuTenThuongHieu(1, page, pageSize);
            model.addAttribute("tatCaSanPhamNike", tatCaSanPhamNike);
        }
        return "templatekhachhang/san-pham-nike";
    }
    @GetMapping("/san-pham-sap-xep")
    public String hienThiSanPhamNikeSapXep(@RequestParam(value = "page",defaultValue = "0") int page,@RequestParam(value = "sortBy",required = false, defaultValue = "0") String sortBy, Model model){
        int pageSize = 12;
//        System.out.println("Received sortBy: " + sortBy);
//        if (sortBy != null && sortBy == 0) {
//            sortBy = null; // hoặc gán lại về giá trị mặc định nào đó nếu cần
//        }
        List<Object[]> mauSacVoiSanPham = mauSacService.getMauSacWithSanPham(1);
        model.addAttribute("mauSacVoiSanPham",mauSacVoiSanPham);
        List<Object[]> kichThuocVoiSanPham = kichThuocService.getKichThuocVoiSanPham(1);
        model.addAttribute("kichThuocVoiSanPham",kichThuocVoiSanPham);
        Page<Object[]> tatCaSanPhamNike;
        switch (sortBy){
            case "1" -> tatCaSanPhamNike = donHangChiTietService.getNikeByPopularity(1, page ,pageSize);
            case "2" -> tatCaSanPhamNike = sanPhamChiTietService.getNikeNyNewest(1, page,pageSize);
            case "3" -> tatCaSanPhamNike = sanPhamChiTietService.getNikeByPriceAsc(1, page, pageSize);
            case "4" -> tatCaSanPhamNike = sanPhamChiTietService.getNikeByPriceDesc(1, page, pageSize);
            case "5" -> tatCaSanPhamNike = sanPhamChiTietService.getNikeByName(1, page, pageSize);
            default -> tatCaSanPhamNike = sanPhamChiTietService.getThuongHieuTenThuongHieu(1, page, pageSize);
        };

        model.addAttribute("tatCaSanPhamNike",tatCaSanPhamNike);
        model.addAttribute("sortBy",sortBy);
        return "templatekhachhang/san-pham-nike";
    }
}
