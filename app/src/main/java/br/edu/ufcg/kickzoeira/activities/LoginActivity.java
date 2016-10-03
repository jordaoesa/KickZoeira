package br.edu.ufcg.kickzoeira.activities;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import br.edu.ufcg.kickzoeira.R;
import br.edu.ufcg.kickzoeira.model.KickZoeiraUser;

public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = "LOGIN";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //private EditText edtEmail;
    private AutoCompleteTextView edtEmail;
    private EditText edtPass;
    private View mProgressView;
    private View mLoginFormView;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("");

        edtEmail = (AutoCompleteTextView) findViewById(R.id.editText);

        edtEmail.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == R.id.next || id == EditorInfo.IME_NULL) {
                    edtPass.requestFocus();
                    return true;
                }
                return false;
            }
        });
        edtPass = (EditText) findViewById(R.id.editText2);

        edtPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    faz_login();
                    return true;
                }
                return false;
            }
        });

        edtEmail.setText("joao@mail.com");
        edtPass.setText("joao123");

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

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        progressDialog = new ProgressDialog(LoginActivity.this);
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

    public void handleLogin(View view) {
        faz_login();
    }

    private void faz_login() {


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
        } else if (!isPasswordValid(password)) {
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

            progressDialog.setMessage("Logando User");
            progressDialog.show();
            progressDialog.setCancelable(false);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Usuário não cadastrado.\nDeseja cadastrar usuário?")
                                        .setCancelable(false)
                                        .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                progressDialog.show();
                                                progressDialog.setCancelable(false);

                                                mAuth.createUserWithEmailAndPassword(email, password)
                                                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull final Task<AuthResult> task) {
                                                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                                                // If sign in fails, display a message to the user. If sign in succeeds
                                                                // the auth state listener will be notified and logic to handle the
                                                                // signed in user can be handled in the listener.
                                                                if (!task.isSuccessful()) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                } else {


                                                                    KickZoeiraUser newUser = new KickZoeiraUser(task.getResult().getUser());
                                                                    mDatabase.child("kickzoeirauser").child(task.getResult().getUser().getUid()).setValue(newUser);
                                                                    Toast.makeText(LoginActivity.this, "Criado com sucesso.", Toast.LENGTH_SHORT).show();
                                                                    Intent it = new Intent(getApplicationContext(), KickZoeiraMainActivity.class);
                                                                    startActivity(it);

                                                                    progressDialog.dismiss();
                                                                }

                                                                // ...
                                                            }
                                                        });

                                            }
                                        })
                                        .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                progressDialog.dismiss();
                                                Toast.makeText(LoginActivity.this, "Usuário nao criado.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                                //                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                //                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {

                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Logado com successo.", Toast.LENGTH_SHORT).show();
                                Intent it = new Intent(getApplicationContext(), KickZoeiraMainActivity.class);
                                startActivity(it);
                            }

                        }
                    });
        }

    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }




}
