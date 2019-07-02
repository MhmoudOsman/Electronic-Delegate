package muhammed.awad.electronicdelegate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FawryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay_fawry);

        Button transferred = findViewById(R.id.pay);
        transferred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FawryActivity.this, "Successful Payment.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
