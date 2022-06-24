package com.edgelordzeta.zetainventorymanager;

public class item {
    private long mId;
    private String upc;
    private int itemCount;
    private String description;
    public item() {}

    public String getUPC() {
        return upc;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public void setUPC(String text) {
        this.upc = text;
    }

    public String getDescription() {
        return description;
    }

public void setCount(int count) {
        itemCount = count;
}
    public int getCount() {
        return itemCount;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }
}
