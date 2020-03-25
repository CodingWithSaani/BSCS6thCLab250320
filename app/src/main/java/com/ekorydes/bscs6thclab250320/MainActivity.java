package com.ekorydes.bscs6thclab250320;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Step 1: To create Object of Firebase Firestore
    private FirebaseFirestore objectFirebaseFirestore;
    private static String HOTEL_DETAILS="HotelDetails";

    private Dialog objectDialog;
    private EditText documentIDET,noOfRoomsET,nameOfRestaurantET,locationOfHotelET;

    private TextView collectionValuesTV;
    private DocumentReference objectDocumentReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try
        {
            //step 2: Initialize the object
            objectFirebaseFirestore=FirebaseFirestore.getInstance();
            objectDialog=new Dialog(this);

            objectDialog.setContentView(R.layout.please_wait_dialog_file);
            objectDialog.setCancelable(false);

            documentIDET=findViewById(R.id.documentIDET);
            noOfRoomsET=findViewById(R.id.noOfRoomsET);

            nameOfRestaurantET=findViewById(R.id.nameOfRestaurantET);
            locationOfHotelET=findViewById(R.id.locationOfHotelET);

            collectionValuesTV=findViewById(R.id.collectionValuesTV);
        }
        catch (Exception e)
        {
            Toast.makeText(this, "onCreate:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void addValuesToFireStore(View v)
    {
        try
        {
            if(!documentIDET.getText().toString().isEmpty() && !noOfRoomsET.getText().toString().isEmpty()
            && !nameOfRestaurantET.getText().toString().isEmpty()
            && !locationOfHotelET.getText().toString().isEmpty()) {
                objectDialog.show();
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("noofrooms", noOfRoomsET.getText().toString());
                objectMap.put("nameofresturant", nameOfRestaurantET.getText().toString());

                objectMap.put("locationofhotel", locationOfHotelET.getText().toString());
                objectFirebaseFirestore.collection(HOTEL_DETAILS)
                        .document(documentIDET.getText().toString())
                        .set(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                objectDialog.dismiss();
                                documentIDET.setText("");

                                noOfRoomsET.setText("");
                                nameOfRestaurantET.setText("");

                                locationOfHotelET.setText("");
                                documentIDET.requestFocus();
                                Toast.makeText(MainActivity.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        objectDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Fails to add data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
            {
                Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e)
        {
            objectDialog.dismiss();
            Toast.makeText(this, "addValuesToFireStore:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getValuesFromCollection(View v)
    {
        try
        {
            if(!documentIDET.getText().toString().isEmpty()) {
                objectDialog.show();
                objectDocumentReference = objectFirebaseFirestore.collection(HOTEL_DETAILS)
                        .document(documentIDET.getText().toString());

                objectDocumentReference.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                objectDialog.dismiss();
                                if (!documentSnapshot.exists()) {
                                    Toast.makeText(MainActivity.this, "No values retrieved", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                collectionValuesTV.setText("");
                                String noOfRooms = documentSnapshot.getString("noofrooms");
                                String nameOfRestaurant = documentSnapshot.getString("nameofresturant");
                                String locationOfHotel = documentSnapshot.getString("locationofhotel");

                                String documentId = documentSnapshot.getId();
                                String completeDocument = "Hotel Name:" + documentId + "\n" +
                                        "No Of Rooms:" + noOfRooms + "\n" +
                                        "Name of Restaurant:" + nameOfRestaurant + "\n"
                                        + "Location of Hotel:" + locationOfHotel;

                                collectionValuesTV.setText(completeDocument);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Fails to retrieve data:" +
                                        e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else
            {
                Toast.makeText(this, "Please enter valid document id", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            objectDialog.dismiss();
            Toast.makeText(this, "getValuesFromCollection:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateValue(View view)
    {
        try
        {
            if(!documentIDET.getText().toString().isEmpty() && !noOfRoomsET.getText().toString().isEmpty()) {
                objectDialog.show();
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("noofrooms", noOfRoomsET.getText().toString());
                objectMap.put("noofRestaurants", "5");
                objectDocumentReference = objectFirebaseFirestore.collection(HOTEL_DETAILS)
                        .document(documentIDET.getText().toString());

                objectDocumentReference.update(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Data updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Fails to update data:"
                                        + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else
            {
                Toast.makeText(this, "Fill All Fields", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e)
        {
            objectDialog.dismiss();
            Toast.makeText(this, "updateValue:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteValue(View view)
    {
        try
        {
            if(!documentIDET.getText().toString().isEmpty()) {
                objectDialog.show();
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("noofRestaurants", FieldValue.delete());
                objectDocumentReference = objectFirebaseFirestore.collection(HOTEL_DETAILS)
                        .document(documentIDET.getText().toString());

                objectDocumentReference.update(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Data deleted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Fails to delete data:"
                                        + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else
            {
                Toast.makeText(this, "Please enter valid id", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e)
        {
            objectDialog.dismiss();
            Toast.makeText(this, "deleteValue:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
