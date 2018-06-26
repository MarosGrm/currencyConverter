package cz.fi.muni.pa181.store;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.gson.JsonObject;
import cz.fi.muni.pa181.entity.Convert;

import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Singleton
public class ConvertStoreCloudant implements ConvertStore {

    private Database db = null;
    private static final String databaseName = "mydb";

    public ConvertStoreCloudant(){
        CloudantClient cloudant = createClient();
        if(cloudant!=null){
            db = cloudant.database(databaseName, true);
        }
    }

    private static CloudantClient createClient() {

        String url;

        if (System.getenv("VCAP_SERVICES") != null) {
            // When running in IBM Cloud, the VCAP_SERVICES env var will have the credentials for all bound/connected services
            // Parse the VCAP JSON structure looking for cloudant.
            JsonObject cloudantCredentials = VCAPHelper.getCloudCredentials("cloudant");
            if(cloudantCredentials == null){
                System.out.println("No cloudant database service bound to this application");
                return null;
            }
            url = cloudantCredentials.get("url").getAsString();
        } else {
            System.out.println("Running locally. Looking for credentials in cloudant.properties");
            url = VCAPHelper.getLocalProperties("cloudant.properties").getProperty("cloudant_url");
            if(url == null || url.length()==0){
                System.out.println("To use a database, set the Cloudant url in src/main/resources/cloudant.properties");
                return null;
            }
        }

        try {
            System.out.println("Connecting to Cloudant");
            CloudantClient client = ClientBuilder.url(new URL(url)).build();
            return client;
        } catch (Exception e) {
            System.out.println("Unable to connect to database");
            //e.printStackTrace();
            return null;
        }
    }

    @Override
    public Database getDB() {
        return db;
    }

    @Override
    public Collection<Convert> getAll() {
        return getAllLimit(-1);
    }

    @Override
    public Collection<Convert> getAllLimit(int limit) {
        List<Convert> docs;
        try {
            docs = db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse().getDocsAs(Convert.class);
            Collections.sort(docs, new Comparator<Convert>() {
                @Override
                public int compare(Convert lhs, Convert rhs) {
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    return lhs.getDate().isAfter(rhs.getDate()) ? -1 : (lhs.getDate().isBefore(rhs.getDate())) ? 1 : 0;
                }
            });
        } catch (IOException e) {
            return null;
        }
        if(limit<0||limit>docs.size()){
            return docs;
        }else{
            return docs.subList(0, limit);
        }
    }

    @Override
    public Convert get(String id) {
        return db.find(Convert.class, id);
    }

    @Override
    public Convert persist(Convert convert) {
        String id = db.save(convert).getId();
        return db.find(Convert.class, id);
    }

    @Override
    public Convert update(String id, Convert convert) {
        Convert convertUpdate = db.find(Convert.class, id);
        convertUpdate.setAmountIn(convert.getAmountIn());
        convertUpdate.setAmountOut(convert.getAmountOut());
        convertUpdate.setCurrencyIn(convert.getCurrencyIn());
        convertUpdate.setCurrencyOut(convert.getCurrencyOut());
        convertUpdate.setDate(convert.getDate());
        db.update(convertUpdate);
        return db.find(Convert.class, id);
    }

    @Override
    public void delete(String id) {
        Convert convert = db.find(Convert.class, id);
        db.remove(id, convert.get_rev());
    }

    @Override
    public int count(){
        return getAll().size();
    }
}
