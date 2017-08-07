package com.zql.android.clippings.device.db.paste;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by scott on 2017/8/7.
 */

@Entity(tableName = "pastes")
public class PasteItem {

    @ColumnInfo()
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "content")
    public String clipContent;
}
