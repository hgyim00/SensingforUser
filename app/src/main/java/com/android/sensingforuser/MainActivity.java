package com.android.sensingforuser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    private Button sensingBtn;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        sensingBtn = (Button) findViewById(R.id.sensingBtn);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("내 위치: ");
    }


    //센싱 버튼 클릭
    public void mOnSensing(View v){
        //예시. 버튼 눌렀을 때 TextView 바꾸기
        textView.setText("내 위치: " + " blabla...");


        //TODO: 센싱 후 textView를 위치 측정한 장소로 바꾸기.
    }
}