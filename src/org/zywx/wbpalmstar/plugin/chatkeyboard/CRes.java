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

import android.content.Context;
import android.content.res.Resources;

import java.util.Locale;

public class CRes {
    private static boolean init;
    public static int app_name;
    public static int plugin_chatkeyboard_layout;
    public static int plugin_chatkeyboard_emojicons_grid;
    public static int plugin_chatkeyboard_emojicons_item;
    public static int plugin_chatkeyboard_shares_grid;
    public static int plugin_chatkeyboard_shares_item;

    public static int plugin_chatkeyboard_parent_layout;
    public static int plugin_chatkeyboard_edit_input_layout;
    public static int plugin_chatkeyboard_pager_layout;
    public static int plugin_chatkeyboard_voice_record_tips_layout;
    public static int plugin_chatkeyboard_voice_record_tips_image;
    public static int plugin_chatkeyboard_voice_record_times;
    public static int plugin_chatkeyboard_btn_emojicon;
    public static int plugin_chatkeyboard_edit_input;
    public static int plugin_chatkeyboard_btn_send;
    public static int plugin_chatkeyboard_btn_add;
    public static int plugin_chatkeyboard_btn_voice;
    public static int plugin_chatkeyboard_down;
    public static int plugin_chatkeyboard_btn_voice_input;
    public static int plugin_chatkeyboard_emojicons_layout;
    public static int plugin_chatkeyboard_emojicons_pager;
    public static int plugin_chatkeyboard_emojicons_pager_indicator;
    public static int plugin_chatkeyboard_shares_layout;
    public static int plugin_chatkeyboard_shares_pager;
    public static int plugin_chatkeyboard_shares_pager_indicator;
    public static int plugin_chatkeyboard_emojicons_grid_view;
    public static int plugin_chatkeyboard_emojicon_item;
    public static int plugin_chatkeyboard_shares_grid_view;
    public static int plugin_chatkeyboard_share_image;
    public static int plugin_chatkeyboard_share_name;

    public static int plugin_chatkeyboard_pages_pointer_focus;
    public static int plugin_chatkeyboard_pages_pointer_normal;
    public static int plugin_chatkeyboard_voice_cancle;
    public static int plugin_chatkeyboard_voice_recording;

    public static int plugin_chatkeyboard_pager_indicator_width;
    public static int plugin_chatkeyboard_pager_indicator_left;
    public static int plugin_chatkeyboard_pager_indicator_top;

    public static boolean init(Context context) {
        if (init) {
            return init;
        }
        String packg = context.getPackageName();
        Resources res = context.getResources();
        app_name = res.getIdentifier("app_name", "string", packg);

        plugin_chatkeyboard_layout = res.getIdentifier("plugin_chatkeyboard_layout", "layout", packg);
        plugin_chatkeyboard_emojicons_grid = res.getIdentifier("plugin_chatkeyboard_emojicons_grid", "layout", packg);
        plugin_chatkeyboard_emojicons_item = res.getIdentifier("plugin_chatkeyboard_emojicons_item", "layout", packg);
        plugin_chatkeyboard_shares_grid = res.getIdentifier("plugin_chatkeyboard_shares_grid", "layout", packg);
        plugin_chatkeyboard_shares_item = res.getIdentifier("plugin_chatkeyboard_shares_item", "layout", packg);

        plugin_chatkeyboard_parent_layout = res.getIdentifier("plugin_chatkeyboard_parent_layout", "id", packg);
        plugin_chatkeyboard_edit_input_layout = res.getIdentifier("plugin_chatkeyboard_edit_input_layout", "id", packg);
        plugin_chatkeyboard_pager_layout = res.getIdentifier("plugin_chatkeyboard_pager_layout", "id", packg);
        plugin_chatkeyboard_voice_record_tips_layout = res.getIdentifier("plugin_chatkeyboard_voice_record_tips_layout", "id", packg);
        plugin_chatkeyboard_voice_record_tips_image = res.getIdentifier("plugin_chatkeyboard_voice_record_tips_image", "id", packg);
        plugin_chatkeyboard_voice_record_times = res.getIdentifier("plugin_chatkeyboard_voice_record_times", "id", packg);
        plugin_chatkeyboard_btn_emojicon = res.getIdentifier("plugin_chatkeyboard_btn_emojicon", "id", packg);
        plugin_chatkeyboard_edit_input = res.getIdentifier("plugin_chatkeyboard_edit_input", "id", packg);
        plugin_chatkeyboard_btn_send = res.getIdentifier("plugin_chatkeyboard_btn_send", "id", packg);
        plugin_chatkeyboard_btn_add = res.getIdentifier("plugin_chatkeyboard_btn_add", "id", packg);
        plugin_chatkeyboard_btn_voice = res.getIdentifier("plugin_chatkeyboard_btn_voice", "id", packg);
        plugin_chatkeyboard_down = res.getIdentifier("plugin_chatkeyboard_down", "id", packg);
        plugin_chatkeyboard_btn_voice_input = res.getIdentifier("plugin_chatkeyboard_btn_voice_input", "id", packg);
        plugin_chatkeyboard_emojicons_layout = res.getIdentifier("plugin_chatkeyboard_emojicons_layout", "id", packg);
        plugin_chatkeyboard_emojicons_pager = res.getIdentifier("plugin_chatkeyboard_emojicons_pager", "id", packg);
        plugin_chatkeyboard_emojicons_pager_indicator = res.getIdentifier("plugin_chatkeyboard_emojicons_pager_indicator", "id", packg);
        plugin_chatkeyboard_shares_layout = res.getIdentifier("plugin_chatkeyboard_shares_layout", "id", packg);
        plugin_chatkeyboard_shares_pager = res.getIdentifier("plugin_chatkeyboard_shares_pager", "id", packg);
        plugin_chatkeyboard_shares_pager_indicator = res.getIdentifier("plugin_chatkeyboard_shares_pager_indicator", "id", packg);
        plugin_chatkeyboard_emojicons_grid_view = res.getIdentifier("plugin_chatkeyboard_emojicons_grid_view", "id", packg);
        plugin_chatkeyboard_emojicon_item = res.getIdentifier("plugin_chatkeyboard_emojicon_item", "id", packg);
        plugin_chatkeyboard_shares_grid_view = res.getIdentifier("plugin_chatkeyboard_shares_grid_view", "id", packg);
        plugin_chatkeyboard_share_image = res.getIdentifier("plugin_chatkeyboard_share_image", "id", packg);
        plugin_chatkeyboard_share_name = res.getIdentifier("plugin_chatkeyboard_share_name", "id", packg);

        plugin_chatkeyboard_pages_pointer_focus = res.getIdentifier("plugin_chatkeyboard_pages_pointer_focus", "drawable", packg);
        plugin_chatkeyboard_pages_pointer_normal = res.getIdentifier("plugin_chatkeyboard_pages_pointer_normal", "drawable", packg);
        plugin_chatkeyboard_voice_cancle = res.getIdentifier("plugin_chatkeyboard_voice_cancle", "drawable", packg);
        plugin_chatkeyboard_voice_recording = res.getIdentifier("plugin_chatkeyboard_voice_recording", "drawable", packg);

        plugin_chatkeyboard_pager_indicator_width = res.getIdentifier("plugin_chatkeyboard_pager_indicator_width", "dimen", packg);
        plugin_chatkeyboard_pager_indicator_left = res.getIdentifier("plugin_chatkeyboard_pager_indicator_left", "dimen", packg);
        plugin_chatkeyboard_pager_indicator_top = res.getIdentifier("plugin_chatkeyboard_pager_indicator_top", "dimen", packg);
        Locale language = Locale.getDefault();
        if (language.equals(Locale.CHINA)
                || language.equals(Locale.CHINESE)
                || language.equals(Locale.TAIWAN)
                || language.equals(Locale.TRADITIONAL_CHINESE)
                || language.equals(Locale.SIMPLIFIED_CHINESE)
                || language.equals(Locale.PRC)) {

        } else {
        }
        init = true;
        return true;
    }
}
