package application.entity;

import java.io.Serializable;

public class ContactEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String title;
	private String firstName;
	private String lastName;
	private String email;
	private String mobile;
	private String address;
	private byte[] picture;
	
	/* Default constructor */ 
	public ContactEntity() {
		
	}
	
	/* Constructor */
	public ContactEntity(String title, String firstName, String lastName, String email, String phoneNum, String address, byte[] picture) {
		
		this.title = title;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.mobile = phoneNum;
		this.address = address;
		this.picture = picture;
		
		
	}
	
	/* Get() and Set() for All */
	
	public String getTitle() {
		
		return this.title;
		
	}
	
	public void setTitle(String title) {
		
		this.title = title;
		
	}
	
	public String getFirstName() {
		
		return this.firstName;
		
	}
	
	public void setFirstName(String firstName) {
		
		this.firstName = firstName;
		
	}
	
	public String getLastName() {
		
		return this.lastName;
		
	}
	
	public void setLastName(String lastName) {
		
		this.lastName = lastName;
		
	}
	
	public String getEmail() {
		
		return this.email;
		
	}
	
	public void setEmail(String email) {
		
		this.email = email;
		
	}
	
	public String getMobile() {
		
		return this.mobile;
		
	}
	
	public void setMobile(String mobile) {
		
		this.mobile = mobile;
		
	}
	
	public String getAddress() {
		
		return this.address;
		
	}
	
	public void setAddress(String address) {
		
		this.address = address;
		
	}
	
	public byte[] getPicture() {
		
		return this.picture;
		
	}
	
	public void setPicture(byte[] picture) {
		
		this.picture = picture;
		
	}

}
