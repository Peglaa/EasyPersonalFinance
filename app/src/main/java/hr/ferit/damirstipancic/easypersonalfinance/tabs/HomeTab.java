package hr.ferit.damirstipancic.easypersonalfinance.tabs;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.ferit.damirstipancic.easypersonalfinance.DataContract;
import hr.ferit.damirstipancic.easypersonalfinance.FinanceDBHelper;
import hr.ferit.damirstipancic.easypersonalfinance.R;
import hr.ferit.damirstipancic.easypersonalfinance.bills.BillsDBAdapter;
import hr.ferit.damirstipancic.easypersonalfinance.expenses.ExpensesDBAdapter;
import hr.ferit.damirstipancic.easypersonalfinance.income.IncomeDBAdapter;
import hr.ferit.damirstipancic.easypersonalfinance.spinner.FinanceTypeAdapter;
import hr.ferit.damirstipancic.easypersonalfinance.spinner.FinanceTypeItem;

public class HomeTab extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String[] months = {"All", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private final List<String> desc = new ArrayList<>(Arrays.asList("Income","Income", "Bills", "Expenses"));
    private IncomeDBAdapter dbIncomeAdapter;
    private BillsDBAdapter dbBillsAdapter;
    private ExpensesDBAdapter dbExpensesAdapter;
    private double[] financeAmounts;
    private SQLiteDatabase mDatabase;
    private TextView tvIncomeTally;

    public HomeTab() {
        // Required empty public constructor
    }

    public static HomeTab newInstance() {

        return new HomeTab();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvTally = requireActivity().findViewById(R.id.tvTally);
        tvIncomeTally = requireActivity().findViewById(R.id.tvIncomeTally);
        tvTally.setText(R.string.tvtally);
        tvTally.setTextColor(Color.BLACK);

        setupDBAdapters();
        setupSpinner();

    }

    private void setupSpinner() {

        Spinner mainMonthSpinner = requireActivity().findViewById(R.id.sMainMonthSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, months);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainMonthSpinner.setAdapter(spinnerAdapter);
        mainMonthSpinner.setOnItemSelectedListener(this);
    }

    private void setupDBAdapters() {

        FinanceDBHelper dbHelper = new FinanceDBHelper(requireContext());
        mDatabase = dbHelper.getWritableDatabase();
        mDatabase.beginTransaction();
        try {
            dbIncomeAdapter = new IncomeDBAdapter(requireContext(), returnAllIncomeItems());
            dbBillsAdapter = new BillsDBAdapter(requireContext(), returnAllBillItems());
            dbExpensesAdapter = new ExpensesDBAdapter(requireContext(), returnAllExpenseItems());
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch(position){
            case 0:
                setupFinanceData();
                break;
            case 1:
                setupMonthData("January");
                break;
            case 2:
                setupMonthData("February");
                break;
            case 3:
                setupMonthData("March");
                break;
            case 4:
                setupMonthData("April");
                break;
            case 5:
                setupMonthData("May");
                break;
            case 6:
                setupMonthData("June");
                break;
            case 7:
                setupMonthData("July");
                break;
            case 8:
                setupMonthData("August");
                break;
            case 9:
                setupMonthData("September");
                break;
            case 10:
                setupMonthData("October");
                break;
            case 11:
                setupMonthData("November");
                break;
            case 12:
                setupMonthData("December");
                break;
        }

        setupBarChart();
        setupListView();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setupFinanceData() {
        List<Double> totalAmounts = new ArrayList<>();

        mDatabase.beginTransaction();
        try {
            dbIncomeAdapter = new IncomeDBAdapter(requireContext(), returnAllIncomeItems());
            dbBillsAdapter = new BillsDBAdapter(requireContext(), returnAllBillItems());
            dbExpensesAdapter = new ExpensesDBAdapter(requireContext(), returnAllExpenseItems());

            totalAmounts.add(dbIncomeAdapter.calculateTotal());

            totalAmounts.add(dbBillsAdapter.calculateTotal());

            totalAmounts.add(dbExpensesAdapter.calculateTotal());

            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }

        financeAmounts = new double[totalAmounts.size()];
        for(int i = 0; i < financeAmounts.length; i++){
            financeAmounts[i] = totalAmounts.get(i);
        }
    }

    private void setupBarChart() {
        BarChart barChart = requireActivity().findViewById(R.id.mainBarChart);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(desc));
        if (dbIncomeAdapter.calculateTotal() == 0.0 && dbBillsAdapter.calculateTotal() == 0.0 && dbExpensesAdapter.calculateTotal() == 0.0) {
            barChart.invalidate();
            barChart.clear();
            barChart.setNoDataText("No data available. Please input your finance data.");
            barChart.setNoDataTextColor(Color.parseColor("#000000"));

        } else {
            ArrayList<BarEntry> barList = new ArrayList<>();

            barList.add(new BarEntry(1, (float) financeAmounts[0]));

            barList.add(new BarEntry(2, (float) financeAmounts[1]));

            barList.add(new BarEntry(3, (float) financeAmounts[2]));

            BarDataSet dataSet = new BarDataSet(barList, "Finance Information");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

            BarData data = new BarData(dataSet);

            barChart.setData(data);
            barChart.animateY(1000);
            barChart.invalidate();
        }
    }

    private void setupListView() {

        double incomePercentage = 0.0;
        if(financeAmounts[0] != 0.0)
            incomePercentage = financeAmounts[0]/(financeAmounts[0]+financeAmounts[1]+financeAmounts[2]) * 100;

        double billPercentage = 0.0;
        if(financeAmounts[1] != 0.0)
            billPercentage = financeAmounts[1]/(financeAmounts[0]+financeAmounts[1]+financeAmounts[2]) * 100;

        double expensePercentage = 0.0;
        if(financeAmounts[2] != 0.0)
            expensePercentage = financeAmounts[2]/(financeAmounts[0]+financeAmounts[1]+financeAmounts[2]) * 100;

        ListView listView = requireActivity().findViewById(R.id.mainListView);
        ArrayList<FinanceTypeItem> financeTypeList = new ArrayList<>();
        financeTypeList.add(new FinanceTypeItem("Income: " + financeAmounts[0] + " (" + round(incomePercentage, 2) + "%)", R.drawable.revenue128));
        financeTypeList.add(new FinanceTypeItem("Bills: " + financeAmounts[1] + " (" + round(billPercentage, 2) + "%)", R.drawable.receipt128));
        financeTypeList.add(new FinanceTypeItem("Expenses: " +  + financeAmounts[2] + " (" + round(expensePercentage, 2) + "%)", R.drawable.expense128));

        FinanceTypeAdapter listAdapter = new FinanceTypeAdapter(requireContext(), financeTypeList);
        listView.setAdapter(listAdapter);

        tvIncomeTally.setText(String.valueOf(round(financeAmounts[0]-financeAmounts[1]-financeAmounts[2], 2)));
        if(financeAmounts[0]-financeAmounts[1]-financeAmounts[2] < 0.0)
            tvIncomeTally.setTextColor(Color.RED);
        else if(financeAmounts[0]-financeAmounts[1]-financeAmounts[2] > 0.0)
            tvIncomeTally.setTextColor(Color.GREEN);
        else
            tvIncomeTally.setTextColor(Color.BLACK);
    }

    private void setupMonthData(String month){
        String queryIncome = "SELECT * FROM " + DataContract.IncomeEntry.TABLE_NAME + " WHERE incomeMonth=?";
        String queryBills = "SELECT * FROM " + DataContract.BillsEntry.TABLE_NAME + " WHERE billMonth=?";
        String queryExpenses = "SELECT * FROM " + DataContract.ExpensesEntry.TABLE_NAME + " WHERE expenseMonth=?";

        List<Double> totalAmounts = new ArrayList<>();

        mDatabase.beginTransaction();
        try {
            dbIncomeAdapter = new IncomeDBAdapter(requireContext(), mDatabase.rawQuery(queryIncome, new String[]{month}));
            dbBillsAdapter = new BillsDBAdapter(requireContext(), mDatabase.rawQuery(queryBills, new String[]{month}));
            dbExpensesAdapter = new ExpensesDBAdapter(requireContext(), mDatabase.rawQuery(queryExpenses, new String[]{month}));

            totalAmounts.add(dbIncomeAdapter.calculateTotal());

            totalAmounts.add(dbBillsAdapter.calculateTotal());

            totalAmounts.add(dbExpensesAdapter.calculateTotal());
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }

        financeAmounts = new double[totalAmounts.size()];
        for(int i = 0; i < financeAmounts.length; i++){
            financeAmounts[i] = totalAmounts.get(i);
        }

    }

    public Cursor returnAllIncomeItems() {
        Cursor cursor;
        mDatabase.beginTransaction();
        try {
            cursor = mDatabase.query(
                    DataContract.IncomeEntry.TABLE_NAME, null, null, null, null, null, null
            );
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }

        return cursor;
    }

    public Cursor returnAllBillItems(){
        Cursor cursor;
        mDatabase.beginTransaction();
        try {
            cursor = mDatabase.query(
                    DataContract.BillsEntry.TABLE_NAME, null, null, null, null, null, null
            );
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }

        return cursor;
    }

    public Cursor returnAllExpenseItems(){
        Cursor cursor;
        mDatabase.beginTransaction();
        try {
            cursor = mDatabase.query(
                    DataContract.ExpensesEntry.TABLE_NAME, null, null, null, null, null, null
            );
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }

        return cursor;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}