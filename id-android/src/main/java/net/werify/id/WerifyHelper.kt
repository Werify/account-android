package net.werify.id

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import net.werify.id.common.ConnectionClassManager
import net.werify.id.common.ConnectionClassManager.Companion.getInstance
import net.werify.id.common.ConnectionQuality
import net.werify.id.common.WConstants
import net.werify.id.interfaces.ConnectionQualityChangeListener
import net.werify.id.model.Request
import net.werify.id.model.Response
import net.werify.id.model.otp.OTPRequestResults
import net.werify.id.model.otp.OTPVerifyResults
import net.werify.id.model.qr.QrResult
import net.werify.id.retrofit.NetworkModule
import net.werify.id.retrofit.NetworkModule.initialize
import net.werify.id.retrofit.NetworkModule.initializeWithDefaultClient
import net.werify.id.retrofit.WerifyConfigure
import net.werify.id.utils.Utils.getCache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

typealias Object = () -> Unit

const val TAG = "WerifyHelper"

/**
 * WerifyNetworking entry point.
 * You must initialize this class before use. The simplest way is to just do
 * {#code WerifyNetworking.initialize(context , appKey)}.
 */
object WerifyHelper {


    @OptIn(DelicateCoroutinesApi::class)
    private val ioContext by lazy { newFixedThreadPoolContext(/*poolSize*/1, "IOContextWrapper") }
    fun <T> execute(dis: CoroutineDispatcher = Dispatchers.Main, r: Object) {
        CoroutineScope(dis).launch {
            flow<T> { r.invoke() }.catch {

            }.flowOn(ioContext).onCompletion {

            }.collect {

            }
        }
    }

    /**
     * Initializes WerifyNetworking with the default config.
     *
     * @param configure The Custom Configuration of WerifyNetworking
     * @param context   The context
     */
    fun initialize(context: Context, configure: WerifyConfigure?) {
        if (configure != null) {
            initialize(
                OkHttpClient().newBuilder()
                    .cache(getCache(context, WConstants.MAX_CACHE_SIZE, WConstants.CACHE_DIR_NAME))
                    .connectTimeout(configure.connectTimeout, TimeUnit.SECONDS)
                    .readTimeout(configure.readTimeout, TimeUnit.SECONDS)
                    .writeTimeout(configure.writeTimeout, TimeUnit.SECONDS)
                    .build(), configure.url
            )
        } else {
            initializeWithDefaultClient()
        }
        //        RequestQueue.initialize();
    }

    /**
     * Method to set connectionQualityChangeListener
     *
     * @param connectionChangeListener The connectionQualityChangeListener
     */
    fun setConnectionQualityChangeListener(connectionChangeListener: ConnectionQualityChangeListener) =
        getInstance().setListener(connectionChangeListener)


    /**
     * Method to set connectionQualityChangeListener
     */
    fun removeConnectionQualityChangeListener() {
        getInstance().removeListener()
    }

    /**
     * Method to enable logging with tag
     * @param level The level for logging
     */
    @JvmOverloads
    fun enableLogging(level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BASIC) =
        NetworkModule.enableLogging(level)


    /**
     * Method to get currentBandwidth
     *
     * @return currentBandwidth
     */
    val currentBandwidth: Int
        get() = getInstance().getCurrentBandwidth()

    /**
     * Method to get currentConnectionQuality
     *
     * @return currentConnectionQuality
     */
    val currentConnectionQuality: ConnectionQuality?
        get() = getInstance().getCurrentConnectionQuality()

    /**
     * Shuts WerifyNetworking down
     */
    fun shutDown() {
        //Core.shutDown();
        //evictAllBitmap();
        getInstance().removeListener()
        ConnectionClassManager.shutDown()
        //ParseUtil.shutDown();
    }



    //region  ( Doesn't need any credentials or authorization )
    fun login(request: Request,callback: RequestCallback<Any>){
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.login(request)
                .catch { callback.onError(it) }
                .onCompletion {
                    Log.e(TAG, "onCompletion  ${it?.printStackTrace()}")
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }
    fun loginOTP(request: Request,callback: RequestCallback<Any>) {
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.loginOTP(request)
                .catch { callback.onError(it) }
                .onCompletion {
                    Log.e(TAG, "onCompletion  ${it?.printStackTrace()}")
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }
    fun requestOTP(request: Request, callback: RequestCallback<OTPRequestResults>) {
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.requestOTP(request)
                .catch { callback.onError(it) }
                .onCompletion {
                    Log.e(TAG, "onCompletion  ${it?.printStackTrace()}")
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }
    fun verifyOTP(request: Request ,callback: RequestCallback<OTPVerifyResults>){
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.verifyOTP(request)
                .catch { callback.onError(it) }
                .onCompletion {
                    Log.e(TAG, "onCompletion  ${it?.printStackTrace()}")
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }
    fun getQRSession(callback: RequestCallback<QrResult>){
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.getQRSession()
                .catch { callback.onError(it) }
                .onCompletion {
                    Log.e(TAG, "onCompletion  ${it?.printStackTrace()}")
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }
    fun checkSession(hash: String, id: String,callback: RequestCallback<Any>){
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.checkSession(hash, id)
                .catch { callback.onError(it) }
                .onCompletion {
                    Log.e(TAG, "onCompletion  ${it?.printStackTrace()}")
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }
    //endregion

    //region  ( Needs token in request header )

    fun getUserProfile(callback: RequestCallback<Any>){
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.getUserProfile()
                .catch { callback.onError(it) }
                .onCompletion {
                    Log.e(TAG, "onCompletion  ${it?.printStackTrace()}")
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }
    fun getUserNumbers(callback: RequestCallback<Any>){
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.getUserNumbers()
                .catch { callback.onError(it) }
                .onCompletion {
                    Log.e(TAG, "onCompletion  ${it?.printStackTrace()}")
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }
    fun getFinancialInfo(callback: RequestCallback<Any>){
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.getFinancialInfo()
                .catch { callback.onError(it) }
                .onCompletion {
                    Log.e(TAG, "onCompletion  ${it?.printStackTrace()}")
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }
    fun getNewModalSession(callback: RequestCallback<Any>){
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.getNewModalSession()
                .catch { callback.onError(it) }
                .onCompletion {
                    Log.e(TAG, "onCompletion  ${it?.printStackTrace()}")
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }
    fun checkUsername(callback: RequestCallback<Any>){
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.checkUsername()
                .catch { callback.onError(it) }
                .onCompletion {
                    Log.e(TAG, "onCompletion  ${it?.printStackTrace()}")
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }

    fun claimModalSession(hash: String, id: String,callback: RequestCallback<Any>){
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.claimModalSession(hash, id)
                .catch { callback.onError(it) }
                .onCompletion {
                    Log.e(TAG, "onCompletion  ${it?.printStackTrace()}")
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }
    fun claimQRSession(hash: String, id: String,callback: RequestCallback<Any>){
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.claimQRSession(hash, id)
                .catch { callback.onError(it) }
                .onCompletion {
                    Log.e(TAG, "onCompletion  ${it?.printStackTrace()}")
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }
    fun updateUserProfile(request: Request,callback: RequestCallback<Any>){
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.updateUserProfile(request)
                .catch { callback.onError(it) }
                .onCompletion {
                    Log.e(TAG, "onCompletion  ${it?.printStackTrace()}")
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }
    fun addMobileNumber(request: Request,callback: RequestCallback<Any>){
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.addMobileNumber(request)
                .catch { callback.onError(it) }
                .onCompletion {
                    Log.e(TAG, "onCompletion  ${it?.printStackTrace()}")
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }


    //endregion
}