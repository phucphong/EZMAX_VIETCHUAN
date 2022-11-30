package com.itechpro.ezmaxvietchuan.screen.vietchuan.congviecsanxuat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.itechpro.ezmaxvietchuan.R;
import com.itechpro.ezmaxvietchuan.Utils.AppConfig;
import com.itechpro.ezmaxvietchuan.Utils.Util;
import com.itechpro.ezmaxvietchuan.adapter.AnhVideoAdapter;
import com.itechpro.ezmaxvietchuan.adapter.ChitietCongviecSX3Adapter;
import com.itechpro.ezmaxvietchuan.mode.CongviecpathDc;
import com.itechpro.ezmaxvietchuan.mode.Congviecthucte;
import com.itechpro.ezmaxvietchuan.mode.Khoahoc;
import com.itechpro.ezmaxvietchuan.mode.SX_Kehoach_Chitietkhuon_JobModel;
import com.itechpro.ezmaxvietchuan.network.RetrofitArrayAPI2;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class CapnhatcongviecsanxuatActivity extends AppCompatActivity implements ChitietCongviecSX3Adapter.OnItemClickListener, AnhVideoAdapter.OnItemClickListener {
    String token;
    LinearLayoutManager HorizontalLayout;
    TextView tvbd_kt,dialogdulieu, tvthoigian,tvsave, tvtitle, tvthembangchung, tvchuy,tvhoanthanh;
    RecyclerView rctab, rcanh;
    ImageView imgthoigian;
    AnhVideoAdapter hinhAnhadapter;
    ChitietCongviecSX3Adapter adapter;
    Congviecthucte obj;
    String  idsxoa = "", id = "";
TextView tvtitlechuy;
    List<Khoahoc> listimage = new ArrayList<>();
    String tenkhuon = "", sukien = "", kehoach = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capnhatcongviecpathdc);
        initview();
        tvbd_kt.setText(obj.getBatdau()+" - "+obj.getKetthuc());
        if (!TextUtils.isEmpty(obj.getTenkhuon())) {
            tenkhuon = obj.getTenkhuon();
        }
        if (!TextUtils.isEmpty(obj.getKehoach())) {
            kehoach = " - " + obj.getKehoach();
        }
        if (!TextUtils.isEmpty(obj.getChitiet())) {
            sukien = " - " + obj.getChitiet();
        }
        tvtitle.setText(tenkhuon + kehoach + sukien);

        adapter = new ChitietCongviecSX3Adapter(obj.getList_congviec_pathDc(), CapnhatcongviecsanxuatActivity.this, CapnhatcongviecsanxuatActivity.this);
        rctab.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        // hình ảnh
        tvhoanthanh.setVisibility(View.VISIBLE);
        tvchuy.setVisibility(View.GONE);
        tvhoanthanh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                for (CongviecpathDc obj :obj.getList_congviec_pathDc()){
                    if (!TextUtils.isEmpty(editable)) {
                        obj.setPtht(editable.toString());
                    }

                }
                adapter.notifyDataSetChanged();
            }
        });
        for (CongviecpathDc objanh :obj.getList_congviec_pathDc()){
            listimage.addAll(objanh.getListfiledk());
        }
        setupdataanh(listimage);
        tvsave.setVisibility(View.GONE);

    }

    private void setupdataanh(List<Khoahoc> listdata) {
        if (listdata.size() <= 0 || listdata == null) {
            rcanh.setVisibility(View.GONE);
        } else {
            rcanh.setVisibility(View.VISIBLE);
        }
        hinhAnhadapter = new AnhVideoAdapter(listdata, CapnhatcongviecsanxuatActivity.this, CapnhatcongviecsanxuatActivity.this);
        rcanh.setAdapter(hinhAnhadapter);
        hinhAnhadapter.notifyDataSetChanged();
    }




    private void capnhatcongviec(String phantramhoanthanh) {
        Retrofit retrofit = Util.initRetrofit(CapnhatcongviecsanxuatActivity.this, false);
        RetrofitArrayAPI2 service = retrofit.create(RetrofitArrayAPI2.class);
        Call<List<SX_Kehoach_Chitietkhuon_JobModel>> call = service.editjobtruongnhom(id,phantramhoanthanh, "brmitechpro " + token);
        call.enqueue(new Callback<List<SX_Kehoach_Chitietkhuon_JobModel>>() {
            @Override
            public void onResponse(Call<List<SX_Kehoach_Chitietkhuon_JobModel>> call, Response<List<SX_Kehoach_Chitietkhuon_JobModel>> response) {


            }

            @Override
            public void onFailure(Call<List<SX_Kehoach_Chitietkhuon_JobModel>> call, Throwable t) {

            }
        });
    }



    private void initview() {
        Intent intent = getIntent();
        obj = (Congviecthucte) intent.getSerializableExtra("obj");

        overridePendingTransition(R.anim.slide_off_right, R.anim.slide_out_left);
        if (!isNetworkConnected()) {
            Util.thongbaothoigian("Mất kết nối internet", CapnhatcongviecsanxuatActivity.this);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        token = AppConfig.getToken(this);
        tvbd_kt = findViewById(R.id.tvbd_kt);
        tvtitlechuy = findViewById(R.id.tvtitlechuy);
        tvtitlechuy.setVisibility(View.GONE);
        dialogdulieu = findViewById(R.id.dialogdulieu);
        tvthoigian = findViewById(R.id.tvthoigian);
        tvthoigian.setVisibility(View.GONE);
        imgthoigian = findViewById(R.id.imgthoigian);
        imgthoigian.setVisibility(View.GONE);
        tvsave = findViewById(R.id.tvsave);
        rctab = findViewById(R.id.rctab);
        rcanh = findViewById(R.id.rcanh);
        tvtitle = findViewById(R.id.tvtitle);
        tvhoanthanh = findViewById(R.id.tvhoanthanh);
        tvchuy = findViewById(R.id.tvchuy);
        tvthembangchung = findViewById(R.id.tvthembangchung);
        tvthembangchung.setText("Danh sách bằng chứng");
        HorizontalLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcanh.setLayoutManager(HorizontalLayout);
        TextView tvcance = findViewById(R.id.tvcance);
        tvcance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(4, intent);
                finish();

            }
        });


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(4, intent);
        finish();

        super.onBackPressed();
    }


    @Override
    public void onItemhinhanh(String idxoa) {
        idsxoa = idxoa;
    }

    @Override
    public void onclickcapnhat(CongviecpathDc obj) {
        String phantramcongviec = "0";
        id = obj.getId();
        phantramcongviec = obj.getPhantramhoanthanh();
        if(TextUtils.isEmpty(phantramcongviec)){
            Util.thongbaothoigian("Vui lòng nhập phần trăm hoàn thành!",CapnhatcongviecsanxuatActivity.this);
        }else {
            capnhatcongviec(phantramcongviec);
        }

    }

    @Override
    public void oncheckcapnhat(CongviecpathDc obj) {
        id = obj.getId();
        String phantramcongviec = "0";
        if(TextUtils.isEmpty(tvhoanthanh.getText().toString())){
          Util.thongbaothoigian("Vui lòng nhập phần trăm hoàn thành!",CapnhatcongviecsanxuatActivity.this);
        }else {
            phantramcongviec = tvhoanthanh.getText().toString();
            obj.setPhantramhoanthanh(phantramcongviec);
            capnhatcongviec(phantramcongviec);
            adapter.notifyDataSetChanged();
        }

    }
}