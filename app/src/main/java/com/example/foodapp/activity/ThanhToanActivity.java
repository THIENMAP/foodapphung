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

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    int tongtt;
    //
    EditText email,mobile,diachi;
    AppCompatButton button;
    TextView tongtien;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        tongtt = getIntent().getIntExtra("tongtt",2);
        initView();
        initControll();
    }

    private void initControll() {


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thanhtoan();
                //Toast.makeText(getApplicationContext(),"Bạn đã gửi đơn hàng",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                startActivity(intent);
            }
        });
    }

    private void thanhtoan() {
        String txtemail = email.getText().toString().trim();
        String txtdiachi = diachi.getText().toString().trim();
        String txtmobile = mobile.getText().toString().trim();
        int txtiduser = Utils.usercuren.getId();
        int txttongtien = tongtt;

        if(TextUtils.isEmpty(txtemail)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập email",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(txtdiachi)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập địa chỉ",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(txtmobile)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập số điện thoại",Toast.LENGTH_SHORT).show();
        }else{

                //post data
                //teest
                compositeDisposable.add(apiBanHang.hoadon(txtiduser,txtemail,txttongtien,txtmobile,txtdiachi)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                hoadonModel -> {
                                    if(hoadonModel.isSuccess()){
                                        Toast.makeText(getApplicationContext(),"Bạn đã gửi đơn hàng",Toast.LENGTH_SHORT).show();

                                    }else {
                                        Toast.makeText(getApplicationContext(),hoadonModel.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                                }
                        ));

        }
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.emailtt);
        diachi = findViewById(R.id.diachitt);
        mobile = findViewById(R.id.sdttt);
        button = findViewById(R.id.btntt_tt);
        tongtien = findViewById(R.id.tongtientt);

        email.setText(Utils.usercuren.getEmail());
        mobile.setText(Utils.usercuren.getMobile());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien.setText(decimalFormat.format(Double.parseDouble(String.valueOf(tongtt)))+"đ");


    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}