package hr.ferit.damirstipancic.easypersonalfinance.income;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import hr.ferit.damirstipancic.easypersonalfinance.DataContract;
import hr.ferit.damirstipancic.easypersonalfinance.R;


public class IncomeDBAdapter extends RecyclerView.Adapter<IncomeDBAdapter.IncomeViewHolder> {

    private final Context mContext;
    private Cursor mCursor;

    public IncomeDBAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public static class IncomeViewHolder extends RecyclerView.ViewHolder{

        public final TextView typeText;
        public final TextView amount;
        public final TextView mKune;
        public final TextView incomeDate;
        public final ImageView ivImage;

        public IncomeViewHolder(@NonNull final View itemView) {
            super(itemView);

            typeText = itemView.findViewById(R.id.tvIncomeType);
            amount = itemView.findViewById(R.id.tvIncomeAmount);
            ivImage = itemView.findViewById(R.id.ivIncomeImage);
            mKune = itemView.findViewById(R.id.tvKune);
            incomeDate = itemView.findViewById(R.id.tvIncomeDate);
        }
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.income_recycler_cell, parent, false);
        return new IncomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final IncomeViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position))
            return;

        String type = mCursor.getString(mCursor.getColumnIndex(DataContract.IncomeEntry.COLUMN_TYPE));
        double amount = mCursor.getDouble(mCursor.getColumnIndex(DataContract.IncomeEntry.COLUMN_AMOUNT));
        String kune = "kn";
        long id = mCursor.getLong(mCursor.getColumnIndex(DataContract.IncomeEntry._ID));
        byte[] imageBytes = mCursor.getBlob(mCursor.getColumnIndex(DataContract.IncomeEntry.COLUMN_IMAGE));
        Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        String date = mCursor.getString(mCursor.getColumnIndex(DataContract.IncomeEntry.COLUMN_DATE));

        holder.mKune.setText(kune);
        holder.typeText.setText(type);
        holder.amount.setText(String.valueOf(amount));
        holder.ivImage.setImageBitmap(image);
        holder.incomeDate.setText(date);
        holder.itemView.setTag(id);
    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null)
            mCursor.close();

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    public double calculateTotal(){
        double tempTotal = 0.0;
        if(mCursor.moveToPosition(0)) {
            tempTotal += mCursor.getDouble(mCursor.getColumnIndex("incomeAmount"));
            while (mCursor.move(1)) {
                tempTotal += mCursor.getDouble(mCursor.getColumnIndex("incomeAmount"));
            }
        }
        return tempTotal;
    }

    /*public List<Double> getEveryIncome(){
        List<Double> amounts = new ArrayList<>();
        if(mCursor.moveToPosition(0)){
            amounts.add(mCursor.getDouble(mCursor.getColumnIndex("incomeAmount")));
            while (mCursor.move(1)) {
                amounts.add(mCursor.getDouble(mCursor.getColumnIndex("incomeAmount")));
            }
        }
        return amounts;
    }*/

}
