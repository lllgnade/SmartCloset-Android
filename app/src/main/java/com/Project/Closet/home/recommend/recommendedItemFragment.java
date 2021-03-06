package com.Project.Closet.home.recommend;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.Project.Closet.Global;
import com.Project.Closet.HTTP.Service.SocialService;
import com.Project.Closet.HTTP.Session.preference.MySharedPreferences;
import com.Project.Closet.HTTP.VO.DetailFeedVO;
import com.Project.Closet.R;
import com.Project.Closet.social.detailFeed.activity_thisFeed;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link recommendedItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class recommendedItemFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String paramBoardNo = "boardInfo";

    // TODO: Rename and change types of parameters
    private ArrayList<DetailFeedVO> selectedFeedList;

    public recommendedItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param boardInfo Parameter 1.
     * @return A new instance of fragment recommendedItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static recommendedItemFragment newInstance(ArrayList<DetailFeedVO> boardInfo) {
        recommendedItemFragment fragment = new recommendedItemFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(paramBoardNo, boardInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedFeedList = getArguments().getParcelableArrayList(paramBoardNo);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_recommended_item, container, false);

        DetailFeedVO feedInfo = selectedFeedList.get(0);

        FrameLayout fl_recommendedItem = v.findViewById(R.id.fl_recommended_item);
        ImageView iv_codi_image = v.findViewById(R.id.iv_codi_image);
        TextView child1 = v.findViewById(R.id.child1);
        TextView child2 = v.findViewById(R.id.child2);
        TextView child3 = v.findViewById(R.id.child3);
        TextView child4 = v.findViewById(R.id.child4);
        ImageView iv_heart= v.findViewById(R.id.iv_heart);
        TextView numHeart = v.findViewById(R.id.tv_numHeart);

        Glide.with(getContext()).load(Global.baseURL+feedInfo.getBoardImagePath()).into(iv_codi_image);

        //?????? ????????? ?????? ????????? ??????
        String if_hearting = feedInfo.getBoard_if_hearting();
        if(if_hearting.equals("hearting")){
            iv_heart.setImageResource(R.drawable.heart_color);
        }
        else if(if_hearting.equals("not_hearting")){
            iv_heart.setImageResource(R.drawable.heart_empty_white);
        }
        numHeart.setText(feedInfo.getBoard_numHeart()+"");

        int childNum = selectedFeedList.size();
        TextView tv_childs[] = {child1,child2,child3,child4};


        // ?????? ??? ???????????? ????????? ????????????
        for(int i=0; i<childNum; i++){
            tv_childs[i].setVisibility(View.VISIBLE);
            tv_childs[i].setText("#"+selectedFeedList.get(i).getCloIdentifier());
        }
        // ??? ?????? ????????? ??????
        for(int i=3; i>childNum-1; i--){
            tv_childs[i].setVisibility(View.GONE);
        }

        BtnOnClickListener onClickListener = new BtnOnClickListener();
        fl_recommendedItem.setOnClickListener(onClickListener);
        iv_heart.setOnClickListener(onClickListener);


        // Inflate the layout for this fragment
        return v;
    }




    class BtnOnClickListener implements Button.OnClickListener {
        String res="";

        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {

                case R.id.iv_heart : //?????? ?????? ?????? ???????????? ???
                    break;
                case R.id.fl_recommended_item : //??????
                    intent = new Intent(getContext(), activity_thisFeed.class);
                    intent.putParcelableArrayListExtra("selectedFeedList", selectedFeedList);
                    startActivity(intent);
                    break;
            }
        }
    }

}