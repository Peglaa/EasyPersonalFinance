package android.damir.stipancic.com.easypersonalfinancev2.bills;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.damir.stipancic.com.easypersonalfinancev2.R;
import android.damir.stipancic.com.easypersonalfinancev2.UtilityClass;
import android.os.Bundle;

public class BillsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);

        UtilityClass.setupToolbar(this, R.id.bills_toolbar);
        if(getSupportActionBar() != null) getSupportActionBar().setTitle(R.string.bills_title);
    }
}