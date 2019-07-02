package muhammed.awad.electronicdelegate.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import muhammed.awad.electronicdelegate.R;

public class CheckOutDialog {

    public static void showDialog(final Activity activity, final DialogListener listener) {
        @SuppressLint("InflateParams")
        View v = activity.getLayoutInflater().inflate(R.layout.activity_check, null);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        mBuilder.setView(v);

        TextView checkOut = v.findViewById(R.id.checkOut);
        ImageView fawry = v.findViewById(R.id.fawry);

        final Dialog dialog = mBuilder.create();
        dialog.setCancelable(true);
        dialog.show();

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "You will pay when receive the order.", Toast.LENGTH_SHORT).show();
                listener.onOkCheckOut();
                dialog.dismiss();
            }
        });

        fawry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFawry();
                dialog.dismiss();
            }
        });
    }


    public interface DialogListener {
        void onOkCheckOut();

        void onFawry();
    }
}
