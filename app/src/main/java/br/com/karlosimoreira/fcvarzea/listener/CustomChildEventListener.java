package br.com.karlosimoreira.fcvarzea.listener;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

import br.com.karlosimoreira.fcvarzea.domain.User;

/**
 * Created by Carlos on 28/05/2016.
 */
public class CustomChildEventListener implements ChildEventListener {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            User u = dataSnapshot.getValue(User.class);
            Log.i("log", "ADDED");
            Log.i("log", "Nome: "+u.getName());
            Log.i("log", "Email: "+u.getEmail());
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        User u = dataSnapshot.getValue(User.class);
        Log.i("log", "CHANGED");
        Log.i("log", "Nome: "+u.getName());
        Log.i("log", "Email: "+u.getEmail());
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        User u = dataSnapshot.getValue(User.class);
        Log.i("log", "REMOVED");
        Log.i("log", "Nome: "+u.getName());
        Log.i("log", "Email: "+u.getEmail());
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
