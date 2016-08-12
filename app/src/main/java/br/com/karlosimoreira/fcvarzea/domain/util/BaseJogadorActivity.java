package br.com.karlosimoreira.fcvarzea.domain.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.karlosimoreira.fcvarzea.domain.User;

/**
 * Created by Carlos on 11/08/2016.
 */
public class BaseJogadorActivity extends AppCompatActivity{
    public static Context mContext;
    public DatabaseReference databaseReference;
    public Toolbar toolbar;
    public String clausura;
    public String value;
    public int valueTipo;
    public Query query;
    public RecyclerView rvUsers;
    public ArrayList<User> auxUserList;
    public SwipeRefreshLayout mSwipeRefreshLayout;

    public void queryJogadores(int tipo){
        switch(tipo)
        {
            case 0:
                query = databaseReference.child("User").orderByChild("name");
                break;
            case 1:
                query = databaseReference.child("User").orderByChild(clausura).equalTo(value);
                break;
        }
    }
    public void getDataForActivity(){
        Intent intent = getIntent();
        if (intent != null){
            Bundle params = intent.getExtras();
            if(params != null){
                clausura = params.getString("clausuraPrimaria");
                value = params.getString("valuePrimario");
                valueTipo = Integer.parseInt(params.getString("tipo"));
                Log.i("getDataForActivity", clausura + " & " + value + " & " + valueTipo);
            }
        }
    }
    public ArrayList<User> getnListUserFirebase(Query mQuery){
        final ArrayList<User>  userList = new ArrayList<>();

        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    userList.add(ds.getValue(User.class));
                }
                for (int i =0; i<userList.size(); i++){
                   // Log.i("Tag", "Name: "+ userList.get(i).getName() + "\n Aniver: " + userList.get(i).getBirthDate()+ "\n Photo: " + userList.get(i).getCity()+ "\n Classificação: " + userList.get(i).getPeBom()+ "\n "+userList.size()) ;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return userList;
    }

}
