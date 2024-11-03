package com.example.fsneaker.controller;

import com.example.fsneaker.entity.KichThuoc;
import com.example.fsneaker.entity.MauSac;
import com.example.fsneaker.entity.SanPham;
import com.example.fsneaker.entity.SanPhamChiTiet;
import com.example.fsneaker.repositories.KichThuocRepo;
import com.example.fsneaker.repositories.MauSacRepo;
import com.example.fsneaker.repositories.SanPhamChiTietRepo;
import com.example.fsneaker.repositories.SanPhamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/qlsanphamchitiet")
     public String index(Model model) {
        return "templateadmin/qlsanphamchitiet.html";
     }

     @PostMapping("/qlsanphamchitiet/store")
    public String store(
             Model model,
             @RequestParam(name = "maSanPhamChiTiet") String maSanPhamChiTiet,
             @RequestParam(name = "soLuong") Integer soLuong,
             @RequestParam(name = "giaBan") Double giaBan,
             @RequestParam(name = "giaBanGiamGia") Double giaBanGiamGia,
             @RequestParam(name = "ngaySanXuat") java.sql.Date ngaySanXuat,
             @RequestParam(name = "sanPhamId") Integer sanPhamId,
             @RequestParam(name = "kichThuocIds") List<Integer> kichThuocIds,
             @RequestParam(name = "mauSacIds") List<Integer> mauSacIds

             ){

         Optional<SanPham> sanPhamOptional = sanPhamRepo.findById(sanPhamId);
         // Kiểm tra xem sản phẩm có tồn tại không
         if (sanPhamOptional.isPresent()) {
             int maxStt = sanPhamChiTietRepo.findMaxStt();
             int index = maxStt + 1;
             for (Integer mauSacId : mauSacIds) {
                 Optional<MauSac> mauSacOptional = mauSacRepo.findById(mauSacId);

                 // Kiểm tra màu sắc có tồn tại không
                 if (mauSacOptional.isPresent()) {
                     for (Integer kichThuocId : kichThuocIds) {
                         Optional<KichThuoc> kichThuocOptional = kichThuocRepo.findById(kichThuocId);

                         // Kiểm tra kích thước có tồn tại không
                         if (kichThuocOptional.isPresent()) {
                             SanPhamChiTiet sanPhamChiTiet = new SanPhamChiTiet();
                             String uniqueMaSanPhamChiTiet = "SPCT" + index;
                             sanPhamChiTiet.setMaSanPhamChiTiet(uniqueMaSanPhamChiTiet);
                             sanPhamChiTiet.setSoLuong(soLuong);
                             sanPhamChiTiet.setGiaBan(giaBan);
                             sanPhamChiTiet.setGiaBanGiamGia(giaBanGiamGia);
                             sanPhamChiTiet.setNgaySanXuat(ngaySanXuat);
                             sanPhamChiTiet.setNgayTao(new Date()); // Lấy ngày hiện tại
                             sanPhamChiTiet.setSanPham(sanPhamOptional.get());
                             sanPhamChiTiet.setKichThuoc(kichThuocOptional.get());
                             sanPhamChiTiet.setMauSac(mauSacOptional.get());

                             // Lưu mỗi sản phẩm chi tiết với màu sắc và kích thước đã chọn
                             index++;
                             sanPhamChiTietRepo.save(sanPhamChiTiet);
                         }
                     }
                 }
             }
         }

         return "redirect:/qlsanpham";
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
    public String update(Model model,
                         @RequestParam Integer id,
                         @RequestParam(name = "sanPhamId") Integer sanPhamId,
                         @RequestParam(name = "kichThuocId") Integer kichThuocId,
                         @RequestParam(name = "mauSacId") Integer mauSacId,
                         @RequestParam(name = "maSanPhamChiTiet") String maSanPhamChiTiet,
                         @RequestParam(name = "soLuong") Integer soLuong,
                         @RequestParam(name = "giaBan") Double giaBan,
                         @RequestParam(name = "giaBanGiamGia") Double giaBanGiamGia,
                         @RequestParam(name = "ngaySanXuat") java.sql.Date ngaySanXuat
                         ){

        Optional<SanPham> sanPhamOptional = sanPhamRepo.findById(sanPhamId);
        Optional<KichThuoc> kichThuocOptional = kichThuocRepo.findById(kichThuocId);
        Optional<MauSac> mauSacOptional = mauSacRepo.findById(mauSacId);

          SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepo.findBySanPhamChitietI2d(id);


            sanPhamChiTiet.setMaSanPhamChiTiet(maSanPhamChiTiet);
            sanPhamChiTiet.setSoLuong(soLuong);
            sanPhamChiTiet.setGiaBan(giaBan);
            sanPhamChiTiet.setGiaBanGiamGia(giaBanGiamGia);
            sanPhamChiTiet.setNgaySanXuat(ngaySanXuat);
            sanPhamChiTiet.setSanPham(sanPhamOptional.get());
            sanPhamChiTiet.setKichThuoc(kichThuocOptional.get());
            sanPhamChiTiet.setMauSac(mauSacOptional.get());
            sanPhamChiTietRepo.save(sanPhamChiTiet);


          return "redirect:/qlsanpham?tab=profile";
      }

}
