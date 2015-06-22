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
import java.util.Set;

import java.util.HashMap;
import java.util.HashSet;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.ElementBuilder;

public final class DefinitionBuilder<T extends Element, U extends T>
{
	private final Class<T> type;
	private final Class<U> impl;

	private Class<? extends ElementBuilder<T>> builder;

	private Supplier<U> create;

	private final Map<Property<?>, Definition.Reference<U, ?>> refs;

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

		this.refs = new HashMap<Property<?>, Definition.Reference<U, ?>> ();
	}

	public void setCreateMethod (final Supplier<U> create)
	{
		assert create != null : "create is NULL";

		this.create = create;
	}

	public <B extends ElementBuilder<T>> void setBuilder (final Class<B> builder)
	{
		this.builder = builder;
	}

	public <V> void addAttribute (final String property, final Class<V> type, final boolean required, final boolean mutable, final Function<U, V> get, final BiConsumer<U, V> set)
	{
		assert property != null : "property is NULL";
		assert type != null : "type is NULL";
		assert get != null : "get is NULL";
		assert set != null : "set is NULL";

		Property<V> prop = new Property<V> (property, type, this.type, mutable, required);

		this.refs.put (prop, new Definition.Reference<U, V> (get, set));
	}

	public <V> void addUniqueAttribute (final String property, final Class<V> type, final boolean required, final boolean mutable, final Function<U, V> get, final BiConsumer<U, V> set)
	{
		assert property != null : "property is NULL";
		assert type != null : "type is NULL";
		assert get != null : "get is NULL";
		assert set != null : "set is NULL";

		this.addAttribute (property, type, required, mutable, get, set);
	}

	public <V> void addRelationship (final String name, final Class<V> type, final BiConsumer<U, V> get, final BiConsumer<U, V> set)
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

	public MetaData<T, U> build ()
	{
		Definition<T, U> defn = new Definition<T, U> (this.type, this.impl, this.create, this.refs);

		return new MetaData<T, U> (defn, this.builder);
	}
}
