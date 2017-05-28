package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.stockhawk.R;

public class GraphFragment extends Fragment {

    public GraphFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Graph Fragment Inflater
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

//        WebView graphWebView = (WebView) view.findViewById(R.id.graphWebView);
// TODO Change goog to retrieve symbol passed to detail.
        String companySymbol = "goog";
        String graphUrl = "http://empyrean-aurora-455.appspot.com/charts.php?symbol=" + companySymbol;

//        WebSettings webSettings = graphWebView.getSettings();
        // Enable JavaScript to show Stock Graph
//        webSettings.setJavaScriptEnabled(true);

//        graphWebView.loadUrl(graphUrl);

        return view;
    }
}
