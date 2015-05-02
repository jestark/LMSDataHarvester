/* Copyright (C) 2015 James E. Stark
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.uoguelph.socs.icc.edm.domain.datastore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class AbstractDataStore implements DataStore
{
	/** The logger */
	protected final Logger log;

	/** The profile */
	protected final DataStoreProfile profile;

	protected AbstractDataStore (final DataStoreProfile profile)
	{
		this.log = LoggerFactory.getLogger (JPADataStore.class);
		this.profile = profile;
	}

	/**
	 * Get the profile data for the <code>DataStore</code>.
	 *
	 * @return A copy of the profile data
	 */

	@Override
	public DataStoreProfile getProfile ()
	{
		return this.profile;
	}

	@Override
	public void close ()
	{
		AbstractDataStoreQuery.closeAll (this);
	}

	/**
	 * Get the relevant <code>DataStoreQuery</code> for the provided interface
	 * and implementation classes.
	 *
	 * @param <T>  The interface type of the <code>DataStoreQuery</code>
	 * @param <U>  The implementation type of the <code>DataStoreQuery</code>
	 * @param type Interface class, not null
	 * @param impl Implementation class, not null
	 *
	 * @return     The <code>DataStoreQuery</code> for the specified interface
	 *             and implementation
	 */

	protected abstract <T extends Element, U extends T> AbstractDataStoreQuery<T, U> createQuery (final Class<T> type, final Class<U> impl);
}
