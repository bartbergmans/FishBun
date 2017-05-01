package com.sangcomz.fishbun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.sangcomz.fishbun.define.Define;
import com.sangcomz.fishbun.ui.album.AlbumActivity;

import java.util.ArrayList;


public class FishBun {

    private static BaseProperty baseProperty;

    //    BaseProperty baseProperty;
    public static BaseProperty with(Activity activity) {
        return baseProperty = new BaseProperty(activity);
    }

    public static BaseProperty with(Fragment fragment) {
        return baseProperty = new BaseProperty(fragment);
    }

    public static class BaseProperty implements BasePropertyImpl {

        private ArrayList<Uri> arrayPaths = new ArrayList<>();
        private Activity activity = null;
        private Fragment fragment = null;
        private int requestCode = Define.ALBUM_REQUEST_CODE;

        public BaseProperty(Activity activity) {
            this.activity = activity;
        }

        public BaseProperty(Fragment fragment) {
            this.fragment = fragment;
        }

        public BaseProperty setArrayPaths(ArrayList<Uri> arrayPaths) {
            this.arrayPaths = arrayPaths;
            return baseProperty;
        }

        public BaseProperty setAlbumThumbnailSize(int size) {
            Define.ALBUM_THUMBNAIL_SIZE = size;
            return baseProperty;
        }

        @Override
        public BaseProperty setPickerSpanCount(int spanCount) {

            if (spanCount <= 0)
                spanCount = 3;
            Define.PHOTO_SPAN_COUNT = spanCount;
            return baseProperty;

        }

        @Deprecated
        @Override
        public BaseProperty setPickerCount(int count) {
            if (count <= 0)
                count = 1;
            Define.MAX_COUNT = count;
            return baseProperty;
        }

        @Override
        public BaseProperty setMaxCount(int count) {
            if (count <= 0)
                count = 1;
            Define.MAX_COUNT = count;
            return baseProperty;
        }

        @Override
        public BaseProperty setMinCount(int count) {
            if (count <= 0)
                count = 1;
            Define.MIN_COUNT = count;
            return baseProperty;
        }

        @Override
        public BaseProperty setActionBarColor(int actionbarColor) {
            Define.COLOR_ACTION_BAR = actionbarColor;
            return baseProperty;
        }

        @Override
        public BaseProperty setActionBarTitleColor(int actionbarTitleColor) {
            Define.COLOR_ACTION_BAR_TITLE_COLOR = actionbarTitleColor;
            return baseProperty;
        }

        @Override
        public BaseProperty setActionBarColor(int actionbarColor, int statusBarColor) {
            Define.COLOR_ACTION_BAR = actionbarColor;
            Define.COLOR_STATUS_BAR = statusBarColor;
            return baseProperty;
        }

        @Override
        public BaseProperty setActionBarColor(int actionbarColor, int statusBarColor, boolean isStatusBarLight) {
            Define.COLOR_ACTION_BAR = actionbarColor;
            Define.COLOR_STATUS_BAR = statusBarColor;
            Define.STYLE_STATUS_BAR_LIGHT = isStatusBarLight;
            return baseProperty;
        }

        @Override
        public BaseProperty setCamera(boolean isCamera) {
            Define.IS_CAMERA = isCamera;
            return baseProperty;
        }

        @Override
        public BaseProperty setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return baseProperty;
        }

        @Override
        public BaseProperty textOnNothingSelected(String message) {
            Define.MESSAGE_NOTHING_SELECTED = message;
            return baseProperty;
        }

        @Override
        public BaseProperty textOnImagesSelectionLimitReached(String message) {
            Define.MESSAGE_LIMIT_REACHED = message;
            return baseProperty;
        }

        @Override
        public BaseProperty setButtonInAlbumActivity(boolean isButton) {
            Define.IS_BUTTON = isButton;
            return baseProperty;
        }

        @Override
        public BaseProperty setReachLimitAutomaticClose(boolean isAutomaticClose) {
            Define.IS_AUTOMATIC_CLOSE = isAutomaticClose;
            return baseProperty;
        }

        @Override
        public BaseProperty setAlbumSpanCount(int portraitSpanCount, int landscapeSpanCount) {
            Define.ALBUM_PORTRAIT_SPAN_COUNT = portraitSpanCount;
            Define.ALBUM_LANDSCAPE_SPAN_COUNT = landscapeSpanCount;
            return baseProperty;
        }

        @Override
        public BaseProperty setAlbumSpanCountOnlyLandscape(int landscapeSpanCount) {
            Define.ALBUM_LANDSCAPE_SPAN_COUNT = landscapeSpanCount;
            return baseProperty;
        }

        @Override
        public BaseProperty setAlbumSpanCountOnlPortrait(int portraitSpanCount) {
            Define.ALBUM_PORTRAIT_SPAN_COUNT = portraitSpanCount;
            return baseProperty;
        }

        @Override
        public BaseProperty setAllViewTitle(String allViewTitle) {
            Define.TITLE_ALBUM_ALL_VIEW = allViewTitle;
            return baseProperty;
        }

        @Override
        public BaseProperty setActionBarTitle(String actionBarTitle) {
            Define.TITLE_ACTIONBAR = actionBarTitle;
            return baseProperty;
        }

        @Override
        public BaseProperty setHomeAsUpIndicatorDrawable(Drawable icon) {
            Define.homeAsUpIndicatorDrawable = icon;
            return baseProperty;
        }

        @Override
        public BaseProperty setOkButtonDrawable(Drawable icon) {
            Define.okButtonDrawable = icon;
            return baseProperty;
        }

        @Override
        public BaseProperty exceptGif(boolean isExcept) {
            Define.EXCEPT_GIF = isExcept;
            return baseProperty;
        }

        @Override
        public BaseProperty setMenuText(String text) {
            Define.TEXT_MENU = text;
            return baseProperty;
        }

        @Override
        public BaseProperty setMenuTextColor(int textColor) {
            Define.COLOR_MENU_TEXT = textColor;
            return baseProperty;
        }

        public void startAlbum() {
            Context context = null;
            if (activity != null)
                context = activity;
            else if (fragment != null)
                context = fragment.getActivity();
            else
                try {
                    throw new Exception("Activity or Fragment Null");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            if (Define.ALBUM_THUMBNAIL_SIZE == -1)
                Define.ALBUM_THUMBNAIL_SIZE = (int) context.getResources().getDimension(R.dimen.album_thum_size);

            setDefaultMessage(context);
            setMenuTextColor();

            Intent i = new Intent(context, AlbumActivity.class);
            i.putParcelableArrayListExtra(Define.INTENT_PATH, arrayPaths);

            if (activity != null)
                activity.startActivityForResult(i, requestCode);

            else if (fragment != null)
                fragment.startActivityForResult(i, requestCode);

        }

        private void setDefaultMessage(Context context) {
            if (Define.MESSAGE_NOTHING_SELECTED.equals(""))
                Define.MESSAGE_NOTHING_SELECTED = context.getResources().getString(R.string.msg_no_selected);

            if (Define.MESSAGE_LIMIT_REACHED.equals(""))
                Define.MESSAGE_LIMIT_REACHED = context.getResources().getString(R.string.msg_full_image);

            if (Define.TITLE_ALBUM_ALL_VIEW.equals(""))
                Define.TITLE_ALBUM_ALL_VIEW = context.getResources().getString(R.string.str_all_view);

            if (Define.TITLE_ACTIONBAR.equals(""))
                Define.TITLE_ACTIONBAR = context.getResources().getString(R.string.album);
        }

        private void setMenuTextColor() {
            if (Define.okButtonDrawable != null
                    || Define.TEXT_MENU == null
                    || Define.COLOR_MENU_TEXT != -1) return;
            if (Define.STYLE_STATUS_BAR_LIGHT)
                Define.COLOR_MENU_TEXT = Color.BLACK;
            else
                Define.COLOR_MENU_TEXT = Color.WHITE;

        }
    }

    interface BasePropertyImpl {

        BaseProperty setArrayPaths(ArrayList<Uri> arrayPaths);

        BaseProperty setAlbumThumbnailSize(int size);

        BaseProperty setPickerSpanCount(int spanCount);

        BaseProperty setPickerCount(int count);

        BaseProperty setMaxCount(int count);

        BaseProperty setMinCount(int count);

        BaseProperty setActionBarColor(int actionbarColor);

        BaseProperty setActionBarTitleColor(int actionbarTitleColor);

        BaseProperty setActionBarColor(int actionbarColor, int statusBarColor);

        BaseProperty setActionBarColor(int actionbarColor, int statusBarColor, boolean isStatusBarLight);

        BaseProperty setCamera(boolean isCamera);

        BaseProperty setRequestCode(int RequestCode);

        BaseProperty textOnNothingSelected(String message);

        BaseProperty textOnImagesSelectionLimitReached(String message);

        BaseProperty setButtonInAlbumActivity(boolean isButton);

        BaseProperty setReachLimitAutomaticClose(boolean isAutomaticClose);

        BaseProperty setAlbumSpanCount(int portraitSpanCount, int landscapeSpanCount);

        BaseProperty setAlbumSpanCountOnlyLandscape(int landscapeSpanCount);

        BaseProperty setAlbumSpanCountOnlPortrait(int portraitSpanCount);

        BaseProperty setAllViewTitle(String allViewTitle);

        BaseProperty setActionBarTitle(String actionBarTitle);

        BaseProperty setHomeAsUpIndicatorDrawable(Drawable icon);

        BaseProperty setOkButtonDrawable(Drawable icon);

        BaseProperty exceptGif(boolean isExcept);

        BaseProperty setMenuText(String text);

        BaseProperty setMenuTextColor(int color);

        void startAlbum();
    }


}
