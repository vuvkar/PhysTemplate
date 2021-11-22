package com.phys.template.models;

public enum Category {
    FIRST, SECOND, THIRD;

    @Override
    public String toString() {
        switch (this) {
            case FIRST:
                return "Առաջին";
            case SECOND:
                return "Երկրորդ";
            case THIRD:
                return "Երրորդ";
        }
        return super.toString();
    }
}

