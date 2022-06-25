package com.directoryapp.network.requests.business;

import android.content.Context;

import com.directoryapp.network.Request;
import com.directoryapp.network.RequestDispatcher;
import com.directoryapp.models.Category;
import com.directoryapp.models.User;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class GetCategoriesRequest extends Request {

    @Override
    public void dispatch(Context context, User loggedInUser, RequestDispatcher dispatcher) {
        getFirestore().collection(Category.DB_COLLECTION_NAME)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Category> categories = new ArrayList<>();

                    for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                        categories.add(Category.fromMap(snapshot.getData()));
                    }

                    // Return result
                    dispatcher.onSuccess(categories);
                })
                .addOnFailureListener(dispatcher::onFail);
    }

}
