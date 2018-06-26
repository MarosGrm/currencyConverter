package cz.fi.muni.pa181.store;

import com.cloudant.client.api.Database;
import cz.fi.muni.pa181.entity.Convert;

import java.util.Collection;

public interface ConvertStore {
    public Database getDB();

    public Collection<Convert> getAll();

    public Collection<Convert> getAllLimit(int limit);

    public Convert get(String id);

    public Convert persist(Convert convert);

    public Convert update(String id, Convert convert);

    public void delete(String id);

    public int count() throws Exception;
}
