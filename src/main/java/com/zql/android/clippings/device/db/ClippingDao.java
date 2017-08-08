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

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.zql.android.clippings.device.db.paste.PasteItem;

import java.util.List;

/**
 * Created by scott on 2017/8/3.
 */
@Dao
public interface ClippingDao {

    @Insert
    void insertClippings(List<Clipping> clippingList);

    @Query("SELECT * FROM clippings WHERE id = :id")
    Clipping getClippingById(int id);

    @Query("SELECT * FROM clippings WHERE type != 0 AND status = 1")
    List<Clipping> getAllClippings();

    @Query("SELECT md5 FROM clippings")
    List<String> getAllMd5();

    @Insert
    void insertLabel(Label label);

    @Query("SELECT * FROM label")
    List<Label> getAllLabels();

    @Query("SELECT * FROM label WHERE md5 = :md5")
    List<Label> getLabelsByMd5(String md5);

    @Query("SELECT md5 FROM label WHERE label = :label")
    List<String> getMd5sByLabel(String label);

    @Update
    int updateClipping(Clipping clipping);

    @Query("SELECT * FROM clippings where favourite = 1")
    List<Clipping> getAllFavourite();

    @Query("SELECT * FROM clippings where title = :title and author = :author and location = :noteLocation and type = 2")
    Clipping getClippingNode(String title,String author,String noteLocation);

    @Delete
    int deleteLabel(Label label);

    @Query("SELECT id FROM label WHERE md5 = :md5 and label = :label")
    int getLabelId(String md5,String label);


    @Query("SELECT * FROM clippings WHERE status = 2")
    List<Clipping> getAllHideClippings();
    ///////////////粘贴板管理////////////////////
    @Insert()
    long addPasteItem(PasteItem pasteItem);

    @Query("SELECT * FROM pastes")
    List<PasteItem> getAllPastes();

    @Query("SELECT * FROM pastes WHERE content = :pasteContent")
    PasteItem findPasteItem(String pasteContent);

    @Delete
    int deletePasteItem(PasteItem pasteItem);
}
