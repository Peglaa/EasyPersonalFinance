package hr.ferit.damirstipancic.easypersonalfinance.expenses;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import hr.ferit.damirstipancic.easypersonalfinance.R;
import hr.ferit.damirstipancic.easypersonalfinance.spinner.FinanceTypeAdapter;
import hr.ferit.damirstipancic.easypersonalfinance.spinner.FinanceTypeItem;

import static hr.ferit.damirstipancic.easypersonalfinance.bills.AddBillFragment.getBitmapAsByteArray;
import static hr.ferit.damirstipancic.easypersonalfinance.income.AddIncomeFragment.convertMonthToString;
import static hr.ferit.damirstipancic.easypersonalfinance.income.AddIncomeFragment.hideKeyboard;

public class AddExpenseFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ArrayList<FinanceTypeItem> expenseTypeList;
    private byte[] spinnerImage;
    public static final String[] expenseType = {"Food & Groceries", "Gas", "Gifts & Donations", "Health & Fitness", "Personal Care", "Pet Care", "Transport", "Shopping", "Travel & Vacation", "Home Maintenance", "Kids Care", "Loan", "Others"};
    public static final int[] imageType = {R.drawable.groceries128, R.drawable.fuel128, R.drawable.donation128,
                                            R.drawable.fitness128, R.drawable.selfcare128, R.drawable.pet128,
                                            R.drawable.transport128, R.drawable.shopping128, R.drawable.travel128,
                                            R.drawable.repair128, R.drawable.children128, R.drawable.loan128, R.drawable.money128};

    /**
     * ALL ICONS MADE BY FREEPIK: "https://www.flaticon.com/authors/freepik"
     */

    private TextView tvExpenseDate;
    private EditText etExpenseAmount;
    private int mMonth;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private String spinnerSelection, date;
    private AddExpenseFragment.OnExpenseFragmentInteractionListener mListener;

    public AddExpenseFragment() {
        // Required empty public constructor
    }

    public static AddExpenseFragment newInstance() {
        return new AddExpenseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_expense, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        setupLayout();
        initSpinnerList();
        setupDatePicker();
        setupExpenseTypeSpinner();
        setupConfirmExpense();
    }

    private void initSpinnerList() {

        expenseTypeList = new ArrayList<>();
        for(int i = 0; i < expenseType.length; i++){
            expenseTypeList.add(new FinanceTypeItem(expenseType[i], imageType[i]));
        }
    }

    private void setupLayout() {

        TextView tvExpenseDateDesc = requireActivity().findViewById(R.id.tvExpenseDateDesc);
        tvExpenseDateDesc.setText(R.string.expensedate);
        tvExpenseDate = requireActivity().findViewById(R.id.tvExpenseFragmentDate);
        etExpenseAmount = requireActivity().findViewById(R.id.etExpenseAmount);

        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        mMonth = c.get(Calendar.MONTH)+1;

        DateFormat fullFormatter = DateFormat.getDateInstance(DateFormat.FULL);
        date = fullFormatter.format(currentDate);
        int day = date.indexOf(",");
        String shortDate = date.substring(0, day).substring(0,3);
        date = shortDate + ", " + date.substring(day+1);
        tvExpenseDate.setText(date);
    }

    private void setupDatePicker() {

        tvExpenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(requireContext(), R.style.DialogTheme, mOnDateSetListener,year,month,day);
                dialog.show();
            }
        });

        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                mMonth = month;
                date = dayOfMonth + "/" + month + "/" + year;
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
                Date temp = null;
                try {
                    temp = formatter.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateFormat fullFormatter = DateFormat.getDateInstance(DateFormat.FULL);
                if (temp != null) {
                    date = fullFormatter.format(temp);
                }
                int day = date.indexOf(",");
                String shortDate = date.substring(0, day).substring(0,3);
                date = shortDate + ", " + date.substring(day+1);
                tvExpenseDate.setText(date);
            }
        };

    }

    private void setupExpenseTypeSpinner() {
        Spinner expenseTypeSpinner = requireActivity().findViewById(R.id.expenseTypeSpinner);
        FinanceTypeAdapter mSpinnerAdapter = new FinanceTypeAdapter(requireContext(), expenseTypeList);
        expenseTypeSpinner.setAdapter(mSpinnerAdapter);
        expenseTypeSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerSelection = expenseType[position];
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageType[position]);
        spinnerImage = getBitmapAsByteArray(bitmap);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setupConfirmExpense() {

        FloatingActionButton confirmExpense = requireView().findViewById(R.id.btnAddExpense);
        confirmExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etExpenseAmount.getText().toString();
                if (str.trim().isEmpty())
                    Toast.makeText(requireContext(), "You forgot to enter an amount!", Toast.LENGTH_SHORT).show();
                else {
                    double check = Double.parseDouble(str);
                    if(check <= 0)
                        Toast.makeText(requireContext(), "Please enter a positive expense amount!", Toast.LENGTH_SHORT).show();
                    else
                        addNewExpenseItem();
                }
            }
        });
    }

    private void addNewExpenseItem() {

        String str = etExpenseAmount.getText().toString();
        double amount = Double.parseDouble(str);
        String month = convertMonthToString(mMonth);
        hideKeyboard(requireActivity());
        sendExpenseItemInfo(spinnerSelection, spinnerImage, amount, month, date);
    }

    public interface OnExpenseFragmentInteractionListener{
        void onExpenseFragmentInteraction(String spinnerSelection, byte[] spinnerImage, double amount, String mMonth, String date);
    }

    private void sendExpenseItemInfo(String spinnerSelection, byte[] spinnerImage, double amount, String mMonth, String date){
        if(mListener != null)
            mListener.onExpenseFragmentInteraction(spinnerSelection, spinnerImage, amount, mMonth, date);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AddExpenseFragment.OnExpenseFragmentInteractionListener){
            mListener = (AddExpenseFragment.OnExpenseFragmentInteractionListener) context;
        }
        else
            throw new RuntimeException(context.toString() + "Must implement OnFragmentInteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}