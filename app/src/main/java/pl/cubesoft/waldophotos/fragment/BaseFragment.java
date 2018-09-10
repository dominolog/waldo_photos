package pl.cubesoft.waldophotos.fragment;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import pl.cubesoft.waldophotos.activity.BaseActivity;
import pl.cubesoft.waldophotos.app.WaldoApplication;

/**
 * Created by CUBESOFT on 02.12.2016.
 */
public class BaseFragment extends Fragment{

    private final CompositeDisposable disposables = new CompositeDisposable();


    protected void subscribe (Disposable disposable) {
        disposables.add(disposable);
    }

    protected void unsubscribe (Disposable disposable) {
        disposables.remove(disposable);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        disposables.clear();
    }

    protected BaseActivity getMyActivity() {
        return (BaseActivity) getActivity();
    }

    protected WaldoApplication getMyApplication() {
        return (WaldoApplication) getActivity().getApplication();
    }


    protected void showSnackBar(int content, int message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

}
