package com.example.fsneaker.service;

import com.example.fsneaker.entity.DonHang;
import com.example.fsneaker.entity.DonHangChiTiet;
import com.example.fsneaker.repositories.DonHangRepo;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PhieuGiaoHangService {

    @Autowired
    private DonHangRepo donHangRepo;


    public String inPhieuGiao(Integer orderId) {
        DonHang order = donHangRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        String filePath = "in_phieu_giao_" + orderId + ".pdf"; // Đường dẫn file PDF

        try {
            // Đường dẫn tới font hỗ trợ Unicode
            String fontPath = "src/main/resources/static/fonts/DejaVuSans.ttf";

            // Khởi tạo PDF writer
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Tải font Unicode
            PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);

            // Thiết lập font cho tài liệu
            document.setFont(font);

            // Tiêu đề
            document.add(new Paragraph("PHIẾU GIAO HÀNG\n\n")
                    .setFontSize(16)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            Table table = new Table(2); // Tạo bảng với 2 cột

            // Tạo ô cho bên gửi
            Cell benGuiCell = new Cell()
                    .add(new Paragraph()
                            .add(new Text("Bên gửi: \n").setBold())
                            .add("Tên: Fsneaker\n")
                            .add("Địa chỉ: Trường cao đẳng FPT Polytechnic Hà Nội\n")
                            .add("SĐT: 0947808444"))
                    .setBorder(Border.NO_BORDER); // Xóa đường viền ô

            // Tạo ô cho bên nhận
            Cell benNhanCell = new Cell()
                    .add(new Paragraph()
                            .add(new Text("Bên nhận: \n").setBold())
                            .add("Tên: " + order.getKhachHang().getTenKhachHang() + "\n")
                            .add("Địa chỉ: " + order.getKhachHang().getDiaChi() + "\n")
                            .add("SĐT: " + order.getKhachHang().getSoDienThoai()))
                    .setBorder(Border.NO_BORDER); // Xóa đường viền ô

            // Thêm các ô vào bảng
            table.addCell(benGuiCell);
            table.addCell(benNhanCell);

            // Thêm bảng vào document
            document.add(table);
            // Nội dung đơn hàng
            document.add(new Paragraph("\nNội dung đơn hàng:")
                    .setBold());
            Table table1 = new Table(2); // Bảng với 2 cột
            table1.setWidth(UnitValue.createPercentValue(100)); // Set chiều rộng 100%

            // Thêm tiêu đề cho bảng
            table1.addHeaderCell(new Cell().add(new Paragraph("Tên sản phẩm").setBold()));
            table1.addHeaderCell(new Cell().add(new Paragraph("Số lượng").setBold()));

            // Thêm dữ liệu sản phẩm
            for (DonHangChiTiet chiTiet : order.getDonHangChiTiets()) {
                // Tạo đoạn văn hiển thị thông tin sản phẩm, màu sắc, và kích thước
                String productDetails = chiTiet.getSanPhamChiTiet().getSanPham().getTenSanPham() + " - " +
                        chiTiet.getSanPhamChiTiet().getMauSac().getTenMauSac() + " - " +
                        chiTiet.getSanPhamChiTiet().getKichThuoc().getTenKichThuoc();

                // Thêm thông tin vào cột "Tên sản phẩm"
                table1.addCell(
                        new Cell().add(
                                new Paragraph(productDetails)
                                        .setFontSize(12) // Kích thước chữ
                        )
                );

                // Thêm số lượng vào cột "Số lượng"
                table1.addCell(
                        new Cell().add(
                                new Paragraph(String.valueOf(chiTiet.getSoLuong()))
                                        .setFontSize(12) // Kích thước chữ
                        )
                );
            }
            document.add(table1);
            // Tổng tiền
            document.add(new Paragraph("\nTổng tiền thu: " + (order.getTongTienGiamGia() == null ? order.getTongTien() : order.getTongTienGiamGia()) + " VND")
                    .setBold());

            // Chữ ký
            document.add(new Paragraph("\n\nChữ ký người nhận:")
                    .setBold());
            document.add(new Paragraph("(Xác nhận nguyên vẹn, không móp/méo)")
                    .setItalic());

            // Đóng tài liệu
            document.close();
            System.out.println("Phiếu giao hàng đã được tạo tại: " + filePath);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo phiếu giao hàng.", e);
        }
        return filePath;
    }

    public String inHoaDon(Integer orderId) {
        DonHang order = donHangRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        String filePath = "in_hoa_don_" + orderId + ".pdf"; // Đường dẫn file PDF

        try {
            // Đường dẫn tới font hỗ trợ Unicode
            String fontPath = "src/main/resources/static/fonts/DejaVuSans.ttf";

            // Khởi tạo PDF writer
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            // Tải font Unicode
            PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);

            // Thiết lập font cho tài liệu
            document.setFont(font);

            Table tablel = new Table(1); // Tạo bảng với 1 cột

            Cell logoCell = new Cell()
                    .add(new Paragraph()
                            .add(new Text("FSNEAKER\n").setBold().setFontSize(16))
                            .add(new Text("Số điện thoại: 0947808444\n"))
                            .add(new Text("Email: Fsneaker123@gmail.com\n"))
                            .add(new Text("Địa chỉ: Đường Trịnh Văn Bô, Xuân Phương, Nam Từ Liêm, Hà Nội\n")))
                    .setBorder(Border.NO_BORDER) // Xóa viền ô
                    .setTextAlignment(TextAlignment.CENTER); // Căn giữa văn bản trong ô

            tablel.setHorizontalAlignment(HorizontalAlignment.CENTER); // Căn giữa bảng
            tablel.addCell(logoCell);
            document.add(tablel); // Thêm bảng vào document

            // Tiêu đề
            document.add(new Paragraph("HOÁ ĐƠN BÁN HÀNG\n\n")
                    .setFontSize(16)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            Table table = new Table(2); // Tạo bảng với 2 cột

            // Tạo ô cho bên gửi
            Cell benGuiCell = new Cell()
                    .add(new Paragraph()
                            .add("Khách hàng: " + order.getKhachHang().getTenKhachHang() + " \n")
                            .add("Địa chỉ: " + order.getKhachHang().getDiaChi() + "\n")
                            .add("Số điện thoại: " + order.getKhachHang().getSoDienThoai() + " \n")
                            .add("Ghi chú" + "\n"))
                    .setBorder(Border.NO_BORDER); // Xóa đường viền ô
            // Tạo ô cho bên nhận
            Cell benNhanCell = new Cell()
                    .add(new Paragraph()
                            .add("Mã hoá đơn: " + order.getMaDonHang() + "\n")
                            .add("Ngày tạo: " + order.getNgayTao() + "\n")
                            .add("Email: " + order.getKhachHang().getEmail()))
                    .setBorder(Border.NO_BORDER); // Xóa đường viền ô

            // Thêm các ô vào bảng
            table.addCell(benGuiCell);
            table.addCell(benNhanCell);

            // Thêm bảng vào document
            document.add(table);
            // Nội dung đơn hàng
            document.add(new Paragraph("\nNội dung đơn hàng:")
                    .setBold());
            Table table1 = new Table(4); // Bảng với 2 cột
            table1.setWidth(UnitValue.createPercentValue(100)); // Set chiều rộng 100%

            // Thêm tiêu đề cho bảng
            table1.addHeaderCell(new Cell().add(new Paragraph("Tên sản phẩm").setBold()));
            table1.addHeaderCell(new Cell().add(new Paragraph("Số lượng").setBold()));
            table1.addHeaderCell(new Cell().add(new Paragraph("Đơn giá").setBold()));
            table1.addHeaderCell(new Cell().add(new Paragraph("Thành tiền").setBold()));

            // Thêm dữ liệu sản phẩm
            for (DonHangChiTiet chiTiet : order.getDonHangChiTiets()) {
                // Tạo đoạn văn hiển thị thông tin sản phẩm, màu sắc, và kích thước
                String productDetails = chiTiet.getSanPhamChiTiet().getSanPham().getTenSanPham() + " - " +
                        chiTiet.getSanPhamChiTiet().getMauSac().getTenMauSac() + " - " +
                        chiTiet.getSanPhamChiTiet().getKichThuoc().getTenKichThuoc();

                // Thêm thông tin vào cột "Tên sản phẩm"
                table1.addCell(
                        new Cell().add(
                                new Paragraph(productDetails)
                                        .setFontSize(12)
                        )
                );

                // Thêm số lượng vào cột "Số lượng"
                table1.addCell(
                        new Cell().add(
                                new Paragraph(String.valueOf(chiTiet.getSoLuong()))
                                        .setFontSize(12)
                        )
                );

                table1.addCell(
                        new Cell().add(
                                new Paragraph(String.valueOf(chiTiet.getGia()))
                                        .setFontSize(12)
                        )
                );

                table1.addCell(
                        new Cell().add(
                                new Paragraph(String.valueOf(chiTiet.getThanhTien()))
                                        .setFontSize(12)
                        )
                );
            }
            document.add(table1);
            // Tổng tiền
            Table table2 = new Table(2); // Tạo bảng với 2 cột

            // Tạo ô cho bên gửi
            Cell tongTien = new Cell()
                    .add(new Paragraph()
                            .add("\nTổng tiền hàng: " + order.getTongTien() + " VND\n")
                            .add("Giảm giá: " + (order.getGiamGia() != null ? (order.getGiamGia().getLoaiVoucher() == "Giảm giá số tiền" ? (order.getGiamGia().getGiaTri() + " VND") : (order.getGiamGia().getGiaTri() + " %")) : "0") + "\n")
                            .add("Phải thanh toán: " + (order.getTongTienGiamGia() == null ? order.getTongTien() : order.getTongTienGiamGia()) + " VND\n")
                            .add("Trạng thái: " + order.getTrangThai()))
                    .setBorder(Border.NO_BORDER); // Xóa đường viền ô

            table2.addCell(tongTien);

            // Thêm bảng vào document
            document.add(table2);

            // Đóng tài liệu
            document.close();
            System.out.println("Phiếu giao hàng đã được tạo tại: " + filePath);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo phiếu giao hàng.", e);
        }
        return filePath;
    }
}
