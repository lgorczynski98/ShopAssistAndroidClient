package com.lgorczynski.shopassist.ui;

public class CredentialsSingleton {

    private static CredentialsSingleton instance = null;
    private String token;
    private String username;
    private String email;
    private int userID;
    private String deviceRegistrationToken;
    public static final String BASE_URL = "https://shopassist.azurewebsites.net/";
    public static final String SERVER_CONNECTION_ADDRESS = "https://shopassist.azurewebsites.net";
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getDeviceRegistrationToken() {
        return deviceRegistrationToken;
    }

    public void setDeviceRegistrationToken(String deviceRegistrationToken) {
        this.deviceRegistrationToken = deviceRegistrationToken;
    }
}
