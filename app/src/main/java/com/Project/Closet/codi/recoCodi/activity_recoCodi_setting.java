package com.Project.Closet.codi.recoCodi;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.Project.Closet.HTTP.Service.ClothesService;
import com.Project.Closet.HTTP.Session.preference.MySharedPreferences;
import com.Project.Closet.HTTP.VO.ClothesVO;
import com.Project.Closet.HTTP.weather.vo.ApiInterface;
import com.Project.Closet.HTTP.weather.vo.Repo;
import com.Project.Closet.R;
import com.Project.Closet.closet.closet_activities.activity_closet_DB;
import com.Project.Closet.codi.weather.PermissionActivity;
import com.Project.Closet.util.MySpinnerAdapter;
import com.Project.Closet.util.Utils;
import com.ssomai.android.scalablelayout.ScalableLayout;

import org.apmem.tools.layouts.FlowLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_recoCodi_setting extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "setting";
    int RECO_CODI = 255;
    Call<List<ClothesVO>> cloListCall;
    List<ClothesVO> clothesList;

    ScrollView scrollView;

    List<CheckBox> checkBoxes;
    CheckBox cb_top;
    CheckBox cb_bottom;
    CheckBox cb_suit;
    CheckBox cb_outer;
    CheckBox cb_shoes;
    CheckBox cb_bag;
    CheckBox cb_accessory;

    Spinner spinner_top;
    Spinner spinner_bottom;
    Spinner spinner_suit;
    Spinner spinner_outer;
    Spinner spinner_bag;
    Spinner spinner_shoes;
    Spinner spinner_accessory;

    Spinner spinner_top_detail      ;
    Spinner spinner_bottom_detail   ;
    Spinner spinner_suit_detail     ;
    Spinner spinner_outer_detail    ;
    Spinner spinner_bag_detail      ;
    Spinner spinner_shoes_detail    ;
    Spinner spinner_accessory_detail;


    RadioButton rb_all;
    RadioButton rb_man;


    TextView tv_main_color;
    TextView tv_sub_color;
    CircleImageView civ_main_color;
    CircleImageView civ_sub_color;

    int main_color_num=-1;
    int sub_color_num=-1;

    String top="";
    String bottom="";
    String suit="";
    String outer="";
    String bag="";
    String shoes="";
    String accessory="";
    //
    String top_detail="";
    String bottom_detail="";
    String suit_detail="";
    String outer_detail="";
    String bag_detail="";
    String shoes_detail="";
    String accessory_detail="";
    //
    List<String> categories;
    List<String> detail_categories;




    private static final int FROM_CLOSET = 1009;
    final int MAIN_COLOR = 0;
    final int SUB_COLOR = 1;

    SlidingDrawer slidingDrawer;
    LinearLayout drawer_content;

    //TextView tv_add_image;
    TextView tv_from_closet;
    TextView tv_cancel;

    ArrayList<ImageView> list_childClothes;
    ArrayList<TextView> list_tv_childClothes;
    int[] child_clothes_no;
    ArrayList<Integer> index_resourceID;
    int selected_clo_index;


    ImageView child1;
    ImageView child2;
    ImageView child3;
    ImageView child4;
    ImageView child5;
    ImageView child6;
    ImageView child7;
    //
    TextView tv_child1;
    TextView tv_child2;
    TextView tv_child3;
    TextView tv_child4;
    TextView tv_child5;
    TextView tv_child6;
    TextView tv_child7;


    //?????? ??????
    boolean weatherApplied;
    final double TEMPER_NULL = 10000;
    double temperature = TEMPER_NULL;
    final static String TEMPER_CODE = "???";
    String[] recommendedDCate;

    RadioButton rb_weather_none;
    RadioButton rb_weather_now;
    RadioButton rb_weather_temper;
    RadioButton rb_weather_season;

    LinearLayout ll_now_weather;
    TextView tv_now_location;
    TextView tv_now_temperature;
    FlowLayout fl_setting_temperature;
    FlowLayout fl_setting_season;


    CheckBox temper0;
    CheckBox temper1;
    CheckBox temper2;
    CheckBox temper3;
    CheckBox temper4;
    CheckBox temper5;
    CheckBox temper6;
    CheckBox temper7;
    List<CheckBox> weatherCheckBoxes;


    private LocationManager lm;
    Location my_location;
    private static final int REQUEST_PERMISSION = 1024;
    String API_KEY = "f73fa03d36a8a1b6c8acdca0ea6d229a";
    public Address addr;
    boolean isNowApplied;

    double longitude; //??????
    double latitude; //??????





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reco_codi_setting);


        TextView header_title = findViewById(R.id.header_title);
        header_title.setText("?????? ?????? ??????");

        scrollView = findViewById(R.id.scrollView);

        //???????????? ??????
        cb_top = findViewById(R.id.cb_top_bottom);
        cb_bottom = cb_top;
        cb_suit = findViewById(R.id.cb_suit);
        cb_outer = findViewById(R.id.cb_outer);
        cb_bag = findViewById(R.id.cb_bag);
        cb_shoes = findViewById(R.id.cb_shoes);
        cb_accessory = findViewById(R.id.cb_accessory);
        checkBoxes = new ArrayList<>();
        checkBoxes.add(cb_top);
        checkBoxes.add(cb_bottom);
        checkBoxes.add(cb_suit);
        checkBoxes.add(cb_outer);
        checkBoxes.add(cb_shoes);
        checkBoxes.add(cb_bag);
        checkBoxes.add(cb_accessory);

        //?????? ?????? ??????
        LinearLayout ll_main_color = findViewById(R.id.ll_main_color);
        LinearLayout ll_sub_color = findViewById(R.id.ll_sub_color);
        tv_main_color = findViewById(R.id.tv_main_color);
        tv_sub_color = findViewById(R.id.tv_sub_color);
        civ_main_color = findViewById(R.id.civ_main_color);
        civ_sub_color = findViewById(R.id.civ_sub_color);

        //??? ?????? ??????
        ll_main_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker(MAIN_COLOR);
            }
        });
        ll_sub_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker(SUB_COLOR);
            }
        });

        rb_all=findViewById(R.id.radioButton1);
        rb_man = findViewById(R.id.radioButton3);


        /*?????? ???????????? - ????????? ??????*/

        spinner_top        = (Spinner)findViewById(R.id.spinner_select_top);
        spinner_bottom        = (Spinner)findViewById(R.id.spinner_select_bottom);
        spinner_suit       = (Spinner)findViewById(R.id.spinner_select_suit);
        spinner_outer      = (Spinner)findViewById(R.id.spinner_select_outer);
        spinner_bag           = (Spinner)findViewById(R.id.spinner_select_bag);
        spinner_shoes         = (Spinner)findViewById(R.id.spinner_select_shoes);
        spinner_accessory         = (Spinner)findViewById(R.id.spinner_select_accessory);
        //
        spinner_top_detail        = (Spinner)findViewById(R.id.spinner_select_top_detail);
        spinner_bottom_detail     = (Spinner)findViewById(R.id.spinner_select_bottom_detail);
        spinner_suit_detail       = (Spinner)findViewById(R.id.spinner_select_suit_detail);
        spinner_outer_detail      = (Spinner)findViewById(R.id.spinner_select_outer_detail);
        spinner_bag_detail        = (Spinner)findViewById(R.id.spinner_select_bag_detail);
        spinner_shoes_detail      = (Spinner)findViewById(R.id.spinner_select_shoes_detail);
        spinner_accessory_detail  = (Spinner)findViewById(R.id.spinner_select_accessory_detail);

        //??????
        String[] itemArray = getResources().getStringArray(R.array.??????);
        List<String> items = new ArrayList<>(Arrays.asList(itemArray));
        items.add("(??????)");
        items.add("??????"); // Last item = Hint (??????)
        MySpinnerAdapter adapter = new MySpinnerAdapter(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_top.setAdapter(adapter);
        spinner_top.setSelection(adapter.getCount());
        spinner_top.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner_top.getSelectedItemPosition() != spinner_top.getCount()) { //hint ??????

                    if(spinner_top.getSelectedItemPosition()==spinner_top.getCount()-1){ //?????? ?????????
                        reset_spinner(spinner_top);
                        spinner_top_detail.setVisibility(View.INVISIBLE);
                        top_detail ="";
                        return;
                    }

                    String selected = (String) spinner_top.getSelectedItem();
                    top = selected;
                    Log.d(TAG, "onItemSelected: "+top);
                    List<String> items = setDetailCategory(selected);
                    if(!items.isEmpty()){
                        items.add("??????"); // Last item = Hint (??????)
                        MySpinnerAdapter adapter = new MySpinnerAdapter(activity_recoCodi_setting.this, android.R.layout.simple_spinner_item, items);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_top_detail.setAdapter(adapter);
                        spinner_top_detail.setSelection(adapter.getCount());
                        spinner_top_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(spinner_top_detail.getSelectedItemPosition() != spinner_top_detail.getCount()) { //hint ??????
                                    top_detail = (String) spinner_top_detail.getSelectedItem();
                                }else
                                    top_detail ="";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                        spinner_top_detail.setVisibility(View.VISIBLE);
                    }
                    else{
                        spinner_top_detail.setVisibility(View.INVISIBLE);
                        top_detail ="";
                    }
                }
                else{
                    top = "";
                    Log.d(TAG, "onItemSelected: "+top);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //??????
        itemArray = getResources().getStringArray(R.array.??????);
        items = new ArrayList<>(Arrays.asList(itemArray));
        items.add("(??????)");
        items.add("??????"); // Last item = Hint (??????)
        adapter = new MySpinnerAdapter(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_bottom.setAdapter(adapter);
        spinner_bottom.setSelection(adapter.getCount());
        spinner_bottom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner_bottom.getSelectedItemPosition() != spinner_bottom.getCount()) { //hint ??????

                    if(spinner_bottom.getSelectedItemPosition()==spinner_bottom.getCount()-1){ //?????? ?????????
                        reset_spinner(spinner_bottom);
                        spinner_bottom_detail.setVisibility(View.INVISIBLE);
                        bottom_detail ="";
                        return;
                    }

                    String selected = (String) spinner_bottom.getSelectedItem();
                    bottom = selected;
                    List<String> items = setDetailCategory(selected);
                    if(!items.isEmpty()){
                        items.add("??????"); // Last item = Hint (??????)
                        MySpinnerAdapter adapter = new MySpinnerAdapter(activity_recoCodi_setting.this, android.R.layout.simple_spinner_item, items);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_bottom_detail.setAdapter(adapter);
                        spinner_bottom_detail.setSelection(adapter.getCount());
                        spinner_bottom_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(spinner_bottom_detail.getSelectedItemPosition() != spinner_bottom_detail.getCount()) { //hint ??????
                                    bottom_detail = (String) spinner_bottom_detail.getSelectedItem();
                                }else
                                    bottom_detail ="";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                        spinner_bottom_detail.setVisibility(View.VISIBLE);
                    }
                    else{
                        spinner_bottom_detail.setVisibility(View.INVISIBLE);
                        bottom_detail ="";
                    }
                }else
                    bottom = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //?????????
        itemArray = getResources().getStringArray(R.array.?????????);
        items = new ArrayList<>(Arrays.asList(itemArray));
        items.add("(??????)");
        items.add("?????????"); // Last item = Hint (??????)
        adapter = new MySpinnerAdapter(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_suit.setAdapter(adapter);
        spinner_suit.setSelection(adapter.getCount());
        spinner_suit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner_suit.getSelectedItemPosition() != spinner_suit.getCount()) { //hint ??????

                    if(spinner_suit.getSelectedItemPosition()==spinner_suit.getCount()-1){ //?????? ?????????
                        reset_spinner(spinner_suit);
                        spinner_suit_detail.setVisibility(View.INVISIBLE);
                        suit_detail ="";
                        return;
                    }

                    String selected = (String) spinner_suit.getSelectedItem();
                    suit = selected;
                    List<String> items = setDetailCategory(selected);
                    if(!items.isEmpty()){
                        items.add("??????"); // Last item = Hint (??????)
                        MySpinnerAdapter adapter = new MySpinnerAdapter(activity_recoCodi_setting.this, android.R.layout.simple_spinner_item, items);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_suit_detail.setAdapter(adapter);
                        spinner_suit_detail.setSelection(adapter.getCount());
                        spinner_suit_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(spinner_suit_detail.getSelectedItemPosition() != spinner_suit_detail.getCount()) { //hint ??????
                                    String selected = (String) spinner_suit_detail.getSelectedItem();
                                    suit_detail = selected;
                                }else
                                    suit_detail ="";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                        spinner_suit_detail.setVisibility(View.VISIBLE);
                    }
                    else{
                        spinner_suit_detail.setVisibility(View.INVISIBLE);
                        suit_detail ="";
                    }
                }else
                    suit = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //??????
        itemArray = getResources().getStringArray(R.array.??????);
        items = new ArrayList<>(Arrays.asList(itemArray));
        items.add("(??????)");
        items.add("??????"); // Last item = Hint (??????)
        adapter = new MySpinnerAdapter(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_outer.setAdapter(adapter);
        spinner_outer.setSelection(adapter.getCount());
        spinner_outer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner_outer.getSelectedItemPosition() != spinner_outer.getCount()) { //hint ??????

                    if(spinner_outer.getSelectedItemPosition()==spinner_outer.getCount()-1){ //?????? ?????????
                        reset_spinner(spinner_outer);
                        spinner_outer_detail.setVisibility(View.INVISIBLE);
                        outer_detail ="";
                        return;
                    }
                    String selected = (String) spinner_outer.getSelectedItem();
                    outer = selected;
                    List<String> items = setDetailCategory(selected);
                    if(!items.isEmpty()){
                        items.add("??????"); // Last item = Hint (??????)
                        MySpinnerAdapter adapter = new MySpinnerAdapter(activity_recoCodi_setting.this, android.R.layout.simple_spinner_item, items);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_outer_detail.setAdapter(adapter);
                        spinner_outer_detail.setSelection(adapter.getCount());
                        spinner_outer_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(spinner_outer_detail.getSelectedItemPosition() != spinner_outer_detail.getCount()) { //hint ??????
                                    String selected = (String) spinner_outer_detail.getSelectedItem();
                                    outer_detail = selected;
                                }else
                                    outer_detail ="";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                        spinner_outer_detail.setVisibility(View.VISIBLE);
                    }
                    else{
                        spinner_outer_detail.setVisibility(View.INVISIBLE);
                        outer_detail ="";
                    }
                }else
                    outer = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //??????
        itemArray = getResources().getStringArray(R.array.??????);
        items = new ArrayList<>(Arrays.asList(itemArray));
        items.add("(??????)");
        items.add("??????"); // Last item = Hint (??????)
        adapter = new MySpinnerAdapter(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_shoes.setAdapter(adapter);
        spinner_shoes.setSelection(adapter.getCount());
        spinner_shoes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner_shoes.getSelectedItemPosition() != spinner_shoes.getCount()) { //hint ??????

                    if(spinner_shoes.getSelectedItemPosition()==spinner_shoes.getCount()-1){ //?????? ?????????
                        reset_spinner(spinner_shoes);
                        spinner_shoes_detail.setVisibility(View.INVISIBLE);
                        shoes_detail ="";
                        return;
                    }
                    String selected = (String) spinner_shoes.getSelectedItem();
                    shoes = selected;
                    List<String> items = setDetailCategory(selected);
                    if(!items.isEmpty()){
                        items.add("??????"); // Last item = Hint (??????)
                        MySpinnerAdapter adapter = new MySpinnerAdapter(activity_recoCodi_setting.this, android.R.layout.simple_spinner_item, items);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_shoes_detail.setAdapter(adapter);
                        spinner_shoes_detail.setSelection(adapter.getCount());
                        spinner_shoes_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(spinner_shoes_detail.getSelectedItemPosition() != spinner_shoes_detail.getCount()) { //hint ??????
                                    String selected = (String) spinner_shoes_detail.getSelectedItem();
                                    shoes_detail = selected;
                                }else
                                    shoes_detail ="";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                        spinner_shoes_detail.setVisibility(View.VISIBLE);
                    }
                    else{
                        spinner_shoes_detail.setVisibility(View.INVISIBLE);
                        shoes_detail ="";
                    }
                }else
                    shoes = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //??????
        itemArray = getResources().getStringArray(R.array.??????);
        items = new ArrayList<>(Arrays.asList(itemArray));
        items.add("(??????)");
        items.add("??????"); // Last item = Hint (??????)
        adapter = new MySpinnerAdapter(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_bag.setAdapter(adapter);
        spinner_bag.setSelection(adapter.getCount());
        spinner_bag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner_bag.getSelectedItemPosition() != spinner_bag.getCount()) { //hint ??????

                    if(spinner_bag.getSelectedItemPosition()==spinner_bag.getCount()-1){ //?????? ?????????
                        reset_spinner(spinner_bag);
                        spinner_bag_detail.setVisibility(View.INVISIBLE);
                        bag_detail ="";
                        return;
                    }

                    String selected = (String) spinner_bag.getSelectedItem();
                    bag = selected;
                    List<String> items = setDetailCategory(selected);
                    if(!items.isEmpty()){
                        items.add("??????"); // Last item = Hint (??????)
                        MySpinnerAdapter adapter = new MySpinnerAdapter(activity_recoCodi_setting.this, android.R.layout.simple_spinner_item, items);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_bag_detail.setAdapter(adapter);
                        spinner_bag_detail.setSelection(adapter.getCount());
                        spinner_bag_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(spinner_bag_detail.getSelectedItemPosition() != spinner_bag_detail.getCount()) { //hint ??????
                                    String selected = (String) spinner_bag_detail.getSelectedItem();
                                    bag_detail = selected;
                                }else
                                    bag_detail ="";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                        spinner_bag_detail.setVisibility(View.VISIBLE);
                    }
                    else{
                        spinner_bag_detail.setVisibility(View.INVISIBLE);
                        bag_detail ="";
                    }
                }else
                    bag = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //????????????
        itemArray = getResources().getStringArray(R.array.????????????);
        items = new ArrayList<>(Arrays.asList(itemArray));
        items.add("(??????)");
        items.add("????????????"); // Last item = Hint (??????)
        adapter = new MySpinnerAdapter(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_accessory.setAdapter(adapter);
        spinner_accessory.setSelection(adapter.getCount());
        spinner_accessory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner_accessory.getSelectedItemPosition() != spinner_accessory.getCount()) { //hint ??????

                    if(spinner_accessory.getSelectedItemPosition()==spinner_accessory.getCount()-1){ //?????? ?????????
                        reset_spinner(spinner_accessory);
                        spinner_accessory_detail.setVisibility(View.INVISIBLE);
                        accessory_detail ="";
                        return;
                    }

                    String selected = (String) spinner_accessory.getSelectedItem();
                    accessory = selected;
                    List<String> items = setDetailCategory(selected);
                    if(!items.isEmpty()){
                        items.add("??????"); // Last item = Hint (??????)
                        MySpinnerAdapter adapter = new MySpinnerAdapter(activity_recoCodi_setting.this, android.R.layout.simple_spinner_item, items);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_accessory_detail.setAdapter(adapter);
                        spinner_accessory_detail.setSelection(adapter.getCount());
                        spinner_accessory_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(spinner_accessory_detail.getSelectedItemPosition() != spinner_accessory_detail.getCount()) { //hint ??????
                                    String selected = (String) spinner_accessory_detail.getSelectedItem();
                                    accessory_detail = selected;
                                }else
                                    accessory_detail ="";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                        spinner_accessory_detail.setVisibility(View.VISIBLE);
                    }
                    else{
                        spinner_accessory_detail.setVisibility(View.INVISIBLE);
                        accessory_detail ="";
                    }
                }else
                    accessory = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        /*?????? ??? ??????*/

        //????????? ??????
        slidingDrawer = findViewById(R.id.sliding_drawer);
        drawer_content = findViewById(R.id.drawer_content);

        tv_from_closet= findViewById(R.id.tv_from_closet);
        tv_cancel = findViewById(R.id.tv_cancel);

        tv_from_closet.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        drawer_content.setOnClickListener(this);

        //??? ??????
        child1 = findViewById(R.id.child1);
        child2 = findViewById(R.id.child2);
        child3 = findViewById(R.id.child3);
        child4 = findViewById(R.id.child4);
        child5 = findViewById(R.id.child5);
        child6 = findViewById(R.id.child6);
        child7 = findViewById(R.id.child7);
        tv_child1 = findViewById(R.id.tv_child1);
        tv_child2 = findViewById(R.id.tv_child2);
        tv_child3 = findViewById(R.id.tv_child3);
        tv_child4 = findViewById(R.id.tv_child4);
        tv_child5 = findViewById(R.id.tv_child5);
        tv_child6 = findViewById(R.id.tv_child6);
        tv_child7 = findViewById(R.id.tv_child7);

        list_childClothes = new ArrayList<ImageView>(Arrays.asList(child1, child2, child3, child4, child5, child6, child7));
        list_tv_childClothes = new ArrayList<TextView>(Arrays.asList(tv_child1, tv_child2, tv_child3, tv_child4, tv_child5, tv_child6, tv_child7));
        child_clothes_no =new int[7]; //??? no ??????
        index_resourceID = new ArrayList<Integer>(Arrays.asList(R.id.child1, R.id.child2, R.id.child3, R.id.child4, R.id.child5, R.id.child6, R.id.child7));

        for(ImageView v : list_childClothes){
            v.setOnClickListener(this);
        }

        //?????? ??????
        rb_weather_none = findViewById(R.id.rb_weather_none);
        rb_weather_now = findViewById(R.id.rb_weather_now);
        rb_weather_temper = findViewById(R.id.rb_weather_temper);
        rb_weather_season = findViewById(R.id.rb_weather_season);
        rb_weather_none.setOnClickListener(this);
        rb_weather_now.setOnClickListener(this);
        rb_weather_temper.setOnClickListener(this);
        rb_weather_season.setOnClickListener(this);

        ll_now_weather = findViewById(R.id.ll_now_weather);
        tv_now_location = findViewById(R.id.tv_now_location);
        tv_now_temperature = findViewById(R.id.tv_now_temperature);
        fl_setting_temperature = findViewById(R.id.fl_setting_temperature);
        fl_setting_season = findViewById(R.id.fl_setting_season);







        //?????? ?????? ??????
        RelativeLayout rl_ok = findViewById(R.id.rl_ok);
        rl_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //?????? ??????
                if(!applyWeather())
                    return;


                try {
                    clothesList = new networkTask().execute().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(clothesList==null)
                    return;

                //???????????? ?????? ??????(?????? ??????) ??????
                deactivateParts();

                categories = new ArrayList<>(Arrays.asList(top,bottom,suit,outer,shoes,bag,accessory));
                for(int i=0; i<7; i++){
                    if(!categories.get(i).isEmpty() && child_clothes_no[i]!=0){
                        String kind = Utils.getKey(Utils.Kind.kindNumMap,i);
                        Toast.makeText(activity_recoCodi_setting.this, "<"+kind+">"+" ????????? '??? ???????????? ??????'??? '??? ??? ??????' ????????? ???????????????." +
                                "\n?????? ??? ????????? ????????? ?????????.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                //?????? ???????????? ??????
                if(!restrictCategory())
                    return;

                //?????? ??? ??????
                if(!restrictClothes())
                    return;

                //?????? ??????
                setGender();


                Intent intent = new Intent(activity_recoCodi_setting.this, activity_recommendCodi.class);
                intent.putParcelableArrayListExtra("clothesList",(ArrayList<ClothesVO>) clothesList);
                intent.putExtra("main_color", main_color_num);  //?????? ?????? ?????? ?????????(default ???:-1)
                intent.putExtra("sub_color", sub_color_num);
                intent.putExtra("temperature", temperature);
                startActivityForResult(intent, RECO_CODI);
            }
        });


        RelativeLayout rl_reset = findViewById(R.id.rl_reset);
        rl_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllSetting();
            }
        });
    }



    public class networkTask extends AsyncTask<String, Void, List<ClothesVO>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            startTime = Util.getCurrentTime();
        }

        @Override
        protected List<ClothesVO> doInBackground(String... params) {
            String userID = MySharedPreferences.getInstanceOf(getApplicationContext()).getUserID();
            try {
                if(!weatherApplied){
                    ClothesVO clothesFilter = new ClothesVO();
                    clothesFilter.setLocation("private");
                    cloListCall = ClothesService.getRetrofit(getApplicationContext()).searchClothesNoPage(clothesFilter, userID);
                }
                else{
                    if(recommendedDCate==null){
                        Toast.makeText(activity_recoCodi_setting.this, "?????? ?????? ???????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                    HashMap map = new HashMap();
                    map.put("location","private");
                    map.put("list",recommendedDCate);
                    cloListCall = ClothesService.getRetrofit(getApplicationContext()).searchClothesByListNoPage(
                            map, userID, "detailCategory");
                }
                return cloListCall.execute().body();
                // Do something with the response.
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ClothesVO> clolist) {
            super.onPostExecute(clolist);
        }
    }



    private void scrollDown(){
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }


    public void openColorPicker(final int numColor) {
        final ColorPicker colorPicker = new ColorPicker(this);  // ColorPicker ?????? ??????
        ArrayList<String> colors = new ArrayList<>();  // Color ????????? list

        final Utils colorUtil = new Utils();
        colorUtil.setColorUtil(getApplicationContext());

        for(int i=0; i<colorUtil.color_name.length; i++){
            colors.add(colorUtil.mapColors.get(colorUtil.color_name[i]));
        }

        colorPicker.setColors(colors)  // ???????????? list ??????
                .setColumns(5)  // 5?????? ??????
                .setRoundColorButton(true)  // ?????? ???????????? ??????
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int colorInt) {

                        String colorIntStr = String.format("#%06X", 0xFFFFFF & colorInt); //"#ff0000"
                        String colorName = Utils.getKey(colorUtil.mapColors,colorIntStr);
                        int colorNum = Utils.colorNumMap.get(colorName);

                        switch(numColor){
                            case MAIN_COLOR:
                                main_color_num = colorNum;
                                tv_main_color.setText(colorName);
                                civ_main_color.setColorFilter(Color.parseColor(colorIntStr));
                                break;
                            case SUB_COLOR :
                                sub_color_num = colorNum;
                                tv_sub_color.setText(colorName);
                                civ_sub_color.setColorFilter(Color.parseColor(colorIntStr));
                                break;
                        }
                    }

                    @Override
                    public void onCancel() {
                        resetColor(numColor);
                    }
                }).show();  // dialog ??????
    }


    List<String> setDetailCategory(String category){

        String[] items;
        switch(category){
            case "???????????????" :
                items = getResources().getStringArray(R.array.???????????????);
                break;
            case "???????????????" :
                items = getResources().getStringArray(R.array.???????????????);
                break;
            case "??????" :
                items = getResources().getStringArray(R.array.??????);
                break;
            case "??????" :
                items = getResources().getStringArray(R.array.??????);
                break;
            case "??????" :
                items = getResources().getStringArray(R.array.??????);
                break;
            case "?????????" :
                items = getResources().getStringArray(R.array.?????????);
                break;
            case "?????????" :
                items = getResources().getStringArray(R.array.?????????);
                break;
            case "??????" :
                items = getResources().getStringArray(R.array.??????);
                break;
            case "??????" :
                items = getResources().getStringArray(R.array.??????);
                break;
            case "??????" :
                items = getResources().getStringArray(R.array.??????);
                break;
            case "??????" :
                items = getResources().getStringArray(R.array.??????);
                break;
            case "??????" :
                items = getResources().getStringArray(R.array.??????);
                break;
            case "?????? ??????" :
                items = getResources().getStringArray(R.array.??????_??????);
                break;
            case "?????? ??????" :
                items = getResources().getStringArray(R.array.??????_??????);
                break;
            case "????????? ??????" :
                items = getResources().getStringArray(R.array.?????????_??????);
                break;
            default:
                items = new String[]{};
        }

        if(items.length !=0){
            List<String> itemList = new ArrayList<>(Arrays.asList(items));
            return itemList;
        }else return Collections.emptyList();
    }

    void reset_spinner(Spinner spinner){
        spinner.setSelection(spinner.getCount());
    }

    void deactivateParts(){

        for(int kindNum=0; kindNum<7; kindNum++){
            if(!checkBoxes.get(kindNum).isChecked()){ //???????????? ?????? ??????
                String Kind = Utils.getKey(Utils.Kind.kindNumMap,kindNum); //????????? ?????????
                Iterator<ClothesVO> iter = clothesList.iterator();
                while (iter.hasNext()) { //??????
                    ClothesVO clothes = iter.next();
                    if(Kind.equals(clothes.getKind()))
                        iter.remove();
                }
//
//                for(ClothesVO clothes : clothesList){ //??????
//                    if(Kind.equals(clothes.getKind()))
//                        clothesList.remove(clothes);
//                }
            }
        }
    }

    boolean restrictCategory(){

        //categories = new ArrayList<>(Arrays.asList(top,bottom,suit,outer,shoes,bag,accessory));


        if((!top.isEmpty()||!bottom.isEmpty())&&!suit.isEmpty()){
            Toast.makeText(this, "??????/????????? ???????????? ????????? ????????? ??? ????????????.", Toast.LENGTH_SHORT).show();
            return false;
        }


            detail_categories = new ArrayList<>(Arrays.asList(top_detail,bottom_detail,suit_detail,
                outer_detail,shoes_detail,bag_detail,accessory_detail));

        for (int kindNum=0; kindNum<7; kindNum++){
            String category = categories.get(kindNum); //?????? ??????(ex.??????)??? ????????? ??????????????? ?????????
            String kind = Utils.getKey(Utils.Kind.kindNumMap,kindNum); //????????? ?????????

            if(!category.isEmpty()){ //??????????????? ?????? ???????????????
                String detail_category = detail_categories.get(kindNum); //????????? ???????????? ?????????
                Iterator<ClothesVO> cloListIter = clothesList.iterator();
                int remain_items=0;

                if(detail_category.isEmpty()){ //??????????????? ???????????? ?????????
                    while (cloListIter.hasNext()) {
                        ClothesVO clothes = cloListIter.next();
                        String cloKind = clothes.getKind();
                        if(cloKind.equals(kind)){
                            if(!clothes.getCategory().equals(category))
                                cloListIter.remove(); //?????? ????????? ?????? ??????????????? ?????? ????????? ??????
                            else
                                remain_items+=1; //???????????? ?????? ???
                        }

                        if(kindNum == Utils.Kind.TOP || kindNum == Utils.Kind.BOTTOM){
                            if("?????????".equals(cloKind))
                                cloListIter.remove();
                        }else if(kindNum == Utils.Kind.SUIT){
                            if("??????".equals(cloKind) || "??????".equals(cloKind))
                                cloListIter.remove(); //??????
                        }
                    }
                }else{ //????????? ??????????????? ???????????? ?????????
                    while (cloListIter.hasNext()) {
                        ClothesVO clothes = cloListIter.next();
                        String cloKind = clothes.getKind();
                        if(cloKind.equals(kind)){
                            if(!clothes.getDetailCategory().equals(detail_category))
                                cloListIter.remove();//?????? ????????? ?????? ?????? ??????????????? ?????? ????????? ??????
                            else
                                remain_items+=1; //???????????? ?????? ???
                        }

                        if(kindNum == Utils.Kind.TOP || kindNum == Utils.Kind.BOTTOM){
                            if("?????????".equals(cloKind))
                                cloListIter.remove();
                        }else if(kindNum == Utils.Kind.SUIT){
                            if("??????".equals(cloKind) || "??????".equals(cloKind))
                                cloListIter.remove(); //??????
                        }
                    }
                }

                if(remain_items==0){
                    if(!detail_category.isEmpty()){
                        category = detail_category;
                    }

//                    if(recommendedDCate!=null ){
//                        List<String>recommendedDCateArray = Arrays.asList(recommendedDCate);
//                        if(!recommendedDCateArray.contains(category)){
//                            Toast.makeText(this, "?????? ????????? ?????? ?????? <"+category+"> ??????????????? ????????? ???????????? ????????????.", Toast.LENGTH_LONG).show();
//                            return false;
//                        }
//                    }

                    Toast.makeText(this, "????????? <"+category+"> ??????????????? ?????? ????????? ?????? ????????? ?????? ????????????. \n??? ?????? ?????? ??????????????????.", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }

        return true;
    }

    boolean restrictClothes(){

        if((child_clothes_no[Utils.Kind.TOP]!=0 || child_clothes_no[Utils.Kind.BOTTOM]!=0)
                && child_clothes_no[Utils.Kind.SUIT]!=0){
            Toast.makeText(this, "??????/????????? ???????????? ????????? ????????? ??? ????????????.", Toast.LENGTH_SHORT).show();
            return false;
        }
        for(int kindNum=0; kindNum<7; kindNum++){
            int cloNo=child_clothes_no[kindNum];
            String kind = Utils.getKey(Utils.Kind.kindNumMap, kindNum);
            int remain_item=0;
            if(cloNo!=0){
                Iterator<ClothesVO> cloListIter = clothesList.iterator();
                while (cloListIter.hasNext()) {
                    ClothesVO clothes = cloListIter.next();
                    String cloKind = clothes.getKind();
                    if(cloKind.equals(kind)){
                        if(clothes.getCloNo()!=cloNo)
                            cloListIter.remove();
                        else
                            remain_item+=1; //???????????? ?????? ???
                    }
                    if(kindNum == Utils.Kind.TOP || kindNum == Utils.Kind.BOTTOM){
                        if("?????????".equals(cloKind))
                            cloListIter.remove();
                    }else if(kindNum == Utils.Kind.SUIT){
                        if("??????".equals(cloKind) || "??????".equals(cloKind))
                            cloListIter.remove(); //??????
                    }
                }
                if(remain_item==0){
                    Toast.makeText(this, "????????? ????????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Integer resourceID = v.getId();
        switch(v.getId()){
            case R.id.child1 :
            case R.id.child2 :
            case R.id.child3 :
            case R.id.child4 :
            case R.id.child5 :
            case R.id.child6 :
            case R.id.child7 :
                selected_clo_index= index_resourceID.indexOf(resourceID);
                slidingDrawer.open();
                break;
            case R.id.drawer_content :
                slidingDrawer.close();
                break;
            case R.id.tv_from_closet :
                //share??? ?????????. mode ????????? result ????????????
                intent = new Intent(this, activity_closet_DB.class);
                intent.putExtra("mode","select_my");
                intent.putExtra("selected_kindNum_str",Integer.toString(selected_clo_index));
                startActivityForResult(intent, FROM_CLOSET);
                slidingDrawer.close();
                break;
            case R.id.tv_cancel :
                resetCurrentItem();
                break;
            case R.id.rb_weather_none :
                setWeatherNone();
                break;
            case R.id.rb_weather_now :
                setNowTemper();
                break;
            case R.id.rb_weather_temper :
                ll_now_weather.setVisibility(View.GONE);
                fl_setting_temperature.setVisibility(View.VISIBLE);
                fl_setting_season.setVisibility(View.GONE);
                scrollDown();
                break;
            case R.id.rb_weather_season :
                ll_now_weather.setVisibility(View.GONE);
                fl_setting_temperature.setVisibility(View.GONE);
                fl_setting_season.setVisibility(View.VISIBLE);
                scrollDown();
                break;
        }
    }

    void resetCurrentItem(){
        //????????? ??????
        list_childClothes.get(selected_clo_index).setImageResource(R.drawable.hanger_gray_small);
        //??? ?????? ??????
        child_clothes_no[selected_clo_index] = 0;
        //????????? ?????????
        list_tv_childClothes.get(selected_clo_index).setVisibility(View.VISIBLE);
        slidingDrawer.close();
    }

    void resetAllItem(){
        for(int i=0; i<7; i++){
            //????????? ??????
            list_childClothes.get(i).setImageResource(R.drawable.hanger_gray_small);
            //??? ?????? ??????
            child_clothes_no[i] = 0;
            //????????? ?????????
            list_tv_childClothes.get(i).setVisibility(View.VISIBLE);
        }
    }

    void setGender(){
        if (rb_man.isChecked()){
            Iterator<ClothesVO> cloListIter = clothesList.iterator();
            while (cloListIter.hasNext()) {
                ClothesVO clothes = cloListIter.next();
                String category = clothes.getCategory();
                if("?????????".equals(category)){
                    cloListIter.remove();
                }
                else if("?????????".equals(category)){
                    cloListIter.remove();
                }
            }
        }
    }

    void setWeatherNone(){
        rb_weather_none.setChecked(true);
        ll_now_weather.setVisibility(View.GONE);
        fl_setting_temperature.setVisibility(View.GONE);
        fl_setting_season.setVisibility(View.GONE);
    }

    void setNowTemper() {
        startFindLocation();
        rb_weather_now.setChecked(true);
        ll_now_weather.setVisibility(View.VISIBLE);
        fl_setting_temperature.setVisibility(View.GONE);
        fl_setting_season.setVisibility(View.GONE);
        scrollDown();
    }

    boolean applyWeather(){
        temperature = TEMPER_NULL;
        recommendedDCate=null;
        weatherApplied=false;

        if(rb_weather_none.isChecked()){ //?????? ??????
            return true; //?????? ?????? ??? ??????
        }else if(rb_weather_now.isChecked()){ //?????? ??????

            if(!isNowApplied){
                Toast.makeText(this, "???????????? ????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                return false;
            }

            //????????? ????????? ????????????
            String temperStr = tv_now_temperature.getText().toString();
            if(!temperStr.isEmpty()){
                int idx = temperStr.indexOf(TEMPER_CODE); //???
                temperStr = temperStr.substring(0, idx);
                temperature = Double.parseDouble(temperStr);
            }
            else{
                Toast.makeText(this, "????????? ????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                return false;
            }
            //?????? ??? ????????? ?????? ?????????
            if(temperature!=TEMPER_NULL){ //???????????? ?????? ?????????
                if(temperature<6)
                    recommendedDCate = getResources().getStringArray(R.array.to5);
                else if(temperature>=6 && temperature<10)
                    recommendedDCate = getResources().getStringArray(R.array.fr6to9);
                else if(temperature>=10 && temperature<12)
                    recommendedDCate = getResources().getStringArray(R.array.fr10to11);
                else if(temperature>=12 && temperature<17)
                    recommendedDCate = getResources().getStringArray(R.array.fr12to16);
                else if(temperature>=17 && temperature<19)
                    recommendedDCate = getResources().getStringArray(R.array.fr17to19);
                else if(temperature>=19 && temperature<22)
                    recommendedDCate = getResources().getStringArray(R.array.fr20to22);
                else if(temperature>=23 && temperature<26)
                    recommendedDCate = getResources().getStringArray(R.array.fr23to26);
                else if(temperature>=27)
                    recommendedDCate = getResources().getStringArray(R.array.fr27);
            }
        }else{ //?????? or ?????? ??????

            temper0= findViewById(R.id.temper0);
            temper1= findViewById(R.id.temper1);
            temper2= findViewById(R.id.temper2);
            temper3= findViewById(R.id.temper3);
            temper4= findViewById(R.id.temper4);
            temper5= findViewById(R.id.temper5);
            temper6= findViewById(R.id.temper6);
            temper7= findViewById(R.id.temper7);

            weatherCheckBoxes= new ArrayList<>();
            weatherCheckBoxes.add(temper0);
            weatherCheckBoxes.add(temper1);
            weatherCheckBoxes.add(temper2);
            weatherCheckBoxes.add(temper3);
            weatherCheckBoxes.add(temper4);
            weatherCheckBoxes.add(temper5);
            weatherCheckBoxes.add(temper6);
            weatherCheckBoxes.add(temper7);


            if(rb_weather_temper.isChecked()){ //?????? ??????
                if(!loadCheckedTemperature())
                    return false;
            }else if(rb_weather_season.isChecked()){ //?????? ??????
                if(!checkSeason()) //???????????? ?????? ??????
                    return false;
                if(!loadCheckedTemperature()) //????????? ?????????????????? ????????? ????????????
                    return false;
            }
        }

        if(recommendedDCate!=null){
            weatherApplied=true;
            return true;
        }
        else{
            Toast.makeText(this, "????????? ????????? ????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    boolean loadCheckedTemperature(){

        HashSet<String> stringHashSet = new HashSet<>();
        int checkedSize=0;
        int checkedTemper=10000;
        for(int temperNum=0; temperNum<8; temperNum++) {
            if (weatherCheckBoxes.get(temperNum).isChecked()){
                checkedSize+=1;
                switch(temperNum){
                    case 0:
                        checkedTemper=5;
                        stringHashSet.addAll(Arrays.asList(getResources().getStringArray(R.array.to5)));
                        break;
                    case 1:
                        checkedTemper=6;
                        stringHashSet.addAll(Arrays.asList(getResources().getStringArray(R.array.fr6to9)));
                        break;
                    case 2:
                        checkedTemper=10;
                        stringHashSet.addAll(Arrays.asList(getResources().getStringArray(R.array.fr10to11)));
                        break;
                    case 3:
                        checkedTemper=12;
                        stringHashSet.addAll(Arrays.asList(getResources().getStringArray(R.array.fr12to16)));
                        break;
                    case 4:
                        checkedTemper=17;
                        stringHashSet.addAll(Arrays.asList(getResources().getStringArray(R.array.fr17to19)));
                        break;
                    case 5:
                        checkedTemper=20;
                        stringHashSet.addAll(Arrays.asList(getResources().getStringArray(R.array.fr20to22)));
                        break;
                    case 6:
                        checkedTemper=23;
                        stringHashSet.addAll(Arrays.asList(getResources().getStringArray(R.array.fr23to26)));
                        break;
                    case 7:
                        checkedTemper=27;
                        stringHashSet.addAll(Arrays.asList(getResources().getStringArray(R.array.fr27)));
                        break;
                }
            }
        }

        if(checkedSize==0){
            Toast.makeText(this, "????????? ????????? ????????? ???????????? ?????????.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(checkedSize==1){
            temperature = checkedTemper;
        }

        if(stringHashSet.size()!=0)
            recommendedDCate = (String[])stringHashSet.toArray(new String[0]);

        if(recommendedDCate!=null && recommendedDCate.length!=0)
            return true;
        else{
            Toast.makeText(this, "????????? ???????????? ????????? ??? ???????????????.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    boolean checkSeason(){
        RadioButton spring= findViewById(R.id.spring);
        RadioButton summer= findViewById(R.id.summer);
        RadioButton fall= findViewById(R.id.fall);
        RadioButton winter= findViewById(R.id.winter);
        List<RadioButton> seasonRadioButtons= new ArrayList<>();
        seasonRadioButtons.add(spring);
        seasonRadioButtons.add(summer);
        seasonRadioButtons.add(fall);
        seasonRadioButtons.add(winter);

        int checkedSize=0;
        for(int seasonNum=0; seasonNum<4; seasonNum++) {
            if (seasonRadioButtons.get(seasonNum).isChecked()){
                checkedSize+=1;
                switch(seasonNum){
                    case 0:
                        setTemperCheckedTrue(new int[]{3,4});
                        setTemperCheckedFalse(new int[]{0,1,2,5,6,7});
                        break;
                    case 1:
                        setTemperCheckedTrue(new int[]{5,6,7});
                        setTemperCheckedFalse(new int[]{0,1,2,3,4});
                        break;
                    case 2:
                        setTemperCheckedTrue(new int[]{1,2,3});
                        setTemperCheckedFalse(new int[]{0,4,5,6,7});
                        break;
                    case 3:
                        setTemperCheckedTrue(new int[]{0, 1});
                        setTemperCheckedFalse(new int[]{2,3,4,5,6,7});
                        break;
                }
            }
        }

        if(checkedSize==0){
            Toast.makeText(this, "????????? ????????? ????????? ???????????? ?????????.", Toast.LENGTH_SHORT).show();
            return false;
        }else
            return true;
    }

    void setTemperCheckedTrue(int[] indexes){
        for (int index : indexes){
            CheckBox cb =weatherCheckBoxes.get(index);
            cb.setChecked(true);
        }
    }

    void setTemperCheckedFalse(int[] indexes){
        for (int index : indexes){
            CheckBox cb =weatherCheckBoxes.get(index);
            cb.setChecked(false);
        }
    }


    /**
     * ???????????? ????????? ??????
     */
    private void startFindLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, PermissionActivity.class);
            startActivityForResult(intent,REQUEST_PERMISSION);
        }
        else {
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            assert lm != null;
            lm.removeUpdates( mLocationListener );
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // ????????? ???????????????
                    10, // ??????????????? ?????? ???????????? (miliSecond)
                    1, // ??????????????? ?????? ???????????? (m)
                    mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // ????????? ???????????????
                    10, // ??????????????? ?????? ???????????? (miliSecond)
                    1, // ??????????????? ?????? ???????????? (m)
                    mLocationListener);

        }
    }

    private LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //????????? ???????????? ???????????? ???????????? ????????????.
            //?????? Location ????????? ???????????? ?????? ?????? ????????? ????????? ??????.

            Log.d("test", "onLocationChanged, location:" + location);
            my_location=location;
            lm.removeUpdates(this);  //?????? ?????? ?????? ??????
            longitude = location.getLongitude(); //??????
            latitude = location.getLatitude();   //??????
            double altitude = location.getAltitude();   //??????
            float accuracy = location.getAccuracy();    //?????????
            String provider = location.getProvider();   //???????????????
            //Gps ?????????????????? ?????? ????????????. ??????????????? ??????.
            //Network ?????????????????? ?????? ????????????
            //Network ????????? Gps??? ?????? ???????????? ?????? ????????????.
            Log.d("test", "???????????? : " + provider + "\n?????? : " + longitude + "\n?????? : " + latitude
                    + "\n?????? : " + altitude + "\n????????? : "  + accuracy);

            if(latitude==0 || longitude == 0){
                Toast.makeText(activity_recoCodi_setting.this, "????????? ?????? ????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
            }
            else {
                getAddress(getBaseContext(), latitude, longitude);//?????? ??? ??? ??? ??? ??????

                //String do_ = addr.getAdminArea(); //???
                String si = addr.getLocality(); //???
                String gu = addr.getThoroughfare(); //???
                String gu_str=getCompleteWord(gu,"???","???");

                tv_now_location.setText("?????? "+si + " " + gu_str+" : "); //?????? ????????? ???????????? :

                try {
                    temperature = new weatherTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, latitude, longitude).get();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int temperInt = (int) Math.round(temperature); //????????? ?????????
                tv_now_temperature.setText(temperInt + TEMPER_CODE);//24???
                isNowApplied = true;

            }
        }
        public void onProviderDisabled(String provider) {
            // Disabled???
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled???
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // ?????????
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };



    /**
     * ??????,????????? ???????????????
     * @param lat
     * @param lng
     * @return ??????
     */
    public String getAddress(Context mContext, double lat, double lng) {
        String addressStr= null;
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;
        try {
            //????????? ??????????????? ????????? ?????? ????????? ?????? ?????? ?????????
            //???????????? ?????? ??????????????? ????????? ????????????????????? ??????????????? ???????????? ?????? ???????????? ??????
            address = geocoder.getFromLocation(lat, lng, 1);
            if(address.size()==0){
                Toast.makeText(mContext,  "?????? ????????? ?????? ??? ??? ????????????.", Toast.LENGTH_SHORT).show();
                finish();
            }
            else addr = address.get(0);

            if (address.size() > 0) {
                // ?????? ????????????
                String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                addressStr  = currentLocationAddress;
            }
        } catch (IOException e) {
            System.out.println("????????? ????????? ??? ????????????.");
            e.printStackTrace();
            return null;
        }
        return addressStr;
    }




    public class weatherTask extends AsyncTask<Double, Void, Double> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            startTime = Util.getCurrentTime();
        }


        @Override
        protected Double doInBackground(Double... params)  {

            Retrofit client = new Retrofit.Builder().baseUrl("http://api.openweathermap.org").addConverterFactory(GsonConverterFactory.create()).build();
            ApiInterface service = client.create(ApiInterface.class);
            Call<Repo> call = service.repo(API_KEY, params[0], params[1]); //lat,lon
            Repo repo = null;
            try {
                repo = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "?????? ?????? ???????????? ??????", Toast.LENGTH_SHORT).show();
                return TEMPER_NULL;
            }
            assert repo != null;
            return repo.getMain().getTemp()- 273.15; //?????? return

        }

        @Override
        protected void onPostExecute(Double res) {
            super.onPostExecute(res);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == RECO_CODI && resultCode == RESULT_OK){
            setResult(RESULT_OK, intent);
            finish();
        }
        else if(requestCode == FROM_CLOSET && resultCode == RESULT_OK){
            //????????? ????????????
            Bundle extras = intent.getExtras();
            String cloNo = extras.getString("cloNo");
            boolean isExist=false;

            for (int i=0;i<7;i++){
                if(i==selected_clo_index)
                    continue;
                if(Integer.parseInt(cloNo)==child_clothes_no[i]){
                    isExist=true;
                    Toast.makeText(this, "?????? ??????????????????.", Toast.LENGTH_SHORT).show();
                }
            }

            if(!isExist){
                byte[] byteArray = intent.getByteArrayExtra("image");
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                //????????? ??????
                list_childClothes.get(selected_clo_index).setImageBitmap(bitmap);
                //????????? ?????????
                list_tv_childClothes.get(selected_clo_index).setVisibility(View.INVISIBLE);
                //??? ?????? ??????
                child_clothes_no[selected_clo_index] = Integer.parseInt(cloNo);
            }
        }else if(requestCode == REQUEST_PERMISSION && resultCode == RESULT_OK){
            setNowTemper();
        }else if(requestCode == REQUEST_PERMISSION && resultCode == RESULT_CANCELED){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                setNowTemper();
            }else{
                rb_weather_none.setChecked(true);
                Toast.makeText(this, "?????? ?????? ?????? ?????? ??? ?????? ??????????????????.", Toast.LENGTH_SHORT).show();
                setWeatherNone();
            }
        }

    }

    public final String getCompleteWord(String name, String firstValue, String secondValue) {

        char lastName = name.charAt(name.length() - 1);

        // ????????? ?????? ????????? ?????? ???????????? ????????? ??????
        if (lastName < 0xAC00 || lastName > 0xD7A3) {
            return name;
        }

        String selectedValue = (lastName - 0xAC00) % 28 > 0 ? firstValue : secondValue;
        return name+selectedValue;
    }

    void resetColor(int COLOR_PART){
        switch(COLOR_PART){
            case MAIN_COLOR:
                main_color_num = -1;
                tv_main_color.setText("?????????");
                civ_main_color.setColorFilter(Color.parseColor("#dddddd"));
                break;
            case SUB_COLOR :
                sub_color_num = -1;
                tv_sub_color.setText("?????????");
                civ_sub_color.setColorFilter(Color.parseColor("#dddddd"));
                break;
        }
    }

    void resetAllSpinners(){

        reset_spinner(spinner_top);
        spinner_top_detail.setVisibility(View.INVISIBLE);
        top_detail ="";

        reset_spinner(spinner_bottom);
        spinner_bottom_detail.setVisibility(View.INVISIBLE);
        bottom_detail ="";

        reset_spinner(spinner_suit);
        spinner_suit_detail.setVisibility(View.INVISIBLE);
        suit_detail ="";

        reset_spinner(spinner_outer);
        spinner_outer_detail.setVisibility(View.INVISIBLE);
        outer_detail ="";

        reset_spinner(spinner_shoes);
        spinner_shoes_detail.setVisibility(View.INVISIBLE);
        shoes_detail ="";

        reset_spinner(spinner_bag);
        spinner_bag_detail.setVisibility(View.INVISIBLE);
        bag_detail ="";

        reset_spinner(spinner_accessory);
        spinner_accessory_detail.setVisibility(View.INVISIBLE);
        accessory_detail ="";

    }

    void resetAllSetting(){

        for(CheckBox cb:checkBoxes){
            cb.setChecked(true);
        }
        resetColor(MAIN_COLOR);
        resetColor(SUB_COLOR);

        resetAllSpinners();
        resetAllItem();

        rb_all.setChecked(true);
        setWeatherNone();


    }



    @Override
    public void onBackPressed() {
        if (slidingDrawer.isOpened()) {
            slidingDrawer.close();
        }else{
            super.onBackPressed();
        }
    }





}