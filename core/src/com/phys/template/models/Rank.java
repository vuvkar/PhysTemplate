package com.phys.template.models;

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
}

