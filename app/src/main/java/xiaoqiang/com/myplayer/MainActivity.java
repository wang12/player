package xiaoqiang.com.myplayer;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.sql.Time;

import xiaoqiang.com.myplayer.player.IJKPlayer;
import xiaoqiang.com.myplayer.player.base.IPlayer;
import xiaoqiang.com.myplayer.player.base.IPlayerListener;

public class MainActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private IPlayer iPlayer;
    private SeekBar seekBar;
    private Handler mHandler;
    private TextView timeView;
    private boolean isTouchSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        surfaceView = (SurfaceView) findViewById(R.id.play_surface);
        seekBar = (SeekBar) findViewById(R.id.play_seekbar);
        timeView = (TextView) findViewById(R.id.time);
        mHandler = new Handler();
        isTouchSeek = false;

        iPlayer = new IJKPlayer();
        iPlayer.setPlayerListener(playerListener);
        iPlayer.setDataSource("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("wqq","onStartTrackingTouch");
                isTouchSeek = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("wqq","onStopTrackingTouch");
                isTouchSeek = false;
                iPlayer.seekTo(seekBar.getProgress());
            }
        });
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d("wqq","surfaceCreated");
                iPlayer.setDisplay(holder);
                if(!iPlayer.isPlaying()) {
                    iPlayer.prepare();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!iPlayer.isPlaying()) {
            iPlayer.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        iPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iPlayer.stop();
        iPlayer.realse();
        iPlayer.reSet();
    }

    private IPlayerListener playerListener = new IPlayerListener() {
        @Override
        public void onPrepare() {
            iPlayer.start();
            Log.d("wqq","onPrepare");
        }

        @Override
        public void onCompletion() {
            Log.d("wqq","onCompletion");
        }

        @Override
        public void onBuffering(int state) {
            Log.d("wqq","onBuffering:"+state);
        }

        @Override
        public void onSeekComplete() {
            Log.d("wqq","onSeekComplete:");
        }

        @Override
        public void onError(int error) {
            Log.d("wqq","onError:"+error);
        }

        @Override
        public void onVideoSizeChanged(int width, int height) {
            Log.d("wqq","onVideoSizeChanged,width:"+width+",height:"+height);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) surfaceView.getLayoutParams();
            params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            params.height = (int) (getWidth()*height/(float)width);
            surfaceView.setLayoutParams(params);

            mHandler.removeCallbacks(timeRunnable);
            seekBar.setMax((int) iPlayer.getDuration());
            mHandler.postDelayed(timeRunnable,1000);

            timeView.setText("0/"+iPlayer.getDuration());
        }

        @Override
        public void onInfo(int arg1, int arg2) {
            Log.d("wqq","onInfo,arg1:"+arg1+",arg2:"+arg2);
        }

        @Override
        public void onInit() {
            Log.d("wqq","onInit");
        }
    };
    int getWidth(){
        return getWindowManager().getDefaultDisplay().getWidth();
    }
    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            if(!isTouchSeek) {
                seekBar.setProgress((int) iPlayer.getCurrentPosition());
            }
            timeView.setText(iPlayer.getCurrentPosition()+"/"+iPlayer.getDuration());
            mHandler.postDelayed(timeRunnable,1000);
        }
    };
}
