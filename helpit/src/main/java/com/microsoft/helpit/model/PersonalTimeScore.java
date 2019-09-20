package com.microsoft.helpit.model;

public class PersonalTimeScore {
    private long x; //time
    private double y; //score
    private int count;

    public PersonalTimeScore(long x, int count, double y) {
        this.x = x;
        this.count = count;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
