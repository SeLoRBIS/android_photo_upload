package com.liha.profile_photo.constants;

public class Constants {

    /** Timeout values */
    public static final int OKHTTP_CONNECT_TIMEOUT = 60;
    public static final int OKHTTP_READ_TIMEOUT = 60;
    public static final int OKHTTP_WRITE_TIMEOUT = 60;

    public static final String SALT = "I hash in SHA_512";

    /**
     * return API REST Url
     * @return url
     */
    public static String getUploadUrlWS() {
        return "http://";
    }

}
