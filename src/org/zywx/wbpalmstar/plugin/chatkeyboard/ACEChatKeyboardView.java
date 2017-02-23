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

import android.animation.LayoutTransition;
import android.animation.LayoutTransition.TransitionListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class ACEChatKeyboardView extends LinearLayout implements
        OnPageChangeListener, TextWatcher, OnClickListener, OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {

    private String TAG = "ACEChatKeyboardView";
    private String TIPS_RECORDER_NORMAL_TITLE = "按住  说话";
    private String TIPS_RECORDER_PRESS_TITLE = "松开 结束";
    private int TIPS_RECORDER_NORMAL_TITLE_COLOR = BUtility.parseColor("#aaa");
    private int TIPS_RECORDER_PRESS_TITLE_COLOR = BUtility.parseColor("#aaa");
    private EUExChatKeyboard mUexBaseObj;
    private View mOutOfTouchView;
    private EditTextEx mEditText;
    private ImageButton mBtnEmojicon;
    private Button mBtnSend;
    private ImageButton mBtnAdd;
    private ImageButton mBtnVoice;
    private Button mBtnVoiceInput;
    private LinearLayout mParentLayout;
    private LinearLayout mEditLayout;
    private LinearLayout mPagerLayout;
    private LinearLayout mEmojiconsLayout;
    private LinearLayout mChatKeyboard;
    private ViewPager mEmojiconsPager;
    private LinearLayout mEmojiconsIndicator;
    private LinearLayout mSharesLayout;
    private ViewPager mSharesPager;
    private LinearLayout mSharesIndicator;
    private LayoutTransition mLayoutTransition;
    private boolean isKeyBoardVisible;
    private String mEmojiconsDeletePath;
    private ArrayList<String> mEmojiconsPath = new ArrayList<String>();
    private ArrayList<String> mEmojiconsText = new ArrayList<String>();
    private int mEmojiconsPageIndex;
    private ArrayList<String> mSharesPath = new ArrayList<String>();
    private ArrayList<String> mSharesText = new ArrayList<String>();
    private int mSharesPageIndex;
    private InputMethodManager mInputManager;
    private FrameLayout mRecordTipsLayout;
    private ImageButton mRecordTipsImage;
    private BitmapDrawable mTouchDownImg = null;
    private Drawable mTouchDownImgDefaule = null;
    private BitmapDrawable mDragOutsideImg = null;
    private Drawable mDragOutsideImgDefaule = null;
    private TextView mRecordTimes;
    private String mEmojiconsXmlPath;
    private String mSharesXmlPath;
    private static int NUMBER_OF_EMOJICONS;
    private static int NUMBER_OF_EMOJICONS_PER_PAGE = 23;
    private static int NUMBER_OF_SHARES;
    private static int NUMBER_OF_SHARES_PER_PAGE = 8;
    private static int TIMER_HANDLER_MESSAGE_WHAT = 0;
    private int mInputMode = 0;
    private boolean isKeyboardChange = false;
    private int keyBoardHeight = 0;
    private int mBrwViewHeight = 0;
    private List<String> keywords = new ArrayList<String>();
    private int mLastAtPosition = 0;
    private boolean isShowSharesBtn = true; 

    public ACEChatKeyboardView(Context context, JSONObject params, EUExChatKeyboard uexBaseObj) {
        super(context);
        this.setOrientation(VERTICAL);
        this.setGravity(Gravity.BOTTOM);
        this.mUexBaseObj = uexBaseObj;
        CRes.init(getContext().getApplicationContext());
        mInputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        View inputLayout = LayoutInflater.from(getContext()).inflate(
                CRes.plugin_chatkeyboard_layout, null, false);
        mOutOfTouchView = new View(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        lp.weight = 1;
        LayoutParams lp2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp2.gravity = Gravity.BOTTOM;

        //this.addView(mOutOfTouchView,lp);
        this.addView(inputLayout, lp2);

        initView();
        initKeyboardParams(params);
        initEvent();

        initPagerIndicator();
        initLayoutTransition();

        switch (mInputMode) {
            case EChatKeyboardUtils.INPUT_MODE_TEXT:
                mBtnVoice.setSelected(false);
                mBtnVoiceInput.setVisibility(View.GONE);
                mEditLayout.setVisibility(View.VISIBLE);
                mEmojiconsLayout.setVisibility(View.GONE);
                mSharesLayout.setVisibility(View.GONE);
                mPagerLayout.setVisibility(View.GONE);
                break;
            case EChatKeyboardUtils.INPUT_MODE_VOICE:
                mBtnVoice.setSelected(true);
                mBtnVoiceInput.setVisibility(View.VISIBLE);
                mEditLayout.setVisibility(View.GONE);
                mEmojiconsLayout.setVisibility(View.GONE);
                mSharesLayout.setVisibility(View.GONE);
                mPagerLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void initView() {
        mParentLayout = (LinearLayout) findViewById(CRes.plugin_chatkeyboard_parent_layout);
        mPagerLayout = (LinearLayout) findViewById(CRes.plugin_chatkeyboard_pager_layout);

        mEditLayout = (LinearLayout) findViewById(CRes.plugin_chatkeyboard_edit_input_layout);
        mChatKeyboard = (LinearLayout) mEditLayout.getParent();
        mEditText = (EditTextEx) findViewById(CRes.plugin_chatkeyboard_edit_input);
        mBtnSend = (Button) findViewById(CRes.plugin_chatkeyboard_btn_send);
        mBtnAdd = (ImageButton) findViewById(CRes.plugin_chatkeyboard_btn_add);
        mBtnVoice = (ImageButton) findViewById(CRes.plugin_chatkeyboard_btn_voice);
        mBtnEmojicon = (ImageButton) findViewById(CRes.plugin_chatkeyboard_btn_emojicon);
        mBtnVoiceInput = (Button) findViewById(CRes.plugin_chatkeyboard_btn_voice_input);

        mRecordTipsLayout = (FrameLayout) findViewById(CRes.plugin_chatkeyboard_voice_record_tips_layout);
        mRecordTipsImage = (ImageButton) findViewById(CRes.plugin_chatkeyboard_voice_record_tips_image);
        mRecordTimes = (TextView) findViewById(CRes.plugin_chatkeyboard_voice_record_times);

        mEmojiconsLayout = (LinearLayout) findViewById(CRes.plugin_chatkeyboard_emojicons_layout);
        mEmojiconsPager = (ViewPager) findViewById(CRes.plugin_chatkeyboard_emojicons_pager);
        mEmojiconsIndicator = (LinearLayout) findViewById(CRes.plugin_chatkeyboard_emojicons_pager_indicator);

        mSharesLayout = (LinearLayout) findViewById(CRes.plugin_chatkeyboard_shares_layout);
        mSharesPager = (ViewPager) findViewById(CRes.plugin_chatkeyboard_shares_pager);
        mSharesIndicator = (LinearLayout) findViewById(CRes.plugin_chatkeyboard_shares_pager_indicator);
    }

    private void initEvent() {
        mBtnEmojicon.setOnClickListener(this);
        mEditText.addTextChangedListener(this);
        mEditText.setOnClickListener(this);

        mBtnSend.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);
        mBtnVoice.setOnClickListener(this);
        mBtnVoiceInput.setOnTouchListener(this);

        mEmojiconsPager.setOnPageChangeListener(this);
        mSharesPager.setOnPageChangeListener(this);

        mOutOfTouchView.setOnTouchListener(this);
        mParentLayout.setOnTouchListener(this);
        mParentLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private void initKeyboardParams(JSONObject json) {
        try {
            // mRecordTipsLayout init
            DisplayMetrics dm = getResources().getDisplayMetrics();
            mRecordTipsLayout.getLayoutParams().height = dm.heightPixels / 2;
            // EmojiconsXmlPath
            mEmojiconsXmlPath = json
                    .optString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_EMOJICONS);
            initEmojicons();
            mEmojiconsPager.setAdapter(new EmotjiconsPagerAdapter());
            // mSharesXmlPath
            mSharesXmlPath = json
                    .optString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_SHARES);
            initShares();
            mSharesPager.setAdapter(new SharesPagerAdapter());
            // placeHold @see placeholder
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_PLACEHOLD)) {
                String placeholder = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_PLACEHOLD);
                mEditText.setHint(placeholder);
            }
            // placeholder
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_PLACEHOLDER)) {
                String placeholder = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_PLACEHOLDER);
                mEditText.setHint(placeholder);
            }
            // touchDownImg
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_TOUCHDOWNIMG)) {
                String touchDownImg = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_TOUCHDOWNIMG);
                touchDownImg = touchDownImg.substring(BUtility.F_Widget_RES_SCHEMA
                        .length());
                touchDownImg = BUtility.F_Widget_RES_path + touchDownImg;
                Bitmap mTouchDownImgBitmap = mUexBaseObj.getBitmap(touchDownImg);
                if (mTouchDownImgBitmap != null) {
                    mTouchDownImg = new BitmapDrawable(getResources(),
                            mTouchDownImgBitmap);
                }
            }
            if (mTouchDownImg != null) {
                mRecordTipsImage.setImageDrawable(mTouchDownImg);
            } else {
                mTouchDownImgDefaule = getResources().getDrawable(CRes.plugin_chatkeyboard_voice_recording);
                if (mTouchDownImgDefaule != null) {
                    mRecordTipsImage.setImageDrawable(mTouchDownImgDefaule);
                }
            }
            // dragOutsideImg
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_DRAGOUTSIDEIMG)) {
                String dragOutsideImg = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_DRAGOUTSIDEIMG);
                dragOutsideImg = dragOutsideImg
                        .substring(BUtility.F_Widget_RES_SCHEMA.length());
                dragOutsideImg = BUtility.F_Widget_RES_path + dragOutsideImg;
                Bitmap mDragOutsideImgBitmap = mUexBaseObj.getBitmap(dragOutsideImg);
                if (mDragOutsideImgBitmap != null) {
                    mDragOutsideImg = new BitmapDrawable(getResources(), mDragOutsideImgBitmap);
                }
            }
            if (mDragOutsideImg == null) {
                mDragOutsideImgDefaule = getResources().getDrawable(CRes.plugin_chatkeyboard_voice_cancle);
            }
            // textColor EditText
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_INPUTTEXTCOLOR)) {
                String textColor = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_INPUTTEXTCOLOR);
                mEditText.setTextColor(BUtility.parseColor(textColor));
            }
            // textColor mRecordTimes
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_TEXTCOLOR)) {
                String textColor = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_TEXTCOLOR);
                mRecordTimes.setTextColor(BUtility.parseColor(textColor));
            }
            // textSize mRecordTimes
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_TEXTSIZE)) {
                String textSize = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_TEXTSIZE);
                try {
                    mRecordTimes.setTextSize(Float.parseFloat(textSize));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // sendBtnText
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_SEND_BTN_TEXT)) {
                String sendBtnText = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_SEND_BTN_TEXT);
                mBtnSend.setText(sendBtnText);
            }
            // sendBtnTextSize
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_SEND_BTN_TEXTSIZE)) {
                String sendBtnTextSize = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_SEND_BTN_TEXTSIZE);
                try {
                    mBtnSend.setTextSize(Float.parseFloat(sendBtnTextSize));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // sendBtnTextColor
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_SEND_BTN_TEXTCOLOR)) {
                String sendBtnTextColor = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_SEND_BTN_TEXTCOLOR);
                mBtnSend.setTextColor(BUtility.parseColor(sendBtnTextColor));
            }
            // sendBtn Color Set
            try {
                // Selector need StateListDrawable
                StateListDrawable myGrad = (StateListDrawable) mBtnSend.getBackground();
                // sendBtn Color Normal
                if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_SEND_BTN_BG_COLOR_UP)) {
                    String sendBtnbgColorUp = json
                            .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_SEND_BTN_BG_COLOR_UP);
                    GradientDrawable drawable = (GradientDrawable) myGrad.getCurrent();
                    drawable.setColor(BUtility.parseColor(sendBtnbgColorUp));
                }
                // sendBtn Color Pressed
                if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_SEND_BTN_BG_COLOR_DOWN)) {
                    String sendBtnbgColorDown = json
                            .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_SEND_BTN_BG_COLOR_DOWN);
                    mBtnSend.setPressed(true);
                    GradientDrawable drawable = (GradientDrawable) myGrad.getCurrent();
                    drawable.setColor(BUtility.parseColor(sendBtnbgColorDown));
                    mBtnSend.setPressed(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // inputMode 0:Text 1:voice
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_INPUT_MODE)) {
                mInputMode = json
                        .getInt(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_INPUT_MODE);
            }
            // keywords
            if( json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_KEYWORDS)){
                JSONArray keywordArray = json.getJSONArray(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_KEYWORDS);
                for (int i = 0; i < keywordArray.length(); i++) {
                    keywords.add(keywordArray.getString(i));
                }
            }
            //inputBgColor
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_INPUT_BG_COLOR)) {
                String inputBgColor = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_INPUT_BG_COLOR);
                float dp3 = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
                        .getDisplayMetrics());
                Drawable[] drawables = new Drawable[2];
                if(json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_ISROUND_FOR_BG)){
                    if(!json.getBoolean(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_ISROUND_FOR_BG)){
                        drawables[0] = new ColorDrawable(BUtility.parseColor(inputBgColor));
                    }
                }
                if(drawables[0] == null){
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setColor(BUtility.parseColor(inputBgColor));
                    drawable.setCornerRadii(new float[] { dp3, dp3, dp3, dp3,
                            dp3, dp3, dp3, dp3 });
                    drawable.setGradientRadius(dp3);
                    drawables[0] = drawable;
                }
                drawables[1] = mEditLayout.getBackground();
                LayerDrawable layer = new LayerDrawable(drawables);
                int dp1 = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 1.5f, getResources()
                        .getDisplayMetrics());
                layer.setLayerInset(1, dp1, dp1, dp1, dp1); 
                mEditLayout.setBackground(layer);
            }
            //chatKeyboardBgColor
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_CHAT_KEY_BOARD_BG_COLOR)) {
                String chatKeyboardBgColor = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_CHAT_KEY_BOARD_BG_COLOR);
                mChatKeyboard.setBackgroundColor(BUtility.parseColor(chatKeyboardBgColor));
            }
            //recorder_bg_color
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_RECORDER_BG_COLOR)) {
                String recorder_bg_color = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_RECORDER_BG_COLOR);
                float dp3 = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
                        .getDisplayMetrics());
                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(BUtility.parseColor(recorder_bg_color));
                drawable.setCornerRadii(new float[] { dp3, dp3, dp3, dp3,
                        dp3, dp3, dp3, dp3 });
                drawable.setGradientRadius(dp3);
                if(json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_ISROUND_FOR_BG)){
                    if(!json.getBoolean(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_ISROUND_FOR_BG)){
                        drawable.setCornerRadii(new float[] { 0, 0, 0, 0, 0, 0,
                                0, 0 });
                    }
                }
                mBtnVoiceInput.setBackground(drawable);
            }
            //recorder_normal_title
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_RECORDER_NORMAL_TITLE)) {
                String recorder_normal_title = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_RECORDER_NORMAL_TITLE);
                TIPS_RECORDER_NORMAL_TITLE = recorder_normal_title;
                mBtnVoiceInput.setText(TIPS_RECORDER_NORMAL_TITLE);
            }
            //recorder_normal_title_color
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_RECORDER_NORMAL_TITLE_COLOR)) {
                String recorder_normal_title_color = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_RECORDER_NORMAL_TITLE_COLOR);
                TIPS_RECORDER_NORMAL_TITLE_COLOR = BUtility.parseColor(recorder_normal_title_color);
                mBtnVoiceInput.setTextColor(TIPS_RECORDER_NORMAL_TITLE_COLOR);
            }
            //recorder_highlighted_title
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_RECORDER_HIGHLIGHTED_TITLE)) {
                String recorder_highlighted_title = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_RECORDER_HIGHLIGHTED_TITLE);
                TIPS_RECORDER_PRESS_TITLE = recorder_highlighted_title;
            }
            //recorder_highlighted_title_color
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_RECORDER_HIGHLIGHTED_TITLE_COLOR)) {
                String recorder_highlighted_title_color = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_RECORDER_HIGHLIGHTED_TITLE_COLOR);
                TIPS_RECORDER_PRESS_TITLE_COLOR = BUtility.parseColor(recorder_highlighted_title_color);
            }
            // mBtnVoice img Set
            try {
                // Selector need StateListDrawable
                StateListDrawable oldGrad = (StateListDrawable) mBtnVoice.getBackground();
                StateListDrawable setGrad = null;
                // keyboard image Set Press
                if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_KEY_BOARD_BTN_IMG)) {
                    String mBtnVoiceImgDown = json
                            .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_KEY_BOARD_BTN_IMG);
                    mBtnVoiceImgDown = mBtnVoiceImgDown
                            .substring(BUtility.F_Widget_RES_SCHEMA.length());
                    mBtnVoiceImgDown = BUtility.F_Widget_RES_path + mBtnVoiceImgDown;
                    Bitmap voiceBtnImgDownBitmap = mUexBaseObj.getBitmap(mBtnVoiceImgDown);
                    if (voiceBtnImgDownBitmap != null) {
                        setGrad = new StateListDrawable();
                        BitmapDrawable bitmapDraw = new BitmapDrawable(
                                getResources(), voiceBtnImgDownBitmap);
                        setGrad.addState(
                                new int[] { android.R.attr.state_selected },
                                bitmapDraw);
                    }
                }
                // mBtnVoice image Normal
                if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_VOICE_BTN_IMG)) {
                    String voiceBtnImgPath = json
                            .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_VOICE_BTN_IMG);
                    voiceBtnImgPath = voiceBtnImgPath
                            .substring(BUtility.F_Widget_RES_SCHEMA.length());
                    voiceBtnImgPath = BUtility.F_Widget_RES_path + voiceBtnImgPath;
                    Bitmap voiceBtnImgBitmap = mUexBaseObj.getBitmap(voiceBtnImgPath);
                    if (voiceBtnImgBitmap != null) {
                        if(setGrad == null){
                            setGrad = new StateListDrawable();
                            mBtnVoice.setSelected(true);
                            setGrad.addState(oldGrad.getState(), oldGrad.getCurrent());
                            mBtnVoice.setSelected(false);
                        }
                        BitmapDrawable bitmapDraw = new BitmapDrawable(getResources(), voiceBtnImgBitmap);
                        setGrad.addState(new int[]{}, bitmapDraw);
                    }
                }else if(setGrad != null){
                    setGrad.addState(oldGrad.getState(), oldGrad.getCurrent());
                }
                if(setGrad != null){
                    mBtnVoice.setBackground(setGrad);
                    int height = (int) TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32,
                                    getResources().getDisplayMetrics());
                    mBtnVoice.getLayoutParams().height = height;
                    mBtnVoice.getLayoutParams().width = height;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // add_btn_img
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_SELECTOR_BTN_IMG)) {
                String add_btn_img = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_SELECTOR_BTN_IMG);
                add_btn_img = add_btn_img.substring(BUtility.F_Widget_RES_SCHEMA
                        .length());
                add_btn_img = BUtility.F_Widget_RES_path + add_btn_img;
                Bitmap selector_btn_img_Bitmap = mUexBaseObj.getBitmap(add_btn_img);
                if (selector_btn_img_Bitmap != null) {
                    mBtnAdd.setImageDrawable(new BitmapDrawable(getResources(),
                            selector_btn_img_Bitmap));
                    mBtnAdd.setScaleType(ScaleType.FIT_XY);
                }
            }
            // emotion_btn_img
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_EMOTION_BTN_IMG)) {
                String emotion_btn_img = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_EMOTION_BTN_IMG);
                emotion_btn_img = emotion_btn_img.substring(BUtility.F_Widget_RES_SCHEMA
                        .length());
                emotion_btn_img = BUtility.F_Widget_RES_path + emotion_btn_img;
                Bitmap emotion_btn_img_Bitmap = mUexBaseObj.getBitmap(emotion_btn_img);
                if (emotion_btn_img_Bitmap != null) {
                    mBtnEmojicon.setImageDrawable(new BitmapDrawable(getResources(),
                            emotion_btn_img_Bitmap));
                    mBtnEmojicon.setScaleType(ScaleType.FIT_XY);
                }
            }
            // mEmojiconsLayout background color Set
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_FACE_VIEW_BG_COLOR)) {
                String face_view_bg_color = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_FACE_VIEW_BG_COLOR);
                mEmojiconsLayout.setBackgroundColor(BUtility.parseColor(face_view_bg_color));
            }
            // mSharesLayout background color Set
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_SHARE_VIEW_BG_COLOR)) {
                String sharesLayoutBgColor = json
                        .getString(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_SHARE_VIEW_BG_COLOR);
                mSharesLayout.setBackgroundColor(BUtility.parseColor(sharesLayoutBgColor));
            }
            // isShowSwitchBtn
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_IS_SHOW_SWITCH_BTN)) {
                boolean isShowSwitchBtn = json
                        .getBoolean(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_IS_SHOW_SWITCH_BTN);
                if(!isShowSwitchBtn){
                    mBtnVoice.setVisibility(View.GONE);
                }
            }
            // isShowemojiconsBtn
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_IS_SHOW_EMOJICONS_BTN)) {
                boolean isShowemojiconsBtn = json
                        .getBoolean(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_IS_SHOW_EMOJICONS_BTN);
                if(!isShowemojiconsBtn){
                    mBtnEmojicon.setVisibility(View.INVISIBLE);
                }
            }
            // isShowSharesBtn
            if (json.has(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_IS_SHOW_SHARES_BTN)) {
                isShowSharesBtn = json
                        .getBoolean(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_IS_SHOW_SHARES_BTN);
                if(!isShowSharesBtn){
                    mBtnAdd.setVisibility(View.GONE);
                    mBtnSend.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * initLayout Animator
     */
    private void initLayoutTransition() {
        if (mLayoutTransition != null) {
            return;
        }
        mLayoutTransition = new LayoutTransition();
        mLayoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING,
                mLayoutTransition.getAnimator(LayoutTransition.CHANGE_APPEARING));
        mLayoutTransition.setAnimator(LayoutTransition.APPEARING, null);
        mLayoutTransition.setAnimator(LayoutTransition.DISAPPEARING, null);
        mLayoutTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, null);
        // mLayoutTransition.getAnimator(LayoutTransition.CHANGE_DISAPPEARING));
        mLayoutTransition.addTransitionListener(new TransitionListener() {

            @Override
            public void startTransition(LayoutTransition transition,
                                        ViewGroup container, View view, int transitionType) {
            }

            @Override
            public void endTransition(LayoutTransition transition, ViewGroup container,
                                      View view, int transitionType) {
                if (view.getId() == CRes.plugin_chatkeyboard_parent_layout && transitionType == LayoutTransition.CHANGE_APPEARING) {
                    goScroll(0);
                    jsonKeyBoardShowCallback(isKeyBoardVisible || mPagerLayout.isShown() ? 1 : 0);
                } else if (view.getId() == CRes.plugin_chatkeyboard_pager_layout && transitionType == LayoutTransition.DISAPPEARING) {
                    if (!isKeyBoardVisible)
                        backScroll();
                    jsonKeyBoardShowCallback(isKeyBoardVisible || mPagerLayout.isShown() ? 1 : 0);
                }
            }
        });
        mParentLayout.setLayoutTransition(mLayoutTransition);
    }
    
    public void setHint(String hint){
        if(mEditText != null){
            mEditText.setHint(hint);
        }
    }

    public void onDestroy() {
        try {
            outOfViewTouch();
            if (mDragOutsideImg != null)
                mDragOutsideImg.getBitmap().recycle();
            if (mTouchDownImg != null)
                mTouchDownImg.getBitmap().recycle();
            if (mDragOutsideImgDefaule != null)
                mDragOutsideImgDefaule.setCallback(null);
            if (mTouchDownImgDefaule != null)
                mTouchDownImgDefaule.setCallback(null);
            mDragOutsideImg = null;
            mDragOutsideImgDefaule = null;
            mTouchDownImg = null;
            mTouchDownImgDefaule = null;
            mParentLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    /**
     * 制造一个真实的路径
     * from BUtility
     * @return
     */
    public String makeRealPath(String path, EBrowserView browserView) {
        path = BUtility.makeUrl(browserView.getCurrentUrl(), path);
        int wgtType = browserView.getCurrentWidget().m_wgtType;
        String widgetPath = browserView.getCurrentWidget().getWidgetPath();
        return BUtility.makeRealPath(path, widgetPath, wgtType);
    }

    /**
     * Reading all emoticons in local cache
     */
    private void initEmojicons() {
        InputStream in = null;
        try {
            /*String xmlPath = makeRealPath(mEmojiconsXmlPath, mUexBaseObj.mBrwView);
            String emojiconsFolder = xmlPath.substring(0, xmlPath.lastIndexOf("/") + 1);
            if (xmlPath.startsWith("widget/")) {
                in = getContext().getAssets().open(xmlPath);
            } else {
                File file = new File(xmlPath);
                in = new FileInputStream(file);
            }*/
            String xmlPath = mEmojiconsXmlPath
                    .substring(BUtility.F_Widget_RES_SCHEMA.length());
            String emojiconsFolder = BUtility.F_Widget_RES_path
                    + xmlPath.substring(0, xmlPath.lastIndexOf("/") + 1);
            String resXmlPath = BUtility.F_Widget_RES_path + xmlPath;
            in = getContext().getAssets().open(resXmlPath);

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, "utf-8");
            int tokenType = 0;
            boolean needContinue = true;
            do {
                tokenType = parser.next();
                switch (tokenType) {
                    case XmlPullParser.START_TAG:
                        String localName = (parser.getName()).toLowerCase();
                        if ("emojicons".equals(localName)) {
                            mEmojiconsDeletePath = emojiconsFolder
                                    + parser.getAttributeValue(null, "delete");
                        } else if ("key".equals(localName)) {
                            mEmojiconsText.add(parser.nextText());
                        } else if ("string".equals(localName)) {
                            mEmojiconsPath
                                    .add(emojiconsFolder + parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        needContinue = false;
                        break;
                }
            } while (needContinue);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        NUMBER_OF_EMOJICONS = mEmojiconsPath.size();
    }

    /**
     * For loading smileys from assets
     */
    private Bitmap getBitmap(String path) {
        InputStream in = null;
        try {
            if (path.startsWith("widget/")) {
                in = getContext().getAssets().open(path);
            } else {
                File file = new File(path);
                in = new FileInputStream(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap temp = BitmapFactory.decodeStream(in, null, null);
        return temp;
    }

    public void insertTextByKeyword(String keyword,String insertText,String color,boolean isReplaceKeyword) {
        if (TextUtils.isEmpty(keyword) || TextUtils.isEmpty(insertText)){
            return;
        }
        if (TextUtils.isEmpty(color)){
            color = "#507daf";
        }
        Editable editable = mEditText.getText();
        int lastAtPosition = mLastAtPosition;
        if(editable.length() < keyword.length() || editable.length() < lastAtPosition + keyword.length()){
            return;
        }
        if(!keyword.equals(editable.subSequence(lastAtPosition, lastAtPosition + keyword.length()).toString())){
            if(!editable.toString().contains(keyword)){
                return;
            }
            lastAtPosition = editable.toString().lastIndexOf(keyword);
        }
        if(isReplaceKeyword){
            editable.replace(lastAtPosition, lastAtPosition + keyword.length(), Html.fromHtml("<font color=\""+ color + "\">" + insertText + "</font>"));
        }else{
            editable.insert(lastAtPosition + keyword.length(), Html.fromHtml("<font color=\""+ color + "\">" + insertText + "</font>"));
        }
        lastAtPosition += insertText.length() + (isReplaceKeyword ? 0 : keyword.length());
        mEditText.setSelection(lastAtPosition);
    }

    /**
     * 更新绘制所有关键字
     * 
     * @deprecated
     * @see #insertAfterAt(String, String, String, boolean)
     * @param insertTexts
     *            包含的所有已插入的关键字(正则匹配所有)
     */
    public void updateEditTextViewWithKeyword(List<String> insertTexts){
        CharSequence charSequence = mEditText.getText();
        for (String name : insertTexts) {
            charSequence = Replacer.replace(charSequence, name, Html.fromHtml("<font color=\"#507daf\">" + name + "</font>"));
        }
        mEditText.setText(charSequence);
        mEditText.setSelection(mLastAtPosition);
    }

    private class EmotjiconsPagerAdapter extends PagerAdapter {
        public EmotjiconsPagerAdapter() {
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View layout = ((Activity) getContext()).getLayoutInflater().inflate(
                    CRes.plugin_chatkeyboard_emojicons_grid, null);
            int initialPosition = position * NUMBER_OF_EMOJICONS_PER_PAGE;
            ArrayList<String> emoticonsInAPage = new ArrayList<String>();

            for (int i = initialPosition; i <= initialPosition
                    + NUMBER_OF_EMOJICONS_PER_PAGE
                    && i <= mEmojiconsPath.size(); i++) {
                if (i == initialPosition + NUMBER_OF_EMOJICONS_PER_PAGE
                        || i == mEmojiconsPath.size()) {
                    emoticonsInAPage.add(mEmojiconsDeletePath);
                } else {
                    emoticonsInAPage.add(mEmojiconsPath.get(i));
                }
            }

            GridView grid = (GridView) layout
                    .findViewById(CRes.plugin_chatkeyboard_emojicons_grid_view);
            EmojiconsGridAdapter adapter = new EmojiconsGridAdapter(
                    emoticonsInAPage);
            grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
            grid.setAdapter(adapter);
            /*if(keyBoardHeight != 0){
				grid.setVerticalSpacing((int)(keyBoardHeight / 25 * 2));
				layout.setPadding(layout.getPaddingLeft(),
						(int) (keyBoardHeight / 250 * 25),
						layout.getPaddingRight(), layout.getPaddingBottom());
			}*/
            mEmojiconsPageIndex = position;
            container.addView(layout);
            return layout;
        }

        @Override
        public int getCount() {
            return (int) Math.ceil((double) NUMBER_OF_EMOJICONS
                    / (double) NUMBER_OF_EMOJICONS_PER_PAGE);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object view) {
            container.removeView((View) view);
        }
    }

    private int getTextHeight(Paint textPaint) {
        return (int) (-textPaint.ascent() + textPaint.descent());
    }

    public class EmojiconsGridAdapter extends BaseAdapter {
        private ArrayList<String> paths;

        public EmojiconsGridAdapter(ArrayList<String> paths) {
            this.paths = paths;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View layout = ((Activity) getContext()).getLayoutInflater().inflate(
                    CRes.plugin_chatkeyboard_emojicons_item, null);
            ImageView image = (ImageView) layout
                    .findViewById(CRes.plugin_chatkeyboard_emojicon_item);
            final String path = paths.get(position);
            image.setImageBitmap(getBitmap(path));
            if (position == paths.size() - 1) {
                image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        KeyEvent event = new KeyEvent(0, 0, 0,
                                KeyEvent.KEYCODE_DEL, 0, 0, 0, 0,
                                KeyEvent.KEYCODE_ENDCALL);
                        mEditText.dispatchKeyEvent(event);
                    }
                });
                image.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mEditText.setText(null);
                        return false;
                    }
                });
            } else {
                image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BitmapDrawable drawable1 = (BitmapDrawable) ((ImageView)v).getDrawable();
                        BitmapDrawable drawable = new BitmapDrawable(getResources(), drawable1.getBitmap());
                        int textHeight = getTextHeight(mEditText.getPaint());
                        drawable.setBounds(0, 0, textHeight, textHeight);
                        CharSequence text = mEmojiconsText.get(mEmojiconsPath
                                .indexOf(path));
                        ImageSpan imageSpan = new ImageSpan(drawable);
                        SpannableString spannable = new SpannableString(text);
                        spannable.setSpan(imageSpan, 0, text.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mEditText.getText().insert(
                                mEditText.getSelectionStart(), spannable);
                    }
                });
            }
            return layout;
        }

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public Object getItem(int position) {
            return paths.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    private void initShares() {
        InputStream in = null;
        try {
            /*String xmlPath = makeRealPath(mSharesXmlPath, mUexBaseObj.mBrwView);
            String sharesFolder = xmlPath.substring(0, xmlPath.lastIndexOf("/") + 1);
            if (xmlPath.startsWith("widget/")) {
                in = getContext().getAssets().open(xmlPath);
            } else {
                File file = new File(xmlPath);
                in = new FileInputStream(file);
            }*/
            String xmlPath = mSharesXmlPath
                    .substring(BUtility.F_Widget_RES_SCHEMA.length());
            String sharesFolder = BUtility.F_Widget_RES_path
                    + xmlPath.substring(0, xmlPath.lastIndexOf("/") + 1);
            String resXmlPath = BUtility.F_Widget_RES_path + xmlPath;
            in = getContext().getAssets().open(resXmlPath);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, "utf-8");
            int tokenType = 0;
            boolean needContinue = true;
            do {
                tokenType = parser.next();
                switch (tokenType) {
                    case XmlPullParser.START_TAG:
                        String localName = (parser.getName()).toLowerCase();
                        if ("key".equals(localName)) {
                            mSharesText.add(parser.nextText());
                        } else if ("string".equals(localName)) {
                            mSharesPath.add(sharesFolder + parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        needContinue = false;
                        break;
                }
            } while (needContinue);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        NUMBER_OF_SHARES = mSharesPath.size();
    }

    private class SharesPagerAdapter extends PagerAdapter {
        public SharesPagerAdapter() {
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View layout = ((Activity) getContext()).getLayoutInflater().inflate(
                    CRes.plugin_chatkeyboard_shares_grid, null);
            int initialPosition = position * NUMBER_OF_SHARES_PER_PAGE;
            ArrayList<String> textsInAPage = new ArrayList<String>();
            ArrayList<String> pathsInAPage = new ArrayList<String>();
            for (int i = initialPosition; i < initialPosition
                    + NUMBER_OF_SHARES_PER_PAGE
                    && i < mSharesPath.size(); i++) {
                textsInAPage.add(mSharesText.get(i));
                pathsInAPage.add(mSharesPath.get(i));
            }

            GridView grid = (GridView) layout
                    .findViewById(CRes.plugin_chatkeyboard_shares_grid_view);
            SharesGridAdapter adapter = new SharesGridAdapter(textsInAPage,
                    pathsInAPage);
            grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
            grid.setAdapter(adapter);
            /*if (keyBoardHeight != 0) {
                grid.setVerticalSpacing((int) (keyBoardHeight / 25));
            }*/
            mSharesPageIndex = position;
            container.addView(layout);
            return layout;
        }

        @Override
        public int getCount() {
            return (int) Math.ceil((double) NUMBER_OF_SHARES
                    / (double) NUMBER_OF_SHARES_PER_PAGE);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object view) {
            container.removeView((View) view);
        }
    }

    public class SharesGridAdapter extends BaseAdapter {
        private ArrayList<String> texts;
        private ArrayList<String> paths;

        public SharesGridAdapter(ArrayList<String> texts,
                                 ArrayList<String> paths) {
            this.texts = texts;
            this.paths = paths;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            View layout = ((Activity) getContext()).getLayoutInflater().inflate(
                    CRes.plugin_chatkeyboard_shares_item, null);
            TextView text = (TextView) layout
                    .findViewById(CRes.plugin_chatkeyboard_share_name);
            text.setText(texts.get(position));
            ImageView image = (ImageView) layout
                    .findViewById(CRes.plugin_chatkeyboard_share_image);
            image.setImageBitmap(getBitmap(paths.get(position)));
            layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareMenuItemIndexCallback(position);
                }
            });
            return layout;
        }

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public Object getItem(int position) {
            return paths.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    private void initPagerIndicator() {
        int emojiconsPagerSize = (int) Math.ceil((double) NUMBER_OF_EMOJICONS
                / (double) NUMBER_OF_EMOJICONS_PER_PAGE);
        if (emojiconsPagerSize > 1) {
            initPagerIndicator(emojiconsPagerSize, mEmojiconsIndicator);
            updateCurrentPage(mEmojiconsPageIndex, mEmojiconsIndicator);
        } else {
            mEmojiconsIndicator.setVisibility(View.INVISIBLE);
        }

        int sharesPagerSize = (int) Math.ceil((double) NUMBER_OF_SHARES
                / (double) NUMBER_OF_SHARES_PER_PAGE);
        if (sharesPagerSize > 1) {
            initPagerIndicator(sharesPagerSize, mSharesIndicator);
            updateCurrentPage(mSharesPageIndex, mSharesIndicator);
        } else {
            mSharesIndicator.setVisibility(View.INVISIBLE);
        }

    }

    private void initPagerIndicator(int pagerSize, LinearLayout layout) {
        int childCount = layout.getChildCount();
        if (pagerSize == childCount) {
            return;
        }
        int width = getResources().getDimensionPixelSize(
                CRes.plugin_chatkeyboard_pager_indicator_width);
        int left = getResources().getDimensionPixelSize(
                CRes.plugin_chatkeyboard_pager_indicator_left);
        int top = getResources().getDimensionPixelSize(
                CRes.plugin_chatkeyboard_pager_indicator_top);
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(
                width, width);
        viewParams.setMargins(left, top, left, top);
        if (pagerSize > childCount) {// 需要增加
            while (childCount < pagerSize) {
                ImageView imageView = new ImageView(getContext());
                layout.addView(imageView, childCount, viewParams);
                childCount++;
            }
        } else {
            while (childCount > pagerSize) {
                layout.removeViewAt(childCount);
                childCount--;
            }
        }
    }

    private void updateCurrentPage(int index, LinearLayout layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            if (i == index) {
                view.setBackgroundResource(CRes.plugin_chatkeyboard_pages_pointer_focus);
            } else {
                view.setBackgroundResource(CRes.plugin_chatkeyboard_pages_pointer_normal);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int index, float positionOffset,
                               int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int index) {
        if (mEmojiconsIndicator.getVisibility() == View.VISIBLE) {
            mEmojiconsPageIndex = index;
            updateCurrentPage(index, mEmojiconsIndicator);
        }
        if (mSharesIndicator.getVisibility() == View.VISIBLE) {
            mSharesPageIndex = index;
            updateCurrentPage(index, mSharesIndicator);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    private int lastline = 0;
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            mBtnSend.setVisibility(mEditText.getText().length() != 0
                    || !isShowSharesBtn ? View.VISIBLE : View.GONE);
            mBtnAdd.setVisibility(mEditText.getText().length() == 0
                    && isShowSharesBtn ? View.VISIBLE : View.GONE);

            // test maxline
            int line = mEditText.getLineCount();
            Log.i(TAG, "laline : " + lastline + " line : " + line + "  he : "  + mEditText.getLineHeight());
            if(line!= lastline && line > 2 && line < 8){
                LayoutParams ls = (LayoutParams) ((View)mEditText.getParent().getParent()).getLayoutParams();
                int addline = 0;
                if(lastline == 0){
                    addline = line - 2 - lastline;
                }else {
                    addline = line - lastline;
                }
                int lineHeightCount = mEditText.getLineHeight() * addline;
                ls.height = ls.height + lineHeightCount;
                lastline = line;
            } else if(line <= 2 && lastline > 2){
                lastline = 0;
                LayoutParams ls = (LayoutParams) ((View)mEditText.getParent().getParent()).getLayoutParams();
                ls.height = EUExUtil.dipToPixels(50);
            } else if(line!= lastline && line > 7 && lastline < 7){
                LayoutParams ls = (LayoutParams) ((View)mEditText.getParent().getParent()).getLayoutParams();
                int lineHeightCount = mEditText.getLineHeight() * 5;
                ls.height = ls.height + lineHeightCount;
                lastline = 7;
            }

            if(before > 0 && count == 0){
                //delete or replace
                return;
            }
            int size = keywords.size();
            int keywordStart = start;
            String keyword = null;
            for (int i = 0; i < size; i++) {
                String keywordTemp = keywords.get(i);
                int keywordlength = keywordTemp.length();
                int startTemp = start + count - keywordlength;
                if(startTemp < 0 || s.length() < keywordlength){
                    continue;
                }
                String edit = s.subSequence(startTemp, startTemp + keywordlength).toString();
                if(keywordTemp.equals(edit)){
                    keywordStart = startTemp;
                    keyword = keywordTemp;
                    break;
                }
            }
            if(keyword == null){
                return;
            }
            mLastAtPosition = keywordStart;
            JSONObject json = new JSONObject();
            json.put(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_KEYWORD, keyword);
            String js = EUExChatKeyboard.SCRIPT_HEADER + "if("
                    + EChatKeyboardUtils.CHATKEYBOARD_FUN_ON_INPUT_KEYWORD + "){"
                    + EChatKeyboardUtils.CHATKEYBOARD_FUN_ON_INPUT_KEYWORD + "("
                    + json.toString() + ");}";
            mUexBaseObj.onCallback(js);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (mRecordTipsLayout.getVisibility() == View.VISIBLE) {
            return;
        }
        if (id == CRes.plugin_chatkeyboard_btn_emojicon) {
            toggleBtnEmojicon(mEmojiconsLayout.isShown() ? false : true);
        } else if (id == CRes.plugin_chatkeyboard_btn_add) {
            toggleBtnAdd(mSharesLayout.isShown() ? false : true);
        } else if (id == CRes.plugin_chatkeyboard_btn_send) {
            toggleBtnSend();
        } else if (id == CRes.plugin_chatkeyboard_btn_voice) {
            toggleBtnVoice();
        } else if (id == CRes.plugin_chatkeyboard_edit_input) {
            if (mPagerLayout.isShown()) {
                mPagerLayout.setVisibility(View.GONE);
            }
        }
    }

    private void toggleBtnEmojicon(boolean visible) {
        if (visible) {
            if (isKeyBoardVisible) {
                backScroll();
                mInputManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPagerLayout.setVisibility(View.VISIBLE);
                    mEmojiconsLayout.setVisibility(View.VISIBLE);
                    mSharesLayout.setVisibility(View.GONE);
                    mEditText.requestFocus();
                }
            }, 200);
        } else {
            if (!isKeyBoardVisible) {
                mInputManager.toggleSoftInputFromWindow(
                        mEditText.getWindowToken(),
                        InputMethodManager.SHOW_IMPLICIT, 0);
            }
            mEmojiconsLayout.setVisibility(View.GONE);
            mPagerLayout.setVisibility(View.GONE);
        }

        mEditText
                .setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            // 此处为失去焦点时的处理内容
                            mEmojiconsLayout.setVisibility(View.GONE);
                            mPagerLayout.setVisibility(View.GONE);
                        } else {
                            // 此处为得到焦点时的处理内容
                        }
                    }
                });

    }

    private void toggleBtnAdd(boolean visible) {
        if (visible) {
            if (isKeyBoardVisible) {
                backScroll();
                mInputManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //由于延迟导致的可能会同时存在的问题  2015-08-12
                    if (isKeyBoardVisible) {
                        mInputManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                    }
                    mPagerLayout.setVisibility(View.VISIBLE);
                    mSharesLayout.setVisibility(View.VISIBLE);
                    mEmojiconsLayout.setVisibility(View.GONE);
                    mBtnVoice.setSelected(false);
                    mBtnVoiceInput.setVisibility(View.GONE);
                    mEditLayout.setVisibility(View.VISIBLE);
                    mEditText.requestFocus();
                }
            }, 200);
        } else {
            if (!isKeyBoardVisible) {
                mInputManager.toggleSoftInputFromWindow(
                        mEditText.getWindowToken(),
                        InputMethodManager.SHOW_IMPLICIT, 0);
            }
            mSharesLayout.setVisibility(View.GONE);
            mPagerLayout.setVisibility(View.GONE);
        }
        mEditText
                .setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            // 此处为失去焦点时的处理内容
                            mEmojiconsLayout.setVisibility(View.GONE);
                            mPagerLayout.setVisibility(View.GONE);
                        } else {
                            // 此处为得到焦点时的处理内容
                        }
                    }
                });
    }

    private void toggleBtnSend() {
        Log.i(TAG, " toggleBtnSend mEditText " + mEditText.getText().toString());
        jsonSendDataCallback();
        jsonSendDataJsonCallback();
        mEditText.setText(null);
    }

    private void jsonSendDataCallback() {
        // TODO	send callback String
        if (mUexBaseObj != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject
                        .put(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_EMOJICONS_TEXT,
                                mEditText.getText().toString());
                String js = EUExChatKeyboard.SCRIPT_HEADER + "if("
                        + EChatKeyboardUtils.CHATKEYBOARD_FUN_ON_COMMIT + "){"
                        + EChatKeyboardUtils.CHATKEYBOARD_FUN_ON_COMMIT + "('"
                        + jsonObject.toString() + "');}";
                mUexBaseObj.onCallback(js);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void jsonSendDataJsonCallback() {
        // TODO send callback Json
        if (mUexBaseObj != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                Editable editable = mEditText.getText();
                jsonObject
                        .put(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_EMOJICONS_TEXT,
                                editable.toString());
                JSONArray array = new JSONArray();
                ForegroundColorSpan[] spans = editable.getSpans(0, editable.length(), ForegroundColorSpan.class);
                for (ForegroundColorSpan span : spans) {
                    JSONObject insertObj = new JSONObject();
                    int spanStart = editable.getSpanStart(span);
                    int spanEnd = editable.getSpanEnd(span);
                    String insertText = editable.subSequence(spanStart, spanEnd).toString();
                    String insertTextColor = "#" + Integer.toHexString(span.getForegroundColor()).substring(2);
                    insertObj.put(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_START, spanStart);
                    insertObj.put(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_END, spanEnd);
                    insertObj.put(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_INSERTTEXT, insertText);
                    insertObj.put(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_INSERTTEXTCOLOR, insertTextColor);
                    array.put(insertObj);
                }
                jsonObject.put(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_INSERTTEXTS, array);
                String js = EUExChatKeyboard.SCRIPT_HEADER + "if("
                        + EChatKeyboardUtils.CHATKEYBOARD_FUN_ON_COMMIT_JSON + "){"
                        + EChatKeyboardUtils.CHATKEYBOARD_FUN_ON_COMMIT_JSON + "("
                        + jsonObject.toString() + ");}";
                mUexBaseObj.onCallback(js);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void jsonKeyBoardShowCallback(int status) {
        // TODO	KeyBoardShow status callback String
        if (mUexBaseObj != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject
                        .put(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_SHOW_STATUS,
                                status);
                String js = EUExChatKeyboard.SCRIPT_HEADER
                        + "if("
                        + EChatKeyboardUtils.CHATKEYBOARD_FUN_ON_KEYBOARDSHOW
                        + "){"
                        + EChatKeyboardUtils.CHATKEYBOARD_FUN_ON_KEYBOARDSHOW
                        + "('" + jsonObject.toString()
                        + "');}";
                mUexBaseObj.onCallback(js);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void jsonVoiceActionCallback(int status) {
        // TODO	VoiceAction callback String
        if (mUexBaseObj != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject
                        .put(EChatKeyboardUtils.CHATKEYBOARD_PARAMS_JSON_KEY_VOICE_STATUS,
                                status);
                String js = EUExChatKeyboard.SCRIPT_HEADER + "if("
                        + EChatKeyboardUtils.CHATKEYBOARD_FUN_ON_VOICEACTION
                        + "){"
                        + EChatKeyboardUtils.CHATKEYBOARD_FUN_ON_VOICEACTION
                        + "('" + jsonObject.toString() + "');}";
                mUexBaseObj.onCallback(js);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void shareMenuItemIndexCallback(int index) {
        // TODO	ShareMenuItem index callback String
        if (mUexBaseObj != null) {
            String js = EUExChatKeyboard.SCRIPT_HEADER
                    + "if("
                    + EChatKeyboardUtils.CHATKEYBOARD_FUN_ON_SHAREMENUITEM
                    + "){"
                    + EChatKeyboardUtils.CHATKEYBOARD_FUN_ON_SHAREMENUITEM
                    + "('" + index + "');}";
            mUexBaseObj.onCallback(js);
        }
    }


    private void toggleBtnVoice() {
        if (mBtnVoiceInput.getVisibility() == View.GONE) {
            if (isKeyBoardVisible) {
                backScroll();
                mInputManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBtnVoice.setSelected(true);
                    mBtnVoiceInput.setVisibility(View.VISIBLE);
                    mEditLayout.setVisibility(View.GONE);
                    mEmojiconsLayout.setVisibility(View.GONE);
                    mSharesLayout.setVisibility(View.GONE);
                    mPagerLayout.setVisibility(View.GONE);
                }
            }, 200);
        } else {
            if (!isKeyBoardVisible) {
                mEditText.requestFocus();
                mInputManager.toggleSoftInputFromWindow(
                        mEditText.getWindowToken(),
                        InputMethodManager.SHOW_IMPLICIT, 0);
            }
            mBtnVoice.setSelected(false);
            mBtnVoiceInput.setVisibility(View.GONE);
            mEditLayout.setVisibility(View.VISIBLE);
            mEmojiconsLayout.setVisibility(View.GONE);
            mSharesLayout.setVisibility(View.GONE);
            mPagerLayout.setVisibility(View.GONE);
        }
    }

    public void outOfViewTouch() {
        backScroll();
        if (isKeyBoardVisible) {
            mInputManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        }
        if (mPagerLayout.isShown()) {
            mPagerLayout.setVisibility(View.GONE);
        }
    }

    Handler timerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TIMER_HANDLER_MESSAGE_WHAT) {
                mRecordTimes.setText(msg.arg1 + "\"");
                if (msg.arg1 > 59) {
                    completeRecord();
                    jsonVoiceActionCallback(2);
                    return;
                }
                Message message = new Message();
                message.what = msg.what;
                message.arg1 = msg.arg1 + 1;
                sendMessageDelayed(message, 1000);
            }
        }
    };

    private void completeRecord() {
        mBtnVoiceInput.setText(TIPS_RECORDER_NORMAL_TITLE);
        mBtnVoiceInput.setTextColor(TIPS_RECORDER_NORMAL_TITLE_COLOR);
        mRecordTimes.setText("");
        if (mTouchDownImg != null) {
            mRecordTipsImage.setImageDrawable(mTouchDownImg);
        } else {
            mRecordTipsImage
                    .setImageDrawable(mTouchDownImgDefaule);
        }
        mRecordTipsLayout.setVisibility(View.INVISIBLE);
        timerHandler.removeMessages(TIMER_HANDLER_MESSAGE_WHAT);
    }

    private void handleRecordWhenDown() {
        mRecordTipsLayout.setVisibility(View.VISIBLE);
        mRecordTimes.setVisibility(View.VISIBLE);
        int imageWidth = mRecordTipsImage.getWidth();
        int imageHeight = mRecordTipsImage.getHeight();
        android.view.ViewGroup.LayoutParams viewParams = mRecordTimes.getLayoutParams();
        ((MarginLayoutParams) viewParams).setMargins((int) (0.65f * imageWidth), (int) (0.55f * imageHeight), 0, 0);
        mRecordTimes.setLayoutParams(viewParams);
        mRecordTimes.invalidate();
        mBtnVoiceInput.setText(TIPS_RECORDER_PRESS_TITLE);
        mBtnVoiceInput.setTextColor(TIPS_RECORDER_PRESS_TITLE_COLOR);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == CRes.plugin_chatkeyboard_btn_voice_input) {
            float btnWidth = mBtnVoiceInput.getWidth() / 2;
            float btnHeight = mBtnVoiceInput.getHeight() / 2;
            float x = event.getX() - btnWidth;
            float y = event.getY() - btnHeight;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handleRecordWhenDown();
                    Message message = new Message();
                    message.what = TIMER_HANDLER_MESSAGE_WHAT;
                    message.arg1 = 0;
                    timerHandler.sendMessage(message);
                    jsonVoiceActionCallback(0);
                    break;
                case MotionEvent.ACTION_UP:
                    if (mRecordTipsLayout.getVisibility() != View.VISIBLE) {
                        break;
                    }
                    completeRecord();
                    if (Math.abs(x) > btnWidth || Math.abs(y) > btnWidth) {
                        jsonVoiceActionCallback(-1);
                    } else {
                        jsonVoiceActionCallback(1);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    completeRecord();
                    jsonVoiceActionCallback(-1);
                    break;
                case MotionEvent.ACTION_OUTSIDE:
                    completeRecord();
                    jsonVoiceActionCallback(-1);
                    break;
                case MotionEvent.ACTION_MASK:
                    completeRecord();
                    jsonVoiceActionCallback(-1);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(x) > btnWidth || Math.abs(y) > btnWidth) {
                        mRecordTimes.setVisibility(View.GONE);
                        if (mDragOutsideImg != null) {
                            mRecordTipsImage.setImageDrawable(mDragOutsideImg);
                        } else {
                            mRecordTipsImage
                                    .setImageDrawable(mDragOutsideImgDefaule);
                        }
                    } else {
                        mRecordTimes.setVisibility(View.VISIBLE);
                        if (mTouchDownImg != null) {
                            mRecordTipsImage.setImageDrawable(mTouchDownImg);
                        } else {
                            mRecordTipsImage.setImageDrawable(mTouchDownImgDefaule);
                        }
                    }
                    break;
            }
            return true;
        }
        // outOfTouchView
        else if (id == CRes.plugin_chatkeyboard_parent_layout) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (!isKeyBoardVisible && !mPagerLayout.isShown()) {
                    return false;
                }
                DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
                float h = mEditLayout.getHeight();
                h = h + (mPagerLayout.isShown() ? mPagerLayout.getHeight() : 0);
                float y = event.getY();
                if (dm.heightPixels - Math.abs(y) > h) {
                    outOfViewTouch();
                    return true;
                }
            }
            return false;
        } else if (id == mOutOfTouchView.getId()) {
            if (isKeyBoardVisible || mPagerLayout.isShown()) {
                outOfViewTouch();
                return true;
            }
            return false;
        }
        return true;
    }

    /**
     * Checking keyboard visibility
     */
    @Override
    public void onGlobalLayout() {
        // TODO Checking keyboard visibility
        Rect r = new Rect();
        mParentLayout.getWindowVisibleDisplayFrame(r);
        int screenHeight = mParentLayout.getRootView()
                .getHeight();
        int heightDifference = screenHeight - (r.bottom);
        boolean isKeyBoardChange = isKeyBoardVisible;
        if (heightDifference > 100) {
            isKeyBoardVisible = true;
            //弹出键盘的时候,判断下俩者有弹出状态则设置隐藏  2015-08-12
            if (mPagerLayout.isShown()) {
                mPagerLayout.setVisibility(View.GONE);
                /*backScroll();
                goScroll(heightDifference);*/
            }
            keyBoardHeight = heightDifference;
            //changeKeyBoardHeight(heightDifference);
        } else {
            isKeyBoardVisible = false;
        }
        if (isKeyBoardVisible && !isKeyBoardChange) {
            goScroll(heightDifference);
        } else if (!mPagerLayout.isShown() && !isKeyBoardVisible) {
            backScroll();
        }
        //添加键盘弹出和隐藏的回调  2015-08-12
        boolean isChange = (isKeyBoardChange != isKeyBoardVisible);
        if (isChange) {
            jsonKeyBoardShowCallback(isKeyBoardVisible || mPagerLayout.isShown() ? 1 : 0);
        }
    }

    private void goScroll(int heightDifference) {
        if (!isKeyboardChange) {
            Log.i(TAG, "↑");
            isKeyboardChange = true;
            if (mUexBaseObj == null || mUexBaseObj.mBrwView == null) {
                return;
            }
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mUexBaseObj.mBrwView
                    .getLayoutParams();
            int tempHeight = lp.height;
            lp.weight = 0;
            if (tempHeight == LayoutParams.MATCH_PARENT) {
                tempHeight = mUexBaseObj.mBrwView.getHeight();
            }
            if (mBrwViewHeight == 0) {
                mBrwViewHeight = lp.height;
            }
            int keyboardHeight = mPagerLayout.isShown() ? mPagerLayout.getHeight() : 0;
            int inputHeight = isKeyBoardVisible || mPagerLayout.isShown() ? EUExUtil.dipToPixels(50) : 0;
            int bottomPoint = ((View) mUexBaseObj.mBrwView.getParent()).getBottom();
            int bottomMargin = mParentLayout.getHeight() - bottomPoint;
            Log.i(TAG, "bottomMargin : " + bottomMargin + "   " + bottomPoint + "   " + mParentLayout.getHeight());
            if (bottomMargin > inputHeight) {
                inputHeight = 0;
            }
            int screenHeight = mParentLayout.getRootView().getHeight();
            if (mBrwViewHeight > 0 || tempHeight > screenHeight - heightDifference) {
                if (bottomMargin + heightDifference + 4 > inputHeight) {//+4是纠正减法导致的计算误差
                    inputHeight = heightDifference;
                } else {
                    inputHeight = heightDifference + inputHeight;
                }
            }
            Log.i(TAG, "Move! height:" + (tempHeight - keyboardHeight - inputHeight) + " tempHeight:" + tempHeight + " ParentkeyboardHeight:" + keyboardHeight + " inputHeight:" + inputHeight);
            lp.height = tempHeight - keyboardHeight - inputHeight;
            ((ViewGroup) mUexBaseObj.mBrwView).setLayoutParams(lp);
            ((ViewGroup) mUexBaseObj.mBrwView).invalidate();
        }
    }

    private void backScroll() {
        if (isKeyboardChange) {
            Log.i(TAG, "↓");
            isKeyboardChange = false;
            if (mUexBaseObj == null || mUexBaseObj.mBrwView == null) {
                return;
            }
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mUexBaseObj.mBrwView
                    .getLayoutParams();
            lp.height = mBrwViewHeight;
            lp.weight = 1;
            ((ViewGroup) mUexBaseObj.mBrwView).setLayoutParams(lp);
            ((ViewGroup) mUexBaseObj.mBrwView).invalidate();
        }
    }

    private void changeKeyBoardHeight(int keyBoardHeight) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mPagerLayout.getLayoutParams();
        int pagerHeight = lp.height;
        if (pagerHeight != keyBoardHeight) {
            lp.height = keyBoardHeight;
            mPagerLayout.setLayoutParams(lp);
            this.keyBoardHeight = keyBoardHeight;
        }
    }
}
