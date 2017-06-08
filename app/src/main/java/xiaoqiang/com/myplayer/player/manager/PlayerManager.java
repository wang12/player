package xiaoqiang.com.myplayer.player.manager;

import xiaoqiang.com.myplayer.player.IJKPlayer;
import xiaoqiang.com.myplayer.player.base.IPlayer;
import xiaoqiang.com.myplayer.player.base.IPlayerListener;

/**
 * Created by xiaoqiang on 2017/5/15.
 */

public class PlayerManager {
    IPlayer player;
    public void init(){
        player = new IJKPlayer();
        player.setPlayerListener(playerListener);
    }
    private IPlayerListener playerListener = new IPlayerListener() {
        @Override
        public void onPrepare() {

        }

        @Override
        public void onCompletion() {

        }

        @Override
        public void onBuffering(int state) {

        }

        @Override
        public void onSeekComplete() {

        }

        @Override
        public void onError(int error) {

        }

        @Override
        public void onVideoSizeChanged(int width, int height) {

        }

        @Override
        public void onInfo(int arg1, int arg2) {

        }

        @Override
        public void onInit() {

        }
    };
}
