package fpl.quangnm.asmand102;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fpl.quangnm.asmand102.dao.NguoiDungDAO;

public class Register_Screen extends AppCompatActivity {
    private NguoiDungDAO nguoiDungDAO;
    EditText edt_cfpassword_res,edt_password_res,edt_fname_res,edt_username_res;
    Button btn_back,btn_res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        edt_username_res = findViewById(R.id.edt_username_res);
        edt_fname_res = findViewById(R.id.edt_fname_res);
        edt_password_res = findViewById(R.id.edt_password_res);
        edt_cfpassword_res = findViewById(R.id.edt_cfpassword_res);
        btn_back = findViewById(R.id.btn_back);
        btn_res = findViewById(R.id.btn_res);
        nguoiDungDAO = new NguoiDungDAO(this);

        btn_back.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(),LogIn_Screen.class));
        });

        btn_res.setOnClickListener(view -> {
            String user = edt_username_res.getText().toString();
            String pass = edt_password_res.getText().toString();
            String cfpass = edt_cfpassword_res.getText().toString();
            String fullname = edt_fname_res.getText().toString();

            if (!pass.equals(cfpass)){
                Toast.makeText(this, "Nhap pass phai trung nhau", Toast.LENGTH_SHORT).show();
            } else {
                boolean check = nguoiDungDAO.Register(user,pass,fullname);
                if (check){
                    Toast.makeText(this, "Dang ki thanh cong", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Dang ki k thanh cong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}