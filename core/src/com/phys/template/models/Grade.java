package com.phys.template.models;

public enum Grade {
    EXCELLENT, GOOD, OK, BAD;

    public int getNumericalGrade() {
        switch (this) {
            case EXCELLENT:
                return 5;
            case GOOD:
                return 4;
            case OK:
                return 3;
            case BAD:
                return 2;
        }

        return 0;
    }

    public String getDescription (boolean sendShort, boolean inBraces) {
        String description = "";

        switch (this) {
            case EXCELLENT:
                description = sendShort ? "գեր." : "Գերազանց";
                break;
            case GOOD:
                description = "լավ";
                break;
            case OK:
                description = sendShort ? "բավ." : "բավարար";
                break;
            case BAD:
                description = sendShort ? "անբավ." : "անբավարար";
                break;
        }

        if (inBraces) {
            description = "(" + description + ")";
        }

        return description;
    }

    public static Grade gradeTypeForGrade (int grade) {
        switch (grade) {
            case 2:
                return BAD;
            case 3:
                return OK;
            case 4:
                return GOOD;
            case 5:
                return EXCELLENT;
        }

        return null;
    }

    @Override
    public String toString() {
        return getNumericalGrade() + " " + getDescription(true, true);
    }

}
