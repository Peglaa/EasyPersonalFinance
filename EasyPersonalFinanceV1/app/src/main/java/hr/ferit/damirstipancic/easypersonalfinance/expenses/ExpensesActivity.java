package hr.ferit.damirstipancic.easypersonalfinance.expenses;

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

public class ExpensesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AddExpenseFragment.OnExpenseFragmentInteractionListener {

    private static final String[] months = {"All", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private TextView tvExpensesTotal;
    private Spinner monthExpensesSpinner;
    private SQLiteDatabase mDatabase;
    private ExpensesDBAdapter expensesDBAdapter;
    RecyclerView expenseRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        FinanceDBHelper dbHelper = new FinanceDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        tvExpensesTotal = findViewById(R.id.tvExpensesTotal);

        setupToolbar();
        setupExpenseRecycler();
        setupSpinner();
        setupAddExpenseButton();
    }


    private void setupToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Expenses");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = getSupportFragmentManager().getBackStackEntryCount();
                if (count == 0) {
                    ExpensesActivity.super.onBackPressed();
                    Intent intent = new Intent(ExpensesActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, 1);
                }
                else {
                    getSupportFragmentManager().popBackStack();
                    hideKeyboard(ExpensesActivity.this);
                }
            }
        });
    }

    private void setupExpenseRecycler(){
        expenseRecycler = findViewById(R.id.expensesRecycler);
        expenseRecycler.setLayoutManager(new LinearLayoutManager(this));
        expensesDBAdapter = new ExpensesDBAdapter(this, returnAllItems());
        expenseRecycler.setAdapter(expensesDBAdapter);
        expenseRecycler.addItemDecoration(new DividerItemDecoration(expenseRecycler.getContext(), DividerItemDecoration.VERTICAL));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(expenseRecycler);

    }

    private void setupSpinner() {

        monthExpensesSpinner = findViewById(R.id.sExpensesMonthSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthExpensesSpinner.setAdapter(spinnerAdapter);
        monthExpensesSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        checkSpinner(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void checkSpinner(int position) {
        switch (position) {
            case 0:
                expensesDBAdapter = new ExpensesDBAdapter(this, returnAllItems());
                expenseRecycler.setAdapter(expensesDBAdapter);
                break;
            case 1:
                expensesDBAdapter = new ExpensesDBAdapter(this, filterItems("January"));
                expenseRecycler.setAdapter(expensesDBAdapter);
                break;
            case 2:
                expensesDBAdapter = new ExpensesDBAdapter(this, filterItems("February"));
                expenseRecycler.setAdapter(expensesDBAdapter);
                break;
            case 3:
                expensesDBAdapter = new ExpensesDBAdapter(this, filterItems("March"));
                expenseRecycler.setAdapter(expensesDBAdapter);
                break;
            case 4:
                expensesDBAdapter = new ExpensesDBAdapter(this, filterItems("April"));
                expenseRecycler.setAdapter(expensesDBAdapter);
                break;
            case 5:
                expensesDBAdapter = new ExpensesDBAdapter(this, filterItems("May"));
                expenseRecycler.setAdapter(expensesDBAdapter);
                break;
            case 6:
                expensesDBAdapter = new ExpensesDBAdapter(this, filterItems("June"));
                expenseRecycler.setAdapter(expensesDBAdapter);
                break;
            case 7:
                expensesDBAdapter = new ExpensesDBAdapter(this, filterItems("July"));
                expenseRecycler.setAdapter(expensesDBAdapter);
                break;
            case 8:
                expensesDBAdapter = new ExpensesDBAdapter(this, filterItems("August"));
                expenseRecycler.setAdapter(expensesDBAdapter);
                break;
            case 9:
                expensesDBAdapter = new ExpensesDBAdapter(this, filterItems("September"));
                expenseRecycler.setAdapter(expensesDBAdapter);
                break;
            case 10:
                expensesDBAdapter = new ExpensesDBAdapter(this, filterItems("October"));
                expenseRecycler.setAdapter(expensesDBAdapter);
                break;
            case 11:
                expensesDBAdapter = new ExpensesDBAdapter(this, filterItems("November"));
                expenseRecycler.setAdapter(expensesDBAdapter);
                break;
            case 12:
                expensesDBAdapter = new ExpensesDBAdapter(this, filterItems("December"));
                expenseRecycler.setAdapter(expensesDBAdapter);
                break;
        }
        tvExpensesTotal.setText(String.valueOf(expensesDBAdapter.calculateTotal()));
    }


    public Cursor returnAllItems() {
        Cursor cursor;
        mDatabase.beginTransaction();
        try {
            cursor = mDatabase.query(
                    DataContract.ExpensesEntry.TABLE_NAME, null, null, null, null, null, null
            );
            mDatabase.setTransactionSuccessful();
        } finally{
            mDatabase.endTransaction();
        }

        return cursor;
    }

    private void removeItem(long id) {
        mDatabase.beginTransaction();
        try {
            mDatabase.delete(DataContract.ExpensesEntry.TABLE_NAME, DataContract.ExpensesEntry._ID + "=" + id, null);
            checkSpinner(monthExpensesSpinner.getSelectedItemPosition());
            tvExpensesTotal.setText(String.valueOf(expensesDBAdapter.calculateTotal()));
            mDatabase.setTransactionSuccessful();
        } finally{
            mDatabase.endTransaction();
        }
    }

    public Cursor filterItems(String month){
        String sQuery;
        mDatabase.beginTransaction();
        try {
            sQuery = "SELECT * FROM " + DataContract.ExpensesEntry.TABLE_NAME + " WHERE expenseMonth=?";
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }

        return mDatabase.rawQuery(sQuery, new String[] {month});
    }

    private void setupAddExpenseButton() {
        FloatingActionButton addExpenseButton = findViewById(R.id.addExpenseBtn);
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddExpenseFragment();
            }
        });
    }

    private void openAddExpenseFragment() {
        AddExpenseFragment addExpenseFragment = AddExpenseFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container_expenses, addExpenseFragment, "ADD_EXPENSE_FRAGMENT").commit();
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if(count == 0) {

            super.onBackPressed();
            Intent intent = new Intent(ExpensesActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, 1);
        }
        else
            getSupportFragmentManager().popBackStack();

    }

    @Override
    public void onExpenseFragmentInteraction(String spinnerSelection, byte[] spinnerImage, double amount, String mMonth, String date) {

        ContentValues cv = new ContentValues();
        mDatabase.beginTransaction();
        try {
            cv.put(DataContract.ExpensesEntry.COLUMN_TYPE, spinnerSelection);
            cv.put(DataContract.ExpensesEntry.COLUMN_AMOUNT, amount);
            cv.put(DataContract.ExpensesEntry.COLUMN_MONTH, mMonth);
            cv.put(DataContract.ExpensesEntry.COLUMN_DATE, date);
            cv.put(DataContract.ExpensesEntry.COLUMN_IMAGE, spinnerImage);

            mDatabase.insert(DataContract.ExpensesEntry.TABLE_NAME, null, cv);
            expensesDBAdapter.swapCursor(returnAllItems());
            checkSpinner(0);
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
        getSupportFragmentManager().popBackStack();
    }
}