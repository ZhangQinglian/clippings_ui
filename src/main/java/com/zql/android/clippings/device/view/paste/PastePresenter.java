package com.zql.android.clippings.device.view.paste;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;

import com.zql.android.clippings.bridge.mvpc.UseCase;
import com.zql.android.clippings.bridge.mvpc.UseCaseHandler;
import com.zql.android.clippings.device.ClippingsApplication;
import com.zql.android.clippings.device.db.paste.PasteItem;
import com.zql.android.clippings.usecase.AddPasteItem;
import com.zql.android.clippings.usecase.CheckPasteExist;
import com.zql.android.clippings.usecase.DeletePasteItem;
import com.zql.android.clippings.usecase.GetAllPastes;

/**
 * Created by scott on 2017/8/7.
 */

public class PastePresenter implements PasteContract.Presenter {

    private PasteContract.View mView;


    public PastePresenter(PasteContract.View view){
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void deletePaste(PasteItem pasteItem) {
        DeletePasteItem.RequestValues value = new DeletePasteItem.RequestValues();
        value.pasteItem = pasteItem;
        UseCaseHandler.getInstance().execute(new DeletePasteItem(), value, new UseCase.UseCaseCallback<DeletePasteItem.ResponseValue>() {
            @Override
            public void onSuccess(DeletePasteItem.ResponseValue response) {
                fresh();
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void checkPaste(ClipboardManager clipboardManager) {
        ClipData clipData = clipboardManager.getPrimaryClip();
        int count = clipData.getItemCount();
        if(count > 0 ){
            ClipData.Item item= clipData.getItemAt(0);
            final CharSequence text = item.getText();
            if(text != null && text.length()>0){
                CheckPasteExist.RequestValues value = new CheckPasteExist.RequestValues();

                value.pasteContent = new StringBuilder(text).toString();
                UseCaseHandler.getInstance().execute(new CheckPasteExist(), value, new UseCase.UseCaseCallback<CheckPasteExist.ResponseValue>() {
                    @Override
                    public void onSuccess(CheckPasteExist.ResponseValue response) {
                        if(!response.isExist){
                            mView.showNewClipDialog(text);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
            }

        }
    }

    @Override
    public void addPaste(CharSequence charSequence) {
        StringBuilder s = new StringBuilder(charSequence);
        AddPasteItem.RequestValues value = new AddPasteItem.RequestValues();
        value.content = s.toString();
        UseCaseHandler.getInstance().execute(new AddPasteItem(), value, new UseCase.UseCaseCallback<AddPasteItem.ResponseValue>() {
            @Override
            public void onSuccess(AddPasteItem.ResponseValue response) {
                fresh();
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void fresh() {
        UseCaseHandler.getInstance().execute(new GetAllPastes(), new GetAllPastes.RequestValues(), new UseCase.UseCaseCallback<GetAllPastes.ResponseValue>() {
            @Override
            public void onSuccess(GetAllPastes.ResponseValue response) {
                mView.updateList(response.pasteItems);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void copy(String pasteContent) {
        ClipboardManager clipManager = (ClipboardManager) ClippingsApplication.own().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(ClipDescription.MIMETYPE_TEXT_PLAIN,pasteContent);
        clipManager.setPrimaryClip(clipData);
        mView.copySuccess();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }


}
