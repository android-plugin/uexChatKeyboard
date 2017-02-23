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

public class EChatKeyboardUtils {

    // key 
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_EMOJICONS = "emojicons";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_SHARES = "shares";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_PLACEHOLD = "placeHold";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_PLACEHOLDER = "placeholder";
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
    public static final String CHATKEYBOARD_PARAMS_JSON_KEY_SHOW_STATUS = "status";
    public static final String CHATKEYBOARD_PARAMS_JSON_KEY_HEIGHT = "height";
    public static final String CHATKEYBOARD_PARAMS_JSON_KEY_INSERTTEXT = "insertText";
    public static final String CHATKEYBOARD_PARAMS_JSON_KEY_INSERTTEXTS = "insertTexts";
    public static final String CHATKEYBOARD_PARAMS_JSON_KEY_INSERTTEXTCOLOR = "insertTextColor";
    public static final String CHATKEYBOARD_PARAMS_JSON_KEY_ISREPLACEKEYWORD = "isReplaceKeyword";
    public static final String CHATKEYBOARD_PARAMS_JSON_KEY_KEYWORD = "keyword";
    public static final String CHATKEYBOARD_PARAMS_JSON_KEY_KEYWORDS = "keywords";
    public static final String CHATKEYBOARD_PARAMS_JSON_KEY_START = "start";
    public static final String CHATKEYBOARD_PARAMS_JSON_KEY_END = "end";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_INPUTTEXTCOLOR = "inputTextColor";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_CHAT_KEY_BOARD_BG_COLOR = "chatKeyboardBgColor";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_INPUT_BG_COLOR = "inputBgColor";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_RECORDER_BG_COLOR = "recorderBgColor";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_RECORDER_NORMAL_TITLE = "recorderNormalTitle";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_RECORDER_HIGHLIGHTED_TITLE = "recorderHighlightedTitle";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_RECORDER_NORMAL_TITLE_COLOR = "recorderNormalTitleColor";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_RECORDER_HIGHLIGHTED_TITLE_COLOR = "recorderHighlightedTitleColor";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_KEY_BOARD_BTN_IMG = "keyboardBtnImg";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_VOICE_BTN_IMG = "voiceBtnImg";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_EMOTION_BTN_IMG = "emotionBtnImg";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_SELECTOR_BTN_IMG = "selectorBtnImg";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_ISROUND_FOR_BG = "isRound";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_FACE_VIEW_BG_COLOR = "faceViewBgColor";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_SHARE_VIEW_BG_COLOR = "shareViewBgColor";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_IS_SHOW_EMOJICONS_BTN = "isShowEmojiconsBtn";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_IS_SHOW_SHARES_BTN = "isShowSharesBtn";
    public final static String CHATKEYBOARD_PARAMS_JSON_KEY_IS_SHOW_SWITCH_BTN = "isShowSwitchBtn";

    // Callback
    public final static String CHATKEYBOARD_FUN_ON_COMMIT = "uexChatKeyboard.onCommit";
    public final static String CHATKEYBOARD_FUN_ON_SHAREMENUITEM = "uexChatKeyboard.onShareMenuItem";
    public final static String CHATKEYBOARD_FUN_ON_VOICEACTION = "uexChatKeyboard.onVoiceAction";
    public final static String CHATKEYBOARD_FUN_ON_KEYBOARDSHOW = "uexChatKeyboard.onKeyBoardShow";
    public final static String CHATKEYBOARD_FUN_ON_COMMIT_JSON = "uexChatKeyboard.onCommitJson";
    public final static String CHATKEYBOARD_FUN_CB_GET_INPUTBAR_HEIGHT = "uexChatKeyboard.cbGetInputBarHeight";
    public final static String CHATKEYBOARD_FUN_ON_INPUT_KEYWORD = "uexChatKeyboard.onInputKeyword";

    // type
    public final static int INPUT_MODE_TEXT = 0;
    public final static int INPUT_MODE_VOICE = 1;
}
