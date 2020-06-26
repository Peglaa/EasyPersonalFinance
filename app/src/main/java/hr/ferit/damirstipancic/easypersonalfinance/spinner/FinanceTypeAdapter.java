package hr.ferit.damirstipancic.easypersonalfinance.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import hr.ferit.damirstipancic.easypersonalfinance.R;

public class FinanceTypeAdapter extends ArrayAdapter<FinanceTypeItem> {

    public FinanceTypeAdapter(Context context, ArrayList<FinanceTypeItem> typeList){
        super(context, 0, typeList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_image_row, parent, false);

        ImageView imageViewType = convertView.findViewById(R.id.ivSpinnerIcon);
        TextView textViewType = convertView.findViewById(R.id.tvSpinnerText);

        FinanceTypeItem currentItem = getItem(position);
        if(currentItem != null) {
            imageViewType.setImageResource(currentItem.getImage());
            textViewType.setText(currentItem.getType());
        }

        return convertView;
    }
}
