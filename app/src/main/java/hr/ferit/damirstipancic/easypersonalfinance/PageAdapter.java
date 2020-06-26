package hr.ferit.damirstipancic.easypersonalfinance;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import hr.ferit.damirstipancic.easypersonalfinance.tabs.BillsTab;
import hr.ferit.damirstipancic.easypersonalfinance.tabs.ExpensesTab;
import hr.ferit.damirstipancic.easypersonalfinance.tabs.HomeTab;
import hr.ferit.damirstipancic.easypersonalfinance.tabs.IncomeTab;

public class PageAdapter extends FragmentPagerAdapter {

    private final int numberOfTabs;
    public PageAdapter(@NonNull FragmentManager fm, int numberOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numberOfTabs = numberOfTabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return HomeTab.newInstance();

            case 1:
                return IncomeTab.newInstance();

            case 2:
                return BillsTab.newInstance();

            default:
                return ExpensesTab.newInstance();

        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
