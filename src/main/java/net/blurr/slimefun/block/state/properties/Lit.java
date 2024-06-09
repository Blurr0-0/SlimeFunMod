package net.blurr.slimefun.block.state.properties;

import net.minecraft.util.StringRepresentable;

public enum Lit implements StringRepresentable {
    OFF("off"),
    FIRE("fire"),
    SOULFIRE("soulfire");

    private final String name;

    private Lit(String pName) {
        this.name = pName;
    }

    public String toString() {
        return this.getSerializedName();
    }
    @Override
    public String getSerializedName() {
        return this.name;
    }
    public boolean isLit() {
        return this != OFF;
    }
}
