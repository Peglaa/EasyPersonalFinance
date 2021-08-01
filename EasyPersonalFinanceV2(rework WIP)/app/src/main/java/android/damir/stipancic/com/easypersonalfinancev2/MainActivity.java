package android.damir.stipancic.com.easypersonalfinancev2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.damir.stipancic.com.easypersonalfinancev2.bills.BillsActivity;
import android.damir.stipancic.com.easypersonalfinancev2.expenses.ExpensesActivity;
import android.damir.stipancic.com.easypersonalfinancev2.income.IncomeActivity;
import android.damir.stipancic.com.easypersonalfinancev2.tabs.TabsAdapter;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mNavDrawer;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager2;
    private TabsAdapter mTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);

        setupToolbar();
        setupNavDrawer();
        setupTabs();
    }

    private void setupToolbar() {
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
    }

    private void setupNavDrawer() {
        mNavDrawer = findViewById(R.id.mainDrawerLayout);
        NavigationView navigationView = findViewById(R.id.mainNavView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mNavDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

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

    private void setupTabs() {

        mTabLayout = findViewById(R.id.mainTabLayout);
        mViewPager2 = findViewById(R.id.mainViewPager);

        FragmentManager fm = getSupportFragmentManager();
        mTabsAdapter = new TabsAdapter(fm, getLifecycle());
        mViewPager2.setAdapter(mTabsAdapter);

        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.home_title));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.income_title));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.bills_title));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.expenses_title));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mTabLayout.selectTab(mTabLayout.getTabAt(position));
            }
        });
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