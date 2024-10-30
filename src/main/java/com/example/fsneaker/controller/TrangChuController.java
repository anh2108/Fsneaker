package com.example.fsneaker.controller;

import com.example.fsneaker.service.DonHangChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TrangChuController {
    @Autowired
    private DonHangChiTietService donHangChiTietService;
    @GetMapping("/trang-chu")
    public String hienThiTrangChu(Model model){
        List<Object[]> sanPhamBanChayPuma = donHangChiTietService.getTopSellingProductsByBrand("Puma", 10); // Lấy 10 sản phẩm bán chạy nhất
        model.addAttribute("sanPhamBanChayPuma",sanPhamBanChayPuma);

        List<Object[]> sanPhamBanChayAdidas = donHangChiTietService.getTopSellingProductsByBrand("Adidas", 10); // Lấy 10 sản phẩm bán chạy nhất
        model.addAttribute("sanPhamBanChayAdidas",sanPhamBanChayAdidas);
        return "trangchu";
    }


    @GetMapping("/trang-chu1")
    public String hienThiTrangChu1(Model model){
        List<Object[]> sanPhamBanChayAsics = donHangChiTietService.getTopSellingProductsByBrand("Asics", 10); // Lấy 10 sản phẩm bán chạy nhất
        model.addAttribute("sanPhamBanChayAsics",sanPhamBanChayAsics);
        return "trangchu";
    }

    @GetMapping("/trang-chu2")
    public String hienThiTrangChu2(Model model){
        List<Object[]> sanPhamBanChayNike = donHangChiTietService.getTopSellingProductsByBrand("Nike", 10); // Lấy 10 sản phẩm bán chạy nhất
        model.addAttribute("sanPhamBanChayNike",sanPhamBanChayNike);
        return "trangchu";
    }

}
