package com.zql.android.clippings.usecase;

import com.zql.android.clippings.bridge.mvpc.UseCase;
import com.zql.android.clippings.device.ClippingsApplication;
import com.zql.android.clippings.device.db.Clipping;

import java.util.List;

/**
 * Created by scott on 2017/8/8.
 */

public class GetAllHideClippings extends UseCase <GetAllHideClippings.RequestValues,GetAllHideClippings.ResponseValues>{

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        List<Clipping> clippings = ClippingsApplication.own().getClippingsDB().clippingDao().getAllHideClippings();
        if(clippings != null){
            ResponseValues responseValues = new ResponseValues();
            responseValues.clippingList = clippings;
            getUseCaseCallback().onSuccess(responseValues);
        }

    }

    public static final class RequestValues implements UseCase.RequestValues{

    }

    public static final class ResponseValues implements UseCase.ResponseValue{
        public List<Clipping> clippingList;
    }
}
