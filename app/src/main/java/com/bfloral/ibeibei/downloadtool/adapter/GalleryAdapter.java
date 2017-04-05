package com.bfloral.ibeibei.downloadtool.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bfloral.ibeibei.downloadtool.R;
import com.bfloral.ibeibei.downloadtool.domain.FilesBean;
import com.bfloral.ibeibei.downloadtool.util.file.ImageCallback;
import com.bfloral.ibeibei.downloadtool.util.file.FileUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MingLi on 10/24/16.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>{

    public interface onItemClickListener{
        void onItemClick(View view, int position);
    }
    private onItemClickListener onItemClickListener;

    public void setOnItemClickListener(GalleryAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private LayoutInflater mInflater;
    private Context mContext;
    private int dp;
    private List<FilesBean> filesBeanList;

    public GalleryAdapter(Context context,List<FilesBean> filesBeanList){
        this(context,filesBeanList,200);
    }

    public GalleryAdapter(Context context,List<FilesBean> filesBeanList,int dp){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.filesBeanList = filesBeanList;
        this.dp = dp;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_image,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setEnabled(false);
        setImage(mContext,null,dp,holder.mImg);
        FileUtils.loadImage(mContext,holder.mImg,filesBeanList.get(holder.getLayoutPosition()), new ImageCallback() {
            @Override
            public void imageLoadedOk(Bitmap bitmap) {
                setImage(mContext,bitmap,dp,holder.mImg);
                holder.itemView.setEnabled(true);
            }

            @Override
            public void imageLoadedFail(String message) {
                holder.itemView.setEnabled(false);
            }
        });

        if (onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v,holder.getLayoutPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return filesBeanList == null ? 0 : filesBeanList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_item_images)
        ImageView mImg;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
    public void updateDataSource(int position){
        notifyItemChanged(position);
    }

    public static void setImage(Context context,Bitmap bitmap,int dp,ImageView imageView){
        if (bitmap == null){
            bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.placeholder);
        }
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int height = dp2px(context,dp);
        float scale = originalHeight/(float)height;
        float width = originalWidth/scale;
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = (int)height;
        layoutParams.width = (int)width;
        imageView.setLayoutParams(layoutParams);
        imageView.setImageBitmap(bitmap);
    }

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources()
                .getDisplayMetrics());
    }
}
