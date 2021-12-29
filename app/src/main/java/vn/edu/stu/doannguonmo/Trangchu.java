package vn.edu.stu.doannguonmo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import vn.edu.stu.doannguonmo.adapter.listviewadapter;
import vn.edu.stu.doannguonmo.model.trasua;

public class Trangchu extends AppCompatActivity {
    final String DB_PATH_SUFFIX = "/databases/";
    final String DB_NAME = "dbTS.sqlite";
    ListView lv;
    ArrayList<trasua> ds;
    listviewadapter lvadapter;
    FloatingActionButton fabThem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchu);
        copyDbFromAssets();
        addControls();
        addEvents();
        docDssanpham();
    }

    private void docDssanpham() {
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );
        ds.clear();
        Cursor cursor = database.rawQuery("select * from NuocUong", null);
        while (cursor.moveToNext()) {
            int ma=cursor.getInt(0);
            String ten=cursor.getString(1);
            String phanloai=cursor.getString(2);
            byte[] hinhanh=cursor.getBlob(3);
            String gia=cursor.getString(4);
            String soluong=cursor.getString(5);
            trasua ts= new trasua(ma, ten, phanloai, hinhanh,gia, soluong);
            ds.add(ts);
        }
        lvadapter = new listviewadapter(Trangchu.this, ds, R.layout.listview);
        lv.setAdapter(lvadapter);
        lvadapter.notifyDataSetChanged();
        cursor.close();
        database.close();

    }
    private void addControls() {
        lv = findViewById(R.id.lvtrangchu);
        ds = new ArrayList<>();
        fabThem = findViewById(R.id.fabThem);
    }

    private void addEvents() {
        fabThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Trangchu.this, ThemSP.class);
                startActivity(intent);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Trangchu.this, ChiTiet.class);
                intent.putExtra("intentChitiet",ds.get(position));
                startActivity(intent);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Trangchu.this);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có chắc muốn xóa sản phẩm này không?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        trasua ts = ds.get(position);
                        SQLiteDatabase database = openOrCreateDatabase(
                                DB_NAME,
                                MODE_PRIVATE,
                                null
                        );
                        int deletedRowCount = database.delete(
                                "NuocUong",
                                "Ma= ?",
                                new String[]{ts.getMa() + ""}
                        );
                        Toast.makeText(
                                Trangchu.this,
                                "Xóa sản phẩm thành công!!!",
                                Toast.LENGTH_SHORT
                        ).show();
                        docDssanpham();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
    }
    private void copyDbFromAssets() {
        File dbFile = getDatabasePath(DB_NAME);
        if (!dbFile.exists()) {
            try {
                File dbDir = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
                if (!dbDir.exists()) dbDir.mkdir();

                InputStream is = getAssets().open(DB_NAME);
                String outputFilePath = getApplicationInfo().dataDir + DB_PATH_SUFFIX + DB_NAME;
                OutputStream os = new FileOutputStream(outputFilePath);
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (Exception ex) {
                ex.fillInStackTrace();
            }
        }
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
//                Intent about1= new Intent(Trangchu.this, About.class);
//                startActivity(about1);
//                break;
            case R.id.menuListView:
                Intent Listview= new Intent(Trangchu.this,Trangchu.class);
                startActivity(Listview);
                break;
//            case R.id.menuDanhMuc:
//                Intent danhmuc= new Intent(Trangchu.this,DanhMuc.class);
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