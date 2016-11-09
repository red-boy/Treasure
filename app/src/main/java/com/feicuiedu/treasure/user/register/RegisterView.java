package com.feicuiedu.treasure.user.register;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public interface RegisterView{

    // 完善视图接口
    void navigateToHome();//导航到HOME页面

    void showProgress();//显示注册中进度视图

    void hideProgress();//隐藏注册中进度视图

    void showMessage(String msg);//显示提示信息

}