package android.damir.stipancic.com.easypersonalfinancev2;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FinanceEntry extends RealmObject {

    @PrimaryKey
    private UUID mId;
    private int mType;
    private String mName;
    private Date mDate;
    private Date mDueDate;
    private float mAmount;
    private int mImage;

    public FinanceEntry(){

    }

    public FinanceEntry(UUID id, int type, String name, Date date, Date dueDate, float amount, int image){
        this.mId = id;

        if(type < 1 || type > 3)
            throw new IllegalArgumentException("value is out of range for type");
        else
            this.mType = type;

        this.mName = name;
        this.mDate = date;
        this.mDueDate = dueDate;
        this.mAmount = amount;
        this.mImage = image;
    }

    public UUID getId() {
        return mId;
    }

    public int getType() {
        return mType;
    }

    public String getName() {
        return mName;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Date getDueDate() {
        return mDueDate;
    }

    public void setDueDate(Date dueDate) {
        mDueDate = dueDate;
    }

    public float getAmount() {
        return mAmount;
    }

    public void setAmount(float amount) {
        mAmount = amount;
    }

    public void setImage(int image){
        mImage = image;
    }

    public int getImage(){
        return mImage;
    }

    @NonNull
    @Override
    public String toString() {
        return "FinanceEntry{" +
                "mId=" + mId +
                ", mType=" + mType +
                ", mName='" + mName + '\'' +
                ", mDate=" + mDate +
                ", mDueDate=" + mDueDate +
                ", mAmount=" + mAmount +
                '}';
    }
}
