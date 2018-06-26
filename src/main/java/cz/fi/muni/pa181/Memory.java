package cz.fi.muni.pa181;

import cz.fi.muni.pa181.entity.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Singleton
public class Memory {

    private static final Logger LOG = LoggerFactory.getLogger(Memory.class);

    private Map<String, Currency> actualCurrency = new HashMap();
    private LocalDateTime lastUpdate = null;

    public void addCurrecy(Currency currency){
        actualCurrency.put(currency.getCode(), currency);
    }

    public Currency getCurrency(String CODE){
        return actualCurrency.get(CODE);
    }

    public Collection<Currency> getAllCurencies(){
        return actualCurrency.values();
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public LocalDateTime getDateForUpdate(){
        return lastUpdate;
    }

    public boolean wasTodayUpdated(LocalDateTime date){
        return (lastUpdate.getDayOfYear() == date.getDayOfYear());
    }

}
