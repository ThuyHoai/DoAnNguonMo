package vn.edu.stu.doannguonmo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import vn.edu.stu.doannguonmo.model.trasua;

public class ThemSP extends AppCompatActivity {
    EditText txt_ten, txt_gia, txt_Phanloai, txt_soluong;
    Button btn_TroLai, btn_them, btn_tailen;
    ImageView imghinh;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect;
    private ImageView ivImage;
    private String userChoosenTask;
    final int REQUEST_CODE_GALLERY = 999;
    private Intent data;
    final String DB_PATH_SUFFIX = "/databases/";
    final String DB_NAME = "dbTS.sqlite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sp);
        addcontrols();
        addevents();
    }

    private void docDLDB(trasua ts) {
        SQLiteDatabase docDl = openOrCreateDatabase(DB_NAME,MODE_PRIVATE,null);
        ContentValues Cdulieu = new ContentValues();
        Cdulieu.put("ten",ts.getTen());
        Cdulieu.put("phanloai",ts.getPhanloai());
        Cdulieu.put("hinhanh",ts.getHinhanh());
        Cdulieu.put("gia",ts.getGia());
        Cdulieu.put("soluong",ts.getSoluong());
        long id = docDl.insert("NuocUong",null,Cdulieu);
        docDl.close();
    }

    private void addcontrols() {
        txt_ten=findViewById(R.id.txt_Ten);
        txt_gia=findViewById(R.id.txt_Gia);
        txt_Phanloai=findViewById(R.id.txt_phanloai);
        txt_soluong=findViewById(R.id.txt_soluong);
        imghinh=findViewById(R.id.img_hinh);
        btn_them=findViewById(R.id.btn_Them);
        btn_tailen=findViewById(R.id.btn_TaiLen);
        btn_TroLai=findViewById(R.id.btn_TroLai);


    }
    private void addevents() {
        btn_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten=txt_ten.getText().toString();
                String gia=txt_gia.getText().toString();
                String soluong =txt_soluong.getText().toString();
                String phanloai = txt_Phanloai.getText().toString();
                trasua ts1=new trasua(1,ten,phanloai,imageViewTOByte(imghinh),gia, soluong);
                Toast.makeText(ThemSP.this, "đã thêm sản phẩm", Toast.LENGTH_SHORT).show();
                docDLDB(ts1);
            }
        });
        btn_tailen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        ThemSP.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });
        btn_TroLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThemSP.this, Trangchu.class);
                startActivity(intent);
            }
        });
    }
    public static final int PICK_IMAGE = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_GALLERY){
            Uri uri = data.getData();
            try{
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imghinh.setImageBitmap(bitmap);
            }catch (FileNotFoundException ex){
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                android.content.Intent intent =  new Intent(android.content.Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else{
                Toast.makeText(getApplicationContext(), "Đã từ chối quyền", Toast.LENGTH_LONG).show();
            }
        }
    }
    private byte[] imageViewTOByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
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
//                        ThemSP.this,
//                        About.class);
//                startActivity(about1);
//                break;
            case R.id.menuListView:
                Intent Listview= new Intent(ThemSP.this,Trangchu.class);
                startActivity(Listview);
                break;
//            case R.id.menuDanhMuc:
//                Intent danhmuc= new Intent(ThemSP.this,DanhMuc.class);
//                startActivity(danhmuc);
//            break;
            case R.id.menuExit:
                finishAffinity();
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}