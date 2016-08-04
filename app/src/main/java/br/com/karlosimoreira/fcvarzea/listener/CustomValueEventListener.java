package br.com.karlosimoreira.fcvarzea.listener;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;

import br.com.karlosimoreira.fcvarzea.domain.User;

/**
 * Created by Carlos on 28/05/2016.
 */
public class CustomValueEventListener extends AppCompatActivity implements ValueEventListener {
    User u;
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        u = dataSnapshot.getValue(User.class);

        Log.i("log", "TAMOS CustomValueEventListener!!!");
        Log.i("log", "Nome: "+u.getName());
        //Log.i("log", "Email: "+u.getClassificacao());
        Log.i("log", "Photo: "+u.getPhoto());
        Log.i("log", "Position: "+u.getPosition());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
