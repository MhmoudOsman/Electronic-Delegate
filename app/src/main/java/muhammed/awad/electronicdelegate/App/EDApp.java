package muhammed.awad.electronicdelegate.App;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

@SuppressLint("Registered")
public class EDApp extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context = null;
    private static EDApp application = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        application = this;
    }

    public static Context getContext() {
        return context;
    }

    static public EDApp getApplication() {
        return application;
    }
}
