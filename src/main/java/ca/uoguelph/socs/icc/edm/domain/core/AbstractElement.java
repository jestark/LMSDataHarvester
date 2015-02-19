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

package ca.uoguelph.socs.icc.edm.domain.core;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;

import ca.uoguelph.socs.icc.edm.domain.builder.ElementFactory;

import ca.uoguelph.socs.icc.edm.domain.factory.QueryFactory;

/**
 * Abstract base class for all of the domain model <code>Element</code>
 * implementations.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class AbstractElement implements Element
{
	/**
	 * Register an <code>Element</code> implementation with the factories.
	 *
	 * @param  type    The <code>Element</code> interface class, not null
	 * @param  impl    The <code>Element</code> implementation class, not null
	 * @param  builder The <code>ElementBuilder</code> implementation, not null
	 * @param  factory The <code>ElementFactory</code>, instance not null
	 */

	protected static <T extends Element, X extends T> void registerElement (Class<T> type, Class<X> impl, Class<? extends ElementBuilder<T>> builder, ElementFactory<T> factory)
	{
		(QueryFactory.getInstance (type)).registerClass (impl);
	}
}
