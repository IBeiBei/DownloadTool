package com.bfloral.ibeibei.downloadtool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bfloral.ibeibei.downloadtool.domain.FilesBean;
import com.bfloral.ibeibei.downloadtool.util.ScreenUtil;
import com.bfloral.ibeibei.downloadtool.util.file.FileUtils;
import com.bfloral.ibeibei.downloadtool.util.file.ImageCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by MingLi on 11/15/16.
 */

public class PhotoViewerActivity extends AppCompatActivity {
    private static final String TAG = "PhotoViewerActivity";
    @BindView(R.id.iv_big_image)
    ImageView iv_big_image;
    public static final String EXTRA_FILESBEAN = "extra_filesbean";
    public static final String TRANSIT_PIC = "picture";
    private FilesBean filesBean;
    private PhotoViewAttacher mAttacher;
    public  ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        ButterKnife.bind(this);
        parseIntent();
        ViewCompat.setTransitionName(iv_big_image, TRANSIT_PIC);

        if (filesBean != null) {
            showProgressDialog(this, getResources().getString(R.string.loading));
            FileUtils.loadImage(this,ScreenUtil.getScreenWidth(this), filesBean, new ImageCallback() {
                @Override
                public void imageLoadedOk(Bitmap bitmap) {
                    dismissDialog();
                    mAttacher = new PhotoViewAttacher(iv_big_image);
                    iv_big_image.setImageBitmap(bitmap);
                    mAttacher.update();
                    mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                        @Override
                        public void onPhotoTap(View view, float v, float v1) {
                            if (mAttacher != null)
                                mAttacher.cleanup();
                            onBackPressed();
                        }

                        @Override
                        public void onOutsidePhotoTap() {
                            if (mAttacher != null)
                                mAttacher.cleanup();
                            onBackPressed();
                        }
                    });
                }

                @Override
                public void imageLoadedFail(String message) {
                    dismissDialog();
                }
            });
        }
    }

    public static Intent newIntent(Context context, FilesBean filesBean) {
        Intent intent = new Intent(context, PhotoViewerActivity.class);
        intent.putExtra(EXTRA_FILESBEAN, filesBean);
        return intent;
    }

    private void parseIntent() {
        filesBean = getIntent().getParcelableExtra(EXTRA_FILESBEAN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAttacher != null)
            mAttacher.cleanup();
        dismissDialog();
    }
    public void showProgressDialog(Activity activity, String message) {
        try{
            if(activity == null || activity.isFinishing() ){
                return;
            }
            if (mProgressDialog == null || !mProgressDialog.isShowing()) {
                mProgressDialog = ProgressDialog.show(activity, "", message);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setCancelable(false);
            }
        }catch (Exception e){
            Log.d(TAG, "showProgressDialog error ", e);
        }
    }
    public void dismissDialog() {
        try{
            if(mProgressDialog!=null){
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }catch (Exception e){
            Log.d(TAG, "cancelDialog error ", e);
        }
    }
}
