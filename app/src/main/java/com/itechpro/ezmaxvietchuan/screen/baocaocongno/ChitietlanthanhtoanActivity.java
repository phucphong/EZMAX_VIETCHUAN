package com.itechpro.ezmaxvietchuan.screen.baocaocongno;



import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.itechpro.ezmaxvietchuan.R;
import com.itechpro.ezmaxvietchuan.Utils.AppConfig;
import com.itechpro.ezmaxvietchuan.Utils.Common;
import com.itechpro.ezmaxvietchuan.Utils.Util;
import com.itechpro.ezmaxvietchuan.adapter.ChitietLichsuthanhtoanAdapter;
import com.itechpro.ezmaxvietchuan.mode.ChitietLichsuthanhtoan;
import com.itechpro.ezmaxvietchuan.network.RetrofitArrayAPI2;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ChitietlanthanhtoanActivity extends AppCompatActivity implements ChitietLichsuthanhtoanAdapter.OnItemClickListener {
    RecyclerView rclichsudieutri;
    TextView tvtitle;
    ImageView imageback;
    String url,token,iddonhang,tenhk,madonhang,loai;
    ChitietLichsuthanhtoanAdapter adapter;
    List<ChitietLichsuthanhtoan> listthanhtoan;
    TextView dialogdulieu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lieutrinhdangthuchien);
        initView();
        if (loai.equals("duan")) {
            getlichsuthanhtoanduan(iddonhang);
        }  if (loai.equals("donhang")) {
            getdonhangthanhtoanchitiet(iddonhang);

        }


        if (!isNetworkConnected()) {

            Util.thongbaothoigian("Mất kết nối internet", ChitietlanthanhtoanActivity.this);

        }
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    void  initView(){
        Intent intent = getIntent();
        loai = intent.getStringExtra("loai");
        iddonhang = intent.getStringExtra("iddonhang");
        madonhang = intent.getStringExtra("madonhang");
        tenhk = intent.getStringExtra("tenhk");
        token = AppConfig.getToken(this);
        url = AppConfig.getDomain(this);

        dialogdulieu = findViewById(R.id.dialogdulieu);
        rclichsudieutri = findViewById(R.id.rclichsudieutri);
        imageback = findViewById(R.id.imageback);
        tvtitle = findViewById(R.id.tvtitle);
        tvtitle.setText("Chi tiết thanh toán");


    }
    void getlichsuthanhtoanduan(String iddonhang) {
        Retrofit retrofit= Util.initRetrofit(ChitietlanthanhtoanActivity.this,true);
        RetrofitArrayAPI2 service = retrofit.create(RetrofitArrayAPI2.class);
        Call<List<ChitietLichsuthanhtoan>> call = null;
        call = service.getchitietlanthanhtoanduan("dsdonhangthanhtoan", "modedsduanthanhtoanchitiet", iddonhang.replace(".0", ""), "brmitechpro " + token);
        call.enqueue(new Callback<List<ChitietLichsuthanhtoan>>() {
            @Override
            public void onResponse(Call<List<ChitietLichsuthanhtoan>> call, Response<List<ChitietLichsuthanhtoan>> response) {
                try {
                    listthanhtoan = response.body();
                    Common.hud.dismiss();
                    dialogdulieu.setVisibility(View.GONE);
                    // 1. pass context and data to the custom adapter
                    adapter = new ChitietLichsuthanhtoanAdapter((listthanhtoan), ChitietlanthanhtoanActivity.this, ChitietlanthanhtoanActivity.this);
                    rclichsudieutri.setAdapter(adapter);
                    if (listthanhtoan.size() == 0 || listthanhtoan == null) {
                        dialogdulieu.setVisibility(View.VISIBLE);

                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<List<ChitietLichsuthanhtoan>> call, Throwable t) {
                Common.hud.dismiss();
            }


        });


    }
    void getdonhangthanhtoanchitiet(String iddonhang) {
        Retrofit retrofit= Util.initRetrofit(ChitietlanthanhtoanActivity.this,false);
        RetrofitArrayAPI2 service = retrofit.create(RetrofitArrayAPI2.class);
        Call<List<ChitietLichsuthanhtoan>> call = service.getchitietlanthanhtoan_donhang("dsdonhangthanhtoan", "modedsdonhangthanhtoanchitiet",iddonhang.replace(".0","") ,"brmitechpro " + token);

        call.enqueue(new Callback<List<ChitietLichsuthanhtoan>>() {


            @Override
            public void onResponse(Call<List<ChitietLichsuthanhtoan>> call, Response<List<ChitietLichsuthanhtoan>> response) {
                try {

                    listthanhtoan = response.body();
                    Common.hud.dismiss();
                    dialogdulieu.setVisibility(View.GONE);
                    // 1. pass context and data to the custom adapter
                    adapter = new ChitietLichsuthanhtoanAdapter((listthanhtoan), ChitietlanthanhtoanActivity.this, ChitietlanthanhtoanActivity.this);
                    rclichsudieutri.setAdapter(adapter);
                    if (listthanhtoan.size() == 0 || listthanhtoan == null) {
                        dialogdulieu.setVisibility(View.VISIBLE);

                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<List<ChitietLichsuthanhtoan>> call, Throwable t) {
                Common.hud.dismiss();

            }


        });


    }

    @Override
    public void onItemClickNhatky(View itemView, int position) {


    }
}