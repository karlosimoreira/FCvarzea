package br.com.karlosimoreira.fcvarzea.activitys.profilerActivitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.domain.User;

/**
 * Created by Carlos on 28/05/2016.
 */

public class SignUpActivity extends CommonActivity implements DatabaseReference.CompletionListener{

    private User user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private AutoCompleteTextView name;
    private int donoArena;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if( firebaseUser == null || user.getId() != null ){
                    return;
                }

                user.setId( firebaseUser.getUid() );
                user.saveDB( SignUpActivity.this );
            }
        };
        initViews();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if( mAuthStateListener != null ){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }


    protected void initViews(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.action_save:
                        sendSignUpData();
                        break;

                }
                return true;
            }
        });

        name = (AutoCompleteTextView) findViewById(R.id.name);
        email = (AutoCompleteTextView) findViewById(R.id.email_fragment);
        cbDonoArena = (CheckBox)findViewById(R.id.cbDonoArena);
        cbDonoArena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbDonoArena.isChecked()){
                    donoArena = 1;
                }else {
                    donoArena = 0;
                }
            }
        });
        password = (EditText) findViewById(R.id.password_fragment);
        progressBar = (ProgressBar) findViewById(R.id.sign_up_progress);
    }

    protected void initUser(){
        user = new User();
        user.setName( name.getText().toString() );
        user.setEmail( email.getText().toString() );
        user.setProprietario(String.valueOf(donoArena));
        user.setPassword( password.getText().toString() );
    }

    public void sendSignUpData(){
        openProgressBar();
        initUser();
        saveUser();
    }

    private void saveUser(){
        mAuth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( !task.isSuccessful() ){
                    closeProgressBar();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseCrash.report( e );
                showSnackbar( e.getMessage() );
            }
        });
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        mAuth.signOut();

        showToast( getResources().getString(R.string.Criar_profile) );
        closeProgressBar();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_save_actions, menu);
        return true;
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
