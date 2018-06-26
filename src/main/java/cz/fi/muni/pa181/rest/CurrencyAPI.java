package cz.fi.muni.pa181.rest;

import cz.fi.muni.pa181.entity.Convert;
import cz.fi.muni.pa181.entity.Currency;
import cz.fi.muni.pa181.Memory;
import cz.fi.muni.pa181.RestUtils;
import cz.fi.muni.pa181.store.ConvertStoreCloudant;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Path("/currency")
public class CurrencyAPI {

    private static final String BASE_URL_CURRENCIES = "https://openexchangerates.org/api/currencies.json?app_id=";
    private static final String BASE_URL_TABLE = "https://openexchangerates.org/api/latest.json?app_id=";

    private static final String API_KEY = "c08bf018afc14a59a686e78345db928a";
    private static final Logger LOG = LoggerFactory.getLogger(CurrencyAPI.class);

    @Inject
    ConvertStoreCloudant store;

    @Inject
    private Memory memory;

    @GET
    @Path("/available")
    @Produces({"application/json"})
    public Collection<Currency> getCurrency() throws Exception {
       if(memory.getDateForUpdate()==null || !memory.wasTodayUpdated(LocalDateTime.now())) { //memory is null or not updated today
           this.parseDataToMemory();
       }
       return memory.getAllCurencies();
    }

    @GET
    @Path("/rate/{from}/{to}")
    @Produces({"application/json"})
    public double getLastHistory(@PathParam("from") String codeFrom,
                                 @PathParam("to") String codeTo){
        return exchangeRate(codeFrom, codeTo);
    }

    @POST
    @Path("/convert")
    @Produces("application/json")
    @Consumes("application/json")
    public Convert convert(Convert convert) {
        convert.setAmountOut( convert.getAmountIn() * exchangeRate(convert.getCurrencyIn(), convert.getCurrencyOut()) );
        convert.setDate(LocalDateTime.now());
        store.persist(convert);
        return convert;
    }

    /**
     * Add data from CURRENCIES rest to memory
     */
    private void parseDataToMemory() throws Exception {
        LOG.info("----------------Memory is empty-------------------");
        JSONObject availableCurrencies = new JSONObject(RestUtils.getHTML(BASE_URL_CURRENCIES + API_KEY));
        availableCurrencies.toMap().forEach((k, v) -> {
            System.out.println("Currency code : " + k + " name : " + v);
            Currency currency = new Currency();
            currency.setCode(k);
            currency.setName(v.toString());
            memory.addCurrecy(currency);
        });
        JSONObject todayRatesTable = new JSONObject(RestUtils.getHTML(BASE_URL_TABLE + API_KEY));
        todayRatesTable.getJSONObject("rates").toMap().forEach((k, v) -> {
            System.out.println("Currency code : " + k + " rate " + v);
            Currency updateCurrency = memory.getCurrency(k);
            updateCurrency.setRate(Double.valueOf(v.toString()));
        });
        memory.setLastUpdate(LocalDateTime.now());
    }

    private double exchangeRate(String from, String to){
        Currency inCurrency = memory.getCurrency(from);
        Currency outCurrency = memory.getCurrency(to);
        if(inCurrency==null || outCurrency==null){
            return -1;
        }
        return outCurrency.getRate()/inCurrency.getRate();
    }
}
