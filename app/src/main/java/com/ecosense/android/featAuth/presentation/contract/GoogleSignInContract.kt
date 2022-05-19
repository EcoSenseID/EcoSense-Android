package com.ecosense.android.featAuth.presentation.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import com.ecosense.android.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

object GoogleSignInContract : ActivityResultContract<Int?, String?>() {

    override fun createIntent(context: Context, input: Int?): Intent {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_id))
            .requestProfile()
            .requestEmail()
            .build()

        return GoogleSignIn
            .getClient(context, signInOptions)
            .signInIntent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        return when (resultCode) {
            Activity.RESULT_OK -> {
                try {
                    GoogleSignIn
                        .getSignedInAccountFromIntent(intent)
                        .getResult(ApiException::class.java)
                        .idToken
                } catch (e: Exception) {
                    Log.d("GoogleSignInContract", "Unexpected error parsing sign-in result")
                    null
                }
            }
            else -> null
        }
    }
}