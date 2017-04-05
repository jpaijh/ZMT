package com.example.ZMTCSD;

/**
 * 代理类
 */
public final class AppDelegate {

//    public static final String BASE_URL="http://www.tjzmt.com:10087";
    public static final String BASE_URL = "http://183.129.133.147:10086";//这自贸通的测试客服端
    /**
     * 订单中结算单的url
     */
    public static final String ORDER_SETTLE_PDF_URL = "http://www.tjzmt.com:9080/manage/orderExport/SettlePdf/";
    /**
     * 增票 中增值税发票的url
     */
    public static final String INVOICE_SETTLE_PDF_URL = "http://www.tjzmt.com:9080/manage/vatInvoice/VatInvoiceExportPdf/";
    /**
     * 付款 中 付款单文件的url
     */
    public static final String PAYMENT_SETTLE_PDF_URL = "http://www.tjzmt.com:9080/manage/PaymentApplyManager/exportExcelPdf/";

    /**
     * SharedPreferences-Refresh_Date 保存所有列表下拉刷新时间的sp
     */
    public static final String SP_REFRESH_DATE = "Refresh_Date";

    /**
     * SharedPreferences-User_Info 存储用户信息的sp
     */
    public static final String SP_USER_INFO = "User_Info";

    /**
     * SharedPreferences-SP_MetaDate_DATE 保存元数据
     */
    public static final String SP_META_DATE="Meta_date";
    /**
     * SharedPreferences-SP_SERVER_DATE 保存所有的服务器信息
     */
    public static final String SP_SERVER_DATE="server_date";

    /**
     * SharedPreferences-SP_List<MOREUSERENTITY> date 保存所有的用户的信息
     */
    public static final String SP_LIST_MOREUSERENTITY="list_MoreUserEntity";

    /**
     * SharedPreferences-SP_MOREUSERENTITY 当前用户的信息
     */
    public static final String SP_MOREUSERENTITY="MoreUserEntity";

    /**
     * SharedPreferences-User_Info-isLogin 存储是否已经登录
     */
    public static final String IS_LOGIN = "isLogin";

    /**
     * SharedPreferences-User_Info- metadata_TIME  存储最新获取元数据的时间
     */
    public static final String METADATE_TIME = "metadata_time";
    public static final long TIME = 300 *1000;
//    /**
//     * SharedPreferences-User_Info-isRefresh 储存是否token刷新成功。
//     */
//    public static final String IS_REFRESH="isRefresh";

    public static final String TOOLBAR_NAME = "toolbar_name";
    /**
     * SharedPreferences-Search_History 存储历史搜索记录的sp(我审批列表搜索记录，商品列表关键字搜索记录)广播动作
     */
    public static final String SP_SEARCH_HISTORY = "Search_History";
    /**
     * SharedPreferences-Search_History-HISTORY_APPROVAL_MANAGE_KEY_SEARCH 关键字搜索界面-审批管理关键字搜索历史记录
     */
    public static final String HISTORY_APPROVAL_MANAGE_KEY_SEARCH = "HISTORY_APPROVAL_MANAGE_KEY_SEARCH";

    /**
     * 我审批 我发起 侧边栏搜索的广播动作
     */
    public static final String APPROCVAL_DRAWER_SCREEN = "android.intent.action.APPROCVAL_DRAWER_SCREEN";
    public static final String APPROCVAL_LIST_SCREEN="Approval_screen";
    /**
     *  客户列表中的筛选广播动作
     */
    public static final String CUSTOMER_DRAWER_SCREEN="android.intent.action.CUSTOMER_DRAWER_SCREEN";
    public static final String CUSTOMER_LIST_SCREEN="Customer_screen";
    /**
     * 新建客户 时的广播动作。
     */
    public static final String CUSTOMER_NEW_REFRESH="android.intent.action.CUSTOMER_NEW_REFRESH";
    /**
     *  客户详情中的附件删除或者增加时，更改客户详情的广播动作
     */
    public static final String CUSTOMER_ATTACHMENT_REFRESH = "android.intent.action.CUSTOMER_ATTACHMENT_REFRESH";

    /**
     * 订单列表中的筛选广播动作
     */
    public static final String ORDER_DRAWER_SCREEN="android.intent.action.ORDER_DRAWER_SCREEN";
    public static final String ORDER_LIST_SCREEN="Order_screen";
    public static final String ORDER_META_DATE="android.intent.action.ORDER_META_DATE";

    /**
     *  增票列表中的筛选广播动作
     */
    public static final String INVOICE_DRAWER_SCREEN="android.intent.action.INVOICE_DRAWER_SCREEN";
    public static final String INVOICE_LIST_SCREEN="Invoice_screen";

    /**
     *  付款列表中的筛选广播动作
     */
    public static final String PAYMENT_DRAWER_SCREEN="android.intent.action.PAYMENT_DRAWER_SCREEN";
    public static final String PAYMENT_LIST_SCREEN="Payment_screen";

    //在付款单中的 删除付款单
    public static final String PAYMENT_DETAILS_REMOVE="android.intent.action.PAYMENT_DETAILS_REMOVE";
    //在付款单中的 提交付款单
    public static final String PAYMENT_DETAILS_COMMIT="android.intent.action.PAYMENT_DETAILS_COMMIT";

    //待客提款成功后 刷新付款列表
    public static final String PAYMENT_NEW_FRAGMENT="android.intent.action.PAYMENT_NEW_FRAGMENT";
    //待客提款中所有选择的 筛选广播动作
    public static final String PAYMENT_NEW_SELECTOR="android.intent.action.PAYMENT_NEW_SELECTOR";


    /**
     *  水单列表中的筛选广播动作
     */
    public static final String BANK_SLIP_DRAWER_SCREEN="android.intent.action.BANK_SLIP_DRAWER_SCREEN";
    public static final String BANK_SLIP_LIST_SCREEN="Bank_Slip_screen";
    /**
     * 这是水单认领成功后 刷新水单列表和水单详情
     */
    public static final String BANK_SLIP_CLAIM_FRAGMENT="android.intent.action.BANK_SLIP_CLAIM_FRAGMENT";
    public static final String BANK_SLIP_CLAIM_DETAILS="android.intent.action.BANK_SLIP_CLAIM_DETAILS";

    /**
     * 这是 中信保的外商代码 搜索的状态
     */
    public static final String COMPANY_BUYER_CODE_DRAWER_SCREEN="android.intent.action.COMPANY_BUYER_CODE_DRAWER_SCREEN";
    public static final String COMPANY_BUYER_CODE_LIST_SCREEN="android.intent.action.COMPANY_BUYER_CODE_LIST_SCREEN";

    /**
     * 这是 中信保的投保 搜索的状态
     */
    public static final String COMPANY_INSURE_DRAWER_SCREEN="android.intent.action.COMPANY_INSURE_DRAWER_SCREEN";
    public static final String COMPANY_INSURE_LIST_SCREEN="android.intent.action.COMPANY_INSURE_LIST_SCREEN";

    /**
     * 这是 中信保的限额 搜索的状态
     */
    public static final String COMPANY_CLLIMIT_DRAWER_SCREEN="android.intent.action.COMPANY_CLLIMIT_DRAWER_SCREEN";
    public static final String COMPANY_CLLIMIT_LIST_SCREEN="android.intent.action.COMPANY_CLLIMIT_LIST_SCREEN";

    /**
     * 这是 中信保的银行 搜索的状态
     */
    public static final String COMPANY_BANk_DRAWER_SCREEN="android.intent.action.COMPANY_BANk_DRAWER_SCREEN";
    public static final String COMPANY_BANk_LIST_SCREEN="android.intent.action.COMPANY_BANk_LIST_SCREEN";

    //中信报 查看批复。
    public static  final String REPLYSKEYWORD="applyKeyword";
    /**
     * 我审批的 商品搜索关键字,订单搜索外销发票号关键字
     */
    public static final String KEYWORD = "keyword";
    /**
     * 我审批 刷新列表数据的广播动作
     */
    public static final String ACTION_REFRESH_LIST_DATA = "android.intent.action.ACTION_REFRESH_LIST_DATA";

    /**
     * 在详情中审批的 刷新详情数据的广播动作
     */
    public static final String ACTION_REFRESH_DETAILS_DATA="android.intent.action.ACTION_REFRESH_DETAILS_DATA";

    /**
     * 我审批的进入详情界面中传的值  这个单据的id   列表进入审批，和详情进入审批
     */
    public static final String APPROVALID=" approvalId";

    /**
     * 我审批的进入详情界面中传的值 这个单据的flag； 列表进入审批，和详情进入审批
     */
    public static final String FLAG="flag";
    /**
     * 审批列表和详情中 进入审批单据的界面的需要的单据的 名称和时间
     */
    public static final String APPROVAL_REPORTERNAME ="reporterName";
    public static final String APPROVAL_REPORTERDATE ="reporterDate";

    /**
     * -APPROVAL_BASE_URL  审批列表的api
     */
    public static final String APPROVAL_BASE_URL="Approval_base_url";
    /**
     * APPROVAL_LIST_TYPE  列表和详情，根据不同的type来显示不同的数据 //传递是我审批的还是 我发起的
     */
    public static final String APPROVAL_LIST_TYPE="Approval_list_Type";

    /**
     * 我审批的 和我发起的, 审批侧边栏列表中判断是那个 分页上。
     * */
    public static final String DRAWER_APPROVAL_TYPE="Drawer_Approval_type";

    /**
     * DeTAILS_PHOTOVVIEW  在详情中的附件中的图片传递到打开图片
     */
    public static final String DETAILS_PHOTOVIEW="Details_photoView";

    public static final String DETAILS_WEBVIEW="Details_webView";
    public static final String DETAILS_WEBVIEW_NAME="Details_webView_name";

    /**
     * LOGIN_ADDUSER 增加多用户的
     */
    public static final String LOGIN_ADDUSER="login_addUser";
    /**
     * -ACCOUNT_MOREUSER  删除用户时,更新用户的数据
     */
    public static final String ACCOUNT_MOREUSER="account_MoreUser";

    /**
     * 将快捷短语缓存到 本地中 ACache_listtop 同意的短语
     */
    public static final String ACACHE_LISTTOP="ACache_ListTop";
    /**
     * 将快捷短语缓存到 本地中 ACache_listButton 不同意的短语
     */
    public static final String ACACHE_LISTBUTTON="ACache_ListButton";

    /**
     *将服务器的配置信息传到服务器的详细界面中来显示
     */
    public static final String  SERVER_ITEM="Server_item";

    /**
     * 将客户的id从列表到详细 客户的id从详情到来访记录 往来单位的id从列表到详细  订单的id从列表到详细
     * 上传附件时需要的用户的id 增票的列表到详情  水单列表到详情  付款的列表到详情
     * 信息列表到信息详情时的id    水单列表到水单详情  中信保限额列表到详情
     * 中信保到查看批复   中信保的外商代码到详情
     */
    public static final String CUSTOMER_ID_COMPANY="CustomerIdCompany";

    /**
     * 用户详细中传递到基本信息  或者 往来单位详细中的基本信息
     */
    public static final String CUSTOMER_BASIC_COMPANY="CustomerBasicCompany";

    /**
     * 用户详细中传递到附件  或者 往来单位详细中的附件
     */
    public static final String CUSTOMER_ATTACH_COMPANY="CustomerAttachCompany";

    /**
     * 订单详情中的服务项中的   具体服务
     */
    public static final String ORDER_ITEMS ="OrderItems";
    /**
     * 订单详情中的服务项中的   状态  从详情中到服务项
     */
    public static final String ORDER_ACTION_NAME ="Order_actionName";
    /**
     * 服务项详情的 到合并前后的商品
     */
    public static final String ORDER_DETAIL_SUBGROUPS ="Order_details_subGroup";
    public static final String ORDER_DETAIL_SUBGROUPNAME ="Order_details_subGroupName";
    /**
     * 服务项详情中的状态到服务项状态界面
     */
    public static final String ORDER_ACTIONS ="Order_Actions";

    /**
     *  上传附件时的内容里的上传到那个用户的name  添加用户来访记录收需要name
     */
    public static final String  CUSTOMER_ATTACH_NAME="Customer_Attach_name";
    /**
     * 将外来单位中的银行信息传到银行的具体信息界面
     */
    public static final String  COMPANY_BANKS_DETAILS="Company_banks_details";

    /**
     *  水单中的列表中的分辨是 待认领 还是  已认领
     */
    public static final String BANKSLIP_LIST_TYPE="BankSlip_list_type";
    public static final String BANKSLIP_DETAIL_ORDER="BankSlip_detail_order";

    /**
     * 分辨是往来单位的详情还是 外商代码的详情
     */
    public static final String TYPE="BankSlip_list_type";

    /**
     * 待客提款 中需要的常量
     */
    public static final String PYNEW_CUSTOMERID="Payment_Customer";
    public static final String PYNEW_CUSTOMERNAME="Payment_CustomerName";
    public static final String PYNEW_CUSTOMERENTITY="Payment_CustomerEntity";

    public static final String PYNEW_METHODLIST="PaymentMethodList";
    public static final String PYNEW_METHOD="PaymentMethod";

    public static final String PYNEW_SUBTYPELIST="PaymentSubTypeList";
    public static final String PYNEW_SUBTYPE="PaymentSubType";

    public static final String PYNEW_GUARANTEETYPELIST="GuaranteeTypeList";
    public static final String PYNEW_GUARANTEETYPE="GuaranteeType";

    public static final String PYNEW_CURRENCY="Currency";
    public static final String PYNEW_CURRENCYLIST="CurrencyList";

    public static final String PYNEW_COMPANYID="Payment_CompanyEntity";
    public static final String PYNEW_COMPANYENTITY="Payment_CompanyEntity";
    public static final String PYNEW_COMPANYBANKENTITY="Payment_CompanyBankEntity";

    //在待客提款 选择客户和收款单位，收款银行的哪一个
    public static final String PYNEW_SELECT_TYPE="Payment_New_Select_type";

    public static final String PYNEW_FOUR_ORDERENTITY="Payment_Four_OrderEntity";
    public static final String PYNEW_FOUR_ORDERCODE="Payment_Four_OrderCode";

    public static final String PYNEW_FINANCE_ORDERENTITY="Payment_financeOrderEntity";
    public static final String PYNEW_PAYMENTACCOUNT="Payment_account";
    public static final String PYNEW_PAYMENTACCOUNTENTITY="Payment_accountEntity";

    private static AppDelegate mInstance = new AppDelegate();

    private AppDelegate() {
    }

    public static AppDelegate getInstance() {
        return mInstance;
    }

    private MyApplication mApp;

    public void init(MyApplication context) {
        this.mApp = context;
    }
}
