package com.example.ZMTCSD.activity;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.ZMTCSD.AppDelegate;
import com.example.ZMTCSD.R;
import com.example.ZMTCSD.entity.CompanyBanksEntity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 银行信息的详细信息
 */
@EActivity(R.layout.activity_banks_details)
public class BanksDetailsActivity extends BaseActivity {
    private CompanyBanksEntity banksEntity;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.tv_title)
    AppCompatTextView tv_title;

    @ViewById(R.id.tv_banksList_accountName)
    AppCompatTextView mAccountName;

    @ViewById(R.id.tv_banksList_companyName)
    AppCompatTextView mCompanyName;

    @ViewById(R.id.tv_banksList_statusStr)
    AppCompatTextView mStatusStr;

    @ViewById(R.id.tv_banksList_bankName)
    AppCompatTextView mBankName;

    @ViewById(R.id.tv_banksList_bankAccount)
    AppCompatTextView mBankAccount;

    @ViewById(R.id.tv_banksList_swiftCode)
    AppCompatTextView mSwiftCode;

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(getStrings(R.string.toolbar_Company_banks));
    }

    @Override
    public void onAfterViews() {
        banksEntity = (CompanyBanksEntity) getIntent().getSerializableExtra(AppDelegate.COMPANY_BANKS_DETAILS);
        initToolbar();
        mAccountName.setText(banksEntity.getAccountName());
        mCompanyName.setText(banksEntity.getCompanyName());
        mStatusStr.setText(banksEntity.getStatusStr());
        mBankName.setText(banksEntity.getBankName());
        mBankAccount.setText(banksEntity.getBankAccount());
        mSwiftCode.setText(banksEntity.getSwiftCode());
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
