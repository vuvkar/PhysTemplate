package com.phys.template.models;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class OverallResultModel implements Json.Serializable {

    private int overallPeople;
    private int participated;
    private float participatedP;
    private int super_;
    private float super_P;
    private int good;
    private float goodP;
    private int average;
    private float averageP;
    private int bad;
    private float badP;
    private int goodAndAbove;
    private float goodAndAboveP;
    private int disability;
    private float disabilityP;

    private int overallGrade;

    @Override
    public void write(Json json) {

    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }
}
