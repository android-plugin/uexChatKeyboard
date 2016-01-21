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
    public final static String CHATKEYBOARD_EXTRA_INPUT_MODE = "org.zywx.wbpalmstar.plugin.uexchatkeyboard.CHATKEYBOARD_EXTRA_INPUT_MODE";
    public final static String CHATKEYBOARD_EXTRA_SEND_BTN_TEXT = "org.zywx.wbpalmstar.plugin.uexchatkeyboard.CHATKEYBOARD_EXTRA_SEND_TEXT";
    public final static String CHATKEYBOARD_EXTRA_SEND_BTN_TEXTSIZE = "org.zywx.wbpalmstar.plugin.uexchatkeyboard.CHATKEYBOARD_EXTRA_SEND_TEXTSIZE";
    public final static String CHATKEYBOARD_EXTRA_SEND_BTN_TEXTCOLOR = "org.zywx.wbpalmstar.plugin.uexchatkeyboard.CHATKEYBOARD_EXTRA_SEND_TEXTCOLOR";
    public final static String CHATKEYBOARD_EXTRA_SEND_BTN_BG_COLOR_UP = "org.zywx.wbpalmstar.plugin.uexchatkeyboard.CHATKEYBOARD_EXTRA_SEND_BG_COLOR_UP";
    public final static String CHATKEYBOARD_EXTRA_SEND_BTN_BG_COLOR_DOWN = "org.zywx.wbpalmstar.plugin.uexchatkeyboard.CHATKEYBOARD_EXTRA_SEND_BG_COLOR_DOWN";

	public final static String CHATKEYBOARD_PARAMS_JSON_KEY_EMOJICONS = "emojicons";
	public final static String CHATKEYBOARD_PARAMS_JSON_KEY_SHARES = "shares";
	public final static String CHATKEYBOARD_PARAMS_JSON_KEY_PLACEHOLD = "placeHold";
	public final static String CHATKEYBOARD_PARAMS_JSON_KEY_TOUCHDOWNIMG = "touchDownImg";
	public final static String CHATKEYBOARD_PARAMS_JSON_KEY_DRAGOUTSIDEIMG = "dragOutsideImg";
	public final static String CHATKEYBOARD_PARAMS_JSON_KEY_TEXTCOLOR = "textColor";
	public final static String CHATKEYBOARD_PARAMS_JSON_KEY_TEXTSIZE = "textSize";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_INPUT_MODE = "inputMode";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_SEND_BTN_TEXT = "sendBtnText";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_SEND_BTN_TEXTSIZE = "sendBtnTextSize";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_SEND_BTN_TEXTCOLOR = "sendBtnTextColor";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_SEND_BTN_BG_COLOR_UP = "sendBtnbgColorUp";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_SEND_BTN_BG_COLOR_DOWN = "sendBtnbgColorDown";
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

    public final static int INPUT_MODE_TEXT = 0;
    public final static int INPUT_MODE_VOICE = 1;
}
