package com.example.fsneaker.service;

import com.example.fsneaker.entity.DonHang;
import com.example.fsneaker.entity.DonHangChiTiet;
import com.example.fsneaker.repositories.DonHangChiTietRepo;
import com.example.fsneaker.repositories.DonHangRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class DonHangService {
    @Autowired
    private DonHangRepo donHangRepo;
    @Autowired
    private DonHangChiTietRepo donHangChiTietRepo;
    public List<DonHang> getDonHangByTrangThai(){
        return donHangRepo.findByTrangThai("Ðang chờ");
    }
    public DonHang getDonHangById(int id){
        return donHangRepo.findById(id).orElse(null);
    }
    public void capNhatDonHang(DonHang donHang) {
        donHangRepo.save(donHang);
    }
    public Boolean themHoaDon(DonHang donHang){
        try{
            donHangRepo.save(donHang);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean kiemTraSanPhamTrongDonHang(int idDonHang, int idSanPhamChiTiet){
        return donHangChiTietRepo.existsByDonHangIdAndSanPhamChiTietId(idDonHang, idSanPhamChiTiet);
    }
    public List<DonHang> getDonHangs(){
        return donHangRepo.findAll();
    }
    public String taoMaDonHang(){
        String format = "00000";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String part1 = now.format(formatter);
        String part2 = String.format("%05d", new Random().nextInt(100000));
        return "HD" + part1 + part2;
    }
    public double tinhTongThuNhap(){
        List<DonHang> donHangs = donHangRepo.findByTrangThai("Đã thanh toán");
        return donHangs.stream()
                .mapToDouble(donHang -> donHang.getGiamGia() != null ? donHang.getTongTienGiamGia() : donHang.getTongTien())
                .sum();
    }
    public Long tongDonHang(){
        return donHangRepo.countByTrangThaiEquals("Đã thanh toán");
    }

    public int tongSanPhamDaBan(){
        List<DonHang> donHangs = donHangRepo.findByTrangThaiEquals("Đã thanh toán");

        //Tính tổng số lượng sản phẩm từ chi tiết đơn hàng
        return donHangs.stream()
                .flatMap(donHang -> donHang.getDonHangChiTiets().stream())
                .mapToInt(DonHangChiTiet::getSoLuong)
                .sum();
    }
    public int calculateToTalSoldProductsInLastWeek(){
        //Lấy ngày hôm nay và ngày cách đây 1 tuần
        LocalDate endDate =LocalDate.now();
        LocalDate startDate = endDate.minusWeeks(1);

        //Lấy tất cả các đơn hàng đã thanh toán trong tuần qua
        List<DonHang> donHangs = donHangRepo.findByNgayMuaBetweenAndTrangThaiEquals(startDate,endDate,"Đã thanh toán");

        //Tính tổng số lượng sản phẩm từ chi tiết đơn hàng
        return donHangs.stream()
                .flatMap(donHang -> donHang.getDonHangChiTiets().stream())
                .mapToInt(DonHangChiTiet::getSoLuong)
                .sum();
    }

    public Map<String , Long> getDonHangTheoTrangThai(){
        List<Object[]> results = donHangRepo.demDonHangTheoTrangThai();
        Map<String, Long> donHangTT = new HashMap<>();
        for(Object[] result :results){
            String trangThai = (String) result[0];
            Long count = (Long) result[1];
            donHangTT.put(trangThai,count);
        }
        return donHangTT;
    }
    //Lấy tổng thu nhập theo ngày trong tháng hiện tại
    public List<Object[]> getThuNhapTheoThang(){
        return donHangRepo.findThuNhapTheoThang();
    }
    //Lấy số đơn hàng theo ngày trong tháng hiện tại
    public List<Object[]> getDonHangTheoThang(){
        return donHangRepo.findDonHangTheoThang();
    }
    //Lấy tổng thu nhập theo tháng trong năm hiện tại
    public List<Object[]> getThuNhapTheoNam(){
        return donHangRepo.findThuNhapTheoNam();
    }
    //Lấy số đơn hàng theo tháng trong năm hiện tại
    public List<Object[]> getDonHangTheoNam(){
        return donHangRepo.findDonHangTheoNam();
    }
    //    public Map<String, Long> thongKeDonHangTheoTrangThai(){
//        Map<String, Long> result = new HashMap<>();
//        result.put("Đang chờ", donHangRepo.countByTrangThai("Đang chờ"));
//        result.put("Đang xử lý", donHangRepo.countByTrangThai("Đang xử lý"));
//        result.put("Đã giao", donHangRepo.countByTrangThai("Đã giao"));
//        result.put("Đã thanh toán", donHangRepo.countByTrangThai("Đã thanh toán"));
//        result.put("Đã hủy", donHangRepo.countByTrangThai("Đã hủy"));
//        return result;
//    }
    public List<Object[]> getSoDonHangTheoTrangThai(){
        return donHangRepo.demDonHangTheoTrangThai();
    }
    public Page<Object[]> thongKeKhachHangTheoTongTien(LocalDate startDate, LocalDate endDate, int page, int size){
        return donHangRepo.thongKeKhachHangTheoTongTien(startDate,endDate, PageRequest.of(page,size));
    }

    public Page<Object[]> thongKeKhachHangTheoSoLanMua(LocalDate startDate, LocalDate endDate, int page, int size){
        return donHangRepo.thongKeKhachHangTheoSoLanMua(startDate,endDate, PageRequest.of(page,size));
    }
    public Page<Object[]> thongKeKHTheoTongTien( int page, int size){
        return donHangRepo.thongKeKHTheoTongTien(PageRequest.of(page,size));
    }

    public Page<Object[]> thongKeKHTheoSoLanMua( int page, int size){
        return donHangRepo.thongKeKHTheoSoLanMua(PageRequest.of(page,size));
    }
    //Hủy đơn hàng
    public DonHang getDonHangByMa(String maDonHang){
        return donHangRepo.findByMaDonHang(maDonHang);
    }
    public Boolean capNhatTrangThai(DonHang donHang){
        try{
            donHangRepo.save(donHang);//Save cập nhật trạng thái
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public Double tinhTongThuNhapTheoThoiGian(LocalDate startDate, LocalDate endDate,String trangThai){
        Double tongThuNhap = donHangRepo.tinhTongThuNhap(startDate,endDate);
        if(tongThuNhap == null || !"Đã thanh toán".equals(trangThai)){
            return 0.0;
        }
        return tongThuNhap;
    }
    public Long tinhTongKhachHangTheoThoiGian(LocalDate startDate, LocalDate endDate){
        return donHangRepo.tinhTongKhachHang(startDate,endDate);
    }
    public Long tinhTongDonHangTheoThoiGian(LocalDate startDate,LocalDate endDate){
        return donHangRepo.tinhTongDonHang(startDate, endDate);
    }
    public Long tinhTongSanPhamDaBanTheoThoiGian(LocalDate startDate, LocalDate endDate, String trangThai){
        Long tongSanPham = donHangRepo.tinhTongSanPhamDaBan(startDate, endDate);
        if(tongSanPham == null || !"Đã thanh toán".equals(trangThai)){
            return 0L;
        }
        return tongSanPham;
    }
    public void updateSanPhamQuantity(Integer idDonHang, Integer idSanPhamChiTiet, Integer soLuong){
        DonHangChiTiet chiTiet = donHangChiTietRepo.findByDonHangIdAndSanPhamChiTietId(idDonHang,idSanPhamChiTiet);
        if(chiTiet != null){
            chiTiet.setSoLuong(soLuong); //Cập nhật số lượng
            chiTiet.setThanhTien(soLuong * chiTiet.getGia());
            donHangChiTietRepo.save(chiTiet); //Lưu vào database
        }
    }
}
