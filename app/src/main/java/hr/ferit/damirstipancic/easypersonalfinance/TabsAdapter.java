package hr.ferit.damirstipancic.easypersonalfinance;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import hr.ferit.damirstipancic.easypersonalfinance.tabs.BillsTab;
import hr.ferit.damirstipancic.easypersonalfinance.tabs.ExpensesTab;
import hr.ferit.damirstipancic.easypersonalfinance.tabs.HomeTab;
import hr.ferit.damirstipancic.easypersonalfinance.tabs.IncomeTab;

public class TabsAdapter extends FragmentStateAdapter {
    public TabsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 1:
                return new IncomeTab();
            case 2:
                return new BillsTab();
            case 3:
                return new ExpensesTab();
        }
        return new HomeTab();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
