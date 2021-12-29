package vn.edu.stu.doannguonmo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import vn.edu.stu.doannguonmo.model.trasua;

public class ChiTiet extends AppCompatActivity {
    TextView txt_Ten,txt_Gia, txt_PL, txt_SL;
    ImageView img_HA;
    final int REQUEST_CODE_GALLER = 999;
    final  String DB_PATH_SUFFIX = "/databases/";
    final String DB_NAME = "dbTS.sqlite";
    trasua TS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        addcontrols();
        addevents();
        Intent intentChitiet = getIntent();
        TS =(trasua) intentChitiet.getSerializableExtra ("intentChitiet");
        byte[] img = TS.getHinhanh();

        Bitmap bitmap = BitmapFactory.decodeByteArray(img,0, img.length);
        txt_Ten.setText((TS.getTen().toString()));
        txt_PL.setText((TS.getPhanloai().toString()));
        img_HA.setImageBitmap(bitmap);
        txt_Gia.setText((TS.getGia().toString()));
        txt_SL.setText((TS.getSoluong().toString()));
    }

    private void addcontrols() {
        txt_Ten = findViewById(R.id.txt_Ten);
        txt_PL = findViewById(R.id.txt_PL);
        img_HA = findViewById(R.id.img_HA);
        txt_Gia = findViewById(R.id.txt_Gia);
        txt_SL = findViewById(R.id.txt_SL);
    }

    private void addevents() {
    }
    private void readDL(trasua ts1) {
        SQLiteDatabase database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        ContentValues dulieuDong = new ContentValues();
        dulieuDong.put("ten", ts1.getTen());
        dulieuDong.put("phanloai", ts1.getPhanloai());
        dulieuDong.put("hinhanh", ts1.getHinhanh());
        dulieuDong.put("gia", ts1.getGia());
        dulieuDong.put("soLuong", ts1.getSoluong());
        int capnhatDong = database.update("NuocUong", dulieuDong, "Ma = ?", new String[]{ts1.getMa() + ""});
        database.close();
    }
    public static final int PICK_NAME = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLER) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img_HA.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_GALLER) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLER);
            } else {
                Toast.makeText(getApplicationContext(), "Đã từ chối quyền", Toast.LENGTH_LONG).show();
            }
        }
    }
    private byte[] imageViewTOByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
//            case R.id.menuAbout:
//                Intent about1= new Intent(
//                        ChiTiet.this,
//                        About.class);
//                startActivity(about1);
//                break;
            case R.id.menuListView:
                Intent Listview= new Intent(ChiTiet.this,Trangchu.class);
                startActivity(Listview);
                break;
//            case R.id.menuDanhMuc:
//                Intent danhmuc= new Intent(ChiTiet.this,DanhMuc.class);
//                startActivity(danhmuc);
//                break;
            case R.id.menuExit:
                finishAffinity();
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}