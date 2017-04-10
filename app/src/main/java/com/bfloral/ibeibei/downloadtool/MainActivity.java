package com.bfloral.ibeibei.downloadtool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.tv_demo_image)
    TextView tv_image;
    @BindView(R.id.tv_demo_file)
    TextView tv_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tv_image.setOnClickListener(this);
        tv_file.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.tv_demo_image:
                Intent intent = new Intent(MainActivity.this,DemoImageActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_demo_file:
                Intent intent1 = new Intent(MainActivity.this,DemoFileActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }
}
