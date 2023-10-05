package com.library.dto;

public class Account {
	
	private String userName;
	private Double walletAmount;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Double getWalletAmount() {
		return walletAmount;
	}
	public void setWalletAmount(Double walletAmount) {
		this.walletAmount = walletAmount;
	}
}
