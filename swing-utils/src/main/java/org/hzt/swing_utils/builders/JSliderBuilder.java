package org.hzt.swing_utils.builders;

import javax.swing.*;
import javax.swing.event.ChangeListener;

public final class JSliderBuilder {

    private final JSlider slider = new JSlider();

    private JSliderBuilder() {
    }

    public static JSliderBuilder buildSlider() {
        return new JSliderBuilder();
    }

    public JSliderBuilder withName(final String name) {
        slider.setName(name);
        return this;
    }

    public JSliderBuilder withInitValue(final int initValue) {
        slider.setValue(initValue);
        return this;
    }

    public JSliderBuilder withMinimum(final int minValue) {
        slider.setMinimum(minValue);
        return this;
    }

    public JSliderBuilder withMaximum(final int maxValue) {
        slider.setMaximum(maxValue);
        return this;
    }

    public JSliderBuilder withListener(final ChangeListener changeListener) {
        slider.addChangeListener(changeListener);
        return this;
    }

    public JSliderBuilder withPaintLabels(final boolean labels) {
        slider.setPaintLabels(labels);
        return this;
    }

    public JSliderBuilder withPaintTicks(final boolean paintTicks) {
        slider.setPaintTicks(paintTicks);
        return this;
    }

    public JSlider build() {
        return slider;
    }
}
