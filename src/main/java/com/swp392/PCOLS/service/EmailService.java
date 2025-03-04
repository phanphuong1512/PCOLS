package com.swp392.PCOLS.service;

import com.swp392.PCOLS.dto.EmailBodyDTO;

public interface EmailService {
    void sendSimpleMail(EmailBodyDTO emailBodyDTO);
}