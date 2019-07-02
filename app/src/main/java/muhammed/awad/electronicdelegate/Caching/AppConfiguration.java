package muhammed.awad.electronicdelegate.Caching;

import java.io.Serializable;
import java.util.Locale;

public class AppConfiguration implements Serializable {

    private static final long serialVersionUID = -1359480939951191822L;

    private int versionCode;
    private String language;
    private String theme;
    private String lastKnownMCC;
    private String lastKnownMNC;
    private Locale locale;

    public int getAppVersion() {
        return versionCode;
    }

    public void setAppVersion(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLastKnownMCC() {
        return lastKnownMCC;
    }

    public void setLastKnownMCC(String lastKnownMCC) {
        this.lastKnownMCC = lastKnownMCC;
    }

    public String getLastKnownMNC() {
        return lastKnownMNC;
    }

    public void setLastKnownMNC(String lastKnownMNC) {
        this.lastKnownMNC = lastKnownMNC;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
