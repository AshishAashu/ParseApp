package com.ashish.parseapp;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText et_user_name, et_user_email, et_user_pass;
    TextView tv_message;
    TextInputLayout input_layout_user_pass;
    Button signup_button,signout_button;
    HashMap<String,String> object;
    JSONObject data;
    ParseUser parse_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setResources();
    }

    private void setResources() {
        object = new HashMap<String, String>();
        input_layout_user_pass = (TextInputLayout) findViewById(R.id.input_layout_user_password);
        et_user_name = (EditText) findViewById(R.id.input_user_name);
        et_user_email = (EditText) findViewById(R.id.input_user_email);
        et_user_pass = (EditText) findViewById(R.id.input_user_password);
        tv_message = (TextView) findViewById(R.id.tv_message);
        signout_button = (Button) findViewById(R.id.signout_button);
        signup_button = (Button) findViewById(R.id.signup_button);

        setPasswordListener();
        setSignupButtonListener();
        setSignoutButtonListener();
    }

    private void setSignoutButtonListener() {
        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parse_user = ParseUser.getCurrentUser();
                if(parse_user!=null) {
                    try {
                        data = new JSONObject();
                        try {
                            data.put("alert", "Bye Bye " + parse_user.getUsername() + "!");
                            data.put("title",
                                    "ParseApp Notify!");
                        } catch (JSONException exp) {
                            throw new IllegalArgumentException("unexpected parsing error", exp);
                        }
                        ParsePush push = new ParsePush();
                        push.setData(data);
                        push.sendInBackground();
                        parse_user.logOut();
                        tv_message.setText("");
                        tv_message.setVisibility(View.GONE);
                        et_user_email.setText("");
                        et_user_name.setText("");
                        et_user_pass.setText("");
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No User Loggedin.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setSignupButtonListener() {
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Signup clicked.", Toast.LENGTH_SHORT).show();
                if (validatedAndAddedData()) {
                    parse_user = new ParseUser();
                    parse_user.setUsername(object.get("user_name"));
                    parse_user.setEmail(object.get("user_email"));
                    parse_user.setPassword(object.get(("user_pass")));
                    parse_user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                tv_message.setVisibility(View.VISIBLE);
                                tv_message.setText("Sucessful Sign Up");
                                data = new JSONObject();
                                try {
                                    data.put("alert", "Thank "+object.get("user_name").toString()+"! for Signup.");
                                    data.put("title",
                                            "ParseApp Notify!");
                                } catch ( JSONException exp) {
                                    // should not happen
                                    throw new IllegalArgumentException("unexpected parsing error", exp);
                                }
                                ParsePush push = new ParsePush();
//                                push.setChannel("News");
                                push.setData(data);
                                push.sendInBackground();
                            } else {
                                if(e.getMessage().equals("Account already exists for this username.")) {
                                    Toast.makeText(getApplicationContext(), "Logging In...", Toast.LENGTH_LONG).show();
                                    userLogin();
                                }else{
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "Input is missing.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void userLogin() {
        ParseUser.logInInBackground(object.get("user_name").toString(),
                                    object.get("user_pass").toString(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    data = new JSONObject();
                    try {
                        data.put("alert", "Welcome Back "+object.get("user_name").toString());
                        data.put("title", "ParseApp Notify!");
                    } catch ( JSONException exp) {
                        // should not happen
                        throw new IllegalArgumentException("unexpected parsing error", exp);
                    }
                    ParsePush push = new ParsePush();
//                    push.setChannel("News");
                    push.setData(data);
                    push.sendInBackground();

                } else {
                    ParseUser.logOut();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validatedAndAddedData() {
        String u_name = et_user_name.getText().toString().trim();
        String u_email = et_user_email.getText().toString().trim();
        String u_pass = et_user_pass.getText().toString().trim();
//        Toast.makeText(getApplicationContext(), u_name +":"+u_email, Toast.LENGTH_LONG).show();
        boolean valid = true;
        if (u_name == null || u_name.length()==0)
            valid = false;
        if (u_email == null || u_email.length() == 0)
            valid = false;
        if (u_pass == null || u_pass.length() == 0 )
            valid = false;
        if (valid) {
            try {
                object.put("user_name", u_name);
                object.put("user_email", u_email);
                object.put("user_pass", u_pass);
            } catch (Exception e) {
                Log.d("Error:", e.toString());
            }
        }
        return valid;
    }

    private void setPasswordListener() {
        et_user_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    input_layout_user_pass.setErrorEnabled(true);
                    if (et_user_pass.getText().toString().length() != input_layout_user_pass.getCounterMaxLength()) {
                        input_layout_user_pass.setError("Password should be length of : " + input_layout_user_pass.getCounterMaxLength());
                    }
                } else if (et_user_pass.getText().toString().length() == 0) {
                    input_layout_user_pass.setErrorEnabled(false);
                }
            }
        });
    }
}
