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
@Entity(name="video")
@Data
@NoArgsConstructor
public class VideoDTO {

	@Id
	private int video_id;
	private String email;
	private String video_link;
	private String upload_url;
	private String output_url;
	private String video_name;
	private LocalDateTime upload_time;
	private Integer delete_state;
	private LocalDateTime delete_time;
	private Integer upscale_state;
	private int waiting_rank;
	private int waiting_time;

}
