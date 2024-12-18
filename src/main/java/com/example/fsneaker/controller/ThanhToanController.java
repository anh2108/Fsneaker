package com.example.fsneaker.controller;

import com.example.fsneaker.dto.CustomUserDetails;
import com.example.fsneaker.entity.*;
import com.example.fsneaker.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ThanhToanController {
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;
    @Autowired
    private DonHangService donHangService;
    @Autowired
    private GioHangService gioHangService;
    @Autowired
    private GioHangChiTietService gioHangChiTietService;
    @Autowired
    private DonHangChiTietService donHangChiTietService;
    @Autowired
    private VoucherService voucherService;
    @GetMapping("/mua-ngay")
    public String hienThiThanhToanCuaMuaNgay(Model model){
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
        String maGioHang = "DEFAULT_CART";//Mã giỏ hàng mặc định
        GioHang gioHang = gioHangService.getGioHangByMa(maGioHang);
        List<GioHangChiTiet> danhSachChiTiet = gioHangChiTietService.getByGioHangId(gioHang.getId());
        model.addAttribute("danhSachChiTiet", danhSachChiTiet);
        int tongSoLuongTrongGioHang = gioHangChiTietService.getSoLuongTrongGioHang(gioHang.getId());
        model.addAttribute("tongSoLuongTrongGioHang",tongSoLuongTrongGioHang);
        //Tính tổng tiền
        BigDecimal tongTien = gioHang.getGioHangChiTietList().stream()
                .map(item -> item.getGia().multiply(BigDecimal.valueOf(item.getSoLuong()))) // Phép nhân giá và số lượng
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Cộng dồn tổng
        model.addAttribute("gioHang",gioHang);
        model.addAttribute("tongTien",tongTien);
        return "templatekhachhang/thanh-toan";
    }
    @GetMapping("/thanh-toan/{idGioHang}")
    public String chuyenGioHangSangDonHang(@PathVariable int idGioHang,@RequestParam(value = "idKhachHang",required = false)Integer idKhachHang, Model model, Authentication authentication){
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
        //kiểm tra xem người dùng đã đăng nhập chưa
        if(authentication == null || !authentication.isAuthenticated()){
            return "redirect:/login";
        }
        if(idKhachHang != null){
            //Lấy lại giỏ hàng sau khi người dùng thêm sản phẩm
            GioHang gioHang = capNhatGioHangTheoKhachHang(idKhachHang);
            model.addAttribute("gioHang", gioHang);
            //Kiểm tra nếu đã tồn tại đơn hàng "đang chờ" cho khách hàng
            DonHang donHang = donHangService.getDonHangByKhachHangAndTrangThaiAndLoaiDonHang(idKhachHang);

            if (donHang == null) {
                //Nếu chưa có đơn hàng "đang chờ" tạo đơn mới
                donHang = donHangService.chuyenGioHangSangDonHang(idGioHang, idKhachHang);
            }else{
                //nếu đã có, cập nhật lại đơn hàng từ giỏ hàng
                donHangService.capNhatDonHangTuGioHang(donHang,gioHang);
            }
            //Tính lại tổng tiền và áp dụng voucher nếu có
            Voucher voucher = donHang.getGiamGia();

            if (voucher != null) {
                BigDecimal tongTien = donHangService.tinhTongTienGiamGia(donHang);

                if (tongTien.compareTo(voucher.getDonToiThieu()) <0) {
                    // Hủy voucher nếu không còn phù hợp
                    donHang.setGiamGia(null);
                    donHang.setTongTienGiamGia(null);

                    // Cập nhật lại số lượng voucher
                    voucher.setSoLuong(voucher.getSoLuong() + 1);
                    voucherService.themVoucher(voucher);
                    model.addAttribute("message","Tổng tiền không đủ điều kiện để áp voucher này. Đã hủy voucher!");
                } else {
                    donHang.setTongTienGiamGia(donHangService.tinhTongTienGiamGia(donHang));
                }
            }
            BigDecimal phiShip =BigDecimal.valueOf(25000);
            model.addAttribute("phiShip",phiShip);
            model.addAttribute("tongTien", donHang.getTongTienGiamGia() != null ? donHang.getTongTienGiamGia() : donHang.getTongTien());
            //Hiển thị thông tin đơn hàng
            model.addAttribute("donHang",donHang);
            //Lọc voucher phù hợp với tổng tiền mới
            List<Voucher> vouchers = voucherService.getAllVoucherByTrangThaiAndGiaTri(1);
            DonHang finalDonHang = donHang;
            List<Voucher> filteredVouchers = vouchers.stream()
                    .filter(voucher1 -> voucher1.getDonToiThieu().compareTo(finalDonHang.getTongTien()) <= 0) // Chỉ giữ các voucher có đơn tối thiểu <= tổng tiền
                    .collect(Collectors.toList());
            model.addAttribute("vouchers", filteredVouchers);
            List<GioHangChiTiet> danhSachChiTiet = gioHangChiTietService.getByGioHangId(gioHang.getId());
            model.addAttribute("danhSachChiTiet", danhSachChiTiet);
            int demTongSoLuongTrongGioHang = gioHangChiTietService.getSoLuongTrongGioHang(gioHang.getId());
            model.addAttribute("demTongSoLuongTrongGioHang",demTongSoLuongTrongGioHang);
        }

        return "templatekhachhang/thanh-toan";
    }
    @PostMapping("/dat-hang/{idDonHang}")
    public String datHang(@PathVariable int idDonHang,@RequestParam(value = "idKhachHang",required = false) Integer idKhachHang,@RequestParam(value ="idGioHang",required = false)Integer idGioHang, RedirectAttributes redirectAttributes){
        GioHang gioHang = gioHangService.getGioHangByUserId(idKhachHang);
        //Cập nhật trạng thái đơn hàng thành "đang xử lý"
        donHangService.capNhatTrangThaiDonHang(idDonHang, "Chờ xác nhận");
        //Xóa các sản phẩm trong giỏ hàng
        gioHangService.xoaSanPhamTrongGioHang(idGioHang);
        //Thông báo đặt hàng thành công
        redirectAttributes.addFlashAttribute("message", "Đặt hàng thành công!");
        return "redirect:/thanh-toan/"+gioHang.getId() +"?idKhachHang=" + idKhachHang;
    }
    @PostMapping("/thanh-toan/ap-voucher")
    public String apDungVoucher(@RequestParam(value="idKhachHang",required = false)Integer idKhachHang,@RequestParam(value="idDonHang",required = false) Integer idDonHang, @RequestParam(value="idVoucher",required = false) Integer idVoucher, Model model,RedirectAttributes redirectAttributes){
        GioHang gioHang = gioHangService.getGioHangByUserId(idKhachHang);
        if(idDonHang == null){
            model.addAttribute("errorMessage","Đơn hàng không tồn tại!");
            return "redirect:/thanh-toan/"+ gioHang.getId()+"?idKhachHang="+idKhachHang;
        }
        //Lấy thông tin đơn hàng
        DonHang donHang = donHangService.getDonHangById(idDonHang);
        if(donHang == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy đơn hàng!");
            return "redirect:/thanh-toan/" + gioHang.getId()+"?idKhachHang="+idKhachHang;
        }
        //Kiểm tra và áp dụng voucher
        Voucher voucher = voucherService.getVoucherById(idVoucher);
        if(voucher == null){
            redirectAttributes.addFlashAttribute("errorMessage","Mã voucher không tồn tại!");
            return "redirect:/thanh-toan/"+gioHang.getId()+"?idKhachHang="+idKhachHang;
        }
        if(voucher.getNgayKetThuc().isBefore(LocalDateTime.now())){
            redirectAttributes.addFlashAttribute("errorMessage","Mã voucher đã hết hạn!");
            return "redirect:/thanh-toan/"+gioHang.getId()+"?idKhachHang="+idKhachHang;
        }
        //Áp dụng voucher và tính tiên sau khi giảm
        BigDecimal tongTienGiamGia =voucherService.applyVoucher(donHang, voucher);
        int slVoucher = voucher.getSoLuong();
        //Cập nhật hóa đơn
        donHang.setGiamGia(voucher);
        voucher.setSoLuong(slVoucher - 1);
        voucherService.themVoucher(voucher);
        donHang.setTongTienGiamGia(tongTienGiamGia);
        donHangService.capNhatDonHang(donHang);
        redirectAttributes.addFlashAttribute("message","Voucher đã được áp dụng thành công!");
        return "redirect:/thanh-toan/"+gioHang.getId() +"?idKhachHang="+idKhachHang;
    }
    @PostMapping("/thanh-toan/huy-voucher")
    public String huyVoucherKhachHang(@RequestParam(value = "idKhachHang",required = false)Integer idKhachHang,@RequestParam(value = "idDonHang",required = false)Integer idDonHang,Model model, RedirectAttributes redirectAttributes){
        //Kiểm tra và lấy thông tin giỏ hàng
        GioHang gioHang = gioHangService.getGioHangByUserId(idKhachHang);
        if(idDonHang == null){
            redirectAttributes.addFlashAttribute("errorMessage","Đơn hàng không tồn tại!");
            return "redirect:/thanh-toan/"+gioHang.getId() + "?idKhachHang=" + idKhachHang;
        }
        DonHang donHang = donHangService.getDonHangById(idDonHang);
        if(donHang == null){
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy đơn hàng!");
            return "redirect:/thanh-toan/"+gioHang.getId() + "?idKhachHang=" + idKhachHang;
        }
        //Kiểm tra voucher đã áp dụng
        Voucher voucher = donHang.getGiamGia();
        if(voucher == null){
            redirectAttributes.addFlashAttribute("errorMessage","Đơn hàng chưa áp dụng voucher!");
            return "redirect:/thanh-toan/"+gioHang.getId() +"?idKhachHang="+idKhachHang;
        }
        //Gỡ bỏ voucher khỏi đơn hàng
        donHang.setGiamGia(null);
        donHang.setTongTienGiamGia(null); //Reset tổng tiền giảm giá
        donHangService.capNhatDonHang(donHang);

        //Cập nhất số lượng voucher
        voucher.setSoLuong(voucher.getSoLuong() + 1);
        voucherService.themVoucher(voucher);
        redirectAttributes.addFlashAttribute("message","Voucher đã được hủy thành công!");
        return "redirect:/thanh-toan/"+gioHang.getId() + "?idKhachHang=" + idKhachHang;
    }

    public GioHang capNhatGioHangTheoKhachHang(Integer idKhachHang){
        //Lấy giỏ hàng hiện tại
        GioHang gioHang = gioHangService.layGioHangTheoKhachHang(idKhachHang);

        //Tính lại tổng tiền hoặc cập nhật giỏ hàng nếu cần (tủy logic)
        gioHangService.savaGioHang(gioHang);
        return gioHang;
    }
}
