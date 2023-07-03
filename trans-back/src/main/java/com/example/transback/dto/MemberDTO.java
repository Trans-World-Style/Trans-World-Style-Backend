package com.example.transback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@AllArgsConstructor
@ToString
@Entity(name="member")
@Data
@NoArgsConstructor
public class MemberDTO {

	@Id
	private String email;
	private String member_name;
	private String google_id;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
}
