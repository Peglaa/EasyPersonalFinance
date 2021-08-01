package android.damir.stipancic.com.easypersonalfinancev2.income;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.damir.stipancic.com.easypersonalfinancev2.R;
import android.damir.stipancic.com.easypersonalfinancev2.UtilityClass;
import android.os.Bundle;

public class IncomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        UtilityClass.setupToolbar(this, R.id.income_toolbar);
        if(getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.income_title);
    }
}