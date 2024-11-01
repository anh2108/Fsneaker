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
        List<Object[]> sanPhamBanChayNike = donHangChiTietService.getTopSellingProductsByBrand(1, 10); // Lấy 10 sản phẩm bán chạy nhất
        model.addAttribute("sanPhamBanChayNike",sanPhamBanChayNike);
        List<Object[]> sanPhamBanChayAdidas = donHangChiTietService.getTopSellingProductsByBrand(2,10);
        model.addAttribute("sanPhamBanChayAdidas",sanPhamBanChayAdidas);
        List<Object[]> sanPhamBanChayAsics = donHangChiTietService.getTopSellingProductsByBrand(5, 10); // Lấy 10 sản phẩm bán chạy nhất
        model.addAttribute("sanPhamBanChayAsics",sanPhamBanChayAsics);
        List<Object[]> sanPhamBanChayPuma = donHangChiTietService.getTopSellingProductsByBrand( 3,10); // Lấy 10 sản phẩm bán chạy nhất
        model.addAttribute("sanPhamBanChayPuma",sanPhamBanChayPuma);
        List<Object[]> sanPhamBanChayNewBalance= donHangChiTietService.getTopSellingProductsByBrand(4,10); // Lấy 10 sản phẩm bán chạy nhất
        model.addAttribute("sanPhamBanChayNewBalance",sanPhamBanChayNewBalance);
        return "trangchu";
    }

}
