package br.com.karlosimoreira.fcvarzea.activitys.profilerActivitys;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.domain.User;

public class LinkAccountsActivity extends CommonActivity implements  DatabaseReference.CompletionListener,GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth mAuth;

    private Toolbar toolbar;
    private User user;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_accounts);
        //Inicializar a ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // FACEBOOK
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessFacebookLoginData( loginResult.getAccessToken() );
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException error) {
                FirebaseCrash.report( error );
                showSnackbar( error.getMessage() );
            }
        });

        // GOOGLE SIGN IN
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(REQUEST_ID_TOKEN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        initViews();
        initUser();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == RC_SIGN_IN_GOOGLE ){

            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent( data );
            GoogleSignInAccount account = googleSignInResult.getSignInAccount();

            if( account == null ){
                showSnackbar(getResources().getString(R.string.Falha_Google_login));
                return;
            }

            accessGoogleLoginData( account.getIdToken() );
        }
        else{

            callbackManager.onActivityResult( requestCode, resultCode, data );
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle( getResources().getString(R.string.title_activity_link_accounts) );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if( databaseError != null ){
            FirebaseCrash.report( databaseError.toException() );
        }
        mAuth.getCurrentUser().delete();
        mAuth.signOut();
        finish();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        FirebaseCrash.report( new Exception( connectionResult.getErrorMessage() ) );
        showSnackbar( connectionResult.getErrorMessage() );
    }


    protected void initViews(){
        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);

        initButtons();
    }

    private void initButtons(){

        setButtonLabel(
                R.id.email_sign_in_button,
                EmailAuthProvider.PROVIDER_ID,
                R.string.action_link,
                R.string.action_unlink,
                R.id.til_email,
                R.id.til_password
        );

        setButtonLabel(
                R.id.email_sign_in_facebook_button,
                FacebookAuthProvider.PROVIDER_ID,
                R.string.action_link_facebook,
                R.string.action_unlink_facebook
        );
        setButtonLabel(
                R.id.email_sign_in_google_button,
                GoogleAuthProvider.PROVIDER_ID,
                R.string.action_link_google,
                R.string.action_unlink_google
        );


    }

    protected void initUser(){
        user = new User();
        user.setEmail( email.getText().toString() );
        user.setPassword( password.getText().toString() );
    }


    private void accessEmailLoginData( String email, String password ){
        accessLoginData(
                "email",
                email,
                password
        );
    }

    private void accessFacebookLoginData(AccessToken accessToken){
        accessLoginData(
                PROVIDER_FACEBOOK,
                (accessToken != null ? accessToken.getToken() : null)
        );
    }

    private void accessGoogleLoginData(String accessToken){
        accessLoginData(
                PROVIDER_GOOGLE,
                accessToken
        );
    }

    private void accessLoginData(final String provider, String... tokens ){
        if( tokens != null && tokens.length > 0 && tokens[0] != null ) {
            AuthCredential credential = FacebookAuthProvider.getCredential(tokens[0]);
            credential = provider.equalsIgnoreCase("email") ? EmailAuthProvider.getCredential( tokens[0], tokens[1] ) : credential;
            credential = provider.equalsIgnoreCase("google") ? GoogleAuthProvider.getCredential( tokens[0], null) : credential;

            mAuth.getCurrentUser()
                    .linkWithCredential( credential )
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            closeProgressBar();

                            if( !task.isSuccessful() ){
                                return;
                            }

                            initButtons();
                            showSnackbar("Conta provider "+provider+" vinculada com sucesso.");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FirebaseCrash.report( e );
                            closeProgressBar();


                            showSnackbar("Error: "+e.getMessage());
                        }
                    });
        }
        else{
            mAuth.signOut();
        }

    }


    public void sendLoginData( View view ){
        if (isALinkedProvider(EmailAuthProvider.PROVIDER_ID)){
            unlinkProvider(EmailAuthProvider.PROVIDER_ID);
            return;
        }
        openProgressBar();
        initUser();
        accessEmailLoginData( user.getEmail(), user.getPassword() );
    }

    public void sendLoginDataFacebook( View view ){
        if (isALinkedProvider(FacebookAuthProvider.PROVIDER_ID)){
            unlinkProvider(FacebookAuthProvider.PROVIDER_ID);
            return;
        }
        LoginManager
                .getInstance()
                .logInWithReadPermissions(
                        this,
                        Arrays.asList("public_profile", "user_friends", "email")
                );

    }

    public void sendLoginDataGoogle( View view ){
        if( isALinkedProvider( GoogleAuthProvider.PROVIDER_ID ) ){
            unlinkProvider( GoogleAuthProvider.PROVIDER_ID );
            return;
        }

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }


    private void setButtonLabel(int buttonId, String providerId,int linkId, int unlinkId, int... fieldsIds ){

        if( isALinkedProvider( providerId ) ){

            ((Button) findViewById( buttonId )).setText( getString( unlinkId ) );
            showHideFields( false, fieldsIds );
        }
        else{
            ((Button) findViewById( buttonId )).setText( getString( linkId ) );
            showHideFields( true, fieldsIds );
        }
    }

    private boolean isALinkedProvider( String providerId ){

        for(UserInfo userInfo : mAuth.getCurrentUser().getProviderData() ){

            if( userInfo.getProviderId().equals( providerId ) ){
                return true;
            }
        }
        return false;
    }

    private void showHideFields( boolean status, int... ids ){
        for( int id : ids ){
            findViewById( id ).setVisibility( status ? View.VISIBLE : View.GONE );
        }
    }

    private void unlinkProvider(final String providerId ){

        mAuth
                .getCurrentUser()
                .unlink( providerId )
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if( !task.isSuccessful() ){
                            return;
                        }

                        initButtons();
                        showSnackbar("Conta provider "+providerId+" desvinculada com sucesso.");

                        if( isLastProvider( providerId ) ){
                            user.setId( mAuth.getCurrentUser().getUid() );
                            user.removeDB( LinkAccountsActivity.this );
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseCrash.report( e );
                showSnackbar("Error: "+e.getMessage());
            }
        });
    }

    private boolean isLastProvider( String providerId ){
        int size = mAuth.getCurrentUser().getProviders().size();
        return(
                size == 0 || (size == 1 && providerId.equals(EmailAuthProvider.PROVIDER_ID) )
        );
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public interface OnFragmentInteractionListener {
    }
}
