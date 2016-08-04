package br.com.karlosimoreira.fcvarzea.fragments;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by Carlos on 06/07/2016.
 */
public abstract class BaseFragments extends Fragment {

    protected static final String REQUEST_ID_TOKEN = "836916932650-dufe9i6pmucfs3igl2lmutchtpt3sd8c.apps.googleusercontent.com";
    protected static final int RC_SIGN_IN_GOOGLE = 7859;
    protected  final String PROVIDER_FACEBOOK = "facebook";
    protected  final String PROVIDER_GOOGLE = "google";

    protected GoogleApiClient mGoogleApiClient;
    protected AutoCompleteTextView email;
    protected EditText password;
    protected ProgressBar progressBar;
    protected TextView cadastro;
    protected AutoCompleteTextView phone;
    protected AutoCompleteTextView birthDate;

    protected void showSnackbar( String message ){
        Snackbar.make(progressBar,
                message,
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    protected void showToast( String message ){
        Toast.makeText(getActivity(),
                message,
                LENGTH_LONG)
                .show();
    }

    protected void openProgressBar(){
        progressBar.setVisibility( View.VISIBLE );
    }

    protected void closeProgressBar(){
        progressBar.setVisibility( View.GONE );
    }

    abstract protected void initViews();

    abstract protected void initUser();
}



