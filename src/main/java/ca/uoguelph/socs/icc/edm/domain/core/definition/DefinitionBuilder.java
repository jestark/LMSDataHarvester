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
import java.util.HashSet;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import ca.uoguelph.socs.icc.edm.domain.Element;

public final class DefinitionBuilder<T extends Element, U extends T>
{
	private final Class<T> type;
	private final Class<U> impl;

	private Supplier<U> create;

//	private final Set<AttributeDefinition<U, ?>> attributes;

	public static <T extends Element, U extends T> DefinitionBuilder<T, U> newInstance (final Class<T> type, final Class<U> impl)
	{
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";

		return new DefinitionBuilder<T, U> (type, impl);
	}

	private DefinitionBuilder (final Class<T> type, final Class<U> impl)
	{
		assert type != null : "type is NULL";
		assert impl != null : "impl is NULL";

		this.type = type;
		this.impl = impl;
	}

	public void setCreateMethod (final Supplier<U> create)
	{
		assert create != null : "create is NULL";

		this.create = create;
	}

	public <A extends Element, V> void addAttribute (final String name, final Class<V> type, final boolean required, final boolean mutable, final Function<A, V> get, final BiConsumer<A, V> set)
	{
		assert name != null : "name is NULL";
		assert name.length () > 0 : "name is empty";
		assert type != null : "type is NULL";
		assert get != null : "get is NULL";
		assert set != null : "set is NULL";

	}

	public <A extends Element, V> void addUniqueAttribute (final String name, final Class<V> type, final boolean required, final boolean mutable, final Function<A, V> get, final BiConsumer<A, V> set)
	{
		assert name != null : "name is NULL";
		assert name.length () > 0 : "name is empty";
		assert type != null : "type is NULL";
		assert get != null : "get is NULL";
		assert set != null : "set is NULL";


	}

	public <A extends Element, V> void addRelationship (final String name, final Class<V> type, final BiConsumer<A, V> get, final BiConsumer<A, V> set)
	{
		assert name != null : "name is NULL";
		assert name.length () > 0 : "name is empty";
		assert type != null : "type is NULL";
		assert get != null : "get is NULL";
		assert set != null : "set is NULL";


	}

	public void addIndex (final String... attributes)
	{

	}

	public ElementDefinition<T, U> build ()
	{
		return null;
	}
}
