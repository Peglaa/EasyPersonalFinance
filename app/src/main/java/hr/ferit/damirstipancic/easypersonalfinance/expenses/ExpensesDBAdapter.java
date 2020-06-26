package hr.ferit.damirstipancic.easypersonalfinance.expenses;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import hr.ferit.damirstipancic.easypersonalfinance.FinanceDBHelper;
import hr.ferit.damirstipancic.easypersonalfinance.R;

public class ExpensesDBAdapter extends RecyclerView.Adapter<ExpensesDBAdapter.ExpensesViewHolder> {

    private final Context mContext;
    private Cursor mCursor;

    public ExpensesDBAdapter(Context context, Cursor cursor){

        mContext = context;
        mCursor = cursor;
    }

    public static class ExpensesViewHolder extends RecyclerView.ViewHolder{

        public final TextView expenseType;
        public final TextView expenseAmount;
        public final TextView expenseDate;
        public final TextView kune;
        public final ImageView expenseImage;

        public ExpensesViewHolder(@NonNull View itemView){
            super(itemView);

            expenseAmount = itemView.findViewById(R.id.tvExpenseAmount);
            expenseType = itemView.findViewById(R.id.tvExpenseType);
            kune = itemView.findViewById(R.id.tvExpenseKune);
            expenseDate = itemView.findViewById(R.id.tvExpenseDate);
            expenseImage = itemView.findViewById(R.id.ivExpenseImage);

        }
    }


    @NonNull
    @Override
    public ExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.expense_recycler_cell, parent, false);
        return new ExpensesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position))
            return;

        FinanceDBHelper dbHelper = new FinanceDBHelper(mContext);
        SQLiteDatabase mDatabase = dbHelper.getWritableDatabase();

        String kune = "kn";

        mDatabase.beginTransaction();
        try {
            String type = mCursor.getString(mCursor.getColumnIndex(DataContract.ExpensesEntry.COLUMN_TYPE));
            double amount = mCursor.getDouble(mCursor.getColumnIndex(DataContract.ExpensesEntry.COLUMN_AMOUNT));
            byte[] imageBytes = mCursor.getBlob(mCursor.getColumnIndex(DataContract.ExpensesEntry.COLUMN_IMAGE));
            Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

            String date = mCursor.getString(mCursor.getColumnIndex(DataContract.ExpensesEntry.COLUMN_DATE));
            long id = mCursor.getLong(mCursor.getColumnIndex(DataContract.ExpensesEntry._ID));

            holder.expenseType.setText(type);
            holder.expenseAmount.setText(String.valueOf(amount));
            holder.kune.setText(kune);
            holder.expenseDate.setText(date);
            holder.expenseImage.setImageBitmap(image);
            holder.itemView.setTag(id);
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null)
            mCursor.close();

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public double calculateTotal(){
        double tempTotal = 0.0;
        if(mCursor.moveToPosition(0)) {
            tempTotal += mCursor.getDouble(mCursor.getColumnIndex("expenseAmount"));
            while (mCursor.move(1)) {
                tempTotal += mCursor.getDouble(mCursor.getColumnIndex("expenseAmount"));
            }
        }
        return tempTotal;
    }

}
