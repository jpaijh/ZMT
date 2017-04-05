package com.example.ZMTCSD.activity;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.adapter.PhraseRecyclerAdapter;
import com.example.ZMTCSD.dal.PhraseDal;
import com.example.ZMTCSD.entity.PhraseEntity;
import com.example.ZMTCSD.helper.MyItemTouchCallback;
import com.example.ZMTCSD.utils.DeviceUtil;
import com.example.ZMTCSD.utils.StringUtil;
import com.example.ZMTCSD.utils.ToastUtil;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_setting_phrase)
public class Setting_PhraseActivity extends BaseActivity implements PhraseRecyclerAdapter.OnStartDragListener {
    private Context mContent;
    private PhraseRecyclerAdapter adapterTop;
    private PhraseRecyclerAdapter adapterButton;
//    private ACache mCache;
    private List<PhraseEntity> listtop;
    private List<PhraseEntity> listbutton = new ArrayList<>();
    private LinearLayoutManager layoutManagerTop;
    private LinearLayoutManager layoutManagerButton;
    private ItemTouchHelper itemTouchHelper;//拖动、和侧滑删除
    private ItemTouchHelper itemTouchHelper2;

    @ViewById(R.id.phrase_recycler_ok)
    RecyclerView mRecycler;

    @ViewById(R.id.phrase_recycler_Unok)
    RecyclerView mRecyclerUnok;

    @ViewById(R.id.img_phrase_ok)
    AppCompatImageView imgOk;

    @ViewById(R.id.img_phrase_Unok)
    AppCompatImageView imgUnok;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @Override
    public void onAfterViews() {
        mContent = this;
        inittoolbar();
        listtop= PhraseDal.GetPhraseTop();
        listbutton=PhraseDal.GetPhraseButton();

        //创建LinearLayoutManager
        layoutManagerTop = new LinearLayoutManager(this);
        //为RecyclerView指定布局管理器对象
        mRecycler.setLayoutManager(layoutManagerTop);
        adapterTop = new PhraseRecyclerAdapter(listtop, true);
        adapterTop.setmDragStartListener(this);
        mRecycler.setAdapter(adapterTop);

        mRecycler.setItemAnimator(new DefaultItemAnimator());//设置删除，增加的动画
        //拖动、和侧滑删除
        ItemTouchHelper.Callback MyCallbackTop = new MyItemTouchCallback(adapterTop, true);
        itemTouchHelper = new ItemTouchHelper(MyCallbackTop);
        itemTouchHelper.attachToRecyclerView(mRecycler);

        layoutManagerButton = new LinearLayoutManager(mContent);
        mRecyclerUnok.setLayoutManager(layoutManagerButton);
        adapterButton = new PhraseRecyclerAdapter(listbutton, false);
        adapterButton.setmDragStartListener(this);
        mRecyclerUnok.setAdapter(adapterButton);
        mRecyclerUnok.setItemAnimator(new DefaultItemAnimator());
        //拖动、和侧滑删除
        ItemTouchHelper.Callback MyCallbackButton = new MyItemTouchCallback(adapterButton, false);
        itemTouchHelper2 = new ItemTouchHelper(MyCallbackButton);
        itemTouchHelper2.attachToRecyclerView(mRecyclerUnok);

    }

    private void inittoolbar() {
        mToolbar.setTitle("");
        mToolbar.inflateMenu(R.menu.activity_login);
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_SettingPhrase));
    }

    @Click(R.id.ll_phrase_ok)
    void goAddItemTop() {
        if (adapterTop.getItemCount() < 8) {
            int position = layoutManagerTop.findLastCompletelyVisibleItemPosition();
            adapterTop.addItem("", position, true);
        } else {

        }
    }

    @Click(R.id.ll_phrase_Unok)
    void goAddItemButton() {
        if (adapterButton.getItemCount() < 8) {
            int position = layoutManagerButton.findLastCompletelyVisibleItemPosition();
            adapterButton.addItem("", position, false);
        }
    }

    void goKeep() {
        List<PhraseEntity> TopEnd = StringUtil.RemoveMcacheList(adapterTop.getListtop());
        List<PhraseEntity> TopButton = StringUtil.RemoveMcacheList(adapterButton.getListbutton());
        LogUtils.e(TopEnd.toString()+"数据"+TopButton.toString());
        //存入缓存
        PhraseDal.SetPhraseTopSp(TopEnd);
        PhraseDal.SetPhraseButtonSp(TopButton);

        DeviceUtil.hideSoft(this,mRecyclerUnok);
//        //TODO 刷新activity
        adapterTop.setListtop(PhraseDal.GetPhraseTop());
        adapterTop.notifyDataSetChanged();
        adapterButton.setListbutton(PhraseDal.GetPhraseButton());
        adapterButton.notifyDataSetChanged();
        ToastUtil.showToast(mContent,"保存成功");
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
        itemTouchHelper2.startDrag(viewHolder);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_delete:
                goKeep();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }
}
