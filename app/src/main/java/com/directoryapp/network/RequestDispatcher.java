package com.directoryapp.network;

import android.content.Context;

import androidx.annotation.NonNull;

import com.directoryapp.helpers.AuthHelper;
import com.directoryapp.models.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestDispatcher {
    private Context context;
    private Object callingObject = null;
    private Method onSuccess = null, onFail = null;
    private User loggedInUser = null;

    private boolean CALLBACKS = false;

    private RequestDispatcher(Context context){
        this.context = context;

        // Set the current user
        this.loggedInUser = AuthHelper.getCurrentUser(context);
    }

    public static RequestDispatcher getInstance(Context context){
        return new RequestDispatcher(context);
    }

    public RequestDispatcher setCallbacks(@NonNull Object parentObject, @NonNull Method onSuccess, @NonNull Method onFail){
        this.callingObject = parentObject;
        this.onSuccess = onSuccess;
        this.onFail = onFail;

        this.CALLBACKS = true;

        return this;
    }

    public RequestDispatcher removeCallbacks(){
        this.callingObject = null;
        this.onSuccess = null;
        this.onFail = null;

        this.CALLBACKS = false;

        return this;
    }

    public void dispatch(Request request){
        request.dispatch(
            this.context,
            this.loggedInUser,
            this
        );
    }

    public void onSuccess(Object result) {
        if(CALLBACKS && onSuccess != null){
            try {
                onSuccess.invoke(callingObject, result);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void onFail(Exception exception) {
        if(CALLBACKS && onFail != null){
            try {
                onFail.invoke(callingObject, exception);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
