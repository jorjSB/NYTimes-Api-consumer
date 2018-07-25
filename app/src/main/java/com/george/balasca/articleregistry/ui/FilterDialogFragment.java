package com.george.balasca.articleregistry.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.george.balasca.articleregistry.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    @BindView(R.id.begin_edittext) EditText getBegin_edittext;
    @BindView(R.id.end_edittext) EditText getEnd_edittext;
    @BindView(R.id.order_spinner) Spinner order_spinner;
    @BindView(R.id.category_spinner) Spinner category_spinner;
    @BindView(R.id.submit_filters_button) Button submit_filters_button;

    OnFiltersSetListener mCallback;

    /**
     * An interface containing onFiltersSet() method signature.
     * Container Activity must implement this interface.
     */
    public interface OnFiltersSetListener {
        void onFiltersSet(String beginDate, String endDate, String order, String category);
    }

    public FilterDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static FilterDialogFragment newInstance() {
        FilterDialogFragment frag = new FilterDialogFragment();
        return frag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check the interface is implemented in parent activity
        try {
            mCallback = (OnFiltersSetListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnFiltersSetListener.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_filter_dialog, container, false);
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_filter_dialog, null);
        ButterKnife.bind(this, view);

        mCallback = (OnFiltersSetListener) getActivity();


        String dialogTitle = getResources().getString(R.string.filter_dialog_title);
        getDialog().setTitle(dialogTitle);

        getBegin_edittext.setOnClickListener(this::onClick);
        getEnd_edittext.setOnClickListener(this::onClick);

        submit_filters_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set values for callback
                if(mCallback != null)
                {
                    mCallback.onFiltersSet(
                            getBegin_edittext.getText().toString(),
                            getEnd_edittext.getText().toString(),
                            order_spinner.getSelectedItem().toString(),
                            category_spinner.getSelectedItem().toString());
                }

                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        DialogFragment newPicker = new DatePickerFragment();
        switch(v.getId()) {
            case R.id.begin_edittext:
                newPicker.show(getActivity().getSupportFragmentManager(), "datePicker");
                newPicker.setTargetFragment(this, 1);
                return;

            case R.id.end_edittext:
                newPicker.show(getActivity().getSupportFragmentManager(), "datePicker");
                newPicker.setTargetFragment(this, 2);
                return;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String date = data.getStringExtra("key_date");;
        if (requestCode == 1) {
            getBegin_edittext.setText(date);
        }else if (requestCode == 2){
            getEnd_edittext.setText(date);
        }
    }
}
