package com.my.closet.clothes.vo;

import org.springframework.stereotype.Component;

@Component("clothesVO")
public class ClothesVO {
	private int no; // PRIMARY KEY. AUTO INCREMENT
	private String name;
	private String category; // not null
	private String brand;
	private String color;
	private String season;
	private String cloSize;
	private String img;
	private String fileName;
	private String filePath;
	private String like;
	private String userID; // FOREIGN KEY(CLOSET). not null 
	private String closetName; // FOREIGN KEY(CLOSET). not null 
	private String date;
	
	//임시
	private int pageStart =-1;
	private int pageSize =-1;

	
	public int getPageStart() {
		return pageStart;
	}

	public void setPageStart(int pageStart) {
		this.pageStart = pageStart;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	

	//생성자
	public ClothesVO() {
		System.out.println("ClothesVO 생성자 호출");
	}
	
	public ClothesVO(int no) {
		System.out.println("ClothesVO 생성자 호출");
		this.no = no;
	}

	public ClothesVO(String userID) {
		System.out.println("ClothesVO 생성자 호출");
		this.userID = userID;
	}
	
	public ClothesVO(String userID, String closetName, String fileName) {
		System.out.println("ClothesVO 생성자 호출");
		this.userID = userID;
		this.closetName = closetName;
		this.fileName = fileName;
	}
	
	//getter & setter
	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getCloSize() {
		return cloSize;
	}

	public void setCloSize(String cloSize) {
		this.cloSize = cloSize;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getLike() {
		return like;
	}

	public void setLike(String like) {
		this.like = like;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getClosetName() {
		return closetName;
	}

	public void setClosetName(String closetName) {
		this.closetName = closetName;
	}
}
