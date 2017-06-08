package xiaoqiang.com.myplayer.player.base;

import android.view.SurfaceHolder;

/**
 * Created by xiaoqiang on 2017/5/9.
 * 播放器接口
 */

public interface IPlayer {
    /**
     * 初始化播放器
     */
    void initPlayer();

    /**
     * 设置播放URL
     * @param url
     */
    void setDataSource(String url);

    /**
     * 设置显示VIew
     * @param sh
     */
    void setDisplay(SurfaceHolder sh);

    /**
     * 播放器准备
     */
    void prepare();

    /**
     * 开始播放
     */
    void start();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 恢复播放
     */
    void resume();

    /**
     * seek到多少时间
     * @param ms 时间增加ms数
     */
    void seekTo(int ms);

    /**
     * 播放器释放
     */
    void realse();

    /**
     * 播放器重置
     */
    void reSet();

    /**
     * 是否正在播放
     * @return
     */
    boolean isPlaying();

    /**
     * 获取当前播放时间
     * @return
     */
    long getCurrentPosition();

    /**
     * 获取最大时间
     * @return
     */
    long getDuration();

    /**
     * 获取当前视频宽度
     * @return
     */
    int getVideoWidth();

    /**
     * 获取当前视频高度
     * @return
     */
    int getVideoHeight();

    /**
     * 添加播放器监听器
     * @param listener
     */
    void setPlayerListener(IPlayerListener listener);
}
