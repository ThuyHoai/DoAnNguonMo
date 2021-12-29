package vn.edu.stu.doannguonmo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText txt_taikhoan, txt_matkhau;
    Button btn_dangnhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addcontrols();
        addevents();
    }

    private void addcontrols() {
        txt_taikhoan = findViewById(R.id.edtDangNhap);
        txt_matkhau = findViewById(R.id.edtPas);
        btn_dangnhap = findViewById(R.id.btnDangNhap);
    }

    private void addevents() {
        btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taikhoan = txt_taikhoan.getText().toString();
                String matkhau = txt_matkhau.getText().toString();
                if (taikhoan.equals("Thuyhoai")) {
                    if (matkhau.equals("123")) {
                        Intent intent = new Intent(MainActivity.this, Trangchu.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "sai mat khau", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}