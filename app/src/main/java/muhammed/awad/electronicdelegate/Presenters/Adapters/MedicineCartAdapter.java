package muhammed.awad.electronicdelegate.Presenters.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import muhammed.awad.electronicdelegate.Models.MedicineCartModel;
import muhammed.awad.electronicdelegate.PharmacyApp.Fragments.PharmacyShoppingCartFragment;
import muhammed.awad.electronicdelegate.Presenters.Holders.MedicineCartHolder;
import muhammed.awad.electronicdelegate.R;

public class MedicineCartAdapter extends RecyclerView.Adapter<MedicineCartHolder> {

    private List<MedicineCartModel> medicine;
    private Context context;

    public MedicineCartAdapter(List<MedicineCartModel> medicine, Context context) {
        this.medicine = medicine;
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public MedicineCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine_cart, parent, false);
        return new MedicineCartHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MedicineCartHolder medicineCartHolder, final int i) {
        MedicineCartModel medicineCartModel = medicine.get(i);

        Picasso.get()
                .load(medicine.get(i).getOrder_image())
                .placeholder(R.drawable.addphoto)
                .error(R.drawable.addphoto)
                .into(medicineCartHolder.imageView);

        medicineCartHolder.medicineNameTextView.setText(medicineCartModel.getOrder_name());

        if (medicineCartModel.getPharmacy_name() != null) {
            medicineCartHolder.pharmacyTextView.setText(medicineCartModel.getPharmacy_name());
        } else if (medicineCartModel.getCompany_name() != null) {
            medicineCartHolder.pharmacyTextView.setText(medicineCartModel.getCompany_name());
        }
        medicineCartHolder.priceTextView.setText(medicineCartModel.getOrder_price());
        medicineCartHolder.quantityTextView.setText(medicineCartModel.getOrder_quantity());

        medicineCartHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medicine.remove(i);
                notifyItemRemoved(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicine.size();
    }
}
