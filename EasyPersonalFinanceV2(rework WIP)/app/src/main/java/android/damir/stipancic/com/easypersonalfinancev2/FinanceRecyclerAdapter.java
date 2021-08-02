package android.damir.stipancic.com.easypersonalfinancev2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FinanceRecyclerAdapter extends RecyclerView.Adapter<FinanceViewHolder> {

    private Context context;
    private ArrayList<FinanceEntry> entries;

    public FinanceRecyclerAdapter(Context context, ArrayList<FinanceEntry> entries) {
        this.context = context;
        this.entries = entries;
    }

    @NonNull
    @Override
    public FinanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.finance_recycler_item, parent, false);
        return new FinanceViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull FinanceViewHolder holder, int position) {
        holder.bindData(entries.get(position));
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }
}
