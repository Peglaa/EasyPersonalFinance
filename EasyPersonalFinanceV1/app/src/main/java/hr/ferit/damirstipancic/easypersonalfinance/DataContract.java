package hr.ferit.damirstipancic.easypersonalfinance;

import android.provider.BaseColumns;

public class DataContract {

    private DataContract() {}

    public static final class IncomeEntry implements BaseColumns{
        public static final String TABLE_NAME = "incomeList";
        public static final String COLUMN_TYPE = "incomeType";
        public static final String COLUMN_AMOUNT = "incomeAmount";
        public static final String COLUMN_IMAGE = "incomeImage";
        public static final String COLUMN_DATE = "incomeDate";
        public static final String COLUMN_MONTH = "incomeMonth";
    }

    public static final class BillsEntry implements BaseColumns{
        public static final String TABLE_NAME = "billsList";
        public static final String COLUMN_TYPE = "billType";
        public static final String COLUMN_AMOUNT = "billAmount";
        public static final String COLUMN_DATE = "billDate";
        public static final String COLUMN_IMAGE = "billImage";
        public static final String COLUMN_OVERDUE = "billOverdue";
        public static final String COLUMN_MONTH = "billMonth";
    }

    public static final class ExpensesEntry implements BaseColumns{
        public static final String TABLE_NAME = "expensesList";
        public static final String COLUMN_TYPE = "expenseType";
        public static final String COLUMN_AMOUNT = "expenseAmount";
        public static final String COLUMN_DATE = "expenseDate";
        public static final String COLUMN_IMAGE = "expenseImage";
        public static final String COLUMN_MONTH = "expenseMonth";
    }
}
