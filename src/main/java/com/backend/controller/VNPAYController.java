package com.backend.controller;

import com.backend.dto.response.UserResponse;
import com.backend.entity.UserInfo;
import com.backend.service.UserService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/vnpay")
public class VNPAYController {

    private static final String VNP_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private static final String VNP_TMN_CODE = "AP79OJ57";
    private static final String VNP_SECRET = "0H3Q4T3X2YPZA5RVX9A4TKU6IJG7VF5S";
    private static final String VNP_RETURN_URL = "http://localhost:8888/api/vnpay/callback";

    @Autowired
    private UserService userService;

    @GetMapping("/generateQR")
    public Map<String, String> generateVnpayQR(@RequestParam("amount") String amount) {
        try {
            // Tạo URL thanh toán VNPAY
            String paymentUrl = VNP_URL + "?vnp_TmnCode=" + VNP_TMN_CODE +
                    "&vnp_Amount=" + amount +
                    "&vnp_ReturnUrl=" + VNP_RETURN_URL;

            // Tạo mã QR từ URL
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(paymentUrl, BarcodeFormat.QR_CODE, 300, 300);

            // Chuyển hình ảnh QR thành Base64
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", baos);
            String base64QR = Base64.getEncoder().encodeToString(baos.toByteArray());

            // Trả về URL thanh toán và ảnh QR
            Map<String, String> response = new HashMap<>();
            response.put("paymentUrl", paymentUrl);
            response.put("qrCode", base64QR);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("/callback")
    public ResponseEntity<Map<String, String>> vnpayCallback(@RequestParam Map<String, String> params) {
        String vnp_ResponseCode = params.get("vnp_ResponseCode");
        String amount = params.get("vnp_Amount");
        Map<String, String> response = new HashMap<>();
        System.out.println(amount);
        if ("00".equals(vnp_ResponseCode)) {
            updateUser(amount);
            response.put("status", "success");
            response.put("message", "Thanh toán thành công!");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "fail");
            response.put("message", "Thanh toán thất bại!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    private void updateUser(String amount) {
        UserResponse user=userService.getMyInfo();
        LocalDateTime currentStartDate = user.getStartDate();
        LocalDateTime currentEndDate = user.getEndDate();
        int months = 0;
        // Tính số tháng được cộng thêm
        if(amount.equals("30000")) {
            months = 1;
        }
        if(amount.equals("280000")) {
            months=12;
        }
        System.out.println(months);
        LocalDateTime newStartDate;
        LocalDateTime newEndDate;

        if (currentEndDate == null || currentEndDate.isBefore(LocalDateTime.now())) {
            // Nếu chưa có hoặc đã hết hạn, tính từ ngày hiện tại
            newStartDate = LocalDateTime.now();
            newEndDate = newStartDate.plusMonths(months);
            System.out.println(newStartDate);
            System.out.println(newEndDate);
        } else {
            // Nếu còn hạn, cộng thêm thời gian vào ngày hết hạn hiện tại
            newStartDate = currentStartDate;
            newEndDate = currentEndDate.plusMonths(months);
        }

        // Cập nhật vào DB
        userService.updateDate(user.getEmail(), newStartDate, newEndDate);

    }
}
