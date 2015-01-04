package com.example.fracker.model;

public class UserLogin {

    private static UserLogin mInstance = null;

    public User userLogin;

    protected UserLogin(){}

    public static synchronized UserLogin getInstance(){
    	if(null == mInstance){
    		mInstance = new UserLogin();
    	}
    	return mInstance;
    }

}
