package android.damir.stipancic.com.easypersonalfinancev2.income;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.damir.stipancic.com.easypersonalfinancev2.FinanceEntry;
import android.damir.stipancic.com.easypersonalfinancev2.FinanceRecyclerAdapter;
import android.damir.stipancic.com.easypersonalfinancev2.R;
import android.damir.stipancic.com.easypersonalfinancev2.UtilityClass;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class IncomeActivity extends AppCompatActivity {

    private RecyclerView mIncomeRecycler;
    private FinanceRecyclerAdapter mRvAdapter;
    private Realm mRealm;
    private ArrayList<FinanceEntry> mFinanceEntries = new ArrayList<>();
    private RealmChangeListener mRealmListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        UtilityClass.setupToolbar(this, R.id.income_toolbar);
        if(getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.income_title);

        setupRecycler();

        mRealm = Realm.getDefaultInstance();
        FinanceEntry testEntry = new FinanceEntry(UUID.randomUUID(), 1, "Electricity", new Date(), new Date(), 1000, R.drawable.energy128);
        mRealm.beginTransaction();
        mRealm.insert(testEntry);
        mRealm.commitTransaction();

        mFinanceEntries.addAll(mRealm.where(FinanceEntry.class).findAll());

        realmRefresh();

    }

    private void setupRecycler() {

        mIncomeRecycler = findViewById(R.id.rvIncome);
        mRvAdapter = new FinanceRecyclerAdapter(this, mFinanceEntries);
        mIncomeRecycler.setLayoutManager(new LinearLayoutManager(this));
        mIncomeRecycler.setAdapter(mRvAdapter);
    }

    private void realmRefresh(){

        mRealmListener = o -> {
            ArrayList<FinanceEntry> newEntries = new ArrayList<>(mRealm.where(FinanceEntry.class).findAll());
            mRvAdapter = new FinanceRecyclerAdapter(IncomeActivity.this, newEntries);
            mIncomeRecycler.setAdapter(mRvAdapter);

        };

        mRealm.addChangeListener(mRealmListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.beginTransaction();
        RealmResults<FinanceEntry> results = mRealm.where(FinanceEntry.class).findAll();
        results.deleteAllFromRealm();
        mRealm.deleteAll();
        mRealm.commitTransaction();
        mRealm.removeChangeListener(mRealmListener);
        mRealm.close();
    }
}