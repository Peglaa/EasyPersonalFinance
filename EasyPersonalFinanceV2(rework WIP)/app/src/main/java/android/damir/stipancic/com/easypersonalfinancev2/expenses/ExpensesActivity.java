package android.damir.stipancic.com.easypersonalfinancev2.expenses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.damir.stipancic.com.easypersonalfinancev2.R;
import android.damir.stipancic.com.easypersonalfinancev2.UtilityClass;
import android.os.Bundle;

public class ExpensesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        UtilityClass.setupToolbar(this, R.id.expenses_toolbar);
        if(getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.expenses_title);
    }
}