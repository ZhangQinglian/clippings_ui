package com.zql.android.clippings.usecase;

import com.zql.android.clippings.bridge.mvpc.UseCase;
import com.zql.android.clippings.device.ClippingsApplication;
import com.zql.android.clippings.device.db.paste.PasteItem;

/**
 * Created by scott on 2017/8/7.
 */

public class DeletePasteItem extends UseCase<DeletePasteItem.RequestValues,DeletePasteItem.ResponseValue> {

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        PasteItem pasteItem = requestValues.pasteItem;
        long n = ClippingsApplication.own().getClippingsDB().clippingDao().deletePasteItem(pasteItem);
        if(n>0){
            getUseCaseCallback().onSuccess(new DeletePasteItem.ResponseValue());
        }

    }

    public static final class RequestValues implements UseCase.RequestValues{

        public PasteItem pasteItem;
    }

    public static final class ResponseValue implements UseCase.ResponseValue{

    }
}
