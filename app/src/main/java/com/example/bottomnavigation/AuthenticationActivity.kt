package com.example.bottomnavigation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.mobile.client.*
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.api.aws.AuthModeStrategyType
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.AWSDataStorePlugin


class AuthenticationActivity : AppCompatActivity() {
    private val TAG = AuthenticationActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        configureAmplify()

        AWSMobileClient.getInstance().initialize(applicationContext, object :
            Callback<UserStateDetails> {
                override fun onResult(userStateDetails: UserStateDetails) {
                    Log.i(TAG, userStateDetails.userState.toString())

                    when (userStateDetails.userState) {
                        UserState.SIGNED_IN -> {
                            Amplify.DataStore.clear(
                                {
                                    Log.i("AmplifyApp", "DataStore has been cleared")
                                }
                            ) {
                                Log.e("AmplifyApp", "Failure clearing datastore")
                            }
                            val i = Intent(this@AuthenticationActivity, MainActivity::class.java)
                            startActivity(i)
                        }
                        UserState.SIGNED_OUT -> showSignIn()
                        else -> {
                            Amplify.DataStore.clear(
                                {
                                    Log.i("AmplifyApp", "DataStore has been cleared")
                                }
                            ) {
                                Log.e("AmplifyApp", "Failure clearing datastore")
                            }
                            AWSMobileClient.getInstance().signOut()
                            showSignIn()
                        }
                    }
                }

                override fun onError(e: Exception) {
                    Log.e(TAG, e.toString())
                }
            })
    }

    private fun showSignIn() {
        Amplify.DataStore.clear(
            {
                Log.i("AmplifyApp", "DataStore has been cleared")
            }
        ) {
            Log.e("AmplifyApp", "Failure clearing datastore")
        }
        try {
            AWSMobileClient.getInstance().showSignIn(this, SignInUIOptions.builder().nextActivity(MainActivity::class.java).build())
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    private fun configureAmplify() {
        try {
            Amplify.addPlugin(AWSApiPlugin())
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            val dataStorePlugin = AWSDataStorePlugin.builder()
                .authModeStrategy(AuthModeStrategyType.MULTIAUTH)
                .build()
            Amplify.addPlugin(dataStorePlugin)
            Amplify.configure(applicationContext)
            Log.i("amplify-app", "Initialized amplify")


        } catch (error: AmplifyException) {
            Log.e("amplify-app", "Could not init amplify", error)
        }
    }
}