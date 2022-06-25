package com.directoryapp.network;

import android.content.Context;

import com.directoryapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Base class for one or more requests to firebase
 */
public abstract class Request {
    /**
     * Logic for the network request to be dispatched
     * @param context The current app context
     * @param loggedInUser The logged in user, if session is active
     * @param dispatcher The instance of the request dispatcher that dispatched this request, to receive callbacks
     */
    public abstract void dispatch(Context context, User loggedInUser, RequestDispatcher dispatcher);


    protected FirebaseAuth getAuth(){
        return FirebaseAuth.getInstance();
    }

    protected FirebaseFirestore getFirestore(){
        return FirebaseFirestore.getInstance();
    }
}
