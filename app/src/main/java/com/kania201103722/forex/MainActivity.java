package com.kania201103722.forex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private ProgressBar loadingProgressBar;
    private SwipeRefreshLayout swipeRefreshLayout1;
    private TextView bamTextView, bbdTextView, bdtTextView, bgnTextView, bhdTextView, bifTextView, bmdTextView, bndTTextView, idrTextView, usdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout1 = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout1);
        bamTextView = (TextView) findViewById(R.id.bamTextView);
        bbdTextView = (TextView) findViewById(R.id.bbdTextView);
        bdtTextView = (TextView) findViewById(R.id.bdtTextView);
        bgnTextView = (TextView) findViewById(R.id.bgnTextView);
       bhdTextView = (TextView) findViewById(R.id.bhdTextView);
        bifTextView = (TextView) findViewById(R.id.bifTextView);
       bmdTextView = (TextView) findViewById(R.id.bmdTextView);
        bndTTextView = (TextView) findViewById(R.id.bndTextView);
        idrTextView = (TextView) findViewById(R.id.idrTextView);
        usdTextView = (TextView) findViewById(R.id.usdTextView);
        loadingProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

        initSwipeRefreshLayout();
        initForex();
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initForex();

                swipeRefreshLayout1.setRefreshing(false);
            }
        });
    }

    public String formatNumber(double number, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(number);
    }

    private void initForex() {
        loadingProgressBar.setVisibility(TextView.VISIBLE);

        String url = "https://openexchangerates.org/api/latest.json?app_id=9e281b166fd24f91a7d7a3a898ed89c8";

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                RootModel rootModel = gson.fromJson(new String(responseBody), RootModel.class);
                RatesModel ratesModel = rootModel.getRatesModel();

                double bam  = ratesModel.getIDR()/ ratesModel.getBAM();
                double bbd = ratesModel.getIDR() / ratesModel.getBBD();
                double bdt = ratesModel.getIDR() / ratesModel.getBDT();
                double bgn = ratesModel.getIDR() / ratesModel.getBGN();
                double bhd = ratesModel.getIDR() / ratesModel.getBHD();
                double bif= ratesModel.getIDR() / ratesModel.getBIF();
                double bmd = ratesModel.getIDR() / ratesModel.getBMD();
                double  bnd = ratesModel.getIDR() / ratesModel.getBND();
                double idr = ratesModel.getIDR() / ratesModel.getIDR();
                double usd = ratesModel.getIDR() / ratesModel.getUSD();

                bamTextView.setText(formatNumber(bam, "###,##0.##"));
               bbdTextView.setText(formatNumber(bbd, "###,##0.##"));
                bdtTextView.setText(formatNumber(bdt, "###,##0.##"));
                bgnTextView.setText(formatNumber(bgn, "###,##0.##"));
                bhdTextView.setText(formatNumber(bhd, "###,##0.##"));
                bifTextView.setText(formatNumber(bif, "###,##0.##"));
                bmdTextView.setText(formatNumber(bmd, "###,##0.##"));
                bndTTextView.setText(formatNumber(bnd, "###,##0.##"));
               idrTextView.setText(formatNumber(idr, "###,##0.##"));
               usdTextView.setText(formatNumber(usd, "###,##0.##"));

                loadingProgressBar.setVisibility(TextView.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                loadingProgressBar.setVisibility(TextView.INVISIBLE);
            }

        });
    }
}