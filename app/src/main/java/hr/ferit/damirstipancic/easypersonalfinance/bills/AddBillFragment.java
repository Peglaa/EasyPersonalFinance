package hr.ferit.damirstipancic.easypersonalfinance.bills;

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

import java.io.ByteArrayOutputStream;
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

import static hr.ferit.damirstipancic.easypersonalfinance.income.AddIncomeFragment.convertMonthToString;
import static hr.ferit.damirstipancic.easypersonalfinance.income.AddIncomeFragment.hideKeyboard;

public class AddBillFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ArrayList<FinanceTypeItem> billTypeList;
    public static final int[] imageType = {R.drawable.car128, R.drawable.school128, R.drawable.energy128,
                                            R.drawable.entertainment128, R.drawable.event128, R.drawable.real_estate128,
                                            R.drawable.rent128, R.drawable.shield128, R.drawable.worldwide128,
                                            R.drawable.smartphone128, R.drawable.call128, R.drawable.water128,
                                            R.drawable.money128};
    public static final String[] billType = {"Car Loan", "Education", "Electricity", "Entertainment", "Events", "Home Loan",
                                                "Rent", "Insurance", "Internet", "Mobile", "Phone", "Water", "Others"};
    /**
     * ALL ICONS MADE BY FREEPIK: "https://www.flaticon.com/authors/freepik"
     */
    private TextView tvBillDate;
    private EditText etBillAmount;
    private int mMonth;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private String spinnerSelection, date;
    private byte[] spinnerImage;
    private AddBillFragment.OnBillFragmentInteractionListener mListener;

    public AddBillFragment() {
        // Required empty public constructor
    }

    public static AddBillFragment newInstance() {
        return new AddBillFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_bill, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupLayout();
        initSpinnerList();
        setupBillTypeSpinner();
        setupDatePicker();
        setupConfirmBill();
    }


    private void setupLayout() {
        tvBillDate = requireView().findViewById(R.id.tvBillDueDate);
        TextView tvDateDesc = requireView().findViewById(R.id.tvDueDateDesc);
        etBillAmount = requireView().findViewById(R.id.etBillAmount);

        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        mMonth = c.get(Calendar.MONTH)+1;

        DateFormat fullFormatter = DateFormat.getDateInstance(DateFormat.FULL);
        DateFormat shortFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        date = fullFormatter.format(currentDate);
        int day = date.indexOf(",");
        String shortDate = date.substring(0, day).substring(0,3);
        date = shortDate + ", " + date.substring(day+1);
        tvBillDate.setText(date);
        date = shortFormatter.format(currentDate);
        tvDateDesc.setText(R.string.duedate);

    }

    private void setupDatePicker() {

        tvBillDate.setOnClickListener(new View.OnClickListener() {
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
                DateFormat shortFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
                if (temp != null) {
                    date = fullFormatter.format(temp);
                }
                int day = date.indexOf(",");
                String shortDate = date.substring(0, day).substring(0,3);
                date = shortDate + ", " + date.substring(day+1);
                tvBillDate.setText(date);
                if (temp != null) {
                    date = shortFormatter.format(temp);
                }
            }
        };

    }

    private void setupConfirmBill() {

        FloatingActionButton confirmBill = requireView().findViewById(R.id.btnAddBill);
        confirmBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etBillAmount.getText().toString();
                if (str.trim().isEmpty())
                    Toast.makeText(requireContext(), "You forgot to enter an amount!", Toast.LENGTH_SHORT).show();
                else {
                    double check = Double.parseDouble(str);
                    if(check <= 0)
                        Toast.makeText(requireContext(), "Please enter a positive bill amount!", Toast.LENGTH_SHORT).show();
                    else
                        addNewBillItem();
                }
            }
        });
    }

    private void addNewBillItem() {
        String str = etBillAmount.getText().toString();
        double amount = Double.parseDouble(str);
        String month = convertMonthToString(mMonth);
        hideKeyboard(requireActivity());
        sendBillItemInfo(spinnerSelection, spinnerImage, amount, month, date);
    }

    private void initSpinnerList() {

        billTypeList = new ArrayList<>();
        for (int i = 0; i < billType.length; i++) {
            billTypeList.add(new FinanceTypeItem(billType[i], imageType[i]));
        }
    }

    private void setupBillTypeSpinner() {

        Spinner billTypeSpinner = requireActivity().findViewById(R.id.billsTypeSpinner);
        FinanceTypeAdapter mSpinnerAdapter = new FinanceTypeAdapter(requireContext(), billTypeList);
        billTypeSpinner.setAdapter(mSpinnerAdapter);
        billTypeSpinner.setOnItemSelectedListener(this);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerSelection = billType[position];
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageType[position]);
        spinnerImage = getBitmapAsByteArray(bitmap);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public interface OnBillFragmentInteractionListener{
        void onBillFragmentInteraction(String spinnerSelection, byte[] spinnerImage, double amount, String mMonth, String date);
    }

    private void sendBillItemInfo(String spinnerSelection, byte[] spinnerImage, double amount, String mMonth, String date) {
        if(mListener != null){
            mListener.onBillFragmentInteraction(spinnerSelection, spinnerImage, amount, mMonth, date);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AddBillFragment.OnBillFragmentInteractionListener){
            mListener = (AddBillFragment.OnBillFragmentInteractionListener) context;
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