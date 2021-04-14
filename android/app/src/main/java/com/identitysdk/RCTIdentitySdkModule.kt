package com.identitysdk

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.facebook.react.bridge.*
import io.signicat.identity_sdk.client.IdentitySDK

class RCTIdentitySdkModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), ActivityEventListener {
    companion object {
        private const val SUCCESS = "SUCCESS"
        private const val ERROR_START_AUTHORIZATION = "ERROR_START_AUTHORIZATION"
        private const val CANCELED = "CANCELED"
        private const val VERIFICATION_REQUEST_CODE = 2000
    }

    init {
        reactContext.addActivityEventListener(this)
    }

    private val identitySDK: IdentitySDK by lazy { IdentitySDK.create() }
    private var currentPromise: Promise? = null

    override fun getName() = "IdentitySdkBridge"

    override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, resultData: Intent?) {
        Log.d("ReactNative", "IdentitySDK.onActivityResult -> requestCode:$requestCode resultCode:$resultCode");
        when (requestCode) {
            VERIFICATION_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        currentPromise?.resolve(SUCCESS)
                    }
                    Activity.RESULT_CANCELED -> {
                        currentPromise?.reject(CANCELED, extractErrorMessage(resultData))
                    }
                    else -> {
                        currentPromise?.reject(ERROR_START_AUTHORIZATION, extractErrorMessage(resultData))
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
    }

    @ReactMethod
    fun startAuthorization(languageCode: String, proceedToken: String, promise: Promise) {
        currentPromise = promise;
        identitySDK
                .getAuthorizationManager()
                .startAuthorization(
                        reactApplicationContext.currentActivity!!,
                        languageCode,
                        proceedToken,
                        VERIFICATION_REQUEST_CODE
                )
    }

    private fun extractErrorMessage(intent: Intent?): String? {
        if (intent != null) {
            val errorCode = intent.getStringExtra("ERROR_CODE")
            val errorMessage = intent.getStringExtra("ERROR_MESSAGE")
            return "$errorCode $errorMessage"
        }
        return "UNKNOWN"
    }

    private fun logError(e: Exception) {
        Log.d("ReactNative", e.message)
        for (element in e.stackTrace) Log.d("ReactNative", element.toString())
    }
}