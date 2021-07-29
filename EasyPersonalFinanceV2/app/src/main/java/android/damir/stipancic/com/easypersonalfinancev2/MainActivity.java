package android.damir.stipancic.com.easypersonalfinancev2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.damir.stipancic.com.easypersonalfinancev2.bills.BillsActivity;
import android.damir.stipancic.com.easypersonalfinancev2.expenses.ExpensesActivity;
import android.damir.stipancic.com.easypersonalfinancev2.income.IncomeActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mNavDrawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //colorPrimaryDark not working to set status bar color so I have to do this??!?!?
        UtilityClass.setStatusBarColor(this);
        setupToolbar();
        setupNavDrawer();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupNavDrawer() {
        mNavDrawer = findViewById(R.id.mainDrawerLayout);
        NavigationView navigationView = findViewById(R.id.mainNavView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mNavDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mNavDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent newActivityIntent;

        if(item.getItemId() == R.id.miIncome) {
            newActivityIntent = new Intent(this, IncomeActivity.class);
            startActivity(newActivityIntent);
            mNavDrawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else if(item.getItemId() == R.id.miBills) {
            newActivityIntent = new Intent(this, BillsActivity.class);
            startActivity(newActivityIntent);
            mNavDrawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else{
            newActivityIntent = new Intent(this, ExpensesActivity.class);
            startActivity(newActivityIntent);
            mNavDrawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if(mNavDrawer.isDrawerOpen(GravityCompat.START)){
            mNavDrawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
}