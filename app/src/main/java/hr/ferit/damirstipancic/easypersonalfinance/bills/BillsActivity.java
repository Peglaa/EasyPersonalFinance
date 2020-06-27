package hr.ferit.damirstipancic.easypersonalfinance.bills;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import hr.ferit.damirstipancic.easypersonalfinance.DataContract;
import hr.ferit.damirstipancic.easypersonalfinance.FinanceDBHelper;
import hr.ferit.damirstipancic.easypersonalfinance.MainActivity;
import hr.ferit.damirstipancic.easypersonalfinance.R;

import static hr.ferit.damirstipancic.easypersonalfinance.income.AddIncomeFragment.hideKeyboard;

public class BillsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AddBillFragment.OnBillFragmentInteractionListener {

    private static final String[] months = {"All", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private Spinner monthBillsSpinner;
    private TextView tvBillsTotal;
    private SQLiteDatabase mDatabase;
    private BillsDBAdapter billsDBAdapter;
    RecyclerView billsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);

        FinanceDBHelper dbHelper = new FinanceDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        tvBillsTotal = findViewById(R.id.tvBillsTotal);

        setupToolbar();
        setupBillsRecycler();
        setupSpinner();
        setupAddBillButton();
        try {
            checkDates(returnAllItems());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Bills");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = getSupportFragmentManager().getBackStackEntryCount();
                if (count == 0) {
                    BillsActivity.super.onBackPressed();
                    Intent intent = new Intent(BillsActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, 1);
                }
                else {
                    getSupportFragmentManager().popBackStack();
                    hideKeyboard(BillsActivity.this);
                }
            }
        });
    }

    private void setupBillsRecycler() {
        billsRecycler = findViewById(R.id.billsRecycler);
        billsRecycler.setLayoutManager(new LinearLayoutManager(this));
        billsDBAdapter = new BillsDBAdapter(this, returnAllItems());
        billsRecycler.setAdapter(billsDBAdapter);
        billsRecycler.addItemDecoration(new DividerItemDecoration(billsRecycler.getContext(), DividerItemDecoration.VERTICAL));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(billsRecycler);

    }

    private void setupSpinner() {
        monthBillsSpinner = findViewById(R.id.sBillsMonthSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthBillsSpinner.setAdapter(spinnerAdapter);
        monthBillsSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        checkSpinner(position);
    }

    public void checkSpinner(int position){

        switch (position) {
            case 0:
                billsDBAdapter = new BillsDBAdapter(this, returnAllItems());
                billsRecycler.setAdapter(billsDBAdapter);
                break;
            case 1:
                billsDBAdapter = new BillsDBAdapter(this, filterItems("January"));
                billsRecycler.setAdapter(billsDBAdapter);
                break;
            case 2:
                billsDBAdapter = new BillsDBAdapter(this, filterItems("February"));
                billsRecycler.setAdapter(billsDBAdapter);
                break;
            case 3:
                billsDBAdapter = new BillsDBAdapter(this, filterItems("March"));
                billsRecycler.setAdapter(billsDBAdapter);
                break;
            case 4:
                billsDBAdapter = new BillsDBAdapter(this, filterItems("April"));
                billsRecycler.setAdapter(billsDBAdapter);
                break;
            case 5:
                billsDBAdapter = new BillsDBAdapter(this, filterItems("May"));
                billsRecycler.setAdapter(billsDBAdapter);
                break;
            case 6:
                billsDBAdapter = new BillsDBAdapter(this, filterItems("June"));
                billsRecycler.setAdapter(billsDBAdapter);
                break;
            case 7:
                billsDBAdapter = new BillsDBAdapter(this, filterItems("July"));
                billsRecycler.setAdapter(billsDBAdapter);
                break;
            case 8:
                billsDBAdapter = new BillsDBAdapter(this, filterItems("August"));
                billsRecycler.setAdapter(billsDBAdapter);
                break;
            case 9:
                billsDBAdapter = new BillsDBAdapter(this, filterItems("September"));
                billsRecycler.setAdapter(billsDBAdapter);
                break;
            case 10:
                billsDBAdapter = new BillsDBAdapter(this, filterItems("October"));
                billsRecycler.setAdapter(billsDBAdapter);
                break;
            case 11:
                billsDBAdapter = new BillsDBAdapter(this, filterItems("November"));
                billsRecycler.setAdapter(billsDBAdapter);
                break;
            case 12:
                billsDBAdapter = new BillsDBAdapter(this, filterItems("December"));
                billsRecycler.setAdapter(billsDBAdapter);
                break;
        }
        tvBillsTotal.setText(String.valueOf(billsDBAdapter.calculateTotal()));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public Cursor returnAllItems() {

            return mDatabase.query(
                    DataContract.BillsEntry.TABLE_NAME, null, null, null, null, null, null
            );

    }

    private void removeItem(long id) {
        mDatabase.beginTransaction();
        try {
            mDatabase.delete(DataContract.BillsEntry.TABLE_NAME, DataContract.BillsEntry._ID + "=" + id, null);
            checkSpinner(monthBillsSpinner.getSelectedItemPosition());
            tvBillsTotal.setText(String.valueOf(billsDBAdapter.calculateTotal()));
            mDatabase.setTransactionSuccessful();
        } finally{
            mDatabase.endTransaction();
        }
    }

    public Cursor filterItems(String month){
        String query;
        mDatabase.beginTransaction();
        try {
            query = "SELECT * FROM " + DataContract.BillsEntry.TABLE_NAME + " WHERE billMonth=?";
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
        return mDatabase.rawQuery(query, new String[] {month});
    }

    private void setupAddBillButton() {
        FloatingActionButton addBillButton = findViewById(R.id.addBillsBtn);
        addBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddBillFragment();
            }
        });
    }

    private void openAddBillFragment() {
        AddBillFragment addBillFragment = AddBillFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container_bills, addBillFragment, "ADD_BILL_FRAGMENT").commit();
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if(count == 0) {

            super.onBackPressed();
            Intent intent = new Intent(BillsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, 1);
        }
        else
            getSupportFragmentManager().popBackStack();

    }

    @Override
    public void onBillFragmentInteraction(String spinnerSelection, byte[] spinnerImage, double amount, String mMonth, String date) {
        ContentValues cv = new ContentValues();
        cv.put(DataContract.BillsEntry.COLUMN_TYPE, spinnerSelection);
        cv.put(DataContract.BillsEntry.COLUMN_AMOUNT, amount);
        cv.put(DataContract.BillsEntry.COLUMN_MONTH, mMonth);
        cv.put(DataContract.BillsEntry.COLUMN_DATE, date);
        cv.put(DataContract.BillsEntry.COLUMN_IMAGE, spinnerImage);
        cv.put(DataContract.BillsEntry.COLUMN_OVERDUE, 0);

        mDatabase.insert(DataContract.BillsEntry.TABLE_NAME, null, cv);

        try {
            checkDates(returnAllItems());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        checkSpinner(0);

        getSupportFragmentManager().popBackStack();
    }

    public void checkDates(Cursor mCursor) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        Date currentDate = new Date();
        Log.d("TAG", "currentDate: " + currentDate);

        if (mCursor.moveToPosition(0)) {
            Log.d("TAG", "DBDate: " + dateFormat.parse(mCursor.getString(mCursor.getColumnIndex(DataContract.BillsEntry.COLUMN_DATE))));
            if ((Objects.requireNonNull(dateFormat.parse(mCursor.getString(mCursor.getColumnIndex(DataContract.BillsEntry.COLUMN_DATE))))).before(currentDate)) {
                updateDateText(mCursor);
            }

            while (mCursor.move(1)) {
                Log.d("TAG", "offsetDate: " + dateFormat.parse(mCursor.getString(mCursor.getColumnIndex(DataContract.BillsEntry.COLUMN_DATE))));
                if (Objects.requireNonNull(dateFormat.parse(mCursor.getString(mCursor.getColumnIndex(DataContract.BillsEntry.COLUMN_DATE)))).before(currentDate)) {
                    updateDateText(mCursor);
                }
            }
        }

        billsDBAdapter.notifyDataSetChanged();

    }

    private void updateDateText(Cursor mCursor) {
        ContentValues cv = new ContentValues();
        cv.put(DataContract.BillsEntry.COLUMN_OVERDUE, 1);
        mDatabase.update(DataContract.BillsEntry.TABLE_NAME, cv, "_ID=" + mCursor.getLong(mCursor.getColumnIndex(DataContract.BillsEntry._ID)), null);
        Log.d("TAG", "updateDateText: " + mCursor.getInt(mCursor.getColumnIndex(DataContract.BillsEntry.COLUMN_OVERDUE)));

    }

}
