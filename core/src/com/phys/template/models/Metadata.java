package com.phys.template.models;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class Metadata implements Json.Serializable {

    private String baseName;
    private String squadName;
    private String SHPName;
    private String CheckerName;

    @Override
    public void write(Json json) {

    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }
}
