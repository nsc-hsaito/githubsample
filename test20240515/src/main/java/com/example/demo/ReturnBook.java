package com.example.demo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReturnBook { //返却登録ページ
	@NotBlank(message = "ISBNコードを入力してください。")
	@Size(min = 13, max = 13, message = "ISBNコードは13桁で入力してください。")
	private String isbn;
	
	@NotBlank(message = "名前を入力してください。")
	@Size(max = 10, message = "名前は10文字以内で入力してください")
	private String rentalname;

	private String rent_date = null; //返却登録だから貸出日はnull
	
	@NotBlank(message = "返却日を選択してください。")
	private String return_date;
}
