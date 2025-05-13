package com.backend.controller;

import com.backend.dto.response.PaymentDTO;
import com.backend.dto.response.ResponseObject;
import com.backend.dto.response.UserResponse;
import com.backend.entity.UserInfo;
import com.backend.service.PaymentService;
import com.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class VNPAYController {

    private static final String VNP_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private static final String VNP_TMN_CODE = "AP79OJ57";
    private static final String VNP_SECRET = "0H3Q4T3X2YPZA5RVX9A4TKU6IJG7VF5S";
    private static final String VNP_RETURN_URL = "http://localhost:8888/api/vnpay/callback";

    @Autowired
    private UserService userService;

    private final PaymentService paymentService;

    @GetMapping("/vn-pay")
    public ResponseObject<PaymentDTO.VNPayResponse> pay(HttpServletRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request));
    }

    @GetMapping("/vn-pay-callback")
    public ResponseObject<PaymentDTO.VNPayResponse> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            updateUser(request.getParameter("vnp_Amount"));
            return new ResponseObject<>(HttpStatus.OK, "Success", new PaymentDTO.VNPayResponse("00", "Success", ""));
        } else {
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Failed", null);
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
