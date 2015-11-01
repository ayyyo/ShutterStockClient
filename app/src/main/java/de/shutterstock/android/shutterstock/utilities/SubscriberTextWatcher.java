package de.shutterstock.android.shutterstock.utilities;


import android.text.Editable;
import android.widget.TextView;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by emanuele on 25.10.15.
 */
public final class SubscriberTextWatcher implements Observable.OnSubscribe<CharSequence> {

    private static class InternalTextWatcher implements android.text.TextWatcher {

        private Subscriber<? super CharSequence> mSubscriber;

        public void setSubscriber(final Subscriber<? super CharSequence> subscriber) {
            mSubscriber = subscriber;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mSubscriber != null) {
                mSubscriber.onNext(s);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    private final TextView mTextView;
    private final InternalTextWatcher mTextWatcher = new InternalTextWatcher();

    public SubscriberTextWatcher(TextView textView) {
        if (textView == null) {
            throw new IllegalArgumentException();
        }
        mTextView = textView;
    }

    @Override
    public void call(Subscriber<? super CharSequence> subscriber) {
        RxJavaUtils.checkUiThread();
        mTextWatcher.setSubscriber(subscriber);
        mTextView.addTextChangedListener(mTextWatcher);
        subscriber.add(new Subscription() {

            private volatile boolean isUnsubscribed = false;

            @Override
            public void unsubscribe() {
                if (mTextView != null) {
                    mTextView.removeTextChangedListener(mTextWatcher);
                }
                isUnsubscribed = true;
            }

            @Override
            public boolean isUnsubscribed() {
                return isUnsubscribed;
            }
        });
        subscriber.onNext(mTextView.getText());
    }
}
