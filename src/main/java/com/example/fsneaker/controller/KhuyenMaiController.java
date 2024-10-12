package com.example.fsneaker.controller;

import com.example.fsneaker.entity.KhuyenMai;
import com.example.fsneaker.repositories.KhuyenMaiRepo;
import com.example.fsneaker.service.KhuyenMaiService;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("khuyenmai")
public class KhuyenMaiController {
    @Autowired
    public KhuyenMaiRepo khuyenMaiRepository;

    @Autowired
    public KhuyenMaiService khuyenMaiService;

    @GetMapping("create")
    public String create(Model model){
        model.addAttribute("khuyenmai", new KhuyenMai());
        return "templateadmin/them-khuyen-mai";
    }

    @GetMapping("hienthi")
    public String index( Model model,@RequestParam(defaultValue = "0") int page) {

        int pageSize = 5; // Số lượng bản ghi mỗi trang
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending()); // Sắp xếp theo id giảm dần

        Page<KhuyenMai> khuyenMaiPage = khuyenMaiRepository.findAll(pageable);

        // Thêm dữ liệu phân trang vào model
        model.addAttribute("khuyenmai", khuyenMaiPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", khuyenMaiPage.getTotalPages());
        return "templateadmin/qlkhuyenmai";
    }

    @PostMapping("add")
    public String add( Model model, @Valid KhuyenMai khuyenMai , BindingResult validate){
        if(validate.hasErrors()){
            //lỗi
            Map<String, String> errors = new HashMap<>();
            for (FieldError e: validate.getFieldErrors()) {
                errors.put(e.getField(), e.getDefaultMessage());
            }

            model.addAttribute("khuyenmai",khuyenMai);
            model.addAttribute("errors",errors);
            return "templateadmin/them-khuyen-mai";
        }

        // Kiểm tra logic: ngày bắt đầu không được trùng với ngày kết thúc
        if (khuyenMai.getNgayBatDau().equals(khuyenMai.getNgayKetThuc())) {
            model.addAttribute("khuyenmai", khuyenMai);
            model.addAttribute("errorNgay", "Ngày bắt đầu và ngày kết thúc không được trùng nhau!");
            return "templateadmin/them-khuyen-mai";
        }

// Kiểm tra mã khuyến mãi có trùng không
        KhuyenMai existingKhuyenMai = khuyenMaiRepository.findByMaKhuyenMai(khuyenMai.getMaKhuyenMai());
        if (existingKhuyenMai != null) {
            model.addAttribute("khuyenmai", khuyenMai);
            model.addAttribute("errorMaKhuyenMai", "Mã khuyến mãi đã tồn tại, vui lòng chọn mã khác!");
            return "templateadmin/them-khuyen-mai";
        }

        khuyenMaiRepository.save(khuyenMai);
        return "redirect:/khuyenmai/hienthi";
    }




    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        KhuyenMai khuyenMai = this.khuyenMaiRepository.findById(id).get();
        model.addAttribute("khuyenmai", khuyenMai);
        return "templateadmin/sua-khuyen-mai";
    }

    @PostMapping("/update/{id}")
    public String updateKhuyenMai(@PathVariable Integer id, @ModelAttribute KhuyenMai km) {
        KhuyenMai existingKhuyenMai = khuyenMaiRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khuyến mãi không tồn tại"));

        existingKhuyenMai.setMaKhuyenMai(km.getMaKhuyenMai());
        existingKhuyenMai.setTenKhuyenMai(km.getTenKhuyenMai());
        existingKhuyenMai.setLoaiKhuyenMai(km.getLoaiKhuyenMai());
        existingKhuyenMai.setMoTa(km.getMoTa());
        existingKhuyenMai.setGiaTri(km.getGiaTri());
        existingKhuyenMai.setNgayBatDau(km.getNgayBatDau());
        existingKhuyenMai.setNgayKetThuc(km.getNgayKetThuc());
        existingKhuyenMai.setTrangThai(km.getTrangThai());

        khuyenMaiRepository.save(existingKhuyenMai);

        return "redirect:/khuyenmai/hienthi";
    }
    // xử lí yêu cầu tìm kiếm khách hàng
    @GetMapping("search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        List<KhuyenMai> km = khuyenMaiService.searchKhuyenMai(keyword);
        model.addAttribute("khuyenmai", km);
        return "templateadmin/qlkhuyenmai";
    }
}
