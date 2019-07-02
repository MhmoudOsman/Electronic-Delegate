package muhammed.awad.electronicdelegate.Caching;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Logger;

public class Engine {

    private static AppConfiguration appConfig;
    public final static Logger LOGGER = Logger.getLogger("Base_LOGGER");
    public static boolean isLanguageFromApp = true;

    public static void deleteFileRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteFileRecursive(child);
        fileOrDirectory.delete();
    }

    public static void validateCachedData(Context context) {
        AppConfiguration appConfig = CachingManager.getInstance().loadAppConfiguration();
        TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = tel.getNetworkOperator();

        String mcc = "", mnc = "";
        int versionCode = 0;

        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        if (networkOperator != null && networkOperator.length() > 4) {
            mcc = networkOperator.substring(0, 3);
            mnc = networkOperator.substring(3);
        }

        String preferAppLanguage;
        if (appConfig == null) {
            appConfig = new AppConfiguration();
            preferAppLanguage = "en";
        } else {
            preferAppLanguage = appConfig.getLanguage();
        }

        boolean deleteCachedData = appConfig.getAppVersion() != versionCode;
        deleteCachedData = deleteCachedData || !mcc.equalsIgnoreCase(appConfig.getLastKnownMCC());
        deleteCachedData = deleteCachedData || !mnc.equalsIgnoreCase(appConfig.getLastKnownMNC());

        if (deleteCachedData) {
            Engine.LOGGER.info("Delete application cached info");
            Engine.deleteFileRecursive(DataFolder.APP_DATA);
            initialize(context);
            appConfig.setLanguage(preferAppLanguage);
            appConfig.setAppVersion(versionCode);
            appConfig.setLastKnownMCC(mcc);
            appConfig.setLastKnownMNC(mnc);
            CachingManager.getInstance().saveAppConfiguration(appConfig);
        }

        Engine.appConfig = appConfig;
    }

    public static AppConfiguration getAppConfiguration() {
        return appConfig;
    }

    @TargetApi(VERSION_CODES.GINGERBREAD)
    public static boolean isExternalStorageRemovable() {
        if (hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    @TargetApi(VERSION_CODES.FROYO)
    public static File getExternalCacheDir(Context context) {
        if (hasFroyo()) {
            File externalFile = context.getExternalCacheDir();
            return externalFile != null ? externalFile : context.getCacheDir();
        }

        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    private static String getDiskCacheDir(Context context) {
        String cachePath = context.getCacheDir().getPath();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !isExternalStorageRemovable())
            cachePath = getExternalCacheDir(context).getPath();

        return cachePath;
    }

    @SuppressLint("ObsoleteSdkInt")
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD;
    }

    @SuppressLint("ObsoleteSdkInt")
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.FROYO;
    }

    public static void initialize(Context appContext) {
        initializeDataFolders(appContext);
    }

    public static void initializeDataFolders(Context appContext) {
        DataFolder.APP_DATA = appContext.getDir("app_data", Context.MODE_PRIVATE);
        DataFolder.CART_MEDICINE_DATA = appContext.getDir("cart_data", Context.MODE_PRIVATE);
    }

    public static void setApplicationLanguage(ContextWrapper context, String language) {
        if (isLanguageFromApp) {
            String currentLanguage = language != null ? language
                    : context.getResources().getConfiguration().locale.getLanguage();

            if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
                Configuration overrideConfiguration = context.getBaseContext().getResources().getConfiguration();
                overrideConfiguration.setLocale(new Locale(currentLanguage));

            } else {
                Resources res = context.getBaseContext().getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = new Locale(currentLanguage);
                appConfig.setLocale(conf.locale);
                res.updateConfiguration(conf, dm);
            }
        }
    }

    public static File getCacheDir(Context appContext, String DirName) {
        File file = new File(getDiskCacheDir(appContext) + File.separator + DirName);
        if (!file.exists())
            file.mkdirs();
        return file;
    }

    public static File getCacheFile(File parent, String fileName, Context appContext) {
        if (!parent.exists()) {
            try {
                getCacheDir(appContext, parent.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File file = new File(parent + File.separator + fileName + "");
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return file;
    }

    public static boolean isExistingFile(File containerFolder, String fileName) {
        File file = new File(containerFolder + File.separator + fileName + "");
        return file.exists();
    }

    public static class FileName {
        public final static String APP_CONFIGURATION = "app_config.dat";
        public final static String APP_CART_MEDICINE = "app_cart.dat";
        public final static String APP_FILES_EXT = ".txt";
    }

    public static class DataFolder {
        public static File APP_DATA;
        public static File CART_MEDICINE_DATA;
    }

    public static class ExpiryInfo {
        final public static int NO_EXPIRY = 0;
        final public static int EXPIRING_LOCATION_DIRECTIONS = 1;
    }
}
