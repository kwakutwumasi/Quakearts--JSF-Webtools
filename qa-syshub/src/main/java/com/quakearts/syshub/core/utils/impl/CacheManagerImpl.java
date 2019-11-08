/*******************************************************************************
* Copyright (C) 2016 Kwaku Twumasi-Afriyie <kwaku.twumasi@quakearts.com>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Kwaku Twumasi-Afriyie <kwaku.twumasi@quakearts.com> - initial API and implementation
 ******************************************************************************/
package com.quakearts.syshub.core.utils.impl;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.SingleFileStoreConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.eviction.EvictionType;
import org.infinispan.manager.CacheContainer;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import com.quakearts.syshub.core.utils.CacheManager;

@Singleton
public class CacheManagerImpl implements CacheManager {
	private static EmbeddedCacheManager cache_container;

	private static synchronized CacheContainer getCacheContainer() {
		if (cache_container == null) {

			cache_container = new DefaultCacheManager(new ConfigurationBuilder()
					// Eviction configuration
					.eviction().strategy(EvictionStrategy.LIRS).type(EvictionType.COUNT).size(1000)
					// Expiration...is this neccessary?
					.expiration().lifespan(3, TimeUnit.DAYS).reaperEnabled(true).wakeUpInterval(1, TimeUnit.DAYS)
					// Save items to file. Good for managing high loads
					.persistence().passivation(true).addStore(SingleFileStoreConfigurationBuilder.class).async()
					.preload(true).location("syshub_log_cache").build());

			cache_container.start();
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				if (cache_container != null) {
					cache_container.stop();
				}
			}));

		}
		return cache_container;
	}

	/* (non-Javadoc)
	 * @see com.quakearts.syshub.core.utils.CacheManager#getCache(java.lang.Class, java.lang.String)
	 */
	@Override
	public <T> Cache<String, T> getCache(Class<T> cacheType, String suffix) {
		return getCacheContainer().getCache(cacheType.getName() + (suffix != null ? suffix : ""));
	}

}