package com.directoryapp.network.requests.business;

import android.content.Context;

import com.directoryapp.network.Request;
import com.directoryapp.network.RequestDispatcher;
import com.directoryapp.models.Business;
import com.directoryapp.models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class GetRecentBusinessesRequest extends Request {

    @Override
    public void dispatch(Context context, User loggedInUser, RequestDispatcher dispatcher) {
        getFirestore().collection(Business.DB_COLLECTION_NAME)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(6)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Business> results = new ArrayList<>();

                    for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                        results.add(Business.fromMap(snapshot.getData()));
                    }

                    // Return result
                    dispatcher.onSuccess(results);
                })
                .addOnFailureListener(dispatcher::onFail);
    }

}
