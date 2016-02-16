package com.zandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by prin on 2016/2/16.
 *
 * 设备相关信息及功能工具类
 * （1）判断网络是否有效
 * （2）GPS是否打开，需要<uses-permission
 * android:name="android.permission.ACCESS_FINE_LOCATION" />权限
 * （3）判断当前网络是否是移动数据网络
 * （4）判断外部存储是否可用
 * （5）获得设备的ANDROID_ID
 * （6）获得系统Android版本
 * （7）获得设备名称
 * （8）获得CPU名称
 * （9）SIM卡是否存在
 * （8）打电话
 */
public class ZDeviceInfoUtil {

    public static NetworkInfo getActiveNetwork(Context context) {
        if (context == null) {
            return null;
        }
        ConnectivityManager mConnMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnMgr == null) {
            return null;
        }
        NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo();
        return aActiveInfo;
    }

    public static String getNetType(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn != null) {
            NetworkInfo info = conn.getActiveNetworkInfo();
            if (info != null) {
                String type = info.getTypeName().toLowerCase(
                        Locale.getDefault());
                if (type.equals("wifi")) {
                    return type;
                } else if (type.equals("mobile")) {
                    String apn = info.getExtraInfo().toLowerCase(
                            Locale.getDefault());
                    if (apn != null
                            && (apn.equals("cmwap") || apn.equals("3gwap")
                            || apn.equals("uniwap") || apn
                            .equals("ctwap"))) {
                        return "wap";
                    } else {
                        return apn;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 描述：判断网络是否有效.
     *
     * @param context
     *            the context
     * @return true, if is network available
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Gps是否打开 需要<uses-permission
     * android:name="android.permission.ACCESS_FINE_LOCATION" />权限
     *
     * @param context
     *            the context
     * @return true, if is gps enabled
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 判断当前网络是否是移动数据网络.
     *
     * @param context
     *            the context
     * @return boolean
     */
    public static boolean isMobile(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    /**
     * 判断外部存储是否可用
     * @return
     */
    public static boolean isExistSdCard() {
        try {
            return Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            ZLogUtil.logException(e);
        }
        return false;
    }

    /**
     * 获得设备的ANDROID_ID
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        return Settings.Secure
                .getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getSysVersion(Context context) {
        String release = android.os.Build.VERSION.RELEASE;
        return release;
    }

    public static String getDeviceName(Context context) {
        String model = android.os.Build.MODEL;
        return model;
    }

    public static String getCpuName() {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader("/proc/cpuinfo");
            br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fr != null)
                try {
                    fr.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        return null;
    }

    public static boolean isSimExist(Context context) {
        final TelephonyManager mTelephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int simState = mTelephonyManager.getSimState();

        if (simState == TelephonyManager.SIM_STATE_ABSENT
                || simState == TelephonyManager.SIM_STATE_UNKNOWN) {
            return false;
        }

        return true;
    }

    public static void startCall(Context context, String uri) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + uri));
        context.startActivity(intent);
    }

}
