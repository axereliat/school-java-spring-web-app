package com.school.common.enumerations;

public enum MarkType {
    PRACTICAL, WRITING, SPEAKING;

    @Override
    public String toString() {
        String word = this.toString();
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }
}
