package hr.ferit.damirstipancic.easypersonalfinance.spinner;

public class FinanceTypeItem {

    private final String mType;
    private final int mFinanceImage;

    public FinanceTypeItem(String type, int financeImage){
        mType = type;
        mFinanceImage = financeImage;
    }

    public String getType(){
        return mType;
    }

    public int getImage(){
        return mFinanceImage;
    }
}
