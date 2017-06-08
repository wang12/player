package xiaoqiang.com.myplayer.player.base;

/**
 * Created by xiaoqiang on 2017/5/9.
 * 播放器状态接口
 */

public interface IPlayerListener {
    int BUFFER_NULL = 0;
    int BUFFER_FILL = 1;

    /**
     * 准备完成，可以开始播放了
     */
    void onPrepare();

    /**
     * 播放完成了
     */
    void onCompletion();

    /**
     * 播放器是否在缓冲。当前缓冲状态
     * @param state
     */
    void onBuffering(int state);

    /**
     * seek完成
     */
    void onSeekComplete();

    /**
     * 播放错误
     * @param error
     */
    void onError(int error);

    /**
     * 播放器大小改变。成功播放会回调第一次
     * @param width
     * @param height
     */
    void onVideoSizeChanged(int width,int height);

    void onInfo(int arg1,int arg2);

    void onInit();
}
