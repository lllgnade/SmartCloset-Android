package com.Project.Closet.codi;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.Project.Closet.HTTP.Service.ClothesService;
import com.Project.Closet.HTTP.VO.ClothesVO;
import com.Project.Closet.R;
import com.Project.Closet.codi.addCodi.activity_addCodi;
import com.Project.Closet.codi.recoCodi.activity_recoCodi_setting;
import com.Project.Closet.codi.weather.PermissionActivity;
import com.Project.Closet.codi.weather.activity_weatherCodi;
import com.Project.Closet.home.activity_home;
import com.Project.Closet.util.OnBackPressedListener;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;

import static android.app.Activity.RESULT_OK;

public class fragment_codi extends Fragment implements OnBackPressedListener {

    ViewGroup viewGroup;
    Toast toast;
    long backKeyPressedTime;

    int MAKE_CODI = 120;
    int WEATHER_CODI = 191;
    int RECO_CODI = 255;

    Activity activity;

    private TabLayout tabLayout;
    public TabPagerAdapter_codi pagerAdapter;
    private ViewPager finalPager;

    DrawerLayout drawer;

    public RelativeLayout Cloth_Info;
    public RelativeLayout Cloth_Info_edit;
    public ImageView iv_image;
    public ImageView iv_edit_image;
    public TextView tv_name;
    public TextView tv_category;
    public TextView tv_detailcategory;
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
    public TextView tv_edit_name;
    public TextView tv_edit_detailcategory;
    public TextView tv_edit_brand;
    public TextView tv_edit_size;


    private FloatingActionMenu fam;
    private FloatingActionButton fabMake, fabRecommend;

    public static fragment_codi newInstance() {

        Bundle args = new Bundle();

        fragment_codi fragment = new fragment_codi();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.frag_codi,container,false);
        toast = Toast.makeText(getContext(),"?????? ??? ????????? ???????????????.",Toast.LENGTH_SHORT);
        return viewGroup;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity = (Activity) context;
            ((activity_home)activity).setOnBackPressedListener(this);
        }
        
    }


    @Override
    public void onStart() {
        super.onStart();

        drawer = getView().findViewById(R.id.final_drawer_layout);

        Cloth_Info = (RelativeLayout) getView().findViewById(R.id.cloth_info);
        Cloth_Info.setVisibility(View.GONE);
        Cloth_Info_edit = (RelativeLayout) getView().findViewById(R.id.cloth_info_edit);
        Cloth_Info_edit.setVisibility(View.GONE);

        iv_image = (ImageView) getView().findViewById(R.id.iv_codi_image);
        iv_edit_image = (ImageView) getView().findViewById(R.id.iv_edit_image);
        tv_category = (TextView) getView().findViewById(R.id.tv_info_catergory);
        tv_detailcategory = (TextView) getView().findViewById(R.id.tv_info_detailcategory);
        tv_season = (TextView) getView().findViewById(R.id.tv_info_season);
        tv_brand = (TextView) getView().findViewById(R.id.tv_info_brand);
        tv_size = (TextView) getView().findViewById(R.id.tv_info_size);
        tv_date = (TextView) getView().findViewById(R.id.tv_info_date);

        iv_heart = (ImageView) getView().findViewById(R.id.iv_heart);
        iv_modify = (ImageView) getView().findViewById(R.id.iv_modify);
        iv_delete = (ImageView) getView().findViewById(R.id.iv_delete);
        iv_save = (ImageView) getView().findViewById(R.id.iv_save);

        tv_cloNo = (TextView) getView().findViewById(R.id.tv_clothes_no);
        tv_cloFavorite = (TextView) getView().findViewById(R.id.tv_clothes_favorite);
        tv_edit_name = (TextView) getView().findViewById(R.id.tv_info_color);
        tv_edit_detailcategory = (TextView) getView().findViewById(R.id.tv_edit_detailcategory);
        tv_edit_brand = (TextView) getView().findViewById(R.id.tv_edit_brand);
        tv_edit_size = (TextView) getView().findViewById(R.id.tv_edit_size);

        tv_edit_category = (TextView) getView().findViewById(R.id.tv_edit_catergory);
        tv_edit_season = (TextView) getView().findViewById(R.id.tv_edit_season);
        tv_edit_date = (TextView) getView().findViewById(R.id.tv_edit_date);
        tv_edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(activity, listener, 2020, 6, 30);
                dialog.show();
            }
        });

        BtnOnClickListener onClickListener = new BtnOnClickListener();
        //iv_heart.setOnClickListener(onClickListener);
        //iv_modify.setOnClickListener(onClickListener);
        //iv_delete.setOnClickListener(onClickListener);


        iv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_edit_name.getText()!=null)
                    tv_name.setText(tv_edit_name.getText());
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

        //NavigationView navigationView = (NavigationView) getView().findViewById(R.id.final_nav_view); //????????? ???


        //?????? ?????? ???????????? ????????? ?????? ??????

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
//                        Toast.makeText(getContext(), "SelectedItem 1", Toast.LENGTH_SHORT).show();
//                    case R.id.menuitem2:
//                        Toast.makeText(getContext(), "SelectedItem 2", Toast.LENGTH_SHORT).show();
//                    case R.id.menuitem3:
//                        Toast.makeText(getContext(), "SelectedItem 3", Toast.LENGTH_SHORT).show();
//                }
//
//                DrawerLayout drawer = getView().findViewById(R.id.final_drawer_layout);
//                //drawer.closeDrawer(GravityCompat.START);
//                return true;
//            }
//        });

        if(tabLayout == null){
            //??? ?????? ??????
            tabLayout = (TabLayout) getView().findViewById(R.id.tabLayout);
            tabLayout.addTab(tabLayout.newTab().setText("??????"));
//            tabLayout.addTab(tabLayout.newTab().setText("???"));
//            tabLayout.addTab(tabLayout.newTab().setText("??????"));
//            tabLayout.addTab(tabLayout.newTab().setText("??????"));
//            tabLayout.addTab(tabLayout.newTab().setText("??????"));
            tabLayout.addTab(tabLayout.newTab().setText("?????????"));
            tabLayout.addTab(tabLayout.newTab().setText("????????????"));
            tabLayout.addTab(tabLayout.newTab().setText("??????"));
            tabLayout.addTab(tabLayout.newTab().setText("??????"));

            tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

            //??? ????????? ?????? (??? ????????? ????????? ??????)
            finalPager = (ViewPager) getView().findViewById(R.id.tab_Pager);
            pagerAdapter = new TabPagerAdapter_codi(getChildFragmentManager(), tabLayout.getTabCount());
            finalPager.setAdapter(pagerAdapter);
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

        //fabAdd = (FloatingActionButton) getView().findViewById(R.id.fab_add_photo);
        fabMake = (FloatingActionButton) getView().findViewById(R.id.fab_make_codi);
        fabRecommend = (FloatingActionButton) getView().findViewById(R.id.fab_recommend_codi);
        fam = (FloatingActionMenu) getView().findViewById(R.id.fab_menu);

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
        //fabAdd.setOnClickListener(onClickListener);
        fabMake.setOnClickListener(onClickListener);
        fabRecommend.setOnClickListener(onClickListener);


        fam.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fam.isOpened()){
                    fam.close(true);
                }
                else{
                    fam.open(true);
                }
            }
        });

        fam.setIconAnimationOpenInterpolator(new CycleInterpolator(-0.5f));
        fam.setIconAnimationCloseInterpolator(new CycleInterpolator(-0.75f));
        fam.setClosedOnTouchOutside(true);
        fam.getMenuIconView().setColorFilter(Color.parseColor("#000000"));

    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            tv_edit_date.setText(year + "???" + monthOfYear + "???" + dayOfMonth +"???");
            Toast.makeText(getContext(), year + "???" + monthOfYear + "???" + dayOfMonth +"???", Toast.LENGTH_SHORT).show();
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
        } else if(fam.isOpened()){
            fam.close(true);
        } else if (Cloth_Info_edit.getVisibility() == View.VISIBLE) {
            Cloth_Info_edit.setVisibility(View.GONE);
        } else if (Cloth_Info.getVisibility() == View.VISIBLE) {
            Cloth_Info.setVisibility(View.GONE);
        } else if(System.currentTimeMillis() > backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            toast.show();
            return;
        } else if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            activity.finish();
            toast.cancel();
        }
    }


    public class FavoriteTask extends AsyncTask<ClothesVO, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(ClothesVO... ClothesFilter) {

            Call<String> stringCall = ClothesService.getRetrofit(getContext()).modifyClothes(ClothesFilter[0]);
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

            Call<String> stringCall = ClothesService.getRetrofit(getContext()).deleteClothes(cloNo[0]);
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


    //?????? ?????????
    class BtnOnClickListener implements Button.OnClickListener {
        String res="";

        @Override
        public void onClick(View view) {

            Intent intent;
            switch (view.getId()) {
                //case R.id.fab_add_photo : //??????- ?????? ??????
                    // ???????????? ?????? ??????
                    //break;
                case R.id.fab_make_codi:
                    intent = new Intent(getContext(), activity_addCodi.class);
                    startActivityForResult(intent, MAKE_CODI);
                    break;
                case R.id.fab_recommend_codi:
                    intent = new Intent(getContext(), activity_recoCodi_setting.class);
                    startActivityForResult(intent, RECO_CODI);
                    break;
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
                            Toast.makeText(getContext(), "??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                            iv_heart.setImageResource(R.drawable.star_color);
                            tv_cloFavorite.setText("yes");
                        }else{
                            Toast.makeText(getContext(), "??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                            iv_heart.setImageResource(R.drawable.star_empty);
                            tv_cloFavorite.setText("no");
                        }
                        ((activity_home)activity).notify_home_changed();
                    }
                    else
                        Toast.makeText(getContext(), "???????????? ??????", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "?????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        Cloth_Info.setVisibility(View.GONE);
                        //pagerAdapter.notifyDataSetChanged();
                        ((activity_home)activity).refresh_clothes(fragment_codi.this);

                    }else{
                        Toast.makeText(getContext(), "?????? ??????", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MAKE_CODI && resultCode == RESULT_OK)
            ((activity_home)activity).refresh_codi(fragment_codi.this);
        else if(requestCode == WEATHER_CODI && resultCode == RESULT_OK)
            ((activity_home)activity).refresh_codi(fragment_codi.this);
        else if(requestCode == RECO_CODI && resultCode == RESULT_OK)
            ((activity_home)activity).refresh_codi(fragment_codi.this);
    }

    //????????? ??????
    public void setInfo(ClothesVO cloInfo){

        /*Cloth_Info.setVisibility(View.VISIBLE);
        String ImageUrl = Global.baseURL+cloInfo.getFilePath();

        Glide.with((iv_image).getContext()).load(ImageUrl).into(iv_image);
        Glide.with((iv_edit_image).getContext()).load(ImageUrl).into(iv_edit_image);
        tv_name.setText(cloInfo.getName());
        tv_category.setText(cloInfo.getClosetName());
        tv_detailcategory.setText(cloInfo.getCategory());
        tv_season.setText(cloInfo.getSeason());
        tv_brand.setText(cloInfo.getBrand());
        tv_size.setText(cloInfo.getCloSize());
        tv_date.setText(cloInfo.getDate());
        tv_cloNo.setText(Integer.toString(cloInfo.getNo()));

        if("yes".equals(cloInfo.getLike())){
            iv_heart.setImageResource(R.drawable.heart_color);
            tv_cloFavorite.setText("yes");
        }
        else{
            iv_heart.setImageResource(R.drawable.heart_empty);
            tv_cloFavorite.setText("no");
        }*/
    }
}