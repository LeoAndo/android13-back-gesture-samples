# android13-back-gesture-samples
Sample for Back Gesture (Android 13+)

# 開発メモ

OS13で、戻るジェスチャーを使いたい場合は開発者オプションの設定が必要。<br>
詳しくは、[ここ](https://developer.android.com/guide/navigation/predictive-back-gesture#dev-option)参照する。<br>
それより古いOSは戻るジェスチャーは使えない。Jetpack API側で従来のキーイベント処理を行ってくれる。<br>

<img width="739" alt="スクリーンショット 2022-11-26 20 43 04" src="https://user-images.githubusercontent.com/16476224/204086962-35b7529b-363b-487c-8009-734787c80961.png">

LifecycleOwnerを引数に取る[OnBackPressedDispatcher#addCallback](https://developer.android.com/reference/androidx/activity/OnBackPressedDispatcher#addCallback(androidx.lifecycle.LifecycleOwner,androidx.activity.OnBackPressedCallback))を使用すれば、画面のライフサイクルの終了タイミングで解放処理を行ってくれるのでメモリリークを防げる。

https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/OnBackPressedDispatcher.java;l=313?q=OnBackPressedDispatcher&ss=androidx%2Fplatform%2Fframeworks%2Fsupport&hl=ja
```java
       @Override
        public void cancel() {
            mLifecycle.removeObserver(this);
            mOnBackPressedCallback.removeCancellable(this);
            if (mCurrentCancellable != null) {
                mCurrentCancellable.cancel();
                mCurrentCancellable = null;
            }
        }
```


Jetpack Composeの場合は、`BackHandler`を使う。こちらに関しては、`com.google.accompanist.web.WebView`を使用していれば内部で`BackHandler`を使っているので、開発者が対応する必要はない。
https://github.com/google/accompanist/blob/7a293b358c293d398a9b95185b41ef3cc1136475/web/src/main/java/com/google/accompanist/web/WebView.kt#L84:L86
```kotlin
    BackHandler(captureBackPresses && navigator.canGoBack) {
        webView?.goBack()
    }
```

`com.google.accompanist.web.WebView`の使い方は以下のサンプルコードを参考にする。<br>
https://github.com/google/accompanist/blob/a0ebf63b2bb5d681fd5c2c6bb6c2e97022b1b296/sample/src/main/java/com/google/accompanist/sample/webview/BasicWebViewSample.kt

# Capture OS: 13

| バックキー | 戻るジェスチャー |
|:---|:---:|
|<img src="https://github.com/LeoAndo/android13-back-gesture-samples/blob/main/JavaSample/capture/android_API33_key_back.gif" width=320 /> |<img src="https://github.com/LeoAndo/android13-back-gesture-samples/blob/main/JavaSample/capture/android_API33_gesture_back.gif" width=320 /> |

# Capture OS: 8

| バックキー | 戻るジェスチャー |
|:---|:---:|
|<img src="https://github.com/LeoAndo/android13-back-gesture-samples/blob/main/JavaSample/capture/android_API26_key_back.gif" width=320 /> | 利用不可能 |





# refs
https://github.com/LeoAndo/development-conference-memo/issues/161<br>
https://github.com/LeoAndo/development-conference-memo/issues/81<br>
https://developer.android.com/guide/navigation/predictive-back-gesture?hl=ja<br>
https://codelabs.developers.google.com/handling-gesture-back-navigation?hl=ja#0<br>
[Jetpack ComposeのBackHandlerを使ったサンプル](https://github.com/raheemadamboev/predictive-back-gesture)<br>
