package com.Project.Closet.social.space;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

import com.Project.Closet.Global;
import com.Project.Closet.HTTP.Service.SocialService;
import com.Project.Closet.HTTP.Session.preference.MySharedPreferences;
import com.Project.Closet.HTTP.VO.DetailFeedVO;
import com.Project.Closet.HTTP.VO.FollowVO;
import com.Project.Closet.HTTP.VO.UserspaceVO;
import com.Project.Closet.R;
import com.Project.Closet.social.space.subfragment.TabPagerAdapter_space;
import com.Project.Closet.util.NumFormat;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;

import static com.Project.Closet.util.NumFormat.formatNumStringZero;

public class activity_space extends AppCompatActivity {

    Toast toast;
    long backKeyPressedTime;

    //int ADD_BOARD = 8080;

    //Activity activity;

    private TabLayout tabLayout;
    public TabPagerAdapter_space pagerAdapter;
    private ViewPager finalPager;

    LinearLayout drawer;


    Call<UserspaceVO> userspaceCall;
    UserspaceVO userInfoSmall;

    DetailFeedVO userspaceInfo;

    String myID;
    public String targetID;

    Button bt_follow;

    TextView tv_numFollower;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space);

        MySharedPreferences pref = MySharedPreferences.getInstanceOf(getApplicationContext());
        myID = pref.getUserID();

        userspaceInfo = getIntent().getExtras().getParcelable("feedInfo");
        if(userspaceInfo == null){
            try {
                userInfoSmall = new userInfoTask().execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            userspaceInfo = new DetailFeedVO();

            userspaceInfo.setUserID(userInfoSmall.getUserID());
            userspaceInfo.setUserName(userInfoSmall.getNickname());
            userspaceInfo.setUserPfImagePath(userInfoSmall.getPfImagePath());
            userspaceInfo.setUserPfContents(userInfoSmall.getPfContents());
            userspaceInfo.setUser_if_following(userInfoSmall.getIf_following());
            userspaceInfo.setUser_following_friendsID(userInfoSmall.getFollowing_friendsID());
            userspaceInfo.setUser_followig_friendsName(userInfoSmall.getFollowing_friendsName());
            userspaceInfo.setUser_followig_friendsImgPath(userInfoSmall.getFollowing_friendsImgPath());
            userspaceInfo.setUserNumBoard(Integer.toString(userInfoSmall.getNumBoard()));
            userspaceInfo.setUserNumFollower(Integer.toString(userInfoSmall.getNumFollower()));
            userspaceInfo.setUserNumFollowing(Integer.toString(userInfoSmall.getNumFollowing()));
        }



        targetID = userspaceInfo.getUserID();

        //?????? ??????
        ImageView iv_profileImage = findViewById(R.id.iv_profileImage);
        Glide.with(getApplicationContext()).load(Global.getOriginalPath(userspaceInfo.getUserPfImagePath())).into(iv_profileImage);
        //????????? ??????
        TextView tv_id = findViewById(R.id.tv_id);
        tv_id.setText("@"+targetID);
        //????????? ??????
        TextView tv_nickname = findViewById(R.id.tv_nickname);
        tv_nickname.setText(userspaceInfo.getUserName());
        //?????????, ?????????, ????????? ??? ??????
        TextView tv_numBoard = findViewById(R.id.tv_numBoard);
        tv_numFollower = findViewById(R.id.tv_numFollower);
        TextView tv_numFollowing = findViewById(R.id.tv_numFollowing);
        tv_numBoard.setText(formatNumStringZero(Integer.parseInt(userspaceInfo.getUserNumBoard())));
        tv_numFollower.setText(formatNumStringZero(Integer.parseInt(userspaceInfo.getUserNumFollower())));
        tv_numFollowing.setText(formatNumStringZero(Integer.parseInt(userspaceInfo.getUserNumFollowing())));
        //????????? ??????
        TextView tv_pfContents = findViewById(R.id.tv_pfContents);
        if(userspaceInfo.getUserPfContents()==null){
            tv_pfContents.setVisibility(View.GONE);
        }else{
            tv_pfContents.setText(userspaceInfo.getUserPfContents());
        }

        LinearLayout ll_following_friends = findViewById(R.id.ll_following_friends);

        //????????? ?????? ??????
        bt_follow = findViewById(R.id.bt_follow);
        final String myID = MySharedPreferences.getInstanceOf(getApplicationContext()).getUserID();
        if(myID.equals(targetID)){
            bt_follow.setVisibility(View.GONE);
            ll_following_friends.setVisibility(View.GONE);
        }
        else{
            bt_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String result = new followTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myID,targetID).get();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            // ?????? ???????????? ????????? ??? ?????? ????????? ???????????? ???????????? ???????????? ?????????.
            if(userspaceInfo.getUser_following_friendsID()==null){ //??????
                ll_following_friends.setVisibility(View.GONE);
            }else{ //??????
                //?????? ?????? ??????
                ImageView iv_following_friendsImg = findViewById(R.id.iv_following_friendsImg);
                Glide.with(getApplicationContext()).load(Global.getOriginalPath(userspaceInfo.getUser_followig_friendsImgPath())).into(iv_following_friendsImg);
                //?????? ????????? ??????
                TextView tv_following_friendsName = findViewById(R.id.tv_following_friendsName);
                tv_following_friendsName.setText(userspaceInfo.getUser_followig_friendsName());
            }
        }



        

        if(userspaceInfo.getUser_if_following().contains("not_following")){
            ViewCompat.setBackgroundTintList(bt_follow, ColorStateList.valueOf(Color.parseColor("#aa0055af")));
            bt_follow.setTextColor(Color.parseColor("#ffffff"));
            bt_follow.setText("?????????");
        }else if(userspaceInfo.getUser_if_following().contains("following")){
            ViewCompat.setBackgroundTintList(bt_follow, ColorStateList.valueOf(Color.parseColor("#ffffff")));
            bt_follow.setTextColor(Color.parseColor("#000000"));
            bt_follow.setText("?????????");
        }

        toast = Toast.makeText(getApplicationContext(),"?????? ??? ????????? ???????????????.",Toast.LENGTH_SHORT);

        drawer = findViewById(R.id.final_drawer_layout);

        //BtnOnClickListener onClickListener = new BtnOnClickListener();


        if(tabLayout == null){
            //??? ?????? ??????
            tabLayout = findViewById(R.id.tabLayout);
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.grid));
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.heart_empty));



            //??? ????????? ?????? (??? ????????? ????????? ??????)
            finalPager = findViewById(R.id.tab_Pager);
            pagerAdapter = new TabPagerAdapter_space(getSupportFragmentManager(), tabLayout.getTabCount());
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



    public class followTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            startTime = Util.getCurrentTime();
        }


        @Override
        protected String doInBackground(String... params) {
            FollowVO followInfo = new FollowVO(params[0],params[1]);
            //params : ?????????, ??????????????? ??????

            Call<String> stringCall = SocialService.getRetrofit(activity_space.this).executeFollow(followInfo);

            //?????? params[0]??? page.

            try {
                return stringCall.execute().body();

                // Do something with the response.
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if(result!=null) {
                String resCut[];
                String numFollow;
                if("fail".equals(result)){

                }else{
                    if(result.contains("not_following")){
                        resCut = result.split("_");
                        applyFollow(false,resCut[0]);
                    }else if(result.contains("following")) {
                        resCut = result.split("_");
                        applyFollow(true,resCut[0]);
                    }
                }
            }
            
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        //activity.setOnBackPressedListener(this);
    }

    //?????? ?????? ??????
    //@Override
    //public void onBackPressed() {
        //
    //}

    //?????? ?????????
    class BtnOnClickListener implements Button.OnClickListener {
        String res="";

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.header_add : //??????- ?????? ??????
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == ADD_BOARD && resultCode == RESULT_OK)
//            ((activity_home)activity).refresh_share();
    }




    public class userInfoTask extends AsyncTask<String, Void, UserspaceVO> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            startTime = Util.getCurrentTime();
        }


        @Override
        protected UserspaceVO doInBackground(String... params) {


            userspaceCall = SocialService.getRetrofit(activity_space.this).showUserSpace(targetID, myID);

            //?????? params[0]??? page.

            try {
                return userspaceCall.execute().body();

                // Do something with the response.
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(UserspaceVO userspaceInfo) {
            super.onPostExecute(userspaceInfo);
            if(userspaceInfo!=null) {
                //
            }
        }
    }





    public void applyFollow(boolean is_following, String numFollow){
        if(!is_following){
            ViewCompat.setBackgroundTintList(bt_follow, ColorStateList.valueOf(Color.parseColor("#aa0055af")));
            bt_follow.setTextColor(Color.parseColor("#ffffff"));
            bt_follow.setText("?????????");
        }else if(is_following){
            ViewCompat.setBackgroundTintList(bt_follow, ColorStateList.valueOf(Color.parseColor("#ffffff")));
            bt_follow.setTextColor(Color.parseColor("#000000"));
            bt_follow.setText("?????????");
        }

        numFollow = NumFormat.formatNumString(Integer.parseInt(numFollow),false); //??? ?????????
        tv_numFollower.setText(numFollow);
    }
}