package com.Project.Closet.closet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.Project.Closet.Global;
import com.Project.Closet.HTTP.Service.ClothesService;
import com.Project.Closet.HTTP.VO.ClothesVO;
import com.Project.Closet.R;
import com.Project.Closet.closet.addClothes.activity_addClothes;
import com.Project.Closet.closet.closet_activities.activity_closet_DB;
import com.Project.Closet.codi.addCodi.activity_addCodi;
import com.Project.Closet.home.activity_home;
import com.Project.Closet.util.OnBackPressedListener;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;

import static android.app.Activity.RESULT_OK;

public class fragment_closet extends Fragment implements OnBackPressedListener {

    ViewGroup viewGroup;
    Toast toast;
    long backKeyPressedTime;

    int ADD_CLOTHES = 100;
    int ADD_FROM_DB = 150;

    Activity activity;

    private TabLayout tabLayout;
    public TabPagerAdapter_closet pagerAdapter;
    private ViewPager finalPager;

    //Button shareButton;

    DrawerLayout drawer;

    LinearLayout ll_detail;
    LinearLayout ll_detail_edit;
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


    public static fragment_closet newInstance() {

        Bundle args = new Bundle();

        fragment_closet fragment = new fragment_closet();
        fragment.setArguments(args);
        return fragment;



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.frag_closet,container,false);
        toast = Toast.makeText(getContext(),"?????? ??? ????????? ???????????????.",Toast.LENGTH_SHORT);

        //shareButton = viewGroup.findViewById(R.id.share_closet);
        //shareButton.setVisibility(View.VISIBLE);

        ll_detail = viewGroup.findViewById(R.id.ll_detail);
        ll_detail_edit = viewGroup.findViewById(R.id.ll_detail_edit);

        drawer = viewGroup.findViewById(R.id.final_drawer_layout);

        Cloth_Info = (RelativeLayout) viewGroup.findViewById(R.id.cloth_info);
        Cloth_Info.setVisibility(View.GONE);
        Cloth_Info_edit = (RelativeLayout) viewGroup.findViewById(R.id.cloth_info_edit);
        Cloth_Info_edit.setVisibility(View.GONE);

        iv_image = (ImageView) viewGroup.findViewById(R.id.iv_image);
        iv_edit_image = (ImageView) viewGroup.findViewById(R.id.iv_edit_image);
        tv_category = (TextView) viewGroup.findViewById(R.id.tv_info_catergory);
        tv_detailcategory = (TextView) viewGroup.findViewById(R.id.tv_info_detailcategory);
        tv_color = (TextView) viewGroup.findViewById(R.id.tv_info_color);
        tv_season = (TextView) viewGroup.findViewById(R.id.tv_info_season);
        tv_brand = (TextView) viewGroup.findViewById(R.id.tv_info_brand);
        tv_size = (TextView) viewGroup.findViewById(R.id.tv_info_size);
        tv_date = (TextView) viewGroup.findViewById(R.id.tv_info_date);

        iv_heart = (ImageView) viewGroup.findViewById(R.id.iv_heart);
        iv_modify = (ImageView) viewGroup.findViewById(R.id.iv_modify);
        iv_delete = (ImageView) viewGroup.findViewById(R.id.iv_delete);
        iv_save = (ImageView) viewGroup.findViewById(R.id.iv_save);

        tv_cloNo = (TextView) viewGroup.findViewById(R.id.tv_clothes_no);
        tv_cloFavorite = (TextView) viewGroup.findViewById(R.id.tv_clothes_favorite);
        tv_edit_color = (TextView) viewGroup.findViewById(R.id.tv_edit_info_color);
        tv_edit_detailcategory = (TextView) viewGroup.findViewById(R.id.tv_edit_detailcategory);
        tv_edit_brand = (TextView) viewGroup.findViewById(R.id.tv_edit_brand);
        tv_edit_size = (TextView) viewGroup.findViewById(R.id.tv_edit_size);
        tv_edit_category = (TextView) viewGroup.findViewById(R.id.tv_edit_catergory);
        tv_edit_season = (TextView) viewGroup.findViewById(R.id.tv_edit_season);
        tv_edit_date = (TextView) viewGroup.findViewById(R.id.tv_edit_date);
        tv_edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                //?????? ??????, ???, ???
                int year = cal.get (Calendar.YEAR);
                int month = cal.get (Calendar.MONTH);
                int date = cal.get (Calendar.DATE) ;

                DatePickerDialog dialog = new DatePickerDialog(activity, listener, year, month, date);
                dialog.show();
            }
        });

        BtnOnClickListener onClickListener = new BtnOnClickListener();
        //iv_heart.setOnClickListener(onClickListener);
        iv_modify.setOnClickListener(onClickListener);
        iv_delete.setOnClickListener(onClickListener);
        //shareButton.setOnClickListener(onClickListener);
        iv_save.setOnClickListener(onClickListener);

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

        //NavigationView navigationView = (NavigationView) viewGroup.findViewById(R.id.final_nav_view); //????????? ???


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
//                DrawerLayout drawer = viewGroup.findViewById(R.id.final_drawer_layout);
//                //drawer.closeDrawer(GravityCompat.START);
//                return true;
//            }
//        });

        if(tabLayout == null){
            //??? ?????? ??????
            tabLayout = (TabLayout) viewGroup.findViewById(R.id.tabLayout);
            tabLayout.addTab(tabLayout.newTab().setText("??????"));
            tabLayout.addTab(tabLayout.newTab().setText("??????"));
            tabLayout.addTab(tabLayout.newTab().setText("??????"));
            tabLayout.addTab(tabLayout.newTab().setText("??????"));
            tabLayout.addTab(tabLayout.newTab().setText("??????"));
            tabLayout.addTab(tabLayout.newTab().setText("??????"));
            tabLayout.addTab(tabLayout.newTab().setText("??????"));
            tabLayout.addTab(tabLayout.newTab().setText("????????????"));

            tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);


            //??? ????????? ?????? (??? ????????? ????????? ??????)
            finalPager = (ViewPager) viewGroup.findViewById(R.id.tab_Pager);
            pagerAdapter = new TabPagerAdapter_closet(getChildFragmentManager(), tabLayout.getTabCount());
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
        fabAdd = (FloatingActionButton) viewGroup.findViewById(R.id.fab_add_photo);
        fabBring = (FloatingActionButton) viewGroup.findViewById(R.id.fab_bring_data);
        fam = (FloatingActionMenu) viewGroup.findViewById(R.id.fab_menu);

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
        fabAdd.setOnClickListener(onClickListener);
        fabBring.setOnClickListener(onClickListener);

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

        //fam.open(true);
        //fam.close(true);
        fam.setClosedOnTouchOutside(true);



        return viewGroup;
    }


    //??????????????? ???????????? ?????? ??????.
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



    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String buyDate = String.format("%d-%02d-%02d",year,monthOfYear+1,dayOfMonth);
            tv_edit_date.setText(buyDate);
            tv_edit_date.setTextColor(Color.parseColor("#000000"));
        }

    };


    @Override
    public void onResume() {
        super.onResume();

        if(tabLayout == null){
            //??? ?????? ??????
            tabLayout = (TabLayout) viewGroup.findViewById(R.id.tabLayout);
            tabLayout.addTab(tabLayout.newTab().setText("??????"));
            tabLayout.addTab(tabLayout.newTab().setText("??????"));
            tabLayout.addTab(tabLayout.newTab().setText("??????"));
            tabLayout.addTab(tabLayout.newTab().setText("??????"));
            tabLayout.addTab(tabLayout.newTab().setText("??????"));
            tabLayout.addTab(tabLayout.newTab().setText("??????"));
            tabLayout.addTab(tabLayout.newTab().setText("??????"));
            tabLayout.addTab(tabLayout.newTab().setText("????????????"));

            tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);


            //??? ????????? ?????? (??? ????????? ????????? ??????)
            finalPager = (ViewPager) viewGroup.findViewById(R.id.tab_Pager);
            pagerAdapter = new TabPagerAdapter_closet(getChildFragmentManager(), tabLayout.getTabCount());
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
            fam.setVisibility(View.VISIBLE);
        } else if(System.currentTimeMillis() > backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            toast.show();
            return;
        } else if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            activity.finish();
            toast.cancel();
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
        String color;
        String season;
        String brand;
        String size;
        String date;

        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                //case R.id.share_closet : //?????? ?????? ??????
                    //intent = new Intent(getContext(), activity_closet_DB.class);
                    //startActivity(intent);
                    //break;
                case R.id.fab_add_photo:
                    intent = new Intent(getContext(), activity_addClothes.class);
                    intent.putExtra("location","private");
                    startActivityForResult(intent,ADD_CLOTHES);
                    break;
                case R.id.fab_bring_data:
                    intent = new Intent(getContext(), activity_closet_DB.class);
                    intent.putExtra("mode", "add");
                    startActivityForResult(intent, ADD_FROM_DB);
                    break;
                case R.id.iv_modify : //?????? ??????
                    //Cloth_Info.setVisibility(View.GONE);
                    Cloth_Info_edit.setVisibility(View.VISIBLE);

                    tv_edit_category.setText(tv_category.getText());
                    if(ll_detail.getVisibility()!=View.GONE){
                        ll_detail_edit.setVisibility(View.VISIBLE);
                        tv_edit_detailcategory.setText(tv_detailcategory.getText());
                    }
                    else{
                        ll_detail_edit.setVisibility(View.GONE);
                    }
                    color =  tv_color.getText().toString();
                    season = tv_season.getText().toString();
                    brand =   tv_brand.getText().toString();
                    size =   tv_size.getText().toString();
                    date =   tv_date.getText().toString();

                    if(!color.isEmpty())
                        tv_edit_color.setText(tv_color.getText());
                    if(!season.isEmpty())
                        tv_edit_season.setText(tv_season.getText());
                    if(!brand.isEmpty())
                        tv_edit_brand.setText(tv_brand.getText());
                    if(!size.isEmpty())
                        tv_edit_size.setText(tv_size.getText());
                    if(!date.isEmpty())
                        tv_edit_date.setText(tv_date.getText());
                    break;

                case R.id.iv_save: //?????? ??????
                    tv_category.setText(tv_edit_category.getText());
                    if(ll_detail_edit.getVisibility()!=View.GONE){
                        ll_detail.setVisibility(View.VISIBLE);
                        tv_detailcategory.setText(tv_edit_detailcategory.getText());
                    }
                    else
                        ll_detail.setVisibility(View.GONE);
                    tv_color.setText(tv_edit_color.getText());

                    season = tv_edit_season.getText().toString();
                    date = tv_edit_date.getText().toString();
                    brand =   tv_edit_brand.getText().toString();
                    size =   tv_edit_size.getText().toString();
                    date =   tv_edit_date.getText().toString();

                    if(!season.equals("??????"))
                        tv_season.setText(tv_edit_season.getText());
                    tv_brand.setText(tv_edit_brand.getText());
                    tv_size.setText(tv_edit_size.getText());
                    if(!date.equals("??????"))
                        tv_date.setText(tv_edit_date.getText());
                    Cloth_Info_edit.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "?????? ??????????????????.", Toast.LENGTH_SHORT).show();
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
                        ((activity_home)activity).refresh_clothes(fragment_closet.this);

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
        if(requestCode == ADD_CLOTHES && resultCode == RESULT_OK)
            ((activity_home)activity).refresh_clothes(fragment_closet.this);
        else if(requestCode == ADD_FROM_DB && resultCode == RESULT_OK){
            ((activity_home)activity).refresh_clothes(fragment_closet.this);
        }

    }

    //????????? ??????
    public void setInfo(ClothesVO cloInfo){

        Cloth_Info.setVisibility(View.VISIBLE);
        fam.setVisibility(View.INVISIBLE);
        String ImageUrl = Global.baseURL+cloInfo.getFilePath();

        Glide.with((iv_image).getContext()).load(ImageUrl).into(iv_image);
        Glide.with((iv_edit_image).getContext()).load(ImageUrl).into(iv_edit_image);

        String category = cloInfo.getCategory();
        String detailCategory = cloInfo.getDetailCategory();
        tv_category.setText(category);
        if(category.equals(detailCategory))
            ll_detail.setVisibility(View.GONE);
        else{
            ll_detail.setVisibility(View.VISIBLE);
            tv_detailcategory.setText(detailCategory);
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