package com.example.fsneaker.controller;

import com.example.fsneaker.entity.DonHang;
import com.example.fsneaker.entity.DonHangChiTiet;
import com.example.fsneaker.entity.LichSuDonHang;
import com.example.fsneaker.repositories.DonHangChiTietRepo;
import com.example.fsneaker.repositories.DonHangRepo;
import com.example.fsneaker.repositories.LichSuDonHangRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HoaDonController {
    @Autowired
    DonHangRepo donHangRepo;

    @Autowired
    DonHangChiTietRepo donHangChiTietRepo;

    @Autowired
    LichSuDonHangRepo lichSuDonHangRepo;

    @GetMapping("/donhangadmin")
    public String donHang(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "limit", defaultValue = "5") Integer limit,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam Optional<LocalDate> startDate,
            @RequestParam Optional<LocalDate> endDate,
//            @RequestParam Optional<String> trangThai,
            @RequestParam(required = false, defaultValue = "tatca") String tab
    ) {
        String s = "%" + keyword + "%";
        PageRequest pageRequest = PageRequest.of(page,limit);
        Page<DonHang> list = this.donHangRepo.searchPageHoaDon(
                s,
                startDate.orElse(null),
                endDate.orElse(null),
                pageRequest
        );

        Page<DonHang> listCXN = this.donHangRepo.searchPageHoaDonfindStatus(
                s,
                startDate.orElse(null),
                endDate.orElse(null),
                "Chờ xác nhận",
                pageRequest
        );

        Page<DonHang> listCG = this.donHangRepo.searchPageHoaDonfindStatus(
                s,
                startDate.orElse(null),
                endDate.orElse(null),
                "Chờ giao",
                pageRequest
        );
        Page<DonHang> listDG = this.donHangRepo.searchPageHoaDonfindStatus(
                s,
                startDate.orElse(null),
                endDate.orElse(null),
                "Đang giao",
                pageRequest
        );
        Page<DonHang> listHT = this.donHangRepo.searchPageHoaDonfindStatus(
                s,
                startDate.orElse(null),
                endDate.orElse(null),
                "Đã thanh toán",
                pageRequest
        );

        Page<DonHang> listDH = this.donHangRepo.searchPageHoaDonfindStatus(
                s,
                startDate.orElse(null),
                endDate.orElse(null),
                "Đã hủy",
                pageRequest
        );

        if (tab == null || tab.trim().isEmpty()) {
            tab = "tatca"; // Giá trị mặc định
        }
        model.addAttribute("data", list);
        model.addAttribute("choXacNhan", listCXN);
        model.addAttribute("choGiao", listCG);
        model.addAttribute("dangGiao", listDG);
        model.addAttribute("hoanThanh", listHT);
        model.addAttribute("daHuy", listDH);
        model.addAttribute("tab", tab);
        return "templateadmin/donhangadmin.html";
    }

    @GetMapping("/donhangadmin-detail/{id}")
    public String detail(
            Model model,
            @PathVariable("id") Integer idDonHang
    ){
        List<DonHangChiTiet> listDHCT = this.donHangChiTietRepo.donHangDetail(idDonHang);
        List<LichSuDonHang> listLSDH = this.lichSuDonHangRepo.lichSuDonHangCT(idDonHang);

        model.addAttribute("dataLSDH",listLSDH);
        model.addAttribute("dataDHCT", listDHCT);
        return "templateadmin/donhang-detail.html";
    }


    // Xác nhận đơn hàng
    @PostMapping("/donhangadmin-detail/{orderId}/confirm")
    public String confirmOrder(@PathVariable Integer orderId) {
        DonHang order = donHangRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        if ("Đang chờ".equals(order.getTrangThai())  || "Đang xử lý".equals(order.getTrangThai()) || "Chờ xác nhận".equals(order.getTrangThai())) {
            saveOrderStatusHistory(order, order.getTrangThai(), "Chờ giao");
            order.setTrangThai("Chờ giao");
            donHangRepo.save(order);
        }
        return "redirect:/donhangadmin-detail/" + orderId;
    }

    // Vận chuyển đơn hàng
    @PostMapping("/donhangadmin-detail/{orderId}/ship")
    public String shipOrder(@PathVariable Integer orderId) {
        DonHang order = donHangRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        if ("Chờ giao".equals(order.getTrangThai())) {
            saveOrderStatusHistory(order, order.getTrangThai(), "Đang giao");
            order.setTrangThai("Đang giao");
            donHangRepo.save(order);
        }
        return "redirect:/donhangadmin-detail/" + orderId;
    }

    // Thanh toán đơn hàng
    @PostMapping("/donhangadmin-detail/{orderId}/pay")
    public String payOrder(@PathVariable Integer orderId) {
        DonHang order = donHangRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        if ("Đang giao".equals(order.getTrangThai())) {
            saveOrderStatusHistory(order, order.getTrangThai(), "Đã thanh toán");
            order.setTrangThai("Đã thanh toán");
            donHangRepo.save(order);
        }
        return "redirect:/donhangadmin-detail/" + orderId;
    }

    @PostMapping("/donhangadmin-detail/{orderId}/huy")
    public String huyOrder(@PathVariable Integer orderId) {
        DonHang order = donHangRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        if ("Đang giao".equals(order.getTrangThai()) || "Chờ giao".equals(order.getTrangThai()) || "Đang chờ".equals(order.getTrangThai()) || "Đang xử lý".equals(order.getTrangThai()) || "Chờ xác nhận".equals(order.getTrangThai())) {
            saveOrderStatusHistory(order, order.getTrangThai(), "Đã hủy");
            order.setTrangThai("Đã hủy");
            donHangRepo.save(order);
        }
        return "redirect:/donhangadmin-detail/" + orderId;
    }

    // Lưu lịch sử trạng thái
    private void saveOrderStatusHistory(DonHang order, String oldStatus, String newStatus) {
        LichSuDonHang history = new LichSuDonHang();
        history.setDonHang(order);
        history.setTrangThaiCu(oldStatus);
        history.setTrangThaiMoi(newStatus);
        lichSuDonHangRepo.save(history);
    }

//    @GetMapping("/donhangadmin-history/{id}")
//    private String lichSuDonHang(
//            Model model,
//            @PathVariable("id") Integer id
//    ){
//        List<LichSuDonHang> listLSDH = this.lichSuDonHangRepo.lichSuDonHangCT(id);
//        model.addAttribute("dataLSDH",listLSDH);
//        return "templateadmin/donhang-detail";
//    }

}
