package com.example.fsneaker.controller;

import com.example.fsneaker.entity.*;
import com.example.fsneaker.repositories.*;
import com.example.fsneaker.response.ResponseMessage;
import com.example.fsneaker.response.ValidationErrorResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.Reader;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class SanPhamController {

    @Autowired
    private SanPhamRepo sanPhamRepo;

    @Autowired
    private SanPhamChiTietRepo sanPhamChiTietRepo;

    @Autowired
    private XuatXuRepo xuatXuRepo;

    @Autowired
    private ThuongHieuRepo thuongHieuRepo;

    @Autowired
    private KhuyenMaiRepo khuyenMaiRepo;
    @Autowired
    private KichThuocRepo kichThuocRepo;
    @Autowired
    private MauSacRepo mauSacRepo;

    @GetMapping("/qlsanpham")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") Integer pageNo,
                        @RequestParam(name = "limit", defaultValue = "5") Integer pageSize,
                        @RequestParam(name = "tab", defaultValue = "home") String tab,
                        @RequestParam(name = "serchSanPham", required = false) String serchSanPham,
                        @RequestParam(name = "idThuongHieu", required = false) Integer idThuongHieu,
                        @RequestParam(name = "idXuatXu", required = false) Integer idXuatXu
    ) {


        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<SanPham> list;

        if (serchSanPham != null && !serchSanPham.isEmpty() && idThuongHieu != null && idXuatXu != null) {
            list = sanPhamRepo.findAll(pageable);
        }

        //search theo mã sản phẩm, tên sản phẩm
        if (serchSanPham != null && !serchSanPham.isEmpty()) {
            list = sanPhamRepo.serchSanPhamByCodeOrName(serchSanPham, pageable);
        }
        // lọc theo thương hiệu
        else if (idThuongHieu != null) {
            list = sanPhamRepo.searchByIdThuongHieu(idThuongHieu, pageable);
        }
        //lọc theo xuất xứ
        else if (idXuatXu != null) {
            list = sanPhamRepo.searchByIdXuatXu(idXuatXu, pageable);
        } else {
            list = sanPhamRepo.findAll(pageable);
        }

        model.addAttribute("listSanPham", list);

        List<KhuyenMai> listKm = khuyenMaiRepo.findAll();
        model.addAttribute("listKhuyenMai", listKm);
        List<ThuongHieu> listTh = thuongHieuRepo.findAll();
        model.addAttribute("listThuongHieu", listTh);
        List<XuatXu> listXu = xuatXuRepo.findAll();
        model.addAttribute("listXuatXu", listXu);
        model.addAttribute("tab", tab);
        return "templateadmin/qlsanpham";
    }

    @GetMapping("/qlsanphamchitiet")
    public String index2(Model model,
                         @RequestParam(name = "page", defaultValue = "0") Integer pageNo,
                         @RequestParam(name = "limit", defaultValue = "5") Integer pageSize,
                         @RequestParam(name = "searchSanPhamChiTiet", required = false) String searchSanPhamChiTiet,
                         @RequestParam(name = "idSanPham", required = false) Integer idSanPham,
                         @RequestParam(name = "idMauSac", required = false) Integer idMauSac,
                         @RequestParam(name = "idKichThuoc", required = false) Integer idKichThuoc,
                         @RequestParam(name = "minPrice", required = false) Double minPrice,
                         @RequestParam(name = "maxPrice", required = false) Double maxPrice) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<SanPhamChiTiet> list2;


        if (searchSanPhamChiTiet != null && !searchSanPhamChiTiet.isEmpty()) {
            list2 = sanPhamChiTietRepo.searchByMaSanPhamChiTiet(searchSanPhamChiTiet, pageable);
        } else if (idSanPham != null) {
            list2 = sanPhamChiTietRepo.searcBySanPhamId(idSanPham, pageable);
        } else if (idMauSac != null) {
            list2 = sanPhamChiTietRepo.searchByMauSacId(idMauSac, pageable);
        } else if (idKichThuoc != null) {
            list2 = sanPhamChiTietRepo.searchByKichThuocId(idKichThuoc, pageable);
        } else if (minPrice != null && maxPrice != null) {
            list2 = sanPhamChiTietRepo.searchByPrice(minPrice, maxPrice, pageable);
        } else {
            list2 = sanPhamChiTietRepo.findAll(pageable);
        }

        model.addAttribute("listSanPhamChiTiet", list2);
        model.addAttribute("listSanPhams", sanPhamRepo.findAll());
        model.addAttribute("listKichThuoc", kichThuocRepo.findAll());
        model.addAttribute("listMauSac", mauSacRepo.findAll());
        model.addAttribute("tab", "profile");

        return "templateadmin/qlsanpham";
    }



    @PostMapping("qlsanpham/store")
    public ResponseEntity<?> store(
            @Valid SanPham sanPham,
            BindingResult result,
            Model model,
            @RequestParam(value = "thuongHieuId", required = false) Integer thuongHieuId,
            @RequestParam(value = "khuyenMaiId", required = false) Integer khuyenMaiId,
            @RequestParam(value = "xuatXuId", required = false) Integer xuatXuId,
            @RequestParam(value = "isAjax", defaultValue = "false") boolean isAjax) {

        if (result.hasErrors()) {
            if (isAjax) {
                Map<String, String> errors = result.getFieldErrors().stream()
                        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
                return new ResponseEntity<>(new ValidationErrorResponse(errors), HttpStatus.BAD_REQUEST);
            }

            model.addAttribute("errors", result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));

            model.addAttribute("showAddModal", true);

            model.addAttribute("sanPham", sanPham);
            model.addAttribute("listKhuyenMai", khuyenMaiRepo.findAll());
            model.addAttribute("listThuongHieu", thuongHieuRepo.findAll());
            model.addAttribute("listXuatXu", xuatXuRepo.findAll());

            return ResponseEntity.status(HttpStatus.OK).body("templateadmin/qlsanpham");
        }
        KhuyenMai km = khuyenMaiId !=null? khuyenMaiRepo.findById(khuyenMaiId).orElse(null):null;
        XuatXu xuatXu = xuatXuRepo.findById(xuatXuId).get();
        ThuongHieu thuongHieu = thuongHieuRepo.findById(thuongHieuId).get();

        sanPham.setThuongHieu(thuongHieu);
        sanPham.setXuatXu(xuatXu);
        sanPham.setKhuyenMai(km);
        sanPham.setNgayTao(Date.valueOf(LocalDate.now()));

        sanPhamRepo.save(sanPham);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/qlsanpham")
                .build();
    }


    @GetMapping("/qlsanpham/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        SanPham sanPham = sanPhamRepo.getSanPhamsById(id);
        List<ThuongHieu> listTh = thuongHieuRepo.findAll();
        model.addAttribute("listThuongHieu", listTh);
        List<XuatXu> listXu = xuatXuRepo.findAll();
        model.addAttribute("listXuatXu", listXu);
        List<KhuyenMai> listKm = khuyenMaiRepo.findAll();
        model.addAttribute("listKhuyenMai", listKm);

        model.addAttribute("sanPham", sanPham);

        return "templateadmin/qlsanpham";
    }

    @PostMapping("qlsanpham/update")
    public ResponseEntity<?> update(
            @Valid SanPham sanPham,
            BindingResult result,
            @RequestParam(value = "thuongHieuId", required = false) Integer thuongHieuId,
            @RequestParam(value = "khuyenMaiId", required = false) Integer khuyenMaiId,
            @RequestParam(value = "xuatXuId", required = false) Integer xuatXuId,
            Model model,
            @RequestParam(value = "isAjax", defaultValue = "false") boolean isAjax
    ) {


        // Kiểm tra lỗi dữ liệu
        if (result.hasErrors()) {
            if (isAjax) {
                // Trả về lỗi dưới dạng JSON nếu là yêu cầu AJAX
                Map<String, String> errors = result.getFieldErrors().stream()
                        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
                return new ResponseEntity<>(new ValidationErrorResponse(errors), HttpStatus.BAD_REQUEST);
            }

            // Nếu không phải là AJAX, trả lại model với thông báo lỗi để hiển thị trên trang
            model.addAttribute("errors", result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            model.addAttribute("showAddModal", true);
            model.addAttribute("sanPham", sanPham);
            model.addAttribute("listKhuyenMai", khuyenMaiRepo.findAll());
            model.addAttribute("listThuongHieu", thuongHieuRepo.findAll());
            model.addAttribute("listXuatXu", xuatXuRepo.findAll());

            // Trả về lại trang HTML với thông báo lỗi
            return ResponseEntity.status(HttpStatus.OK).body("templateadmin/qlsanpham");
        }

        if (sanPham != null) {
            // Lấy các đối tượng liên quan từ repository
            KhuyenMai km = khuyenMaiRepo.findById(khuyenMaiId).orElse(null);
            XuatXu xuatXu = xuatXuRepo.findById(xuatXuId).orElse(null);
            ThuongHieu thuongHieu = thuongHieuRepo.findById(thuongHieuId).orElse(null);

            // Gán các thuộc tính cho sanPham
             sanPham.setKhuyenMai(km);
            if (xuatXu != null) sanPham.setXuatXu(xuatXu);
            if (thuongHieu != null) sanPham.setThuongHieu(thuongHieu);
            // Lưu lại sanPham đã cập nhật
            sanPhamRepo.save(sanPham);
        }

        // Trả về thông báo thành công nếu là AJAX
        if (isAjax) {
            return new ResponseEntity<>(new ResponseMessage("Update thành công!"), HttpStatus.OK);
        }

        // Nếu không phải AJAX, trả về thông báo thành công và chuyển hướng
        model.addAttribute("successMessage", "Update thành công!");
        model.addAttribute("sanPham", sanPham);
        model.addAttribute("listKhuyenMai", khuyenMaiRepo.findAll());
        model.addAttribute("listThuongHieu", thuongHieuRepo.findAll());
        model.addAttribute("listXuatXu", xuatXuRepo.findAll());

        // Trả về view sau khi cập nhật thành công
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/qlsanpham")
                .build();
    }




}
