package com.phys.template.models;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class Metadata implements Json.Serializable {

    private String baseName = "";
    private String squadName =  "";


    @Override
    public void write(Json json) {
        json.writeValue("baseName", baseName);
        json.writeValue("squadName", squadName);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        baseName = jsonData.getString("baseName");
        squadName = jsonData.getString("squadName");
    }

    public void setBaseNumber(String baseName) {
        this.baseName = baseName;
    }

    public void setSquadName(String squadName) {
        this.squadName = squadName;
    }

    public String getBaseName () {
        return baseName;
    }

    public String getSquadName() {
        return squadName;
    }

}
