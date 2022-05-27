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

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.common.collect.Lists;
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
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;
import javax.net.ssl.HttpsURLConnection;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    //Cloud token은 매 30분 마다 갱신됨

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

        /**
         * @auther Me
         * @since 2022/05/27 11:33 오후
         서비스 계정을 사용하여 JWT 생성 후 ai-platform api 호출 하는 함수
         네트워크를 사용하기 때문에 따로 Thread 만들어서 사용
         **/
        InputStream is = getResources().openRawResource(R.raw.test);


        GoogleCredential credentials = GoogleCredential.fromStream(is)
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/ai-platform"));

        String token = credentials.getAccessToken();
        PrivateKey privateKey = credentials.getServiceAccountPrivateKey();
        String privateKeyId = credentials.getServiceAccountPrivateKeyId();

        long now = System.currentTimeMillis();
        String signedJwt = "";


        // 서비스 계정 (raw에 저장되어있는) 을 갖고 RSA256으로 암호화 하여 JWT 생성
        try {
            KeyPair keyPair = new KeyPair(null, privateKey);

            signedJwt = Jwts.builder()
                    .setHeaderParam("kid", privateKeyId)
                    .setIssuer("test2-110@wifi-indoor-positioning-351013.iam.gserviceaccount.com")
                    .setSubject("test2-110@wifi-indoor-positioning-351013.iam.gserviceaccount.com")
                    .setAudience("https://ml.googleapis.com/")
                    .setExpiration(new Date(now + 3600 * 1000L))
                    .setIssuedAt(new Date(now))
                    .signWith(privateKey, SignatureAlgorithm.RS256)
                    .compact();

        } catch (Exception e) {
            Log.d("error", e.toString());
        }


        //Api 엔드포인트
        URL url = new URL("https://ml.googleapis.com/v1/projects/wifi-indoor-positioning-351013/models/SecondModel/versions/tutorial1:predict");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("POST");

        //인증방식: Bearer
        conn.setRequestProperty("Authorization", "Bearer " + signedJwt );
        //센서 측정값은 JSON형식으로 POST
        conn.setRequestProperty("Content-Type", "application/json");


        //TODO 아래 Dummy값 실제 값으로 변경할 것
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