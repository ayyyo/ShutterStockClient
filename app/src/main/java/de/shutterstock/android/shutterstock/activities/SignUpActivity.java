package de.shutterstock.android.shutterstock.activities;

import android.app.IntentService;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import de.shutterstock.android.shutterstock.R;
import de.shutterstock.android.shutterstock.content.model.ShutterStockError;
import de.shutterstock.android.shutterstock.content.model.User;
import de.shutterstock.android.shutterstock.net.RestClient;
import de.shutterstock.android.shutterstock.utilities.SubscriberTextWatcher;
import retrofit.HttpException;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.util.Patterns.EMAIL_ADDRESS;

/**
 * Created by emanuele on 25.10.15.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, Observer<Object> {


    private class FormValidatorObservable implements Func3<CharSequence, CharSequence, CharSequence, Boolean> {

        @Override
        public Boolean call(CharSequence newUsername, CharSequence newEmail, CharSequence newPassword) {

            boolean emailValid = !TextUtils.isEmpty(newEmail) &&
                    EMAIL_ADDRESS.matcher(newEmail).matches();
            if (!emailValid) {
                mEmail.setError("Invalid Email!");
            }

            boolean passValid = !TextUtils.isEmpty(newPassword) && newPassword.length() > 7;
            if (!passValid) {
                mPassword.setError("Invalid Password!");
            }

            boolean usernameValid = !TextUtils.isEmpty(newUsername);
            if (!usernameValid) {
                mUsername.setError("Invalid Number!");
            }
            return emailValid && passValid && usernameValid;
        }
    }

    private TextView mEmail;
    private TextView mUsername;
    private TextView mPassword;

    private Observable<CharSequence> mEmailChangeObservable;
    private Observable<CharSequence> mPasswordChangeObservable;
    private Observable<CharSequence> mUsernameChangeObservable;
    private final CompositeSubscription mSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        mEmail = (TextView) findViewById(R.id.input_email);
        mUsername = (TextView) findViewById(R.id.input_username);
        mPassword = (TextView) findViewById(R.id.input_password);

        mEmailChangeObservable = Observable.create(new SubscriberTextWatcher(mEmail)).skip(1);
        mPasswordChangeObservable = Observable.create(new SubscriberTextWatcher(mPassword)).skip(1);
        mUsernameChangeObservable = Observable.create(new SubscriberTextWatcher(mUsername)).skip(1);

      /*  mSubscription.add(Observable
                .combineLatest(mUsernameChangeObservable, mEmailChangeObservable, mPasswordChangeObservable, new FormValidatorObservable())
                .subscribe(this));*/

        findViewById(R.id.btn_create_account).setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void onClick(View v) {
        User user = new User();
        user.email = mEmail.getText().toString();
        user.password = mPassword.getText().toString();
        user.username = mUsername.getText().toString();

        Log.e(getClass().getSimpleName(), " " + new Gson().toJson(user));

        mSubscription.add(RestClient.getApiDescriptor().registerUserCommand(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this));
    }

    @Override
    public void onCompleted() {
        Log.e(getClass().getSimpleName(), ")))");
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            try {
                ShutterStockError error = RestClient.getRestClient().convertError(((HttpException) e).response().errorBody());
                StringBuilder builder = new StringBuilder();
                if (error.errors != null) {
                    for (ShutterStockError.Error err : error.errors) {
                        builder.append(err.text);
                        builder.append("\n");
                    }
                }
                Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void onNext(Object o) {
        if (o instanceof Boolean) {
            findViewById(R.id.btn_create_account).setEnabled((boolean) o);
        } else if (o instanceof String) {
            Toast.makeText(this, " " + o, Toast.LENGTH_SHORT).show();
            Log.e(getClass().getSimpleName(), " " + o);
        }
    }


}
