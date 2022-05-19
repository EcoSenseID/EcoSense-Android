package com.ecosense.android.featAuth.domain

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


class GoogleSignInContract : ActivityResultContract<Int?, GoogleSignInAccount?>() {

    override fun createIntent(context: Context, input: Int?): Intent {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("700975405784-turn28oap23hefqq9aij0j74e36skkus.apps.googleusercontent.com")
            .requestEmail()
            .build()

        return GoogleSignIn
            .getClient(context, signInOptions)
            .signInIntent
    }


    override fun parseResult(resultCode: Int, intent: Intent?): GoogleSignInAccount? {
        return when (resultCode) {
            Activity.RESULT_OK -> {
                try {
                    GoogleSignIn
                        .getSignedInAccountFromIntent(intent)
                        .getResult(ApiException::class.java)
                } catch (e: Exception) {
                    Log.d("TAG", "Unexpected error parsing sign-in result")
                    null
                }
            }
            else -> null
        }
    }
}