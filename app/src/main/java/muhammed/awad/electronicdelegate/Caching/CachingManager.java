package muhammed.awad.electronicdelegate.Caching;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import muhammed.awad.electronicdelegate.App.App;
import muhammed.awad.electronicdelegate.Models.MedicineCartModel;

public class CachingManager {

    private Context context;
    protected static CachingManager self;

    public static CachingManager getInstance() {
        if (self == null) {
            self = new CachingManager();
        }
        return self;
    }

    private CachingManager() {
        context = App.getContext();
    }

    protected boolean isObjectCachedAndNotExpired(long expireInHours, File objectFile) {
        boolean exist, expired = false;
        exist = objectFile.exists();

        if (exist) {
            Date now = new Date();
            Date expireDate = new Date(objectFile.lastModified() + expireInHours * 60 * 60 * 1000);

            if (now.after(expireDate)) {
                expired = true;
            }
        }
        return exist && !expired;
    }

    public static void saveObject(Serializable object, File objectFile) throws IOException {
        if (!objectFile.exists()) {
            objectFile.createNewFile();
        }
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(objectFile));
        outputStream.writeObject(object);
        objectFile.setLastModified(new Date().getTime());
        outputStream.close();

        objectFile.setLastModified(new Date().getTime());
    }

    public static Serializable loadObject(File objectFile) throws Exception {
        Object cachedObject = null;
        if (objectFile.exists()) {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(objectFile));
            cachedObject = inputStream.readObject();
            inputStream.close();
            objectFile.setLastModified(new Date().getTime());
        }

        return (Serializable) cachedObject;
    }

    public void saveAppConfiguration(AppConfiguration appConfig) {
        File saveToFile = new File(Engine.DataFolder.APP_DATA, Engine.FileName.APP_CONFIGURATION);
        try {
            saveObject(appConfig, saveToFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public AppConfiguration loadAppConfiguration() {
        AppConfiguration appConfig = null;
        try {
            appConfig = (AppConfiguration) loadObject(new File(Engine.DataFolder.APP_DATA, Engine.FileName.APP_CONFIGURATION));
        } catch (Throwable t) {

        }
        return appConfig;
    }

    public void saveMedicineCartModels(List<MedicineCartModel> medicineCartModels) {
        if (medicineCartModels != null) {
            String itemFileName = Engine.FileName.APP_CART_MEDICINE;
            File containerFolder = Engine.DataFolder.CART_MEDICINE_DATA;
            Serializable serializableRoutes = (Serializable) medicineCartModels;
            saveObject(itemFileName, containerFolder, serializableRoutes, context);
        }
    }

    @SuppressWarnings("unchecked")
    public List<MedicineCartModel> getMedicineCartModels() {
        String itemFileName = Engine.FileName.APP_CART_MEDICINE;
        File containerFolder = Engine.DataFolder.CART_MEDICINE_DATA;
        Serializable object = loadObject(itemFileName, containerFolder, context, Engine.ExpiryInfo.EXPIRING_LOCATION_DIRECTIONS);
        return (List<MedicineCartModel>) object;
    }

    public void deleteMedicineCartModels() {
        Engine.deleteFileRecursive(Engine.DataFolder.CART_MEDICINE_DATA);
    }

    private void saveObject(String listFileName, File containerFolder, Serializable object, Context context) {
        String language = "en";
        listFileName = listFileName + "_" + language.toUpperCase() + Engine.FileName.APP_FILES_EXT;

        File folder = Engine.getCacheFile(containerFolder, listFileName, context);

        try {
            saveObject(object, folder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Serializable loadObject(String listFileName, File containerFolder, Context context, int expireHours) {
        String language = "en";

        listFileName = listFileName + "_" + language.toUpperCase() + Engine.FileName.APP_FILES_EXT;

        if (!Engine.isExistingFile(containerFolder, listFileName)) {
            return null;
        }
        File folder = Engine.getCacheFile(containerFolder, listFileName, context);
        Serializable object = null;
        try {
            boolean isNotExpired;
            if (expireHours == Engine.ExpiryInfo.NO_EXPIRY) {
                isNotExpired = true;
            } else {
                isNotExpired = isObjectCachedAndNotExpired(expireHours, folder);
            }

            if (isNotExpired) {
                object = loadObject(folder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
