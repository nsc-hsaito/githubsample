package com.example.demo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Book {
	private Integer id;
	
	@NotBlank(message = "タイトルを入力してください。")
	@Size(max = 30, message = "タイトルは30文字以内で入力してください。")
	private String title;
	
	private Integer category_id;
	
	@NotBlank(message = "発行日を入力してください。")
	private String publish_date;
	
	@NotBlank(message = "ISBNを入力してください。")
	@Size(min = 13, max = 13, message="ISBNは13桁で入力してください。")
	private String isbn;
	
	@NotBlank(message = "購入者を入力してください。")
	private String buyer;
	
	@NotBlank(message = "購入日を入力してください。")
	private String buy_date;
	
	private String memo;
	
	private int status;
}
