package org.hzt.swing_utils.builders;

import javax.swing.JButton;
import javax.swing.event.ChangeListener;

public final class JButtonBuilder {

    private final JButton button = new JButton();

    private JButtonBuilder() {
    }

    public static JButtonBuilder buildButton() {
        return new JButtonBuilder();
    }

    public JButtonBuilder withName(String name) {
        button.setName(name);
        return this;
    }

    public JButtonBuilder withInitValue(String text) {
        button.setText(text);
        return this;
    }

    public JButtonBuilder withToolTipText(String toolTipText) {
        button.setToolTipText(toolTipText);
        return this;
    }

    public JButtonBuilder withSize(int width, int height) {
        button.setSize(width, height);
        return this;
    }

    public JButtonBuilder withListener(ChangeListener changeListener) {
        button.addChangeListener(changeListener);
        return this;
    }

    public JButton build() {
        return button;
    }
}
