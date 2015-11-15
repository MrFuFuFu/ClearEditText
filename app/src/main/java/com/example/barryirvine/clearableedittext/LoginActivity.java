package com.example.barryirvine.clearableedittext;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.example.barryirvine.clearableedittext.textwatchers.MinimumLengthTextWatcher;
import com.example.barryirvine.clearableedittext.textwatchers.ValidEmailTextWatcher;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private TextInputLayout mEmailView;
    private ValidEmailTextWatcher mValidEmailTextWatcher;
    private TextInputLayout mPasswordView;
    private MinimumLengthTextWatcher mValidPasswordTextWatcher;
    private View mProgressView;
    private View mLoginFormView;

    private static final String STATE_EMAIL_ERROR_TEXT = "STATE_EMAIL_ERROR_TEXT";
    private static final String STATE_PASSWORD_ERROR_TEXT = "STATE_PASSWORD_ERROR_TEXT";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BARRY", "onCreate");
        setContentView(R.layout.activity_login);
        setTitle(R.string.action_sign_in_short);

        // Set up the login form.
        mEmailView = (TextInputLayout) findViewById(R.id.email_text_input_layout);
        // The animation is switched off by default in the XML. This ensures that it only starts animating once the view is laid out.
        mEmailView.post(new Runnable() {
            @Override
            public void run() {
                mEmailView.setHintAnimationEnabled(true);
            }
        });
        mValidEmailTextWatcher = new ValidEmailTextWatcher(mEmailView);
        mEmailView.getEditText().addTextChangedListener(mValidEmailTextWatcher);
        mPasswordView = (TextInputLayout) findViewById(R.id.password_text_input_layout);
        mValidPasswordTextWatcher = new MinimumLengthTextWatcher(mPasswordView, getResources().getInteger(R.integer.min_length_password));
        mPasswordView.getEditText().addTextChangedListener(mValidPasswordTextWatcher);
        mPasswordView.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView textView, final int id, final KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        final Button emailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        emailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


    }

    @Override
    protected void onResume() {
        super.onResume();
        // Hide toolbar when in landscape on phones to
        if (!getResources().getBoolean(R.bool.is_tablet) && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
        } else {
            getSupportActionBar().show();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        /**
         * TextInputLayout isn't saving its state like it should. This works around that issue.
         * https://code.google.com/p/android/issues/detail?id=181621
         */
        outState.putCharSequence(STATE_EMAIL_ERROR_TEXT, mEmailView.getError());
        outState.putCharSequence(STATE_PASSWORD_ERROR_TEXT, mPasswordView.getError());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        /**
         * TextInputLayout isn't saving its state like it should. This works around that issue.
         * https://code.google.com/p/android/issues/detail?id=181621
         */
        super.onRestoreInstanceState(savedInstanceState);
        mEmailView.setError(savedInstanceState.getCharSequence(STATE_EMAIL_ERROR_TEXT));
        mPasswordView.setError(savedInstanceState.getCharSequence(STATE_PASSWORD_ERROR_TEXT));
    }

    private boolean allFieldsAreValid() {
        /**
         * Since the text watchers automatically focus on erroneous fields, do them in reverse order so that the first one in the form gets focus
         * &= may not be the easiest construct to decipher but it's a lot more concise. It just means that once it's false it doesn't get set to true
         */
        boolean isValid = mValidPasswordTextWatcher.validate();
        isValid &= mValidEmailTextWatcher.validate();
        return isValid;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        if (allFieldsAreValid()) {
            final String email = mEmailView.getEditText().getText().toString();
            final String password = mPasswordView.getEditText().getText().toString();
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

