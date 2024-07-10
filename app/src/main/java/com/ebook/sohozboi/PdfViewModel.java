package com.ebook.sohozboi;

import android.webkit.WebView;
import androidx.lifecycle.ViewModel;

public class PdfViewModel extends ViewModel {
    public WebView webView;

    @Override
    protected void onCleared() {
        super.onCleared();
        webView = null; // Clean up webView
    }
}
