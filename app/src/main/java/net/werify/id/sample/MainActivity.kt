package net.werify.id.sample

import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import net.werify.id.RequestCallback
import net.werify.id.TAG
import net.werify.id.WerifyHelper
import net.werify.id.model.otp.OTPRequestResults
import net.werify.id.model.otp.OTPVerifyResults
import net.werify.id.model.otp.RequestOTP
import net.werify.id.model.otp.VerifyOTP
import net.werify.id.model.otp.toVerifyObject
import net.werify.id.model.qr.QrResult
import net.werify.id.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private fun print(msg: String) = runOnUiThread { binding.content.apiResult.append(msg) }


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.content.apiResult.movementMethod = ScrollingMovementMethod()

        binding.fab.setOnClickListener {
            requestOTP(RequestOTP("mamad@mamad.com"))
        }
    }

    private fun checkSession(hash: String, id: String) {
        WerifyHelper.checkSession(hash, id,
            object : RequestCallback<Any> {
                override fun onError(throwable: Throwable) {
                        print("4- checkSession: $throwable\n")
                }
                override fun onSuccess(result: Any) {
                    print("4- checkSession: onSuccess\n")
                }
            })
    }
    private fun getQRSession() {
        WerifyHelper.getQRSession(
            object : RequestCallback<QrResult> {
                override fun onError(throwable: Throwable) {
                    print("3- getQRSession: $throwable\n")
                }

                override fun onSuccess(result: QrResult) {
                    print("3- getQRSession: onSuccess\n")

                    Glide.with(this@MainActivity)
                        .asBitmap()
                        .load(result.url)
                        .into(binding.content.userQr)
                    checkSession(result.hash, result.id)
                }
            })
    }
    private fun verifyOTP(r: VerifyOTP) {
        WerifyHelper.verifyOTP(r,
            object : RequestCallback<OTPVerifyResults> {
                override fun onError(throwable: Throwable) {
                    print("2- verifyOTP: $throwable\n")
                }
                override fun onSuccess(result: OTPVerifyResults) {
                    print("2- verifyOTP: onSuccess\n")
                    getQRSession()
                }
            })
    }
    private fun requestOTP(r: RequestOTP) {
        WerifyHelper.requestOTP(r,
            object : RequestCallback<OTPRequestResults> {
                override fun onError(throwable: Throwable) {
                    print("1- requestOTP: $throwable\n")
                }
                override fun onSuccess(result: OTPRequestResults) {
                    print("1- requestOTP: onSuccess\n")
                    verifyOTP(result.toVerifyObject())
                }
            })

        WerifyHelper.login(r,
            object : RequestCallback<Any> {
                override fun onError(throwable: Throwable) {
                    print("5-login: $throwable\n")
                }
                override fun onSuccess(result: Any) {
                    print("5- login: onSuccess\n")
                }
            })

        WerifyHelper.loginOTP(r,
            object : RequestCallback<Any> {
                override fun onError(throwable: Throwable) {
                    print("6-loginOTP: $throwable\n")
                }
                override fun onSuccess(result: Any) {
                    print("6- loginOTP: onSuccess\n")
                }
            })
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}