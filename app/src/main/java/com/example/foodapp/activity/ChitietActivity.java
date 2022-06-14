package com.example.foodapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodapp.R;
import com.example.foodapp.model.Sanpham;
import com.example.foodapp.retrofit.ApiBanHang;
import com.example.foodapp.retrofit.RetrofitClient;
import com.example.foodapp.utils.Utils;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChitietActivity extends AppCompatActivity {
    TextView tensp,giasp,mota;
    Button btnchitietthem;
    ImageView imgchitietsp,icongiohang;
    Spinner spinner;
    Toolbar toolbar;
    Sanpham sanpham;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet);
        initView();
        ActionToolBar();
        initData();
        initControll();
    }

    private void initControll() {
        icongiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityGiohang.class);
                startActivity(intent);
            }
        });
        btnchitietthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themgiohang();
            }
        });
    }

    private void themgiohang() {
        if(Utils.usercuren.getId() != 0){
            String txtidsp = Integer.toString(sanpham.getId());
            String txttensp = sanpham.getTensp();
            String txtgiasp = sanpham.getGia();
            String txturl = sanpham.getUrlanh();
            String txtidkhach =Integer.toString(Utils.usercuren.getId()) ;
            String txtsosp = Integer.toString(spinner.getSelectedItemPosition()+1);
            //Toast.makeText(getApplicationContext(),txtidkhach+txttensp+txtsosp+txtgiasp+txturl,Toast.LENGTH_LONG).show();
            //post data
            compositeDisposable.add(apiBanHang.addgiohang(txtidsp,txtidkhach,txtsosp,txttensp,txtgiasp,txturl)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                             giohangModel-> {
                                if(giohangModel.isSuccess()){

                                    Toast.makeText(getApplicationContext(),"Đã thêm vào giỏ hàng",Toast.LENGTH_SHORT).show();


                                }else {
                                    Toast.makeText(getApplicationContext(),giohangModel.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(),"loi"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                            }
                    ));
        }
    }

    private void initData() {
        sanpham = (Sanpham) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanpham.getTensp());
        giasp.setText(sanpham.getGia());
        mota.setText(sanpham.getMota());
        Glide.with(getApplicationContext()).load(sanpham.getUrlanh()).into(imgchitietsp);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText(decimalFormat.format(Double.parseDouble(sanpham.getGia()))+"đ");
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(adapterspin);
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        giasp = findViewById(R.id.item_giachitiet);
        tensp = findViewById(R.id.item_tenchitiet);
        mota = findViewById(R.id.item_motachitiet);
        imgchitietsp = findViewById(R.id.img_chitiet);
        icongiohang = findViewById(R.id.giohang);
        btnchitietthem = findViewById(R.id.btngiohang);
        spinner = findViewById(R.id.spinner_chitiet);
        toolbar = findViewById(R.id.toolbarchitiet);

    }
    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}