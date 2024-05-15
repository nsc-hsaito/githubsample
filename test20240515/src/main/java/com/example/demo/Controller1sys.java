package com.example.demo;

import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class Controller1sys {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate npJdbcTemplate;
	
	
	//★★★★★toppageハンドラメソッド★★★★★
	@GetMapping("/toppage")
	public String toppage(Model model) {
		//BOOKテーブルのカラム表示＋JOINでSTATUSテーブルのstatusも表示
		String sql = "SELECT * FROM book JOIN status ON book.isbn = status.isbn";

		RowMapper<Book> rowMapper = BeanPropertyRowMapper.newInstance(Book.class);
		List<Book> booklist = jdbcTemplate.query(sql, rowMapper);
		model.addAttribute("books", booklist);
		return "toppage";
	}
	
	
	//★★★★★bookinfoハンドラメソッド★★★★★
	@GetMapping("/bookinfo/{id}")
	public String bookinfo(@PathVariable int id, Model model) {
		
		//書籍の情報をBOOKテーブルから持ってくる
		String sql = "SELECT title, category_id, publish_date, isbn, buyer, buy_date, memo FROM book WHERE id = ?";
		
		Object[] args = new Object[] {id};
		int[] argTypes = new int[] {Types.INTEGER};
		RowMapper<Book> rowMapper = BeanPropertyRowMapper.newInstance(Book.class);
		Book book = jdbcTemplate.queryForObject(sql, args, argTypes, rowMapper);
		model.addAttribute("book", book);
		
		
		
		return "bookinfo";
	}
	
	
	//★★★★★bookformハンドラメソッド★★★★★
	@GetMapping("/bookform")
	public String form(Book book) {
		return "bookform";
	}
		
	
	//★★★★★createハンドラメソッド★★★★★
	@PostMapping("/create")
	public String create(@jakarta.validation.Valid Book book, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "bookform";
		}

		//入力された書籍情報をBOOKテーブルに追加する
		String sql = "INSERT INTO book(title, category_id, publish_date, isbn, buyer, buy_date, memo) VALUES(:title, :category_id, :publish_date, :isbn, :buyer, :buy_date, :memo)";
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(book);
		npJdbcTemplate.update(sql, paramSource);
			
		//入力された書籍情報をSTATUSテーブルに追加する
		String sql2 = "INSERT INTO status(isbn, status) VALUES(:isbn, 0)";
		SqlParameterSource paramSource2 = new BeanPropertySqlParameterSource(book);
		npJdbcTemplate.update(sql2, paramSource2);
		return "redirect:/toppage";
		}
	
	
	//★★★★★rentalbookハンドラメソッド★★★★★
	@GetMapping("/rentalbook/{id}")
	public String rentalbook(@PathVariable int id, RentalBook rentalBook,Model model) {
		
		String sql = "SELECT title, isbn FROM book WHERE id = ?";
		
		 Object[] args = new Object[] {id};
		 int[] argTypes = new int[] {Types.INTEGER};
		 RowMapper<Book> rowMapper = BeanPropertyRowMapper.newInstance(Book.class);
			Book rentalBookId = jdbcTemplate.queryForObject(sql, args, argTypes, rowMapper);
			model.addAttribute("rentalBookId", rentalBookId);
		return "rentalbook";
	}
	
	
	//★★★★★rentalcreateハンドラメソッド★★★★★
	@PostMapping("/rentalcreate")
	public String rentalcreate(@jakarta.validation.Valid RentalBook rentalBook, BindingResult bindingResult) {
		
		//貸出登録で入力された情報をRECORDテーブルに追加する
		String sql = "INSERT INTO record(isbn, rentalname, rent_date, return_date) VALUES(:isbn, :rentalname, :rent_date, :return_date)";
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(rentalBook);
		npJdbcTemplate.update(sql, paramSource);
		
		//貸出登録で入力された書籍のSTATUSテーブルstatusを1(貸出中）にする
		String sql2 = "UPDATE status SET status = 1 WHERE :isbn = isbn";
		SqlParameterSource paramSource2 = new BeanPropertySqlParameterSource(rentalBook);
		npJdbcTemplate.update(sql2, paramSource2);
		
		return "redirect:/toppage";
	}
	
	
	//★★★★★returnbookのハンドラメソッド★★★★★
	@GetMapping("/returnbook/{id}")
	public String returnbook(@PathVariable int id,ReturnBook returnBook, Model model) {
		
		String sql = "SELECT title, isbn, FROM book WHERE id = ?";
		
		Object[] args = new Object[] {id};
		 int[] argTypes = new int[] {Types.INTEGER};
		 RowMapper<Book> rowMapper = BeanPropertyRowMapper.newInstance(Book.class);
			Book returnBookId = jdbcTemplate.queryForObject(sql, args, argTypes, rowMapper);
			model.addAttribute("returnBookId", returnBookId);
		
		return "returnbook";
	}
	
	
	//★★★★★returncreateのハンドラメソッド★★★★★
	@PostMapping("/returncreate")
	public String returncreate(@jakarta.validation.Valid ReturnBook returnBook, BindingResult bindingResult) {
		//返却登録で入力された情報をRECORDテーブルに追加する
		String sql = "INSERT INTO record(isbn, rentalname, rent_date, return_date) VALUES(:isbn, :rentalname, :rent_date, :return_date)";
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(returnBook);
		npJdbcTemplate.update(sql, paramSource);
		
		//貸出登録で入力された書籍のSTATUSテーブルstatusを1(貸出中）にする
		String sql2 = "UPDATE status SET status = 0 WHERE :isbn = isbn";
		SqlParameterSource paramSource2 = new BeanPropertySqlParameterSource(returnBook);
		npJdbcTemplate.update(sql2, paramSource2);
		return "redirect:/toppage";
	}
	
}
