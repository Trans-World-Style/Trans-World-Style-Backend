package com.example.transback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@ToString
@Entity(name="video")
@Data
@NoArgsConstructor
public class VideoVO {

	@Id
	private int video_id;
	private String video_link;
}
