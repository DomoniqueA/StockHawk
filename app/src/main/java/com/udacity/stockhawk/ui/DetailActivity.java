package com.udacity.stockhawk.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.udacity.stockhawk.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_ABSOLUTE_CHANGE;
import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_HISTORY;
import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_PERCENTAGE_CHANGE;
import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_PRICE;
import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_SYMBOL;
import static com.udacity.stockhawk.data.Contract.Quote._ID;
import static com.udacity.stockhawk.data.Contract.Quote.getStockFromUri;


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

    private static final int ID_DETAIL_LOADER = 221;

    private Uri mUri;

    @BindView(R.id.stock)
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

    private final DecimalFormat dollarFormatWithPlus;
    private final DecimalFormat dollarFormat;
    private final DecimalFormat percentageFormat;

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

        if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");

        setQuoteDetail(mUri);
    }
    // TODO RETRIEVE STOCK INFO
    public void setQuoteDetail(Uri uri){
        String stock = getStockFromUri(uri);
        //mStockView.setText(stock);
        mStockView.setText("GOOG");
        mCompanyView.setText("Google");
        mPriceView.setText(dollarFormat.format(971.47));

        float rawAbsoluteChange = (float) 1.93;
        float percentageChange = (float) 0.20;
        if (rawAbsoluteChange > 0) {
            mAbsChangeView.setBackgroundResource(R.drawable.percent_change_pill_green);
            mPercentChangeView.setBackgroundResource(R.drawable.percent_change_pill_green);
        } else {
            mAbsChangeView.setBackgroundResource(R.drawable.percent_change_pill_red);
            mPercentChangeView.setBackgroundResource(R.drawable.percent_change_pill_red);
        }
        String absChange = dollarFormatWithPlus.format(rawAbsoluteChange);
        String perChange = percentageFormat.format(percentageChange);

        mAbsChangeView.setText(absChange);
        mPercentChangeView.setText(perChange);

//        LineData historicalRecords;
//        historicalRecords = MockUtils.getHistory();
//        mStockChart.getLineData();
//        mStockChart.setData(historicalRecords);
    }


}
