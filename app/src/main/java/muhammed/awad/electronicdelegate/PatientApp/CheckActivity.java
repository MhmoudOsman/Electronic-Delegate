package muhammed.awad.electronicdelegate.PatientApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import muhammed.awad.electronicdelegate.FawryActivity;
import muhammed.awad.electronicdelegate.R;

public class CheckActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView checkOut;
    private ImageView fawry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        checkOut = findViewById(R.id.checkOut);
        fawry = findViewById(R.id.fawry);

        checkOut.setOnClickListener(this);
        fawry.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.checkOut:
                Toast.makeText(this, "You will pay when receive the order.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.fawry:
                Intent intent = new Intent(this, FawryActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }
}
