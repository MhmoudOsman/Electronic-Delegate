package muhammed.awad.electronicdelegate.Presenters.Holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import muhammed.awad.electronicdelegate.R;

public class PharmacyViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout layout;
    public TextView name;

    public PharmacyViewHolder(@NonNull View view) {
        super(view);

        layout = view.findViewById(R.id.layout);
        name = view.findViewById(R.id.item_pharmacy_request_textView);
    }
}
