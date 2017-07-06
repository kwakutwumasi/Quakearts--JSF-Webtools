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
import com.quakearts.syshub.model.AgentConfiguration;
import static com.quakearts.webapp.orm.query.helper.ParameterMapBuilder.createParameters;

public class AgentConfigurationFinder extends AbstractSysHubFinder {	

	public List<AgentConfiguration> findObjects(Map<String, Serializable> parameters,QueryOrder...queryOrders){
		return getDataStore().list(AgentConfiguration.class, parameters, queryOrders);
	}
	public AgentConfiguration getById(int id){
		return getDataStore().get(AgentConfiguration.class,id);
	}
	public List<AgentConfiguration> filterByText(String searchString){
		return getDataStore().list(AgentConfiguration.class, createParameters()
					.addVariableString("agentName", searchString)
					.build());	
	}
	public List<AgentConfiguration> findByName(String searchString){
		return getDataStore().list(AgentConfiguration.class, createParameters()
					.add("agentName", searchString)
					.build());
	}
}
