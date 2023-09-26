package org.hzt.swing_utils.builders;

import javax.swing.*;
import javax.swing.event.ChangeListener;

public final class JButtonBuilder {

    private final JButton button = new JButton();

    private JButtonBuilder() {
    }

    public static JButtonBuilder buildButton() {
        return new JButtonBuilder();
    }

    public JButtonBuilder withName(final String name) {
        button.setName(name);
        return this;
    }

    public JButtonBuilder withInitValue(final String text) {
        button.setText(text);
        return this;
    }

    public JButtonBuilder withToolTipText(final String toolTipText) {
        button.setToolTipText(toolTipText);
        return this;
    }

    public JButtonBuilder withSize(final int width, final int height) {
        button.setSize(width, height);
        return this;
    }

    public JButtonBuilder withListener(final ChangeListener changeListener) {
        button.addChangeListener(changeListener);
        return this;
    }

    public JButton build() {
        return button;
    }
}
