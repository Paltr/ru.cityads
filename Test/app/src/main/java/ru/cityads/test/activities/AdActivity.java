package ru.cityads.test.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.telephony.TelephonyManager;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.trello.rxlifecycle.components.RxActivity;

import ru.cityads.test.R;
import ru.cityads.test.services.AdResponse;
import ru.cityads.test.services.AdsService;

/**
 * Activity that shows ads.
 */
public final class AdActivity extends RxActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        setupWebView();
        requestAd();
    }

    //region Private behaviour methods

    private void finishDisplayAd()
      {
        this.finish();
      }

    //endregion

    //region Private ad request methods

    private void requestAd()
    {
        showProgress();
        final AdsService adsService = new AdsService();
        String deviceId = getDeviceID();
        if(deviceId == null)
        {
            deviceId = "000000000000000";
        }

        final Observable<AdResponse> adRequest = adsService.requestAd(deviceId);
        adRequest.compose(bindToLifecycle())
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::acceptAdResponse, this::handleAdRequestError);
    }

    @SuppressWarnings("UnusedParameters")
    private void handleAdRequestError(Throwable e)
    {
      showErrorMessage(R.string.error_dialog_unknown_error_message);
    }

    private void acceptAdResponse(AdResponse adResponse)
    {
      if(adResponse.checkStatus())
      {
        loadUrl(adResponse.getUrl());
      }
      else
      {
        showErrorMessage(adResponse.getMessage());
      }
    }

    //endregion

    //region Private view helpers

    private void setupWebView()
    {
        getWebView().setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url)
          {
            AdActivity.this.showWebView();
          }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
            {
                AdActivity.this.showErrorMessage(R.string.error_dialog_web_load_fail_message);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                final Uri uri = Uri.parse(url);
                final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                finishDisplayAd();
                return true;
            }
        });
    }

    private void showProgress()
    {
        getProgressBar().setVisibility(View.VISIBLE);
        getWebView().setVisibility(View.GONE);
    }

    private void showErrorMessage(@StringRes int errorMessageId)
    {
        final String message = getResources().getString(errorMessageId);
        showErrorMessage(message);
    }

    private void showErrorMessage(String errorMessage)
    {
        final AlertDialog.Builder dialog  = new AlertDialog.Builder(this);
        dialog.setMessage(errorMessage);
        dialog.setTitle(R.string.error_dialog_title);
        dialog.setPositiveButton(R.string.error_dialog_button_text, (a, b) -> finishDisplayAd());
        dialog.create();
        dialog.show();
    }

    private void loadUrl(String url)
  {
    getWebView().loadUrl(url);
  }

    private void showWebView()
    {
        getProgressBar().setVisibility(View.GONE);
        getWebView().setVisibility(View.VISIBLE);
    }

    //endregion

    //region Private view getters

    private ProgressBar getProgressBar()
  {
    return (ProgressBar)findViewById(R.id.progressBar);
  }

    private WebView getWebView()
  {
    return (WebView)findViewById(R.id.webView);
  }

    //endregion

    //region Private device id getter method

    private String getDeviceID()
    {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        if(telephonyManager == null)
        {
            return null;
        }

        return telephonyManager.getDeviceId();
    }

    //endregion
}
