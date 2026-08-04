package com.example.mynotesv2

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.configuration.AmplifyOutputs
import com.amplifyframework.kotlin.core.Amplify
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NoteApplication: Application(){

    override fun onCreate() {
        super.onCreate()

        try{
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSApiPlugin())

           Amplify.configure(AmplifyOutputs(R.raw.amplify_outputs), applicationContext)

            Log.i("AmplifySetup", "Initialized Amplify successfully")
        }catch (error: AmplifyException){
            Log.e("AmplifySetup", "Could not initialize Amplify", error)
        }

    }
}