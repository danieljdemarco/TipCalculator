package com.wholeheartgarden.tipcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CalculatorActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {
    private final String TAG = CalculatorActivity.class.getSimpleName();

    // UI components
    private EditText mBillSubtotal;
    private EditText mTipPercent;
    private EditText mTipAmount;
    private Button mRoundDown;
    private Button mRoundUp;

    // Keeps track of whether the tip amount or tip percent was last edited. When another field
    // such as the Bill Subtotal is changed, it will update the tip field that was not last
    // edited, assuming that the user wants to keep the more recently edited field
    private int mLastEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        // initialize UI components
        mBillSubtotal = (EditText) findViewById(R.id.edit_text_bill_subtotal);
        mTipPercent = (EditText) findViewById(R.id.edit_text_tip_percent);
        mTipAmount = (EditText) findViewById(R.id.edit_text_tip_amount);
        mRoundDown = (Button) findViewById(R.id.button_round_down);
        mRoundUp = (Button) findViewById(R.id.button_round_up);

        // set listeners
        mBillSubtotal.setOnFocusChangeListener(this);
        mTipPercent.setOnFocusChangeListener(this);
        mTipAmount.setOnFocusChangeListener(this);
        mRoundDown.setOnClickListener(this);
        mRoundUp.setOnClickListener(this);

    }

    // calculates the tip percent based on current tip amount
    private float calculateTipPercent() {
        float billSubtotal = Float.parseFloat(mBillSubtotal.getText().toString());
        float tipAmount = Float.parseFloat(mTipAmount.getText().toString());
        return (tipAmount / billSubtotal) * 100;
    }

    // calculates the tip amount based on current tip percent
    private float calculateTipAmount() {
        float billSubtotal = Float.parseFloat(mBillSubtotal.getText().toString());
        float tipPercent = Float.parseFloat(mTipPercent.getText().toString()) / 100;
        return billSubtotal * tipPercent;
    }

    // whenever the user changes a field, attempt to update the other fields
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) { // look for the field that is losing focus
            switch (view.getId()) {
                case R.id.edit_text_bill_subtotal:
                    Toast.makeText(this, "Bill Subtotal", Toast.LENGTH_SHORT).show();
                    if (mBillSubtotal.getText().length() > 0) {
                        if (mTipPercent.getText().length() > 0 && mTipAmount.getText().length() > 0) Log.d(TAG, getApplicationContext().getResources().getResourceName(mLastEdited));
                        if (mLastEdited == R.id.edit_text_tip_percent) {
                            mTipAmount.setText(String.valueOf(calculateTipAmount()));
                        } else if (mLastEdited == R.id.edit_text_tip_amount) {
                            mTipPercent.setText(String.valueOf(calculateTipPercent()));
                        }
                    }
                    break;
                case R.id.edit_text_tip_percent:
                    Toast.makeText(this, "Tip Percent", Toast.LENGTH_SHORT).show();
                    if (mBillSubtotal.getText().length() > 0 && mTipPercent.getText().length() > 0) {
                        mTipAmount.setText(String.valueOf(calculateTipAmount()));
                        mLastEdited = R.id.edit_text_tip_percent;
                    }
                    break;
                case R.id.edit_text_tip_amount:
                    Toast.makeText(this, "Tip Amount", Toast.LENGTH_SHORT).show();
                    if (mBillSubtotal.getText().length() > 0 && mTipAmount.getText().length() > 0) {
                        mTipPercent.setText(String.valueOf(calculateTipPercent()));
                        mLastEdited = R.id.edit_text_tip_amount;
                    }
                    break;
                default:
                    Log.e(TAG, "Unexpected Operation on View " + view.toString());
            }
        }
    }

    // handle click events to round the tip amount up or down to the nearest integer, and update
    // tip percent
    @Override
    public void onClick(View view) {
        double oldTipAmount;
        double newTipAmount;
        switch (view.getId()) {
            case R.id.button_round_down:
                Toast.makeText(this, "Round Down", Toast.LENGTH_SHORT).show();
                oldTipAmount = Double.parseDouble(mTipAmount.getText().toString());
                newTipAmount = Math.floor(oldTipAmount);
                mTipAmount.setText(String.valueOf(newTipAmount));
                mTipPercent.setText(String.valueOf(calculateTipPercent()));
                mLastEdited = R.id.edit_text_tip_amount;
                break;
            case R.id.button_round_up:
                Toast.makeText(this, "Round Up", Toast.LENGTH_SHORT).show();
                oldTipAmount = Double.parseDouble(mTipAmount.getText().toString());
                newTipAmount = Math.ceil(oldTipAmount);
                mTipAmount.setText(String.valueOf(newTipAmount));
                mTipPercent.setText(String.valueOf(calculateTipPercent()));
                mLastEdited = R.id.edit_text_tip_amount;
                break;
            default:
                Log.e(TAG, "Unexpected Operation on View " + view.toString());
        }
    }
}
