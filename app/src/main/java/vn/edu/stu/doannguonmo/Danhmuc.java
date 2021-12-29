package vn.edu.stu.doannguonmo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import vn.edu.stu.doannguonmo.model.danhmuc;
import vn.edu.stu.doannguonmo.model.trasua;

public class Danhmuc extends AppCompatActivity {
    EditText edtMaDM, edtTenDM;
    Button btn_Them, btn_SuaDM;
    ListView lvTS;
    ArrayList<trasua> trasuaArrayList;
    ArrayAdapter<danhmuc> adapterDM;
    danhmuc dmTS = null;
    final String DB_PATH_SUFFIX = "/databases/";
    final String DB_NAME = "dbTS.sqlite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhmuc);

        trasuaArrayList = new ArrayList<>();
        copyDbFromAssets();
        addControls();
        docTSTuDb();
        docDssanpham();
        addEvents();
    }

    private void docDssanpham() {
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );
        trasuaArrayList.clear();
        Cursor cursor = database.rawQuery("select * from NuocUong", null);
        while (cursor.moveToNext()) {
            int ma = cursor.getInt(0);
            String ten = cursor.getString(1);
            String phanloai = cursor.getString(2);
            byte[] hinhanh = cursor.getBlob(3);
            String gia = cursor.getString(4);
            String soluong = cursor.getString(5);
            trasua ts = new trasua(ma, ten, phanloai, hinhanh, gia, soluong);
            trasuaArrayList.add(ts);
        }
        cursor.close();
        database.close();
    }

    private void docTSTuDb() {
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );
        Cursor cursor = database.rawQuery("Select * From danhmucNU", null);
        adapterDM.clear();
        while (cursor.moveToNext()) {
            String maDM = cursor.getString(0);
            String tenDM = cursor.getString(1);
            adapterDM.add(new danhmuc(maDM, tenDM));
        }
        cursor.close();
        database.close();
        adapterDM.notifyDataSetChanged();
    }

    private void addControls() {
        btn_Them = findViewById(R.id.btn_Them);
        btn_SuaDM = findViewById(R.id.btn_SuaDM);
        edtMaDM = findViewById(R.id.edtMaDM);
        edtTenDM = findViewById(R.id.edtTenDM);
        lvTS = findViewById(R.id.lvTS);
        adapterDM = new ArrayAdapter<>(
                Danhmuc.this,
                android.R.layout.simple_list_item_1
        );
        lvTS.setAdapter(adapterDM);
        registerForContextMenu(lvTS);
    }

    private void addEvents() {
        btn_Them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maDM = edtMaDM.getText().toString();
                String tenDM = edtTenDM.getText().toString();
                danhmuc dm = new danhmuc(maDM, tenDM);
                SQLiteDatabase database = openOrCreateDatabase(
                        DB_NAME,
                        MODE_PRIVATE,
                        null
                );
                ContentValues row = new ContentValues();
                row.put("Ma", dm.getMaDM());
                row.put("Ten", dm.getTenDM());

                long insertedID = database.insert(
                        "danhmucNU",
                        null,
                        row
                );

                Toast.makeText(
                        Danhmuc.this,
                        edtMaDM.getText(),
                        Toast.LENGTH_LONG
                ).show();
                edtMaDM.setText(null);
                edtTenDM.setText(null);
                database.close();
                docTSTuDb();
            }
        });
        lvTS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dmTS = (danhmuc) adapterDM.getItem(position);
                edtMaDM.setText(dmTS.getMaDM());
                edtTenDM.setText(dmTS.getTenDM());
            }
        });
        btn_SuaDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dmTS != null) {
                    String maDM = edtMaDM.getText().toString();
                    String tenDM = edtTenDM.getText().toString();
                    danhmuc danhmuc = new danhmuc(maDM, tenDM);
                    SQLiteDatabase database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
                    ContentValues row = new ContentValues();
//                    row.put("Ma", danhmuc.getMaDM());
                    row.put("Ten", danhmuc.getTenDM());
                    int updatedRowCount = database.update("danhmucNU", row, "Ma = ?", new String[]{dmTS.getMaDM()});
                    Toast.makeText(Danhmuc.this, "Đã cập nhật thành công", Toast.LENGTH_LONG).show();
                    edtMaDM.setText("");
                    edtTenDM.setText("");
                    database.close();
                    docTSTuDb();
                    dmTS = null;
                }
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
        switch (item.getItemId()) {
//            case R.id.menuAbout:
//                Intent about1 = new Intent(
//                        Danhmuc.this,
//                        About.class);
//                startActivity(about1);
//                break;
            case R.id.menuListView:
                Intent Listview = new Intent(Danhmuc.this, Trangchu.class);
                startActivity(Listview);
                break;
            case R.id.menuDanhMuc:
                Intent danhmuc = new Intent(Danhmuc.this, Danhmuc.class);
                startActivity(danhmuc);
                break;
            case R.id.menuExit:
                finishAffinity();
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lvTS) {
            getMenuInflater().inflate(R.menu.menu_phanloai, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        danhmuc dmTS = adapterDM.getItem(index);
        switch (item.getItemId()) {
            case R.id.menuList:
                Intent intent = new Intent(
                        Danhmuc.this,
                        Trangchu.class
                );
                intent.putExtra("key", dmTS);
                startActivity(intent);
                break;
            case R.id.menuDel:
                xoaTS(index);
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private boolean kiemtraTS(danhmuc dmTSD) {
        SQLiteDatabase database = openOrCreateDatabase(
                DB_NAME,
                MODE_PRIVATE,
                null
        );
        int tamp = 0;
        Cursor cursor = database.rawQuery("Select * From NuocUong where PhanLoai = ?", new String[]{dmTSD.getTenDM()});
        while (cursor.moveToNext()) {
            tamp++;
        }
        if (tamp > 0) {
            return true;
        }
        return false;
    }

    private void xoaTS(int index) {
        danhmuc danhmuc = adapterDM.getItem(index);
        if (kiemtraTS(danhmuc)) {
            Toast.makeText(
                    Danhmuc.this,
                    "Có ràng buộc. Không thể xóa!",
                    Toast.LENGTH_LONG
            ).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Danhmuc.this);
            builder.setIcon(android.R.drawable.ic_delete);
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn có chắc muốn xóa sản phẩm này không?");
            builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    danhmuc danhmuc = adapterDM.getItem(index);
                    SQLiteDatabase database = openOrCreateDatabase(
                            DB_NAME,
                            MODE_PRIVATE,
                            null
                    );
                    int deletedRowCount = database.delete(
                            "danhmucNU",
                            "Ma=?",
                            new String[]{danhmuc.getMaDM() + ""}
                    );
                    Toast.makeText(
                            Danhmuc.this,
                            "Xóa Thành Công",
                            Toast.LENGTH_LONG
                    ).show();
                    database.close();
                    docTSTuDb();
                }
            });
            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}