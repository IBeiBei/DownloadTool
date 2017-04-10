package com.bfloral.ibeibei.downloadtool;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bfloral.ibeibei.downloadtool.data.api.BaseUrl;
import com.bfloral.ibeibei.downloadtool.domain.FilesBean;
import com.bfloral.ibeibei.downloadtool.util.file.FileCallback;
import com.bfloral.ibeibei.downloadtool.util.file.FileUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoFileActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.tv_filename)
    TextView tv_filename;
    public static ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_file_download);
        ButterKnife.bind(this);
        tv_filename.setOnClickListener(this);
        BaseUrl.url="https://github.com";
    }
    public void getFile(Context context,final FilesBean filesBean){
        setLoadingIndicator(true);
        FileUtils.loadFile(context,filesBean, new FileCallback() {
            @Override
            public void loadFileOk(File file) {
                setLoadingIndicator(false);
                openFile(file,filesBean);
            }

            @Override
            public void loadFileFail(String message) {
                setLoadingIndicator(false);
            }
        });
    }
    public void setLoadingIndicator(boolean active) {
        if (active) {
            showProgressDialog(this, getResources().getString(R.string.loading));
        } else {
            dismissDialog();
        }
    }
    public void showProgressDialog(Context context, String message) {
        try{
            if (dialog == null || !dialog.isShowing()) {
                dialog = ProgressDialog.show(context, "", message);
                dialog.setIndeterminate(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
            }
        }catch (Exception e){
        }
    }

    public void dismissDialog() {
        try{
            if(dialog!=null){
                dialog.dismiss();
                dialog = null;
            }
        }catch (Exception e){
        }
    }
    public void openFile(File file, FilesBean filesBean) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = FileUtils.getMIMEType(filesBean.getFileName());
        intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        FilesBean filesBean = new FilesBean();
        filesBean.setFileName("java.docx");
        filesBean.setUrl("https://github.com/IBeiBei/DownloadTool/blob/demo-download-file/app/src/main/res/raw/java.docx");
        getFile(this,filesBean);
    }
}
