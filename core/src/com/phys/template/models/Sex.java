package com.phys.template.models;

public enum Sex {
    MALE, FEMALE;

    @Override
    public String toString() {
        switch (this) {
            case MALE:
                return "Արական";
            case FEMALE:
                return "Իգական";
        }
        return super.toString();
    }
}
