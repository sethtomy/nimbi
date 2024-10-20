package com.sethtomy.nimbi.diablo2.domain;

public enum Act {
    I("Act I"),
    II("Act II"),
    III("Act III"),
    IV("Act IV"),
    V("Act V");

    private final String text;

    Act(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
