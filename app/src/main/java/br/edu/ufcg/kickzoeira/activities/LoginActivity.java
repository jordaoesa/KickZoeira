package br.edu.ufcg.kickzoeira.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.edu.ufcg.kickzoeira.R;
import br.edu.ufcg.kickzoeira.model.KickZoeiraUser;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = "LOGIN";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //private EditText edtEmail;
    private AutoCompleteTextView edtEmail;
    private EditText edtPass;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (AutoCompleteTextView) findViewById(R.id.editText);
        edtPass = (EditText) findViewById(R.id.editText2);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void handleLogin(View view){



//        if (mAuth != null) {
//            return;
//        }

        // Reset errors.
        edtEmail.setError(null);
        edtPass.setError(null);

        // Store values at the time of the login attempt.
        final String email = edtEmail.getText().toString();
        final String password = edtPass.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            edtPass.setError(getString(R.string.error_invalid_password));
            focusView = edtPass;
            cancel = true;
        } else if(!isPasswordValid(password)){
            edtPass.setError(getString(R.string.error_invalid_password));
            focusView = edtEmail;
            cancel = true;

        }

        if (TextUtils.isEmpty(email)) {

            edtEmail.setError(getString(R.string.error_field_required));
            focusView = edtEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            edtEmail.setError(getString(R.string.error_invalid_email));
            focusView = edtEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {


            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());


                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {

                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                                // If sign in fails, display a message to the user. If sign in succeeds
                                                // the auth state listener will be notified and logic to handle the
                                                // signed in user can be handled in the listener.
                                                if (!task.isSuccessful()) {
                                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }else{
                                                    KickZoeiraUser newUser = new KickZoeiraUser(task.getResult().getUser());
                                                    mDatabase.child("kickzoeirauser").child(task.getResult().getUser().getUid()).setValue(newUser);
                                                    Toast.makeText(LoginActivity.this, "criado com sucesso", Toast.LENGTH_SHORT).show();
                                                    Intent it = new Intent(getApplicationContext(), KickZoeiraMainActivity.class);
                                                    startActivity(it);
                                                }

                                                // ...
                                            }
                                        });


//                                Log.w(TAG, "signInWithEmail:failed", task.getException());
//                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "success", Toast.LENGTH_SHORT).show();
                                Intent it = new Intent(getApplicationContext(), KickZoeiraMainActivity.class);
                                startActivity(it);
                            }

                        }
                    });
        }

    }


    public void handleSingUp(View view){
        Intent it = new Intent(getApplicationContext(), NewUserActivity.class);
        startActivity(it);
    }



    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        System.out.println("Pass nao eh valido");
        return password.length() > 4;
    }
}
