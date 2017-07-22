package com.lzj.vtm.demo.blvs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.PolyvBitRate;
import com.easefun.polyvsdk.video.PolyvPlayErrorReason;
import com.easefun.polyvsdk.video.PolyvVideoView;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoPlayErrorListener2;
import com.easefun.polyvsdk.video.listener.IPolyvOnVideoStatusListener;
import com.lzj.vtm.demo.R;

public class PolyvPlayerActivity extends FragmentActivity {

    private static final String TAG = PolyvPlayerActivity.class.getSimpleName();
    /**
     * 播放器的parentView
     */
    private RelativeLayout viewLayout = null;
    /**
     * 播放主视频播放器
     */
    private PolyvVideoView videoView = null;
    /**
     * 字幕文本视图
     */
    private TextView srtTextView = null;
    /**
     * 广告倒计时
     */
    private TextView advertCountDown = null;
    /**
     * 缩略图界面
     */
    private PolyvPlayerPreviewView firstStartView = null;
    /**
     * 视频加载缓冲视图
     */
    private ProgressBar loadingProgress = null;

    private int fastForwardPos = 0;
    private boolean isPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polyv_activity_player);
        findIdAndNew();
        initView();

        int playModeCode = getIntent().getIntExtra("playMode", PlayMode.portrait.getCode());
        PlayMode playMode = PlayMode.getPlayMode(playModeCode);
        if (playMode == null)
            playMode = PlayMode.portrait;
        String vid = getIntent().getStringExtra("value");
        int bitrate = getIntent().getIntExtra("bitrate", PolyvBitRate.ziDong.getNum());
        boolean startNow = getIntent().getBooleanExtra("startNow", false);
        boolean isMustFromLocal = getIntent().getBooleanExtra("isMustFromLocal", false);

        play(vid, bitrate, startNow, isMustFromLocal);
    }

    private void findIdAndNew() {

        viewLayout = (RelativeLayout) findViewById(R.id.view_layout);
        videoView = (PolyvVideoView) findViewById(R.id.polyv_video_view);
        srtTextView = (TextView) findViewById(R.id.srt);
        advertCountDown = (TextView) findViewById(R.id.count_down);
        loadingProgress = (ProgressBar) findViewById(R.id.loading_progress);
        firstStartView = (PolyvPlayerPreviewView) findViewById(R.id.polyv_player_first_start_view);

        videoView.setPlayerBufferingIndicator(loadingProgress);
    }

    private void initView() {
        videoView.setOpenAd(true);
        videoView.setOpenTeaser(true);
        videoView.setOpenQuestion(true);
        videoView.setOpenSRT(true);
        videoView.setOpenPreload(true, 2);
        videoView.setAutoContinue(true);
        videoView.setNeedGestureDetector(true);

        videoView.setOnVideoStatusListener(new IPolyvOnVideoStatusListener() {
            @Override
            public void onStatus(int status) {
                if (status < 60) {
                    Toast.makeText(PolyvPlayerActivity.this, String.format("状态错误 %d", status), Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, String.format("状态正常 %d", status));
                }
            }
        });

        videoView.setOnVideoPlayErrorListener(new IPolyvOnVideoPlayErrorListener2() {
            @Override
            public boolean onVideoPlayError(@PolyvPlayErrorReason.PlayErrorReason int playErrorReason) {
                String message = "";
                switch (playErrorReason) {
                    case PolyvPlayErrorReason.NETWORK_DENIED:
                        message = "无法连接网络，请连接网络后播放";
                        break;
                    case PolyvPlayErrorReason.OUT_FLOW:
                        message = "流量超标";
                        break;
                    case PolyvPlayErrorReason.TIMEOUT_FLOW:
                        message = "账号过期";
                        break;
                    case PolyvPlayErrorReason.LOCAL_VIEWO_ERROR:
                        message = "本地视频文件损坏，请重新下载";
                        break;
                    case PolyvPlayErrorReason.START_ERROR:
                        message = "播放异常，请重新播放";
                        break;
                    case PolyvPlayErrorReason.NOT_PERMISSION:
                        message = "非法播放";
                        break;
                    case PolyvPlayErrorReason.USER_TOKEN_ERROR:
                        message = "请先设置播放凭证，再进行播放";
                        break;
                    case PolyvPlayErrorReason.VIDEO_STATUS_ERROR:
                        message = "视频状态异常，无法播放";
                        break;
                    case PolyvPlayErrorReason.VID_ERROR:
                        message = "视频id不正确，请设置正确的视频id进行播放";
                        break;
                    case PolyvPlayErrorReason.BITRATE_ERROR:
                        message = "清晰度不正确，请设置正确的清晰度进行播放";
                        break;
                    case PolyvPlayErrorReason.VIDEO_NULL:
                        message = "视频信息加载失败，请重新播放";
                        break;
                    case PolyvPlayErrorReason.MP4_LINK_NUM_ERROR:
                        message = "MP4 播放地址服务器数据错误";
                        break;
                    case PolyvPlayErrorReason.M3U8_LINK_NUM_ERROR:
                        message = "HLS 播放地址服务器数据错误";
                        break;
                    case PolyvPlayErrorReason.HLS_SPEED_TYPE_NULL:
                        message = "请设置播放速度";
                        break;
                    case PolyvPlayErrorReason.NOT_LOCAL_VIDEO:
                        message = "找不到本地下载的视频文件，请连网后重新下载";
                        break;
                    case PolyvPlayErrorReason.HLS_15X_INDEX_EMPTY:
                        message = "视频不支持1.5倍自动清晰度播放";
                        break;
                    case PolyvPlayErrorReason.HLS_15X_ERROR:
                        message = "视频不支持1.5倍当前清晰度播放";
                        break;
                    case PolyvPlayErrorReason.HLS_15X_URL_ERROR:
                        message = "1.5倍当前清晰度视频正在编码中";
                        break;
                    case PolyvPlayErrorReason.M3U8_15X_LINK_NUM_ERROR:
                        message = "HLS 1.5倍播放地址服务器数据错误";
                        break;
                    case PolyvPlayErrorReason.CHANGE_EQUAL_BITRATE:
                        message = "切换清晰度相同，请选择其它清晰度";
                        break;
                    case PolyvPlayErrorReason.CHANGE_EQUAL_HLS_SPEED:
                        message = "切换播放速度相同，请选择其它播放速度";
                        break;
                    case PolyvPlayErrorReason.CAN_NOT_CHANGE_BITRATE:
                        message = "未开始播放视频不能切换清晰度，请先播放视频";
                        break;
                    case PolyvPlayErrorReason.CAN_NOT_CHANGE_HLS_SPEED:
                        message = "未开始播放视频不能切换播放速度，请先播放视频";
                        break;
                    case PolyvPlayErrorReason.QUESTION_ERROR:
                        message = "视频问答数据加载失败，请重新播放";
                        break;
                    case PolyvPlayErrorReason.CHANGE_BITRATE_NOT_EXIST:
                        message = "视频没有这个清晰度，请切换其它清晰度";
                        break;
                    case PolyvPlayErrorReason.HLS_URL_ERROR:
                        message = "播放地址异常，无法播放";
                        break;
                    case PolyvPlayErrorReason.LOADING_VIDEO_ERROR:
                        message = "视频信息加载中出现异常，请重新播放";
                        break;
                    case PolyvPlayErrorReason.HLS2_URL_ERROR:
                        message = "播放地址异常，无法播放";
                        break;
                    case PolyvPlayErrorReason.TOKEN_NULL:
                        message = "播放授权获取失败，请重新播放";
                        break;
                    case PolyvPlayErrorReason.EXCEPTION_COMPLETION:
                        message = "视频异常结束，请重新播放";
                        break;
                    case PolyvPlayErrorReason.WRITE_EXTERNAL_STORAGE_DENIED:
                        message = "检测到拒绝读取存储设备，请先为应用程序分配权限，再重新播放";
                        break;
                    default:
                        message = "播放异常，请联系管理员或者客服";
                        break;
                }

                message += "(error code " + playErrorReason + ")";
                AlertDialog.Builder builder = new AlertDialog.Builder(PolyvPlayerActivity.this);
                builder.setTitle("错误");
                builder.setMessage(message);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                builder.show();
                return true;
            }
        });

    }

    public void play(final String vid, final int bitrate, boolean startNow, final boolean isMustFromLocal) {
        if (TextUtils.isEmpty(vid)) return;

        videoView.release();
        srtTextView.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.VISIBLE);
        advertCountDown.setVisibility(View.GONE);
        firstStartView.hide();

        if (startNow) {
            videoView.setVid(vid, bitrate, isMustFromLocal);
        } else {
            firstStartView.setCallback(new PolyvPlayerPreviewView.Callback() {

                @Override
                public void onClickStart() {
                    videoView.setVid(vid, bitrate, isMustFromLocal);
                }
            });

            firstStartView.show(vid);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //回来后继续播放
        if (isPlay) {
            videoView.onActivityResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //弹出去暂停
        isPlay = videoView.onActivityStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.destroy();
        firstStartView.hide();
    }

    public static Intent newIntent(Context context, PlayMode playMode, String vid) {
        return newIntent(context, playMode, vid, PolyvBitRate.ziDong.getNum());
    }

    public static Intent newIntent(Context context, PlayMode playMode, String vid, int bitrate) {
        return newIntent(context, playMode, vid, bitrate, false);
    }

    public static Intent newIntent(Context context, PlayMode playMode, String vid, int bitrate, boolean startNow) {
        return newIntent(context, playMode, vid, bitrate, startNow, false);
    }

    public static Intent newIntent(Context context, PlayMode playMode, String vid, int bitrate, boolean startNow,
                                   boolean isMustFromLocal) {
        Intent intent = new Intent(context, PolyvPlayerActivity.class);
        intent.putExtra("playMode", playMode.getCode());
        intent.putExtra("value", vid);
        intent.putExtra("bitrate", bitrate);
        intent.putExtra("startNow", startNow);
        intent.putExtra("isMustFromLocal", isMustFromLocal);
        return intent;
    }

    /**
     * 播放模式
     * @author TanQu
     */
    public enum PlayMode {
        /** 横屏 */
        landScape(3),
        /** 竖屏 */
        portrait(4);

        private final int code;

        private PlayMode(int code) {
            this.code = code;
        }

        /**
         * 取得类型对应的code
         * @return
         */
        public int getCode() {
            return code;
        }

        public static PlayMode getPlayMode(int code) {
            switch (code) {
                case 3:
                    return landScape;
                case 4:
                    return portrait;
            }

            return null;
        }
    }
}
