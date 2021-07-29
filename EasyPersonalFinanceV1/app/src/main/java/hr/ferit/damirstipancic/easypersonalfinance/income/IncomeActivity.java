package hr.ferit.damirstipancic.easypersonalfinance.income;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import hr.ferit.damirstipancic.easypersonalfinance.DataContract;
import hr.ferit.damirstipancic.easypersonalfinance.FinanceDBHelper;
import hr.ferit.damirstipancic.easypersonalfinance.MainActivity;
import hr.ferit.damirstipancic.easypersonalfinance.R;

import static hr.ferit.damirstipancic.easypersonalfinance.income.AddIncomeFragment.hideKeyboard;

public class IncomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AddIncomeFragment.OnIncomeFragmentInteractionListener {

    private static final String[] months = {"All", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private Spinner monthSpinner;
    private TextView tvIncomeTotal;
    private SQLiteDatabase mDatabase;
    private IncomeDBAdapter incomeDBAdapter;
    RecyclerView incomeRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        FinanceDBHelper dbHelper = new FinanceDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        tvIncomeTotal = findViewById(R.id.tvIncomeTotal);

        setupToolbar();
        setupIncomeRecycler();
        setupSpinner();
        setupAddIncomeButton();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = getSupportFragmentManager().getBackStackEntryCount();
                if (count == 0) {
                    IncomeActivity.super.onBackPressed();
                    Intent intent = new Intent(IncomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, 1);
                }
                else {
                    getSupportFragmentManager().popBackStack();
                    hideKeyboard(IncomeActivity.this);
                }
            }


        });
    }


    private void setupIncomeRecycler() {
        incomeRecycler = findViewById(R.id.incomeRecycler);
        incomeRecycler.setLayoutManager(new LinearLayoutManager(this));
        incomeDBAdapter = new IncomeDBAdapter(this, returnAllItems());
        incomeRecycler.setAdapter(incomeDBAdapter);
        incomeRecycler.addItemDecoration(new DividerItemDecoration(incomeRecycler.getContext(), DividerItemDecoration.VERTICAL));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());

            }
        }).attachToRecyclerView(incomeRecycler);

    }


    private void setupSpinner() {
        monthSpinner = findViewById(R.id.sMonthSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(spinnerAdapter);
        monthSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        checkSpinner(position);
    }

    public void checkSpinner(int position){

        switch (position) {
            case 0:
                incomeDBAdapter = new IncomeDBAdapter(this, returnAllItems());
                incomeRecycler.setAdapter(incomeDBAdapter);
                break;
            case 1:
                incomeDBAdapter = new IncomeDBAdapter(this, filterItems("January"));
                incomeRecycler.setAdapter(incomeDBAdapter);
                break;
            case 2:
                incomeDBAdapter = new IncomeDBAdapter(this, filterItems("February"));
                incomeRecycler.setAdapter(incomeDBAdapter);
                break;
            case 3:
                incomeDBAdapter = new IncomeDBAdapter(this, filterItems("March"));
                incomeRecycler.setAdapter(incomeDBAdapter);
                break;
            case 4:
                incomeDBAdapter = new IncomeDBAdapter(this, filterItems("April"));
                incomeRecycler.setAdapter(incomeDBAdapter);
                break;
            case 5:
                incomeDBAdapter = new IncomeDBAdapter(this, filterItems("May"));
                incomeRecycler.setAdapter(incomeDBAdapter);
                break;
            case 6:
                incomeDBAdapter = new IncomeDBAdapter(this, filterItems("June"));
                incomeRecycler.setAdapter(incomeDBAdapter);
                break;
            case 7:
                incomeDBAdapter = new IncomeDBAdapter(this, filterItems("July"));
                incomeRecycler.setAdapter(incomeDBAdapter);
                break;
            case 8:
                incomeDBAdapter = new IncomeDBAdapter(this, filterItems("August"));
                incomeRecycler.setAdapter(incomeDBAdapter);
                break;
            case 9:
                incomeDBAdapter = new IncomeDBAdapter(this, filterItems("September"));
                incomeRecycler.setAdapter(incomeDBAdapter);
                break;
            case 10:
                incomeDBAdapter = new IncomeDBAdapter(this, filterItems("October"));
                incomeRecycler.setAdapter(incomeDBAdapter);
                break;
            case 11:
                incomeDBAdapter = new IncomeDBAdapter(this, filterItems("November"));
                incomeRecycler.setAdapter(incomeDBAdapter);
                break;
            case 12:
                incomeDBAdapter = new IncomeDBAdapter(this, filterItems("December"));
                incomeRecycler.setAdapter(incomeDBAdapter);
                break;
        }
        tvIncomeTotal.setText(String.valueOf(incomeDBAdapter.calculateTotal()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setupAddIncomeButton() {
        FloatingActionButton addIncomeButton = findViewById(R.id.addIncomeBtn);
        addIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddIncomeFragment();
            }
        });
    }

    private void openAddIncomeFragment() {
        AddIncomeFragment addIncomeFragment = AddIncomeFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container, addIncomeFragment, "ADD_INCOME_FRAGMENT").commit();
    }

    @Override
    public void onIncomeFragmentInteraction(String spinnerSelection, byte[] spinnerImage, double amount, String mMonth, String date) {
        ContentValues cv = new ContentValues();
        mDatabase.beginTransaction();
        try {
            cv.put(DataContract.IncomeEntry.COLUMN_TYPE, spinnerSelection);
            cv.put(DataContract.IncomeEntry.COLUMN_AMOUNT, amount);
            cv.put(DataContract.IncomeEntry.COLUMN_MONTH, mMonth);
            cv.put(DataContract.IncomeEntry.COLUMN_DATE, date);
            cv.put(DataContract.IncomeEntry.COLUMN_IMAGE, spinnerImage);
            Log.d("TAG", "onIncomeFragmentInteraction: " + date);
            String temp = date.substring(date.length()-4);
            Log.d("TAG", "YEARTEST: " + temp);

            mDatabase.insert(DataContract.IncomeEntry.TABLE_NAME, null, cv);
            incomeDBAdapter.swapCursor(returnAllItems());
            checkSpinner(0);
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if(count == 0) {

            super.onBackPressed();
            Intent intent = new Intent(IncomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, 1);
        }
        else
            getSupportFragmentManager().popBackStack();

    }

    public Cursor returnAllItems(){
        Cursor cursor;
        mDatabase.beginTransaction();
        try {
            cursor = mDatabase.query(
                    DataContract.IncomeEntry.TABLE_NAME, null, null, null, null, null, null
            );
            mDatabase.setTransactionSuccessful();
        } finally{
            mDatabase.endTransaction();
        }
        return cursor;

    }

    public Cursor filterItems(String month){
        String query;
        mDatabase.beginTransaction();
        try {
            query = "SELECT * FROM " + DataContract.IncomeEntry.TABLE_NAME + " WHERE incomeMonth=?";
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }

        return mDatabase.rawQuery(query, new String[] {month});
    }

    private void removeItem(long id){
        mDatabase.beginTransaction();
        try {
            mDatabase.delete(DataContract.IncomeEntry.TABLE_NAME, DataContract.IncomeEntry._ID + "=" + id, null);
            checkSpinner(monthSpinner.getSelectedItemPosition());
            tvIncomeTotal.setText(String.valueOf(incomeDBAdapter.calculateTotal()));
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
    }
}
