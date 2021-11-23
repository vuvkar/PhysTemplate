package com.phys.template.models;

public enum Category {
    FIRST, SECOND, THIRD;

    public String getDescription() {
        switch (this) {
            case FIRST:
                return "Հատուկ նշանակության, զորային հետախուզության \n և դեսանտագրհային ստորաբաժանումների զինծառայողներ";
            case SECOND:
                return "Մոտոհրաձգային, հրաձգային, և լեռնահրաձգային ստորաբաժանումների զինծառայողներ";
            case THIRD:
                return "Վերը չնշված զորատեսակների, հատուկ զորքերի և ապահովման ստորաբաժանումների զինծառայողներ";
        }

        return null;
    }

    public String getShortName() {
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

    @Override
    public String toString() {
        return getShortName();
    }
}

