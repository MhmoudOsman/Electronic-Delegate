package muhammed.awad.electronicdelegate.Presenters.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import muhammed.awad.electronicdelegate.CompanyApp.CompanyRequestsActivity;
import muhammed.awad.electronicdelegate.Models.PharmacyModel;
import muhammed.awad.electronicdelegate.Presenters.Holders.PharmacyViewHolder;
import muhammed.awad.electronicdelegate.R;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyViewHolder> {

    private List<PharmacyModel> pharmacyModels;
    private Context context;

    public PharmacyAdapter(List<PharmacyModel> pharmacyModels, Context context) {
        this.pharmacyModels = pharmacyModels;
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public PharmacyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pharmacy_request, parent, false);
        return new PharmacyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacyViewHolder pharmacyViewHolder, int i) {
        final PharmacyModel pharmacyModel = pharmacyModels.get(i);
        pharmacyViewHolder.name.setText(pharmacyModel.getTitle());

        pharmacyViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CompanyRequestsActivity.class);
                i.putExtra(CompanyRequestsActivity.ID, pharmacyModel.getId());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pharmacyModels.size();
    }
}