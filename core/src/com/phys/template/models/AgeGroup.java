package com.phys.template.models;

import com.badlogic.gdx.utils.GdxRuntimeException;

public enum AgeGroup {
    FIRST, SECOND, THIRD, FOURTH, FIFTH, SIXTH, SEVENTH, EIGHTH, NINTH, TENTH, ELEVENTH, THIRTEENTH, FOURTEENTH, SIXTEENTH, SEVENTEENTH, EIGHTEENTH;
    
    public String getShortName () {
        return getDescription().substring(0, 25) + "...";
    }
    
    public String getDescription() {
        switch (this) {
            case FIRST:
                return "Մինչև 6 ամիս ծառայած պարտադիր ժամկետային զինծառայողներ";
            case SECOND:
                return "Պարտադիր ժամկետային զինծառայողներ";
            case THIRD:
                return "ՌՈՒՀ-ի 1-ին կուրսի կուսանտներ, դիմորդներ";
            case FOURTH:
                return "ՌՈՒՀ-ի 2-րդ կուրսի կուրսանտներ";
            case FIFTH:
                return "ՌՈՒՀ-ի 3-րդ և բարձր կուրսերի կուրսանտներ";
            case SIXTH:
                return "1-ին տարիքային խմբի սպաներ, ենթասպաներ, պայմանագրային զինծառայողներ (մինչև 25)";
            case SEVENTH:
                return "2-րդ տարիքային խմբի սպաներ, ենթասպաներ, պայմանագրային զինծառայողներ (25-29)";
            case EIGHTH:
                return "3-րդ տարիքային խմբի սպաներ, ենթասպաներ, պայմանագրային զինծառայողներ (30-34)";
            case NINTH:
                return "4-րդ տարիքային խմբի սպաներ, ենթասպաներ, պայմանագրային զինծառայողներ (35-39)";
            case TENTH:
                return "5-րդ տարիքային խմբի սպաներ, ենթասպաներ, պայմանագրային զինծառայողներ (40-44)";
            case ELEVENTH:
                return "6-րդ տարիքային խմբի սպաներ, ենթասպաներ, պայմանագրային զինծառայողներ (45-49)";
            case THIRTEENTH:
                return "7-րդ տարիքային խմբի սպաներ, ենթասպաներ, պայմանագրային զինծառայողներ (50-54)";
            case FOURTEENTH:
                return "1-ին տարիքային խմբի կին զինծառայողներ (մինչև 25), 2-րդ կուրսի կուրսանտներ";
            case SIXTEENTH:
                return "2-րդ տարիքային խմբի կին զինծառայողներ (25-29), 1-ին կուրսի կուրսանտներ, դիմորդներ";
            case SEVENTEENTH:
                return "3-րդ տարիքային խմբի կին զինծառայողներ (30-34), կուրսանտներ";
            case EIGHTEENTH:
                return "4-րդ տարիքային խմբի կին զինծառայողներ (35-39)";
        }

        throw new GdxRuntimeException("I AM A BAAAD GUY< DAH!");
    }

    @Override
    public String toString() {
        return getShortName();
    }
}
