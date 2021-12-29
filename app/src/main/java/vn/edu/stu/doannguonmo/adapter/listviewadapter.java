package vn.edu.stu.doannguonmo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.stu.doannguonmo.ChiTiet;
import vn.edu.stu.doannguonmo.R;
import vn.edu.stu.doannguonmo.Sua;
import vn.edu.stu.doannguonmo.model.trasua;

public class listviewadapter extends BaseAdapter {
    public listviewadapter(Context context, ArrayList<trasua> traSua, int layout) {
        this.context = context;
        this.traSua = traSua;
        this.layout = layout;

        final  String DB_PATH_SUFFIX = "/databases/";
        final String DB_NAME = "dbTS.sqlite";
    }

    Context context;
    private ArrayList<trasua>traSua;
    int layout;

    @Override
    public int getCount() {
        return traSua.size();
    }

    @Override
    public Object getItem(int position) {
        return traSua.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);
        TextView txtName = convertView.findViewById(R.id.tvTenSP);
        TextView txtGia = convertView.findViewById(R.id.tvGia);
        ImageView imgHinh = convertView.findViewById(R.id.imgSanPham);
        trasua trasua = traSua.get(position);
        txtName.setText(trasua.getTen());
        txtGia.setText(String.valueOf(trasua.getGia()));
        byte[] img = trasua.getHinhanh();
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        imgHinh.setImageBitmap(bitmap);

        ImageButton imgSua = convertView.findViewById(R.id.imgSua);
        imgSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        context,
                        Sua.class
                );
                intent.putExtra("intentChitiet",traSua.get(position));
                context.startActivity(intent);
            }
        });
        ImageView imgSanPham = convertView.findViewById(R.id.imgSanPham);
        imgSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        context,
                        ChiTiet.class
                );
                intent.putExtra("intentChitiet",traSua.get(position));
                context.startActivity(intent);
            }
        });
        imgSanPham.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        return convertView;
    }
}
