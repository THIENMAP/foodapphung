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
import com.example.foodapp.adapter.RaucuAdapter;
import com.example.foodapp.retrofit.ApiBanHang;
import com.example.foodapp.retrofit.RetrofitClient;
import com.example.foodapp.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ActivityDangKi extends AppCompatActivity {
    EditText email,pass,repass,mobile,username;
    AppCompatButton button;
    TextView txtdn1;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
        initView();
        initControll();
    }



    private void initControll() {
        txtdn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityDangNhap.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangKi();
            }
        });
    }

    private void dangKi() {
        String txtemail = email.getText().toString().trim();
        String txtusername = username.getText().toString().trim();
        String txtpass = pass.getText().toString().trim();
        String txtrepass = repass.getText().toString().trim();
        String txtmobile = mobile.getText().toString().trim();
        if(TextUtils.isEmpty(txtemail)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập email",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(txtusername)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập username",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(txtmobile)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập số điện thoại",Toast.LENGTH_SHORT).show();
        }else{
            if(txtpass.equals(txtrepass)){
                //post data
                //teest
                compositeDisposable.add(apiBanHang.dangki(txtusername,txtemail,txtpass,txtmobile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    if(userModel.isSuccess()){
                                        Utils.usercuren.setEmail(txtemail);
                                        Utils.usercuren.setPass(txtpass);
                                        Toast.makeText(getApplicationContext(),"Thành công",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),ActivityDangNhap.class);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                                }
                        ));
            }else {
                Toast.makeText(getApplicationContext(),"Nhập lại mật khẩu chưa đúng",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        repass = findViewById(R.id.repass);
        username = findViewById(R.id.username);
        mobile = findViewById(R.id.mobile);
        button = findViewById(R.id.btndangki);
        txtdn1 = findViewById(R.id.txtdn1);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}