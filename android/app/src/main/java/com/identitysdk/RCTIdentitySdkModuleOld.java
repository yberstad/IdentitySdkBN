package com.identitysdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.UiThreadUtil;

import org.jetbrains.annotations.NotNull;

import io.signicat.identity_sdk.client.IdentitySDK;

public class RCTIdentitySdkModuleOld extends ReactContextBaseJavaModule {
    private static final String SUCCESS = "SUCCESS";
    private static final String CANCELED = "CANCELED";
    private static final String ERROR_START_AUTHORIZATION = "ERROR_START_AUTHORIZATION";
    private static final int VERIFICATION_REQUEST_CODE = 2000;
    //private final IdentitySDK identitySDK;
    private Promise mPromise;
    private Intent intent;

    @NotNull
    @Override
    public String getName() {
        return "IdentitySdkBridge";
    }

//    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
//
//        @Override
//        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
//            Log.d("ReactNative", "IdentitySDK.onActivityResult -> requestCode:" + requestCode + " resultCode:" + resultCode);
//            if (requestCode == VERIFICATION_REQUEST_CODE) {
//                switch(resultCode) {
//                    case Activity.RESULT_OK:
//                        mPromise.resolve(SUCCESS);
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        mPromise.reject(CANCELED, extractErrorMessage(intent));
//                        break;
//                    default:
//                        mPromise.reject(ERROR_START_AUTHORIZATION, extractErrorMessage(intent));
//                }
//            }
//        }
//    };

    public RCTIdentitySdkModuleOld(ReactApplicationContext reactContext) {
        super(reactContext);

        // Add the listener for `onActivityResult`
//        reactContext.addActivityEventListener(mActivityEventListener);
//        identitySDK = IdentitySDK.Companion.create();
    }

    @ReactMethod
    public void startAuthorization(final String languageCode, String proceedToken, final Promise promise) {
        try {
            mPromise = promise;
            ReactApplicationContext context = getReactApplicationContext();
            //intent = new Intent(context, IdentitySdkActivity.class);

            if (intent.resolveActivity(context.getPackageManager()) != null) {
                intent.setFlags((Intent.FLAG_ACTIVITY_NEW_TASK));
                UiThreadUtil.runOnUiThread(() -> context.startActivity(intent));
            }
        } catch (Exception e) {
            logError(e);
            promise.reject(ERROR_START_AUTHORIZATION, e.getMessage());
        }
    }


    private String extractErrorMessage(Intent intent) {
        if (intent != null) {
            String errorCode = intent.getStringExtra("ERROR_CODE");
            String errorMessage = intent.getStringExtra("ERROR_MESSAGE");
            return errorCode + " " + errorMessage;
        }
        return "UNKNOWN";
    }

    private void logError(Exception e) {
        Log.d("ReactNative", e.getMessage());
        for (StackTraceElement element : e.getStackTrace())
            Log.d("ReactNative", element.toString());
    }

    private AppCompatActivity getActivity() {
        return (AppCompatActivity)getReactApplicationContext().getCurrentActivity();
    }
}
