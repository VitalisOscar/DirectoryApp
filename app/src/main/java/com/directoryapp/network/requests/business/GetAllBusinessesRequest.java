package com.directoryapp.network.requests.business;

import android.content.Context;

import com.directoryapp.network.Request;
import com.directoryapp.network.RequestDispatcher;
import com.directoryapp.models.Business;
import com.directoryapp.models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Map;

public class GetAllBusinessesRequest extends Request {

    Map<String, Object> filters;
    int page = 0, limit = 15;

    // Help in keeping track of where to start from on next page
    DocumentSnapshot previousSnapshot = null;

    public GetAllBusinessesRequest(Map<String, Object> filters, int page, int limit){
        this.filters = filters;
        this.page = page;
        this.limit = limit;
        this.previousSnapshot = null;
    }

    public GetAllBusinessesRequest(Map<String, Object> filters){
        this(filters, 0, 15);
    }

    public GetAllBusinessesRequest setPage(int page){
        this.page = page;
        return this;
    }

    @Override
    public void dispatch(Context context, User loggedInUser, RequestDispatcher dispatcher) {
        Query query = getFirestore().collection(Business.DB_COLLECTION_NAME);

        // Add Filters
        if(filters.containsKey("category_id")){
            query = query.whereEqualTo("category.id", filters.get("category_id"));
        }

        if(filters.containsKey("owner_id")){
            query = query.whereEqualTo("owner.id", filters.get("owner_id"));
        }

        if(filters.containsKey("keyword")){
            query = query.whereArrayContains("searchIndex", filters.get("keyword"));
        }

        if(filters.containsKey("location")){
            query = query.whereArrayContains("searchIndex", filters.get("location"));
        }

        query = query.orderBy("timestamp", Query.Direction.DESCENDING);

        // Pagination
        if(page > 0){
            query = query.limit(limit);

            if(previousSnapshot != null) {
                query = query.startAfter(previousSnapshot);
            }
        }

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Business> results = new ArrayList<>();

                    // Total fetched
                    int total = queryDocumentSnapshots.size();

                    int i = 0;
                    for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                        results.add(Business.fromMap(snapshot.getData()));

                        // Set the last snapshot. Next page will begin here
                        if(i == total - 1){
                            previousSnapshot = snapshot;
                        }

                        i++;
                    }

                    // Return result
                    dispatcher.onSuccess(results);
                })
                .addOnFailureListener(dispatcher::onFail);
    }

}
