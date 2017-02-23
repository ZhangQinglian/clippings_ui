package com.zql.android.clippings;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.zql.android.clippings.sdk.parser.Clipping;
import com.zql.android.clippings.sdk.parser.ClippingsParser;
import com.zql.android.clippings.sdk.provider.ClippingContract;
import com.zql.android.clippings.sdk.provider.ClippingsProvider;
import com.zqlite.android.logly.Logly;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private BottomBar mBottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logly.setGlobalTag(new Logly.Tag(Logly.FLAG_THREAD_NAME, "Clippings", Logly.DEBUG));
        setContentView(R.layout.activity_main);

        mBottomBar = (BottomBar) findViewById(R.id.main_bottom_bar);
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
            }
        });
    }

    private void testQuery() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ClippingContract.CLIPPINGS_URI, ClippingContract.PROJECTION_CLIPPINGS_ALL, null, null, null);
        if (cursor != null) {
            try {

                while (cursor.moveToNext()) {
                    Clipping clipping = Clipping.getInstance(cursor);
                    Logly.d(clipping.toString());
                }

            } catch (Exception e) {

            } finally {
                cursor.close();
            }
        }

    }

    private void initDatabase() {
        try {
            ClippingsParser.own().parse(getAssets().open("My Clippings.txt"), this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
