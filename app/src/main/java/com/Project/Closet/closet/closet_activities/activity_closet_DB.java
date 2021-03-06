package com.Project.Closet.closet.closet_activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.Project.Closet.Global;
import com.Project.Closet.HTTP.Service.ClothesService;
import com.Project.Closet.HTTP.VO.ClothesVO;
import com.Project.Closet.R;
import com.Project.Closet.closet.addClothes.activity_addClothes;
import com.Project.Closet.social.addFeed.activity_addBoard;
import com.Project.Closet.util.OnBackPressedListener;
import com.Project.Closet.util.Utils;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;

public class activity_closet_DB extends AppCompatActivity implements OnBackPressedListener {


    Toast toast;
    long backKeyPressedTime;

    int ADD_CLOTHES = 100;


    Activity activity;

    LinearLayout ll_detail;
    private TabLayout tabLayout;
    public TabPagerAdapter_closet_share pagerAdapter;
    public TabPagerAdapter_closet_my myPagerAdapter;
    public TabPagerAdapter_closet_my_selected myPagerAdapter_selected;
    private ViewPager finalPager;

    Button bt_select;

    DrawerLayout drawer;

    String mode;
    String selected_kindNum_str;
    int selected_kind=-1;

    public RelativeLayout Cloth_Info;
    public RelativeLayout Cloth_Info_edit;
    public ImageView iv_image;
    public ImageView iv_edit_image;

    public TextView tv_category;
    public TextView tv_detailcategory;
    public TextView tv_color;
    public TextView tv_season;
    public TextView tv_brand;
    public TextView tv_size;
    public TextView tv_date;

    public ImageView iv_heart;
    public ImageView iv_modify;
    public ImageView iv_delete;
    public ImageView iv_save;
    public TextView tv_cloNo;
    public TextView tv_cloFavorite;
    public TextView tv_edit_category;
    public TextView tv_edit_season;
    public TextView tv_edit_date;
    public TextView tv_edit_color;
    public TextView tv_edit_detailcategory;
    public TextView tv_edit_brand;
    public TextView tv_edit_size;

    private FloatingActionMenu fam;
    private FloatingActionButton fabAdd, fabBring;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_closet);

        try{
            mode = getIntent().getExtras().getString("mode");
            selected_kindNum_str = getIntent().getExtras().getString("selected_kindNum_str");
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        if(mode==null)
            mode = "show";
        if(selected_kindNum_str!=null && !selected_kindNum_str.isEmpty())
            selected_kind = Integer.parseInt(selected_kindNum_str);

        toast = Toast.makeText(activity_closet_DB.this,"?????? ??? ????????? ???????????????.",Toast.LENGTH_SHORT);

        activity = this;


        ll_detail = findViewById(R.id.ll_detail);


        drawer = findViewById(R.id.final_drawer_layout);

        Cloth_Info = (RelativeLayout) findViewById(R.id.cloth_info);
        Cloth_Info.setVisibility(View.GONE);
        Cloth_Info_edit = (RelativeLayout) findViewById(R.id.cloth_info_edit);
        Cloth_Info_edit.setVisibility(View.GONE);

        iv_image = (ImageView) findViewById(R.id.iv_image);
        iv_edit_image = (ImageView) findViewById(R.id.iv_edit_image);
        tv_category = (TextView) findViewById(R.id.tv_info_catergory);
        tv_detailcategory = (TextView) findViewById(R.id.tv_info_detailcategory);
        tv_color = (TextView) findViewById(R.id.tv_info_color);
        tv_season = (TextView) findViewById(R.id.tv_info_season);
        tv_brand = (TextView) findViewById(R.id.tv_info_brand);
        tv_size = (TextView) findViewById(R.id.tv_info_size);
        tv_date = (TextView) findViewById(R.id.tv_info_date);

        iv_heart = (ImageView) findViewById(R.id.iv_heart);
        iv_modify = (ImageView) findViewById(R.id.iv_modify);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_save = (ImageView) findViewById(R.id.iv_save);

        tv_cloNo = (TextView) findViewById(R.id.tv_clothes_no);
        tv_cloFavorite = (TextView) findViewById(R.id.tv_clothes_favorite);
        tv_edit_color = (TextView) findViewById(R.id.tv_info_color);
        tv_edit_detailcategory = (TextView) findViewById(R.id.tv_edit_detailcategory);
        tv_edit_brand = (TextView) findViewById(R.id.tv_edit_brand);
        tv_edit_size = (TextView) findViewById(R.id.tv_edit_size);

        tv_edit_category = (TextView) findViewById(R.id.tv_edit_catergory);
        tv_edit_season = (TextView) findViewById(R.id.tv_edit_season);
        tv_edit_date = (TextView) findViewById(R.id.tv_edit_date);
        tv_edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(activity, listener, 2020, 6, 30);
                dialog.show();
            }
        });

        BtnOnClickListener onClickListener = new BtnOnClickListener();
        iv_heart.setOnClickListener(onClickListener);
        iv_modify.setOnClickListener(onClickListener);
        iv_delete.setOnClickListener(onClickListener);



        iv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_edit_category.getText()!="??????????????? ??????????????????.")
                    tv_category.setText(tv_edit_category.getText());
                if(tv_edit_detailcategory.getText()!=null)
                    tv_detailcategory.setText(tv_edit_detailcategory.getText());
                if(tv_edit_season.getText()!="????????? ??????????????????.")
                    tv_season.setText(tv_edit_season.getText());
                if(tv_edit_brand.getText()!=null)
                    tv_brand.setText(tv_edit_brand.getText());
                if(tv_edit_size.getText()!=null)
                    tv_size.setText(tv_edit_size.getText());
                if(tv_edit_date.getText()!=null)
                    tv_date.setText(tv_edit_date.getText());

                Cloth_Info_edit.setVisibility(View.GONE);
            }
        });
        final String[] Season = {""};
        tv_edit_season.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                final String[] items = getResources().getStringArray(R.array.Season);
                final ArrayList<String> selectedItem  = new ArrayList<String>();
                selectedItem.add(items[0]);

                builder.setTitle("???????????? ??????");

                builder.setSingleChoiceItems(R.array.Season, 0, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int pos)
                    {
                        selectedItem.clear();
                        selectedItem.add(items[pos]);
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int pos)
                    {
                        Season[0] = selectedItem.get(0);

                        switch(Season[0]){
                            case "???":
                                tv_edit_season.setText("???");
                                break;
                            case "??????":
                                tv_edit_season.setText("??????");
                                break;
                            case "??????":
                                tv_edit_season.setText("??????");
                                break;
                            case "??????":
                                tv_edit_season.setText("??????");
                                break;
                        }
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        final String[] Category = {""};
        tv_edit_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                final String[] items = getResources().getStringArray(R.array.Kind);
                final ArrayList<String> selectedItem  = new ArrayList<String>();
                selectedItem.add(items[0]);

                builder.setTitle("???????????? ??????");

                builder.setSingleChoiceItems(R.array.Kind, 0, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int pos)
                    {
                        selectedItem.clear();
                        selectedItem.add(items[pos]);
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int pos)
                    {
                        Category[0] = selectedItem.get(0);

                        switch(Category[0]){
                            case "??????":
                                tv_edit_category.setText("??????");
                                break;
                            case "??????":
                                tv_edit_category.setText("??????");
                                break;
                            case "?????????":
                                tv_edit_category.setText("?????????");
                                break;
                            case "??????":
                                tv_edit_category.setText("??????");
                                break;
                            case "??????":
                                tv_edit_category.setText("??????");
                                break;
                            case "??????":
                                tv_edit_category.setText("??????");
                                break;
                            case "????????????":
                                tv_edit_category.setText("????????????");
                                break;
                        }
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        //NavigationView navigationView = (NavigationView) findViewById(R.id.final_nav_view); //????????? ???


//        //?????? ?????? ???????????? ????????? ?????? ??????
//        filterButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(drawer.isDrawerOpen(GravityCompat.START)) {
//                    drawer.closeDrawer(GravityCompat.START);
//                } else {
//                    drawer.openDrawer(GravityCompat.START);
//                }
//            }
//        });

        //??????(??????) ????????? ??????
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                switch (menuItem.getItemId())
//                {
//                    case R.id.menuitem1:
//                        Toast.makeText(activity_closet_DB.this, "SelectedItem 1", Toast.LENGTH_SHORT).show();
//                    case R.id.menuitem2:
//                        Toast.makeText(activity_closet_DB.this, "SelectedItem 2", Toast.LENGTH_SHORT).show();
//                    case R.id.menuitem3:
//                        Toast.makeText(activity_closet_DB.this, "SelectedItem 3", Toast.LENGTH_SHORT).show();
//                }
//
//                DrawerLayout drawer = findViewById(R.id.final_drawer_layout);
//                //drawer.closeDrawer(GravityCompat.START);
//                return true;
//            }
//        });

        if(tabLayout == null){

            //??? ?????? ??????
            tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
            //??? ????????? ?????? (??? ????????? ????????? ??????)
            finalPager = (ViewPager) findViewById(R.id.tab_Pager);
            finalPager.setBackgroundColor(Color.parseColor("#B3E0FF")); //?????? ????????? ?????? ?????? ??????

            if(selected_kind==-1){
                tabLayout.addTab(tabLayout.newTab().setText("??????"));
                tabLayout.addTab(tabLayout.newTab().setText("??????"));
                tabLayout.addTab(tabLayout.newTab().setText("??????"));
                tabLayout.addTab(tabLayout.newTab().setText("??????"));
                tabLayout.addTab(tabLayout.newTab().setText("??????"));
                tabLayout.addTab(tabLayout.newTab().setText("??????"));
                tabLayout.addTab(tabLayout.newTab().setText("??????"));
                tabLayout.addTab(tabLayout.newTab().setText("????????????"));
            }else{
                tabLayout.addTab(tabLayout.newTab().setText(Utils.getKey(Utils.Kind.kindNumMap,selected_kind)));
            }


            if("select_my".equals(mode)){

                if(selected_kind==-1){
                    myPagerAdapter = new TabPagerAdapter_closet_my(getSupportFragmentManager(), tabLayout.getTabCount());
                    finalPager.setAdapter(myPagerAdapter);
                }else{
                    myPagerAdapter_selected = new TabPagerAdapter_closet_my_selected(getSupportFragmentManager(), tabLayout.getTabCount(),selected_kind);
                    finalPager.setAdapter(myPagerAdapter_selected);
                    //tabLayout.setVisibility(View.GONE);
                }
            }
            else{
                pagerAdapter = new TabPagerAdapter_closet_share(getSupportFragmentManager(), tabLayout.getTabCount());
                finalPager.setAdapter(pagerAdapter);
            }

            finalPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    finalPager.setCurrentItem(tab.getPosition());
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }
                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }

        //????????? ?????? ?????? ??????
        fam = (FloatingActionMenu) findViewById(R.id.fab_menu);

        //handling menu status (open or close)
        fam.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    //Toast.makeText(getContext(), "Menu is opened", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getContext(), "Menu is closed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //handling each floating action button clicked
        fam.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity_closet_DB.this, activity_addClothes.class);
                intent.putExtra("location","public");
                startActivityForResult(intent,ADD_CLOTHES);

            }
        });
        fam.setClosedOnTouchOutside(true);


        LinearLayout ll_tools;

        switch(mode){
            case "select":
            case "select_my":
                //?????? ????????? ???
                //ScalableLayout header = findViewById(R.id.header);
                //header.setVisibility(View.GONE);
                //info?????? ?????? ????????? ??????. select ????????? ????????? ???.
                fam.setVisibility(View.GONE);
                ll_tools = findViewById(R.id.ll_tools);
                ll_tools.setVisibility(View.GONE);
                bt_select = findViewById(R.id.bt_select);
                bt_select.setVisibility(View.VISIBLE);
                bt_select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String color = tv_color.getText().toString();
                        String detailCategory = tv_detailcategory.getText().toString();

                        //??????, ????????? ??????
                        String cloNo = tv_cloNo.getText().toString();
                        String identifier = color+"_"+detailCategory;

                        //????????? ??????
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        Bitmap bitmap = ((BitmapDrawable)iv_image.getDrawable()).getBitmap();
                        float scale = (float) (1024/(float)bitmap.getWidth());
                        int image_w = (int) (bitmap.getWidth() * scale);
                        int image_h = (int) (bitmap.getHeight() * scale);
                        Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
                        resize.compress(Bitmap.CompressFormat.JPEG, 30, stream);
                        byte[] byteArray = stream.toByteArray();

                        //intent??? ?????? ??????
                        Intent intent = new Intent(activity_closet_DB.this, activity_addBoard.class);
                        intent.putExtra("cloNo", cloNo);
                        intent.putExtra("identifier", identifier);
                        intent.putExtra("image", byteArray);
                        setResult(RESULT_OK,intent);
                        finish();

                    }
                });
                break;
            case "add" :
                fam.setVisibility(View.GONE);
                ll_tools = findViewById(R.id.ll_tools);
                ll_tools.setVisibility(View.GONE);
                //fam.setVisibility(View.GONE);
                bt_select = findViewById(R.id.bt_select);
                bt_select.setText("????????? ??????");
                bt_select.setVisibility(View.VISIBLE);

                break;
        }


    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            tv_edit_date.setText(year + "???" + monthOfYear + "???" + dayOfMonth +"???");
            Toast.makeText(activity_closet_DB.this, year + "???" + monthOfYear + "???" + dayOfMonth +"???", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        //activity.setOnBackPressedListener(this);
    }

    //?????? ?????? ????????? ????????? ?????? ?????????(??????)??? ?????????.
    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (Cloth_Info_edit.getVisibility() == View.VISIBLE) {
            Cloth_Info_edit.setVisibility(View.GONE);
        } else if (Cloth_Info.getVisibility() == View.VISIBLE) {
            Cloth_Info.setVisibility(View.GONE);
        } else {
            if("add".equals(mode)){
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
            finish();

        }

    }


    public class FavoriteTask extends AsyncTask<ClothesVO, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(ClothesVO... ClothesFilter) {

            Call<String> stringCall = ClothesService.getRetrofit(getApplicationContext()).modifyClothes(ClothesFilter[0]);
            try {
                return stringCall.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public class DeleteTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... cloNo) {

            Call<String> stringCall = ClothesService.getRetrofit(getApplicationContext()).deleteClothes(cloNo[0]);
            try {
                return stringCall.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    /*
    public class AddTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... cloNo) {

            //Call<String> stringCall = ClothesService.getRetrofit(getApplicationContext()).deleteClothes(cloNo[0]);
            try {
                return stringCall.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

     */



    //?????? ?????????
    class BtnOnClickListener implements Button.OnClickListener {
        String res="";

        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                //case R.id.share_closet : //?????? ?????? ??????
                    //intent = new Intent(activity_closet_DB.this, activity_closet_DB.class);
                    //startActivity(intent);
                    //break;
                case R.id.iv_heart : //????????????
                    //????????? ??? vo ??????
                    ClothesVO clothesFilter = new ClothesVO();
                    clothesFilter.setCloNo(Integer.parseInt(tv_cloNo.getText().toString()));
                    boolean reverted_favorite;
                    //???????????? ?????? ???????????? ??????????????? ??????
                    if("yes".equals(tv_cloFavorite.getText().toString())){
                        clothesFilter.setFavorite("no");
                        reverted_favorite = false;
                    }
                    else{
                        clothesFilter.setFavorite("yes");
                        reverted_favorite = true;
                    }

                    try {
                        res = new FavoriteTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, clothesFilter).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e("tag",res);
                    if("ok".equals(res)){
                        if(reverted_favorite){
                            Toast.makeText(activity_closet_DB.this, "??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                            iv_heart.setImageResource(R.drawable.star_color);
                            tv_cloFavorite.setText("yes");

                        }else{
                            Toast.makeText(activity_closet_DB.this, "??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                            iv_heart.setImageResource(R.drawable.star_empty);
                            tv_cloFavorite.setText("no");
                        }
                        pagerAdapter.notifyDataSetChanged();
                    }
                    else
                        Toast.makeText(activity_closet_DB.this, "???????????? ??????", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.iv_modify : //?????? ??????
                    //Cloth_Info.setVisibility(View.GONE);
                    Cloth_Info_edit.setVisibility(View.VISIBLE);
                    tv_edit_date.setText(tv_date.getText());
                    break;
                case R.id.iv_delete : //?????? ??????
                    //?????? Alert ???????????????
                    try {
                        res = new DeleteTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, tv_cloNo.getText().toString()).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if("ok".equals(res)){
                        Toast.makeText(activity_closet_DB.this, "?????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        Cloth_Info.setVisibility(View.GONE);
                        pagerAdapter.notifyDataSetChanged();

                    }else{
                        Toast.makeText(activity_closet_DB.this, "?????? ??????", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_CLOTHES && resultCode == RESULT_OK)
            pagerAdapter.notifyDataSetChanged();

    }



    //????????? ??????
    public void setInfo(ClothesVO cloInfo){

        Cloth_Info.setVisibility(View.VISIBLE);
        String ImageUrl = Global.baseURL+cloInfo.getFilePath();

        Glide.with((iv_image).getContext()).load(ImageUrl).into(iv_image);
        Glide.with((iv_edit_image).getContext()).load(ImageUrl).into(iv_edit_image);

        String category = cloInfo.getCategory();
        String detailCategory = cloInfo.getDetailCategory();
        tv_category.setText(category);
        tv_detailcategory.setText(detailCategory);
        if(category.equals(detailCategory))
            ll_detail.setVisibility(View.GONE);
        else{
            ll_detail.setVisibility(View.VISIBLE);
        }
        tv_color.setText(cloInfo.getColor());
        tv_season.setText(cloInfo.getSeason());
        tv_brand.setText(cloInfo.getBrand());
        tv_size.setText(cloInfo.getCloSize());
        tv_date.setText(cloInfo.getBuyDate());
        tv_cloNo.setText(Integer.toString(cloInfo.getCloNo()));

        if("yes".equals(cloInfo.getFavorite())){
            iv_heart.setImageResource(R.drawable.star_color);
            tv_cloFavorite.setText("yes");
        }
        else{
            iv_heart.setImageResource(R.drawable.star_empty);
            tv_cloFavorite.setText("no");
        }
    }
}