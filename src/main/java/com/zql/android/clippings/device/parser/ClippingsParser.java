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

package com.zql.android.clippings.device.parser;

import android.content.Context;
import android.support.annotation.NonNull;

import com.zql.android.clippings.bridge.mvpc.UseCase;
import com.zql.android.clippings.bridge.mvpc.UseCaseHandler;
import com.zql.android.clippings.device.ClippingsApplication;
import com.zql.android.clippings.device.db.Clipping;
import com.zql.android.clippings.device.db.ClippingDao;
import com.zql.android.clippings.usecase.InsertClippings;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * @author qinglian.zhang, created on 2017/2/21.
 *         用于解析Kindle中的My Clippings.txt
 */
public class ClippingsParser {

    private final String kDivide = "=========";

    private final String kLeftBracketsStr = "(";

    private final String kRightBracketsHeseStr = ")";

    private final char kLeftBracketsChar = '(';

    private final char kRightBracketsChar = ')';

    public static final String CLIPPINGS_NAME = "My Clippings.txt";

    public final SimpleDateFormat kDateFormatZh = new SimpleDateFormat("yyyy年MM月dd日E ahh:mm:ss",Locale.CHINESE);

    private final String kLabelRegex = "#\\d+-\\d+";
    private final Pattern kLabelPattern ;
    private final String kLocationRegex = "#\\d+-\\d+|#\\d+";
    private final Pattern kLocationPattern ;

    private static ClippingsParser sInstance;

    private StringBuilder mStringBuilder = new StringBuilder();

    public interface Callback{
        void error();
        void success();
    }

    private ClippingsParser() {
        kLabelPattern = Pattern.compile(kLabelRegex);
        kLocationPattern = Pattern.compile(kLocationRegex);
    }

    public static synchronized ClippingsParser own() {
        if (sInstance == null) sInstance = new ClippingsParser();
        return sInstance;
    }

    /**
     * 解析My Clippings.txt对应的InputStream
     *
     * @param inputStream My Clippings.txt对应的InputStream
     */
    public void parse(final InputStream inputStream, Context context) {
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {

                ByteArrayOutputStream cache = new ByteArrayOutputStream(inputStream.available());
                int len;
                byte[] buffer = new byte[128];
                while ((len = inputStream.read(buffer)) > 0) {
                    cache.write(buffer, 0, len);
                }
                String content = new String(cache.toByteArray());
                cache.close();
                inputStream.close();
                String[] s = content.split("\n");
                for (int i = 0; i < s.length; i++) {
                    e.onNext(s[i]);
                }
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER).observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe(new ClippingSubscriber(context));
    }

    private class ClippingSubscriber implements Subscriber<String> {

        private Subscription mSubscription;

        private List<Clipping> mClippingList = new ArrayList<>();

        private List<String> mRawData = new ArrayList<>();

        private Context mContext;
        public ClippingSubscriber(Context context){
            mContext = context;
        }

        @Override
        public void onSubscribe(Subscription s) {
            mSubscription = s;
            mSubscription.request(1);
        }

        @Override
        public void onNext(String data) {
            if(data != null && !data.contains(kDivide)){
                if(data.trim().length()>0) mRawData.add(data);
                mSubscription.request(1);
            }else if(data != null && data.contains(kDivide)){
                mClippingList.add(parseClipping(mRawData));
                mRawData.clear();
                mSubscription.request(1);
            }

        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onComplete() {
            UseCaseHandler.getInstance().execute(new InsertClippings(), new InsertClippings.RequestValue(mClippingList), new UseCase.UseCaseCallback<InsertClippings.ResponseValue>() {
                @Override
                public void onSuccess(InsertClippings.ResponseValue response) {

                }

                @Override
                public void onError() {

                }
            });
        }
    }

    private Clipping parseClipping(List<String> rawData){
        Clipping clipping = new Clipping();
        clipping.author = getAuthor(rawData.get(0));
        clipping.title = getTitle(rawData.get(0),clipping.author);
        clipping.type = getType(rawData.get(1),rawData.size());
        clipping.date = getDateTime(rawData.get(1));
        clipping.location = getLocation(rawData.get(1));
        clipping.content = getContent(rawData);
        clipping.md5 = Clipping.getMD5String(clipping);
        clipping.status = Clipping.K_CLIPPING_STATUS_NORMAL;
        return clipping;
    }

    @NonNull
    private String getAuthor(String line1){
        cleanStringBuilder();
        int bracketsCount = 0;
        line1 = line1.replaceAll("\r|\n","");
        int index = line1.length()-1;

        while (line1.charAt(index) == kRightBracketsChar){
            bracketsCount++;
            index--;
        }
        index = line1.length() - 1;

        while (bracketsCount >0){
            char c = line1.charAt(index);
            index --;
            mStringBuilder.append(c);
            if(c == kLeftBracketsChar) bracketsCount--;
        }
        return mStringBuilder.reverse().toString().substring(1,mStringBuilder.length()-1);
    }

    @NonNull
    private String getTitle(String line1, String title){

        int end = line1.lastIndexOf(title)-2;
        return line1.substring(0,end);
    }

    private int getType(String line2,int size){
        if(size == 2) return Clipping.K_CLIPPING_TYPE_BOOKMARK;

        Matcher matcher = kLabelPattern.matcher(line2);
        if(matcher.find()){
            return Clipping.K_CLIPPING_TYPE_LABEL;
        }
        return Clipping.K_CLIPPING_TYPE_NOTE;

    }

    private String getLocation(String line2){

        Matcher matcher = kLocationPattern.matcher(line2);
        if(matcher.find()){
            return matcher.group();
        }
        return "";
    }
    private long getDateTime(String line2){
        String[] ss = line2.split(" ");
        String dateTime = ss[ss.length-2] + " " + ss[ss.length-1];
        try {
             return kDateFormatZh.parse(dateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String getContent(List<String> rawData){
        if(rawData.size()>2){
            return rawData.get(2);
        }
        return "";
    }
    private void cleanStringBuilder(){
        mStringBuilder.delete(0,mStringBuilder.length());
    }
}
