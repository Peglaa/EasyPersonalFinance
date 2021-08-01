package android.damir.stipancic.com.easypersonalfinancev2;

import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class UtilityClass {

    Context mContext;

    public UtilityClass(Context context){
        this.mContext = context;
    }

    public static void setupToolbar(AppCompatActivity activity, int toolbar_resource){
        Toolbar toolbar = activity.findViewById(toolbar_resource);
        activity.setSupportActionBar(toolbar);
    }
}
