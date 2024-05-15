package com.example.demo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RentalBook { //貸出登録ページ
	
	@NotNull(message = "ISBNコードを入力してください。")
	@Size(min = 13, max = 13, message = "ISBNコードは13桁で入力してください。")
	private String isbn;
	
	@NotBlank(message = "名前を入力してください。")
	@Size(max = 10, message = "名前は10文字以内で入力してください")
	private String rentalname;
	
	@NotBlank(message = "貸出日を選択してください。")
	private String rent_date;
	
	private String return_date = null; //貸出登録だから返却日はnull
}
