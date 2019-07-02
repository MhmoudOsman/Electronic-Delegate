package muhammed.awad.electronicdelegate.Presenters.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import muhammed.awad.electronicdelegate.CompanyApp.CompanyRequestsActivity;
import muhammed.awad.electronicdelegate.Models.PatientModel;
import muhammed.awad.electronicdelegate.PharmacyApp.PharmacyRequestsActivity;
import muhammed.awad.electronicdelegate.Presenters.Holders.PharmacyViewHolder;
import muhammed.awad.electronicdelegate.R;

public class PatientAdapter extends RecyclerView.Adapter<PharmacyViewHolder> {

    private List<PatientModel> patientModels;
    private Context context;

    public PatientAdapter(List<PatientModel> patientModels, Context context) {
        this.patientModels = patientModels;
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
        final PatientModel patientModel = patientModels.get(i);
        pharmacyViewHolder.name.setText(patientModel.getFullname());

        pharmacyViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PharmacyRequestsActivity.class);
                i.putExtra(PharmacyRequestsActivity.ID, patientModel.getId());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return patientModels.size();
    }
}