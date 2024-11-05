package com.example.fsneaker.controller;

import com.example.fsneaker.entity.*;
import com.example.fsneaker.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
   public String index(Model model){

        List<SanPham> list = sanPhamRepo.findAll();
        model.addAttribute("listSanPham", list);

        List<SanPhamChiTiet> list2 = sanPhamChiTietRepo.findAll();
        model.addAttribute("listSanPhamChiTiet", list2);

        List<KhuyenMai> listKm = khuyenMaiRepo.findAll();
        model.addAttribute("listKhuyenMai", listKm);
        List<ThuongHieu> listTh = thuongHieuRepo.findAll();
        model.addAttribute("listThuongHieu", listTh);
        List<XuatXu> listXu = xuatXuRepo.findAll();
        model.addAttribute("listXuatXu", listXu);

        List<KichThuoc> listKichThuoc = kichThuocRepo.findAll();
        model.addAttribute("listKichThuoc", listKichThuoc);

        List<MauSac> listMauSac = mauSacRepo.findAll();
        model.addAttribute("listMauSac", listMauSac);




        return "templateadmin/qlsanpham.html";

    }

    @PostMapping("qlsanpham/store")
    public String store(
            Model model,
            @RequestParam(name = "khuyenMaiId") Integer khuyenMaiId,
            @RequestParam(name = "thuongHieuId") Integer thuongHieuId,
            @RequestParam(name = "xuatXuId") Integer xuatXuId,
            @RequestParam(name = "maSanPham") String maSanPham,
            @RequestParam(name = "tenSanPham") String tenSanPham,
            @RequestParam(name = "trangThai") Integer trangThai
            ){

        Optional<KhuyenMai> kmOptional = khuyenMaiRepo.findById(khuyenMaiId);
        Optional<ThuongHieu> thuongHieuOptional = thuongHieuRepo.findById(thuongHieuId);
        Optional<XuatXu> xuatOptional = xuatXuRepo.findById(xuatXuId);

        SanPham sp = new SanPham();
        sp.setKhuyenMai(kmOptional.get());
        sp.setThuongHieu(thuongHieuOptional.get());
        sp.setXuatXu(xuatOptional.get());
        sp.setMaSanPham(maSanPham);
        sp.setTenSanPham(tenSanPham);
        sp.setNgayTao(new Date());
        sp.setTrangThai(trangThai);

        sanPhamRepo.save(sp);

        return "redirect:/qlsanpham";

    }

    @GetMapping("/qlsanpham/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        SanPham sanPham = sanPhamRepo.getSanPhamsById(id);


        List<ThuongHieu> listTh = thuongHieuRepo.findAll();
        model.addAttribute("listThuongHieu", listTh);
        List<XuatXu> listXu = xuatXuRepo.findAll();
        model.addAttribute("listXuatXu", listXu);
        List<KhuyenMai> listKm = khuyenMaiRepo.findAll();
        model.addAttribute("listKhuyenMai", listKm);

        model.addAttribute("sanPham", sanPham);

        return "templateadmin/qlsanpham.html";
    }

    @PostMapping("qlsanpham/update")
    public String update(
            Model model,
            @RequestParam Integer id,
            @RequestParam(name = "khuyenMaiId") Integer khuyenMaiId,
            @RequestParam(name = "thuongHieuId") Integer thuongHieuId,
            @RequestParam(name = "xuatXuId") Integer xuatXuId,

            @RequestParam(name = "maSanPham") String maSanPham,
            @RequestParam(name = "tenSanPham") String tenSanPham,
            @RequestParam(name = "trangThai") Integer trangThai
    ){

        Optional<KhuyenMai> kmOptional = khuyenMaiRepo.findById(khuyenMaiId);
        Optional<ThuongHieu> thuongHieuOptional = thuongHieuRepo.findById(thuongHieuId);
        Optional<XuatXu> xuatOptional = xuatXuRepo.findById(xuatXuId);

        SanPham sp = sanPhamRepo.getSanPhamsById(id);
        if (sp != null && kmOptional.isPresent() && thuongHieuOptional.isPresent() && xuatOptional.isPresent()) {
            sp.setKhuyenMai(kmOptional.get());
            sp.setThuongHieu(thuongHieuOptional.get());
            sp.setXuatXu(xuatOptional.get());
            sp.setMaSanPham(maSanPham);
            sp.setTenSanPham(tenSanPham);
            sp.setTrangThai(trangThai);
            sp.setNgayTao(new Date()); // Ngày tạo có thể không thay đổi khi cập nhật
            sanPhamRepo.save(sp);
        }
        return "redirect:/qlsanpham";

    }



}
