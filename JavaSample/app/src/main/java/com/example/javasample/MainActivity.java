package com.example.javasample;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WebView webView = findViewById(R.id.webView);

        final OnBackPressedCallback callback = new OnBackPressedCallback(webView.canGoBack()) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "handleOnBackPressed canGoBack= " + webView.canGoBack());
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        };

        // WebViewの初期設定
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
                Log.d(TAG, "doUpdateVisitedHistory canGoBack= " + webView.canGoBack());
                callback.setEnabled(webView.canGoBack());
            }
        });
        webView.loadUrl("https://www.yahoo.co.jp/");

        // メモリリークを防ぐには、LifecycleOwnerを指定できるaddCallbackを使用すると良い。
        // onDestroyのタイミングで自動的にCallbackの登録解除が行われる！
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: IN");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: IN");
    }
}