/*
 * Copyright (C) 2006 Bilibili
 * Copyright (C) 2006 The Android Open Source Project
 * Copyright (C) 2013 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tv.danmaku.ijk.media.player;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.InvalidParameterException;

import tv.danmaku.ijk.media.player.misc.IAndroidIO;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;

/**
 * @author bbcallen
 *
 *         Java wrapper of ffplay.
 */
public final class IjkMediaPlayer {
    private final static String TAG = IjkMediaPlayer.class.getName();

    public static final int MEDIA_NOP = 0; // interface test message
    public static final int MEDIA_PREPARED = 1;
    public static final int MEDIA_PLAYBACK_COMPLETE = 2;
    public static final int MEDIA_BUFFERING_UPDATE = 3;
    public static final int MEDIA_SEEK_COMPLETE = 4;
    public static final int MEDIA_SET_VIDEO_SIZE = 5;
    public static final int MEDIA_TIMED_TEXT = 99;
    public static final int MEDIA_ERROR = 100;
    public static final int MEDIA_INFO = 200;

    private long mNativeMediaPlayer;
    private long mNativeMediaDataSource;
    private long mNativeAndroidIO;
    private int mNativeSurfaceTexture;
    private int mListenerContext;

    private SurfaceHolder mSurfaceHolder;
    private PowerManager.WakeLock mWakeLock = null;
    private boolean mStayAwake;

    private int mVideoWidth;
    private int mVideoHeight;

    static {
        System.loadLibrary("ijkffmpeg");
        System.loadLibrary("ijksdl");
        System.loadLibrary("ijksoundtouch");
        System.loadLibrary("ijkplayer");
    }
    private static volatile boolean mIsNativeInitialized = false;
    private static void initNativeOnce() {
        synchronized (IjkMediaPlayer.class) {
            if (!mIsNativeInitialized) {
                native_init();
                mIsNativeInitialized = true;
            }
        }
    }

    /**
     * Default constructor. Consider using one of the create() methods for
     * synchronously instantiating a IjkMediaPlayer from a Uri or resource.
     * <p>
     * When done with the IjkMediaPlayer, you should call {@link #release()}, to
     * free the resources. If not released, too many IjkMediaPlayer instances
     * may result in an exception.
     * </p>
     */
    private static Handler mHandler;
    public IjkMediaPlayer(Handler mHandler) {
        initPlayer();
        this.mHandler = mHandler;
    }

    private void initPlayer() {
        initNativeOnce();
        native_setup(new WeakReference<IjkMediaPlayer>(this));
    }

    private native void _setVideoSurface(Surface surface);

    public void setDisplay(SurfaceHolder sh) {
        mSurfaceHolder = sh;
        Surface surface;
        if (sh != null) {
            surface = sh.getSurface();
        } else {
            surface = null;
        }
        _setVideoSurface(surface);
        updateSurfaceScreenOn();
    }
    public void setDataSource(String path)
            throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        _setDataSource(path, null, null);
    }
    private native void _setDataSource(String path, String[] keys, String[] values)
            throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    private native void _setDataSourceFd(int fd)
            throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    private native void _setDataSource(IMediaDataSource mediaDataSource)
            throws IllegalArgumentException, SecurityException, IllegalStateException;

    private native void _setAndroidIOCallback(IAndroidIO androidIO)
            throws IllegalArgumentException, SecurityException, IllegalStateException;

    public void prepareAsync() throws IllegalStateException {
        _prepareAsync();
    }

    public native void _prepareAsync() throws IllegalStateException;

    public void start() throws IllegalStateException {
        stayAwake(true);
        _start();
    }

    private native void _start() throws IllegalStateException;

    public void stop() throws IllegalStateException {
        stayAwake(false);
        _stop();
    }

    private native void _stop() throws IllegalStateException;

    public void pause() throws IllegalStateException {
        stayAwake(false);
        _pause();
    }

    private native void _pause() throws IllegalStateException;


    private void stayAwake(boolean awake) {
        if (mWakeLock != null) {
            if (awake && !mWakeLock.isHeld()) {
                mWakeLock.acquire();
            } else if (!awake && mWakeLock.isHeld()) {
                mWakeLock.release();
            }
        }
        mStayAwake = awake;
        updateSurfaceScreenOn();
    }

    private void updateSurfaceScreenOn() {
        if (mSurfaceHolder != null) {
            mSurfaceHolder.setKeepScreenOn(mStayAwake);
        }
    }

    private native void _setStreamSelected(int stream, boolean select);

    public int getVideoWidth() {
        return mVideoWidth;
    }

    public int getVideoHeight() {
        return mVideoHeight;
    }

    public native boolean isPlaying();

    public native void seekTo(long msec) throws IllegalStateException;

    public native long getCurrentPosition();

    public native long getDuration();

    public void release() {
        stayAwake(false);
        updateSurfaceScreenOn();
        _release();
    }

    private native void _release();

    public void reset() {
        stayAwake(false);
        _reset();
        mVideoWidth = 0;
        mVideoHeight = 0;
    }

    private native void _reset();


    private native void _setLoopCount(int loopCount);

    private native int _getLoopCount();

    private native float _getPropertyFloat(int property, float defaultValue);
    private native void  _setPropertyFloat(int property, float value);
    private native long  _getPropertyLong(int property, long defaultValue);
    private native void  _setPropertyLong(int property, long value);

    public native void setVolume(float leftVolume, float rightVolume);

    public native int getAudioSessionId();

    private native String _getVideoCodecInfo();
    private native String _getAudioCodecInfo();

    private native void _setOption(int category, String name, String value);
    private native void _setOption(int category, String name, long value);

    private native Bundle _getMediaMeta();

    private static native String _getColorFormatName(int mediaCodecColorFormat);

    private static native void native_init();

    private native void native_setup(Object IjkMediaPlayer_this);

    private native void native_finalize();

    private native void native_message_loop(Object IjkMediaPlayer_this);

    protected void finalize() throws Throwable {
        super.finalize();
        native_finalize();
    }

    private static   void postEventFromNative(Object weakThiz, int what,
                                            int arg1, int arg2, Object obj) {
        if (weakThiz == null)
            return;

        @SuppressWarnings("rawtypes")
        IjkMediaPlayer mp = (IjkMediaPlayer) ((WeakReference) weakThiz).get();
        if (mp == null) {
            return;
        }

//        if (what == MEDIA_INFO && arg1 == IMediaPlayer.MEDIA_INFO_STARTED_AS_NEXT) {
//            // this acquires the wakelock if needed, and sets the client side
//            // state
//            mp.start();
//        }
       Message message =  mHandler.obtainMessage(what);
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = obj;
        message.sendToTarget();
    }
    public void setOnNativeInvokeListener(OnNativeInvokeListener listener) {
//        mOnNativeInvokeListener = listener;
    }
    public static native void native_profileBegin(String libName);
    public static native void native_profileEnd();
    public static native void native_setLogLevel(int level);

    public interface OnNativeInvokeListener {

        int CTRL_WILL_TCP_OPEN = 0x20001;               // NO ARGS
        int CTRL_DID_TCP_OPEN = 0x20002;                // ARG_ERROR, ARG_FAMILIY, ARG_IP, ARG_PORT, ARG_FD

        int CTRL_WILL_HTTP_OPEN = 0x20003;              // ARG_URL, ARG_SEGMENT_INDEX, ARG_RETRY_COUNTER
        int CTRL_WILL_LIVE_OPEN = 0x20005;              // ARG_URL, ARG_RETRY_COUNTER
        int CTRL_WILL_CONCAT_RESOLVE_SEGMENT = 0x20007; // ARG_URL, ARG_SEGMENT_INDEX, ARG_RETRY_COUNTER

        int EVENT_WILL_HTTP_OPEN = 0x1;                 // ARG_URL
        int EVENT_DID_HTTP_OPEN = 0x2;                  // ARG_URL, ARG_ERROR, ARG_HTTP_CODE
        int EVENT_WILL_HTTP_SEEK = 0x3;                 // ARG_URL, ARG_OFFSET
        int EVENT_DID_HTTP_SEEK = 0x4;                  // ARG_URL, ARG_OFFSET, ARG_ERROR, ARG_HTTP_CODE

        String ARG_URL = "url";
        String ARG_SEGMENT_INDEX = "segment_index";
        String ARG_RETRY_COUNTER = "retry_counter";

        String ARG_ERROR = "error";
        String ARG_FAMILIY = "family";
        String ARG_IP = "ip";
        String ARG_PORT = "port";
        String ARG_FD = "fd";

        String ARG_OFFSET = "offset";
        String ARG_HTTP_CODE = "http_code";

        /*
         * @return true if invoke is handled
         * @throws Exception on any error
         */
        boolean onNativeInvoke(int what, Bundle args);
    }
    private static String onSelectCodec(Object weakThiz, String mimeType, int profile, int level) {
        if (weakThiz == null || !(weakThiz instanceof WeakReference<?>))
            return null;

        @SuppressWarnings("unchecked")
        WeakReference<IjkMediaPlayer> weakPlayer = (WeakReference<IjkMediaPlayer>) weakThiz;
        IjkMediaPlayer player = weakPlayer.get();
        if (player == null)
            return null;


        return "";
    }

    private static boolean onNativeInvoke(Object weakThiz, int what, Bundle args) {
        if (weakThiz == null || !(weakThiz instanceof WeakReference<?>))
            throw new IllegalStateException("<null weakThiz>.onNativeInvoke()");

        @SuppressWarnings("unchecked")
        WeakReference<IjkMediaPlayer> weakPlayer = (WeakReference<IjkMediaPlayer>) weakThiz;
        IjkMediaPlayer player = weakPlayer.get();
        if (player == null)
            throw new IllegalStateException("<null weakPlayer>.onNativeInvoke()");
        switch (what) {
            case OnNativeInvokeListener.CTRL_WILL_CONCAT_RESOLVE_SEGMENT: {
                int segmentIndex = args.getInt(OnNativeInvokeListener.ARG_SEGMENT_INDEX, -1);
                if (segmentIndex < 0)
                    throw new InvalidParameterException("onNativeInvoke(invalid segment index)");
                return true;
            }
            default:
                return false;
        }
    }

}
