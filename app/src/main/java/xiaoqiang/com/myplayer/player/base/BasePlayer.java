package xiaoqiang.com.myplayer.player.base;

/**
 * Created by xiaoqiang on 2017/5/9.
 */

public abstract class BasePlayer implements IPlayer {
    protected final static int ERROR_STATE = 0x01;
    protected IPlayerListener listener;

    public BasePlayer() {
    }


    @Override
    public void setPlayerListener(IPlayerListener listener) {
        this.listener = listener;
    }
}
