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
import hr.ferit.damirstipancic.easypersonalfinance.bills.BillsDBAdapter;
import hr.ferit.damirstipancic.easypersonalfinance.spinner.FinanceTypeAdapter;
import hr.ferit.damirstipancic.easypersonalfinance.spinner.FinanceTypeItem;

import static hr.ferit.damirstipancic.easypersonalfinance.bills.AddBillFragment.imageType;
import static hr.ferit.damirstipancic.easypersonalfinance.bills.AddBillFragment.billType;
import static hr.ferit.damirstipancic.easypersonalfinance.tabs.HomeTab.round;
import static hr.ferit.damirstipancic.easypersonalfinance.tabs.IncomeTab.PIE_COLORS;
import static hr.ferit.damirstipancic.easypersonalfinance.tabs.IncomeTab.months;



public class BillsTab extends Fragment implements AdapterView.OnItemSelectedListener{

    private double[] financeAmounts;
    private SQLiteDatabase mDatabase;
    private ArrayList<String> typeList;

    public BillsTab() {
        // Required empty public constructor
    }

    public static BillsTab newInstance() {
        return new BillsTab();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bills_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FinanceDBHelper dbHelper = new FinanceDBHelper(requireContext());
        mDatabase = dbHelper.getWritableDatabase();

        setupSpinner(view);
    }

    private void setupSpinner(View view) {

        Spinner billsMonthSpinner = view.findViewById(R.id.sBillsTabMonthSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, months);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        billsMonthSpinner.setAdapter(spinnerAdapter);
        billsMonthSpinner.setOnItemSelectedListener(this);
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

        for (String s : billType) {
            if(addBillTypeMonth(s, month) != 0.0) {
                totalAmounts.add(addBillTypeMonth(s, month));
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

        for (String s : billType) {
            if(addBillsTypeTotal(s) != 0.0) {
                totalAmounts.add(addBillsTypeTotal(s));
                typeList.add(s);
            }
        }

        financeAmounts = new double[totalAmounts.size()];
        for(int i = 0; i < financeAmounts.length; i++){
            financeAmounts[i] = totalAmounts.get(i);
        }
    }

    private void setupPieChart() {

        PieChart pieChart = requireView().findViewById(R.id.billsPieChart);
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

            PieDataSet dataSet = new PieDataSet(pieList, "Bills preview");
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

        ListView listView = requireView().findViewById(R.id.billsListView);
        ArrayList<FinanceTypeItem> financeTypeList = new ArrayList<>();
        for(int i = 0; i < financeAmounts.length; i++) {

            financeTypeList.add( new FinanceTypeItem(typeList.get(i) + ": " + financeAmounts[i] + " (" + round(percentage[i], 2) + "%)", returnDrawable(typeList.get(i))));
        }

        FinanceTypeAdapter listAdapter = new FinanceTypeAdapter(requireContext(), financeTypeList);
        listView.setAdapter(listAdapter);

    }

    public double addBillsTypeTotal(String type){
        Cursor mCursor;
        mDatabase.beginTransaction();
        try {
            String query = "SELECT * FROM " + DataContract.BillsEntry.TABLE_NAME + " WHERE billType=?";
            mCursor = mDatabase.rawQuery(query, new String[]{type});
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
        BillsDBAdapter adapter = new BillsDBAdapter(requireContext(), mCursor);
        return adapter.calculateTotal();
    }

    public double addBillTypeMonth(String type, String month){
        Cursor mCursor;
        mDatabase.beginTransaction();
        try {
            String query = "SELECT * FROM " + DataContract.BillsEntry.TABLE_NAME + " WHERE billType=? AND billMonth=?";
            mCursor = mDatabase.rawQuery(query, new String[]{type, month});
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
        BillsDBAdapter adapter = new BillsDBAdapter(requireContext(), mCursor);
        return adapter.calculateTotal();
    }

    private int returnDrawable(String type) {
        switch(type){
            case "Car Loan":
                return imageType[0];

            case "Education":
                return imageType[1];

            case "Electricity":
                return imageType[2];

            case "Entertainment":
                return imageType[3];

            case "Events":
                return imageType[4];

            case "Home Loan":
                return imageType[5];

            case "Rent":
                return imageType[6];

            case "Insurance":
                return imageType[7];

            case "Internet":
                return imageType[8];

            case "Mobile":
                return imageType[9];

            case "Phone":
                return imageType[10];

            case "Water":
                return imageType[11];

            default:
                return imageType[12];

        }
    }
}