package muhammed.awad.electronicdelegate.Caching;

import java.util.ArrayList;
import java.util.List;

import muhammed.awad.electronicdelegate.Models.MedicineCartModel;

public class CartMedicineManager {

    private static CartMedicineManager self;
    private List<MedicineCartModel> medicineCartModels;

    public static CartMedicineManager getInstance() {
        if (self == null) {
            self = new CartMedicineManager();
        }
        return self;
    }

    private CartMedicineManager() {
        medicineCartModels = CachingManager.getInstance().getMedicineCartModels();
    }

    public List<MedicineCartModel> getMedicineCartModels() {
        if (medicineCartModels == null) return new ArrayList<>();
        return medicineCartModels;
    }

    public void saveMedicineCartModels(List<MedicineCartModel> medicineCartModels) {
        if (medicineCartModels == null)
            return;
        this.medicineCartModels = medicineCartModels;
        CachingManager.getInstance().saveMedicineCartModels(medicineCartModels);
    }

    public void addMedicineCartModels(MedicineCartModel medicineCartModel) {
        if (medicineCartModel == null)
            return;
        List<MedicineCartModel> medicineCartModels = getMedicineCartModels();
        medicineCartModels.add(medicineCartModel);
        this.medicineCartModels = medicineCartModels;
        CachingManager.getInstance().saveMedicineCartModels(medicineCartModels);
    }

    public void delete() {
        CachingManager.getInstance().deleteMedicineCartModels();
        medicineCartModels = null;
    }

    public boolean isMedicineCartModelsExist() {
        return medicineCartModels != null;
    }
}
