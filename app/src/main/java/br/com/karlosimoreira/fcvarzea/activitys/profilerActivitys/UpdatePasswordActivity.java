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
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.karlosimoreira.fcvarzea.activitys.MainActivity;
import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.domain.User;

public class UpdatePasswordActivity extends AppCompatActivity implements ValueEventListener {

    private Toolbar toolbar;
    private EditText newPassword;
    private EditText password;
    private User user;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
    public void onCancelled(DatabaseError databaseError) {
        FirebaseCrash.report( databaseError.toException() );
    }

    private void init() {
        toolbar.setTitle(getResources().getString(R.string.title_activity_update_password));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        newPassword = (EditText) findViewById(R.id.new_password);
        password = (EditText) findViewById(R.id.password_fragment);

        user = new User();
        user.setId(mAuth.getCurrentUser().getUid());
        user.contextDataDB(this);
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
                            updateData();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseCrash.report( e );
                Toast.makeText(
                        UpdatePasswordActivity.this,
                        e.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }


    public void updatePassword() {
        user.setNewPassword( newPassword.getText().toString() );
        user.setPassword( password.getText().toString() );

        reauthenticate();
    }

    private void updateData(){
        user.setNewPassword( newPassword.getText().toString() );
        user.setPassword( password.getText().toString() );

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if( firebaseUser == null ){
            return;
        }

        firebaseUser
                .updatePassword( user.getNewPassword() )
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if( task.isSuccessful() ){
                            newPassword.setText("");
                            password.setText("");

                            Toast.makeText(
                                    UpdatePasswordActivity.this,
                                    R.string.Sucesso_update,
                                    Toast.LENGTH_SHORT
                            ).show();
                            callMainActivity();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseCrash.report( e );
                        Toast.makeText(
                                UpdatePasswordActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }


    private void callMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
