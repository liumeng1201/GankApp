package com.lm.android.gankapp.listener;

import android.content.Context;

import cn.bmob.v3.listener.SaveListener;

/**
 * 自定义bmob saveListener
 * Created by liumeng on 2015/12/30.
 */
public abstract class MyBmobSaveListener extends SaveListener {
    private Context context;

    public MyBmobSaveListener() { }

    public MyBmobSaveListener(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess() {
        successOpt();
    }

    /**
     * 操作成功要执行的操作
     */
    protected abstract void successOpt();

    @Override
    public void onFailure(int i, String s) {
        // 对bmob出错信息进行统一处理
        switch (i) {
            case 9001:
                s = "Application Id为空，请初始化";
                break;
            case 9002:
                s = "解析返回数据出错";
                break;
            case 9003:
                s = "上传文件出错";
                break;
            case 9004:
                s = "文件上传失败";
                break;
            case 9005:
                s = "批量操作只支持最多50条";
                break;
            case 9006:
                s = "objectId为空";
                break;
            case 9007:
                s = "文件大小超过10M";
                break;
            case 9008:
                s = "上传文件不存在";
                break;
            case 9009:
                s = "没有缓存数据";
                break;
            case 9010:
                s = "网络超时";
                break;
            case 9011:
                s = "BmobUser类不支持批量操作";
                break;
            case 9012:
                s = "上下文为空";
                break;
            case 9013:
                s = "BmobObject（数据表名称）格式不正确";
                break;
            case 9014:
                s = "第三方账号授权失败";
                break;
            case 9016:
                s = "无网络连接，请检查您的手机网络";
                break;
            case 9017:
                s = "与第三方登录出错：" + s;
                break;
            case 9018:
                s = "参数不能为空";
                break;
            case 9019:
                s = "格式不正确：手机号码、邮箱地址、验证码";
                break;
            case 9015:
            default:
                s = "其他错误";
                break;
        }

        failureOpt(i, s);
    }

    /**
     * 操作失败时要执行的操作
     *
     * @param i 错误号
     * @param s 错误描述
     */
    protected abstract void failureOpt(int i, String s);
}
