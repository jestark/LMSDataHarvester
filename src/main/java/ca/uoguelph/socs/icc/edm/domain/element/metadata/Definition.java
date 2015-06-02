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

package ca.uoguelph.socs.icc.edm.domain.element.metadata;

import java.util.Map;

import java.util.function.Supplier;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T>
 * @param   <U>
 */

public final class Definition<T extends Element, U extends T>
{
	private final Class<T> type;
	private final Class<U> impl;

	private final Supplier<U> create;

	private final Map<Property<T>, Attribute<U, ?>> attributes;
//	private final Map indexes;

	protected <T extends Element, U extends T> Definition<T, U> newInstance (final Class<T> type, final Class<U> impl, final Supplier<U> create, final Map<Property<T>, Attribute<U, ?>> attributes)
	{
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		assert create != null : "create is NULL";

		return new Definition<T, U> (type, impl, create, attributes);
	}

	protected Definition (final Class<T> type, final Class<U> impl, final Supplier<U> create, final Map<Property<T>, Attribute<U, ?>> attributes)
	{
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		assert create != null : "create is NULL";
		assert attributes != null : "attributes is NULL";

		this.type = type;
		this.impl = impl;
		this.create = create;
		this.attributes = attributes;
	}

	/**
	 *
	 */

	public Class<T> getInterfaceClass ()
	{
		return this.type;
	}

	public Class<U> getImplementationClass ()
	{
		return this.impl;
	}

	public U create ()
	{
		return this.create.get ();
	}
}
