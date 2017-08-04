/*******************************************************************************
 *    Copyright 2017-present, Clippings Contributors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/

package com.zql.android.clippings.device.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author qinglian.zhang, created on 2017/2/21.
 */
@Entity(tableName = "clippings")
public class Clipping {

    public static final int K_CLIPPING_TYPE_BOOKMARK = 0x00;

    public static final int K_CLIPPING_TYPE_LABEL = 0x01;

    public static final int K_CLIPPING_TYPE_NOTE = 0x02;

    public static final int K_CLIPPING_STATUS_NORMAL = 0x01;

    public static final int K_CLIPPING_STATUS_DELETED = 0x02;

    public static final int K_CLIPPINGS_FAVOURITE = 1;

    public static final int K_CLIPPINGS_UN_FAVOURITE = 2;

    private static MessageDigest MD5 ;

    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在数据库中的id
     */
    @PrimaryKey(autoGenerate = true)
    public int id;

    /**
     * 书名
     */
    @ColumnInfo(name = "title")
    public String title;

    /**
     * 作者
     */
    @ColumnInfo(name = "author")
    public String author;

    /**
     * 简报类型 <br />
     * {@link #K_CLIPPING_TYPE_BOOKMARK} <br />
     * {@link #K_CLIPPING_TYPE_LABEL} <br />
     * {@link #K_CLIPPING_TYPE_NOTE}
     */
    @ColumnInfo(name = "type")
    public int type;

    /**
     * 该简报在书本中的位置 <br />
     * 通常形式#211-213 <br />
     * 一般认为一个“位置”指的是128字节的数据 <br />
     */
    @ColumnInfo(name = "location")
    public String location;

    /**
     * 日期
     */
    @ColumnInfo(name = "date")
    public long date;

    /**
     * 标注内容 <br />
     * 仅当{@link Clipping#type}为{@link #K_CLIPPING_TYPE_LABEL}和{@link #K_CLIPPING_TYPE_NOTE}时有内容
     */
    @ColumnInfo(name="content")
    public String content;

    /**
     * 该简报对应的md5
     */
    @ColumnInfo(name = "md5")
    public String md5;

    /**
     * 简报的状态 <br />
     * {@link #K_CLIPPING_STATUS_NORMAL} <br />
     * {@link #K_CLIPPING_STATUS_DELETED}
     */
    @ColumnInfo(name = "status")
    public int status;

    /**
     * 简报是否被用户喜欢 <br/ >
     * {@link #K_CLIPPINGS_FAVOURITE} <br />
     * {@link #K_CLIPPINGS_UN_FAVOURITE}
     */
    @ColumnInfo(name = "favourite")
    public int favourite;

    @Override
    public String toString() {
        return title + " | " + author + " | " + location + " | " + type + " | " + date + " | \n" + content ;
    }

    public String getString(){
        return title +  author + location +  type +  date + content ;
    }

    public String getLocaleDate(){
        SimpleDateFormat saf = new SimpleDateFormat("MM/dd/yyyy E ahh:mm ", Locale.CHINESE);
        return saf.format(new Date(date));
    }

    public static String getMD5String(Clipping clipping){
        MD5.update(clipping.getString().getBytes());
        return new BigInteger(1,MD5.digest()).toString(16);
    }

    public static String getNoteLocation(Clipping clipping){
        if(clipping.type == Clipping.K_CLIPPING_TYPE_LABEL){
            String[] tmp = clipping.location.split("-");
            return "#" + tmp[1];
        }
        return "";
    }

    public static String getLocationAndType(Clipping clipping){
        if(clipping == null) return "";
        return clipping.location + (clipping.type== Clipping.K_CLIPPING_TYPE_LABEL?" - 摘要":" - 笔记");
    }

    public static String getNote(Clipping clipping){
        if(clipping == null) return "";
        return " \" " + clipping.content.trim() + " \" ";
    }

}
