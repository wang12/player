package xiaoqiang.com.myplayer.player;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import xiaoqiang.com.myplayer.player.base.BasePlayer;
import xiaoqiang.com.myplayer.player.base.IPlayer;
import xiaoqiang.com.myplayer.player.base.IPlayerListener;

/**
 * Created by xiaoqiang on 2017/5/9.
 */

public class IJKPlayer extends BasePlayer implements IPlayer {
    private IjkMediaPlayer ijk;
    private boolean isBuffer = false;


    public IJKPlayer() {
        super();
        ijk = new IjkMediaPlayer(new MyHandler());
    }

    @Override
    public void initPlayer() {
        //注册各种监听器
    }

    @Override
    public void setDataSource(String url) {
        try {
            ijk.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDisplay(SurfaceHolder sh) {
        ijk.setDisplay(sh);
    }

    @Override
    public void prepare() {
        ijk.prepareAsync();
    }

    @Override
    public void start() {
        ijk.start();
    }

    @Override
    public void stop() {
        ijk.stop();
    }

    @Override
    public void pause() {
        ijk.pause();
    }

    @Override
    public void resume() {
        ijk.start();
    }

    @Override
    public void seekTo(int ms) {
        ijk.seekTo(ms);
    }

    @Override
    public void realse() {
        ijk.release();
    }

    @Override
    public void reSet() {
        ijk.reset();
    }

    @Override
    public boolean isPlaying() {
        return ijk.isPlaying();
    }

    @Override
    public long getCurrentPosition() {
        return ijk.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return ijk.getDuration();
    }

    @Override
    public int getVideoWidth() {
        return ijk.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return ijk.getVideoHeight();
    }

    class MyHandler extends Handler {
        public MyHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case IjkMediaPlayer.MEDIA_NOP:
                    listener.onInit();
                    break;
                case IjkMediaPlayer.MEDIA_BUFFERING_UPDATE:
                    if(msg.arg1 == 0){
                        listener.onBuffering(IPlayerListener.BUFFER_NULL);
                        isBuffer = true;
                    }else if(isBuffer && msg.arg1 > 1*1024){
                        isBuffer = false;
                        listener.onBuffering(IPlayerListener.BUFFER_FILL);
                    }
                    break;
                case IjkMediaPlayer.MEDIA_ERROR:
                    listener.onError(msg.arg1);
                    break;
                case IjkMediaPlayer.MEDIA_INFO:
                    listener.onInfo(msg.arg1, msg.arg2);
                    break;
                case IjkMediaPlayer.MEDIA_PLAYBACK_COMPLETE:
                    listener.onCompletion();
                    break;
                case IjkMediaPlayer.MEDIA_PREPARED:
                    listener.onPrepare();
                    break;
                case IjkMediaPlayer.MEDIA_SEEK_COMPLETE:
                    listener.onSeekComplete();
                    break;
                case IjkMediaPlayer.MEDIA_SET_VIDEO_SIZE:
                    listener.onVideoSizeChanged(msg.arg1, msg.arg2);
                    break;
                case IjkMediaPlayer.MEDIA_TIMED_TEXT:
                    Log.d("wqq","MEDIA_TIMED_TEXT,arg1:"+msg.arg1+",arg2:"+msg.arg2);
                    break;
            }
        }
    }

}
