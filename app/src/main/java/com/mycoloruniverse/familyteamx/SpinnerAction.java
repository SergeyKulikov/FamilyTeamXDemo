package com.mycoloruniverse.familyteamx;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Sergey on 05.03.2018.
 */

public class SpinnerAction {
    Spinner spinner;
    ArrayAdapter<String> adapter_type;

    public SpinnerAction(String prompt, String[] items, Spinner spinnerView) {

        this.spinner = spinnerView;
        /*
        adapter_type = new ArrayAdapter<String>(spinnerView.getContext(),
                android.R.layout.simple_spinner_item,
                items
        );
        this.spinner = spinnerView;

        // adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        */
        adapter_type = new ArrayAdapter<String>(spinnerView.getContext(),
                R.layout.spinner_item,
                items
        );
        adapter_type.setDropDownViewResource(R.layout.spinner_dropdown_item);

        this.spinner.setAdapter(adapter_type);
        this.spinner.setPrompt(prompt);
        this.spinner.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

        /*
        this.spinner.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(R.drawable.ic_target_white_24),
                null, null, null);
        */

        /*
        switch (currentTask.getType()) {
            case TYPE_FREE_CONTENT:
                tvTaskName.setCompoundDrawablesWithIntrinsicBounds(
                        getResources().getDrawable(R.drawable.ic_target_white_24),
                        null, null, null
                );
                break;
            case TYPE_MARKET:
                tvTaskName.setCompoundDrawablesWithIntrinsicBounds(
                        getResources().getDrawable(R.drawable.ic_market_white_24),
                        null, null, null
                );
                break;
            case TYPE_UTILITIES:
                tvTaskName.setCompoundDrawablesWithIntrinsicBounds(
                        getResources().getDrawable(R.drawable.ic_home_utilities_white_24),
                        null, null, null
                );
                break;
        }
        */
    }

    public Spinner fill() {
        return this.spinner;
    }

    public ArrayAdapter<String> getAdapter() {
        return adapter_type;
    }

    public int findItemByName(String name) {
        return adapter_type.getPosition(name);
    }

}
