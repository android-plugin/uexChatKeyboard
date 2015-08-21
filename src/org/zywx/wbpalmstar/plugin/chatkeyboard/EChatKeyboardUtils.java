package org.zywx.wbpalmstar.plugin.chatkeyboard;

public class EChatKeyboardUtils {
	public final static String CHATKEYBOARD_EXTRA_UEXBASE_OBJ = "org.zywx.wbpalmstar.plugin.uexchatkeyboard.CHATKEYBOARD_EXTRA_UEXBASE_OBJ";
	public final static String CHATKEYBOARD_EXTRA_EMOJICONS_XML_PATH = "org.zywx.wbpalmstar.plugin.uexchatkeyboard.CHATKEYBOARD_EXTRA_EMOJICONS_XML_PATH";
	public final static String CHATKEYBOARD_EXTRA_SHARES_XML_PATH = "org.zywx.wbpalmstar.plugin.uexchatkeyboard.CHATKEYBOARD_EXTRA_SHARES_XML_PATH";
	public final static String CHATKEYBOARD_EXTRA_PLACEHOLD = "org.zywx.wbpalmstar.plugin.uexchatkeyboard.CHATKEYBOARD_EXTRA_EMOJICONS_PLACEHOLD";
	public final static String CHATKEYBOARD_EXTRA_TOUCHDOWNIMG = "org.zywx.wbpalmstar.plugin.uexchatkeyboard.CHATKEYBOARD_EXTRA_TOUCHDOWNIMG";
	public final static String CHATKEYBOARD_EXTRA_DRAGOUTSIDEIMG = "org.zywx.wbpalmstar.plugin.uexchatkeyboard.CHATKEYBOARD_EXTRA_DRAGOUTSIDEIMG";
	public final static String CHATKEYBOARD_EXTRA_TEXTCOLOR = "org.zywx.wbpalmstar.plugin.uexchatkeyboard.CHATKEYBOARD_EXTRA_TEXTCOLOR";
	public final static String CHATKEYBOARD_EXTRA_TEXTSIZE = "org.zywx.wbpalmstar.plugin.uexchatkeyboard.CHATKEYBOARD_EXTRA_TEXTSIZE";

	public final static String CHATKEYBOARD_PARAMS_JSON_KEY_EMOJICONS = "emojicons";
	public final static String CHATKEYBOARD_PARAMS_JSON_KEY_SHARES = "shares";
	public final static String CHATKEYBOARD_PARAMS_JSON_KEY_PLACEHOLD = "placeHold";
	public final static String CHATKEYBOARD_PARAMS_JSON_KEY_TOUCHDOWNIMG = "touchDownImg";
	public final static String CHATKEYBOARD_PARAMS_JSON_KEY_DRAGOUTSIDEIMG = "dragOutsideImg";
	public final static String CHATKEYBOARD_PARAMS_JSON_KEY_TEXTCOLOR = "textColor";
	public final static String CHATKEYBOARD_PARAMS_JSON_KEY_TEXTSIZE = "textSize";
	public static final String CHATKEYBOARD_PARAMS_JSON_KEY_EMOJICONS_PATH = "emojiconsPath";
	public static final String CHATKEYBOARD_PARAMS_JSON_KEY_EMOJICONS_TEXT = "emojiconsText";
	public static final String CHATKEYBOARD_PARAMS_JSON_KEY_VOICE_STATUS = "status";
	//添加的键盘变化的回调。 @Note 2015-08-12
	public static final String CHATKEYBOARD_PARAMS_JSON_KEY_SHOW_STATUS = "status";

	public final static String CHATKEYBOARD_FUN_ON_COMMIT = "uexChatKeyboard.onCommit";
	public final static String CHATKEYBOARD_FUN_ON_SHAREMENUITEM = "uexChatKeyboard.onShareMenuItem";
	public final static String CHATKEYBOARD_FUN_ON_VOICEACTION = "uexChatKeyboard.onVoiceAction";
	//添加的键盘弹出的监听回调 @Note 2015-08-12
	public final static String CHATKEYBOARD_FUN_ON_KEYBOARDSHOW = "uexChatKeyboard.onKeyBoardShow";
	//添加新的回调,将数据以直接json的形式回调给前端; @Note 2015-08-18
	public final static String CHATKEYBOARD_FUN_ON_COMMIT_JSON = "uexChatKeyboard.onCommitJson";
}
