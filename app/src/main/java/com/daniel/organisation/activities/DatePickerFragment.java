package com.daniel.organisation.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.daniel.organisation.R;
import android.content.DialogInterface;
import java.util.Calendar;

/**
 * @Author daniexpo
 * Esta clase va a manejar el DatePicker de las tareas
 */
public class DatePickerFragment extends DialogFragment{
    //Creo un objeto listener
    private DatePickerDialog.OnDateSetListener listener;

    /**
     * Crea un nuevo fragmento y le establece el listener.
     * @param listener
     * @return
     */
    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_OrganisationBlue);
        fragment.setListener(listener);
        return fragment;
    }

    /**
     * Set Listener
     * @param listener
     */
    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    /**
     * Esto es lo que va a mostrar el DatePicker cuando se abra
     * y le va a pasar el Listener a la activity
     * @param savedInstanceState
     * @return devuelve el objeto Dialog del DatePicker
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

}
