package com.example.fsneaker.controller;

import com.example.fsneaker.entity.NhanVien;
import com.example.fsneaker.repositories.NhanVienRepo;
import com.example.fsneaker.service.NhanVienService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class NhanVienController {
    @Autowired
    public NhanVienRepo nhanVienRepo;

    @Autowired
    public NhanVienService nhanVienService;

    @GetMapping("create")
    public String create(Model model){
        model.addAttribute("nhanvien", new NhanVien());
        return "templateadmin/addNhanVien";
    }

    @GetMapping("/qlnhanvien")
    public String index( Model model,@RequestParam(defaultValue = "0") int page) {

        int pageSize = 5; // Số lượng bản ghi mỗi trang
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending()); // Sắp xếp theo id giảm dần

        Page<NhanVien> nhanVienPage = nhanVienRepo.findAll(pageable);

        // Thêm dữ liệu phân trang vào model
        model.addAttribute("nhanvien", nhanVienPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", nhanVienPage.getTotalPages());
        return "templateadmin/qlnhanvien";
    }


    @PostMapping("add")
    public String add(Model model, @Valid @ModelAttribute("nhanvien") NhanVien nhanVien, BindingResult validate) {
        if (validate.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError e : validate.getFieldErrors()) {
                errors.put(e.getField(), e.getDefaultMessage());
            }

            model.addAttribute("nhanvien", nhanVien);
            model.addAttribute("errors", errors);
            return "templateadmin/addNhanVien";
        }

        nhanVienRepo.save(nhanVien);
        return "redirect:/qlnhanvien";
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        NhanVien nhanVien = this.nhanVienRepo.findById(id).get();
        model.addAttribute("nhanvien", nhanVien);
        return "templateadmin/updateNhanVien";
    }


    @PostMapping("/update/{id}")
    public String updateNhanVien(@PathVariable Integer id, @ModelAttribute NhanVien nhanVien) {
        NhanVien existingNhanVien = nhanVienRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nhân viên không tồn tại"));

        existingNhanVien.setMaNhanVien(nhanVien.getMaNhanVien());
        existingNhanVien.setTenNhanVien(nhanVien.getTenNhanVien());
        existingNhanVien.setEmail(nhanVien.getEmail());
        existingNhanVien.setSoDienThoai(nhanVien.getSoDienThoai());
        existingNhanVien.setNgaySinh(nhanVien.getNgaySinh());
        existingNhanVien.setGioiTinh(nhanVien.getGioiTinh());
        existingNhanVien.setDiaChi(nhanVien.getDiaChi());
        existingNhanVien.setVaiTro(nhanVien.getVaiTro());
        existingNhanVien.setTaiKhoan(nhanVien.getTaiKhoan());
        existingNhanVien.setMatKhau(nhanVien.getMatKhau());
        existingNhanVien.setCccd(nhanVien.getCccd());
        existingNhanVien.setTrangThai(nhanVien.getTrangThai());

        nhanVienRepo.save(existingNhanVien);

        return "redirect:/qlnhanvien";
    }

    @GetMapping("search")
    public String search(@RequestParam("keyword") String keyword,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         Model model) {

        int pageSize = 5; // Số lượng bản ghi mỗi trang
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending()); // Sắp xếp theo id tăng dần

        Page<NhanVien> nhanVienPage = nhanVienService.searchNhanVien(keyword,pageable);

        // Thêm dữ liệu phân trang vào model
        model.addAttribute("nhanvien", nhanVienPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", nhanVienPage.getTotalPages());
        return "templateadmin/qlnhanvien";
    }

}
