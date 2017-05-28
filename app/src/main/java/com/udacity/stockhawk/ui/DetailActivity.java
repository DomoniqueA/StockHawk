package com.udacity.stockhawk.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_PRICE;


public class DetailActivity extends AppCompatActivity {

    /*
     * The columns of data that we are interested in displaying within our DetailActivity's
     * stock detail display.
     */
    public static final String[] QUOTE_DETAIL = {
            Contract.Quote._ID,
            Contract.Quote.COLUMN_SYMBOL,
            COLUMN_PRICE,
            Contract.Quote.COLUMN_ABSOLUTE_CHANGE,
            Contract.Quote.COLUMN_PERCENTAGE_CHANGE,
            Contract.Quote.COLUMN_HISTORY
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
    @BindView(R.id.stock_history)
    TextView mHistoryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mUri = getIntent().getData();

        if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");

    }


}
