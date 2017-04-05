package com.example.ZMTCSD.activity;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alorma.timeline.RoundTimelineView;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.entity.OrderDetailsEntity;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.utils.StringUtil;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 订单服务项-- 服务项的状态
 */
@EActivity(R.layout.activity_order_action)
public class OrderItemsActionActivity extends BaseActivity {
    private ActionAdapter mAdapter;
    private List<OrderDetailsEntity.Actions> actionList;
    private int actionNo;
    @ViewById(R.id.recyclerView)
    RecyclerView mRecycler;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_OrderItemsAction));
    }

    @Override
    public void onAfterViews() {
        actionList = (List<OrderDetailsEntity.Actions>) getIntent().getSerializableExtra(AppDelegate.ORDER_ACTIONS);
        actionNo = getIntent().getIntExtra(AppDelegate.ORDER_ACTION_NAME, -2);
        initToolbar();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ActionAdapter(actionList);
        mRecycler.setAdapter(mAdapter);
    }

    public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ViewHolder> {
        private List<OrderDetailsEntity.Actions> actionList;

        public ActionAdapter(List<OrderDetailsEntity.Actions> actionList) {
            this.actionList = actionList;
        }

        @Override
        public int getItemCount() {
            return actionList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_action_list, viewGroup, false);
            return new ViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            OrderDetailsEntity.Actions entity = actionList.get(position);
            holder.name.setText(entity.getActionName());
            if (entity.getCrrateDate() != null) {
                holder.time.setText(StringUtil.dateRemoveT(entity.getCrrateDate()));
            } else holder.time.setText("--");

            if (position <= actionNo) {
                holder.timeline.setDrawInternal(true);//设置可以有中心圈
                holder.timeline.setLineColor(getColors(R.color.details_Approval));
                holder.name.setTextColor(getColors(R.color.details_Approval));
                holder.time.setTextColor(getColors(R.color.details_Approval));
            }

            if (position == 0) {
                holder.timeline.setTimelineType(RoundTimelineView.TYPE_START);
            }
            if (position == getItemCount() - 1) {
                holder.timeline.setTimelineType(RoundTimelineView.TYPE_END);
            }

        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            RoundTimelineView timeline;
            AppCompatTextView name;
            AppCompatTextView time;

            public ViewHolder(View itemView, int type) {
                super(itemView);
                timeline = (RoundTimelineView) itemView.findViewById(R.id.timeline_orderAction);
                name = (AppCompatTextView) itemView.findViewById(R.id.tv_name_orderAction);
                time = (AppCompatTextView) itemView.findViewById(R.id.tv_time_orderAction);
            }
        }
    }

    @Override
    public void onBackgrounds() {
        super.onBackgrounds();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
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
