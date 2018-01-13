package com.mvideostreammming.mvideostreammming.utils;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AppConst {






    @IntDef({AlertType.NONE, AlertType.SIMPLE, AlertType.SUCCESS, AlertType.ERROR, AlertType.LOGIN_ERROR, AlertType.PERMISSION_ERROR, AlertType.GPS_ERROR, AlertType.LOG_OUT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AlertType {
        int NONE = 0;
        int SUCCESS = 1;
        int ERROR = 2;
        int LOGIN_ERROR = 3;
        int PERMISSION_ERROR = 4;
        int GPS_ERROR = 5;
        int LOG_OUT = 6;
        int SIMPLE = 7;
    }


}

