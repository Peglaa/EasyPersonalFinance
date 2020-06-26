package hr.ferit.damirstipancic.easypersonalfinance.bills;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

import hr.ferit.damirstipancic.easypersonalfinance.DataContract;

import hr.ferit.damirstipancic.easypersonalfinance.R;

public class BillsDBAdapter extends RecyclerView.Adapter<BillsDBAdapter.BillsViewHolder> {

    private final Context mContext;
    private final Cursor mCursor;

    public BillsDBAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }


    public static class BillsViewHolder extends RecyclerView.ViewHolder{

        public final TextView billType;
        public final TextView billAmount;
        public final TextView kune;
        public final TextView dueDate;
        public final ImageView ivCircle;

        public BillsViewHolder(@NonNull View itemView) {
            super(itemView);

            billAmount = itemView.findViewById(R.id.tvBillAmount);
            billType = itemView.findViewById(R.id.tvBillType);
            dueDate = itemView.findViewById(R.id.tvDueDate);
            kune = itemView.findViewById(R.id.tvBillsKune);
            ivCircle = itemView.findViewById(R.id.ivBillsCircle);

        }
    }

    @NonNull
    @Override
    public BillsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.bills_recycler_cell, parent, false);
        return new BillsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillsViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position))
            return;


        String kune = "kn";
        String type = mCursor.getString(mCursor.getColumnIndex(DataContract.BillsEntry.COLUMN_TYPE));
        double amount = mCursor.getDouble(mCursor.getColumnIndex(DataContract.BillsEntry.COLUMN_AMOUNT));
        String date = mCursor.getString(mCursor.getColumnIndex(DataContract.BillsEntry.COLUMN_DATE));
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        Date temp = null;
        try {
            temp = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat fullFormatter = DateFormat.getDateInstance(DateFormat.FULL);
        if (temp != null) {
            date = fullFormatter.format(temp);
        }
        int day = date.indexOf(",");
        String shortDate = date.substring(0, day).substring(0, 3);
        date = shortDate + ", " + date.substring(day + 1);
        byte[] imageBytes = mCursor.getBlob(mCursor.getColumnIndex(DataContract.BillsEntry.COLUMN_IMAGE));
        Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        long id = mCursor.getLong(mCursor.getColumnIndex(DataContract.BillsEntry._ID));

        holder.billType.setText(type);
        holder.billAmount.setText(String.valueOf(amount));
        holder.dueDate.setText(date);
        if ((mCursor.getInt(mCursor.getColumnIndex(DataContract.BillsEntry.COLUMN_OVERDUE))) == 1)
            holder.dueDate.setTextColor(Color.RED);
        holder.kune.setText(kune);
        holder.ivCircle.setImageBitmap(image);
        holder.itemView.setTag(id);

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    /*public void swapCursor(Cursor newCursor) {
        if (mCursor != null)
            mCursor.close();

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }*/

    public double calculateTotal(){
        double tempTotal = 0.0;
        if(mCursor.moveToPosition(0)) {
            tempTotal += mCursor.getDouble(mCursor.getColumnIndex("billAmount"));
            while (mCursor.move(1)) {
                tempTotal += mCursor.getDouble(mCursor.getColumnIndex("billAmount"));
            }
        }
        return tempTotal;
    }

    /*public List<Double> getEveryIncome(){
        List<Double> amounts = new ArrayList<>();
        if(mCursor.moveToPosition(0)){
            amounts.add(mCursor.getDouble(mCursor.getColumnIndex("billAmount")));
            while (mCursor.move(1)) {
                amounts.add(mCursor.getDouble(mCursor.getColumnIndex("billAmount")));
            }
        }
        return amounts;
    }*/

}
