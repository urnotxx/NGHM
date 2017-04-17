package com.zzx.haoniu.nghmtaxi.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zzx.haoniu.nghmtaxi.app.AppContext;
import com.zzx.haoniu.nghmtaxi.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/2/21.
 */

public class LatLngDBManager {
    static private LatLngDBManager dbMgr = new LatLngDBManager();
    private DbOpenHelper dbHelper;

    private LatLngDBManager() {
        dbHelper = DbOpenHelper.getInstance(AppContext.getInstance().getApplicationContext());
    }

    public static synchronized LatLngDBManager getInstance() {
        if (dbMgr == null) {
            dbMgr = new LatLngDBManager();
        }
        return dbMgr;
    }

    /**
     * save latlng
     *
     * @param info
     */
    synchronized public void saveLatLng(LatLngInfo info) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (info.getOrderId() != null && !StringUtils.isEmpty(info.getOrderId()))
            values.put(LatLngDao.COLUMN_ORDERID, info.getOrderId());
        if (info.getAddTime() != 0)
            values.put(LatLngDao.COLUMN_TIME, info.getAddTime());
        if (info.getLat() != 0)
            values.put(LatLngDao.COLUMN_LAT, info.getLat());
        if (info.getLng() != 0)
            values.put(LatLngDao.COLUMN_LNG, info.getLng());
        if (db.isOpen()) {
            db.insert(LatLngDao.TABLE_NAME, null, values);
        }
    }

    /**
     * get Latlng list
     *
     * @return
     */
    synchronized public List<LatLngInfo> getLatlngtList(String orderId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<LatLngInfo> infos = new ArrayList<>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + LatLngDao.TABLE_NAME + " where orderId = '" + orderId + "'"  /* + " desc" */, null);
            while (cursor.moveToNext()) {
                long addTime = cursor.getLong(cursor.getColumnIndex(LatLngDao.COLUMN_TIME));
                double lat = cursor.getDouble(cursor.getColumnIndex(LatLngDao.COLUMN_LAT));
                double lng = cursor.getDouble(cursor.getColumnIndex(LatLngDao.COLUMN_LNG));
                LatLngInfo info = new LatLngInfo();
                info.setOrderId(orderId);
                info.setAddTime(addTime);
                info.setLat(lat);
                info.setLng(lng);
                infos.add(info);
            }
            cursor.close();
        }
        return infos;
    }

    synchronized public void closeDB() {
        if (dbHelper != null) {
            dbHelper.closeDB();
        }
        dbMgr = null;
    }
}
