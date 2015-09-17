package org.zywx.wbpalmstar.plugin.chatkeyboard;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import org.json.JSONObject;
import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;

public class EUExChatKeyboard extends EUExBase implements Parcelable {

	public static final String CHATKEYBOARD_FUN_PARAMS_KEY = "chatKeyboardFunParamsKey";
	public static final String CHATKEYBOARD_ACTIVITY_ID = "chatKeyboardActivityID";
    static final String func_on_callback = "javascript:uexChatKeyboard.cbGetInputBarHeight";

	public static final int CHATKEYBOARD_MSG_OPEN = 0;
	public static final int CHATKEYBOARD_MSG_CLOSE = 1;
    public static final int CHATKEYBOARD_MSG_GET_INPUTBAR_HEIGHT = 2;
	private static LocalActivityManager mgr;

	public EUExChatKeyboard(Context context, EBrowserView view) {
		super(context, view);
		mgr = ((ActivityGroup) mContext).getLocalActivityManager();
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

	@Override
	public void onHandleMessage(Message msg) {
		if (msg.what == CHATKEYBOARD_MSG_OPEN) {
			handleOpen(msg);
		} else if (msg.what == CHATKEYBOARD_MSG_GET_INPUTBAR_HEIGHT) {
            handleGetInputBarHeight(msg);
        } else {
			handleMessageInChatKeyboard(msg);
		}
	}

	private void handleMessageInChatKeyboard(Message msg) {
		String activityId = CHATKEYBOARD_ACTIVITY_ID
				+ EUExChatKeyboard.this.hashCode();
		Activity activity = mgr.getActivity(activityId);

		if (activity != null && activity instanceof ACEChatKeyboardActivity) {
			String[] params = msg.getData().getStringArray(
					CHATKEYBOARD_FUN_PARAMS_KEY);
			ACEChatKeyboardActivity cActivity = ((ACEChatKeyboardActivity) activity);

			switch (msg.what) {
			case CHATKEYBOARD_MSG_CLOSE:
				handleClose(cActivity, mgr);
				break;
			}
		}
	}

	private void handleClose(ACEChatKeyboardActivity cActivity,
			LocalActivityManager mgr) {
		View decorView = cActivity.getWindow().getDecorView();
		mBrwView.removeViewFromCurrentWindow(decorView);
		String activityId = CHATKEYBOARD_ACTIVITY_ID
				+ EUExChatKeyboard.this.hashCode();
		mgr.destroyActivity(activityId, true);
	}

	private void handleOpen(Message msg) {
		String[] params = msg.getData().getStringArray(
                CHATKEYBOARD_FUN_PARAMS_KEY);
		try {
			String activityId = CHATKEYBOARD_ACTIVITY_ID
					+ EUExChatKeyboard.this.hashCode();
			ACEChatKeyboardActivity cActivity = (ACEChatKeyboardActivity) mgr
					.getActivity(activityId);
			if (cActivity != null) {
				return;
			}
			Intent intent = createActivityIntent(params);
			Window window = mgr.startActivity(activityId, intent);
			View decorView = window.getDecorView();
			DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					dm.widthPixels, RelativeLayout.LayoutParams.WRAP_CONTENT);
			addView2CurrentWindow(activityId, decorView, lp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    private void handleGetInputBarHeight(Message msg) {
         //当前输入框的高度是固定的，50dp
        int height = dp2px(mContext, 50);
        String result = "{\"height\":" + "\"" + height + "\"}";
        String jsCallBack = func_on_callback + "('" + result + "');";
        onCallback(jsCallBack);
    }

	private Intent createActivityIntent(String[] params) throws Exception {
		Intent intent = new Intent(mContext, ACEChatKeyboardActivity.class);
		JSONObject json = new JSONObject(params[0]);
		String emojicons = json
				.getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_EMOJICONS);
		String shares = json
				.getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_SHARES);
		intent.putExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_UEXBASE_OBJ, this);
		intent.putExtra(
				EChatKeyboardUtils.CHATKEYBOARD_EXTRA_EMOJICONS_XML_PATH,
				emojicons);
		intent.putExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_SHARES_XML_PATH,
				shares);

		boolean hasPlacehold = json
				.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_PLACEHOLD);
		if (hasPlacehold) {
			String placehold = json
					.getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_PLACEHOLD);
			intent.putExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_PLACEHOLD,
					placehold);
		}
		boolean hasTouchDownImg = json
				.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_TOUCHDOWNIMG);
		if (hasTouchDownImg) {
			String touchDownImg = json
					.getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_TOUCHDOWNIMG);
			intent.putExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_TOUCHDOWNIMG,
					touchDownImg);
		}
		boolean hasDragOutsideImg = json
				.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_DRAGOUTSIDEIMG);
		if (hasDragOutsideImg) {
			String dragOutsideImg = json
					.getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_DRAGOUTSIDEIMG);
			intent.putExtra(
					EChatKeyboardUtils.CHATKEYBOARD_EXTRA_DRAGOUTSIDEIMG,
					dragOutsideImg);
		}
		boolean hasTextColor = json
				.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_TEXTCOLOR);
		if (hasTextColor) {
			String textColor = json
					.getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_TEXTCOLOR);
			intent.putExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_TEXTCOLOR,
					BUtility.parseColor(textColor));
		}
		boolean hasTextSize = json
				.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_TEXTSIZE);
		if (hasTextSize) {
			String textSize = json
					.getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_TEXTSIZE);
			intent.putExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_TEXTSIZE,
					Float.parseFloat(textSize));
		}
        boolean hasInputMode = json
                .has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_INPUT_MODE);
        if (hasInputMode){
            String inputMode = json
                    .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_INPUT_MODE);
            intent.putExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_INPUT_MODE,
                    Integer.valueOf(inputMode));
        }
		return intent;
	}

	private void addView2CurrentWindow(final String activityId, final View child,
			RelativeLayout.LayoutParams parms) {
		int l = (int) (parms.leftMargin);
		int t = (int) (parms.topMargin);
		int w = parms.width;
		int h = parms.height;
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w, h);
		lp.gravity = Gravity.BOTTOM;
		lp.leftMargin = l;
		lp.topMargin = t;
		adptLayoutParams(parms, lp);
		mBrwView.addViewToCurrentWindow(child, lp);
		
		mBrwView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
                    float h = child.getHeight();
                    float y = event.getY();
                    if (dm.heightPixels - Math.abs(y) > h) {
                        Activity activity = mgr.getActivity(activityId);
                        if (activity != null
                                && activity instanceof ACEChatKeyboardActivity) {
                            ((ACEChatKeyboardActivity) activity).outOfViewTouch();
                        }
                    }
                }
                return false;
            }
        });
	}

	public void open(String[] params) {
		sendMessageWithType(CHATKEYBOARD_MSG_OPEN, params);
	}

	public void close(String[] params) {
		sendMessageWithType(CHATKEYBOARD_MSG_CLOSE, params);
	}

    public void getInputBarHeight(String[] params) {
        sendMessageWithType(CHATKEYBOARD_MSG_GET_INPUTBAR_HEIGHT, params);
    }
    
    /**
     * 隐藏键盘的接口
     * @param params
     */
    public void hideKeyboard(String[] params){
    	((Activity)mContext).runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				String activityId = CHATKEYBOARD_ACTIVITY_ID
						+ EUExChatKeyboard.this.hashCode();
		    	Activity activity = mgr.getActivity(activityId);
		        if (activity != null
		                && activity instanceof ACEChatKeyboardActivity) {
		            ((ACEChatKeyboardActivity) activity).outOfViewTouch();
		        }
			}
		});
    }

    public int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
	protected boolean clean() {
		close(null);
		return false;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}
}
