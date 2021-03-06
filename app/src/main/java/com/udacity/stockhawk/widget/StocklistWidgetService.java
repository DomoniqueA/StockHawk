package com.udacity.stockhawk.widget;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StocklistWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockWidgetViewFactory(getApplicationContext());
    }

    private class StockWidgetViewFactory implements RemoteViewsFactory {

        public static final String SET_BACKGROUND_RESOURCE = "setBackgroundResource";
        private final Context mApplicationContext;
        private final DecimalFormat dollarFormatWithPlus;
        private final DecimalFormat dollarFormat;
        private final DecimalFormat percentageFormat;
        private List<ContentValues> mCvList = new ArrayList<>();

        public StockWidgetViewFactory(Context applicationContext) {
            mApplicationContext = applicationContext;

                dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
                dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
                dollarFormatWithPlus.setPositivePrefix("+$");
                percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
                percentageFormat.setMaximumFractionDigits(2);
                percentageFormat.setMinimumFractionDigits(2);
                percentageFormat.setPositivePrefix("+");


        }

        @Override
        public void onCreate() {
            ContentResolver contentResolver = mApplicationContext.getContentResolver();

            mCvList.clear();

            Cursor cursor = contentResolver.query(
                    Contract.Quote.URI,
                    null,
                    null,
                    null,
                    null
            );
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String symbol = cursor.getString(cursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL));
                    float price = cursor.getFloat(cursor.getColumnIndex(Contract.Quote.COLUMN_PRICE));
                    float absChange = cursor.getFloat(cursor.getColumnIndex(Contract.Quote.COLUMN_ABSOLUTE_CHANGE));
                    float percentageChange = cursor.getFloat(cursor.getColumnIndex(Contract.Quote.COLUMN_PERCENTAGE_CHANGE));


                    ContentValues cv = new ContentValues();

                    cv.put(Contract.Quote.COLUMN_SYMBOL, symbol);
                    cv.put(Contract.Quote.COLUMN_PRICE, price);
                    cv.put(Contract.Quote.COLUMN_ABSOLUTE_CHANGE, absChange);
                    cv.put(Contract.Quote.COLUMN_PERCENTAGE_CHANGE, percentageChange);

                    mCvList.add(cv);

                }

                cursor.close();
            }
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mCvList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            ContentValues cv = mCvList.get(position);
            RemoteViews remoteViews = new RemoteViews(
                    mApplicationContext.getPackageName(),
                    R.layout.list_item_quote);

            remoteViews.setTextViewText(R.id.symbol, cv.getAsString(Contract.Quote.COLUMN_SYMBOL));
            remoteViews.setTextViewText(R.id.price, dollarFormat.format(cv.getAsFloat(Contract.Quote.COLUMN_PRICE)));

            float absChange = cv.getAsFloat(Contract.Quote.COLUMN_ABSOLUTE_CHANGE);
            float perChange = cv.getAsFloat(Contract.Quote.COLUMN_PERCENTAGE_CHANGE);

            if (absChange>0){
                remoteViews.setInt(
                        R.id.change,
                        SET_BACKGROUND_RESOURCE,
                        R.drawable.percent_change_pill_green);
            } else {
                remoteViews.setInt(
                        R.id.change,
                        SET_BACKGROUND_RESOURCE,
                        R.drawable.percent_change_pill_red);
            }

            remoteViews.setTextViewText(R.id.change, percentageFormat.format(perChange / 100));

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }

}