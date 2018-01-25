package com.labelwall.mywall.util.icon;

import com.joanzapata.iconify.Icon;

/**
 * Created by Administrator on 2018-01-03.
 */

public enum  EcIcons implements Icon {
    icon_scan('\ue602'),
    icon_ali_pay('\ue606');

    private char character;

    EcIcons(char character) {
        this.character = character;
    }

    @Override
    public String key() {
        return name().replace('_', '-');
    }

    @Override
    public char character() {
        return character;
    }
}
