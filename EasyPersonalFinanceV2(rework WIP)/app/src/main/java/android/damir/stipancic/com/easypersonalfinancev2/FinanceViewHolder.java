package android.damir.stipancic.com.easypersonalfinancev2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FinanceViewHolder extends RecyclerView.ViewHolder {

    private TextView mTvType, mTvDate, mTvDueDate, mTvAmount, mTvKune;
    private ImageView mIvDescImage;
    private Context mContext;

    public FinanceViewHolder(@NonNull View itemView, Context context) {
        super(itemView);

        mTvType = itemView.findViewById(R.id.tvItemType);
        mTvAmount = itemView.findViewById(R.id.tvItemAmount);
        mTvKune = itemView.findViewById(R.id.tvItemKune);
        mTvDate = itemView.findViewById(R.id.tvItemDate);
        mTvDueDate = itemView.findViewById(R.id.tvItemDueDate);
        mIvDescImage = itemView.findViewById(R.id.ivItemDescriptionImage);
        mContext = context;
    }

    public void bindData(final FinanceEntry entry){

        mTvKune.setText("kn");
        mTvAmount.setText(String.valueOf(entry.getAmount()));
        mTvType.setText(entry.getName());
        mTvDate.setText(entry.getDate().toString().substring(4, 10));
        if(entry.getType() == 2) mTvDueDate.setText(entry.getDueDate().toString().substring(4, 10));
        Bitmap image = BitmapFactory.decodeResource(mContext.getResources(), entry.getImage());
        mIvDescImage.setImageBitmap(image);
    }
}
