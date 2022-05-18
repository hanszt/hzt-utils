package org.hzt.swing_utils.builders;

import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

public final class JSliderBuilder {

    private final JSlider slider = new JSlider();

    private JSliderBuilder() {
    }

    public static JSliderBuilder buildSlider() {
        return new JSliderBuilder();
    }

    public JSliderBuilder withName(String name) {
        slider.setName(name);
        return this;
    }

    public JSliderBuilder withInitValue(int initValue) {
        slider.setValue(initValue);
        return this;
    }

    public JSliderBuilder withMinimum(int minValue) {
        slider.setMinimum(minValue);
        return this;
    }

    public JSliderBuilder withMaximum(int maxValue) {
        slider.setMinimum(maxValue);
        return this;
    }

    public JSliderBuilder withListener(ChangeListener changeListener) {
        slider.addChangeListener(changeListener);
        return this;
    }

    public JSliderBuilder withPaintLabels(boolean labels) {
        slider.setPaintLabels(labels);
        return this;
    }

    public JSliderBuilder withPaintTicks(boolean paintTicks) {
        slider.setPaintTicks(paintTicks);
        return this;
    }

    public JSlider build() {
        return slider;
    }
}
