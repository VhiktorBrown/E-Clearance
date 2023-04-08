package com.theelitedevelopers.e_clearance.modules.payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.theelitedevelopers.e_clearance.R;
import com.theelitedevelopers.e_clearance.data.local.Constants;
import com.theelitedevelopers.e_clearance.data.local.SharedPref;
import com.theelitedevelopers.e_clearance.data.models.dto.PayStackAuthorizationDto;
import com.theelitedevelopers.e_clearance.data.models.dto.PayStackRequest;
import com.theelitedevelopers.e_clearance.data.remote.ServiceGenerator;
import com.theelitedevelopers.e_clearance.databinding.ActivityPayStackWebviewBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class PayStackWebViewActivity extends AppCompatActivity {
    ActivityPayStackWebviewBinding binding;
    PayStackAuthorizationDto payStackAuthorizationDto;
    String amount;
    String callBackUrl = "https://syticks.com/confirm";
    PayStackRequest payStackRequest = new PayStackRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayStackWebviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        amount = String.valueOf(getIntent().getDoubleExtra(Constants.CHECKOUT_AMOUNT, 0));
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.initializingText.setVisibility(View.VISIBLE);
        payStackRequest.setAmount(amount);
        payStackRequest.setEmail("partyhardentt@gmail.com");

        //make request
        getAuthorizationUrl(payStackRequest);
        initViews();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initViews(){
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().getAllowContentAccess();
        binding.webView.getSettings().setLoadWithOverviewMode(true);
        binding.webView.getSettings().setLoadWithOverviewMode(true);

        binding.reloadButton.setOnClickListener(v -> {
            binding.reloadButton.setVisibility(View.GONE);

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.initializingText.setVisibility(View.VISIBLE);

            //make request
            getAuthorizationUrl(payStackRequest);
        });

        binding.webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri url = request.getUrl();

                if(url.toString().startsWith(callBackUrl)){
                    goBackToCompletePayment();
                    return true;
                }else if(url.toString().equals("https://standard.paystack.co/close")) {
                    goBackToCompletePayment();
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request.getUrl().toString());
            }
        });

    }

    @SuppressLint("SetJavaScriptEnabled")
    public void loadUrl(PayStackAuthorizationDto payStackAuthorizationDto){
        binding.webView.loadUrl(payStackAuthorizationDto.getPayStackData().getAuthorizationUrl());
    }

    private void getAuthorizationUrl(PayStackRequest payStackRequest) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + Constants.PAY_STACK_Sk);

        Single<Response<PayStackAuthorizationDto>> fetchAuthorizationResponse = ServiceGenerator.getInstance()
                .getPayStackApi().fetchAuthorizationUrl(headers, payStackRequest);
        fetchAuthorizationResponse.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<PayStackAuthorizationDto>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Response<PayStackAuthorizationDto> fetchAuthorizationResponse) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.initializingText.setVisibility(View.GONE);

                        binding.reloadButton.setVisibility(View.GONE);

                        if(fetchAuthorizationResponse.isSuccessful() && fetchAuthorizationResponse.body() != null){
                            if(fetchAuthorizationResponse.body().getStatus()){
                                payStackAuthorizationDto = fetchAuthorizationResponse.body();
                                loadUrl(payStackAuthorizationDto);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.initializingText.setVisibility(View.GONE);

                        binding.reloadButton.setVisibility(View.VISIBLE);

                        Toast.makeText(getApplicationContext(), Constants.CHECK_CONN, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void goBackToCompletePayment(){
        Intent intent = new Intent();
        intent.putExtra(Constants.REFERENCE, payStackAuthorizationDto.getPayStackData().getReference());
        setResult(RESULT_OK, intent);
        finish();
    }
}