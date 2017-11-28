package model;

import java.util.Observable;


public class HSVModel extends Observable {

    // set maxes
    public static final Integer MAX_HUE = 359;
    public static final Float MAX_SV = 1f;
    public static final Integer MIN_HUE = 0;
    public static final Float MIN_SV = 0f;

    // initalize value holders
    private Integer hue;
    private Float saturation;
    private Float value;

    public HSVModel() {
        this(MAX_HUE, MAX_SV, MAX_SV);
    }

    public HSVModel(Integer hue, Float saturation, Float value) {
        super();

        this.hue = hue;
        this.saturation = saturation;
        this.value = value;
    }


    // getters
    public Integer getHue() {
        return hue;
    }

    public Float getSaturation() {
        return saturation;
    }

    public Float getValue() {
        return value;
    }


    // setters
    public void setHue(Integer hue) {
        this.hue = hue;

        this.updateObservers();
    }

    public void setSaturation(Float saturation) {
        this.saturation = saturation;

        this.updateObservers();
    }

    public void setValue(Float value) {
        this.value = value;

        this.updateObservers();
    }

    // set all at once
    public void setHSV(Integer hue, float saturation, float value) {
        this.hue = hue;
        this.saturation = saturation;
        this.value = value;

        this.updateObservers();
    }

    //propagate update to observers
    private void updateObservers() {
        this.setChanged();
        this.notifyObservers();
    }

}