/*******************************************************************************
* Copyright (C) 2018 Kwaku Twumasi-Afriyie <kwaku.twumasi@quakearts.com>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Kwaku Twumasi-Afriyie <kwaku.twumasi@quakearts.com> - initial API and implementation
 ******************************************************************************/
package com.quakearts.webapp.orm;

import com.quakearts.webapp.orm.exception.DataStoreException;

@FunctionalInterface
public interface DataStoreConnection {
	<Connection> Connection getConnection(Class<Connection> expectedConnection) throws DataStoreException;
}