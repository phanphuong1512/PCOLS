package com.swp392.PCOLS.dto;


import lombok.Builder;

@Builder
public record EmailBodyDTO(String to, String subject, String body) {

}


