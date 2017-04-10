package com.bfloral.ibeibei.downloadtool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bfloral.ibeibei.downloadtool.adapter.GalleryAdapter;
import com.bfloral.ibeibei.downloadtool.data.api.BaseUrl;
import com.bfloral.ibeibei.downloadtool.domain.FilesBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Example activity
 */
public class DemoImageActivity extends AppCompatActivity {
    private GalleryAdapter galleryAdapter;
    private List<FilesBean> filesBeanList;
    @BindView(R.id.rv_images)
    RecyclerView rv_images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_image);
        ButterKnife.bind(this);
        //init baseUrl
        BaseUrl.url = "https://github.com";
        //init data
        filesBeanList = new ArrayList<>();
        FilesBean filesBean = new FilesBean();
        filesBean.setUrl("https://github.com/IBeiBei/DownloadTool/blob/master/app/src/main/res/raw/1.jpg");
        FilesBean filesBean1 = new FilesBean();
        filesBean1.setUrl("https://github.com/IBeiBei/DownloadTool/blob/master/app/src/main/res/raw/2.jpg");
        FilesBean filesBean2 = new FilesBean();
        filesBean2.setUrl("https://github.com/IBeiBei/DownloadTool/blob/master/app/src/main/res/raw/3.jpg");
        FilesBean filesBean3 = new FilesBean();
        filesBean3.setUrl("https://github.com/IBeiBei/DownloadTool/blob/master/app/src/main/res/raw/4.jpg");
        filesBeanList.add(filesBean);
        filesBeanList.add(filesBean1);
        filesBeanList.add(filesBean2);
        filesBeanList.add(filesBean3);

        //init adapter
        galleryAdapter = new GalleryAdapter(this, filesBeanList);
        galleryAdapter.setOnItemClickListener(new GalleryAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageView imageView = (ImageView) view.findViewById(R.id.iv_item_images);
                showPopupWindow(imageView, filesBeanList.get(position));
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_images.setLayoutManager(linearLayoutManager);
        rv_images.setItemAnimator(new DefaultItemAnimator());
        rv_images.setAdapter(galleryAdapter);
    }

    public void showPopupWindow(ImageView view, FilesBean filesBean) {
        Intent intent = PhotoViewerActivity.newIntent(this, filesBean);
        try {
            startActivity(intent);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            startActivity(intent);
        }
    }
}
