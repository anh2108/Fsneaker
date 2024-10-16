package com.example.fsneaker.controller;

import com.example.fsneaker.entity.NhanVien;
import com.example.fsneaker.entity.Voucher;
import com.example.fsneaker.repositories.NhanVienRepo;
import com.example.fsneaker.repositories.VoucherRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootApplication
@EnableScheduling
@Component
@Controller

public class VoucherController {
    @Autowired
    VoucherRepo voucherRepo;
    @Autowired
    NhanVienRepo nhanVienRepo;


    @GetMapping("/qlvoucher")
    public String Voucher(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int pageNo,
            @RequestParam(name = "limit", defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam Optional<LocalDate> ngayBatDau,
            @RequestParam Optional<LocalDate> ngayKetThuc,
            @RequestParam Optional<Integer>  trangThai
    ) {
        String s = "%" + keyword + "%";
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Page<Voucher> list = this.voucherRepo.searchPage(
                s,
                ngayBatDau.orElse(null),
                ngayKetThuc.orElse(null),
                trangThai.orElse(null),
                pageRequest);
        for (Voucher voucher : list) {
            voucher.setTrangThai(voucher.getTrangThai());
            voucherRepo.save(voucher);
        }
        model.addAttribute("data", list);

        List<NhanVien> listNV = this.nhanVienRepo.findAll();
        model.addAttribute("dataNV", listNV);
        return "templateadmin/voucher/qlvoucher.html";
    }

    @GetMapping("/qlvoucher/create")
    public String create(Model model) {
        List<NhanVien> listNV = this.nhanVienRepo.findAll();
        model.addAttribute("dataNV", listNV);
        return "templateadmin/voucher/add.html";
    }

    @GetMapping("/qlvoucher-edit/{id}")
    public String edit(
            @PathVariable("id") Voucher voucher,
            Model model)
    {
        List<NhanVien> listNV = this.nhanVienRepo.findAll();
        model.addAttribute("dataNV", listNV);

        model.addAttribute("data", voucher);
        return "templateadmin/voucher/edit.html";
    }

    @PostMapping("/qlvoucher-update/{id}")
    public String update(
            @Valid Voucher voucher,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            List<FieldError> listErrors = result.getFieldErrors();
            Map<String, String> errors = new HashMap<>();

            for (FieldError fe : listErrors) {
                errors.put(fe.getField(), fe.getDefaultMessage());
            }
            List<NhanVien> listNV = this.nhanVienRepo.findAll();
            model.addAttribute("dataNV", listNV);
            model.addAttribute("errors", errors);
            model.addAttribute("data", voucher);
            return "templateadmin/voucher/edit.html";
        }
        this.voucherRepo.save(voucher);
        return "redirect:/qlvoucher";
    }

    @PostMapping("/qlvoucher/add")
    public String add(
            @Valid Voucher voucher,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            List<FieldError> listErrors = result.getFieldErrors();
            Map<String, String> errors = new HashMap<>();

            for (FieldError fe : listErrors) {
                errors.put(fe.getField(), fe.getDefaultMessage());
            }
            List<NhanVien> listNV = this.nhanVienRepo.findAll();
            model.addAttribute("dataNV", listNV);
            model.addAttribute("errors", errors);
            model.addAttribute("data", voucher);
            return "templateadmin/voucher/add.html";
        }
        this.voucherRepo.save(voucher);
        return "redirect:/qlvoucher";
    }

    @PostMapping("/saveVoucher")
    public String saveVoucher(@ModelAttribute("voucher") Voucher voucher) {
        // Cập nhật trạng thái trước khi lưu
        voucher.setTrangThai(voucher.getTrangThai());

        voucherRepo.save(voucher);
        return "redirect:/qlvoucher";
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateVoucherStatus() {
        LocalDate today = LocalDate.now();

        List<Voucher> upcomingVouchers = voucherRepo.findByNgayBatDauAfter(today);
        for (Voucher voucher : upcomingVouchers) {
            voucher.setTrangThai(2); // Sắp diễn ra
            voucherRepo.save(voucher);
        }

        List<Voucher> ongoingVouchers = voucherRepo.findByNgayBatDauLessThanEqualAndNgayKetThucGreaterThanEqual(today, today);
        for (Voucher voucher : ongoingVouchers) {
            voucher.setTrangThai(1); // Đang diễn ra
            voucherRepo.save(voucher);
        }

        List<Voucher> expiredVouchers = voucherRepo.findByNgayKetThucBefore(today);
        for (Voucher voucher : expiredVouchers) {
            voucher.setTrangThai(0); // Ngừng hoạt động
            voucherRepo.save(voucher);
        }

        System.out.println("Cập nhật trạng thái voucher thành công!");
    }

}
