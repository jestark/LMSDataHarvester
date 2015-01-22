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

package ca.uoguelph.socs.icc.edm.domain.factory;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementManager;

public final class MappedManagerFactory<T extends Element, X extends ElementManager<T>> extends AbstractMappedFactory<Class<? extends X>, X, DomainModel>
{
	/** The logger */
	private final Log log;

	/** The type of the <code>ElemementManager</code> which is being returned */
	private final Class<T> type;

	/** <code>Element<code> to <code>ElementManager</code> implementation mapping */
	private final Map<Class<? extends T>, Class<? extends X>> elementmanagers;
	
	public MappedManagerFactory (Class<T> type)
	{
		super ();

		this.log = LogFactory.getLog (MappedManagerFactory.class);

		if (type == null)
		{
			this.log.error ("Type is NULL");
			throw new NullPointerException ("Type is NULL");
		}

		this.type = type;

		this.elementmanagers = new HashMap<Class<? extends T>, Class<? extends X>> ();
	}

	public void registerElement (Class<? extends T> impl, Class<? extends X> manager)
	{
		if (impl == null)
		{
			this.log.error ("Element is NULL");
			throw new NullPointerException ("Element is NULL");
		}

		if (manager == null)
		{
			this.log.error ("Manager is NULL");
			throw new NullPointerException ("Manager is NULL");
		}

		this.elementmanagers.put (impl, manager);
	}

	@Override
	public Set<Class<? extends T>> getRegisteredElements ()
	{
		return this.elementmanagers.keySet ();
	}

	@Override
	public DataStoreQuery<T> create (DomainModel model)
	{
		if (model == null)
		{
			this.log.error ("DomainModel is null");
			throw new NullPointerException ("DomainModel is null");
		}

		Class<? extends X> manager = this.elementmanagers.get ((model.getProfile ()).getImplClass (this.type));

		if (manager == null)
		{
			String message = "Manager not registered for element: " + (model.getProfile ()).getImplClass (this.type);

			this.log.error (message);
			throw new IllegalStateException (message);
		}

		if (! this.isRegistered (manager))
		{
			String message = "Manager not registered: " + manager;

			this.log.error (message);
			throw new IllegalStateException (message);
		}

		return this.create (manager, model);
	}
}
