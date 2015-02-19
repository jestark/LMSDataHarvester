/* Copyright (C) 2014, 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStoreQuery;

/**
 * Low-level interface for creating instances of <code>IdGenerator</code>
 * implementations.  Implementations of this interface are used by the
 * <code>IdGeneratorFactory</code> to create and initialize an
 * <code>IdGenerator</code> implementation.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     IdGeneratorFactory
 */

public interface IdGeneratorImplFactory
{
	/**
	 * Create the <code>IdGenerator</code> using the specified
	 * <code>DataStoreQuery</code>.  Implementations of this method will use the
	 * provided <code>DataStoreQuery</code> to initialize the
	 * <code>IdGenerator</code>.
	 *
	 * @param  query The <code>DataStoreQuery</code>, not null
	 *
	 * @return The <code>IdGenerator</code> instance
	 */

	public abstract IdGenerator create (DataStoreQuery<?> query);
}
