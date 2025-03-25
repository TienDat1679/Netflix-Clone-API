package com.backend.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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

    @GetMapping("/generateQR")
    public Map<String, String> generateVnpayQR(@RequestParam("amount") int amount, @RequestParam("userId") String userId) {
        try {
            // Tạo URL thanh toán VNPAY
            String paymentUrl = VNP_URL + "?vnp_TmnCode=" + VNP_TMN_CODE +
                    "&vnp_Amount=" + (amount * 100) +
                    "&vnp_OrderInfo=" + "Thanh toan cho user: " + userId +
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
    public ResponseEntity<String> vnpayCallback(@RequestParam Map<String, String> params) {
        String vnp_ResponseCode = params.get("vnp_ResponseCode");
        if ("00".equals(vnp_ResponseCode)) {
            return ResponseEntity.ok("Thanh toán thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thanh toán thất bại!");
        }
    }
}
