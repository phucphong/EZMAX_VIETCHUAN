package com.itechpro.ezmaxvietchuan.screen.vietchuan.congviecduocgiao;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itechpro.ezmaxvietchuan.Popup.DialogTimkiem;
import com.itechpro.ezmaxvietchuan.R;
import com.itechpro.ezmaxvietchuan.Utils.AppConfig;
import com.itechpro.ezmaxvietchuan.Utils.Common;
import com.itechpro.ezmaxvietchuan.Utils.Util;
import com.itechpro.ezmaxvietchuan.adapter.AnhVideokhongxoaAdapter;
import com.itechpro.ezmaxvietchuan.adapter.ChitietCongviecDuocgiao3Adapter;
import com.itechpro.ezmaxvietchuan.mode.CongviecpathDc;
import com.itechpro.ezmaxvietchuan.mode.Congviecthucte;
import com.itechpro.ezmaxvietchuan.mode.Khoahoc;
import com.itechpro.ezmaxvietchuan.mode.Timkiem;
import com.itechpro.ezmaxvietchuan.network.RetrofitArrayAPI2;
import com.itechpro.ezmaxvietchuan.screen.thuvienvideo.AllVideoActivity;
import com.itechpro.ezmaxvietchuan.screen.thuvienanh.ImagesActivity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CapnhatcongviecduocgiaoActivity extends AppCompatActivity implements DialogTimkiem.callbacktk,ChitietCongviecDuocgiao3Adapter.OnItemClickListener, AnhVideokhongxoaAdapter.OnItemClickListener {
    String token;
    String ngayhomnay;
    LinearLayoutManager HorizontalLayout;
    TextView dialogdulieu, tvthoigian,tvsave, tvtitle, tvbd_kt,tvthembangchung, tvchuy,tvhoanthanh,tvtitleptht;
    RecyclerView rctab, rcanh;
    ImageView imgthoigian;
    AnhVideokhongxoaAdapter hinhAnhadapter;
    ChitietCongviecDuocgiao3Adapter adapter;
    Congviecthucte obj;
    Calendar calendar;
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
    List<CongviecpathDc> listcvngay = new ArrayList<>();
    DatePickerDialog datePickerDialog;
    int year,month,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capnhatcongviecpathdc);
        initview();

        tvsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isluu =true;
               for ( CongviecpathDc objcv: obj.getList_congviec_pathDc()){
                   String ngaybatdau="";
                   String ngayketthuc="";
                   batdauthucte = objcv.getNgayttbd();
                   kethucthucte = objcv.getNgayttkt();
                   if(!TextUtils.isEmpty(batdauthucte)&&!TextUtils.isEmpty(kethucthucte)){
                       ngaybatdau = batdauthucte.substring(0,10);
                       ngayketthuc = kethucthucte.substring(0,10);
                       Date datebatdautt = new Date();
                       Date dateketthuctt = new Date();
                       datebatdautt = Util.convertStringtoDate(batdauthucte);
                       dateketthuctt = Util.convertStringtoDate(kethucthucte);
                       if (!ngayketthuc.equals(ngaybatdau)) {
                           isluu =false;
                           Util.thongbaothoigian("Ngày kết thúc thực tế và ngày bắt đầu thực tế phải cùng một ngày!", CapnhatcongviecduocgiaoActivity.this);
                       }else if (dateketthuctt.compareTo(datebatdautt) < 0) {
                           isluu =false;
                           Util.thongbaothoigian("Ngày kết thúc thực tế phải  > ngày bắt đầu thực tế!", CapnhatcongviecduocgiaoActivity.this);
                       }
                   }

               }
               if(isluu){
                   yeucaubangchung = obj.isBangchung();
                   soluongbangchung = obj.getSobangchungtoithieu();
                   if(!TextUtils.isEmpty(obj.getList_congviec_pathDc().get(0).getNgayttbd())&&!TextUtils.isEmpty(obj.getList_congviec_pathDc().get(0).getNgayttkt())){
                       batdauthucte = obj.getList_congviec_pathDc().get(0).getNgayttbd();
                       kethucthucte = obj.getList_congviec_pathDc().get(0).getNgayttkt();
                   }else {
                       batdauthucte = obj.getList_congviec_pathDc().get(0).getNgaybd();
                       kethucthucte = obj.getList_congviec_pathDc().get(0).getNgaykt();
                   }
                   batdauthucte = obj.getList_congviec_pathDc().get(0).getNgaybd();
                   kethucthucte = obj.getList_congviec_pathDc().get(0).getNgaykt();
                   id = obj.getList_congviec_pathDc().get(0).getId();
                   idca = obj.getList_congviec_pathDc().get(0).getIdca();
                   if(TextUtils.isEmpty(idca)){
                       idca = "0.0";
                   }
                   check_updateCV(listimage);
               }



            }
        });
        tvthembangchung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialogchonfile();
            }
        });

        tvthoigian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(CapnhatcongviecduocgiaoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yeartungay, int monthtungay, int daytungay) {
                        String thang = "";
                        String ngay = "";
                        int mont=monthtungay+1;
                        if(mont<10){
                            thang= "0"+mont;
                        }else {
                            thang= mont+"";
                        }
                        if(daytungay<10){
                            ngay= "0"+daytungay;
                        }else {
                            ngay= daytungay+"";
                        }
                        tvthoigian.setText(ngay + "/" +thang + "/" + yeartungay);
                        setupdatacv(tvthoigian.getText().toString());
                    }
                }, year, (month-1), day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() -1000);
                datePickerDialog.show();

            }
        });

    }

    private  void  setupdatacv(String ngay){
        listcvngay.clear();
        for (CongviecpathDc  objcv :obj.getList_congviec_pathDc()){
            String ngaybatdau = Util.ngaythangnam(objcv.getNgaybd()+":00");
            if(ngaybatdau.equals(ngay)){
                listcvngay.add(objcv);
            }
        }


        adapter = new ChitietCongviecDuocgiao3Adapter(listcvngay, CapnhatcongviecduocgiaoActivity.this,CapnhatcongviecduocgiaoActivity.this);
        rctab.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void setupdataanh(List<Khoahoc> listdata) {

        if (listdata.size() <= 0 || listdata == null) {
            rcanh.setVisibility(View.GONE);
        } else {
            rcanh.setVisibility(View.VISIBLE);
        }

        hinhAnhadapter = new AnhVideokhongxoaAdapter(listdata, CapnhatcongviecduocgiaoActivity.this, CapnhatcongviecduocgiaoActivity.this);
        rcanh.setAdapter(hinhAnhadapter);
        hinhAnhadapter.notifyDataSetChanged();
    }


    private void showdialogchonfile() {
        final Dialog dialog = new Dialog(CapnhatcongviecduocgiaoActivity.this);
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
                Intent intent = new Intent(CapnhatcongviecduocgiaoActivity.this, ImagesActivity.class);
                startActivityForResult(intent, 2);
                dialog.dismiss();


            }
        });

        calldienthoai.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                Common.ismuticheck = true;
                Intent intent = new Intent(CapnhatcongviecduocgiaoActivity.this, AllVideoActivity.class);
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
        Retrofit retrofit = Util.initRetrofit(CapnhatcongviecduocgiaoActivity.this, true);
        RetrofitArrayAPI2 service = retrofit.create(RetrofitArrayAPI2.class);
        Call<ResponseBody> call = service.editcongviecduocgiaonew("android", obj, "brmitechpro " + token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Common.hud.dismiss();
                if (response.code() == 200) {
                    Util.themmoithanhcong("Lưu thành công", CapnhatcongviecduocgiaoActivity.this);
                }else {
                    Util.themmoithanhcong("Lỗi "+response.code(), CapnhatcongviecduocgiaoActivity.this);
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
                Util.thongbaothoigian("Vui lòng nhập bằng chứng!", CapnhatcongviecduocgiaoActivity.this);
            } else if (listVideoAnh.size() < soluongbangchung) {
                Util.thongbaothoigian("Số lượng bằng chứng vẫn đang thiếu vui lòng lấy thêm dữ liệu!", CapnhatcongviecduocgiaoActivity.this);
            } else {
                uploadFile(id,listVideoAnh);
            }
        } else {
            uploadFile(id,listVideoAnh);
        }

    }
    private void uploadFile(String ido,  List<Khoahoc> listimage ) {
        if (listfile != null) {
            listfile.clear();
        }
        long fileSize=0;
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
                long fileSizeInBytes = file.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                long fileSizeInMB   = fileSizeInKB / 1024;
                fileSize = fileSize+fileSizeInMB;
                Log.e("fileSizeInMB",fileSize+"");
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData(file.getName(), file.getName(), requestFile);
                listfile.add(body);
                String descriptionString = "android";
                description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
            }
        }
        nvbatdautt = Util.fomartngaythang(batdauthucte);
        nvketthuctt = Util.fomartngaythang(kethucthucte);
        Retrofit retrofit = Util.initRetrofit(CapnhatcongviecduocgiaoActivity.this, true);
        RetrofitArrayAPI2 service = retrofit.create(RetrofitArrayAPI2.class);
        if(fileSize>25){
            Util.thongbaothoigian("Kích thước  hiện tại "+fileSize+"  lớn hơn 25MB", CapnhatcongviecduocgiaoActivity.this);
        }else {
            Call<ResponseBody> call;
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
    }
    private void initview() {

        Intent intent = getIntent();
        obj = (Congviecthucte) intent.getSerializableExtra("obj");

        overridePendingTransition(R.anim.slide_off_right, R.anim.slide_out_left);
        if (!isNetworkConnected()) {
            Util.thongbaothoigian("Mất kết nối internet", CapnhatcongviecduocgiaoActivity.this);
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
        tvhoanthanh = findViewById(R.id.tvhoanthanh);
        tvtitleptht = findViewById(R.id.tvtitleptht);
        tvtitleptht.setVisibility(View.GONE);
        tvhoanthanh.setVisibility(View.GONE);
        tvthembangchung = findViewById(R.id.tvthembangchung);
        tvbd_kt = findViewById(R.id.tvbd_kt);
        ngayhomnay = Util.ngayhomngay();
        tvthoigian.setText(ngayhomnay);
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
        String chuy="";
        for (CongviecpathDc  objcv :obj.getList_congviec_pathDc()){
             chuy = "\n"+objcv.getNvchuybangiaocasau();

        }
        tvchuy.setText(chuy);

        tvbd_kt.setText(obj.getBatdau()+" - "+obj.getKetthuc());
        year = Integer.parseInt(Util.nam(obj.getBatdau()));
        month = Integer.parseInt(Util.thang(obj.getBatdau()));
        day = Integer.parseInt(Util.ngay(obj.getBatdau()));
        for (CongviecpathDc objanh :obj.getList_congviec_pathDc()){
            listimage.addAll(objanh.getListfiledk());
        }
        setupdatacv(tvthoigian.getText().toString());
        setupdataanh(listimage);

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
    public void calbackFilter(Timkiem timkiem) {

    }



    @Override
    public void onItemClick(View itemView, int position, CongviecpathDc obj) {
obj.setHoanthanh(!obj.isHoanthanh());
adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemhinhanh(String idsxoa) {

    }
}