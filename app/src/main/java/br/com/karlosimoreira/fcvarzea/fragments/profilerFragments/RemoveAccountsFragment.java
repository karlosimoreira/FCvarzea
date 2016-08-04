package br.com.karlosimoreira.fcvarzea.fragments.profilerFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.domain.User;


public class RemoveAccountsFragment extends Fragment {

    private View view;
    private EditText password;
    private FirebaseAuth mAuth;
    private User user;
    private Button btnRemove;

    public RemoveAccountsFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_remove_accounts, container, false);
        setHasOptionsMenu(true);
        init();
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void init(){
        password = (EditText)view.findViewById(R.id.password_fragment);
        password.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() || actionId == EditorInfo.IME_ACTION_DONE) {
                    removeProfile();
                }
                return false;
            }});

        user = new User();
        user.setId( mAuth.getCurrentUser().getUid() );

        user.valueEventListenerDataDB(getValueEventListener());
    }

    private ValueEventListener getValueEventListener(){

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User nUser = dataSnapshot.getValue(User.class);
                user.setEmail(nUser.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                FirebaseCrash.report( databaseError.toException() );
            }
        };

        return valueEventListener;
    }
    private DatabaseReference.CompletionListener getCompletionListener(){

        DatabaseReference.CompletionListener completionListener = new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if( databaseError != null ){
                    FirebaseCrash.report( databaseError.toException() );
                }
                Toast.makeText(
                        getActivity(),
                        R.string.remove_profile,
                        Toast.LENGTH_SHORT
                ).show();
            }
        };

        return completionListener;
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
                            Log.i("reauthenticate","onComplete ");
                            //user.removeDB(getCompletionListener());
                            deleteUserData();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseCrash.report( e );
                Toast.makeText(
                       getActivity(),
                        e.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    public void removeProfile() {
        user.setPassword( password.getText().toString() );
        reauthenticate();
    }

    private void deleteUserData(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if( firebaseUser == null ){
            Log.i("deleteUserData","firebaseUser: " + firebaseUser);
            return;
        }

        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if( !task.isSuccessful() ){
                    Log.i("deleteUserData","onComplete");
                    return;
                }
                Log.i("deleteUserData","removeDB");
               user.removeDB(getCompletionListener());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseCrash.report( e );
                        Toast.makeText(
                                getActivity(),
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_save_actions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save){
            removeProfile();
        }
        return super.onOptionsItemSelected(item);
    }
}
