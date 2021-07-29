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

    public static void setStatusBarColor(Activity activity) {
        Window window = activity.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
    }

    public static void setupToolbar(AppCompatActivity activity, int toolbar_resource){
        Toolbar toolbar = activity.findViewById(toolbar_resource);
        activity.setSupportActionBar(toolbar);
    }
}
