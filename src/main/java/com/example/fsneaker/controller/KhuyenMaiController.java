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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class KhuyenMaiController {
    @Autowired
    public KhuyenMaiRepo khuyenMaiRepository;

    @Autowired
    public KhuyenMaiService khuyenMaiService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");


    @GetMapping("create")
    public String create(Model model){
        model.addAttribute("khuyenmai", new KhuyenMai());
        return "templateadmin/them-khuyen-mai";
    }

    @GetMapping("/qlkhuyenmai")
    public String index( Model model,@RequestParam(defaultValue = "0") int page) {

        int pageSize = 5; // Số lượng bản ghi mỗi trang
        Pageable pageable = PageRequest.of(page, pageSize); // Sắp xếp theo id giảm dần

        Page<KhuyenMai> khuyenMaiPage = khuyenMaiRepository.findAllWithSorting(pageable);



        // Thêm dữ liệu phân trang vào model
        model.addAttribute("khuyenmai", khuyenMaiPage);
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


        // kiểm tra điều kiệu của mã khi thêm mới
        KhuyenMai existingKhuyenMai = khuyenMaiRepository.findByMaKhuyenMai(khuyenMai.getMaKhuyenMai());
        if (existingKhuyenMai != null) {
            // Nếu mã khuyến mãi đã tồn tại, thông báo lỗi
            model.addAttribute("khuyenmai", khuyenMai);
            model.addAttribute("errorMaKhuyenMai", "Mã khuyến mãi đã tồn tại, vui lòng chọn mã khác!");
            return "templateadmin/them-khuyen-mai";
        }



        // Kiểm tra giá trị khuyến mãi
        if (khuyenMai.getLoaiKhuyenMai().equalsIgnoreCase("Giảm giá %")) {
            // Kiểm tra giá trị phần trăm không được vượt quá 100%
            if (khuyenMai.getGiaTri() < 1 || khuyenMai.getGiaTri() > 100) {
                model.addAttribute("khuyenmai", khuyenMai);
                model.addAttribute("errorGiaTri", "Giá trị phần trăm phải từ 1 đến 100%");
                return "templateadmin/them-khuyen-mai"; // Trả về form với thông báo lỗi
            }

            // Kiểm tra nếu đơn tối thiểu ít hơn giá trị giảm theo %
            if (khuyenMai.getDonToiThieu() <= khuyenMai.getGiaTri()) {
                model.addAttribute("khuyenmai", khuyenMai);
                model.addAttribute("errorDonToiThieu", "Đơn tối thiểu phải lớn hơn giá trị giảm.");
                return "templateadmin/them-khuyen-mai";
            }

        } else if (khuyenMai.getLoaiKhuyenMai().equalsIgnoreCase("Giảm giá số tiền")) {
            // Kiểm tra giá trị tiền phải lớn hơn 0
            if (khuyenMai.getGiaTri() < 0) {
                model.addAttribute("khuyenmai", khuyenMai);
                model.addAttribute("errorGiaTri", "Giá trị giảm phải lớn hơn 0.");
                return "templateadmin/them-khuyen-mai"; // Trả về form với thông báo lỗi
            }

            // Kiểm tra giá trị tiền phải nhỏ hơn hoặc bằng đơn tối thiểu
            if (khuyenMai.getGiaTri() > khuyenMai.getDonToiThieu()) {
                model.addAttribute("khuyenmai", khuyenMai);
                model.addAttribute("errorGiaTri", "Giá trị giảm phải nhỏ hơn hoặc bằng đơn tối thiểu.");
                return "templateadmin/them-khuyen-mai"; // Trả về form với thông báo lỗi
            }
        }

        // Kiểm tra logic: ngày bắt đầu không được trùng với ngày kết thúc
        if (khuyenMai.getNgayBatDau().equals(khuyenMai.getNgayKetThuc())) {
            model.addAttribute("khuyenmai", khuyenMai);
            model.addAttribute("errorNgay", "Ngày bắt đầu và ngày kết thúc không được trùng nhau!");
            return "templateadmin/them-khuyen-mai";
        }

        khuyenMaiRepository.save(khuyenMai);
        return "redirect:/qlkhuyenmai";
    }




    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        KhuyenMai khuyenMai = this.khuyenMaiRepository.findById(id).get();
        model.addAttribute("khuyenmai", khuyenMai);
        return "templateadmin/sua-khuyen-mai";
    }

    @PostMapping("/update/{id}")
    public String updateKhuyenMai(@PathVariable Integer id, @Valid @ModelAttribute KhuyenMai km, BindingResult validate, Model model) {
        // Tìm kiếm khuyến mãi hiện tại theo ID
        KhuyenMai existingKhuyenMai = khuyenMaiRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khuyến mãi không tồn tại"));

        // Kiểm tra lỗi từ validate
        if (validate.hasErrors()) {
            // Lưu lại thông tin khuyến mãi cũ để hiển thị lại trong form
            model.addAttribute("khuyenmai", km);
            // Chuyển đổi lỗi thành map để hiển thị
            Map<String, String> errors = new HashMap<>();
            for (FieldError e : validate.getFieldErrors()) {
                errors.put(e.getField(), e.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            return "templateadmin/sua-khuyen-mai"; // Trả về form với thông báo lỗi
        }



        // Kiểm tra điều kiện của mã khi cập nhật
        if (!existingKhuyenMai.getMaKhuyenMai().equals(km.getMaKhuyenMai())) {
            KhuyenMai existingByMa = khuyenMaiRepository.findByMaKhuyenMai(km.getMaKhuyenMai());
            if (existingByMa != null) {
                model.addAttribute("khuyenmai", km);
                model.addAttribute("errorMaKhuyenMai", "Mã khuyến mãi đã tồn tại, vui lòng chọn mã khác!");
                return "templateadmin/sua-khuyen-mai"; // Trả về form với thông báo lỗi
            }
        }

        // Kiểm tra giá trị khuyến mãi
        if (km.getLoaiKhuyenMai().equalsIgnoreCase("Giảm giá %")) {
            // Kiểm tra giá trị phần trăm không được vượt quá 100%
            if (km.getGiaTri() < 1 || km.getGiaTri() > 100) {
                model.addAttribute("khuyenmai", km);
                model.addAttribute("errorGiaTri", "Giá trị phần trăm phải từ 1 đến 100%");
                return "templateadmin/sua-khuyen-mai"; // Trả về form với thông báo lỗi
            }

            // Kiểm tra nếu đơn tối thiểu ít hơn giá trị giảm theo %
            if (km.getDonToiThieu() <= km.getGiaTri()) {
                model.addAttribute("khuyenmai", km);
                model.addAttribute("errorDonToiThieu", "Đơn tối thiểu phải lớn hơn giá trị giảm.");
                return "templateadmin/sua-khuyen-mai";
            }

        } else if (km.getLoaiKhuyenMai().equalsIgnoreCase("Giảm giá số tiền")) {
            // Kiểm tra giá trị tiền phải lớn hơn 0
            if (km.getGiaTri() <= 0) {
                model.addAttribute("khuyenmai", km);
                model.addAttribute("errorGiaTri", "Giá trị giảm phải lớn hơn 0.");
                return "templateadmin/sua-khuyen-mai"; // Trả về form với thông báo lỗi
            }

            // Kiểm tra giá trị tiền phải nhỏ hơn hoặc bằng đơn tối thiểu
            if (km.getGiaTri() > km.getDonToiThieu()) {
                model.addAttribute("khuyenmai", km);
                model.addAttribute("errorDonToiThieu", "Giá trị giảm phải nhỏ hơn hoặc bằng đơn tối thiểu.");
                return "templateadmin/sua-khuyen-mai"; // Trả về form với thông báo lỗi
            }
        }

        // Kiểm tra logic: ngày bắt đầu không được trùng với ngày kết thúc
        if (km.getNgayBatDau().equals(km.getNgayKetThuc())) {
            model.addAttribute("khuyenmai", km);
            model.addAttribute("errorNgay", "Ngày bắt đầu và ngày kết thúc không được trùng nhau!");
            return "templateadmin/sua-khuyen-mai"; // Trả về form với thông báo lỗi
        }

        // Cập nhật thông tin cho khuyến mãi hiện tại
        existingKhuyenMai.setMaKhuyenMai(km.getMaKhuyenMai());
        existingKhuyenMai.setTenKhuyenMai(km.getTenKhuyenMai());
        existingKhuyenMai.setLoaiKhuyenMai(km.getLoaiKhuyenMai());
        existingKhuyenMai.setMoTa(km.getMoTa());
        existingKhuyenMai.setGiaTri(km.getGiaTri());
        existingKhuyenMai.setDonToiThieu(km.getDonToiThieu()); // Cập nhật đơn tối thiểu
        existingKhuyenMai.setNgayBatDau(km.getNgayBatDau());
        existingKhuyenMai.setNgayKetThuc(km.getNgayKetThuc());
        existingKhuyenMai.setTrangThai(km.getTrangThai());

        // Lưu khuyến mãi đã cập nhật
        khuyenMaiRepository.save(existingKhuyenMai);

        return "redirect:/qlkhuyenmai"; // Chuyển hướng đến trang quản lý khuyến mãi
    }

    @GetMapping("search")
    public String search(@RequestParam("keyword") String keyword,
                         @RequestParam(value = "ngayBatDau", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate ngayBatDau,
                         @RequestParam(value = "ngayKetThuc", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate ngayKetThuc,
                         @RequestParam(value = "trangThai", required = false) Integer trangThai,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         Model model) {
        int pageSize = 5; // Số lượng bản ghi mỗi trang
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending()); // Sắp xếp theo id tăng dần

        // Sử dụng phương thức searchKhuyenMai từ service
        Page<KhuyenMai> khuyenMaiPage = khuyenMaiService.searchKhuyenMai(keyword, ngayBatDau, ngayKetThuc,trangThai, pageable);


        // Thêm dữ liệu phân trang vào model
        model.addAttribute("khuyenmai", khuyenMaiPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", khuyenMaiPage.getTotalPages());
        model.addAttribute("keyword", keyword); // Để giữ lại từ khóa trong form tìm kiếm
        model.addAttribute("ngayBatDau", ngayBatDau); // Để giữ lại ngày bắt đầu trong form tìm kiếm
        model.addAttribute("ngayKetThuc", ngayKetThuc); // Để giữ lại ngày kết thúc trong form tìm kiếm
        model.addAttribute("trangThai", trangThai); // Để giữ lại trạng thái trong form tìm kiếm
        return "templateadmin/qlkhuyenmai";

    }
}
