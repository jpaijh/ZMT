package com.example.ZMTCSD.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.apkfuns.logutils.LogUtils;
import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.dal.MetaDataDal;
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.CustomerAccountAddEntity;
import com.example.ZMTCSD.utils.DeviceUtil;
import com.example.ZMTCSD.utils.StringUtil;
import com.example.ZMTCSD.utils.ToastUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

/**
 * 新建客户
 */
@EActivity(R.layout.activity_customer_new)
public class CustomerNewActivity extends BaseActivity {
    private Context mContent;
    private final static int CUSTOMER_TYPE = 1;
    private final static int CUSTOMER_SERVERITEM = 2;
    private RequestQueue mRequestQueue; //volley
    private KProgressHUD hud;
    private String type = "0";
    private String ItemIds;
    private String json;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.tv_new_accountType)
    AppCompatTextView accountType;

    @ViewById(R.id.med_new_loginName)
    MaterialEditText med_loginName;

    @ViewById(R.id.med_new_customerName)
    MaterialEditText med_customerName; //请输入客户名称

    @ViewById(R.id.med_new_contactName)
    MaterialEditText med_contactName;

    @ViewById(R.id.med_new_contactPhone)
    MaterialEditText med_contactPhone;

    @ViewById(R.id.med_new_email)
    MaterialEditText med_email;

    @ViewById(R.id.tv_new_serviceItemId)
    AppCompatTextView serviceItemIds;

    @ViewById(R.id.tv_new_AccountAdd)
    AppCompatButton BtnAccount;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_CustomerNew));
    }

    @Override
    public void onAfterViews() {
        mContent = CustomerNewActivity.this;
        initToolbar();
        mRequestQueue = Volley.newRequestQueue(mContent);
        hud = KProgressHUD.create(mContent).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("正在创建客户").setCancellable(true);
        accountType.setText("国内工厂");
        serviceItemIds.setText(MetaDataDal.GetOneName());
        ItemIds = MetaDataDal.GetOneServerId();

        initEditText();
        super.onAfterViews();
    }

    private void initEditText() {
        med_loginName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (med_loginName.length() != 0 && med_customerName.length() != 0 && med_contactName.length() != 0 &&
                        med_contactPhone.length() != 0 && med_email.length() != 0 && serviceItemIds.length() != 0) {
                    BtnAccount.setEnabled(true);
                } else {
                    BtnAccount.setEnabled(false);
                }
            }
        });
        med_customerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (med_loginName.length() != 0 && med_customerName.length() != 0 && med_contactName.length() != 0 &&
                        med_contactPhone.length() != 0 && med_email.length() != 0 && serviceItemIds.length() != 0) {
                    BtnAccount.setEnabled(true);
                } else {
                    BtnAccount.setEnabled(false);
                }
            }
        });
        med_contactName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (med_loginName.length() != 0 && med_customerName.length() != 0 && med_contactName.length() != 0 &&
                        med_contactPhone.length() != 0 && med_email.length() != 0 && serviceItemIds.length() != 0) {
                    BtnAccount.setEnabled(true);
                } else {
                    BtnAccount.setEnabled(false);
                }
            }
        });
        med_contactPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (med_loginName.length() != 0 && med_customerName.length() != 0 && med_contactName.length() != 0 &&
                        med_contactPhone.length() != 0 && med_email.length() != 0 && serviceItemIds.length() != 0) {
                    BtnAccount.setEnabled(true);
                } else {
                    BtnAccount.setEnabled(false);
                }
            }
        });
        med_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (med_loginName.length() != 0 && med_customerName.length() != 0 && med_contactName.length() != 0 &&
                        med_contactPhone.length() != 0 && med_email.length() != 0 && serviceItemIds.length() != 0) {
                    BtnAccount.setEnabled(true);
                } else {
                    BtnAccount.setEnabled(false);
                }
            }
        });
        serviceItemIds.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (med_loginName.length() != 0 && med_customerName.length() != 0 && med_contactName.length() != 0 &&
                        med_contactPhone.length() != 0 && med_email.length() != 0 && serviceItemIds.length() != 0) {
                    BtnAccount.setEnabled(true);
                } else {
                    BtnAccount.setEnabled(false);
                }
            }
        });
    }

    @Click(R.id.ll_accountType)
    void SelectAccountType() {
        //进入选择客户的类型
        CustomerTypeActivity_.intent(mContent).extra("value", type).startForResult(CUSTOMER_TYPE);
    }

    @Click(R.id.ll_serviceItemIds)
    void SelectServerItems() {
        //选择服务项
        CustomerServerItemActivity_.intent(mContent).extra("serviceItemIds", ItemIds).startForResult(CUSTOMER_SERVERITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CUSTOMER_TYPE == requestCode && RESULT_OK == resultCode) {
            type = data.getStringExtra("value");
            accountType.setText(StringUtil.SelecterCustomerType(type));
        } else if (CUSTOMER_SERVERITEM == requestCode && RESULT_OK == resultCode) {
            ItemIds = data.getStringExtra("serviceItemIds");
            String name = data.getStringExtra("ItemNames");
            serviceItemIds.setText(name);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Click(R.id.tv_new_AccountAdd)
    void DoAccountAdd() {
        String loginName = med_loginName.getText().toString().trim();
        String customerName = med_customerName.getText().toString().trim();
        String contactName = med_contactName.getText().toString().trim();
        String contactPhone = med_contactPhone.getText().toString().trim();
        String email = med_email.getText().toString().trim();
        CustomerAccountAddEntity entity = new CustomerAccountAddEntity();
        entity.setContactName(contactName);
        entity.setContactPhoneNumber(contactPhone);
        entity.setEmail(email);
        entity.setLoginName(loginName);
        entity.setAccountType(type);
        entity.setCustomerName(customerName);
        entity.setServiceItemIds(ItemIds.split(","));
        json = com.alibaba.fastjson.JSONObject.toJSONString(entity);
        LogUtils.e("增加客户" + json.toString());
        hud.show();
        onAccountAdd();
    }

    @Background
    public void onAccountAdd() {
        final String url = MoreUserDal.GetServerUrl() + "/api/platformaccount/AccountAdd";
        LogUtils.e("增加客户" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                hud.dismiss();
                ToastUtil.showToast(mContent, "增加客户成功");
                Intent intent = new Intent(AppDelegate.CUSTOMER_NEW_REFRESH);
                LocalBroadcastManager.getInstance(mContent).sendBroadcastSync(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("增加客户失败" + VolleyErrorHelper.getMessage(error, mContent));
                hud.dismiss();
                ToastUtil.showToast(mContent, "增加客户失败" + VolleyErrorHelper.getMessage(error, mContent));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "bearer " + MoreUserDal.GetAccessToken());
                map.put("Content-Type", "application/json");
                return map;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() {
                return json.getBytes();
            }
        };
        jsonObjectRequest.setTag(this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50 * 1000, 1, 1.0f));
        mRequestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onBackgrounds() {
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cancel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
                DeviceUtil.hideSoft((Activity) mContent);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        DeviceUtil.hideSoft(this);
        mRequestQueue.cancelAll(mContent);
        finish();
        super.onDestroy();
    }
}
