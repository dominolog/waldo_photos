package pl.cubesoft.waldophotos.fragment;

import android.support.v4.app.Fragment;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by CUBESOFT on 02.12.2016.
 */
public class BaseFragment extends Fragment{

    final CompositeSubscription compositeSubscription = new CompositeSubscription();

    protected void subscribe (Subscription subscription) {
        compositeSubscription.add(subscription);
    }

    protected void unsubscribe (Subscription subscription) {
        compositeSubscription.remove(subscription);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        compositeSubscription.unsubscribe();
    }


    protected void showSnackBar(int content, int message) {
    }

}
