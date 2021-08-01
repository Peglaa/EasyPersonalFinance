package android.damir.stipancic.com.easypersonalfinancev2;

import java.util.Date;
import java.util.UUID;

public class FinanceEntry {

    private final UUID mId;
    private final int mType;
    private final String mName;
    private Date mDate;
    private Date mDueDate;
    private float mAmount;

    public FinanceEntry(UUID id, int type, String name, Date date, Date dueDate, float amount){
        this.mId = id;

        if(type < 1 || type > 3)
            throw new IllegalArgumentException("value is out of range for type");
        else
            this.mType = type;

        this.mName = name;
        this.mDate = date;
        this.mDueDate = dueDate;
        this.mAmount = amount;
    }
}
