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

package ca.uoguelph.socs.icc.edm.domain.manager;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.ElementManager;
import ca.uoguelph.socs.icc.edm.domain.factory.ConcreteFactory;

/**
 * Create an <code>ElementManager</code> for the specified
 * <code>DomainModel</code>.  Implementations of this interface are intended to
 * be used with the <code>AbstractManagerFactory</code> to create a specific
 * <code>ElementManager</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The type of manager returned by the factory
 * @see     ca.uoguelph.socs.icc.edm.domain.AbstractManagerFactory
 */

public interface ManagerFactory<T> extends ConcreteFactory<T, DomainModel>
{
	/**
	 * Create the <code>ElementManager</code>.
	 *
	 * @param  model The <code>DomainModel</code> to be associated with the
	 *               <code>ElementManager</code>
	 * @return       The <code>ElementManager</code>
	 */

	@Override
	public abstract T create (DomainModel model);
}
