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

import java.util.function.Function;
import java.util.function.BiConsumer;

import ca.uoguelph.socs.icc.edm.domain.Element;

public final class AttributeDefinition<E extends Element, T>
{
	private final Class<T> type;

	private final String name;

	private final boolean mutable;

	private final boolean required;

	private final Function<E, T> get;

	private final BiConsumer<E, T> set;

	protected static <E extends Element, T> AttributeDefinition<E, T> newInstance (final String name, final Class<T> type, final boolean required, final boolean mutable, final Function<E, T> get, final BiConsumer<E, T> set)
	{
		return new AttributeDefinition<E, T> (name, type, required, mutable, get, set);
	}

	private AttributeDefinition (final String name, final Class<T> type, final boolean required, final boolean mutable, final Function<E, T> get, final BiConsumer<E, T> set)
	{
		assert name != null : "name is NULL";
		assert type != null : "type is NULL";
		assert get != null : "get Method is NULL";
		assert set != null : "set Method is NULL";

		this.name = name;
		this.type = type;
		this.required = required;
		this.mutable = mutable;
		this.get = get;
		this.set = set;
	}

	public String getName ()
	{
		return this.name;
	}

	public Class<T> getType ()
	{
		return this.type;
	}

	public boolean isMutable ()
	{
		return this.mutable;
	}

	public boolean isRequired ()
	{
		return this.required;
	}

	public T getValue (final E element)
	{
		assert element != null : "element is NULL";

		return this.get.apply (element);
	}

	public void setValue (final E element, final T value)
	{
		assert element != null : "element is NULL";

		this.set.accept (element, value);
	}
}
