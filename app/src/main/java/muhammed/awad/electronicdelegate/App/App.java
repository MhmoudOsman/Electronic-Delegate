package muhammed.awad.electronicdelegate.App;

import com.google.firebase.database.FirebaseDatabase;

import muhammed.awad.electronicdelegate.Caching.Engine;

public class App extends EDApp {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Engine.initialize(this);
        Engine.validateCachedData(this);
        Engine.setApplicationLanguage(this, Engine.getAppConfiguration().getLanguage());
    }
}
