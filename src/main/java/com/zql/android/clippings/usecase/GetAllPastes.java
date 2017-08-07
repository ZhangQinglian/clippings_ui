package com.zql.android.clippings.usecase;

import com.zql.android.clippings.bridge.mvpc.UseCase;
import com.zql.android.clippings.device.ClippingsApplication;
import com.zql.android.clippings.device.db.paste.PasteItem;

import java.util.List;

/**
 * Created by scott on 2017/8/7.
 */

public class GetAllPastes extends UseCase<GetAllPastes.RequestValues,GetAllPastes.ResponseValue> {

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        List<PasteItem> pasteItems = ClippingsApplication.own().getClippingsDB().clippingDao().getAllPastes();
        GetAllPastes.ResponseValue responseValue = new ResponseValue();
        responseValue.pasteItems = pasteItems;
        getUseCaseCallback().onSuccess(responseValue);
    }

    public final static class RequestValues implements UseCase.RequestValues{

    }

    public final static class ResponseValue implements UseCase.ResponseValue{
        public List<PasteItem> pasteItems;
    }
}
