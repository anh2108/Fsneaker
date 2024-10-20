package com.example.fsneaker.controller;

import com.example.fsneaker.entity.KhachHang;
import com.example.fsneaker.service.KhachHangService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @Controller
    @SessionAttributes("khachHang")
    public class KhachHangController {
        @Autowired
        private KhachHangService khachHangService;
        @GetMapping("/qlkhachhang")
        public String KhachHang(@RequestParam(value = "keyword",required = false)String keyword, @RequestParam(value = "page",defaultValue =  "0")int page, Model model){
            int pageSize = 8;
            Page<KhachHang> khachHangPage;
            if(keyword != null && !keyword.isEmpty()){
                khachHangPage = khachHangService.searchPaginated(keyword,page,pageSize);
                model.addAttribute("keyword",keyword);
            }else{
                khachHangPage = khachHangService.findPaginated(page,pageSize);
            }
            model.addAttribute("khachHangs",khachHangPage);
            return "templateadmin/qlkhachhang";
        }
        @GetMapping("/themkhachhang-form")
        public String addKhachHangForm(Model model){
            KhachHang khachHang = new KhachHang();
            model.addAttribute("khachHang", khachHang);
            return "templateadmin/themkhachhang";
        }
        @PostMapping("/themkhachhang")
        public String addKhachHang(@Valid @ModelAttribute("khachHang") KhachHang khachHang, BindingResult result){
            if(result.hasErrors()){
                return "templateadmin/themkhachhang";
            }
            khachHangService.themKH(khachHang);
            return "redirect:/qlkhachhang";
        }
        @GetMapping("/sua/{id}")
        public String suaKhachHangForm(@PathVariable("id")Integer id, Model model){
            KhachHang khachHang = khachHangService.getKhachHangById(id);
            model.addAttribute("khachHang", khachHang);
            return "templateadmin/suakhachhang";
        }
        // Lưu thôn tin khách hàng sau khi chỉnh sửa
        @PostMapping("/cap-nhat/{id}")
        public String updateKhachHang(@PathVariable("id") Integer id,@Valid @ModelAttribute("khachHang")KhachHang khachHang, BindingResult result){
            if(result.hasErrors()){
                return "templateadmin/suakhachhang";
            }
            //Lấy thông tin khách hàng cũ từ cơ sở dữ liệu theo id
            KhachHang khachHangCu = khachHangService.getKhachHangById(id);
            //Cập nhật thông tin mới từ form
            khachHangCu.setMaKhachHang(khachHang.getMaKhachHang());
            khachHangCu.setTenKhachHang(khachHang.getTenKhachHang());
            khachHangCu.setEmail(khachHang.getEmail());
            khachHangCu.setNgaySinh(khachHang.getNgaySinh());
            khachHangCu.setGioiTinh(khachHang.getGioiTinh());
            khachHangCu.setDiaChi(khachHang.getDiaChi());
            khachHangCu.setSoDienThoai(khachHang.getSoDienThoai());
            khachHangCu.setTrangThai(khachHang.getTrangThai());

            // Lưu lại thông tin đã được cập nhật
            khachHangService.themKH(khachHangCu);
            return "redirect:/qlkhachhang";
        }
        //Xử lý yêu cầu tìm kiếm khách hàng
//    @GetMapping("/tim-kiem-khach-hang")
//    public String timKiemKhachHang(@RequestParam("keyword") String keyword,Model model){
//        List<KhachHang> khachHangs = khachHangService.timKiemKhachHang(keyword);
//        model.addAttribute("khachHangs", khachHangs);
//        return "templateadmin/qlkhachhang";
//    }
        //Chọn khách hàng va quay lại trang bán hàng
        @GetMapping("/select")
        public String selectKhachHang(@RequestParam("id")Integer id, Model model){
            KhachHang khachHang = khachHangService.getKhachHangById(id);
            if(khachHang != null){
                model.addAttribute("khachHang", khachHang);
            }else{
                model.addAttribute("error", "Khách hàng không tồn tại");
            }
            return "redirect:/don-hang/chi-tiet";
        }
    }

