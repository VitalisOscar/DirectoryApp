package com.directoryapp.network.requests.business;

import android.content.Context;

import com.directoryapp.network.Request;
import com.directoryapp.network.RequestDispatcher;
import com.directoryapp.helpers.Utils;
import com.directoryapp.models.Category;
import com.directoryapp.models.Business;
import com.directoryapp.models.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SeedDbRequest extends Request {

    @Override
    public void dispatch(Context context, User loggedInUser, RequestDispatcher dispatcher) {
        FirebaseFirestore db = getFirestore();
        WriteBatch batch = db.batch();

        for (Category c:categories()){
            batch.set(db.collection(Category.DB_COLLECTION_NAME).document(c.getId()), c.toMap());
        }

        for (Business p:businesses()){
            DocumentReference ref = db.collection(Business.DB_COLLECTION_NAME).document();
            p.setId(ref.getId());

            batch.set(ref, p.toMap());
        }

        batch.commit()
                .addOnSuccessListener(dispatcher::onSuccess)
                .addOnFailureListener(dispatcher::onFail);
    }

    private ArrayList<Business> businesses(){
        ArrayList<Business> businesses = new ArrayList<>();
        ArrayList<Category> categories = categories();

        for(Category c:categories){
            for(int i = 0; i < 30; i++){
                Map<String, Map<String, Object>> openHours = new HashMap<>();
                Map<String, Object> mon = new HashMap<>();
                mon.put("opens", 6);
                mon.put("closes", 21);

                openHours.put("Monday", mon);

                businesses.add(new Business(
                        c.getName() + " " + i,
                        "Nice place here. Come one, come all. Good quality world class services",
                        "Mombasa Road",
                        "Nairobi",
                        openHours,
                        c.getImageUrl(),
                        c,
                        new User("Owner 1", "owner1@gmail.com", "0700123456", new ArrayList<>())
                ));
            }
        }

        return businesses;
    }

    private ArrayList<Category> categories(){
        ArrayList<Category> categories = new ArrayList<>();
        String timestamp = Utils.getCurrentTimestamp();
        categories.add(new Category(
                "Restaurants",
                "Restaurants description",
                "https://media.istockphoto.com/photos/cozy-restaurant-for-gathering-with-friends-picture-id1159992039?k=20&m=1159992039&s=612x612&w=0&h=t2lqevaWYLXvcjDeKzbHvIRF6GE3gxiqO6AIezr3Mws=",
                "1",
                timestamp
        ));

        categories.add(new Category(
                "Clubs and Bars",
                "Clubs and bars description",
                "https://media.istockphoto.com/photos/beer-picture-id462573477?k=20&m=462573477&s=612x612&w=0&h=Tk_Dbfva_pn-6RR-lGwXO0MHBf08CxLbopdBEub-i2A=",
                "2",
                timestamp
        ));

        categories.add(new Category(
                "Shopping",
                "Shopping description",
                "https://media.istockphoto.com/photos/elegant-shopping-mall-picture-id182408547?k=20&m=182408547&s=612x612&w=0&h=i1leNKgSdrSqEVlgpb3VLKnnHvwz5ZecKrMYNh2rICw=",
                "3",
                timestamp
        ));

        categories.add(new Category(
                "Auto Care",
                "Auto care description",
                "https://media.istockphoto.com/photos/napa-autocare-center-picture-id1317474398?k=20&m=1317474398&s=612x612&w=0&h=FcAoqWtWK4zzpKJWhzJcs1hCOG0XMFEt-bfJitvpJwM=",
                "4",
                timestamp
        ));

        categories.add(new Category(
                "Car hire",
                "Car hire description",
                "https://media.istockphoto.com/photos/car-shape-keyring-and-remote-control-key-picture-id622989514?k=20&m=622989514&s=612x612&w=0&h=1cWZudQ0H2FEkLpMB-Yz3icChOPAir5rs7itM3vTAtw=",
                "5",
                timestamp
        ));

        categories.add(new Category(
                "Hotels and Accommodation",
                "Hotels description",
                "https://media.istockphoto.com/photos/hotel-room-condominium-or-apartment-doorway-picture-id1165738696?k=20&m=1165738696&s=612x612&w=0&h=glThQeKk3IgE5RXN2IpeaQxQCnb4gBTMTu2lL1vmq3Y=",
                "6",
                timestamp
        ));

        return categories;
    }

}
