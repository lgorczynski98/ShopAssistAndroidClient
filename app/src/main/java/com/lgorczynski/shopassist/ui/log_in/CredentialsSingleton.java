package com.lgorczynski.shopassist.ui.log_in;

public class CredentialsSingleton {

    private static CredentialsSingleton instance = null;
    private String token;
    public static final String BASE_URL = "http://192.168.1.2:8000/";

    private CredentialsSingleton(){

    }

    public static CredentialsSingleton getInstance(){
        if(instance == null){
            instance = new CredentialsSingleton();
        }
        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
