package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import au.com.bytecode.opencsv.CSVReader;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.stockhawk.R.id.stock;
import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_ABSOLUTE_CHANGE;
import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_HISTORY;
import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_PERCENTAGE_CHANGE;
import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_PRICE;
import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_SYMBOL;
import static com.udacity.stockhawk.data.Contract.Quote._ID;


public class DetailActivity extends AppCompatActivity {

    /*
     * The columns of data that we are interested in displaying within our DetailActivity's
     * stock detail display.
     */
    public static final String[] QUOTE_DETAIL = {
            _ID,
            COLUMN_SYMBOL,
            COLUMN_PRICE,
            COLUMN_ABSOLUTE_CHANGE,
            COLUMN_PERCENTAGE_CHANGE,
            COLUMN_HISTORY
    };

    /*
     * Storing the indices of the values in the array of Strings above to more quickly be able
     * to access the data from our query. If the order of the Strings above changes, these
     * indices must be adjusted to match the order of the Strings.
     */
    public static final int INDEX_ID = 0;
    public static final int INDEX_STOCK_SYMBOL = 1;
    public static final int INDEX_STOCK_PRICE = 2;
    public static final int INDEX_STOCK_ABSOLUTE_CHANGE = 3;
    public static final int INDEX_STOCK_PERCENTAGE_CHANGE = 4;
    public static final int INDEX_STOCK_HISTORY = 5;
    public static final String EXTRA_SYMBOL = "extra:symbol";
    private static final int ID_DETAIL_LOADER = 221;
    private final DecimalFormat dollarFormatWithPlus;
    private final DecimalFormat dollarFormat;
    private final DecimalFormat percentageFormat;
    @BindView(stock)
    TextView mStockView;
    @BindView(R.id.company_name)
    TextView mCompanyView;
    @BindView(R.id.stock_price)
    TextView mPriceView;
    @BindView(R.id.absolute_change)
    TextView mAbsChangeView;
    @BindView(R.id.percent_change)
    TextView mPercentChangeView;
    @BindView(R.id.stock_chart)
    LineChart mStockChart;
    private Uri mUri;

    {
        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus.setPositivePrefix("+$");
        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mUri = getIntent().getData();
        String symbol = getIntent().getStringExtra(EXTRA_SYMBOL);

        if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");

        setQuoteDetail(mUri, symbol);
        getHistory(symbol);
    }

    private void getHistory(String symbol) {
        String history = getHistoryString(symbol);

        CSVReader reader = new CSVReader(new StringReader(history));

        List<Entry> entries = new ArrayList<>();
        final List<Long> xAxisValues = new ArrayList<>();
        String[] nextLine;
        int xAxisPosition = 0;
        try {
            while ((nextLine = reader.readNext()) != null) {
                xAxisValues.add(Long.valueOf(nextLine[0]));
                Entry entry = new Entry(
                        xAxisPosition, //timestamp
                        Float.valueOf(nextLine[1]) // symbol value
                );
                entries.add(entry);
                xAxisPosition++;
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        LineData lineData = new LineData(new LineDataSet(entries, "label"));
        mStockChart.setData(lineData);
        mStockChart.setContentDescription("Line Chart for " + symbol);

        XAxis xAxis = mStockChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter(){
            @Override
            public String getFormattedValue(float value, AxisBase axisBase) {
                Date date = new Date(xAxisValues.get(xAxisValues.size() - (int) value - 1));
                return new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                        .format(date);
            }
        });

    }

    private String getHistoryString(String symbol) {
        Cursor cursor = getContentResolver().query(Contract.Quote.makeUriForStock(symbol),
                null,
                null,
                null,
                null);
        String history = "";
        if (cursor.moveToFirst()){
            history = cursor.getString(cursor.getColumnIndex(Contract.Quote.COLUMN_HISTORY));
            cursor.close();
        }
        return history;
    }


    public void setQuoteDetail(Uri uri, String symbol){

        mStockView.setText(symbol);
//
//        mCompanyView.setText("Google");
//        mPriceView.setText(dollarFormat.format(971.47));
//
//        float rawAbsoluteChange = (float) 1.93;
//        float percentageChange = (float) 0.20;
//        if (rawAbsoluteChange > 0) {
//            mAbsChangeView.setBackgroundResource(R.drawable.percent_change_pill_green);
//            mPercentChangeView.setBackgroundResource(R.drawable.percent_change_pill_green);
//        } else {
//            mAbsChangeView.setBackgroundResource(R.drawable.percent_change_pill_red);
//            mPercentChangeView.setBackgroundResource(R.drawable.percent_change_pill_red);
//        }
//        String absChange = dollarFormatWithPlus.format(rawAbsoluteChange);
//        String perChange = percentageFormat.format(percentageChange);
//
//        mAbsChangeView.setText(absChange);
//        mPercentChangeView.setText(perChange);

//        LineData historicalRecords;
//        historicalRecords = MockUtils.getHistory();
//        mStockChart.getLineData();
//        mStockChart.setData(historicalRecords);
    }


}
