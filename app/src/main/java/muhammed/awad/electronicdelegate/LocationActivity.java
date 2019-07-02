package muhammed.awad.electronicdelegate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import muhammed.awad.electronicdelegate.PatientApp.PatientMainActivity;

public class LocationActivity extends AppCompatActivity {
    Spinner g, d;
    Button done_btn;

    String selected_governorate, selected_district;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        g = findViewById(R.id.governorate_spinner);
        d = findViewById(R.id.district_spinner);
        done_btn = findViewById(R.id.done_btn);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.governorate, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        g.setAdapter(adapter1);

        g.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_governorate = String.valueOf(parent.getItemAtPosition(position));

                if (selected_governorate.equals("Cairo")) {
                    ArrayAdapter<CharSequence> cairo_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.cairodistrict, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    cairo_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    d.setAdapter(cairo_adapter);
                } else if (selected_governorate.equals("Giza")) {
                    ArrayAdapter<CharSequence> giza_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.gizadistrict, android.R.layout.simple_spinner_item);
                    // Specify the layout to use when the list of choices appears
                    giza_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                    d.setAdapter(giza_adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        d.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_district = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_governorate.equals("Select governorate")) {
                    Toast.makeText(getApplicationContext(), "please enter governorate name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selected_district.equals("Select district") || selected_district.length() == 0) {
                    Toast.makeText(getApplicationContext(), "please enter district name", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), PatientMainActivity.class);
                intent.putExtra("g", selected_governorate);
                intent.putExtra("d", selected_district);
                startActivity(intent);
            }
        });
    }
}
