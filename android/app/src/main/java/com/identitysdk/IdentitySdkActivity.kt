package com.identitysdk

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.signicat.identity_sdk.client.IdentitySDK

class IdentitySdkActivity : AppCompatActivity() {
    private companion object {
        const val VERIFICATION_REQUEST_CODE = 2000
        const val LANGUAGE_CODE = "en"
    }
    // Initialize SDK
    private val identitySDK: IdentitySDK by lazy { IdentitySDK.create() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startAuthorization()
    }
    // Start SDK authorization
    private fun startAuthorization() {
        identitySDK
                .getAuthorizationManager()
                .startAuthorization(
                        this,
                        "NO",
                        "f7d8c981-5906-4290-baa9-78bb9371954d",
                        VERIFICATION_REQUEST_CODE
                )
    }
    // Authorization verification result will be returned in onActivityResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == VERIFICATION_REQUEST_CODE) {
            val informationText = when (resultCode) {
                Activity.RESULT_OK -> "SUCCESS"
                Activity.RESULT_CANCELED -> extractErrorMessage(data)
                else -> "UNKNOWN"
            }
        }
    }
    private fun extractErrorMessage(intent: Intent?): String {
        return intent?.let {
            val errorCode = it.getStringExtra(IdentitySDK.INTENT_RESULT_ERROR_CODE)
            val errorMsg = it.getStringExtra(IdentitySDK.INTENT_RESULT_ERROR_MESSAGE)
            return@let errorMsg
        } ?: "UNKNOWN"
    }
    override fun onDestroy() {
        super.onDestroy()
        identitySDK.cleanUp()
    }
}