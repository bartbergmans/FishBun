package com.sangcomz.fishbun.ui.album;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.sangcomz.fishbun.bean.Album;
import com.sangcomz.fishbun.permission.PermissionCheck;
import com.sangcomz.fishbun.util.CameraUtil;
import com.sangcomz.fishbun.util.RegexUtil;

import java.util.ArrayList;
import java.util.HashMap;

class AlbumController {

    private AlbumActivity albumActivity;
    private ContentResolver resolver;
    private CameraUtil cameraUtil = new CameraUtil();


    AlbumController(AlbumActivity albumActivity) {
        this.albumActivity = albumActivity;
        this.resolver = albumActivity.getContentResolver();
    }

//    private void setSpanCount(int albumListSize) {
//        if (Define.ALBUM_LANDSCAPE_SPAN_COUNT > albumListSize)
//            Define.ALBUM_LANDSCAPE_SPAN_COUNT = albumListSize;
//        if (Define.ALBUM_PORTRAIT_SPAN_COUNT > albumListSize)
//            Define.ALBUM_PORTRAIT_SPAN_COUNT = albumListSize;
//    }

    boolean checkPermission() {
        PermissionCheck permissionCheck = new PermissionCheck(albumActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionCheck.CheckStoragePermission())
                return true;
        } else
            return true;
        return false;
    }

    void getAlbumList(String allViewTitle,
                      Boolean exceptGif) {
        new LoadAlbumList(allViewTitle, exceptGif).execute();
    }

    private class LoadAlbumList extends AsyncTask<Void, Void, ArrayList<Album>> {

        String allViewTitle;
        Boolean exceptGif;

        LoadAlbumList(String allViewTitle,
                      Boolean exceptGif) {
            this.allViewTitle = allViewTitle;
            this.exceptGif = exceptGif;

        }

        @Override
        protected ArrayList<Album> doInBackground(Void... params) {
            HashMap<Long, Album> albumHashMap = new HashMap<>();
            final String orderBy = MediaStore.Images.Media._ID + " DESC";
            String[] projection = new String[]{
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.BUCKET_ID};

            Cursor c = resolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    null, null, orderBy);

            int totalCounter = 0;
            if (c != null) {
                int bucketData = c
                        .getColumnIndex(MediaStore.Images.Media.DATA);
                int bucketColumn = c
                        .getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                int bucketColumnId = c
                        .getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

                albumHashMap.put((long) 0, new Album(0, allViewTitle, null, 0));

                RegexUtil regexUtil = new RegexUtil();
                while (c.moveToNext()) {
                    if (exceptGif && regexUtil.checkGif(c.getString(bucketData))) continue;
                    totalCounter++;
                    long bucketId = c.getInt(bucketColumnId);
                    Album album = albumHashMap.get(bucketId);
                    if (album == null) {
                        int imgId = c.getInt(c.getColumnIndex(MediaStore.MediaColumns._ID));
                        Uri path = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + imgId);
                        albumHashMap.put(bucketId,
                                new Album(bucketId,
                                        c.getString(bucketColumn),
                                        path.toString(), 1));
                        if (albumHashMap.get((long) 0).thumbnailPath == null)
                            albumHashMap.get((long) 0).thumbnailPath = path.toString();
                    } else {
                        album.counter++;
                    }
                    if (c.isLast()) {
                        albumHashMap.get((long) 0).counter = totalCounter;
                    }
                }
                c.close();
            }

            if (totalCounter == 0)
                albumHashMap.clear();

            ArrayList<Album> albumList = new ArrayList<>();
            for (Album album : albumHashMap.values()) {
                if (album.bucketId == 0)
                    albumList.add(0, album);
                else
                    albumList.add(album);
            }
            return albumList;
        }

        @Override
        protected void onPostExecute(ArrayList<Album> albumList) {
            super.onPostExecute(albumList);
//            if (albumList.size() > 0) {
//                setSpanCount(albumList.size());
//            }
            albumActivity.setAlbumList(albumList);
        }
    }

    void takePicture(Activity activity, String saveDir) {
        cameraUtil.takePicture(activity, saveDir);
    }

    String getSavePath() {
        return cameraUtil.getSavePath();
    }


    String getPathDir() {
        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM + "/Camera").getAbsolutePath();
    }
}
