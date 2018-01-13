package com.mvideostreammming.mvideostreammming.utils;

/**
 * Created by adeeb on 1/31/2017.
 */

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import com.mvideostreammming.mvideostreammming.R;
import com.mvideostreammming.mvideostreammming.databinding.CommonAlertBinding;


public class MyDialog extends Dialog implements View.OnClickListener {


    private OnDialogFragmentClickListener listener;
    private String message;
    @AppConst.AlertType
    private int alertType;
    private CommonAlertBinding binding;
    private Context con;


    public MyDialog(Context activity, OnDialogFragmentClickListener listener, int type, String message) {
        super(activity, R.style.full_screen_dialog);
        this.con = activity;
        this.alertType = type;
        this.listener = listener;
        this.message = message;
    }

    @Override
    public void onClick(View view) {

        if (listener != null) {
            listener.onOkClicked(alertType, view.getId());
        }
        dismiss();
    }

    public interface OnDialogFragmentClickListener {
        void onOkClicked(int alertType, int buttonId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.common_alert, null, false);

        switch (alertType) {

            case AppConst.AlertType.SUCCESS:
                binding.setIsSuccess(true);
                binding.laytLogin.setVisibility(View.GONE);
                binding.btnButton.setVisibility(View.VISIBLE);
                break;
            case AppConst.AlertType.NONE:
                binding.setIsSuccess(false);
                binding.laytLogin.setVisibility(View.GONE);
                binding.btnButton.setVisibility(View.VISIBLE);
                break;
            case AppConst.AlertType.SIMPLE:
                binding.setIsSuccess(true);
                binding.title.setVisibility(View.GONE);
                binding.laytLogin.setVisibility(View.GONE);
                binding.btnButton.setVisibility(View.VISIBLE);
                break;
            case AppConst.AlertType.ERROR:
                binding.setIsSuccess(false);
                binding.laytLogin.setVisibility(View.GONE);
                binding.btnButton.setVisibility(View.VISIBLE);
                break;
            case AppConst.AlertType.LOGIN_ERROR:
              //  binding.setIsSuccess(false);
              //  binding.btnButton.setVisibility(View.GONE);
              //  binding.laytLogin.setVisibility(View.VISIBLE);
              //  binding.btnOk.setText(con.getString(R.string.login));
                break;
            case AppConst.AlertType.LOG_OUT:
              //  binding.setIsSuccess(false);
              //  binding.tvMsg.setTextSize(TypedValue.COMPLEX_UNIT_PX, con.getResources().getDimension(R.dimen.big_txt_size));
               // binding.title.setVisibility(View.GONE);
               // binding.btnButton.setVisibility(View.GONE);
              //  binding.laytLogin.setVisibility(View.VISIBLE);
              //  binding.btnOk.setText(con.getString(R.string.log_out));
                break;
            case AppConst.AlertType.GPS_ERROR:
            case AppConst.AlertType.PERMISSION_ERROR:
             //   binding.setIsSuccess(false);
             //   binding.btnButton.setVisibility(View.GONE);
            //    binding.laytLogin.setVisibility(View.VISIBLE);
              //  binding.btnOk.setText(con.getString(R.string.setting));
                break;

        }


        binding.setMessage(message);
        binding.executePendingBindings();
        setContentView(binding.getRoot());

        binding.btnButton.setOnClickListener(this);
        binding.btnOk.setOnClickListener(this);
        binding.btnSkip.setOnClickListener(this);

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}

