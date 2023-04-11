package net.werify.id.common

import net.werify.id.interfaces.ConnectionQualityChangeListener


class ConnectionClassManager {


    private val BYTES_TO_BITS = 8
    private val DEFAULT_SAMPLES_TO_QUALITY_CHANGE = 5
    private val MINIMUM_SAMPLES_TO_DECIDE_QUALITY = 2
    private val DEFAULT_POOR_BANDWIDTH = 150
    private val DEFAULT_MODERATE_BANDWIDTH = 550
    private val DEFAULT_GOOD_BANDWIDTH = 2000
    private val BANDWIDTH_LOWER_BOUND: Long = 10

    private var mCurrentConnectionQuality = ConnectionQuality.UNKNOWN
    private var mCurrentBandwidthForSampling = 0
    private var mCurrentNumberOfSample = 0
    private var mCurrentBandwidth = 0
    private var mConnectionQualityChangeListener: ConnectionQualityChangeListener? = null

    companion object {
        private var sInstance: ConnectionClassManager? = null
        @JvmStatic
        fun getInstance(): ConnectionClassManager {
            if (sInstance == null) {
                synchronized(ConnectionClassManager::class.java) {
                    if (sInstance == null) {
                        sInstance = ConnectionClassManager()
                    }
                }
            }
            return sInstance!!
        }

        @JvmStatic
        fun shutDown() {
            if (sInstance != null) {
                sInstance = null
            }
        }
    }

    @Synchronized
    fun updateBandwidth(bytes: Long, timeInMs: Long) {
        if (timeInMs == 0L || bytes < 20000 || bytes * 1.0 / timeInMs *
            BYTES_TO_BITS < BANDWIDTH_LOWER_BOUND
        ) {
            return
        }
        val bandwidth = bytes * 1.0 / timeInMs * BYTES_TO_BITS
        mCurrentBandwidthForSampling = ((mCurrentBandwidthForSampling *
                mCurrentNumberOfSample + bandwidth) / (mCurrentNumberOfSample + 1)).toInt()
        mCurrentNumberOfSample++
        if (mCurrentNumberOfSample == DEFAULT_SAMPLES_TO_QUALITY_CHANGE ||
            (mCurrentConnectionQuality === ConnectionQuality.UNKNOWN) && mCurrentNumberOfSample == MINIMUM_SAMPLES_TO_DECIDE_QUALITY
        ) {
            val lastConnectionQuality = mCurrentConnectionQuality
            mCurrentBandwidth = mCurrentBandwidthForSampling
            if (mCurrentBandwidthForSampling <= 0) {
                mCurrentConnectionQuality = ConnectionQuality.UNKNOWN
            } else if (mCurrentBandwidthForSampling < DEFAULT_POOR_BANDWIDTH) {
                mCurrentConnectionQuality = ConnectionQuality.POOR
            } else if (mCurrentBandwidthForSampling < DEFAULT_MODERATE_BANDWIDTH) {
                mCurrentConnectionQuality = ConnectionQuality.MODERATE
            } else if (mCurrentBandwidthForSampling < DEFAULT_GOOD_BANDWIDTH) {
                mCurrentConnectionQuality = ConnectionQuality.GOOD
            } else if (mCurrentBandwidthForSampling > DEFAULT_GOOD_BANDWIDTH) {
                mCurrentConnectionQuality = ConnectionQuality.EXCELLENT
            }
            if (mCurrentNumberOfSample == DEFAULT_SAMPLES_TO_QUALITY_CHANGE) {
                mCurrentBandwidthForSampling = 0
                mCurrentNumberOfSample = 0
            }
            if (mCurrentConnectionQuality !== lastConnectionQuality &&
                mConnectionQualityChangeListener != null
            ) {
                //TODO : Notify CurrentConnectionQuality to main thread
               /* Core.getInstance().getExecutorSupplier().forMainThreadTasks()
                    .execute(Runnable {
                        mConnectionQualityChangeListener!!
                            .onChange(mCurrentConnectionQuality, mCurrentBandwidth)
                    })*/
            }
        }
    }

    fun getCurrentBandwidth(): Int {
        return mCurrentBandwidth
    }

    fun getCurrentConnectionQuality(): ConnectionQuality? {
        return mCurrentConnectionQuality
    }

    fun setListener(connectionQualityChangeListener: ConnectionQualityChangeListener?) {
        mConnectionQualityChangeListener = connectionQualityChangeListener
    }

    fun removeListener() {
        mConnectionQualityChangeListener = null
    }


}