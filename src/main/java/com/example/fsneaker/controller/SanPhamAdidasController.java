package com.example.fsneaker.controller;

import com.example.fsneaker.service.DonHangChiTietService;
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
    @Autowired
    private DonHangChiTietService donHangChiTietService;
    @GetMapping("/san-pham-adidas")
    public String hienThiAdidas(@RequestParam(value = "page",defaultValue = "0")int page,@RequestParam(value = "color", required = false)String color,@RequestParam(value = "kichThuoc",required = false)String kichThuoc,@RequestParam(value = "minGia",required = false)Integer minGia,@RequestParam(value = "maxGia",required = false)Integer maxGia, Model model){
        int pageSize = 12;
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
        List<Object[]> mauSacVoiSanPham = mauSacService.getMauSacWithSanPham(2);
        model.addAttribute("mauSacVoiSanPham",mauSacVoiSanPham);
        List<Object[]> kichThuocVoiSanPham = kichThuocService.getKichThuocVoiSanPham(2);
        model.addAttribute("kichThuocVoiSanPham",kichThuocVoiSanPham);
        if(color != null && !color.isEmpty()){
            Page<Object[]> sanPhamTheoMauSac = sanPhamChiTietService.getThuongHieuAndMauSac(2,color,page,pageSize);
            model.addAttribute("sanPhamTheoMauSac",sanPhamTheoMauSac);
        }else if(kichThuoc != null && !kichThuoc.isEmpty()){
            Page<Object[]> sanPhamTheoKichThuoc = sanPhamChiTietService.getThuongHieuAndKichThuoc(2,kichThuoc,page,pageSize);
            model.addAttribute("sanPhamTheoKichThuoc",sanPhamTheoKichThuoc);
        }else if(minGia != null && maxGia != null) {
            Page<Object[]> sanPhamTheoGia = sanPhamChiTietService.getSanPhamTheoThuongHieuVaGia(2,minGia, maxGia, page, pageSize);
            model.addAttribute("sanPhamTheoGia", sanPhamTheoGia);
        }else{
            Page<Object[]> tatCaSanPhamAdidas = sanPhamChiTietService.getThuongHieuTenThuongHieu(2, page, pageSize);
            model.addAttribute("tatCaSanPhamAdidas", tatCaSanPhamAdidas);
        }
        return "templatekhachhang/san-pham-adidas";
    }

    @GetMapping("/san-pham-sap-xep-adidas")
    public String hienThiSanPhamAdidasSapXep(@RequestParam(value = "page",defaultValue = "0") int page,@RequestParam(value = "sortBy",required = false, defaultValue = "0") String sortBy, Model model){
        int pageSize = 12;
//        System.out.println("Received sortBy: " + sortBy);
//        if (sortBy != null && sortBy == 0) {
//            sortBy = null; // hoặc gán lại về giá trị mặc định nào đó nếu cần
//        }
        List<Object[]> mauSacVoiSanPham = mauSacService.getMauSacWithSanPham(2);
        model.addAttribute("mauSacVoiSanPham",mauSacVoiSanPham);
        List<Object[]> kichThuocVoiSanPham = kichThuocService.getKichThuocVoiSanPham(2);
        model.addAttribute("kichThuocVoiSanPham",kichThuocVoiSanPham);
        Page<Object[]> tatCaSanPhamAdidas;
        switch (sortBy){
            case "1" -> tatCaSanPhamAdidas = donHangChiTietService.getNikeByPopularity(2, page ,pageSize);
            case "2" -> tatCaSanPhamAdidas = sanPhamChiTietService.getAdidasNyNewest(2, page,pageSize);
            case "3" -> tatCaSanPhamAdidas = sanPhamChiTietService.getAdidasByPriceAsc(2, page, pageSize);
            case "4" -> tatCaSanPhamAdidas = sanPhamChiTietService.getAdidasByPriceDesc(2, page, pageSize);
            case "5" -> tatCaSanPhamAdidas = sanPhamChiTietService.getAdidasByName(2, page, pageSize);
            default -> tatCaSanPhamAdidas = sanPhamChiTietService.getThuongHieuTenThuongHieu(2, page, pageSize);
        };

        model.addAttribute("tatCaSanPhamAdidas",tatCaSanPhamAdidas);
        model.addAttribute("sortBy",sortBy);
        return "templatekhachhang/san-pham-adidas";
    }
}
