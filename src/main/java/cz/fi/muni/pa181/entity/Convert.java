package cz.fi.muni.pa181.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Convert {
    private String _id;
    private String _rev;
    private String currencyIn;
    private String currencyOut;
    private double amountIn;
    private double amountOut;
    private LocalDateTime date;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_rev() {
        return _rev;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    public String getCurrencyIn() {
        return currencyIn;
    }

    public void setCurrencyIn(String currencyIn) {
        this.currencyIn = currencyIn;
    }

    public String getCurrencyOut() {
        return currencyOut;
    }

    public void setCurrencyOut(String currencyOut) {
        this.currencyOut = currencyOut;
    }

    public double getAmountIn() {
        return amountIn;
    }

    public void setAmountIn(double amountIn) {
        this.amountIn = amountIn;
    }

    public double getAmountOut() {
        return amountOut;
    }

    public void setAmountOut(double amountOut) {
        this.amountOut = amountOut;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Convert)) return false;
        Convert convert = (Convert) o;
        return Objects.equals(getDate(), convert.getDate());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getDate());
    }
}
