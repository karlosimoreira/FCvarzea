package br.com.karlosimoreira.fcvarzea.activitys.profilerActivitys;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.karlosimoreira.fcvarzea.activitys.MainActivity;
import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.domain.User;

public class RemoveUserActivity extends AppCompatActivity implements ValueEventListener, DatabaseReference.CompletionListener{
    private Toolbar toolbar;
    private EditText password;
    private FirebaseAuth mAuth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        User nUser = dataSnapshot.getValue(User.class);
        user.setEmail(nUser.getEmail());
    }
    @Override
    public void onCancelled(DatabaseError firebaseError) {
        FirebaseCrash.report( firebaseError.toException() );
    }
    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if( databaseError != null ){
            FirebaseCrash.report( databaseError.toException() );
        }
        Toast.makeText(
                RemoveUserActivity.this,
                R.string.remove_profile,
                Toast.LENGTH_SHORT
        ).show();
        callMainActivity();
        finish();
    }


    public void init(){
        toolbar.setTitle(getResources().getString(R.string.title_activity_remove_user));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        password = (EditText)findViewById(R.id.password_fragment);

        user = new User();
        user.setId( mAuth.getCurrentUser().getUid() );
        user.contextDataDB( this );
    }

    private void reauthenticate(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if( firebaseUser == null ){
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(
                user.getEmail(),
                user.getPassword()
        );

        firebaseUser.reauthenticate( credential )
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if( task.isSuccessful() ){
                            deleteUser();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseCrash.report( e );
                Toast.makeText(
                        RemoveUserActivity.this,
                        e.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    public void removeProfile(View view) {
        user.setPassword( password.getText().toString() );

        reauthenticate();
    }

    private void deleteUser(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if( firebaseUser == null ){
            return;
        }

        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if( !task.isSuccessful() ){
                    return;
                }

                user.removeDB( RemoveUserActivity.this);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseCrash.report( e );
                        Toast.makeText(
                                RemoveUserActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void callMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
