package com.backend.config;

import jakarta.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VNPAYConfig {

   /* public String createPaymentUrl(HttpServletRequest request) {
        String vnp_TmnCode = "AP79OJ57";
        String vnp_HashSecret = "0H3Q4T3X2YPZA5RVX9A4TKU6IJG7VF5S";
        String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", "1000000"); // Số tiền (VND), nhân 100 lần
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", "123456"); // Mã đơn hàng duy nhất
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang test");
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", "https://yourwebsite.com/vnpay-return");
        vnp_Params.put("vnp_IpAddr", request.getRemoteAddr());

        // Lấy thời gian hiện tại
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(new Date());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        // Tạo checksum (chữ ký bảo mật)
        String signData = VNPayUtils.hashAllFields(vnp_Params, vnp_HashSecret);
        vnp_Params.put("vnp_SecureHash", signData);

        // Tạo URL thanh toán
        String queryUrl = VNPayUtils.createQueryUrl(vnp_Params);
        return vnp_Url + "?" + queryUrl;
    }*/

}
