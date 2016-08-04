package br.com.karlosimoreira.fcvarzea.fragments.profilerFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import br.com.karlosimoreira.fcvarzea.fragments.BaseFragments;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LinkAccountsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LinkAccountsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LinkAccountsFragment extends BaseFragments implements  DatabaseReference.CompletionListener,GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseAuth mAuth;

    private View view;
    private Button btnLoguin;
    private Button btnFacebook;
    private Button btnGoogle;

    private User user;
    private CallbackManager callbackManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LinkAccountsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LinkAccountsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LinkAccountsFragment newInstance(String param1, String param2) {
        LinkAccountsFragment fragment = new LinkAccountsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        // FACEBOOK
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
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

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        Log.i("onCreate", "Entrou");

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_link_accounts, container, false);

        btnLoguin = (Button) view.findViewById(R.id.email_sign_in_button_fragment) ;
        btnLoguin.setOnClickListener(this);
        btnFacebook = (Button) view.findViewById(R.id.email_sign_in_facebook_button_fragment) ;
        btnFacebook.setOnClickListener(this);
        btnGoogle = (Button) view.findViewById(R.id.email_sign_in_google_button_fragment) ;
        btnGoogle.setOnClickListener(this);
        initViews();
        initUser();

        Log.i("onCreateView", "Entrou");
        // Inflate the layout for this fragment
        return view;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    protected void initViews(){
        email = (AutoCompleteTextView) view.findViewById(R.id.email_fragment);
        password = (EditText) view.findViewById(R.id.password_fragment);
        progressBar = (ProgressBar) view.findViewById(R.id.login_progress_fragment);

        initButtons();
    }

    private void initButtons(){

       setButtonLabel(

                R.id.email_sign_in_button_fragment,
                EmailAuthProvider.PROVIDER_ID,
                R.string.action_link,
                R.string.action_unlink,
                R.id.til_email_fragment,
                R.id.til_password_fragment
        );

        setButtonLabel(

                R.id.email_sign_in_facebook_button_fragment,
                FacebookAuthProvider.PROVIDER_ID,
                R.string.action_link_facebook,
                R.string.action_unlink_facebook
        );
        setButtonLabel(

                R.id.email_sign_in_google_button_fragment,
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


    public void sendLoginData(){
        if (isALinkedProvider(EmailAuthProvider.PROVIDER_ID)){
            unlinkProvider(EmailAuthProvider.PROVIDER_ID);
            return;
        }
        openProgressBar();
        initUser();
        accessEmailLoginData( user.getEmail(), user.getPassword() );
    }

    public void sendLoginDataFacebook(){
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

    public void sendLoginDataGoogle(){
        if( isALinkedProvider( GoogleAuthProvider.PROVIDER_ID ) ){
            unlinkProvider( GoogleAuthProvider.PROVIDER_ID );
            return;
        }

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }


    private void setButtonLabel( int buttonId, String providerId,int linkId, int unlinkId, int... fieldsIds ){

        if( isALinkedProvider( providerId ) ){

            ((Button) view.findViewById( buttonId )).setText( getString( unlinkId ) );
            showHideFields( false, fieldsIds );
        }
        else{
            ((Button) view.findViewById( buttonId )).setText( getString( linkId ) );
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
            view.findViewById( id ).setVisibility( status ? View.VISIBLE : View.GONE );
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
                            user.removeDB( LinkAccountsFragment.this );
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
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if( databaseError != null ){
            FirebaseCrash.report( databaseError.toException() );
        }
        mAuth.getCurrentUser().delete();
        mAuth.signOut();
        //finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        FirebaseCrash.report( new Exception( connectionResult.getErrorMessage() ) );
        showSnackbar( connectionResult.getErrorMessage() );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_in_button_fragment:
                sendLoginData();
                break;
            case R.id.email_sign_in_facebook_button_fragment:
                sendLoginDataFacebook();
                break;
            case R.id.email_sign_in_google_button_fragment:
                sendLoginDataGoogle();
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
