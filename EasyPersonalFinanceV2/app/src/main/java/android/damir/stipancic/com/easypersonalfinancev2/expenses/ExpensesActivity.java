package android.damir.stipancic.com.easypersonalfinancev2.expenses;

import androidx.appcompat.app.AppCompatActivity;

import android.damir.stipancic.com.easypersonalfinancev2.R;
import android.damir.stipancic.com.easypersonalfinancev2.UtilityClass;
import android.os.Bundle;

public class ExpensesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        //colorPrimaryDark not working to set status bar color so I have to do this??!?!?
        UtilityClass.setStatusBarColor(this);
    }
}