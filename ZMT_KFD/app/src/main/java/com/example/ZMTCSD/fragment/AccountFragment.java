package com.example.ZMTCSD.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import com.alibaba.fastjson.JSON;
import com.apkfuns.logutils.LogUtils;
import com.balysv.materialripple.MaterialRippleLayout;
import com.example.ZMTCSD.MyApplication_;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.activity.LoginActivity_;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.dal.ServerDal;
import com.example.ZMTCSD.entity.MoreUserEntity;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.entity.ServerDeployEntity;
import com.thinkcool.circletextimageview.CircleTextImageView;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

//账户管理
@EFragment(R.layout.my_basic_recycler)
public class AccountFragment extends BaseFragment {
    private Context mContext;
//    AccountAdapter
    private MyAccountAdapter mAdapter;
    private MyConterRecyclerAdapter mContentAdapter;
    private List<ServerDeployEntity> ServerList;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.recyclerView)
    RecyclerView  mRecycler;

    @ViewById(R.id.tv_no_data)
    AppCompatTextView tv_no_data;

    @Override
    public void onAfterViews() {
        setHasOptionsMenu(true);//将fragment中的菜单添加到activity.
        mContext=this.getActivity();
        hideView(mToolbar);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mRecycler.setHasFixedSize(true);
        ServerList=ServerDal.GetListServer();
        mContentAdapter=new MyConterRecyclerAdapter(ServerList);
        mRecycler.setAdapter(mContentAdapter);
        super.onAfterViews();
    }

    private class MyConterRecyclerAdapter extends RecyclerView.Adapter<MyConterRecyclerAdapter.ViewHolder> {

        private List<ServerDeployEntity> grouplist;

        public MyConterRecyclerAdapter(List<ServerDeployEntity> prolist) {
            if(prolist !=null){
                this.grouplist = prolist;
            }else{
                this.grouplist=new ArrayList<>();
            }
        }

        @Override
        public int getItemCount() {
            return grouplist.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_customer_attach_recycler, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final ServerDeployEntity ServerEntity = grouplist.get(position);
            holder.groupName.setText(ServerEntity.getServerName()+"("+ServerEntity.getServerRemark()+")");

            List<MoreUserEntity> moreList= MoreUserDal.SortMoreEntity(ServerEntity.getServerId());

            mAdapter=new MyAccountAdapter(moreList );
            holder.groupRecycler.setSwipeMenuItemClickListener(new OnSwipeMenuItemClickListener() {
                @Override
                public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
                    closeable.smoothCloseMenu();// 关闭被点击的菜单。
                    LogUtils.e( MoreUserDal.SortMoreEntity(grouplist.get( holder.getAdapterPosition()).getServerId()).get(adapterPosition).getServerId() + "sddsads"+ holder.groupRecycler.getAdapter().getItemCount()   );
                    List<MoreUserEntity> moreUserEntityList=MoreUserDal.SortMoreEntity(grouplist.get( holder.getAdapterPosition()).getServerId());
                    MoreUserEntity more=moreUserEntityList.get(adapterPosition);
                    //删除用户
                    MoreUserDal.CleanMoreUser(more.getLoginName(),more.getServerId());

                    mContentAdapter=new MyConterRecyclerAdapter(ServerDal.GetListServer());
                    mRecycler.setAdapter(mContentAdapter);
                }
            });
            holder.groupRecycler.setAdapter(mAdapter);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public AppCompatTextView groupName; // 分组的头部信息
             SwipeMenuRecyclerView groupRecycler;  //分组的具体信息

            public ViewHolder(final View itemView) {
                super(itemView);
                groupName = (AppCompatTextView) itemView.findViewById(R.id.tv_customer_attach_name);
                groupRecycler = (SwipeMenuRecyclerView) itemView.findViewById(R.id.recycler_customer_attach);
                groupRecycler.setLayoutManager(new LinearLayoutManager(mContext));
                groupRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                        .colorResId(R.color.line_color).marginResId(R.dimen.left_16, R.dimen.right_0) //记录左右的距离
                        .build());
                groupRecycler.setItemAnimator(new SlideInLeftAnimator(new OvershootInterpolator(1f)));
//                为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
//                 设置菜单创建器。
                groupRecycler.setSwipeMenuCreator(new SwipeMenuCreator() {
                    @Override
                    public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                        int size = getResources().getDimensionPixelSize(R.dimen.lay_60);
                        if (viewType == MyAccountAdapter.VIEW_TYPE_NONE) {// 根据Adapter的ViewType来决定菜单的样式、颜色等属性、或者是否添加菜单。
                        } else {
                        SwipeMenuItem deleteItem = new SwipeMenuItem(mContext).setImage(R.mipmap.ic_action_delete)
                                .setBackgroundDrawable(R.drawable.selector_swipemenu_red).setText("删除") // 文字，还可以设置文字颜色，大小等。。
                                .setWidth(size).setHeight(size).setTextColor(Color.WHITE);
                        swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
                    }
                    }
                });
//                 设置菜单Item点击监听。
            }
        }
    }

    private class MyAccountAdapter extends SwipeMenuAdapter<MyAccountAdapter.ViewHolder>{
        public static final int VIEW_TYPE_MENU = 1;
        public static final int VIEW_TYPE_NONE = 2;
        List<MoreUserEntity> moreList;

        public MyAccountAdapter(List<MoreUserEntity> moreList) {
            this.moreList = moreList;
        }

        @Override
        public int getItemViewType(int position) {
            //在这里判断如果是现在显示是账户就不会有删除。
             return MoreUserDal.SameMoreUser(moreList.get(position))? VIEW_TYPE_NONE : VIEW_TYPE_MENU;
        }

        @Override
        public View onCreateContentView(ViewGroup viewGroup, int viewType) {
           return  MaterialRippleLayout.on(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_company_list, viewGroup, false))
                    .rippleOverlay(true)//如果这是真的,涟漪在前景;错:背景
                    .rippleAlpha(0.1f)//α的涟漪
                    .rippleColor(getColors(R.color.color_ripple_btn))//涟漪的颜色
                    .rippleDelayClick(true)  //延迟调用onclicklistener直到涟漪反应结束
                    .rippleHover(true)//如果这是真的,悬停效果时画的观点是感动
                    .create();
//            return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_company_list, viewGroup, false);
        }

        @Override
        public MyAccountAdapter.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
            return  new ViewHolder(realContentView,viewType);
        }

        @Override
        public void onBindViewHolder(MyAccountAdapter.ViewHolder holder, int position) {
            MoreUserEntity entity=moreList.get(position);
            holder.UserName.setText(  MoreUserDal.getUserName(entity.getClaimsList(),"userName"));
            holder.LoginName.setText(MoreUserDal.getUserName(entity.getClaimsList(),"loginName"));
        }

        @Override
        public int getItemCount() {
            return moreList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public RelativeLayout AccountItem;
            public CircleTextImageView mCircleImg;
            public AppCompatTextView UserName;
            public AppCompatTextView LoginName;
            public AppCompatImageView  TickImg;
            public ViewHolder(View itemView, int viewType) {
                super(itemView);
                AccountItem= (RelativeLayout) itemView.findViewById(R.id.ll_content);
                mCircleImg= (CircleTextImageView) itemView.findViewById(R.id.img_circle);
                UserName= (AppCompatTextView) itemView.findViewById(R.id.tv_name);
                LoginName= (AppCompatTextView) itemView.findViewById(R.id.tv_value);
                TickImg= (AppCompatImageView) itemView.findViewById(R.id.img_tick);
                if(viewType == VIEW_TYPE_NONE){
                    showView(TickImg);
                    AccountItem.setEnabled(false);
                }
                AccountItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MoreUserEntity more=moreList.get(getLayoutPosition());
                        // TODO 保存现在显示的账户 并且更新
                        MyApplication_.getInstance().getUserInfoSp().edit().putString(AppDelegate.SP_MOREUSERENTITY, JSON.toJSONString(more)).commit();
//                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new HomeFragment_()).commit();
//                        AnimationSet set=new AnimationSet(true);
//                        set.addAnimation( );
                        LogUtils.e("账户 "+more.toString());
//                        Animation animation= AnimationUtils.loadAnimation(mContext,R.anim.push_left_in);
//                        getActivity().findViewById(R.id.fragment).startAnimation(animation);
//                        MainActivity_.intent(mContext).start();
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(AppDelegate.ACCOUNT_MOREUSER));
                    }
                });
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                LoginActivity_.intent(getActivity()).extra(AppDelegate.LOGIN_ADDUSER,false).start();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
