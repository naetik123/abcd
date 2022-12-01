package au.edu.unsw.infs3634.cryptoprofile;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.client.AuthUiInitProvider;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    final static String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setTitle("Welcome");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
       if (currentUser == null){
           List<AuthUI.IdpConfig> providers = Arrays.asList(
                   new AuthUI.IdpConfig.EmailBuilder().build());
           // Create and launch sign-in intent
           Intent signInIntent = AuthUI.getInstance()
                   .createSignInIntentBuilder()
                   .setAvailableProviders(providers)
                   .setIsSmartLockEnabled(false)
                   .build();
           signInLauncher.launch(signInIntent);

       } else{
           launchMainActivity();

       }


    }
    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            launchMainActivity();
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            //Log.d(TAG, "Authentication Failed");
            System.out.println(response.getError().getErrorCode() + "Authentication Failed");
        }
    }

    private void launchMainActivity(){
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);
    }

}