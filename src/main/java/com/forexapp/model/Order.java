package com.forexapp.model;

public interface Order {

	default User getUser() {
		return this.getUser();
	}


}
