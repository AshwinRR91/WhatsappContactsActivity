package com.mystartup.whatsappcontactsactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText userName;
    private EditText password;
    private Button loginButton;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = findViewById(R.id.username_text);
        password = findViewById(R.id.password_text);
        loginButton = findViewById(R.id.login_button);
        signUpButton = findViewById(R.id.sign_up_button);
        loginButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        if(ParseUser.getCurrentUser()!=null){
            switchToWhatsAppContactsActivity();
            finish();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.login_button:
                ParseUser.logInInBackground(userName.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(e!=null){
                            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        else{
                            switchToWhatsAppContactsActivity();
                        }
                    }
                });
                break;
            case R.id.sign_up_button:
                ParseUser parseUser = new ParseUser();
                parseUser.setUsername(userName.getText().toString());
                parseUser.setPassword(password.getText().toString());
                parseUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e!=null){
                            Toast.makeText(MainActivity.this,"Sign Up"+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "You are signed up", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });
                break;
        } }

        public void switchToWhatsAppContactsActivity(){

            Intent intent = new Intent(MainActivity.this, WhatsAppContacts.class);
            startActivity(intent);

        }
}
//TODO onRootLayoutTapped method to be created