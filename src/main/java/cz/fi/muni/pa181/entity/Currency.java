package cz.fi.muni.pa181.entity;

import java.util.Objects;

public class Currency {
    private String name;
    private String code;
    private double rate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Currency)) return false;
        Currency currency = (Currency) o;
        return Objects.equals(getName(), currency.getName()) &&
                Objects.equals(getCode(), currency.getCode());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getCode());
    }
}
