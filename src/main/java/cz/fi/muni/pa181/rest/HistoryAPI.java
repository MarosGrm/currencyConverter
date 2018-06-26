package cz.fi.muni.pa181.rest;

import cz.fi.muni.pa181.entity.Convert;
import cz.fi.muni.pa181.store.ConvertStoreCloudant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.Collection;

@Path("/history")
public class HistoryAPI {

    private static final Logger LOG = LoggerFactory.getLogger(HistoryAPI.class);

    @Inject
    ConvertStoreCloudant store;

    @GET
    @Path("/all")
    @Produces({"application/json"})
    public Collection<Convert> getAllHistory(){
        return store.getAll();
    }

    @GET
    @Path("/last/{limit}")
    @Produces({"application/json"})
    public Collection<Convert> getLastHistory(@PathParam("limit") int limit){
        return store.getAllLimit(limit);
    }
}
