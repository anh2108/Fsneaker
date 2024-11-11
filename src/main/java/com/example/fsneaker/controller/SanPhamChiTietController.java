package com.example.fsneaker.controller;

import com.example.fsneaker.entity.KichThuoc;
import com.example.fsneaker.entity.MauSac;
import com.example.fsneaker.entity.SanPham;
import com.example.fsneaker.entity.SanPhamChiTiet;
import com.example.fsneaker.repositories.KichThuocRepo;
import com.example.fsneaker.repositories.MauSacRepo;
import com.example.fsneaker.repositories.SanPhamChiTietRepo;
import com.example.fsneaker.repositories.SanPhamRepo;
import com.example.fsneaker.response.ValidationErrorResponse;
import com.example.fsneaker.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class SanPhamChiTietController {

    @Autowired
    private SanPhamChiTietRepo sanPhamChiTietRepo;
    @Autowired
    private SanPhamRepo sanPhamRepo;
    @Autowired
    private MauSacRepo mauSacRepo;
    @Autowired
    private KichThuocRepo kichThuocRepo;
    @Autowired
    private StorageService storageService;

//    @GetMapping("/qlsanphamchitiet")
//     public String index(Model model) {
//        return "templateadmin/qlsanphamchitiet.html";
//     }

    @PostMapping("/qlsanphamchitiet/store")
    public ResponseEntity<?> store(@Valid SanPhamChiTiet sanPhamChiTiet,
                                   BindingResult result,
                                   Model model,
                                   @RequestParam(value = "isAjax", defaultValue = "0") Integer isAjax,
                                   @RequestParam(value = "sanPhamId", required = false) Integer sanPhamId,
                                   @RequestParam(value = "kichThuocIds", required = false) Integer[] kichThuocIds,
                                   @RequestParam(value = "mauSacIds", required = false) Integer[] mauSacIds,
                                   @RequestParam(value = "fileStore", required = false) MultipartFile fileStore) {

        // Kiểm tra lỗi từ BindingResult
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();

            if (isAjax == 1) {
                for (FieldError fieldError : result.getFieldErrors()) {
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
                return new ResponseEntity<>(new ValidationErrorResponse(errors), HttpStatus.BAD_REQUEST);
            }

            for (FieldError fieldError : result.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            model.addAttribute("errors", errors);
            model.addAttribute("showAddModal", true); // Mở lại modal nếu có lỗi
            model.addAttribute("sanPhamChiTiet", sanPhamChiTiet);
            model.addAttribute("listSanPham", sanPhamRepo.findAll());
            model.addAttribute("listKichThuoc", kichThuocRepo.findAll());
            model.addAttribute("listMauSac", mauSacRepo.findAll());

            return ResponseEntity.status(HttpStatus.OK).body("Lưu thành công");

        }

        // Kiểm tra xem sản phẩm có được chọn không
        Optional<SanPham> sanPhamOptional = sanPhamRepo.findById(sanPhamId);
        if (!sanPhamOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sản phẩm không tồn tại.");
        }
        SanPham sanPham = sanPhamOptional.get();

        // Tiến hành lưu trữ sản phẩm chi tiết với các kích thước và màu sắc được chọn
        for (Integer kichThuocId : kichThuocIds) {
            for (Integer mauSacId : mauSacIds) {
                int maxStt = sanPhamChiTietRepo.findMaxStt();
                int index = maxStt + 1;

                SanPhamChiTiet newSanPhamChiTiet = new SanPhamChiTiet();
                String uniqueMaSanPhamChiTiet = "SPCT" + index;
                newSanPhamChiTiet.setMaSanPhamChiTiet(uniqueMaSanPhamChiTiet);
                newSanPhamChiTiet.setSanPham(sanPham);
                newSanPhamChiTiet.setKichThuoc(kichThuocRepo.findById(kichThuocId).orElse(null));
                newSanPhamChiTiet.setMauSac(mauSacRepo.findById(mauSacId).orElse(null));
                newSanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong());
                newSanPhamChiTiet.setGiaBan(sanPhamChiTiet.getGiaBan());
                newSanPhamChiTiet.setGiaBanGiamGia(sanPhamChiTiet.getGiaBanGiamGia());

                // Xử lý upload file
                if (fileStore != null && !fileStore.isEmpty()) {
                    LocalDateTime ldt = LocalDateTime.now();
                    String fileName = "product_" + ldt;
                    String saveLink = storageService.uploadFile(fileStore, fileName);
                    newSanPhamChiTiet.setImanges(saveLink);
                }

                index++;
                sanPhamChiTietRepo.save(newSanPhamChiTiet);
                return ResponseEntity.status(HttpStatus.CREATED).body(newSanPhamChiTiet);
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Thêm sản phẩm chi tiết thành công!");
    }

    @GetMapping("/qlsanphamchitiet/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findBySanPhamChitietI2d(id);
        List<SanPham> listSp = sanPhamRepo.findAll();
        model.addAttribute("listSp", listSp);

        List<KichThuoc> listKt = kichThuocRepo.findAll();
        model.addAttribute("listKichThuoc", listKt);

        List<MauSac> listMs = mauSacRepo.findAll();
        model.addAttribute("listMauSac", listMs);

        model.addAttribute("sanPhamChiTiet", sanPhamChiTiet);

          return "templateadmin/qlsanpham.html";
      }


    @PostMapping("/qlsanphamchitiet/update")
    public ResponseEntity<?> update(@Valid SanPhamChiTiet sanPhamChiTiet,
                                    BindingResult result,
                                    @RequestParam(name = "sanPhamId", required = false) Integer sanPhamId,
                                    @RequestParam(name = "kichThuocId", required = false) Integer kichThuocId,
                                    @RequestParam(name = "mauSacId", required = false) Integer mauSacId,
                                    @RequestParam(name = "fileEdit", required = false) MultipartFile fileEdit,
                                    Model model) {

        // Nếu có lỗi validation, trả lỗi về dạng JSON
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : result.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return new ResponseEntity<>(new ValidationErrorResponse(errors), HttpStatus.BAD_REQUEST);
        }

        // Nếu không có lỗi, thực hiện cập nhật
        Optional<SanPham> sanPhamOptional = sanPhamRepo.findById(sanPhamId);
        Optional<KichThuoc> kichThuocOptional = kichThuocRepo.findById(kichThuocId);
        Optional<MauSac> mauSacOptional = mauSacRepo.findById(mauSacId);

        if(!fileEdit.isEmpty() && fileEdit != null) {
            LocalDateTime ldt = LocalDateTime.now();
            String fileName = "product_" + ldt;
            String saveLink = "";
            saveLink = storageService.uploadFile(fileEdit, fileName);
            sanPhamChiTiet.setImanges(saveLink);
        }


        if (sanPhamOptional.isPresent() && kichThuocOptional.isPresent() && mauSacOptional.isPresent()) {
            sanPhamChiTiet.setSanPham(sanPhamOptional.get());
            sanPhamChiTiet.setKichThuoc(kichThuocOptional.get());
            sanPhamChiTiet.setMauSac(mauSacOptional.get());

            sanPhamChiTietRepo.save(sanPhamChiTiet);
        }

        return ResponseEntity.status(HttpStatus.OK).body("Cập nhật thành công");
    }



}
