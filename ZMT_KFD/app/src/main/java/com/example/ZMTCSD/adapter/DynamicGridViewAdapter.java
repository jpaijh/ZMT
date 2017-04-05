package com.example.ZMTCSD.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.ZMTCSD.R;
import com.example.ZMTCSD.view.dynamicgridview.BaseDynamicGridAdapter;
import java.util.List;

import static android.R.attr.id;

/**
 * 首页：DynamicGridView的数据适配器
 * Created by 赵 鑫 on 2015/8/25.
 */
public class DynamicGridViewAdapter extends BaseDynamicGridAdapter {
    public DynamicGridViewAdapter(Context context, List<ItemHomeGridView> items, int columnCount) {
        super(context, items, columnCount);
    }

    public static class ItemHomeGridView {
        private String PermissionId;
        private String textRes; // 标题资源
        private int imgRes; // 图片资源

        public ItemHomeGridView(String PermissionId,String textRes, int imgRes) {
            this.PermissionId=PermissionId;
            this.imgRes = imgRes;
            this.textRes = textRes;
        }

        public String getPermissionId() {
            return PermissionId;
        }

        public void setPermissionId(String permissionId) {
            PermissionId = permissionId;
        }

        public int getImgRes() {
            return imgRes;
        }

        public void setImgRes(int imgRes) {
            this.imgRes = imgRes;
        }

        public String getTextRes() {
            return textRes;
        }

        public void setTextRes(String textRes) {
            this.textRes = textRes;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_home_gridview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ItemHomeGridView item = (ItemHomeGridView) getItem(position);
        holder.build(item.getTextRes(), item.getImgRes());
        return convertView;
    }

    private class ViewHolder {
        private TextView titleText;
        private ImageView image;
//        private BadgeView badge;

        private ViewHolder(View view) {
            titleText = (TextView) view.findViewById(R.id.item_title);
            image = (ImageView) view.findViewById(R.id.item_img);
//            badge = new BadgeView(getContext(), image); //设置图片上的数字
//            badge.setBadgeBackgroundColor(Color.YELLOW); //设置背景颜色
//            badge.setTextColor(Color.BLACK);
        }

        void build(String textRes, int imgRes) {
            titleText.setText(textRes);
            image.setImageResource(imgRes);
//            badge.setText("5");
//            badge.show();//向设置几条未读的信息
        }
    }
}
