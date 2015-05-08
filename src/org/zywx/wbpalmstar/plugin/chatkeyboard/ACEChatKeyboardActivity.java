package org.zywx.wbpalmstar.plugin.chatkeyboard;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.zywx.wbpalmstar.base.BUtility;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ACEChatKeyboardActivity extends FragmentActivity implements
		OnPageChangeListener, TextWatcher, OnClickListener, OnTouchListener {

	private String TAG = "ACEChatKeyboardActivity";
	private EUExChatKeyboard mUexBaseObj;
	private EditText mEditText;
	private ImageButton mBtnEmojicon;
	private Button mBtnSend;
	private ImageButton mBtnAdd;
	private ImageButton mBtnVoice;
	private Button mBtnVoiceInput;
	private LinearLayout mParentLayout;
	private LinearLayout mEditLayout;
	private LinearLayout mPagerLayout;
	private LinearLayout mEmojiconsLayout;
	private ViewPager mEmojiconsPager;
	private LinearLayout mEmojiconsIndicator;
	private LinearLayout mSharesLayout;
	private ViewPager mSharesPager;
	private LinearLayout mSharesIndicator;
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
	private Bitmap mTouchDownImg = null;
	private Bitmap mDragOutsideImg = null;
	private TextView mRecordTimes;
	private String mEmojiconsXmlPath;
	private String mSharesXmlPath;
	private static int NUMBER_OF_EMOJICONS;
	private static int NUMBER_OF_EMOJICONS_PER_PAGE = 23;
	private static int NUMBER_OF_SHARES;
	private static int NUMBER_OF_SHARES_PER_PAGE = 8;
	private static int TIMER_HANDLER_MESSAGE_WHAT = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, " onCreate ");
		CRes.init(getApplication());
		Intent intent = getIntent();
		getExtraFromIntent(intent);

		setContentView(CRes.plugin_chatkeyboard_layout);
		mParentLayout = (LinearLayout) findViewById(CRes.plugin_chatkeyboard_parent_layout);
		mEditLayout = (LinearLayout) findViewById(CRes.plugin_chatkeyboard_edit_input_layout);
		mPagerLayout = (LinearLayout) findViewById(CRes.plugin_chatkeyboard_pager_layout);
		
		mRecordTipsLayout = (FrameLayout) findViewById(CRes.plugin_chatkeyboard_voice_record_tips_layout);
		mRecordTipsImage = (ImageButton) findViewById(CRes.plugin_chatkeyboard_voice_record_tips_image);
		if (mTouchDownImg != null) {
			mRecordTipsImage.setImageBitmap(mTouchDownImg);
		}
		DisplayMetrics dm = getResources().getDisplayMetrics();
		mRecordTipsLayout.getLayoutParams().height = dm.heightPixels / 2;
		mRecordTimes = (TextView) findViewById(CRes.plugin_chatkeyboard_voice_record_times);
		int textColor = intent.getIntExtra(
				EChatKeyboardUtils.CHATKEYBOARD_EXTRA_TEXTCOLOR,
				BUtility.parseColor("#DDFFFFFF"));
		mRecordTimes.setTextColor(textColor);
		float textSize = intent.getFloatExtra(
				EChatKeyboardUtils.CHATKEYBOARD_EXTRA_TEXTSIZE, 20.0f);
		mRecordTimes.setTextSize(textSize);

		mBtnEmojicon = (ImageButton) findViewById(CRes.plugin_chatkeyboard_btn_emojicon);
		mBtnEmojicon.setOnClickListener(this);
		mEditText = (EditText) findViewById(CRes.plugin_chatkeyboard_edit_input);
		mEditText.addTextChangedListener(this);
		mEditText.setOnClickListener(this);
		if (intent
				.hasExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_PLACEHOLD)) {
			String hint = intent
					.getStringExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_PLACEHOLD);
			mEditText.setHint(hint);
		}
		
		mBtnSend = (Button) findViewById(CRes.plugin_chatkeyboard_btn_send);
		mBtnSend.setOnClickListener(this);
		mBtnAdd = (ImageButton) findViewById(CRes.plugin_chatkeyboard_btn_add);
		mBtnAdd.setOnClickListener(this);
		mBtnVoice = (ImageButton) findViewById(CRes.plugin_chatkeyboard_btn_voice);
		mBtnVoice.setOnClickListener(this);
		mBtnVoiceInput = (Button) findViewById(CRes.plugin_chatkeyboard_btn_voice_input);
		mBtnVoiceInput.setOnTouchListener(this);

		mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		initEmojicons();
		initShares();

		mEmojiconsLayout = (LinearLayout) findViewById(CRes.plugin_chatkeyboard_emojicons_layout);
		mEmojiconsPager = (ViewPager) findViewById(CRes.plugin_chatkeyboard_emojicons_pager);
		mEmojiconsPager.setAdapter(new EmotjiconsPagerAdapter());
		mEmojiconsPager.setOnPageChangeListener(this);
		mEmojiconsIndicator = (LinearLayout) findViewById(CRes.plugin_chatkeyboard_emojicons_pager_indicator);

		mSharesLayout = (LinearLayout) findViewById(CRes.plugin_chatkeyboard_shares_layout);
		mSharesPager = (ViewPager) findViewById(CRes.plugin_chatkeyboard_shares_pager);
		mSharesPager.setAdapter(new SharesPagerAdapter());
		mSharesPager.setOnPageChangeListener(this);
		mSharesIndicator = (LinearLayout) findViewById(CRes.plugin_chatkeyboard_shares_pager_indicator);

		initPagerIndicator();
		checkKeyboardHeight(mParentLayout);
	}

	private void getExtraFromIntent(Intent intent) {
		if (intent.hasExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_UEXBASE_OBJ)) {
			mUexBaseObj = (EUExChatKeyboard) intent
					.getParcelableExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_UEXBASE_OBJ);
		}
		if (intent
				.hasExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_EMOJICONS_XML_PATH)) {
			mEmojiconsXmlPath = intent
					.getStringExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_EMOJICONS_XML_PATH);
		}
		if (intent
				.hasExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_SHARES_XML_PATH)) {
			mSharesXmlPath = intent
					.getStringExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_SHARES_XML_PATH);
		}
		if (intent.hasExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_TOUCHDOWNIMG)) {
			String touchDownImg = intent
					.getStringExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_TOUCHDOWNIMG);
			touchDownImg = touchDownImg.substring(BUtility.F_Widget_RES_SCHEMA
					.length());
			touchDownImg = BUtility.F_Widget_RES_path + touchDownImg;
			mTouchDownImg = mUexBaseObj.getBitmap(touchDownImg);
		}
		if (intent
				.hasExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_DRAGOUTSIDEIMG)) {
			String dragOutsideImg = intent
					.getStringExtra(EChatKeyboardUtils.CHATKEYBOARD_EXTRA_DRAGOUTSIDEIMG);
			dragOutsideImg = dragOutsideImg
					.substring(BUtility.F_Widget_RES_SCHEMA.length());
			dragOutsideImg = BUtility.F_Widget_RES_path + dragOutsideImg;
			mDragOutsideImg = mUexBaseObj.getBitmap(dragOutsideImg);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		if (isKeyBoardVisible) {
			mInputManager.toggleSoftInputFromWindow(
					mEditText.getWindowToken(),
					InputMethodManager.SHOW_FORCED, 0);
		}
		super.onDestroy();
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
	 * Reading all emoticons in local cache
	 */
	private void initEmojicons() {
		InputStream in = null;
		try {
			String xmlPath = mEmojiconsXmlPath
					.substring(BUtility.F_Widget_RES_SCHEMA.length());
			String emojiconsFolder = BUtility.F_Widget_RES_path
					+ xmlPath.substring(0, xmlPath.lastIndexOf("/") + 1);
			String resXmlPath = BUtility.F_Widget_RES_path + xmlPath;
			in = getAssets().open(resXmlPath);
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
		AssetManager mngr = getAssets();
		InputStream in = null;
		try {
			in = mngr.open(path);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Bitmap temp = BitmapFactory.decodeStream(in, null, null);
		return temp;
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
			View layout = getLayoutInflater().inflate(
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

	public class EmojiconsGridAdapter extends BaseAdapter {
		private ArrayList<String> paths;

		public EmojiconsGridAdapter(ArrayList<String> paths) {
			this.paths = paths;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View layout = getLayoutInflater().inflate(
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
				final Drawable drawable = image.getDrawable();
				image.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
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
			String xmlPath = mSharesXmlPath
					.substring(BUtility.F_Widget_RES_SCHEMA.length());
			String sharesFolder = BUtility.F_Widget_RES_path
					+ xmlPath.substring(0, xmlPath.lastIndexOf("/") + 1);
			String resXmlPath = BUtility.F_Widget_RES_path + xmlPath;
			in = getAssets().open(resXmlPath);
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
			View layout = getLayoutInflater().inflate(
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
			View layout = getLayoutInflater().inflate(
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
					if (mUexBaseObj != null) {
						String js = EUExChatKeyboard.SCRIPT_HEADER
								+ "if("
								+ EChatKeyboardUtils.CHATKEYBOARD_FUN_ON_SHAREMENUITEM
								+ "){"
								+ EChatKeyboardUtils.CHATKEYBOARD_FUN_ON_SHAREMENUITEM
								+ "('" + position + "');}";
						mUexBaseObj.onCallback(js);
					}
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
				ImageView imageView = new ImageView(this);
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

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		mBtnSend.setVisibility(mEditText.getText().length() != 0 ? View.VISIBLE
				: View.GONE);
		mBtnAdd.setVisibility(mEditText.getText().length() == 0 ? View.VISIBLE
				: View.GONE);
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	/**
	 * Checking keyboard visibility
	 */
	private void checkKeyboardHeight(final View parentLayout) {
		parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Rect r = new Rect();
						parentLayout.getWindowVisibleDisplayFrame(r);
						int screenHeight = parentLayout.getRootView()
								.getHeight();
						int heightDifference = screenHeight - (r.bottom);
						if (heightDifference > 100) {
							isKeyBoardVisible = true;
							if (!mEditText.isFocused()) {
								new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {
										mEditText.requestFocus();
									}
								}, 100);
							}
						} else {
							isKeyBoardVisible = false;
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
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
				mInputManager.toggleSoftInputFromWindow(
						mEditText.getWindowToken(),
						InputMethodManager.SHOW_FORCED, 0);
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
						InputMethodManager.SHOW_FORCED, 0);
			}
			mEmojiconsLayout.setVisibility(View.GONE);
			mPagerLayout.setVisibility(View.GONE);
		}
	}

	private void toggleBtnAdd(boolean visible) {
		if (visible) {
			if (isKeyBoardVisible) {
				mInputManager.toggleSoftInputFromWindow(
						mEditText.getWindowToken(),
						InputMethodManager.SHOW_FORCED, 0);
			}
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
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
						InputMethodManager.SHOW_FORCED, 0);
			}
			mSharesLayout.setVisibility(View.GONE);
			mPagerLayout.setVisibility(View.GONE);
		}
	}

	private void toggleBtnSend() {
		Log.i(TAG, " toggleBtnSend mEditText " + mEditText.getText().toString());
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
		mEditText.setText(null);
	}

	private void toggleBtnVoice() {
		if (mBtnVoiceInput.getVisibility() == View.GONE) {
			if (isKeyBoardVisible) {
				mInputManager.toggleSoftInputFromWindow(
						mEditText.getWindowToken(),
						InputMethodManager.SHOW_FORCED, 0);
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
				mInputManager.toggleSoftInputFromWindow(
						mEditText.getWindowToken(),
						InputMethodManager.SHOW_FORCED, 0);
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
		if (isKeyBoardVisible) {
			mInputManager.toggleSoftInputFromWindow(mEditText.getWindowToken(),
					InputMethodManager.SHOW_FORCED, 0);
		}
		if (mPagerLayout.isShown()) {
			mPagerLayout.setVisibility(View.GONE);
		}
	}
	
	Handler timerHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == TIMER_HANDLER_MESSAGE_WHAT) {
				mRecordTimes.setText(msg.arg1+"\"");
				if (msg.arg1 > 59) {
					completeRecord();
					jsonVoiceActionCallback(1);
				}
				Message message = new Message(); 
				message.what = msg.what;
				message.arg1 = msg.arg1 + 1;
				sendMessageDelayed(message, 1000);
			}
		}
	};
	
	private void completeRecord() {
		mBtnVoiceInput.setText("按住  说话");
		mRecordTimes.setText("");
		if (mTouchDownImg != null) {
			mRecordTipsImage.setImageBitmap(mTouchDownImg);
		} else {
			mRecordTipsImage
					.setImageResource(CRes.plugin_chatkeyboard_voice_recording);
		}
		mRecordTipsLayout.setVisibility(View.GONE);
		timerHandler.removeMessages(TIMER_HANDLER_MESSAGE_WHAT);
	}

	private void handleRecordWhenDown() {
		int imageWidth = mRecordTipsImage.getWidth();
		int imageHeight = mRecordTipsImage.getHeight();
		android.view.ViewGroup.LayoutParams viewParams = mRecordTimes.getLayoutParams();
		((MarginLayoutParams) viewParams).setMargins((int)(0.65f * imageWidth), (int)(0.55f * imageHeight), 0, 0);
		mRecordTimes.setLayoutParams(viewParams);
		mRecordTimes.setVisibility(View.VISIBLE);
		mBtnVoiceInput.setText("松开 结束");
		mRecordTipsLayout.setVisibility(View.VISIBLE);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == CRes.plugin_chatkeyboard_btn_voice_input) {
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
				completeRecord();
				if (Math.abs(x) > btnWidth || Math.abs(y) > btnWidth) {
					jsonVoiceActionCallback(-1);
				} else {
					jsonVoiceActionCallback(1);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (Math.abs(x) > btnWidth || Math.abs(y) > btnWidth) {
					mRecordTimes.setVisibility(View.GONE);
					if (mDragOutsideImg != null) {
						mRecordTipsImage.setImageBitmap(mDragOutsideImg);
					} else {
						mRecordTipsImage
								.setImageResource(CRes.plugin_chatkeyboard_voice_cancle);
					}
				} else {
					mRecordTimes.setVisibility(View.VISIBLE);
					if (mTouchDownImg != null) {
						mRecordTipsImage.setImageBitmap(mTouchDownImg);
					} else {
						mRecordTipsImage
								.setImageResource(CRes.plugin_chatkeyboard_voice_recording);
					}
				}
				break;
			}
			return true;
		}
		return true;
	}

	private void jsonVoiceActionCallback(int status) {
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
}
