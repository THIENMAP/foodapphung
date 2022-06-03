package com.example.foodapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.R;
import com.example.foodapp.retrofit.ApiBanHang;
import com.example.foodapp.retrofit.RetrofitClient;
import com.example.foodapp.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ActivityDangNhap extends AppCompatActivity {
    EditText emaildn,passdn;
    AppCompatButton buttondn;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    TextView txtdangki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControll();

    }
//click vao textview chuyen qua man hinh dang ki
    private void initControll() {
        txtdangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityDangKi.class);
                startActivity(intent);
            }
        });
        buttondn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hhgu
                String txtemail = emaildn.getText().toString().trim();
                String txtpass = passdn.getText().toString().trim();
                if(TextUtils.isEmpty(txtemail)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập email",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(txtpass)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập pass",Toast.LENGTH_SHORT).show();
                }else {
                    //post data
                    compositeDisposable.add(apiBanHang.dangnhap(txtemail,txtpass)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        if(userModel.isSuccess()){
                                            Paper.book().write("email",txtemail);
                                            Paper.book().write("pass",txtpass);
                                            Utils.usercuren = userModel.getResult().get(0);
                                            Toast.makeText(getApplicationContext(),"Chào mừng "+userModel.getResult().get(0).getUsername(),Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                            ));
                }

            }
        });
    }

    private void initView() {
        Paper.init(this);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtdangki = findViewById(R.id.txtdk);
        emaildn = findViewById(R.id.emaildn);
        passdn = findViewById(R.id.passdn);
        buttondn = findViewById(R.id.btndn);
        if(Paper.book().read("email") !=null && Paper.book().read("pass") != null){
            emaildn.setText(Paper.book().read("email"));
            passdn.setText(Paper.book().read("pass"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.usercuren.getEmail() != null && Utils.usercuren.getPass() != null){
            emaildn.setText(Utils.usercuren.getEmail());
            passdn.setText(Utils.usercuren.getPass());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}