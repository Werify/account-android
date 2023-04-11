package net.werify.id;


import android.content.Context;

import net.werify.id.common.ConnectionClassManager;
import net.werify.id.common.ConnectionQuality;
import net.werify.id.interfaces.ConnectionQualityChangeListener;
import net.werify.id.internal.InternalNetworking;
import net.werify.id.retrofit.WerifyConfigure;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * WerifyNetworking entry point.
 * You must initialize this class before use. The simplest way is to just do
 * {#code WerifyNetworking.initialize(context , appKey)}.
 */
public class WerifyNetworking {

    /**
     * private constructor to prevent instantiation of this class
     */
    private WerifyNetworking() {
    }

    /**
     * Initializes WerifyNetworking with the default config.
     *
     * @param configure  The Custom Configuration of WerifyNetworking
     * @param context The context
     */
    public static void initialize(Context context, WerifyConfigure configure) {
        InternalNetworking.setClientWithCache(context.getApplicationContext() ,configure);
//        RequestQueue.initialize();
    }

    /**
     * Method to set connectionQualityChangeListener
     *
     * @param connectionChangeListener The connectionQualityChangeListener
     */
    public static void setConnectionQualityChangeListener(ConnectionQualityChangeListener connectionChangeListener) {
        ConnectionClassManager.getInstance().setListener(connectionChangeListener);
    }

    /**
     * Method to set connectionQualityChangeListener
     */
    public static void removeConnectionQualityChangeListener() {
        ConnectionClassManager.getInstance().removeListener();
    }


    /**
     * Method to enable logging
     */
    public static void enableLogging() {
        enableLogging(HttpLoggingInterceptor.Level.BASIC);
    }

    /**
     * Method to enable logging with tag
     *
     * @param level The level for logging
     */
    public static void enableLogging(HttpLoggingInterceptor.Level level) {
        InternalNetworking.enableLogging(level);
    }

    /**
     * Method to get currentBandwidth
     *
     * @return currentBandwidth
     */
    public static int getCurrentBandwidth() {
        return ConnectionClassManager.getInstance().getCurrentBandwidth();
    }

    /**
     * Method to get currentConnectionQuality
     *
     * @return currentConnectionQuality
     */
    public static ConnectionQuality getCurrentConnectionQuality() {
        return ConnectionClassManager.getInstance().getCurrentConnectionQuality();
    }

    /**
     * Shuts WerifyNetworking down
     */
    public static void shutDown() {
        //Core.shutDown();
        //evictAllBitmap();
        ConnectionClassManager.getInstance().removeListener();
        ConnectionClassManager.shutDown();
        //ParseUtil.shutDown();
    }
}
