package com.example.prm391x_searchfood_vietcvfx12045.model;

public class User {
	private String email = "";
	private String password = "";
	private String message = "";

	public User() {

	}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean kiemtra() {

		if (!email.matches("\\w+@\\w+\\.\\w+") && !email.matches("\\w+\\.\\w+@\\w+\\.\\w+")) {
			message = "Email: text@text.text | text.text@text.text";
			return false;
		}
		
		if (password.length() < 8) {
			message = "Mật khẩu phải từ 8 ký tự trở lên";
			return false;

		} else if (password.matches("\\w*\\s+\\w*")) {
			message = "Mật khẩu không được chứa KHOẢNG TRỐNG";
			return false;
		}

		message = "Email & Password đã đúng cú pháp yêu cầu";

		return true;
	}

}
