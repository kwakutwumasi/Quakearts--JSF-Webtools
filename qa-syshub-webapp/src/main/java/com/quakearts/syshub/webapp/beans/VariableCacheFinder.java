/*******************************************************************************
 * Copyright (C) 2017 Kwaku Twumasi-Afriyie <kwaku.twumasi@quakearts.com>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Kwaku Twumasi-Afriyie <kwaku.twumasi@quakearts.com> - initial API and implementation
 ******************************************************************************/
package com.quakearts.syshub.webapp.beans;

import java.io.Serializable;
import java.util.Map;
import java.util.List;
import com.quakearts.webapp.orm.query.QueryOrder;
import com.quakearts.syshub.model.VariableCache;
import static com.quakearts.webapp.orm.query.helper.ParameterMapBuilder.createParameters;

public class VariableCacheFinder extends AbstractSysHubFinder {	
	public VariableCacheFinder(){
		super();
	}

	public List<VariableCache> findObjects(Map<String, Serializable> parameters,QueryOrder...queryOrders){
		return getDataStore().list(VariableCache.class, parameters, queryOrders);
	}
	public VariableCache getById(String id){
		return getDataStore().get(VariableCache.class,id);
	}
	public List<VariableCache> filterByText(String searchString){
		return getDataStore().list(VariableCache.class, createParameters()
					.addVariableString("appKey", searchString)
					.build());	}
}
