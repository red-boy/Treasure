package com.feicuiedu.treasure.user.login;

import com.google.gson.annotations.SerializedName;

public class LoginResult {
//    {
//        "errcode": 1,                  //状态值
//            "errmsg": "登录成功！",        //返回信息
//            "headpic": "add.jpg",          //头像地址
//            "tokenid": 171                 //用户令牌
//    }
    @SerializedName("errcode")
    private int code;

    @SerializedName("headpic")
    private String iconUrl;

    @SerializedName("errmsg")
    private String msg;

    @SerializedName("tokenid")
    private int tokenId;

    public int getCode() {
        return code;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getMsg() {
        return msg;
    }

    public int getTokenId() {
        return tokenId;
    }
}
