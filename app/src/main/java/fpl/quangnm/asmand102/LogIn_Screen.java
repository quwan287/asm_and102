package fpl.quangnm.asmand102;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fpl.quangnm.asmand102.dao.NguoiDungDAO;

public class LogIn_Screen extends AppCompatActivity {
    private NguoiDungDAO dao;
    TextView txt_Login_Signup,txt_Login;
    EditText edt_password_login,edt_username_login;
    Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);
        edt_username_login = findViewById(R.id.edt_username_login);
        edt_password_login = findViewById(R.id.edt_password_login);
        btn_login = findViewById(R.id.btn_login);
        txt_Login = findViewById(R.id.txt_Login);
        txt_Login_Signup = findViewById(R.id.txt_Login_Signup);
        dao = new NguoiDungDAO(LogIn_Screen.this);

        btn_login.setOnClickListener(view -> {
            String user = edt_username_login.getText().toString();
            String pass = edt_password_login.getText().toString();

            boolean check = dao.CheckLogin(user,pass);

            if (check){
                Toast.makeText(this, "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LogIn_Screen.this,Home_Screen.class));
            } else {
                Toast.makeText(this, "Vui long ktra lai", Toast.LENGTH_SHORT).show();
            }
        });
        txt_Login_Signup.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(),Register_Screen.class));
        });
    }
}