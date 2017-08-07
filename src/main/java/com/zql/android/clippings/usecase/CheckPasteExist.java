package com.zql.android.clippings.usecase;

import com.zql.android.clippings.bridge.mvpc.UseCase;
import com.zql.android.clippings.device.ClippingsApplication;
import com.zql.android.clippings.device.db.paste.PasteItem;

/**
 * Created by scott on 2017/8/7.
 */

public class CheckPasteExist extends UseCase<CheckPasteExist.RequestValues,CheckPasteExist.ResponseValue> {

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        String paste = requestValues.pasteContent;
        if(paste != null){
            PasteItem pasteItem = ClippingsApplication.own().getClippingsDB().clippingDao().findPasteItem(paste);
            CheckPasteExist.ResponseValue responseValue = new ResponseValue();
            if(pasteItem != null){
                responseValue.isExist = true;
            }else {
                responseValue.isExist = false;
            }
            getUseCaseCallback().onSuccess(responseValue);
        }
    }

    public static final class RequestValues implements UseCase.RequestValues{
        public String pasteContent ;
    }

    public static final class ResponseValue implements UseCase.ResponseValue{
        public boolean isExist = false;
    }
}
