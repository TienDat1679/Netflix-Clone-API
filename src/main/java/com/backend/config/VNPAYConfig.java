package com.backend.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
@Configuration
public class VNPAYConfig {


     @Getter
     private final String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
     private final String vnp_ReturnUrl = "http://localhost:8888/api/payment/vn-pay-callback";
     private final String vnp_TmnCode = "58X4B4HP";
     @Getter
     private final String secretKey = "VRLDWNVWDNPCOEPBZUTWSEDQAGXJCNGZ";
     private final String vnp_Version = "2.1.0";
     private final String vnp_Command = "pay";
     private final String orderType = "other";

     public Map<String, String> getVNPayConfig() {
          Map<String, String> vnpParamsMap = new HashMap<>();
          vnpParamsMap.put("vnp_Version", this.vnp_Version);
          vnpParamsMap.put("vnp_Command", this.vnp_Command);
          vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
          vnpParamsMap.put("vnp_CurrCode", "VND");
          vnpParamsMap.put("vnp_TxnRef",  VNPayUtil.getRandomNumber(8));
          vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang:" +  VNPayUtil.getRandomNumber(8));
          vnpParamsMap.put("vnp_OrderType", this.orderType);
          vnpParamsMap.put("vnp_Locale", "vn");
          vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl);
          Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
          SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
          String vnpCreateDate = formatter.format(calendar.getTime());
          vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);
          calendar.add(Calendar.MINUTE, 15);
          String vnp_ExpireDate = formatter.format(calendar.getTime());
          vnpParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);
          return vnpParamsMap;
     }

}
