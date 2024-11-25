package com.example.fsneaker.controller;

import com.example.fsneaker.entity.*;
import com.example.fsneaker.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class GioHangController {
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;
    @Autowired
    private GioHangService gioHangService;
    @Autowired
    private GioHangChiTietService gioHangChiTietService;
    @Autowired
    private DonHangService donHangService;
    @Autowired
    private KhachHangService khachHangService;
    @Autowired
    private DonHangChiTietService  donHangChiTietService;
    @GetMapping("/gio-hang")
    public String hienThiGioHang(HttpServletRequest request, Model model){
        List<Object[]> tenSanPhamVoiSanPham = sanPhamChiTietService.getNiekByTenSanPham(1);
        model.addAttribute("tenSanPhamVoiSanPham", tenSanPhamVoiSanPham);
        List<Object[]> tenSanPhamPumaVoiSanPham = sanPhamChiTietService.getNiekByTenSanPham(3);
        model.addAttribute("tenSanPhamPumaVoiSanPham", tenSanPhamPumaVoiSanPham);
        List<Object[]> tenSanPhamAdidasVoiSanPham = sanPhamChiTietService.getNiekByTenSanPham(2);
        model.addAttribute("tenSanPhamAdidasVoiSanPham",tenSanPhamAdidasVoiSanPham);
        List<Object[]> tenSanPhamNewBalanceVoiSanPham = sanPhamChiTietService.getNiekByTenSanPham(4);
        model.addAttribute("tenSanPhamNewBalanceVoiSanPham",tenSanPhamNewBalanceVoiSanPham);
        List<Object[]> tenSanPhamAsicsVoiSanPham = sanPhamChiTietService.getNiekByTenSanPham(5);
        model.addAttribute("tenSanPhamAsicsVoiSanPham", tenSanPhamAsicsVoiSanPham);

        String sessionId;
        GioHang gioHang;

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (request.getSession().getAttribute("userId") == null) {
            // Lấy hoặc tạo sessionId
            sessionId = (String) request.getSession().getAttribute("sessionId");
            if (sessionId == null) {
                sessionId = UUID.randomUUID().toString(); // Tạo sessionId duy nhất
                request.getSession().setAttribute("sessionId", sessionId);
            }
            // Tìm giỏ hàng theo sessionId
            gioHang = gioHangService.getGioHangBySessionId(sessionId);
            if (gioHang == null) {
                // Nếu chưa có giỏ hàng, tạo giỏ hàng mới
                gioHang = new GioHang();
                gioHang.setMaGioHang(sessionId);
                gioHang.setNgayTao(LocalDate.now());
                gioHang.setTrangThai(0); // 0: Chưa thanh toán
                gioHangService.savaGioHang(gioHang);
            }
        } else {
            // Nếu người dùng đã đăng nhập, lấy giỏ hàng theo tài khoản
            int userId = (int) request.getSession().getAttribute("userId");
            gioHang = gioHangService.getGioHangByUserId(userId);
            KhachHang khachHang = khachHangService.getKhachHangById(userId);
            // Nếu giỏ hàng chưa có, tạo giỏ hàng mới và gắn với khách hàng
            if (gioHang == null) {
                gioHang = new GioHang();
                gioHang.setKhachHang(khachHang); // Gắn khách hàng vào giỏ hàng
                gioHang.setNgayTao(LocalDate.now());
                gioHang.setTrangThai(0); // 0: Chưa thanh toán
                gioHangService.savaGioHang(gioHang);
            }
            // Nếu có session giỏ hàng tạm thời, gộp vào giỏ hàng đã đăng nhập
            sessionId = (String) request.getSession().getAttribute("sessionId");
            if (sessionId != null) {
                GioHang tempGioHang = gioHangService.getGioHangBySessionId(sessionId);
                if (tempGioHang != null) {
                    gioHangChiTietService.mergeCart(tempGioHang, gioHang);
                    gioHangService.delete(tempGioHang); // Xóa giỏ hàng tạm thời
                    request.getSession().removeAttribute("sessionId"); // Xóa sessionId
                }
            }
        }

        // Tính toán dữ liệu hiển thị
        List<GioHangChiTiet> danhSachChiTiet = gioHangChiTietService.getByGioHangId(gioHang.getId());
        model.addAttribute("danhSachChiTiet", danhSachChiTiet);
        int demTongSoLuongTrongGioHang = gioHangChiTietService.getSoLuongTrongGioHang(gioHang.getId());
        model.addAttribute("demTongSoLuongTrongGioHang",demTongSoLuongTrongGioHang);
        double tongTien = gioHangChiTietService.tinhTongTien(gioHang.getId());
        model.addAttribute("gioHang", gioHang);
        model.addAttribute("tongTien", tongTien);
        return "templatekhachhang/gio-hang";
    }
    @PostMapping("/them-vao-gio-hang")
    @ResponseBody
    public ResponseEntity<?> themVaoGioHang(
            @RequestParam(value = "idSanPham", required = false) Integer idSanPham,
            @RequestParam(value = "idKichThuoc", required = false) Integer idKichThuoc,
            @RequestParam(value = "idMauSac", required = false) Integer idMauSac,
            @RequestParam(value = "soLuong", required = false) Integer soLuong,
            HttpServletRequest request) {


        String sessionId;
        GioHang gioHang;

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (request.getSession().getAttribute("userId") == null) {
            // Lấy hoặc tạo sessionId
            sessionId = (String) request.getSession().getAttribute("sessionId");
            if (sessionId == null) {
                sessionId = UUID.randomUUID().toString(); // Tạo sessionId duy nhất
                request.getSession().setAttribute("sessionId", sessionId);
            }
            // Tìm giỏ hàng theo sessionId
            gioHang = gioHangService.getGioHangBySessionId(sessionId);
            if (gioHang == null) {
                // Nếu chưa có giỏ hàng, tạo giỏ hàng mới
                gioHang = new GioHang();
                gioHang.setMaGioHang(sessionId);
                gioHang.setNgayTao(LocalDate.now());
                gioHang.setTrangThai(0); // 0: Chưa thanh toán
                gioHangService.savaGioHang(gioHang);
            }
        } else {
            // Nếu người dùng đã đăng nhập, lấy giỏ hàng theo tài khoản
            int userId = (int) request.getSession().getAttribute("userId");
            gioHang = gioHangService.getGioHangByUserId(userId);
            KhachHang khachHang = khachHangService.getKhachHangById(userId);
            // Nếu giỏ hàng chưa có, tạo giỏ hàng mới và gắn với khách hàng
            if (gioHang == null) {
                gioHang = new GioHang();
                gioHang.setKhachHang(khachHang); // Gắn khách hàng vào giỏ hàng
                gioHang.setNgayTao(LocalDate.now());
                gioHang.setTrangThai(0); // 0: Chưa thanh toán
                gioHangService.savaGioHang(gioHang);
            }
            // Nếu có session giỏ hàng tạm thời, gộp vào giỏ hàng đã đăng nhập
            sessionId = (String) request.getSession().getAttribute("sessionId");
            if (sessionId != null) {
                GioHang tempGioHang = gioHangService.getGioHangBySessionId(sessionId);
                if (tempGioHang != null) {
                    gioHangChiTietService.mergeCart(tempGioHang, gioHang);
                    gioHangService.delete(tempGioHang); // Xóa giỏ hàng tạm thời
                    request.getSession().removeAttribute("sessionId"); // Xóa sessionId
                }
            }
        }

        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.getBySanPhamIdAndKichThuocIdAndMauSacId(idSanPham,idKichThuoc,idMauSac);
        if(sanPhamChiTiet == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sản phẩm không tồn tại!");
        }

            //Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
            GioHangChiTiet gioHangChiTiet = gioHangChiTietService.getByGioHangIdAndSanPhamIdAndKichThuocIdAndMauSacId(idSanPham,idKichThuoc,idMauSac);
            if(gioHangChiTiet != null){
                //Sản phẩm đã tồn tại, cập nhật số lượng
                gioHangChiTiet.setSoLuong(gioHangChiTiet.getSoLuong() + soLuong);
                gioHangChiTietService.saveGioHangChiTiet(gioHangChiTiet);
            }else {
                String part2 = String.format("%05d", new Random().nextInt(100000));
                //Sản phẩm chưa tồn tại, thêm mới
                GioHangChiTiet newgioHangChiTiet = new GioHangChiTiet();
                newgioHangChiTiet.setMaGioHangChiTiet("GHCT" + part2);
                newgioHangChiTiet.setGioHang(gioHang);
                newgioHangChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
                newgioHangChiTiet.setSoLuong(soLuong);
                newgioHangChiTiet.setGia(sanPhamChiTiet.getGiaBan());
                gioHangChiTietService.saveGioHangChiTiet(newgioHangChiTiet);
            }
//            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuong);
//            sanPhamChiTietService.capNhatSanPhamChiTiet(sanPhamChiTiet);
        return ResponseEntity.ok("Thêm vào giỏ hàng thành công");
    }
//    @PostMapping("/them-vao-gio-hang")
//    public String themVaoGIoHang(@RequestParam(value = "idSanPham",required = false) Integer idSanPham,@RequestParam(value = "idKichThuoc",required = false)Integer idKichThuoc, @RequestParam(value = "idMauSac",required = false)Integer idMauSac,@RequestParam(value = "soLuong",required = false) Integer soLuong, Model model){
//        String maGioHang = "DEFAULT_CART";
//        GioHang gioHang = gioHangService.getGioHangByMa(maGioHang);
//        if(gioHang == null){
//            gioHang = new GioHang();
//            gioHang.setMaGioHang(maGioHang);
//            gioHang.setNgayTao(LocalDate.now());
//            gioHang.setTrangThai(1);
//            gioHangService.savaGioHang(gioHang);
//        }
//        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.getBySanPhamIdAndKichThuocIdAndMauSacId(idSanPham,idKichThuoc,idMauSac);
//        if(sanPhamChiTiet != null){
//            //Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
//            GioHangChiTiet gioHangChiTiet = gioHangChiTietService.getByGioHangIdAndSanPhamIdAndKichThuocIdAndMauSacId(idSanPham,idKichThuoc,idMauSac);
//            if(gioHangChiTiet != null){
//                //Sản phẩm đã tồn tại, cập nhật số lượng
//                gioHangChiTiet.setSoLuong(gioHangChiTiet.getSoLuong() + soLuong);
//                gioHangChiTietService.saveGioHangChiTiet(gioHangChiTiet);
//            }else {
//                //Sản phẩm chưa tồn tại, thêm mới
//                GioHangChiTiet newgioHangChiTiet = new GioHangChiTiet();
//                newgioHangChiTiet.setMaGioHangChiTiet("CART_ITEM" + idSanPham);
//                newgioHangChiTiet.setGioHang(gioHang);
//                newgioHangChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
//                newgioHangChiTiet.setSoLuong(soLuong);
//                newgioHangChiTiet.setGia(sanPhamChiTiet.getGiaBan());
//                gioHangChiTietService.saveGioHangChiTiet(newgioHangChiTiet);
//            }
//            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuong);
//            sanPhamChiTietService.capNhatSanPhamChiTiet(sanPhamChiTiet);
//        }
//        return "redirect:/gio-hang";
//    }
    @PostMapping("/xoa-san-pham")
    public String xoaSanPhamTrongGioHang(@RequestParam(value = "idSanPham",required = false)Integer idSanPham,@RequestParam(value = "idKichThuoc",required = false)Integer idKichThuoc,@RequestParam(value ="idMauSac",required = false)Integer idMauSac, HttpServletRequest request){
        String sessionId;
        GioHang gioHang;

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (request.getSession().getAttribute("userId") == null) {
            // Lấy hoặc tạo sessionId
            sessionId = (String) request.getSession().getAttribute("sessionId");
            if (sessionId == null) {
                sessionId = UUID.randomUUID().toString(); // Tạo sessionId duy nhất
                request.getSession().setAttribute("sessionId", sessionId);
            }
            // Tìm giỏ hàng theo sessionId
            gioHang = gioHangService.getGioHangBySessionId(sessionId);
            if (gioHang == null) {
                // Nếu chưa có giỏ hàng, tạo giỏ hàng mới
                gioHang = new GioHang();
                gioHang.setMaGioHang(sessionId);
                gioHang.setNgayTao(LocalDate.now());
                gioHang.setTrangThai(0); // 0: Chưa thanh toán
                gioHangService.savaGioHang(gioHang);
            }
        } else {
            // Nếu người dùng đã đăng nhập, lấy giỏ hàng theo tài khoản
            int userId = (int) request.getSession().getAttribute("userId");
            gioHang = gioHangService.getGioHangByUserId(userId);
            KhachHang khachHang = khachHangService.getKhachHangById(userId);
            // Nếu giỏ hàng chưa có, tạo giỏ hàng mới và gắn với khách hàng
            if (gioHang == null) {
                gioHang = new GioHang();
                gioHang.setKhachHang(khachHang); // Gắn khách hàng vào giỏ hàng
                gioHang.setNgayTao(LocalDate.now());
                gioHang.setTrangThai(0); // 0: Chưa thanh toán
                gioHangService.savaGioHang(gioHang);
            }
            // Nếu có session giỏ hàng tạm thời, gộp vào giỏ hàng đã đăng nhập
            sessionId = (String) request.getSession().getAttribute("sessionId");
            if (sessionId != null) {
                GioHang tempGioHang = gioHangService.getGioHangBySessionId(sessionId);
                if (tempGioHang != null) {
                    gioHangChiTietService.mergeCart(tempGioHang, gioHang);
                    gioHangService.delete(tempGioHang); // Xóa giỏ hàng tạm thời
                    request.getSession().removeAttribute("sessionId"); // Xóa sessionId
                }
            }
        }

            //Lấy sản phẩm chi tiết từ giỏ hàng
            GioHangChiTiet gioHangChiTiet = gioHangChiTietService.getByGioHangIdAndSanPhamIdAndKichThuocIdAndMauSacId(idSanPham,idKichThuoc,idMauSac);
            if(gioHangChiTiet != null){
//                //Hoàn lại số lượng tồn kho
//                SanPhamChiTiet sanPhamChiTiet = gioHangChiTiet.getSanPhamChiTiet();
//                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + gioHangChiTiet.getSoLuong());
//                sanPhamChiTietService.capNhatSanPhamChiTiet(sanPhamChiTiet);

                //Xóa sản phẩm chi tiết khỏi giở hàng
                gioHangChiTietService.deleteGioHangChiTiet(gioHangChiTiet);
            }


        return "redirect:/gio-hang";
    }
    @PostMapping("/cap-nhat-gio-hang")
    public String  capNhatGioHang(@RequestParam(value ="idSanPham",required = false)Integer idSanPham, @RequestParam(value = "idKichThuoc",required = false)Integer idKichThuoc, @RequestParam(value="idMauSac",required = false)Integer idMauSac, @RequestParam(value="soLuong",required = false)Integer soLuong,HttpServletRequest request ,RedirectAttributes redirectAttributes){
        String sessionId;
        GioHang gioHang;

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (request.getSession().getAttribute("userId") == null) {
            // Lấy hoặc tạo sessionId
            sessionId = (String) request.getSession().getAttribute("sessionId");
            if (sessionId == null) {
                sessionId = UUID.randomUUID().toString(); // Tạo sessionId duy nhất
                request.getSession().setAttribute("sessionId", sessionId);
            }
            // Tìm giỏ hàng theo sessionId
            gioHang = gioHangService.getGioHangBySessionId(sessionId);
            if (gioHang == null) {
                // Nếu chưa có giỏ hàng, tạo giỏ hàng mới
                gioHang = new GioHang();
                gioHang.setMaGioHang(sessionId);
                gioHang.setNgayTao(LocalDate.now());
                gioHang.setTrangThai(0); // 0: Chưa thanh toán
                gioHangService.savaGioHang(gioHang);
            }
        } else {
            // Nếu người dùng đã đăng nhập, lấy giỏ hàng theo tài khoản
            int userId = (int) request.getSession().getAttribute("userId");
            gioHang = gioHangService.getGioHangByUserId(userId);
            KhachHang khachHang = khachHangService.getKhachHangById(userId);
            // Nếu giỏ hàng chưa có, tạo giỏ hàng mới và gắn với khách hàng
            if (gioHang == null) {
                gioHang = new GioHang();
                gioHang.setKhachHang(khachHang); // Gắn khách hàng vào giỏ hàng
                gioHang.setNgayTao(LocalDate.now());
                gioHang.setTrangThai(0); // 0: Chưa thanh toán
                gioHangService.savaGioHang(gioHang);
            }
            // Nếu có session giỏ hàng tạm thời, gộp vào giỏ hàng đã đăng nhập
            sessionId = (String) request.getSession().getAttribute("sessionId");
            if (sessionId != null) {
                GioHang tempGioHang = gioHangService.getGioHangBySessionId(sessionId);
                if (tempGioHang != null) {
                    gioHangChiTietService.mergeCart(tempGioHang, gioHang);
                    gioHangService.delete(tempGioHang); // Xóa giỏ hàng tạm thời
                    request.getSession().removeAttribute("sessionId"); // Xóa sessionId
                }
            }
        }

            //Tìm sản phẩm trong giỏ hàng
            GioHangChiTiet gioHangChiTiet = gioHangChiTietService.getByGioHangIdAndSanPhamIdAndKichThuocIdAndMauSacId(idSanPham,idKichThuoc,idMauSac);
            if(gioHangChiTiet != null){
                if(soLuong == null){
                    redirectAttributes.addFlashAttribute("errorMessage","Số lượng không được để trống!");
                    return "redirect:/gio-hang";
                }
                int oldQuantity = gioHangChiTiet.getSoLuong();
                int defference = soLuong - oldQuantity;
                SanPhamChiTiet sanPhamChiTiet = gioHangChiTiet.getSanPhamChiTiet();
                if(sanPhamChiTiet.getSoLuong() < defference){
                    redirectAttributes.addFlashAttribute("errorMessage", "Số lượng trong kho không đủ!");
                    return "redirect:/gio-hang";
                }
                gioHangChiTiet.setSoLuong(soLuong);
                gioHangChiTietService.saveGioHangChiTiet(gioHangChiTiet);

//                //Điều chỉnh số lượng tồn kho
//                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - defference);
//                sanPhamChiTietService.capNhatSanPhamChiTiet(sanPhamChiTiet);
            }

        return "redirect:/gio-hang";
    }
}