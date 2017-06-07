package org.ieszaidinvergeles.android.webizv.appinterface.user.notificationdetail;

// WIzv

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ieszaidinvergeles.android.webizv.R;
import org.ieszaidinvergeles.android.webizv.model.pojo.Notificacion;

import static org.ieszaidinvergeles.android.webizv.model.data.Constants.PARAMETER_NOTIFICACION;

public class NotificationDetailFragment extends Fragment implements NotificationDetailContract.View {

    private NotificationDetailContract.UserActionsListener listener;
    private TextView tvDate;
    private TextView tvDetail;
    private TextView tvTitle;

    @Override
    public void hideDate() {
        tvDate.setVisibility(View.GONE);
    }

    @Override
    public void hideMessage() {
        tvDetail.setVisibility(View.GONE);
    }

    @Override
    public void hideTitle() {
        tvTitle.setVisibility(View.GONE);
    }

    public static NotificationDetailFragment newInstance(Notificacion notificacion) {
        final Bundle arguments = new Bundle();
        arguments.putParcelable(PARAMETER_NOTIFICACION, notificacion);
        final NotificationDetailFragment fragment = new NotificationDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listener = new NotificationDetailPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.notification_detail_fragment, container, false);
        tvTitle = (TextView) root.findViewById(R.id.notification_title);
        tvDetail = (TextView) root.findViewById(R.id.notification_message);
        tvDate = (TextView) root.findViewById(R.id.notification_date);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Notificacion notificacion = getArguments().getParcelable(PARAMETER_NOTIFICACION);
        listener.openNotification(notificacion);
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if (active) {
            tvTitle.setText("");
            tvDetail.setText(getString(R.string.loading));
        }
    }

    @Override
    public void showDate(String date) {
        tvDate.setVisibility(View.VISIBLE);
        tvDate.setText(date);
    }

    @Override
    public void showMessage(String message) {
        tvDetail.setVisibility(View.VISIBLE);
        tvDetail.setText(message);
    }

    @Override
    public void showMissingNotification() {
        tvTitle.setText("");
        tvDetail.setText(getString(R.string.no_data));
    }

    @Override
    public void showTitle(String title) {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
    }

}