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

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
//    @Autowired
//    private StorageService storageService;

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
                                   @RequestParam(value = "images", required = false) MultipartFile[] images) {

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

        // Kiểm tra nếu có ảnh được upload
        List<String> imageNames = new ArrayList<>();
        if (images != null && images.length > 0) {
            String uploadDir = "Fsneaker/src/main/resources/static/images/product/";
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    String imageName = image.getOriginalFilename();
                    Path path = Paths.get(uploadDir + imageName);
                    imageNames.add(imageName);
                    try {
                        Files.createDirectories(path.getParent());
                        Files.write(path, image.getBytes());
                    } catch (IOException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu ảnh.");
                    }
                }
            }
        }

        // Kiểm tra xem sản phẩm có được chọn không
        Optional<SanPham> sanPhamOptional = sanPhamRepo.findById(sanPhamId);
        if (!sanPhamOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sản phẩm không tồn tại.");
        }
        SanPham sanPham = sanPhamOptional.get();

        // Lấy giá trị phiếu giảm giá
        BigDecimal giaTriGiamGia = sanPhamChiTietRepo.giaTri(sanPhamId);

        // Tính giá bán sau giảm
        if (giaTriGiamGia != null && giaTriGiamGia.compareTo(BigDecimal.valueOf(100)) <= 0) {
            BigDecimal phanTramGiamGia = giaTriGiamGia.divide(BigDecimal.valueOf(100));
            BigDecimal giaBanGiamGia = sanPhamChiTiet.getGiaBan().multiply(BigDecimal.ONE.subtract(phanTramGiamGia));
            sanPhamChiTiet.setGiaBanGiamGia(giaBanGiamGia);
        } else if (giaTriGiamGia != null && giaTriGiamGia.compareTo(BigDecimal.valueOf(100)) > 0) {
            BigDecimal giabanGiamGia = giaTriGiamGia.subtract(giaTriGiamGia);
            sanPhamChiTiet.setGiaBanGiamGia(giabanGiamGia);
        } else {
            sanPhamChiTiet.setGiaBanGiamGia(BigDecimal.ZERO);
        }

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

                // Lưu tên ảnh vào sản phẩm chi tiết nếu có
                if (!imageNames.isEmpty()) {
                    newSanPhamChiTiet.setImanges(String.join(",", imageNames));
                }
//                if (imageName != null) {
//                    newSanPhamChiTiet.setImanges(imageName);
//                }

                index++;
                sanPhamChiTietRepo.save(newSanPhamChiTiet);


            }
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/qlsanphamchitiet")
                .build();
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


//    @PostMapping("/qlsanphamchitiet/update")
//    public ResponseEntity<?> update(@Valid SanPhamChiTiet sanPhamChiTiet,
//                                    BindingResult result,
//                                    Model model,
//                                    @RequestParam(name = "sanPhamId", required = false) Integer sanPhamId,
//                                    @RequestParam(name = "kichThuocId", required = false) Integer kichThuocId,
//                                    @RequestParam(name = "mauSacId", required = false) Integer mauSacId,
//                                    @RequestParam(value = "image", required = false) MultipartFile image
//                                    ) {
//
//        // Nếu có lỗi validation, trả lỗi về dạng JSON
//        if (result.hasErrors()) {
//            Map<String, String> errors = new HashMap<>();
//            for (FieldError fieldError : result.getFieldErrors()) {
//                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
//            }
//
//            return new ResponseEntity<>(new ValidationErrorResponse(errors), HttpStatus.BAD_REQUEST);
//        }
//
//        // Nếu không có lỗi, thực hiện cập nhật
//        Optional<SanPham> sanPhamOptional = sanPhamRepo.findById(sanPhamId);
//        Optional<KichThuoc> kichThuocOptional = kichThuocRepo.findById(kichThuocId);
//        Optional<MauSac> mauSacOptional = mauSacRepo.findById(mauSacId);
//
//
//
//
//        if (sanPhamOptional.isPresent() && kichThuocOptional.isPresent() && mauSacOptional.isPresent()) {
//            sanPhamChiTiet.setSanPham(sanPhamOptional.get());
//            sanPhamChiTiet.setKichThuoc(kichThuocOptional.get());
//            sanPhamChiTiet.setMauSac(mauSacOptional.get());
//
//            sanPhamChiTietRepo.save(sanPhamChiTiet);
//        }
//
//        // Trả về view sau khi cập nhật thành công
//        return ResponseEntity.status(HttpStatus.FOUND)
//                .header("Location", "/qlsanphamchitiet")
//                .build();
//    }


    @PostMapping("/qlsanphamchitiet/update")
    public ResponseEntity<?> update(@Valid SanPhamChiTiet sanPhamChiTiet,
                                    BindingResult result,
                                    @RequestParam(name = "sanPhamId", required = false) Integer sanPhamId,
                                    @RequestParam(name = "kichThuocId", required = false) Integer kichThuocId,
                                    @RequestParam(name = "mauSacId", required = false) Integer mauSacId,
                                    @RequestParam(value = "imagees", required = false) MultipartFile[] imagees,
                                    Model model) {

        // Kiểm tra lỗi từ BindingResult
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : result.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(new ValidationErrorResponse(errors), HttpStatus.BAD_REQUEST);
        }

        // Lấy sản phẩm chi tiết hiện tại từ cơ sở dữ liệu
        SanPhamChiTiet existingProductDetail = sanPhamChiTietRepo.findById(sanPhamChiTiet.getId());
        if (existingProductDetail == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm chi tiết.");
        }

        // Cập nhật các thông tin từ request
        existingProductDetail.setSanPham(sanPhamRepo.findById(sanPhamId).orElse(null));
        existingProductDetail.setKichThuoc(kichThuocRepo.findById(kichThuocId).orElse(null));
        existingProductDetail.setMauSac(mauSacRepo.findById(mauSacId).orElse(null));
        existingProductDetail.setSoLuong(sanPhamChiTiet.getSoLuong());
        existingProductDetail.setGiaBan(sanPhamChiTiet.getGiaBan());
        existingProductDetail.setNgayTao(existingProductDetail.getNgayTao());
        // Xử lý ảnh nếu có tải lên ảnh mới
        // Xử lý lưu trữ nhiều ảnh
        if (imagees != null && imagees.length > 0) {
            String uploadDir = "Fsneaker/src/main/resources/static/images/product/";
            List<String> imageNames = new ArrayList<>();

            for (MultipartFile image : imagees) {
                if (!image.isEmpty()) {
                    String imageName = image.getOriginalFilename();
                    Path path = Paths.get(uploadDir + imageName);
                    try {
                        Files.createDirectories(path.getParent());
                        Files.write(path, image.getBytes());
                        imageNames.add(imageName); // Thêm tên ảnh vào danh sách
                    } catch (IOException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu ảnh: " + imageName);
                    }
                }
            }

            // Lưu danh sách ảnh vào cột `imanges` (chuỗi các tên ảnh cách nhau bởi dấu phẩy)
            existingProductDetail.setImanges(String.join(",", imageNames));
        }else{
            existingProductDetail.setImanges(existingProductDetail.getImanges());
        }

        // Tính giá bán sau giảm giá (logic giữ nguyên)
        BigDecimal giaTriGiamGia = sanPhamChiTietRepo.giaTri(sanPhamId);
        if (giaTriGiamGia != null && giaTriGiamGia.compareTo(BigDecimal.valueOf(100)) <= 0) {
            BigDecimal phanTramGiamGia = giaTriGiamGia.divide(BigDecimal.valueOf(100));
            BigDecimal giaBanGiamGia = existingProductDetail.getGiaBan().multiply(BigDecimal.ONE.subtract(phanTramGiamGia));
            existingProductDetail.setGiaBanGiamGia(giaBanGiamGia);
        } else if (giaTriGiamGia != null && giaTriGiamGia.compareTo(BigDecimal.valueOf(100)) > 0) {
            BigDecimal giabanGiamGia = giaTriGiamGia.subtract(giaTriGiamGia);
            existingProductDetail.setGiaBanGiamGia(giabanGiamGia);
        } else {
            existingProductDetail.setGiaBanGiamGia(BigDecimal.ZERO);
        }

        // Lưu lại thay đổi
        sanPhamChiTietRepo.save(existingProductDetail);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/qlsanphamchitiet")
                .build();
    }

}
