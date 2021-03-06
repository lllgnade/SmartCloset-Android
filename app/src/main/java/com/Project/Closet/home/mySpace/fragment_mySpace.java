package com.Project.Closet.home.mySpace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.Project.Closet.Global;
import com.Project.Closet.HTTP.Service.SocialService;
import com.Project.Closet.HTTP.Session.preference.MySharedPreferences;
import com.Project.Closet.HTTP.VO.DetailFeedVO;
import com.Project.Closet.HTTP.VO.FollowVO;
import com.Project.Closet.HTTP.VO.UserspaceVO;
import com.Project.Closet.R;
import com.Project.Closet.activity_login;
import com.Project.Closet.activity_profile;
import com.Project.Closet.home.activity_home;
import com.Project.Closet.social.space.subfragment.TabPagerAdapter_space;
import com.Project.Closet.util.NumFormat;
import com.Project.Closet.util.OnBackPressedListener;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;

import static com.Project.Closet.util.NumFormat.formatNumStringZero;

public class fragment_mySpace extends Fragment implements OnBackPressedListener {


    Toast toast;
    long backKeyPressedTime;

    //int ADD_BOARD = 8080;

    Activity activity;

    private TabLayout tabLayout;
    public TabPagerAdapter_mySpace pagerAdapter;
    private ViewPager finalPager;

    LinearLayout drawer;


    Call<UserspaceVO> userspaceCall;
    UserspaceVO userInfoSmall;
    DetailFeedVO userspaceInfo;


    String myID;
    public String targetID;

    Button bt_follow;

    TextView tv_numFollower;

    int gridsize=2;
    String pageSize="8";


    public static fragment_mySpace newInstance() {

        Bundle args = new Bundle();

        fragment_mySpace fragment = new fragment_mySpace();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_space, container,false);
        toast = Toast.makeText(getContext(),"?????? ??? ????????? ???????????????.",Toast.LENGTH_SHORT);



        MySharedPreferences pref = MySharedPreferences.getInstanceOf(getContext());
        myID = pref.getUserID();

        targetID = myID;
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
        System.out.println(userInfoSmall.getPfImagePath());
        userspaceInfo.setUserPfContents(userInfoSmall.getPfContents());
        userspaceInfo.setUser_if_following(userInfoSmall.getIf_following());
        userspaceInfo.setUser_following_friendsID(userInfoSmall.getFollowing_friendsID());
        userspaceInfo.setUser_followig_friendsName(userInfoSmall.getFollowing_friendsName());
        userspaceInfo.setUser_followig_friendsImgPath(userInfoSmall.getFollowing_friendsImgPath());
        userspaceInfo.setUserNumBoard(Integer.toString(userInfoSmall.getNumBoard()));
        userspaceInfo.setUserNumFollower(Integer.toString(userInfoSmall.getNumFollower()));
        userspaceInfo.setUserNumFollowing(Integer.toString(userInfoSmall.getNumFollowing()));




        //?????? ??????
        ImageView iv_profileImage = v.findViewById(R.id.iv_profileImage);
        Glide.with(getContext()).load(Global.getOriginalPath(userspaceInfo.getUserPfImagePath())).into(iv_profileImage);
        //????????? ??????
        TextView tv_id = v.findViewById(R.id.tv_id);
        tv_id.setText("@"+targetID);
        //????????? ??????
        TextView tv_nickname = v.findViewById(R.id.tv_nickname);
        tv_nickname.setText(userspaceInfo.getUserName());
        //?????????, ?????????, ????????? ??? ??????
        TextView tv_numBoard = v.findViewById(R.id.tv_numBoard);
        tv_numFollower = v.findViewById(R.id.tv_numFollower);
        TextView tv_numFollowing = v.findViewById(R.id.tv_numFollowing);
        tv_numBoard.setText(formatNumStringZero(Integer.parseInt(userspaceInfo.getUserNumBoard())));
        tv_numFollower.setText(formatNumStringZero(Integer.parseInt(userspaceInfo.getUserNumFollower())));
        tv_numFollowing.setText(formatNumStringZero(Integer.parseInt(userspaceInfo.getUserNumFollowing())));
        //????????? ??????
        TextView tv_pfContents = v.findViewById(R.id.tv_pfContents);
        if(userspaceInfo.getUserPfContents()==null){
            tv_pfContents.setVisibility(View.GONE);
        }else{
            tv_pfContents.setText(userspaceInfo.getUserPfContents());
        }

        LinearLayout ll_following_friends = v.findViewById(R.id.ll_following_friends);

        //????????? ?????? ??????
        bt_follow = v.findViewById(R.id.bt_follow);
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
                ImageView iv_following_friendsImg = v.findViewById(R.id.iv_following_friendsImg);
                Glide.with(getContext()).load(Global.getOriginalPath(userspaceInfo.getUser_followig_friendsImgPath())).into(iv_following_friendsImg);
                //?????? ????????? ??????
                TextView tv_following_friendsName = v.findViewById(R.id.tv_following_friendsName);
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

        toast = Toast.makeText(getContext(),"?????? ??? ????????? ???????????????.",Toast.LENGTH_SHORT);

        drawer = v.findViewById(R.id.final_drawer_layout);

        //BtnOnClickListener onClickListener = new BtnOnClickListener();


        if(tabLayout == null){
            //??? ?????? ??????
            tabLayout = v.findViewById(R.id.tabLayout);
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.grid));
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.heart_empty));



            //??? ????????? ?????? (??? ????????? ????????? ??????)
            finalPager = v.findViewById(R.id.tab_Pager);
            pagerAdapter = new TabPagerAdapter_mySpace(getChildFragmentManager(), tabLayout.getTabCount());
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


        //???????????? ??????
        Button bt_logout = v.findViewById(R.id.bt_logout);
        bt_logout.setVisibility(View.VISIBLE);
        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySharedPreferences pref = MySharedPreferences.getInstanceOf(getContext());
                pref.setUserID("");
                startActivity(new Intent(getContext(), activity_login.class));
                ActivityCompat.finishAffinity(getActivity());
            }
        });


        return v;
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

    }


    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            toast.show();
            return;
        } else if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            activity.finish();
            toast.cancel();
        }
    }



    public class userInfoTask extends AsyncTask<String, Void, UserspaceVO> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            startTime = Util.getCurrentTime();
        }


        @Override
        protected UserspaceVO doInBackground(String... params) {


            userspaceCall = SocialService.getRetrofit(getContext()).showUserSpace(myID, myID);

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

            Call<String> stringCall = SocialService.getRetrofit(getContext()).executeFollow(followInfo);

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