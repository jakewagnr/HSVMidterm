package com.algonquincollege.wagn0070.hsvmidterm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import model.HSVModel;

import static model.HSVModel.MAX_SV;
import static model.HSVModel.MIN_HUE;
import static model.HSVModel.MIN_SV;

/**
 *  Display colors based on Hue, Saturation and Value values
 *  @author Jacob Wagner (wagn0070@algonquinlive.com)
 */

public class MainActivity extends Activity implements Observer, SeekBar.OnSeekBarChangeListener {
    // About dialog
    private static final String ABOUT_DIALOG_TAG = "About";

    // class members
    private AboutDialogFragment mAboutDialog;
    private TextView mColorSwatch;
    private HSVModel mModel;
    private SeekBar mHueSB;
    private SeekBar mSaturationSB;
    private SeekBar mValueSB;
    private TextView mHueTV;
    private TextView mSaturationTV;
    private TextView mValueTV;
    private Integer mMax_SV = Math.round(MAX_SV * 100);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initialize about dialog
        mAboutDialog = new AboutDialogFragment();

        //initialize new model of color black
        mModel = new HSVModel(0, 0f, 0f);
        mModel.addObserver(this);

        //bind members to views
        mColorSwatch = (TextView) findViewById(R.id.colorSwatch);
        mHueSB = (SeekBar) findViewById(R.id.hueSB);
        mSaturationSB = (SeekBar) findViewById(R.id.saturationSB);
        mValueSB = (SeekBar) findViewById(R.id.valueSB);
        mHueTV = (TextView) findViewById(R.id.hue);
        mSaturationTV = (TextView) findViewById(R.id.saturation);
        mValueTV = (TextView) findViewById(R.id.value);

        //set slider maxes
        mHueSB.setMax(mModel.MAX_HUE);
        mSaturationSB.setMax(mMax_SV);
        mValueSB.setMax(mMax_SV);

        //listeners to watch for movement in the seekbars
        mHueSB.setOnSeekBarChangeListener(this);
        mSaturationSB.setOnSeekBarChangeListener(this);
        mValueSB.setOnSeekBarChangeListener(this);

        mColorSwatch.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                String displayValues = "H: " + mModel.getHue() + "\u00B0 S: " + mModel.getSaturation()*100 + "% V: " + mModel.getValue()*100 + "%";
                Toast.makeText(getApplicationContext(), displayValues, Toast.LENGTH_SHORT).show();
                return false;
            }

        });


        this.updateView();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mAboutDialog.show(getFragmentManager(), ABOUT_DIALOG_TAG);
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //add the about icon to the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Event handler for the <SeekBar>s: red, green, blue, and alpha.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser == false) {
            return;
        }

        //check which seekbar updated and adjust color swatch accordingly
        switch (seekBar.getId()) {
            case R.id.hueSB:
                mModel.setHue(mHueSB.getProgress());
                mHueTV.setText(getResources().getString(R.string.hueProgress, progress).toUpperCase());
                break;

            case R.id.saturationSB:
                mModel.setSaturation((float) mSaturationSB.getProgress() / 100);
                mSaturationTV.setText(getResources().getString(R.string.saturationProgress, progress).toUpperCase());
                break;

            case R.id.valueSB:
                mModel.setValue((float) mValueSB.getProgress() / 100);
                mValueTV.setText(getResources().getString(R.string.valueProgress, progress).toUpperCase());
                break;

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.hueSB:
                mHueTV.setText(getResources().getString(R.string.hue));
                break;
            case R.id.saturationSB:
                mSaturationTV.setText(getResources().getString(R.string.saturation));
                break;
            case R.id.valueSB:
                mValueTV.setText(getResources().getString(R.string.value));
                break;
        }
    }

    //update model to update color
    @Override
    public void update(Observable observable, Object data) {
        this.updateView();
    }

    private void updateColorSwatch() {
        float[] getHSV = {mModel.getHue(), mModel.getSaturation(), mModel.getValue()};
        mColorSwatch.setBackgroundColor(Color.HSVToColor(getHSV));
    }

    private void updateHueSB() {
        mHueSB.setProgress(Math.round(mModel.getHue()));
    }

    private void updateSaturationSB() {
        mSaturationSB.setProgress(Math.round(mModel.getSaturation() * 100));
    }

    private void updateValueSB() {
        mValueSB.setProgress(Math.round(mModel.getValue() * 100));
    }


    public void updateView() {
        this.updateColorSwatch();
        this.updateHueSB();
        this.updateSaturationSB();
        this.updateValueSB();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences settings = getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt("hue", Math.round(mModel.getHue()));
        editor.putInt("saturation", Math.round(mModel.getSaturation()));
        editor.putInt("value", Math.round(mModel.getValue()));

        editor.commit();
    }

    public void buttonToast(){
        String displayValues = "H: " + mModel.getHue() + "\u00B0 S: " + mModel.getSaturation()*100 + "% V: " + mModel.getValue()*100 + "%";
        Toast.makeText(getApplicationContext(), displayValues, Toast.LENGTH_SHORT).show();
    }

    public void asBlack(View view) {
        mModel.setHSV(MIN_HUE, MIN_SV, MIN_SV);
        buttonToast();
    }

    public void asRed(View view) {
        mModel.setHSV(MIN_HUE, MAX_SV, MAX_SV);
        buttonToast();
    }

    public void asLime(View view) {
        mModel.setHSV(120, MAX_SV, MAX_SV);
        buttonToast();
    }

    public void asBlue(View view) {
        mModel.setHSV(240, MAX_SV, MAX_SV);
        buttonToast();
    }

    public void asYellow(View view) {
        mModel.setHSV(60, MAX_SV, MAX_SV);
        buttonToast();
    }

    public void asCyan(View view) {
        mModel.setHSV(180, MAX_SV, MAX_SV);
        buttonToast();
    }

    public void asMagenta(View view) {
        mModel.setHSV(300, MAX_SV, MAX_SV);
        buttonToast();
    }

    public void asSilver(View view) {
        mModel.setHSV(MIN_HUE, MIN_SV, 0.75f);
        buttonToast();
    }

    public void asGray(View view) {
        mModel.setHSV(MIN_HUE, MIN_SV, .50f);
        buttonToast();
    }

    public void asMaroon(View view) {
        mModel.setHSV(MIN_HUE, MAX_SV, .50f);
        buttonToast();
    }

    public void asOlive(View view) {
        mModel.setHSV(60, MAX_SV, .50f);
        buttonToast();
    }

    public void asGreen(View view) {
        mModel.setHSV(120, MAX_SV, .50f);
        buttonToast();
    }

    public void asPurple(View view) {
        mModel.setHSV(300, MAX_SV, .50f);
        buttonToast();
    }

    public void asTeal(View view) {
        mModel.setHSV(180, MAX_SV, .50f);
        buttonToast();
    }

    public void asNavy(View view) {
        mModel.setHSV(240, MAX_SV, .50f);
        buttonToast();
    }

}