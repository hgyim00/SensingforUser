package com.android.sensingforuser;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String myToken = "ya29.a0ARrdaM_o4t2_T0L0alcejfXbAyZmhbBTOGYKuDbHCgDbDLte2V7woYQy0EpOrghcwfLRF8O7B8EwI9m04eirx5M2vdQRFjf4o01tv88186PiXpPXwVPrQKGeLo0hFi-J8Q60F4sh0iYgFladhnEXbxf7ayEzdCN-3Dbzog";
    private ImageView sensingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        sensingBtn = (ImageView) findViewById(R.id.sensingButton);

        sensingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    try {
                        apiTestMethod();
                    } catch (Exception e) {
                        Log.d("http", e.toString());

                    }
                }).start();
            }
        });
    }



    public void apiTestMethod() throws IOException, JSONException {
        URL url = new URL("https://ml.googleapis.com/v1/projects/wifi-indoor-positioning-351013/models/SecondModel/versions/tutorial1:predict");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + myToken );
        conn.setRequestProperty("Content-Type", "application/json");


        JSONArray arr1 = new JSONArray();
        arr1.put(0.3);
        arr1.put(0.4);
        arr1.put(0.5);
        arr1.put(0.5);

        JSONArray arr2 = new JSONArray();
        arr2.put(0.1);
        arr2.put(0.2);
        arr2.put(0.3);
        arr2.put(0.3);

        JSONArray obj = new JSONArray();

        obj.put(arr1);
        obj.put(arr2);

        JSONObject fin = new JSONObject();

        fin.put("instances", obj);

        Log.d("http", fin.toString());

        // OutputStream으로 Post 데이터를 넘겨주겠다는 옵션
        conn.setDoOutput(true);

        // InputStream으로 서버로 부터 응답을 받겠다는 옵션
        conn.setDoInput(true);

        // Request Body에 데이터를 담기위한 OutputStream 객체 생성
        OutputStream outputStream;
        outputStream = conn.getOutputStream();
        outputStream.write(fin.toString().getBytes());
        outputStream.flush();

        int response = conn.getResponseCode();
        String responseMsg = conn.getResponseMessage();

        Log.d("http", responseMsg.toString());

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        // 출력물의 라인과 그 합에 대한 변수.
        String line;
        String page = "";

        // 라인을 받아와 합친다.
        while ((line = reader.readLine()) != null){
            Log.d("http", line.toString());
        }
        conn.disconnect();


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