package muhammed.awad.electronicdelegate.Presenters.Holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import muhammed.awad.electronicdelegate.R;

public class MedicineCartHolder extends RecyclerView.ViewHolder{

    public ImageView imageView ,del;
    public TextView medicineNameTextView;
    public TextView priceTextView;
    public TextView pharmacyTextView;
    public TextView quantityTextView;

    public MedicineCartHolder(@NonNull View view) {
        super(view);

        imageView = view.findViewById(R.id.medicine_image);
        medicineNameTextView = view.findViewById(R.id.medicine_name);
        priceTextView = view.findViewById(R.id.medicine_price);
        pharmacyTextView = view.findViewById(R.id.pharm_name);
        quantityTextView = view.findViewById(R.id.quantity_txt);
        del = view.findViewById(R.id.delete_item);

    }
}
