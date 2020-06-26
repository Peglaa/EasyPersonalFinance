package hr.ferit.damirstipancic.easypersonalfinance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import hr.ferit.damirstipancic.easypersonalfinance.bills.BillsActivity;
import hr.ferit.damirstipancic.easypersonalfinance.expenses.ExpensesActivity;
import hr.ferit.damirstipancic.easypersonalfinance.income.IncomeActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private ViewPager viewPager;
    public PageAdapter pageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        setupTabs();

    }

    private void setupTabs() {

        TabLayout mainTabLayout = findViewById(R.id.mainTabLayout);
        mainTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mainTabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager = findViewById(R.id.mainViewPager);

        pageAdapter = new PageAdapter(getSupportFragmentManager(), mainTabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.setOffscreenPageLimit(2);

        mainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition() == 0){
                    pageAdapter.notifyDataSetChanged();

                } else if (tab.getPosition() == 1){
                    pageAdapter.notifyDataSetChanged();

                } else if (tab.getPosition() == 2){
                    pageAdapter.notifyDataSetChanged();

                } else if (tab.getPosition() == 3){
                    pageAdapter.notifyDataSetChanged();

                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mainTabLayout));

    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch(item.getItemId()){
            case R.id.nav_income:
                intent = new Intent(this, IncomeActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_bills:
                intent = new Intent(this, BillsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_expenses:
                intent = new Intent(this, ExpensesActivity.class);
                startActivity(intent);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
