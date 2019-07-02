package muhammed.awad.electronicdelegate.PharmacyApp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import muhammed.awad.electronicdelegate.Caching.CartMedicineManager;
import muhammed.awad.electronicdelegate.PharmacyApp.Fragments.PharmacyOrderStatusFragment;
import muhammed.awad.electronicdelegate.PharmacyApp.Fragments.PharmacyRequestsFragment;
import muhammed.awad.electronicdelegate.PharmacyApp.Fragments.PharmacyShoppingCartFragment;
import muhammed.awad.electronicdelegate.Models.CompanyModel;
import muhammed.awad.electronicdelegate.PharmacyApp.Fragments.AllPharmaceuticalsFragment;
import muhammed.awad.electronicdelegate.PharmacyApp.Fragments.CartFragment;
import muhammed.awad.electronicdelegate.PharmacyApp.Fragments.NewsFeedFragment;
import muhammed.awad.electronicdelegate.R;
import muhammed.awad.electronicdelegate.Register2Activity;

public class PharmacyMainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    LinearLayout hed;
    TextView company_title, signout;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        fragmentManager = getSupportFragmentManager();

        drawer();

        View view = navigationView.getHeaderView(0);

        company_title = view.findViewById(R.id.company_title_txt);
        signout = findViewById(R.id.sign_out_btn);
        view.setBackgroundColor(Color.parseColor("#33BC99"));


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : DELETE CACHE
                CartMedicineManager.getInstance().delete();

                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getApplicationContext(), Register2Activity.class);
                startActivity(intent);
            }
        });

        returndata();

        Fragment allPharmaceuticalsFragment = new AllPharmaceuticalsFragment();
        loadFragment(allPharmaceuticalsFragment);
        getSupportActionBar().setTitle("Pharmaceuticals");
    }

    public void returndata() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        final String userId = user.getUid();

        mDatabase.child("AllUsers").child("Pharmacies").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        CompanyModel companyModel = dataSnapshot.getValue(CompanyModel.class);

                        company_title.setText(companyModel.getTitle());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "can\'t fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void drawer() {
        mDrawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_pharmaceuticals:
                                Fragment allPharmaceuticalsFragment = new AllPharmaceuticalsFragment();
                                loadFragment(allPharmaceuticalsFragment);
                                getSupportActionBar().setTitle("Pharmaceuticals");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.nav_requests:
                                Fragment requestsFragment = new PharmacyRequestsFragment();
                                loadFragment(requestsFragment);
                                getSupportActionBar().setTitle("Requests");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.nav_cart:
                                Fragment cartFragment = new CartFragment();
                                loadFragment(cartFragment);
                                getSupportActionBar().setTitle("My Store");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.feed:
                                Fragment newsFeedFragment = new NewsFeedFragment();
                                loadFragment(newsFeedFragment);
                                getSupportActionBar().setTitle("News Feed");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.cart:
                                Fragment shoppingCart = new PharmacyShoppingCartFragment();
                                loadFragment(shoppingCart);
                                getSupportActionBar().setTitle("Cart");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.order:
                                Fragment orderStatus = new PharmacyOrderStatusFragment();
                                loadFragment(orderStatus);
                                getSupportActionBar().setTitle("Order Status");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                        }
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);

        getFragmentManager().popBackStack();
        // Commit the transaction
        fragmentTransaction.commit();
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
