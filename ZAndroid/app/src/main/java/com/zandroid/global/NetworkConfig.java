package com.zandroid.global;

public class NetworkConfig {

    public static String BoxServiceAddress = "";


    //开发
    public static final String BoxServiceAddress_dev = "http://api.ebox.dev.gegebox.com";
    //正式
    public static final String BoxServiceAddress_release = "http://api.ebox.gegebox.com";
    //调试
    public static final String BoxServiceAddress_debug = "http://api.ebox.dev.gegebox.com";


    static {
        new NetworkConfig();
    }

    private NetworkConfig() {
        switch (Constants.config) {
            case DEBUG:
                BoxServiceAddress = BoxServiceAddress_debug;
                break;

            case DEV:
                BoxServiceAddress = BoxServiceAddress_dev;
                break;

            case RELEASE:
                BoxServiceAddress = BoxServiceAddress_release;
                break;
            default:
                break;
        }
    }
}
