package com.zql.android.clippings.device.view.paste;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zql.android.clippings.R;
import com.zql.android.clippings.device.view.BaseActivity;
import com.zql.android.clippings.device.view.custom.DecorViewProxy;

/**
 * Created by scott on 2017/8/7.
 */

public class PasteActivity extends BaseActivity {


    private PasteContract.Presenter mPresenter;

    private ClipboardManager clipboardManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_paste;
    }

    @Override
    protected void initView() {
        PasteFragment pasteFragment = new PasteFragment();
        mPresenter = new PastePresenter(pasteFragment);
        addFragment(R.id.content,pasteFragment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.paste_title);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        new DecorViewProxy().bind(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.checkPaste(clipboardManager);
    }
}
