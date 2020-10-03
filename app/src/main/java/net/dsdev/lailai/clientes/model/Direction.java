package net.dsdev.lailai.clientes.model;

import com.google.gson.annotations.SerializedName;

public class Direction {

    @SerializedName("name")
    private String name;

    @SerializedName("direction")
    private String direction;

    @SerializedName("icon")
    private String icon;

    public Direction() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
