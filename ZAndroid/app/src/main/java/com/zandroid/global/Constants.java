package com.zandroid.global;

public class Constants
{
    public enum Config
    {
        DEBUG, DEV, RELEASE
    }

    public static final boolean DEBUG = true;
    public static final boolean LOG = true;
    public static final Config config = Config.DEV;
}
