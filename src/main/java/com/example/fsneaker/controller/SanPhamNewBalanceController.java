package com.example.fsneaker.controller;

import com.example.fsneaker.entity.SanPhamChiTiet;
import com.example.fsneaker.service.DonHangChiTietService;
import com.example.fsneaker.service.KichThuocService;
import com.example.fsneaker.service.MauSacService;
import com.example.fsneaker.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SanPhamNewBalanceController {
    @Autowired
    private MauSacService mauSacService;
    @Autowired
    private KichThuocService kichThuocService;
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;
    @Autowired
    private DonHangChiTietService donHangChiTietService;
    @GetMapping("/san-pham-newbalance")
    public String hienThiNewBalance(@RequestParam(value = "page",defaultValue = "0")int page,
                                    Model model,
                                    @RequestParam(value = "tenSanPham", required = false) String tenSanPham,
                                    @RequestParam(value = "color", required = false) String color,
                                    @RequestParam(value = "kichThuoc", required = false) String kichThuoc,
                                    @RequestParam(value = "minGia", required = false) Integer minGia,
                                    @RequestParam(value = "maxGia", required = false) Integer maxGia

    ){
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
        List<Object[]> mauSacVoiSanPham = mauSacService.getMauSacWithSanPham(4);
        model.addAttribute("mauSacVoiSanPham",mauSacVoiSanPham);
        List<Object[]> kichThuocVoiSanPham = kichThuocService.getKichThuocVoiSanPham(4);
        model.addAttribute("kichThuocVoiSanPham",kichThuocVoiSanPham);


        if(color != null && !color.isEmpty()){
            Page<Object[]> sanPhamTheoMauSac = sanPhamChiTietService.getThuongHieuAndMauSac(4,color,page,pageSize);
            model.addAttribute("sanPhamTheoMauSac",sanPhamTheoMauSac);
        }
        else if(kichThuoc != null && !kichThuoc.isEmpty()){
            Page<Object[]> sanPhamTheoKichThuoc = sanPhamChiTietService.getThuongHieuAndKichThuoc(4,kichThuoc,page,pageSize);
            model.addAttribute("sanPhamTheoKichThuoc",sanPhamTheoKichThuoc);
        }
        else if(minGia != null && maxGia != null){
            Page<Object[]> sanPhamTheoGia = sanPhamChiTietService.getSanPhamTheoThuongHieuVaGia(4,minGia, maxGia, page, pageSize);
            model.addAttribute("sanPhamTheoGia", sanPhamTheoGia);
        } else if (tenSanPham !=null && !tenSanPham.isEmpty()) {
            Page<Object[]> sanPhamTheoTenSanPham = sanPhamChiTietService.getNikeByTenSanPhamAndThuongHieu(4,tenSanPham, page,pageSize);
            model.addAttribute("sanPhamTheoTenSanPham", sanPhamTheoTenSanPham);
        }
        else {
            Page<Object[]> tatCaSanPhamNewBalance = sanPhamChiTietService.getThuongHieuTenThuongHieu(4,page, pageSize);
            model.addAttribute("tatCaSanPhamNewBalance",tatCaSanPhamNewBalance);
        }

        return "templatekhachhang/san-pham-newbalance";
    }

    @GetMapping("san-pham-sapxep")
    public String hiemthiSanPhamNewBlanceSapXep(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                @RequestParam(value = "sortBy", required = false, defaultValue = "0") String sortBy,
                                                Model model
    ){
        int pageSize = 12;
        List<Object[]> mauSacVoiSanPham = mauSacService.getMauSacWithSanPham(4);
        model.addAttribute("mauSacVoiSanPham",mauSacVoiSanPham);
        List<Object[]> kichThuocVoiSanPham = kichThuocService.getKichThuocVoiSanPham(4);
        model.addAttribute("kichThuocVoiSanPham",kichThuocVoiSanPham);
        Page<Object[]> tatCaSanPhamNewBlance;
        switch (sortBy){
            case "1" -> tatCaSanPhamNewBlance = donHangChiTietService.getNikeByPopularity(4, page ,pageSize);
            case "2" -> tatCaSanPhamNewBlance = sanPhamChiTietService.getNikeNyNewest(4, page,pageSize);
            case "3" -> tatCaSanPhamNewBlance = sanPhamChiTietService.getNikeByPriceAsc(4, page, pageSize);
            case "4" -> tatCaSanPhamNewBlance = sanPhamChiTietService.getNikeByPriceDesc(4, page, pageSize);
            case "5" -> tatCaSanPhamNewBlance = sanPhamChiTietService.getNikeByName(4, page, pageSize);
            default -> tatCaSanPhamNewBlance = sanPhamChiTietService.getThuongHieuTenThuongHieu(4, page, pageSize);
        };
        model.addAttribute("tatCaSanPhamNewBalance",tatCaSanPhamNewBlance);
        model.addAttribute("sortBy",sortBy);
        return "templatekhachhang/san-pham-newbalance";
    }

    @GetMapping("/xem-chi-tiet-newbalance/{id}")
        public String xemChiTiet(@PathVariable("id") Integer id,Model model){
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
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.getSanPhamChiTietById(id);
            model.addAttribute("sanPhamChiTiet", sanPhamChiTiet);
            List<SanPhamChiTiet> sanPhamChiTiets = sanPhamChiTietService.getSanPhamChiTietBySanPhamIdAndMauSacId(sanPhamChiTiet.getSanPham().getId(), sanPhamChiTiet.getMauSac().getId());
            model.addAttribute("sanPhamChiTiets", sanPhamChiTiets);
            return "templatekhachhang/chi-tiet-san-pham";
        }
}