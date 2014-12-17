/* Copyright (C) 2014 James E. Stark
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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.datastore.DataStoreQuery;
import ca.uoguelph.socs.icc.edm.datastore.IdGenerator;

public abstract class AbstractManagerFactory<T extends Element, X extends ElementManager<T>>
{
	private X manager;

	private Log log;

	protected AbstractManagerFactory ()
	{
		this.manager = null;

		this.log = LogFactory.getLog (AbstractManagerFactory.class);
	}

	public final void registerBuilder (Class<?> impl, BuilderFactory<T> factory)
	{
	}

	public final void registerQuery (Class<?> impl, QueryFactory<T> factory)
	{
	}

	public final Set<Class<?>> getRegisteredBuilders ()
	{
		return null;
	}

	public final Set<Class<?>> getRegisteredQueries ()
	{
		return null;
	}

	protected final DataStoreQuery<T> createQuery (DomainModel model)
	{
		return null;
	}

	protected final ElementBuilder<T> createBuilder (DomainModel model)
	{
		return null;
	}

	protected abstract X needsaname (DomainModel model);

	protected final X createManager (DomainModel model)
	{
		if (this.manager == null)
		{
			this.manager = needsaname (model);
		}

		return this.manager;
	}

	public final X getManager (DomainModel model)
	{
		return this.createManager (model);
	}
}
