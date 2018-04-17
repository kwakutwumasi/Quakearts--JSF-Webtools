package com.quakearts.webapp.orm.cdi;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.quakearts.webapp.orm.DataStore;
import com.quakearts.webapp.orm.DataStoreFactory;
import com.quakearts.webapp.orm.DataStoreFunction;
import com.quakearts.webapp.orm.exception.DataStoreException;
import com.quakearts.webapp.orm.query.QueryOrder;

/**A lazy initialized {@linkplain DataStore}. This is to enable CDI injection of the resource
 * Note: the DataStore instance is not thread safe, and must be acauired and disposed of 
 * within the contex of a single thread. Re use may result in unwanted errors.
 * @author kwakutwumasi-afriyie
 *
 */
public class CDIDataStore implements DataStore {

	private DataStore wrappedDataStore;
	
	public DataStore getWrappedDataStore() {
		if(wrappedDataStore == null)
			wrappedDataStore = DataStoreFactory.getInstance().getDataStore();
		
		return wrappedDataStore;
	}
	
	public void save(Object object) throws DataStoreException {
		getWrappedDataStore().save(object);
	}

	public <Entity> Entity get(Class<Entity> clazz, Serializable id) throws DataStoreException {
		return getWrappedDataStore().get(clazz, id);
	}

	public void update(Object object) throws DataStoreException {
		getWrappedDataStore().update(object);
	}

	public void delete(Object object) throws DataStoreException {
		getWrappedDataStore().delete(object);
	}

	public <Entity> List<Entity> list(Class<Entity> clazz, Map<String, Serializable> parameters, QueryOrder... orders)
			throws DataStoreException {
		return getWrappedDataStore().list(clazz, parameters, orders);
	}

	public void saveOrUpdate(Object object) throws DataStoreException {
		getWrappedDataStore().saveOrUpdate(object);
	}

	public <Entity> Entity refresh(Entity object) throws DataStoreException {
		return getWrappedDataStore().refresh(object);
	}

	public void flushBuffers() throws DataStoreException {
		getWrappedDataStore().flushBuffers();
	}

	public void executeFunction(DataStoreFunction function) throws DataStoreException {
		getWrappedDataStore().executeFunction(function);
	}

	public String getConfigurationProperty(String propertyName) {
		return getWrappedDataStore().getConfigurationProperty(propertyName);
	}

}