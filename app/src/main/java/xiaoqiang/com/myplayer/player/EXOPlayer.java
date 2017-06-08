package xiaoqiang.com.myplayer.player;

import android.view.SurfaceHolder;

import xiaoqiang.com.myplayer.player.base.BasePlayer;
import xiaoqiang.com.myplayer.player.base.IPlayer;
import xiaoqiang.com.myplayer.player.base.IPlayerListener;

/**
 * Created by xiaoqiang on 2017/5/9.
 */

public class EXOPlayer extends BasePlayer implements IPlayer {
    @Override
    public void initPlayer() {

    }

    @Override
    public void setDataSource(String url) {

    }

    @Override
    public void setDisplay(SurfaceHolder sh) {

    }

    @Override
    public void prepare() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void seekTo(int ms) {

    }

    @Override
    public void realse() {

    }

    @Override
    public void reSet() {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public long getCurrentPosition() {
        return 0;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public int getVideoWidth() {
        return 0;
    }

    @Override
    public int getVideoHeight() {
        return 0;
    }

    @Override
    public void setPlayerListener(IPlayerListener listener) {

    }
}
