package net.werify.id.sample

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import net.werify.id.RequestCallback
import net.werify.id.WerifyHelper
import net.werify.id.model.otp.OTPRequestResults
import net.werify.id.model.otp.OTPVerifyResults
import net.werify.id.model.otp.RequestLoginOTP
import net.werify.id.model.otp.RequestOTP
import net.werify.id.model.otp.VerifyOTP
import net.werify.id.model.otp.toVerifyObject
import net.werify.id.model.qr.QrResult
import net.werify.id.model.user.FinancialResult
import net.werify.id.model.user.Profile
import net.werify.id.model.user.UserInfo
import net.werify.id.model.user.UserNumberRequest
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
            binding.content.progressBar.visibility = View.VISIBLE
            binding.content.apiResult.text = ""
            requestOTP(RequestOTP("mamad@mamad.com"))
        }
    }

    private fun checkSession(hash: String, id: String) {
        WerifyHelper.checkSession(hash, id,
            object : RequestCallback<Any> {
                override fun onError(throwable: Throwable) {
                        print("4- checkSession: ${throwable.message}\n")
                }
                override fun onSuccess(result: Any) {
                    print("4- checkSession: onSuccess\n")
                }
            })

        WerifyHelper.claimModalSession(hash, id,
            object : RequestCallback<Any> {
                override fun onError(throwable: Throwable) {
                    print("7- claimModalSession: ${throwable.message}\n")
                }
                override fun onSuccess(result: Any) {
                    print("7- claimModalSession: onSuccess\n")
                }
            })

        WerifyHelper.claimQRSession(hash, id,
            object : RequestCallback<String> {
                override fun onError(throwable: Throwable) {
                    print("8- claimQRSession: ${throwable.message}\n")
                }
                override fun onSuccess(result: String) {
                    print("8- claimQRSession: onSuccess $result\n")
                    binding.content.progressBar.visibility = View.GONE
                }
            })

        val profile = Profile("Ali","ahmadi","Ahmadi")
       try {
           WerifyHelper.updateFinancialInfo(profile,
               object : RequestCallback<Any> {
                   override fun onError(throwable: Throwable) {
                       print("14- updateFinancialInfo: ${throwable.message}\n")
                   }
                   override fun onSuccess(result: Any) {
                       print("14- updateFinancialInfo: onSuccess $result\n")
                       binding.content.progressBar.visibility = View.GONE
                   }
               })
       }finally {
           WerifyHelper.updateUserProfile(profile,
               object : RequestCallback<Any> {
                   override fun onError(throwable: Throwable) {
                       print("15- updateUserProfile: ${throwable.message}\n")
                   }
                   override fun onSuccess(result: Any) {
                       print("15- updateUserProfile: onSuccess $result\n")
                       binding.content.progressBar.visibility = View.GONE
                   }
               })
       }

        WerifyHelper.addMobileNumber(
            UserNumberRequest("0912456789"),
            object : RequestCallback<Any> {
                override fun onError(throwable: Throwable) {
                    print("15- addMobileNumber: ${throwable.message}\n")
                }
                override fun onSuccess(result: Any) {
                    print("15- addMobileNumber: onSuccess $result\n")
                    binding.content.progressBar.visibility = View.GONE
                }
            })

    }
    private fun getQRSession() {

        WerifyHelper.getUserProfile(
            object : RequestCallback<UserInfo> {
                override fun onError(throwable: Throwable) {
                    print("9- getUserProfile: ${throwable.message}\n")
                }

                override fun onSuccess(result: UserInfo) {
                    print("9- getUserProfile: onSuccess\n")
                }
            })

        WerifyHelper.getUserNumbers(
            object : RequestCallback<FinancialResult> {
                override fun onError(throwable: Throwable) {
                    print("10- getUserNumbers: ${throwable.message}\n")
                }

                override fun onSuccess(result: FinancialResult) {
                    print("10- getUserNumbers: onSuccess\n")
                }
            })

        WerifyHelper.getFinancialInfo(
            object : RequestCallback<FinancialResult> {
                override fun onError(throwable: Throwable) {
                    print("11- getFinancialInfo: ${throwable.message}\n")
                }

                override fun onSuccess(result: FinancialResult) {
                    print("11- getFinancialInfo: onSuccess\n")
                }
            })

        WerifyHelper.getNewModalSession(
            object : RequestCallback<FinancialResult> {
                override fun onError(throwable: Throwable) {
                    print("12- getNewModalSession: ${throwable.message}\n")
                }

                override fun onSuccess(result: FinancialResult) {
                    print("12- getNewModalSession: onSuccess\n")
                }
            })

        WerifyHelper.checkUsername(
            object : RequestCallback<Any> {
                override fun onError(throwable: Throwable) {
                    print("13- checkUsername: ${throwable.message}\n")
                }

                override fun onSuccess(result: Any) {
                    print("13- checkUsername: onSuccess\n")
                }
            })
        WerifyHelper.getQRSession(
            object : RequestCallback<QrResult> {
                override fun onError(throwable: Throwable) {
                    print("3- getQRSession: ${throwable.message}\n")
                }

                override fun onSuccess(result: QrResult) {
                    print("3- getQRSession: onSuccess\n")
                    /*  Glide.with(this@MainActivity)
                          .asBitmap()
                          .load(result.url)
                          .into(binding.content.userQr)*/
                    checkSession(result.hash, result.id)
                }
            })
    }
    private fun verifyOTP(r: VerifyOTP) {
        WerifyHelper.verifyOTP(r,
            object : RequestCallback<OTPVerifyResults> {
                override fun onError(throwable: Throwable) {
                    print("2- verifyOTP: ${throwable.message}\n")
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
                    print("1- requestOTP: ${throwable.message}\n")
                }
                override fun onSuccess(result: OTPRequestResults) {
                    print("1- requestOTP: onSuccess\n")
                    verifyOTP(result.toVerifyObject())
                }
            })

        WerifyHelper.login(r,
            object : RequestCallback<Any> {
                override fun onError(throwable: Throwable) {
                    print("5-login: ${throwable.message}\n")
                }
                override fun onSuccess(result: Any) {
                    print("5- login: onSuccess\n")
                }
            })

        WerifyHelper.loginOTP(
            RequestLoginOTP("hash","opt","id"),
            object : RequestCallback<Any> {
                override fun onError(throwable: Throwable) {
                    print("6-loginOTP: ${throwable.message}\n")
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