package com.backend.model;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {

}
