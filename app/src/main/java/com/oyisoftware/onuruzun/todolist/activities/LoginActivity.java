package com.oyisoftware.onuruzun.todolist.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oyisoftware.onuruzun.todolist.R;
import com.oyisoftware.onuruzun.todolist.database.DatabaseHelper;
import com.oyisoftware.onuruzun.todolist.model.User;

import java.util.List;


import static com.oyisoftware.onuruzun.todolist.database.DatabaseHelper.INTENT_USER_ID;

public class LoginActivity extends AppCompatActivity {

    private final AppCompatActivity activity = LoginActivity.this;
    EditText email,pswd;
    Button login;
    DatabaseHelper db;
    Button registertxt;
    List<User> userinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db=new DatabaseHelper(LoginActivity.this);
        email=(EditText)findViewById(R.id.uname);
        pswd=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);
        registertxt=(Button)findViewById(R.id.button);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!email.getText().toString().isEmpty() &&!pswd.getText().toString().isEmpty() ) {

                    String email_login = email.getText().toString();
                    String password_login = pswd.getText().toString();

                    if (db.check_User(email_login, password_login) == false) {
                        Toast.makeText(LoginActivity.this, "User Does Not Exist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "User Exist " + email_login, Toast.LENGTH_SHORT).show();


                        userinfo = db.getUser(email_login);

                        Intent intent = new Intent(activity, ToDoActivity.class);
                        intent.putExtra(INTENT_USER_ID, userinfo.get(0).getId());
                        activity.startActivity(intent);
                    }
                }
                else {

                    Toast.makeText(LoginActivity.this,"Please enter the all fields",Toast.LENGTH_SHORT).show();
                }
            }
        });


        registertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Intent intent = new Intent(activity, RegisterActivity.class);
                    activity.startActivity(intent);

            }
        });
    }



}
