package com.mvideostreammming.mvideostreammming;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.MediaController;

import com.mvideostreammming.mvideostreammming.databinding.VideoViewBinding;
import com.mvideostreammming.mvideostreammming.utils.AppConst;
import com.mvideostreammming.mvideostreammming.utils.CommonUtils;
import com.mvideostreammming.mvideostreammming.utils.MyDialog;
import com.nineoldandroids.animation.ObjectAnimator;


public class VideoPlayerActivity extends AppCompatActivity implements View.OnClickListener,MyDialog.OnDialogFragmentClickListener {


    private VideoViewBinding binding;
    private boolean canShowTool = true, canBuffer = true, isShowingDialog;
    MediaController mediaController;
    private int old_duration, seekTo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(VideoPlayerActivity.this, R.layout.video_view);

        binding.executePendingBindings();

        setSupportActionBar(binding.include.toolbar);

        ImageButton ib = new ImageButton(this);
        ib.setImageResource(R.drawable.ic_replay);


        binding.include.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.include.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (getIntent().getExtras() != null) {

            if (getIntent().getExtras().containsKey("videoUrl")) {

                String link = getIntent().getExtras().getString("videoUrl");

                mediaController = new MediaController(this);
                mediaController.setAnchorView(binding.vvPlayer);
                binding.vvPlayer.setMediaController(mediaController);


                binding.vvPlayer.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                            canShowTool = true;
                            mediaController.show();
                            hideShowToolBar();
                            setToolThread();

                        }
                        return true;
                    }
                });
                binding.vvPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        Log.d("myplayer", "onPrepared");
                        canBuffer = true;
                        binding.vvPlayer.start();
                        setBufferListener();
                        canShowTool = false;
                        hideShowToolBar();


                    }
                });
                binding.vvPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {

                        canShowTool = true;
                        canBuffer = false;
                        hideShowToolBar();
                        binding.ibRefresh.setVisibility(View.VISIBLE);
                    }
                });
                binding.vvPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {

                        if (!isShowingDialog) {
                            if (!CommonUtils.isNetworkAvailable(VideoPlayerActivity.this)) {
                                showDialog("internet error", AppConst.AlertType.ERROR);
                                isShowingDialog = true;
                            } else {
                                showDialog("error", AppConst.AlertType.ERROR);
                                isShowingDialog = true;
                            }
                            canBuffer=false;
                            binding.setIsBuffering(false);
                        }
                        return true;
                    }
                });


                if (URLUtil.isValidUrl(link)) {
                    Uri video = Uri.parse(link);
                    binding.setIsBuffering(true);
                    binding.vvPlayer.setVideoURI(video);
                }


            }

        }

        binding.ibRefresh.setOnClickListener(this);

        binding.laytVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mediaController.show();
                    canShowTool = true;
                    hideShowToolBar();
                    setToolThread();

                }
                return true;
            }
        });


    }

    private void setBufferListener() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {

                try {
                    int duration = binding.vvPlayer.getCurrentPosition();
                    if (old_duration == duration && binding.vvPlayer.isPlaying()) {
                        //videoMessage.setVisibility(View.VISIBLE);
                        if (canBuffer) {
                            binding.setIsBuffering(true);
                        } else {
                            binding.ibRefresh.setVisibility(View.GONE);
                        }
                    } else {
                        binding.setIsBuffering(false);
                        //videoMessage.setVisibility(View.GONE);
                    }
                    old_duration = duration;

                    handler.postDelayed(this, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    private void setToolThread() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (binding.vvPlayer.isPlaying()) {
                    canShowTool = false;
                    hideShowToolBar();
                }
            }
        }, 3000);
    }

    private void hideShowToolBar() {
        ObjectAnimator mover = ObjectAnimator.ofFloat(binding.laytToolbar, "translationY", canShowTool ? 0 : -binding.laytToolbar.getHeight());
        mover.setDuration(300);
        mover.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int currentOrientation = getResources().getConfiguration().orientation;

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {

            Log.v("TAG", "Landscape !!!");
        } else {
            Log.v("TAG", "Portrait !!!");
        }

    }

    private void showDialog(String msg, int alertType) {
        MyDialog dialog = new MyDialog(VideoPlayerActivity.this, this, alertType, msg);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    @Override
    public void onClick(View view) {

        if (view == binding.ibRefresh) {

            if (CommonUtils.isNetworkAvailable(VideoPlayerActivity.this)) {
                canBuffer = true;
                binding.vvPlayer.seekTo(seekTo);
                binding.vvPlayer.start();
                view.setVisibility(View.GONE);
                canShowTool = false;
                hideShowToolBar();
                seekTo = 0;
            } else {
                showDialog("internet error msg", AppConst.AlertType.ERROR);
            }
        }
    }

    @Override
    public void onOkClicked(int msgType, int buttonId) {
        try {
            seekTo = binding.vvPlayer.getCurrentPosition();
            binding.vvPlayer.stopPlayback();
            isShowingDialog = false;
            canShowTool = true;
            canBuffer = false;
            hideShowToolBar();
            binding.ibRefresh.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
