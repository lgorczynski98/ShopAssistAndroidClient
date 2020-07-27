package com.lgorczynski.shopassist.ui;

public class CredentialsSingleton {

    private static CredentialsSingleton instance = null;
    private String token;
    public static final String BASE_URL = "http://192.168.1.2:8000/";
    public static final String RECEIPTS_IMAGE_BASE_URL = CredentialsSingleton.BASE_URL + "receipts/image/";
    public static final String RECEIPTS_THUMBNAIL_BASE_URL = CredentialsSingleton.BASE_URL + "receipts/thumbnail/";
    public static final String LOYALTYCARDS_IMAGE_BASE_URL = CredentialsSingleton.BASE_URL + "loyaltycards/image/";
    public static final int PREF_THUMBNAIL_WIDTH = 200;
    public static final int PREF_THUMBNAIL_HEIGHT = 200;

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
