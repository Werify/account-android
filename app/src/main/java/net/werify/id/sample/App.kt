package net.werify.id.sample

import android.app.Application
import net.werify.id.WerifyNetworking
import net.werify.id.retrofit.WerifyConfigure
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        WerifyNetworking.initialize(this , WerifyConfigure
            .Builder("APP KEY")
            .connectTimeout(60 , TimeUnit.SECONDS)
            .readTimeout(60 , TimeUnit.SECONDS)
            .writeTimeout(60 , TimeUnit.SECONDS)
            //.setUrl("BASE URL FOR WERIFY ID")
            //.setOKHttpClient(YOU CAN SET NEW INSTANCE OF OK HTTP CLIENT)
            .build())

        WerifyNetworking.enableLogging()
    }
}