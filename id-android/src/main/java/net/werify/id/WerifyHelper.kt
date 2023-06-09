package net.werify.id

import android.content.Context
import android.util.Log
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import net.werify.id.model.Request
import net.werify.id.model.otp.RequestLoginOTP
import net.werify.id.model.otp.OTPRequestResults
import net.werify.id.model.otp.OTPVerifyResults
import net.werify.id.model.qr.QrResult
import net.werify.id.model.user.FinancialResult
import net.werify.id.model.user.Profile
import net.werify.id.model.user.UserInfo
import net.werify.id.retrofit.NetworkModule
import net.werify.id.retrofit.NetworkModule.initialize
import net.werify.id.retrofit.NetworkModule.initializeWithDefaultClient
import net.werify.id.retrofit.WerifyConfigure
import okhttp3.OkHttpClient
import java.lang.ref.WeakReference
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
    private lateinit var cameraSourceRef: WeakReference<CameraSource>
    private lateinit var barcodeDetector: BarcodeDetector
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
                context,
                OkHttpClient().newBuilder()
                    .connectTimeout(configure.connectTimeout, TimeUnit.SECONDS)
                    .readTimeout(configure.readTimeout, TimeUnit.SECONDS)
                    .writeTimeout(configure.writeTimeout, TimeUnit.SECONDS), configure.url
            )
        } else {
            initializeWithDefaultClient(context)
        }

        val barcodeDetector =
            BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Log.e(TAG, "release")
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == 1) {
                    val scannedValue = barcodes.valueAt(0).rawValue
                    Log.e(TAG, "receiveDetections $scannedValue")
                } else {
                    Log.e(TAG, "receiveDetections $barcodes")
                }
            }
        })
    }


    /**
     * Shuts WerifyNetworking down
     */
    fun shutDown() {
        if (::barcodeDetector.isInitialized) {
            barcodeDetector.release()
        }
        if (::cameraSourceRef.isInitialized) {
            cameraSourceRef.get()?.release()
        }
    }


    //region  ( Doesn't need any credentials or authorization )
    fun login(request: Request, callback: RequestCallback<Any>) {
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.login(request)
                .catch { callback.onError(it) }
                .onCompletion {}.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "login collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }

    //id, hash, otp
    fun loginOTP(request: RequestLoginOTP, callback: RequestCallback<Any>) {
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.loginOTP(request)
                .catch { callback.onError(it) }
                .onCompletion {
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "loginOTP collect  $it")
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
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "requestOTP collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }

    fun verifyOTP(request: Request, callback: RequestCallback<OTPVerifyResults>) {
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.verifyOTP(request)
                .catch { callback.onError(it) }
                .onCompletion {
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "verifyOTP collect  $it")
                    if (it.succeed) {
                        it.results?.also { results ->
                            NetworkModule.cacheAccessToken(results.tokenType, results.accessToken)
                            callback.onSuccess(results)
                        } ?: callback.onError(Throwable("Result Object Is Null."))
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }

    fun getQRSession(callback: RequestCallback<QrResult>) {
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.getQRSession()
                .catch { callback.onError(it) }
                .onCompletion {
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "getQRSession collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }

    fun checkSession(hash: String, id: String, callback: RequestCallback<Any>) {
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.checkSession(hash, id)
                .catch { callback.onError(it) }
                .onCompletion {
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "checkSession collect  $it")
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

    fun getUserProfile(callback: RequestCallback<UserInfo>) {
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.getUserProfile()
                .catch { callback.onError(it) }
                .onCompletion {
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "getUserProfile collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }

    fun getUserNumbers(callback: RequestCallback<FinancialResult>) {
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.getUserNumbers()
                .catch {
                    callback.onError(it)
                    Log.e(TAG, "getUserNumbers  onError $it")
                }
                .onCompletion {
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "getUserNumbers collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }

    fun getFinancialInfo(callback: RequestCallback<FinancialResult>) {
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.getFinancialInfo()
                .catch { callback.onError(it) }
                .onCompletion {
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "getFinancialInfo collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }

    fun getNewModalSession(callback: RequestCallback<FinancialResult>) {
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.getNewModalSession()
                .catch { callback.onError(it) }
                .onCompletion {
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "getNewModalSession collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }

    fun checkUsername(callback: RequestCallback<Any>) {
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.checkUsername()
                .catch { callback.onError(it) }
                .onCompletion {
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "checkUsername collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }

    fun claimModalSession(hash: String, id: String, callback: RequestCallback<Any>) {
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.claimModalSession(hash, id)
                .catch { callback.onError(it) }
                .onCompletion {
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "claimModalSession collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }

    fun claimQRSession(hash: String, id: String, callback: RequestCallback<String>) {
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.claimQRSession(hash, id)
                .catch {
                    callback.onError(it)
                    Log.e(TAG, "claimQRSession  $it")
                }
                .onCompletion {
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "claimQRSession collect  $it")
                    callback.onSuccess(it)
                }
        }
    }

    fun updateUserProfile(request: Profile, callback: RequestCallback<Any>) {
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.updateUserProfile(request)
                .catch { callback.onError(it) }
                .onCompletion {
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "updateUserProfile collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }

    fun updateFinancialInfo(request: Profile, callback: RequestCallback<Any>) {
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.updateFinancialInfo(request)
                .catch { callback.onError(it) }
                .onCompletion {
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "updateFinancialInfo collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }

    fun addMobileNumber(request: Request, callback: RequestCallback<Any>) {
        CoroutineScope(Dispatchers.Main).launch {
            NetworkModule.addMobileNumber(request)
                .catch { callback.onError(it) }
                .onCompletion {
                }.flowOn(ioContext)
                .collect {
                    Log.e(TAG, "addMobileNumber collect  $it")
                    if (it.succeed) {
                        callback.onSuccess(it.results!!)
                    } else {
                        callback.onError(Throwable(it.message))
                    }
                }
        }
    }

    //endregion


    fun initQrReader(ctx: Context, width: Int, height: Int): Boolean {
        if (::barcodeDetector.isInitialized) {
            cameraSourceRef = WeakReference(
                CameraSource.Builder(ctx, barcodeDetector)
                    .setRequestedPreviewSize(width, height)
                    .setAutoFocusEnabled(true) //you should add this feature
                    .build()
            )
            return true
        }
        return false
    }

    /** HOW TO USED
     *  SurfaceView.holder.addCallback(object : SurfaceHolder.Callback {
     *      override fun surfaceCreated(holder: SurfaceHolder) {}
     *      override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int ) {
     *          try {
     *                 //initialize QR Scanner
     *                 WerifyHelper.initQrReader(Context, width, height)
     *                //Start preview after 1s delay
     *                WerifyHelper.getCameraSource()?.start(holder)
     *             } catch (e: IOException) {
     *                  e.printStackTrace()
     *             }
     *       }
     *       override fun surfaceDestroyed(holder: SurfaceHolder) {
     *               WerifyHelper.getCameraSource()?.stop()
     *        }
     */
    fun getCameraSource() = cameraSourceRef.get()
}