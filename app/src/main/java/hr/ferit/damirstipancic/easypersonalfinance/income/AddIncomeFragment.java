package hr.ferit.damirstipancic.easypersonalfinance.income;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddIncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddIncomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner incomeTypeSpinner;
    private ArrayList<FinanceTypeItem> incomeTypeList;
    private static final String[] incomeType = {"Salary", "Brokerage", "Gifts", "Business", "Interest", "Loan", "Rental income", "Selling income", "Others"};
    public static final int[] imageIncomeType = {R.drawable.growth128, R.drawable.brokerage128, R.drawable.gift128,
                                            R.drawable.business128, R.drawable.interest128, R.drawable.loan128,
                                            R.drawable.rent128, R.drawable.payment128, R.drawable.money128};
    /**
     * ALL ICONS MADE BY FREEPIK: "https://www.flaticon.com/authors/freepik"
     */
    private TextView tvDesc, tvDate;
    private EditText etAmount;
    private FloatingActionButton confirmIncome;
    private int mMonth;
    private byte[] spinnerImage;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private String spinnerSelection, date;
    private OnIncomeFragmentInteractionListener mListener;


    public AddIncomeFragment() {
        // Required empty public constructor
    }

    public static AddIncomeFragment newInstance() {
        return new AddIncomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_income, container, false);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupLayout();
        initSpinnerList();
        setupIncomeTypeSpinner();
        setupDatePicker();
        setupConfirmIncome();
    }

    private void initSpinnerList() {

        incomeTypeList = new ArrayList<>();
        for (int i = 0; i < incomeType.length; i++) {
            incomeTypeList.add(new FinanceTypeItem(incomeType[i], imageIncomeType[i]));
        }
    }


    private void setupLayout() {
        tvDesc = requireView().findViewById(R.id.tvIncomeDesc);
        tvDate = requireView().findViewById(R.id.tvDate);
        etAmount = requireView().findViewById(R.id.etAmount);
        incomeTypeSpinner = requireView().findViewById(R.id.incomeTypeSpinner);
        confirmIncome = requireView().findViewById(R.id.btnAddIncome);

        //Get current Date
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        mMonth = c.get(Calendar.MONTH)+1;

        //Format the curret date to FULL date format
        DateFormat fullFormatter = DateFormat.getDateInstance(DateFormat.FULL);
        date = fullFormatter.format(currentDate);

        //Style the date for viewing
        int day = date.indexOf(",");
        String shortDate = date.substring(0, day).substring(0,3);
        date = shortDate + ", " + date.substring(day+1);
        tvDate.setText(date);
    }

    private void setupIncomeTypeSpinner() {
        incomeTypeSpinner = requireActivity().findViewById(R.id.incomeTypeSpinner);
        FinanceTypeAdapter mSpinnerAdapter = new FinanceTypeAdapter(requireContext(), incomeTypeList);
        incomeTypeSpinner.setAdapter(mSpinnerAdapter);
        incomeTypeSpinner.setOnItemSelectedListener(this);
    }

    private void setupDatePicker() {

        tvDate.setOnClickListener(new View.OnClickListener() {
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

                //Convert selected date to Date format
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
                Date temp = null;
                try {
                    temp = formatter.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //Format the selected date to FULL format
                DateFormat fullFormatter = DateFormat.getDateInstance(DateFormat.FULL);
                if (temp != null) {
                    date = fullFormatter.format(temp);
                }

                //Style the date for viewing
                int day = date.indexOf(",");
                String shortDate = date.substring(0, day).substring(0,3);
                date = shortDate + ", " + date.substring(day+1);
                tvDate.setText(date);
            }
        };

    }

    private void setupConfirmIncome() {

        confirmIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etAmount.getText().toString();
                if (str.trim().isEmpty())
                    Toast.makeText(requireContext(), "You forgot to enter an amount!", Toast.LENGTH_SHORT).show();
                else {
                    double check = Double.parseDouble(str);
                    if(check <= 0)
                        Toast.makeText(requireContext(), "Please enter a positive income amount!", Toast.LENGTH_SHORT).show();
                    else
                        addNewIncomeItem();
                }
            }
        });
    }

    private void addNewIncomeItem() {
        String str = etAmount.getText().toString();
        double amount = Double.parseDouble(str);
        String month = convertMonthToString(mMonth);
        hideKeyboard(requireActivity());
        sendIncomeItemInfo(spinnerSelection, spinnerImage, amount, month, date);
    }

    public static String convertMonthToString(int month) {
        String convertedMonth = null;
        switch(month){
            case 1:
                convertedMonth = "January";
                break;
            case 2:
                convertedMonth = "February";
                break;
            case 3:
                convertedMonth = "March";
                break;
            case 4:
                convertedMonth = "April";
                break;
            case 5:
                convertedMonth = "May";
                break;
            case 6:
                convertedMonth = "June";
                break;
            case 7:
                convertedMonth = "July";
                break;
            case 8:
                convertedMonth = "August";
                break;
            case 9:
                convertedMonth = "September";
                break;
            case 10:
                convertedMonth = "October";
                break;
            case 11:
                convertedMonth = "November";
                break;
            case 12:
                convertedMonth = "December";
                break;
        }

        return convertedMonth;
    }

    private void sendIncomeItemInfo(String spinnerSelection, byte[] spinnerImage, double amount, String mMonth, String date) {
        if(mListener != null){
            mListener.onIncomeFragmentInteraction(spinnerSelection, spinnerImage, amount, mMonth, date);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerSelection = incomeType[position];
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageIncomeType[position]);
        spinnerImage = getBitmapAsByteArray(bitmap);
        switch(position){
            case 0:
                setIncomeDesc(R.string.salaryincomedesc);
                break;
            case 1:
                setIncomeDesc(R.string.brokerageincomedesc);
                break;
            case 2:
                setIncomeDesc(R.string.giftincomedesc);
                break;
            case 3:
                setIncomeDesc(R.string.businessincomedesc);
                break;
            case 4:
                setIncomeDesc(R.string.interesetincomedesc);
                break;
            case 5:
                setIncomeDesc(R.string.loanincomedesc);
                break;
            case 6:
                setIncomeDesc(R.string.rentalincomedesc);
                break;
            case 7:
                setIncomeDesc(R.string.salesincomedesc);
                break;
            case 8:
                setIncomeDesc(R.string.otherincomedesc);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setIncomeDesc(int desc){
        tvDesc.setText(desc);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnIncomeFragmentInteractionListener){
            mListener = (OnIncomeFragmentInteractionListener) context;
        }
        else
            throw new RuntimeException(context.toString() + "Must implement OnFragmentInteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnIncomeFragmentInteractionListener{
        void onIncomeFragmentInteraction(String spinnerSelection, byte[] spinnerImage, double amount, String mMonth, String date);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}
