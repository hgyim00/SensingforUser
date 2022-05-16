package com.android.sensingforuser;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    private ImageView sensingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        sensingBtn = (ImageView) findViewById(R.id.sensingButton);
    }




    //센싱 버튼 클릭
    public void mOnSensing(View view){
        //예시. 버튼 눌렀을 때 TextView 바꾸기
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("위치").setMessage("당신은 ~에 있습니다");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //TODO: 센싱 후 textView를 위치 측정한 장소로 바꾸기.
    }
}