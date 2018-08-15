package com.ashish.parseapp.compose;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ashish.parseapp.R;

public class Post extends AppCompatActivity {
    EditText input_user_post;
    Button user_post_submit_button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_post);
        setResourses();
    }

    private void setResourses() {
        input_user_post = (EditText) findViewById(R.id.input_user_post);
        user_post_submit_button = (Button) findViewById(R.id.user_post_submit_button);
        setUserPostSubmitButtonListener();
    }

    private void setUserPostSubmitButtonListener() {
        user_post_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input_user_post.getText().toString().length()>=10){
                    Toast.makeText(Post.this,"Posting...", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Post.this,"Minimum Post length is 10.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
