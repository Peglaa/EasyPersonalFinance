package hr.ferit.damirstipancic.easypersonalfinance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import hr.ferit.damirstipancic.easypersonalfinance.DataContract.*;

import androidx.annotation.Nullable;

public class FinanceDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "financelist.db";
    public static final int DATABASE_VERSION = 1;

    public FinanceDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_INCOME_TABLE = "CREATE TABLE " + IncomeEntry.TABLE_NAME + " (" +
                IncomeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IncomeEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                IncomeEntry.COLUMN_AMOUNT + " DOUBLE NOT NULL, " +
                IncomeEntry.COLUMN_IMAGE + " BLOB NOT NULL, " +
                IncomeEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                IncomeEntry.COLUMN_MONTH + " INTEGER NOT NULL" +
                ");";

        final String SQL_CREATE_BILLS_TABLE = "CREATE TABLE " + BillsEntry.TABLE_NAME + " (" +
                BillsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BillsEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                BillsEntry.COLUMN_AMOUNT + " DOUBLE NOT NULL, " +
                BillsEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                BillsEntry.COLUMN_IMAGE + " BLOB NOT NULL, " +
                BillsEntry.COLUMN_OVERDUE + " INTEGER, " +
                BillsEntry.COLUMN_MONTH + " INTEGER NOT NULL " +
                ");";

        final String SQL_CREATE_EXPENSES_TABLE = "CREATE TABLE " + ExpensesEntry.TABLE_NAME + " (" +
                ExpensesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ExpensesEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                ExpensesEntry.COLUMN_AMOUNT + " DOUBLE NOT NULL, " +
                ExpensesEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                ExpensesEntry.COLUMN_IMAGE + " BLOB NOT NULL, " +
                ExpensesEntry.COLUMN_MONTH + " INTEGER NOT NULL " +
                ");";

        db.execSQL(SQL_CREATE_INCOME_TABLE);
        db.execSQL(SQL_CREATE_BILLS_TABLE);
        db.execSQL(SQL_CREATE_EXPENSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + IncomeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BillsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ExpensesEntry.TABLE_NAME);
        onCreate(db);
    }
}
