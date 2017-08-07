package com.zql.android.clippings.device.view.paste;

import android.content.ClipboardManager;

import com.zql.android.clippings.bridge.mvpc.IContract;
import com.zql.android.clippings.bridge.mvpc.IPresenter;
import com.zql.android.clippings.bridge.mvpc.IView;
import com.zql.android.clippings.device.db.paste.PasteItem;

import java.util.List;

/**
 * Created by scott on 2017/8/7.
 */

public interface PasteContract extends IContract {

    interface Presenter extends IPresenter{
        void deletePaste(String content);
        void checkPaste(ClipboardManager clipboardManager);
        void addPaste(CharSequence charSequence);
        void fresh();
        void copy(String pasteContent);
    }

    interface View extends IView<PasteContract.Presenter>{
        void showNewClipDialog(CharSequence clipContent);
        void updateList(List<PasteItem> pasteItems);
        void copySuccess();
    }

}
