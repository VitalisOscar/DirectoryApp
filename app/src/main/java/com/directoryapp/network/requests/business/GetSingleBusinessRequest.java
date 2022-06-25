package com.directoryapp.network.requests.business;

import android.content.Context;

import com.directoryapp.network.Request;
import com.directoryapp.network.RequestDispatcher;
import com.directoryapp.models.Business;
import com.directoryapp.models.User;
import com.google.firebase.firestore.Query;

public class GetSingleBusinessRequest extends Request {

    // Seller d limits to a single product for a particular seller, e.g from a seller's dashboard
    String id, owner_id;

    public GetSingleBusinessRequest(String id){
        this(id, null);
    }

    public GetSingleBusinessRequest(String id, String owner_id){
        this.id = id;
        this.owner_id = owner_id;
    }

    @Override
    public void dispatch(Context context, User loggedInUser, RequestDispatcher dispatcher) {
        Query query = getFirestore().collection(Business.DB_COLLECTION_NAME)
                .whereEqualTo("id", this.id);

        // seller id constraint
        if(owner_id != null){
            query = query.whereEqualTo("owner.id", owner_id);
        }

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Business business;

                    if(queryDocumentSnapshots.size() == 0){
                        dispatcher.onFail(new Exception("Business not found or cannot be accessed on this screen"));
                    }

                    business = Business.fromMap(queryDocumentSnapshots.getDocuments().get(0).getData());

                    // Return result
                    dispatcher.onSuccess(business);
                })
                .addOnFailureListener(dispatcher::onFail);
    }

}
