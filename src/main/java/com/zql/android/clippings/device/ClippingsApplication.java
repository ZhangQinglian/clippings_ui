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

package com.zql.android.clippings.device;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.zql.android.clippings.device.db.ClippingsDB;


/**
 * @author qinglian.zhang, created on 2017/2/23.
 */
public class ClippingsApplication extends Application {

    private static ClippingsApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        if(sInstance == null) sInstance = this;
    }

    public static ClippingsApplication own(){
        return sInstance;
    }

    public ClippingsDB getClippingsDB(){
        return Room.databaseBuilder(this,ClippingsDB.class,"clippings").build();
    }
}
