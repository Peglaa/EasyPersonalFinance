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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import hr.ferit.damirstipancic.easypersonalfinance.DataContract;
import hr.ferit.damirstipancic.easypersonalfinance.FinanceDBHelper;
import hr.ferit.damirstipancic.easypersonalfinance.R;
import hr.ferit.damirstipancic.easypersonalfinance.income.IncomeDBAdapter;
import hr.ferit.damirstipancic.easypersonalfinance.spinner.FinanceTypeAdapter;
import hr.ferit.damirstipancic.easypersonalfinance.spinner.FinanceTypeItem;

import static hr.ferit.damirstipancic.easypersonalfinance.income.AddIncomeFragment.imageIncomeType;
import static hr.ferit.damirstipancic.easypersonalfinance.tabs.HomeTab.round;

public class IncomeTab extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final int[] PIE_COLORS = {
            Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53), Color.rgb(0,139,139),
            Color.rgb(0,0,255), Color.rgb(0,255,255), Color.rgb(138,43,226),
            Color.rgb(219,112,147), Color.rgb(176,196,222), Color.rgb(47,79,79),
            Color.rgb(30,144,255)};
    public static final String[] months = {"All", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static final String[] incomeType = {"Salary", "Brokerage", "Gifts", "Business", "Interest", "Loan", "Rental income", "Selling income", "Others"};
    private double[] financeAmounts;
    private SQLiteDatabase mDatabase;
    private ArrayList<String> typeList;

    public IncomeTab() {
        // Required empty public constructor
    }

    public static IncomeTab newInstance() {

        return new IncomeTab();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_income_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FinanceDBHelper dbHelper = new FinanceDBHelper(requireContext());
        mDatabase = dbHelper.getWritableDatabase();

        setupSpinner();
    }

    private void setupSpinner() {

        Spinner mainMonthSpinner = requireActivity().findViewById(R.id.sIncomeTabMonthSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, months);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainMonthSpinner.setAdapter(spinnerAdapter);
        mainMonthSpinner.setOnItemSelectedListener(this);
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

        setupPieChart();
        setupListView();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setupMonthData(String month) {

        List<Double> totalAmounts = new ArrayList<>();
        typeList = new ArrayList<>();

        for (String s : incomeType) {
            if(addIncomeTypeMonth(s, month) != 0.0) {
                totalAmounts.add(addIncomeTypeMonth(s, month));
                typeList.add(s);
            }
        }

        financeAmounts = new double[totalAmounts.size()];
        for(int i = 0; i < financeAmounts.length; i++){
            financeAmounts[i] = totalAmounts.get(i);
        }
    }

    private void setupFinanceData() {
        List<Double> totalAmounts = new ArrayList<>();
        typeList = new ArrayList<>();

        for (String s : incomeType) {
            if(addIncomeTypeTotal(s) != 0.0) {
                totalAmounts.add(addIncomeTypeTotal(s));
                typeList.add(s);
            }
        }

        financeAmounts = new double[totalAmounts.size()];
        for(int i = 0; i < financeAmounts.length; i++){
            financeAmounts[i] = totalAmounts.get(i);
        }
    }

    private void setupPieChart() {

        PieChart pieChart = requireActivity().findViewById(R.id.incomePieChart);
        if (financeAmounts.length == 0) {
            pieChart.invalidate();
            pieChart.clear();
            pieChart.setNoDataText("No data available. Please input your finance data.");
            pieChart.setNoDataTextColor(Color.parseColor("#000000"));
        } else {
            ArrayList<PieEntry> pieList = new ArrayList<>();
            for(int i = 0; i<financeAmounts.length; i++){
                pieList.add(new PieEntry((float) financeAmounts[i], typeList.get(i)));
            }

            PieDataSet dataSet = new PieDataSet(pieList, "Income preview");
            dataSet.setColors(PIE_COLORS);
            PieData data = new PieData(dataSet);

            pieChart.setData(data);
            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColor(Color.parseColor("#ECECEC"));
            pieChart.getLegend().setEnabled(false);
            pieChart.getDescription().setEnabled(false);
            pieChart.invalidate();
            pieChart.animateY(1000);
        }
    }

    public void setupListView(){

        double total = 0.0;
        double[] percentage = new double[financeAmounts.length];
        for (double financeAmount : financeAmounts) {
            total += financeAmount;
        }

        for(int i = 0; i < financeAmounts.length; i++){
            percentage[i] = financeAmounts[i]/total*100;
        }

        ListView listView = requireActivity().findViewById(R.id.incomeListView);
        ArrayList<FinanceTypeItem> financeTypeList = new ArrayList<>();
        for(int i = 0; i < financeAmounts.length; i++) {

            financeTypeList.add( new FinanceTypeItem(typeList.get(i) + ": " + financeAmounts[i] + " (" + round(percentage[i], 2) + "%)", returnDrawable(typeList.get(i))));
        }

        FinanceTypeAdapter listAdapter = new FinanceTypeAdapter(requireContext(), financeTypeList);
        listView.setAdapter(listAdapter);

    }

    public double addIncomeTypeTotal(String type){
        Cursor mCursor;
        mDatabase.beginTransaction();
        try {
            String query = "SELECT * FROM " + DataContract.IncomeEntry.TABLE_NAME + " WHERE incomeType=?";
            mCursor = mDatabase.rawQuery(query, new String[]{type});
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
        IncomeDBAdapter adapter = new IncomeDBAdapter(requireContext(), mCursor);
        return adapter.calculateTotal();
    }

    public double addIncomeTypeMonth(String type, String month){
        Cursor mCursor;
        mDatabase.beginTransaction();
        try {
            String query = "SELECT * FROM " + DataContract.IncomeEntry.TABLE_NAME + " WHERE incomeType=? AND incomeMonth=?";
            mCursor = mDatabase.rawQuery(query, new String[]{type, month});
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
        IncomeDBAdapter adapter = new IncomeDBAdapter(requireContext(), mCursor);
        return adapter.calculateTotal();
    }

    private int returnDrawable(String type) {
        switch(type){
            case "Salary":
                return imageIncomeType[0];

            case "Brokerage":
                return imageIncomeType[1];

            case "Gifts":
                return imageIncomeType[2];

            case "Business":
                return imageIncomeType[3];

            case "Interest":
                return imageIncomeType[4];

            case "Loan":
                return imageIncomeType[5];

            case "Rental income":
                return imageIncomeType[6];

            case "Selling income":
                return imageIncomeType[7];

            default:
                return imageIncomeType[8];

        }
    }
}