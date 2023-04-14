# id-android sdk for werify

## Requirements

Id-Android SDK can be included in any Android application.

Id-Android SDK supports Android 5.1+.

## Using Id-Android SDK in your application

Add this in your build.gradle

```groovy
 implementation project(path: ':id-android')
```

Do not forget to request camera permission in your `Application` for scan QR.

Then initialize it in onCreate() Method of application class :

```kotlin
WerifyHelper.initialize(
    getApplicationContext(), WerifyConfigure
        .Builder("APP KEY")
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .setUrl("BASE URL FOR WERIFY ID")
        // Adding an Network Interceptor for Debugging purpose :
        .setOKHttpClient(`YOU CAN SET NEW INSTANCE OF OK HTTP CLIENT`)
        .build()
)
``` 
### Now you can used this method in to your application.

#### login function

```kotlin
 WerifyHelper.login(RequestOTP("identifier"),
    object : RequestCallback<Any> {
        override fun onError(throwable: Throwable) {}
        override fun onSuccess(result: Any) {}
    })
```

#### loginOTP function

```kotlin
 WerifyHelper.loginOTP(
    RequestLoginOTP("hash", "opt", "id"),
    object : RequestCallback<Any> {
        override fun onError(throwable: Throwable) {}
        override fun onSuccess(result: Any) {}
    })
```

#### requestOTP function then you received `OTPRequestResults` Object

```kotlin
  WerifyHelper.requestOTP(RequestOTP("identifier"),
    object : RequestCallback<OTPRequestResults> {
        override fun onError(throwable: Throwable) {}
        override fun onSuccess(result: OTPRequestResults) {
            // verifyOTP(result.toVerifyObject())
        }
    })
```

#### verifyOTP function

```kotlin
WerifyHelper.verifyOTP(VerifyOTP(type, hash, otp, id),
    object : RequestCallback<OTPVerifyResults> {
        override fun onError(throwable: Throwable) {
        }
        override fun onSuccess(result: OTPVerifyResults) {
            // getQRSession()
        }
    })
```

#### getQRSession function

```kotlin
 WerifyHelper.getQRSession(
    object : RequestCallback<QrResult> {
        override fun onError(throwable: Throwable) {}
        override fun onSuccess(result: QrResult) {
            // checkSession(result.hash, result.id)
        }
    })
```

#### getUserProfile function

```kotlin
  WerifyHelper.getUserProfile(
    object : RequestCallback<UserInfo> {
        override fun onError(throwable: Throwable) {}
        override fun onSuccess(result: UserInfo) {}
    })
```

#### getUserNumbers function

```kotlin
 WerifyHelper.getUserNumbers(
    object : RequestCallback<FinancialResult> {
        override fun onError(throwable: Throwable) {}
        override fun onSuccess(result: FinancialResult) {}
    })
```

#### getFinancialInfo function

```kotlin
 WerifyHelper.getFinancialInfo(
    object : RequestCallback<FinancialResult> {
        override fun onError(throwable: Throwable) {}
        override fun onSuccess(result: FinancialResult) {}
    })
```

#### getNewModalSession function

```kotlin
 WerifyHelper.getNewModalSession(
    object : RequestCallback<FinancialResult> {
        override fun onError(throwable: Throwable) {}
        override fun onSuccess(result: FinancialResult) {}
    })
```

#### checkSession function

```kotlin
  WerifyHelper.checkSession(hash, id,
    object : RequestCallback<Any> {
        override fun onError(throwable: Throwable) {}
        override fun onSuccess(result: Any) {}
    })
```

#### claimModalSession function

```kotlin
  WerifyHelper.claimModalSession(hash, id,
    object : RequestCallback<Any> {
        override fun onError(throwable: Throwable) {}
        override fun onSuccess(result: Any) {}
    })
```

#### claimQRSession function

```kotlin
  WerifyHelper.claimQRSession(hash, id,
    object : RequestCallback<String> {
        override fun onError(throwable: Throwable) {}
        override fun onSuccess(result: String) {
            // Result Path to `.svg` file.
            //NOTE: Android does not recognize the SVG file formats, 
            // an external library must be used to use it.
        }
    })
```

#### claimQRSession function

```kotlin

   // data class Profile (
   //     var firstName    : String,
   //     var middleName   : String,
   //     var lastName     : String,
   //     var mobileNumber : String,
   //     var avatar       : String,
   //     var cover        : String,
   //     var isPrivate    : String,
   //     var language     : String,
   //     var currency     : String,
   //     var timezone     : String,
   //     var calendar     : String,
   //     var shortcuts    : String,
   //     var layout       : String,
   //     var latitude     : String,
   //     var longitude    : String,
   //     var lastOnline   : String,
   //     var updatedAt    : String
   // )
  WerifyHelper.updateFinancialInfo(Profile("firstName", "middleName", "lastName"),
    object : RequestCallback<String> {
        override fun onError(throwable: Throwable) {}
        override fun onSuccess(result: String) {}
    })
```

#### updateUserProfile function

```kotlin

    // data class Profile (
    //     var firstName    : String,
    //     var middleName   : String,
    //     var lastName     : String,
    //     var mobileNumber : String,
    //     var avatar       : String,
    //     var cover        : String,
    //     var isPrivate    : String,
    //     var language     : String,
    //     var currency     : String,
    //     var timezone     : String,
    //     var calendar     : String,
    //     var shortcuts    : String,
    //     var layout       : String,
    //     var latitude     : String,
    //     var longitude    : String,
    //     var lastOnline   : String,
    //     var updatedAt    : String
    // )
 WerifyHelper.updateUserProfile(Profile("firstName", "middleName", "lastName"),
    object : RequestCallback<Any> {
        override fun onError(throwable: Throwable) {}
        override fun onSuccess(result: Any) {}
    })
```

#### addMobileNumber function

```kotlin
  WerifyHelper.addMobileNumber(
    UserNumberRequest("mobile_number"),
    object : RequestCallback<Any> {
        override fun onError(throwable: Throwable) {}
        override fun onSuccess(result: Any) {}
    })
```

#### getCameraSource function for QR scanner

```kotlin
// In your Activity Or Fragment
YourSurfaceViewInstance.holder.addCallback(object : SurfaceHolder.Callback {
    override fun surfaceCreated(holder: SurfaceHolder) {}
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        try {
            //initialize QR Scanner  
            if (WerifyHelper.initQrReader(Context, width, height)) {
                //Start preview after 1s delay
                WerifyHelper.getCameraSource()?.start(holder)
            } else {
                throw java.lang.IllegalStateException("Scanner Can't init ...")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        WerifyHelper.getCameraSource()?.stop()
    }
})
```
