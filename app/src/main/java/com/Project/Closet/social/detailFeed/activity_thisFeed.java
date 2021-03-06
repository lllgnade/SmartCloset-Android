package com.Project.Closet.social.detailFeed;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Project.Closet.Global;
import com.Project.Closet.HTTP.Service.ClothesService;
import com.Project.Closet.HTTP.Service.CodiService;
import com.Project.Closet.HTTP.Service.SocialService;
import com.Project.Closet.HTTP.Session.preference.MySharedPreferences;
import com.Project.Closet.HTTP.VO.ClothesVO;
import com.Project.Closet.HTTP.VO.CodiVO;
import com.Project.Closet.HTTP.VO.CommentFeedVO;
import com.Project.Closet.HTTP.VO.CommentVO;
import com.Project.Closet.HTTP.VO.DetailFeedVO;
import com.Project.Closet.HTTP.VO.HeartVO;
import com.Project.Closet.R;
import com.Project.Closet.closet.activity_cloInfo;
import com.Project.Closet.home.activity_home;
import com.Project.Closet.social.space.activity_space;
import com.Project.Closet.util.NumFormat;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;

public class activity_thisFeed extends AppCompatActivity {


    NestedScrollView scrollView;

    String userID, userName, userPfImagePath, userPfContents,
            user_if_following, board_if_hearting,
            boardNo, boardImagePath, boardContents, boardRegDate;
    String board_numHeart, board_numComment;

    ImageView iv_profileImage, iv_heart, iv_image;
    TextView tv_writerName, tv_pfContents, tv_numHeart, tv_numComment,
            tv_contents, tv_regDate;

    LinearLayout ll_bottom;
    EditText et_comment;

    int pos;
    ArrayList<DetailFeedVO> selectedFeedList;

    //int gridsize=2;
    String pageSize = "10";

    DetailFeedVO feed;

    int page = 0;
    RecyclerView rv_clothes_list;
    RecyclerView rv_comment_list;
    ArrayList<CommentFeedVO> commentList = new ArrayList();

    //?????????????????? ?????????
    ChildCloAdapter childCloAdapter;
    FeedCommentAdapter commentListAdapter;

    Call<List<CommentFeedVO>> commentListCall; // ????????? VO ???????????? ???????????? ?????? http ??????

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_thisfeed);

        pos = getIntent().getExtras().getInt("pos");
        selectedFeedList = getIntent().getExtras().getParcelableArrayList("selectedFeedList");
        feed = selectedFeedList.get(0);

        scrollView = findViewById(R.id.scrollView);

        ll_bottom = findViewById(R.id.ll_bottom);

        userID = feed.getUserID();
        userName = feed.getUserName();
        userPfImagePath = feed.getUserPfImagePath();
        userPfContents = feed.getUserPfContents();
        user_if_following = feed.getUser_if_following();
        board_if_hearting = feed.getBoard_if_hearting();
        boardNo = feed.getBoardNo();
        boardImagePath = feed.getBoardImagePath();
        boardContents = feed.getBoardContents();
        boardRegDate = feed.getBoardRegDate();
        board_numHeart = feed.getBoard_numHeart();
        board_numComment = feed.getBoard_numComment();


        tv_writerName = findViewById(R.id.tv_writerName);
        tv_pfContents = findViewById(R.id.tv_pfContents);
        tv_numHeart = findViewById(R.id.tv_numHeart);
        tv_numComment = findViewById(R.id.tv_numComment);
        tv_contents = findViewById(R.id.tv_contents);
        tv_regDate = findViewById(R.id.tv_regDate);

        iv_profileImage = findViewById(R.id.iv_profileImage);
        iv_image = findViewById(R.id.iv_codi_image);
        iv_heart = findViewById(R.id.iv_heart);


        //?????? ?????? ?????????
        long ts = Timestamp.valueOf(boardRegDate).getTime();
        boardRegDate = NumFormat.formatTimeString(ts);
        //??? ?????????
        String numHeartstr = NumFormat.formatNumString(Integer.parseInt(board_numHeart));
        String numCommentstr = NumFormat.formatNumString(Integer.parseInt(board_numComment));


        Glide.with(this).load(Global.baseURL + boardImagePath).into(iv_image);
        Glide.with(this).load(Global.baseURL + userPfImagePath).into(iv_profileImage);
        if (board_if_hearting.equals("hearting")) {
            iv_heart.setImageResource(R.drawable.heart_color);
        } else
            iv_heart.setImageResource(R.drawable.heart_empty);

        tv_writerName.setText(userName);
        if (userPfContents != null && !userPfContents.isEmpty())
            tv_pfContents.setText(userPfContents);
        else
            tv_pfContents.setVisibility(View.GONE);
        tv_numHeart.setText(numHeartstr);
        tv_numComment.setText(numCommentstr);
        tv_contents.setText(boardContents);
        tv_regDate.setText(boardRegDate);

        //?????? ??????????????? ?????? ???????????? ?????? ????????? ??????
        new commentListTask().execute(Integer.toString(page), boardNo);


        //??? ?????????????????? ????????? ?????????
        childCloAdapter = new ChildCloAdapter(selectedFeedList);
        childCloAdapter.setOnItemClickListener(new ChildCloAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos, DetailFeedVO cloInfo) {
                Intent intent = new Intent(activity_thisFeed.this, activity_cloInfo.class);
                intent.putExtra("cloInfo", cloInfo);
                startActivity(intent);
            }
        });
        //?????? ?????????????????? ????????? ?????????
        commentListAdapter = new FeedCommentAdapter(commentList);
        commentListAdapter.setOnItemClickListener(new FeedCommentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, final int pos) {

                /*

                LinearLayout ll_delete = v.findViewById(R.id.ll_delete);
                if(ll_delete.getVisibility()!=View.GONE) {
                    ll_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CommentVO commentInfo = new CommentVO();
                            commentInfo.setBoardNo(feed.getBoardNo());
                            commentInfo.setCommentNo(Integer.toString(commentList.get(pos).getCommentNo()));
                            new commentTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, commentInfo);
                        }
                    });
                }

                 */

            }
        });

        //??? ??????????????????
        rv_clothes_list = (RecyclerView) findViewById(R.id.rv_clothes_list);
        LinearLayoutManager nLinearLayoutManager = new LinearLayoutManager(this);
        rv_clothes_list.setLayoutManager(nLinearLayoutManager);
        rv_clothes_list.setAdapter(childCloAdapter);

        //?????? ??????????????????
        rv_comment_list = (RecyclerView) findViewById(R.id.rv_comment_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        rv_comment_list.setLayoutManager(mLinearLayoutManager);
        rv_comment_list.setAdapter(commentListAdapter);
        rv_comment_list.setNestedScrollingEnabled(true);
        /*
        rv_comment_list.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (!rv_comment_list.canScrollVertically(-1)) {
                    //???????????? ??????????????? ???????????? ????????????
                    //page = 0;
                    //new networkTask().execute(Integer.toString(page));
                    //Log.e("test","????????? ??????");
                }
                else if (!rv_comment_list.canScrollVertically(1)) {
                    //???????????? ??????????????? ???????????? ?????? ????????? ??? ????????? ??????
                    new commentListTask().execute(Integer.toString(++page),boardNo);
                    Log.e("test","????????? ??? ??????");
                }
                else {
                }
            }
        });

         */


        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisible >= totalItemCount - 1) {
                    Log.d("log", "lastVisibled");
                    //???????????? ??????????????? ???????????? ?????? ????????? ??? ????????? ??????
                    new commentListTask().execute(Integer.toString(++page), boardNo);
                    Log.e("test", "????????? ??? ??????");
                }
            }
        };

        rv_comment_list.addOnScrollListener(onScrollListener);


        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(rv_comment_list.getContext(),
                nLinearLayoutManager.getOrientation());
        rv_clothes_list.addItemDecoration(dividerItemDecoration1);

        BtnOnClickListener onClickListener = new BtnOnClickListener();


        LinearLayout profile_area = findViewById(R.id.profile_area);
        profile_area.setOnClickListener(onClickListener);

        //?????? ?????? ??????
        TextView send_comment = findViewById(R.id.send_comment);
        send_comment.setOnClickListener(onClickListener);
        et_comment = findViewById(R.id.et_comment);

        //???????????? ?????? ?????? ??????
        ImageView iv_inbox = findViewById(R.id.iv_inbox);
        iv_inbox.setOnClickListener(onClickListener);


        LinearLayout ll_icon_heart = findViewById(R.id.ll_icon_heart);
        ll_icon_heart.setOnClickListener(onClickListener);


    }

    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent;
            String myID;
            MySharedPreferences pref;
            String res = null;
            switch (view.getId()) {
                case R.id.profile_area:
                    intent = new Intent(getApplicationContext(), activity_space.class);
                    assert feed != null;
                    intent.putExtra("feedInfo", feed);
                    startActivity(intent);
                    break;
                case R.id.send_comment:
                    CommentVO commentInfo = new CommentVO();
                    myID = MySharedPreferences.getInstanceOf(getApplicationContext()).getUserID();
                    String comm_contents = et_comment.getText().toString();
                    commentInfo.setBoardNo(feed.getBoardNo());
                    commentInfo.setWriterID(myID);
                    commentInfo.setContents(comm_contents);
                    res = null;
                    try {
                        res = new commentTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, commentInfo).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!"fail".equals(res)) {
                        commentList.clear();
                        page = 0;
                        new commentListTask().execute(Integer.toString(page), boardNo);
                        scrollDown();
                        //rv_comment_list.scrollToPosition(commentList.size()-1);
                    }
                    break;
                case R.id.iv_inbox:
                    pref = MySharedPreferences.getInstanceOf(getApplicationContext());
                    myID = pref.getUserID();
                    res = null;
                    try {
                        CodiVO codiInfo = new CodiVO();
                        codiInfo.setFavorite("no");
                        codiInfo.setUserID(myID);
                        codiInfo.setFilePath(feed.getBoardImagePath());
                        String contents = feed.getBoardContents();

                        String[] season = getResources().getStringArray(R.array.Season);
                        for (String seasonStr : season) {
                            if (contents.contains("#" + seasonStr)) {
                                codiInfo.setSeason(seasonStr);
                            }
                        }

                        String[] place = getResources().getStringArray(R.array.Place);
                        for (String placeStr : place) {
                            if (contents.contains("#" + placeStr)) {
                                codiInfo.setPlace(placeStr);
                            }
                        }

                        if (contents.contains("#??????") || contents.contains("#??????")) {
                            codiInfo.setGender("??????");
                        } else if (contents.contains("#??????") || contents.contains("#??????")) {
                            codiInfo.setGender("??????");
                        }

                        res = new AddTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, codiInfo).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e("tag", res);

                    if ("ok".equals(res)) {
                        Toast.makeText(getApplicationContext(), "???????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "?????????????????????.", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.ll_icon_heart:
                    DetailFeedVO feedInfo = feed;
                    pref = MySharedPreferences.getInstanceOf(getApplicationContext());
                    myID = pref.getUserID();
                    try {
                        res = new heartTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, feedInfo.getBoardNo(), myID).get();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (res != null) {
                        String resCut[] = null;
                        String numHeart;
                        if ("fail".equals(res)) {
                            Toast.makeText(activity_thisFeed.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                        } else {
                            if (res.contains("not_hearting")) {
                                iv_heart.setImageResource(R.drawable.heart_empty);
                                resCut = res.split("_");
                                numHeart = NumFormat.formatNumString(Integer.parseInt(resCut[0])); //??? ?????????
                                tv_numHeart.setText(numHeart);
                                for (DetailFeedVO feed : selectedFeedList) {
                                    feed.setBoard_if_hearting("not_hearting");
                                    feed.setBoard_numHeart(resCut[0]);
                                }
                                //feedListByBoardNo.set(pos,selectedFeedList);
                                //notifyItemChanged(pos);
                            } else if (res.contains("hearting")) {
                                iv_heart.setImageResource(R.drawable.heart_color);
                                resCut = res.split("_");
                                numHeart = NumFormat.formatNumString(Integer.parseInt(resCut[0])); //??? ?????????
                                tv_numHeart.setText(numHeart);
                                for (DetailFeedVO feed : selectedFeedList) {
                                    feed.setBoard_if_hearting("hearting");
                                    feed.setBoard_numHeart(resCut[0]);
                                }
                                //feedListByBoardNo.set(pos,selectedFeedList);
                                //notifyItemChanged(pos);
                            }
                        }
                    }
                    break;

            }
        }
    }


    public class commentListTask extends AsyncTask<String, Void, List<CommentFeedVO>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            startTime = Util.getCurrentTime();
        }


        @Override
        protected List<CommentFeedVO> doInBackground(String... params) {


            commentListCall = SocialService.getRetrofit(getBaseContext()).showCommentInBoard(params[1], params[0], pageSize);
            //?????? params[0]??? page. [1]??? boardNo

            try {
                return commentListCall.execute().body();

                // Do something with the response.
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<CommentFeedVO> comments) {
            super.onPostExecute(comments);
            if (comments != null) {
                for (CommentFeedVO e : comments) {
                    //????????? ???????????? ????????? ??? ????????? url ???????????? ??????
                    commentList.add(e);
                }
                commentListAdapter.notifyDataSetChanged();
            }
        }
    }

    public class commentTask extends AsyncTask<CommentVO, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            startTime = Util.getCurrentTime();
        }


        @Override
        protected String doInBackground(CommentVO... params) {

            String mode;

            if (params[0].getCommentNo() == null)
                mode = "add";
            else
                mode = "delete";


            Call<String> stringCall;
            switch (mode) {
                case "add":
                    stringCall = SocialService.getRetrofit(getApplicationContext()).addComment(params[0]);
                    break;
                case "delete":
                    stringCall = SocialService.getRetrofit(getApplicationContext()).deleteComment(params[0].getCommentNo(), params[0].getBoardNo());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + mode);
            }

            //?????? params[0]??? CommentVO

            try {
                return stringCall.execute().body();

                // Do something with the response.
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String numComment) {
            super.onPostExecute(numComment);
            if (numComment != null && !numComment.equals("fail")) {
                et_comment.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);

                selectedFeedList.get(0).setBoard_numComment(numComment);
                numComment = NumFormat.formatNumString(Integer.parseInt(numComment));//?????????
                tv_numComment.setText(numComment);
            } else
                Toast.makeText(activity_thisFeed.this, "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteComment(int pos) throws ExecutionException, InterruptedException {
        CommentVO commentInfo = new CommentVO();
        commentInfo.setBoardNo(feed.getBoardNo());
        commentInfo.setCommentNo(Integer.toString(commentList.get(pos).getCommentNo()));
        String res = new commentTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, commentInfo).get();
        if (!"fail".equals(res)) {
            commentList.remove(pos);
            commentListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("pos", pos);
        intent.putParcelableArrayListExtra("feedInfo", selectedFeedList);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();

    }


    public class AddTask extends AsyncTask<CodiVO, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(CodiVO... codiInfo) {

            Call<String> stringCall = CodiService.getRetrofit(getApplicationContext()).addCodiFrData(codiInfo[0]);
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


    public class heartTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            startTime = Util.getCurrentTime();
        }


        @Override
        protected String doInBackground(String... params) {
            HeartVO heartInfo = new HeartVO(params[0], params[1]);
            //params : ????????? ??????, ??????

            Call<String> stringCall = SocialService.getRetrofit(getApplicationContext()).executeHeart(heartInfo);

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
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
        }
    }


    private void scrollDown() {
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        }, 200);

    }
}








