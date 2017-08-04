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

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by scott on 2017/8/3.
 */

@Database(entities = {Clipping.class,Label.class},version = 1)
public abstract class ClippingsDB extends RoomDatabase {
    public abstract ClippingDao clippingDao();
}
