package muhammed.awad.electronicdelegate.PatientApp;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import muhammed.awad.electronicdelegate.PatientApp.Fragments.PatientCartFragment;
import muhammed.awad.electronicdelegate.PatientApp.Fragments.PatientOrderStatusFragment;
import muhammed.awad.electronicdelegate.PatientApp.Fragments.PharmaciesFragment;
import muhammed.awad.electronicdelegate.PharmacyApp.PharmacyMedActivity;
import muhammed.awad.electronicdelegate.R;
import muhammed.awad.electronicdelegate.Register2Activity;

public class PatientMainActivity extends AppCompatActivity {

    FragmentPagerAdapter fragmentPagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    ImageView more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        more = findViewById(R.id.mor);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(PatientMainActivity.this, more);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.mor_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.sign_out:

                                Signout();

                                return true;

                            default:
                                return true;
                        }
                    }
                });

                popup.show(); //showing popup menu
            }
        });

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[]
                    {
                            new PharmaciesFragment(),
                            // new DrugsFragment()
                            new PatientCartFragment(),
                            new PatientOrderStatusFragment()

                    };
            private final String[] mFragmentNames = new String[]
                    {
                            "PHARMACIES",
                            // "DRUGS",
                            "CART",
                            "ORDERS"
                    };

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void Signout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), Register2Activity.class);
        startActivity(intent);
    }

    private long exitTime = 0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finishAffinity();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        doExitApp();
    }
}
