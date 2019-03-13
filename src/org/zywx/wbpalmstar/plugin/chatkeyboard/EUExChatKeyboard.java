/*
 *  Copyright (C) 2014 The AppCan Open Source Project.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.

 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.zywx.wbpalmstar.plugin.chatkeyboard;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.engine.EBrowserActivity;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

public class EUExChatKeyboard extends EUExBase {

    private static final String TAG = "EUExChatKeyboard";
    private static final String CHATKEYBOARD_FUN_PARAMS_KEY = "chatKeyboardFunParamsKey";

    private static final int CHATKEYBOARD_MSG_OPEN = 0;
    private static final int CHATKEYBOARD_MSG_CLOSE = 1;
    private static final int CHATKEYBOARD_MSG_GET_INPUTBAR_HEIGHT = 2;
    private static final int CHATKEYBOARD_MSG_HIDE_KEYBOARD = 3;

    private ACEChatKeyboardView mChatKeyboardView;
    private String[] openParams;

    public EUExChatKeyboard(Context context, EBrowserView view) {
        super(context, view);
    }

    @Override
    public void onHandleMessage(Message msg) {
        if (msg == null) {
            return;
        }
        switch (msg.what) {
            case CHATKEYBOARD_MSG_OPEN:
                handleOpen(msg);
                break;
            case CHATKEYBOARD_MSG_CLOSE:
                handleClose();
                break;
            case CHATKEYBOARD_MSG_HIDE_KEYBOARD:
                handleHideKeyboard();
                break;
            default:
                ;
        }
    }

    public void open(String[] params) {
        openParams = params;
        // android6.0以上动态权限申请
        if (mContext.checkCallingOrSelfPermission(Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED){
            requsetPerssions(Manifest.permission.RECORD_AUDIO, "请先申请权限"
                    + Manifest.permission.RECORD_AUDIO, 1);
        } else {
            sendMessageWithType(CHATKEYBOARD_MSG_OPEN, params);
        }
    }

    public void close(String[] params) {
        sendMessageWithType(CHATKEYBOARD_MSG_CLOSE, params);
    }

    public int getInputBarHeight(String[] params) {
        //当前输入框的高度是固定的，50dp
        int height = EUExUtil.dipToPixels(50);
        JSONObject result = new JSONObject();
        try {
            result.put(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_HEIGHT, height);
        } catch (JSONException e) {
        }
        String jsCallBack = SCRIPT_HEADER
                + "if("
                + EChatKeyboardUtils.CHATKEYBOARD_FUN_CB_GET_INPUTBAR_HEIGHT
                + "){"
                + EChatKeyboardUtils.CHATKEYBOARD_FUN_CB_GET_INPUTBAR_HEIGHT
                + "('" + result.toString() + "');}";
        onCallback(jsCallBack);
        return height;
    }

    public void hideKeyboard(String[] params) {
        sendMessageWithType(CHATKEYBOARD_MSG_HIDE_KEYBOARD, params);
    }
    
    public void setPlaceholder(final String[] params){
        if(params == null || params.length < 1){
            return;
        }
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject(params[0]);
                    String hint = json.getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_PLACEHOLDER);
                    if(mChatKeyboardView != null){
                        mChatKeyboardView.setHint(hint);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendMessageWithType(int msgType, String[] params) {
        if (mHandler == null) {
            return;
        }
        Message msg = new Message();
        msg.what = msgType;
        msg.obj = this;
        Bundle b = new Bundle();
        b.putStringArray(CHATKEYBOARD_FUN_PARAMS_KEY, params);
        msg.setData(b);
        mHandler.sendMessage(msg);
    }

    public void insertTextByKeyword(final String[] params){
        if(params == null || params.length < 1){
            return;
        }
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject(params[0]);
                    String keyword = json.getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_KEYWORD);
                    String inserText = json.getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_INSERTTEXT);
                    String keywordColor = json.optString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_INSERTTEXTCOLOR);
                    boolean isReplaceKeyword = "1".equals(json
                            .optString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_ISREPLACEKEYWORD,"1"));
                    if(mChatKeyboardView != null){
                        mChatKeyboardView.insertTextByKeyword(keyword,inserText,keywordColor,isReplaceKeyword);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleOpen(Message msg) {
        String[] params = msg.getData().getStringArray(
                CHATKEYBOARD_FUN_PARAMS_KEY);
        if (params == null || params.length < 1) return;
        try {
            if (mChatKeyboardView != null) {
                handleClose();
            }
            JSONObject json = new JSONObject(params[0]);
            mChatKeyboardView = new ACEChatKeyboardView(mContext, json, this);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            lp.bottomMargin=json.optInt("bottom",0);
            addView2CurrentWindow(mChatKeyboardView, lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleClose() {
        if (mChatKeyboardView == null) {
            return;
        }
        removeViewFromCurrentWindow(mChatKeyboardView);
        mChatKeyboardView.onDestroy();
        mChatKeyboardView = null;
    }

    /**
     * 隐藏键盘的接口
     */
    private void handleHideKeyboard() {
        if (mChatKeyboardView != null) {
            mChatKeyboardView.outOfViewTouch();
        }
    }

    private void addView2CurrentWindow(View child,
                                       RelativeLayout.LayoutParams parms) {
        int l = (int) (parms.leftMargin);
        int t = (int) (parms.topMargin);
        int w = parms.width;
        int h = parms.height;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w, h);
        lp.gravity = Gravity.BOTTOM;
        lp.leftMargin = l;
        lp.bottomMargin = parms.bottomMargin;
        lp.topMargin = t;
        adptLayoutParams(parms, lp);
        mBrwView.addViewToCurrentWindow(child, lp);
    }

    @Override
    protected boolean clean() {
        Log.i(TAG, "clean");
        close(null);
        if (mChatKeyboardView != null) {
            mChatKeyboardView.onDestroy();
        }
        return false;
    }

    public boolean setText(String[] params){
        if (mChatKeyboardView!=null){
            return mChatKeyboardView.setText(params[0]);
        }
        return false;
    }

    public String getText(String[] params){
        if (mChatKeyboardView!=null){
            return mChatKeyboardView.getText();
        }
        return null;
    }

    @Override
    public void onRequestPermissionResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if (grantResults[0] != PackageManager.PERMISSION_DENIED){
                sendMessageWithType(CHATKEYBOARD_MSG_OPEN, openParams);
            } else {
                // 对于 ActivityCompat.shouldShowRequestPermissionRationale
                // 1：用户拒绝了该权限，没有勾选"不再提醒"，此方法将返回true。
                // 2：用户拒绝了该权限，有勾选"不再提醒"，此方法将返回 false。
                // 3：如果用户同意了权限，此方法返回false
                // 拒绝了权限且勾选了"不再提醒"
                if (!ActivityCompat.shouldShowRequestPermissionRationale((EBrowserActivity)mContext, permissions[0])) {
                    Toast.makeText(mContext, "请先设置权限" + permissions[0], Toast.LENGTH_LONG).show();
                } else {
                    requsetPerssions(Manifest.permission.RECORD_AUDIO, "请先申请权限" + permissions[0], 1);
                }
            }
        }
    }
}
