package com.oyisoftware.onuruzun.todolist.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oyisoftware.onuruzun.todolist.R;
import com.oyisoftware.onuruzun.todolist.database.DatabaseHelper;
import com.oyisoftware.onuruzun.todolist.model.User;

public class RegisterActivity extends AppCompatActivity{

    private EditText username,password,email;
    private DatabaseHelper databaseHelper;
    private Button btnReg;
    private TextView txtLoginPage;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username=(EditText)findViewById(R.id.reg_uname);
        password=(EditText)findViewById(R.id.reg_password);
        email=(EditText)findViewById(R.id.reg_email);
        btnReg=(Button)findViewById(R.id.butonRegister);
        txtLoginPage=(TextView)findViewById(R.id.linkLogin);
        databaseHelper=new DatabaseHelper(RegisterActivity.this);
        user = new User();

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!email.getText().toString().isEmpty() &&!email.getText().toString().isEmpty() && !email.getText().toString().isEmpty() ) {

                    if (!databaseHelper.check_User(email.getText().toString())) {

                        user.setUsername(username.getText().toString().trim());
                        user.setEmail(email.getText().toString().trim());
                        user.setPassword(password.getText().toString().trim());

                        databaseHelper.create_user(user);

                        Toast.makeText(RegisterActivity.this,getString(R.string.success_message),Toast.LENGTH_SHORT).show();
                        emptyInputEditText();

                        finish();


                    } else {

                        Toast.makeText(RegisterActivity.this,getString(R.string.error_email_exists),Toast.LENGTH_SHORT).show();
                    }

                }

                else {

                    Toast.makeText(RegisterActivity.this,"Please enter the all fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
        txtLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

    private void emptyInputEditText() {
        username.setText(null);
        password.setText(null);
        email.setText(null);
    }
}
