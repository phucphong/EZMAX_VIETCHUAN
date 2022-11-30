package com.itechpro.ezmaxvietchuan.screen.vietchuan.congviecduocgiao;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itechpro.ezmaxvietchuan.R;
import com.itechpro.ezmaxvietchuan.Utils.AppConfig;
import com.itechpro.ezmaxvietchuan.Utils.Common;
import com.itechpro.ezmaxvietchuan.Utils.Util;
import com.itechpro.ezmaxvietchuan.adapter.AnhVideoAdapter;
import com.itechpro.ezmaxvietchuan.adapter.ChitietCongviecDuocgiao3Adapter;
import com.itechpro.ezmaxvietchuan.mode.CongviecpathDc;
import com.itechpro.ezmaxvietchuan.mode.Congviecthucte;
import com.itechpro.ezmaxvietchuan.mode.Khoahoc;
import com.itechpro.ezmaxvietchuan.network.RetrofitArrayAPI2;
import com.itechpro.ezmaxvietchuan.screen.Appkhachhang.thuvienvideo.AllVideoActivity;
import com.itechpro.ezmaxvietchuan.screen.thuvienanh.ImagesActivity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CapnhatcongviecpathdcActivity extends AppCompatActivity implements ChitietCongviecDuocgiao3Adapter.OnItemClickListener, AnhVideoAdapter.OnItemClickListener {
    String token;
    String ngayhomnay;
    LinearLayoutManager HorizontalLayout;
    TextView dialogdulieu, tvthoigian,tvsave, tvtitle, tvthembangchung, tvchuy;
    RecyclerView rctab, rcanh;
    ImageView imgthoigian;
    AnhVideoAdapter hinhAnhadapter;
    ChitietCongviecDuocgiao3Adapter adapter;
    Congviecthucte obj;
    String picturePath = "", idca = "", chuy = "", giocadcam = "", idsxoa = "", id = "";
    String batdauthucte = "", kethucthucte = "";
    int soluongbangchung;
    boolean yeucaubangchung;
    RequestBody description;
    List<MultipartBody.Part> listfile = new ArrayList<>();
    ArrayList<String> selectedImageList;
    List<Khoahoc> listimage = new ArrayList<>();
    String tenkhuon = "", sukien = "", kehoach = "";
    List<CongviecpathDc> listtatcacv = new ArrayList<>();
    List<CongviecpathDc> listcvpathdc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capnhatcongviecpathdc);
        initview();

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
        for (CongviecpathDc  obj :obj.getList_congviec_pathDc()){
            String ngaybatdau = Util.ngaythangnam(obj.getNgaybd()+":00");
            if(ngaybatdau.equals(ngayhomnay)){
                listcvpathdc.add(obj);
            }
        }
        Log.e("listcvpathdc",listcvpathdc.size()+"");
        adapter = new ChitietCongviecDuocgiao3Adapter(listcvpathdc, CapnhatcongviecpathdcActivity.this);
        rctab.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // hình ảnh
        tvchuy.setText(obj.getList_congviec_pathDc().get(0).getNvchuybangiaocasau());

         ngayhomnay = Util.ngayhomngay();
        tvthoigian.setText(ngayhomnay);
        for (CongviecpathDc objanh :obj.getList_congviec_pathDc()){
            listimage.addAll(objanh.getListfiledk());
        }
        setupdataanh(listimage);

        tvsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yeucaubangchung = obj.isBangchung();
                soluongbangchung = obj.getSobangchungtoithieu();
                batdauthucte = obj.getList_congviec_pathDc().get(0).getNgaybd();
                kethucthucte = obj.getList_congviec_pathDc().get(0).getNgaykt();
                id = obj.getList_congviec_pathDc().get(0).getId();
                idca = obj.getList_congviec_pathDc().get(0).getIdca();
                check_updateCV(listimage);

                Log.e("a", "â");
            }
        });
        tvthembangchung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialogchonfile();
            }
        });

    }

    private void setupdataanh(List<Khoahoc> listdata) {
        if (listdata.size() <= 0 || listdata == null) {
            rcanh.setVisibility(View.GONE);
        } else {
            rcanh.setVisibility(View.VISIBLE);
        }

        hinhAnhadapter = new AnhVideoAdapter(listdata, CapnhatcongviecpathdcActivity.this, CapnhatcongviecpathdcActivity.this);
        rcanh.setAdapter(hinhAnhadapter);
        hinhAnhadapter.notifyDataSetChanged();
    }

    private void showdialogchonfile() {
        final Dialog dialog = new Dialog(CapnhatcongviecpathdcActivity.this);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setContentView(R.layout.dialog_call);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView huy = dialog.findViewById(R.id.huy);
        TextView calltongdai = dialog.findViewById(R.id.calltongdai);
        TextView calldienthoai = dialog.findViewById(R.id.calldienthoai);
        TextView tvthemmoi = dialog.findViewById(R.id.tvthemmoi);
        tvthemmoi.setText("Chọn File");
        calltongdai.setText("Ảnh");
        calldienthoai.setText("Video");
        calltongdai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.ismuticheck = true;
                Intent intent = new Intent(CapnhatcongviecpathdcActivity.this, ImagesActivity.class);
                startActivityForResult(intent, 2);
                dialog.dismiss();


            }
        });

        calldienthoai.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                Common.ismuticheck = true;
                Intent intent = new Intent(CapnhatcongviecpathdcActivity.this, AllVideoActivity.class);
                startActivityForResult(intent, 3);
                dialog.dismiss();
            }
        });
        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 2 || requestCode == 3 && resultCode == 4) {

                selectedImageList = (ArrayList<String>) data.getSerializableExtra("list");
                for (int i = 0; i < selectedImageList.size(); i++) {
                    String ngay = "";
                    String gio = "";
                    String giophut = "";
                    Khoahoc popup = new Khoahoc();
                    popup.setFiletxt(selectedImageList.get(i));
                    popup.setId("");
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        try {
                            FileTime creationTime = (FileTime) Files.getAttribute(Paths.get(selectedImageList.get(i)), "creationTime");
                            String[] separated = creationTime.toString().split("T");
                            try {
                                ngay = separated[0];
                                gio = separated[1];
                            } catch (Exception ex) {
                                ngay = "";
                                gio = "";
                            }
                            String[] separated1 = gio.split(":");
                            try {
                                giophut = separated1[0] + ":" + separated1[1];
                            } catch (Exception ex) {
                                giophut = "";
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    popup.setNgaytao(ngay + " " + giophut);
                    popup.setTenanh(ngay + " " + giophut);
                    listimage.add(popup);
                }
                setupdataanh(listimage);
                obj.setListfiledk(listimage);
            }

        } catch (Exception ex) {

        }


    }

    private void capnhatcongviec() {
        Retrofit retrofit = Util.initRetrofit(CapnhatcongviecpathdcActivity.this, true);
        RetrofitArrayAPI2 service = retrofit.create(RetrofitArrayAPI2.class);
        Call<ResponseBody> call = service.editcongviecduocgiaonew("android", obj, "brmitechpro " + token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Common.hud.dismiss();
                if (response.code() == 200) {
                    Util.themmoithanhcong("Lưu thành công", CapnhatcongviecpathdcActivity.this);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Common.hud.dismiss();
            }
        });
    }

    private void check_updateCV(List<Khoahoc> listVideoAnh) {
        if (soluongbangchung>0) {
            if (listVideoAnh.size() == 0) {
                Util.thongbaothoigian("Vui lòng nhập bằng chứng!", CapnhatcongviecpathdcActivity.this);
            } else if (listVideoAnh.size() < soluongbangchung) {
                Util.thongbaothoigian("Số lượng bằng chứng vẫn đang thiếu vui lòng lấy thêm dữ liệu!", CapnhatcongviecpathdcActivity.this);
            } else {
                uploadFile(id,listVideoAnh);
            }
        } else {
            capnhatcongviec();
        }

    }
    private void uploadFile(String ido,  List<Khoahoc> listimage ) {
        if (listfile != null) {
            listfile.clear();
        }
        String nvbatdautt = "";
        String nvketthuctt = "";
        String strdate = "";
        for (int i = 0; i < listimage.size(); i++) {
            if (TextUtils.isEmpty(listimage.get(i).getId())) {
                picturePath = listimage.get(i).getFiletxt();
                if (TextUtils.isEmpty(strdate)) {
                    strdate = listimage.get(i).getNgaytao();
                } else {
                    strdate += "," + listimage.get(i).getNgaytao();
                }
                File file = new File(picturePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData(file.getName(), file.getName(), requestFile);
                listfile.add(body);
                String descriptionString = "android";
                description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
            }
        }
        nvbatdautt = Util.fomartngaythang(batdauthucte);
        nvketthuctt = Util.fomartngaythang(kethucthucte);
        Retrofit retrofit = Util.initRetrofit(CapnhatcongviecpathdcActivity.this, true);
        RetrofitArrayAPI2 service = retrofit.create(RetrofitArrayAPI2.class);
        Call<ResponseBody>    call ;
        if (listfile.size() > 0) {
            call = service.editcongviecduocgiao_file(listfile, description, ido.replace(".0", ""), idca.replace(".0", ""), nvbatdautt, nvketthuctt, chuy, giocadcam, idsxoa, strdate, "brmitechpro " + token);
        } else {
            call = service.editcongviecduocgiao("android", ido.replace(".0", ""), idca.replace(".0", ""), nvbatdautt, nvketthuctt, chuy, giocadcam, idsxoa, strdate, "brmitechpro " + token);
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Common.hud.dismiss();
                if (response.code() == 200) {
                    capnhatcongviec();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Common.hud.dismiss();
            }
        });
    }
    private void initview() {
        Intent intent = getIntent();
        obj = (Congviecthucte) intent.getSerializableExtra("obj");

        overridePendingTransition(R.anim.slide_off_right, R.anim.slide_out_left);
        if (!isNetworkConnected()) {
            Util.thongbaothoigian("Mất kết nối internet", CapnhatcongviecpathdcActivity.this);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        token = AppConfig.getToken(this);
        dialogdulieu = findViewById(R.id.dialogdulieu);
        tvthoigian = findViewById(R.id.tvthoigian);
        imgthoigian = findViewById(R.id.imgthoigian);
        tvsave = findViewById(R.id.tvsave);
        rctab = findViewById(R.id.rctab);
        rcanh = findViewById(R.id.rcanh);
        tvtitle = findViewById(R.id.tvtitle);
        tvchuy = findViewById(R.id.tvchuy);
        tvthembangchung = findViewById(R.id.tvthembangchung);
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
}