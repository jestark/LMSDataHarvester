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

package ca.uoguelph.socs.icc.edm.domain.core.definition;

import java.util.Set;

import java.util.function.Supplier;

import ca.uoguelph.socs.icc.edm.domain.Element;

public final class ElementDefinition<T extends Element, U extends T>
{
	private final Class<T> type;
	private final Class<U> impl;

	private final Supplier<U> create;

//	private final Set<AttributeDefinition<U, ?>> attributes;

	protected ElementDefinition (final Class<T> type, final Class<U> impl, final Supplier<U> create)
	{
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";
		assert create != null : "create is NULL";

		this.type = type;
		this.impl = impl;
		this.create = create;
	}

	protected void addAttribute (final AttributeDefinition<U, ?> attribute)
	{

	}

	protected void addUniqueAttribute (final AttributeDefinition<U, ?> attribute)
	{

	}

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
