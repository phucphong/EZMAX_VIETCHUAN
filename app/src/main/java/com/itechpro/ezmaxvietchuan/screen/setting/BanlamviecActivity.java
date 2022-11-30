package com.itechpro.ezmaxvietchuan.screen.setting;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itechpro.ezmaxvietchuan.R;
import com.itechpro.ezmaxvietchuan.Utils.AppConfig;
import com.itechpro.ezmaxvietchuan.adapter.SetupBanlamviecAdapter;
import com.itechpro.ezmaxvietchuan.mode.Laymenuapp;

import java.util.ArrayList;
import java.util.List;

public class BanlamviecActivity extends AppCompatActivity implements SetupBanlamviecAdapter.OnItemClickListener {
    RecyclerView rctab;
    Button btncancel;
    TextView tvtitle;
    SetupBanlamviecAdapter adapter;
    ImageView image_navi;
    List<Laymenuapp> listbanlamviec = new ArrayList<>();
List<Laymenuapp> listdatamenu = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banlamviec);
        rctab = findViewById(R.id.rctab);
        btncancel = findViewById(R.id.btncancel);
        tvtitle = findViewById(R.id.tvtitle);
        image_navi = findViewById(R.id.image_navi);
        image_navi.setVisibility(View.GONE);
        tvtitle.setVisibility(View.VISIBLE);
        tvtitle.setText("Bàn làm việc");
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(BanlamviecActivity.this, 4);
        rctab.setLayoutManager(mLayoutManager);
        listdatamenu = AppConfig.getListMenu(BanlamviecActivity.this, "menu");

        if (listbanlamviec != null) {
            adapter = new SetupBanlamviecAdapter(listdatamenu, BanlamviecActivity.this, BanlamviecActivity.this);
            rctab.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for ( Laymenuapp obj :listdatamenu){
                    if(obj.isCheck()){
                        listbanlamviec.add(obj);
                    }
                }
                AppConfig.saveListBanlamviec(BanlamviecActivity.this,listbanlamviec,"banlamviec");
                AppConfig.saveListMenu(BanlamviecActivity.this,listdatamenu,"menu");
                finish();
            }
        });
    }

    @Override
    public void onItemClick(View itemView, int position) {

        if (!listdatamenu.get(position).isCheck()) {
            listdatamenu.get(position).setCheck(true);
        } else {
            listdatamenu.get(position).setCheck(false);
        }
        adapter = new SetupBanlamviecAdapter(listdatamenu, BanlamviecActivity.this, BanlamviecActivity.this);
        rctab.setAdapter(adapter);
        adapter.notifyItemChanged(position);
    }
}