package com.phys.template.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;

public enum Rank {
    SHN, KRTST, ST, AVST, AV, ENT, AVENT, LT, AVLT, KN, MR, PGT, GT, GNMR, GNLT, GNGP;

    @Override
    public String toString() {
        switch (this) {
            case SHN:
                return "Շարքային";
            case KRTST:
                return "Կրտսեր սերժանտ";
            case ST:
                return "Սերժանտ";
            case AVST:
                return "Ավագ սերժանտ";
            case AV:
                return "Ավագ";
            case ENT:
                return "Ենթասպա";
            case AVENT:
                return "Ավագ ենթասպա";
            case LT:
                return "Լեյտենանտ";
            case AVLT:
                return "Ավագ լեյտենանտ";
            case KN:
                return "Կապիտան";
            case MR:
                return "Մայոր";
            case PGT:
                return "Փոխգնդապետ";
            case GT:
                return "Գնդապետ";
            case GNMR:
                return "Գեներալ մայոր";
            case GNLT:
                return "Գեներալ լեյտենանտ";
            case GNGP:
                return "Գեներալ գնդապետ";
        }
        return super.toString();
    }
    
    public String shortName () {
        switch (this) {
            case SHN:
                return "շ-ն";
            case KRTST:
                return "կրտ. ս-տ";
            case ST:
                return "ս-տ";
            case AVST:
                return "ավ. ս-տ";
            case AV:
                return "ավ.";
            case ENT:
                return "ենթ.";
            case AVENT:
                return "ավ. ենթ.";
            case LT:
                return "լ-տ";
            case AVLT:
                return "ավ. լ-տ";
            case KN:
                return "կ-ն";
            case MR:
                return "մ-ր";
            case PGT:
                return "փ/գ-տ";
            case GT:
                return "գ-տ";
            case GNMR:
                return "գեն. մ-ր";
            case GNLT:
                return "գեն. լ-տ";
            case GNGP:
                return "գեն. գ-տ";
        }
        throw new GdxRuntimeException("Can't keep my hand to myself");
    }
}

