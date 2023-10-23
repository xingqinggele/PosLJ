package com.example.poslj.newprojectview.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.poslj.R;
import com.example.poslj.newprojectview.bean.ImageGridBean;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/22
 * 描述:图片上传Adapter
 */
public class ImageGridAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<ImageGridBean> imageBeanList;
    private Context context;
    private OnImageClickListener onImageClickListener;

    public ImageGridAdapter(Context context, List<ImageGridBean> imageBeanList) {
        this.imageBeanList = imageBeanList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imageBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoldr viewHoldr = null;
        if (convertView == null) {
            viewHoldr = new ViewHoldr();
            convertView = layoutInflater.inflate(R.layout.item_newspaper_grid_view, null);
            viewHoldr.image_name = convertView.findViewById(R.id.image_name);
            viewHoldr.simple_iv = convertView.findViewById(R.id.simple_iv);
            viewHoldr.clear_iv = convertView.findViewById(R.id.clear_iv);
            convertView.setTag(viewHoldr);
        } else {
            viewHoldr = (ViewHoldr) convertView.getTag();
        }
        viewHoldr.image_name.setText(imageBeanList.get(position).getImagename());
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(context.getResources())
                .setPlaceholderImage(imageBeanList.get(position).getPlaceholderimage())
                .build();
        viewHoldr.simple_iv.setHierarchy(hierarchy);
        if (!TextUtils.isEmpty(imageBeanList.get(position).getUrl())) {
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_FILE_SCHEME)
                    .path(imageBeanList.get(position).getUrl())
                    .build();
            viewHoldr.simple_iv.setImageURI(uri);
        }else {
            viewHoldr.simple_iv.setImageURI("");
        }

        if (TextUtils.isEmpty(imageBeanList.get(position).getUrl())) {
            viewHoldr.clear_iv.setVisibility(View.GONE);
        } else {
            viewHoldr.clear_iv.setVisibility(View.VISIBLE);
        }

        viewHoldr.simple_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageClickListener.onItemClick(position);
            }
        });
        viewHoldr.clear_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBeanList.get(position).setUrl(null);
                imageBeanList.get(position).setNeturl(null);
                renewal();
            }
        });
        return convertView;
    }


    //实体类
    private static class ViewHoldr {
        private TextView image_name;
        private SimpleDraweeView simple_iv;
        private ImageView clear_iv;
    }

    //点击图片按钮
    public static interface OnImageClickListener {
        public void onItemClick(int position); //传递boolean类型数据给activity
    }

    /**
     * 点击图片入口
     *
     * @param onImageClickListener
     */
    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }


    //更新adapter
    public void renewal() {
        notifyDataSetChanged();
    }
}
