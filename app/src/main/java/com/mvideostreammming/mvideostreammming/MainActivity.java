package com.mvideostreammming.mvideostreammming;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=new Intent(getApplicationContext(),VideoPlayerActivity.class);
//http://83.111.135.5:8080/uaecompanies/Video_Videos/VAT-Video-Final.mp4
       // intent.putExtra("videoUrl","rtsp://ss1c6.idc.mundu.tv:554/prf0/cid_4.sdp");
        intent.putExtra("videoUrl","http://83.111.135.5:8080/uaecompanies/Video_Videos/VAT-Video-Final.mp4");
        startActivity(intent);
    }
}
