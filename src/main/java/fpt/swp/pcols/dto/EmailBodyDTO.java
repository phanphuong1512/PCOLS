package fpt.swp.pcols.dto;


import lombok.Builder;

@Builder
public record EmailBodyDTO(String to, String subject, String body) {

}


