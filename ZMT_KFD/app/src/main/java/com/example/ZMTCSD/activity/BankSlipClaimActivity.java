package com.example.ZMTCSD.activity;

/**
 * 水单认领界面
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import com.alibaba.fastjson.JSONObject;
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
import com.example.ZMTCSD.dal.MoreUserDal;
import com.example.ZMTCSD.entity.BankSlipClaimEntity;
import com.example.ZMTCSD.utils.DeviceUtil;
import com.example.ZMTCSD.utils.StringUtil;
import com.example.ZMTCSD.utils.ToastUtil;
import com.example.ZMTCSD.utils.VolleyErrorHelper;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import java.util.HashMap;
import java.util.Map;


@EActivity(R.layout.activity_bank_slip_claim)
public class BankSlipClaimActivity extends BaseActivity {
    private static final int BANK_CUSTOMER_REQUEST = 1; //请求客户的请求码
    private Context mContent;
    private double NotClaimAmount;//这是未认领的金额
    private double BankRMBRate; //这是银行的汇率
    private int BankSlipId;
    private RequestQueue mRequestQueue; //volley
    private String json; //这是你认领是发送的jsog格式
    private KProgressHUD hud;
    private int accountId;
    private String accountName;
    private String accountCode;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.tv_bankClaim_customerName)
    AppCompatTextView tv_customerName;

    @ViewById(R.id.med_bankClaim_claimAmount)
    MaterialEditText med_claimAmount;

    @ViewById(R.id.tv_bankClaim_Amount)
    AppCompatTextView tv_Amount; //剩余可认领

    @ViewById(R.id.tv_bankClaim_allAmount)
    AppCompatTextView tv_allAmount; //全部认领

    @ViewById(R.id.med_bankClaim_RMBRate)
    MaterialEditText med_RMBRate;

    @ViewById(R.id.tv_bankClaim_bankRate)
    AppCompatTextView tv_bankRate;

    @ViewById(R.id.tv_bankClaim_Claim)
    AppCompatButton mBankClaim;

    @ViewById(R.id.tv_rmbClaimAmount)
    AppCompatTextView tv_rmbClaimAmount;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_BankSlipClaim));
    }

    @Override
    public void onAfterViews() {
        mContent = BankSlipClaimActivity.this;
        initToolbar();
        mRequestQueue = Volley.newRequestQueue(mContent);
        hud = KProgressHUD.create(mContent).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("正在认领金额.").setCancellable(false);
        NotClaimAmount = getIntent().getDoubleExtra("netReceiptAmount", 0.00);
        BankRMBRate = getIntent().getDoubleExtra("bankRMBRate", 0.00);
        BankSlipId = getIntent().getIntExtra("bankSlipId", 0);
//        med_claimAmount.setText(StringUtil.numberFormat1(NotClaimAmount));
//        med_claimAmount.setCursorVisible(false);//隐藏光标
//        med_claimAmount.requestFocus();//光标放在最后
        tv_Amount.setText(getStrings(R.string.bankClaim_seven) + StringUtil.numberFormat(NotClaimAmount));
//        med_RMBRate.setText(StringUtil.numberForRate(BankRMBRate));
        tv_bankRate.setText(getStrings(R.string.bankClaim_eleven) + StringUtil.numberForRate(BankRMBRate));
        tv_rmbClaimAmount.setText(getStrings(R.string.bankClaim_twelve) );

        initTextChanged();
    }

    private void initTextChanged() {
        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String oldStr = dest.toString();
                String newStr = source.toString();
                //如果第一位输入小数点则自动补零,变成0.
                if (oldStr.length() == 0 && newStr.equals(".")) {
                    return "0.";
                }
                if (oldStr != null && oldStr.contains(".") && newStr.contains(".")) {
                    return "";
                }
                return null;
            }
        };

        tv_customerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (tv_customerName.length() != 0 && med_claimAmount.length() != 0 && med_RMBRate.length() != 0) {
                    mBankClaim.setEnabled(true);
                } else {
                    mBankClaim.setEnabled(false);
                }
            }
        });
        med_claimAmount.setFilters(new InputFilter[]{inputFilter});
        med_claimAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (med_claimAmount.length() != 0 && med_RMBRate.length() != 0) {
                    tv_rmbClaimAmount.setText(getStrings(R.string.bankClaim_twelve) + StringUtil.numberDecimal(
                            Double.valueOf(med_claimAmount.getText().toString()) * Double.valueOf(med_RMBRate.getText().toString())));
                    if (tv_customerName.length() != 0) {
                        mBankClaim.setEnabled(true);
                    } else
                        mBankClaim.setEnabled(false);
                } else {
                    tv_rmbClaimAmount.setText(getStrings(R.string.bankClaim_twelve));
                    mBankClaim.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }

            }
        });
        med_RMBRate.setFilters(new InputFilter[]{inputFilter});
        med_RMBRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (med_claimAmount.length() != 0 && med_RMBRate.length() != 0) {
                    tv_rmbClaimAmount.setText(getStrings(R.string.bankClaim_twelve) + StringUtil.numberDecimal(
                            Double.valueOf(med_claimAmount.getText().toString()) * Double.valueOf(med_RMBRate.getText().toString())));
                    if (tv_customerName.length() != 0) {
                        mBankClaim.setEnabled(true);
                    } else
                        mBankClaim.setEnabled(false);
                } else {
                    tv_rmbClaimAmount.setText(getStrings(R.string.bankClaim_twelve));
                    mBankClaim.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 4) {
                    edt.delete(posDot + 5, posDot + 6);
                }

            }
        });
    }

    @Click(R.id.ll_bankClaim_customer)
    void SelectCustomer() {
        BankSelectCustomerActivity_.intent(this).startForResult(BANK_CUSTOMER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (BANK_CUSTOMER_REQUEST == requestCode && RESULT_OK == resultCode) {
            accountName = data.getStringExtra("accountName");
            accountId = data.getIntExtra("accountId", 0);
            accountCode = data.getStringExtra("accountCode");
            tv_customerName.setText(accountName);
//            LogUtils.e("sss" + accountName + ":" + accountId + "ss" + accountCode);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Click(R.id.tv_bankClaim_allAmount)
    void OnAmountClaim() {
        med_claimAmount.setText(StringUtil.numberFormat1(NotClaimAmount));
    }


    @Click(R.id.tv_bankClaim_Claim)
    void OnDoClaim() {
        String amount = med_claimAmount.getText().toString().trim();
        String rmbRate = med_RMBRate.getText().toString().trim();

        if (Double.valueOf(amount) > NotClaimAmount) {
            ToastUtil.showToast(mContent, "认领金额不能大于可认领的金额");
        } else {
            BankSlipClaimEntity entity = new BankSlipClaimEntity();
            entity.setClaimAmount(amount);
            entity.setCustomerRMBRate(rmbRate);
            entity.setBankSlipId(BankSlipId);
            entity.setAccountId(accountId);
            entity.setAccountCode(accountCode);
            entity.setAccountName(accountName);
            json = JSONObject.toJSONString(entity);
//            LogUtils.e("" + json.toString());
            hud.show();
            onBackgrounds();
        }
    }

    @Override
    public void onBackgrounds() {
        //http://183.129.133.147:10086/api/bankslip/Claim
        final String url = MoreUserDal.GetServerUrl() + "/api/bankslip/Claim";
        LogUtils.d(json.toString() + "水单认领的url:" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {
                LogUtils.d("水单认领成功");
                //TODO 由于是在详情中认领的所以成功后返回 刷新详情 并且刷新列表
                hud.dismiss();
                LocalBroadcastManager.getInstance(mContent).sendBroadcastSync(new Intent(AppDelegate.BANK_SLIP_CLAIM_FRAGMENT));
                LocalBroadcastManager.getInstance(mContent).sendBroadcastSync(new Intent(AppDelegate.BANK_SLIP_CLAIM_DETAILS));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d("水单认领错误" + VolleyErrorHelper.getMessage(error, mContent));
                hud.dismiss();
                ToastUtil.showToast(mContent, "水单认领金额失败" + VolleyErrorHelper.getMessage(error, mContent));
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
        mRequestQueue.add(jsonObjectRequest);
        super.onBackgrounds();
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
        mRequestQueue.cancelAll(this);
        finish();
        super.onDestroy();
    }
}
