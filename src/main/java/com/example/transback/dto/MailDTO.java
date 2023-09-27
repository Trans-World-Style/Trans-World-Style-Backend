package com.example.transback.dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
public class MailDTO {
	private String address;
	private String title;
	private String content;
}
