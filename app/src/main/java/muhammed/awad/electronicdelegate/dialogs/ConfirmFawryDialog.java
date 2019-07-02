package muhammed.awad.electronicdelegate.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import muhammed.awad.electronicdelegate.R;

public class ConfirmFawryDialog {

    public static void showDialog(final Activity activity, String code, final DialogListener listener) {
        @SuppressLint("InflateParams")
        View v = activity.getLayoutInflater().inflate(R.layout.dialog_pay_fawry, null);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        mBuilder.setView(v);

        Button fawry = v.findViewById(R.id.pay);
        TextView codeTextView = v.findViewById(R.id.fawry_textView);
        codeTextView.setText("Code: " + code);

        final Dialog dialog = mBuilder.create();
        dialog.setCancelable(true);
        dialog.show();

        fawry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Successful Payment.", Toast.LENGTH_SHORT).show();
                listener.onFawry();
                dialog.dismiss();
            }
        });
    }

    public interface DialogListener {
        void onFawry();
    }
}
