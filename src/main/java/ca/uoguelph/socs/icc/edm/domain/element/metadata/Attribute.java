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

import java.util.function.Function;
import java.util.function.BiConsumer;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <E> The type of the <code>Element</code> to which the
 *              <code>Attribute</code> is attached
 * @param   <V> The type of the <code>Attribute</code>
 */

public final class Attribute<E extends Element, V>
{
	/** The type of the <code>Attribute</code> */
	private final Class<V> type;

	/** The name of the <code>Attribute</code> */
	private final String name;

	/** Can the <code>Attribute</code> be changed? */
	private final boolean mutable;

	/** Is the <code>Attribute</code> allowed to be null? */
	private final boolean required;

	/** Method reference to fetch the value of the <code>Attribute</code> */
	private final Function<E, V> get;

	/** Method reference to set the value of the <code>Attribute</code> */
	private final BiConsumer<E, V> set;

	/**
	 * Create the <code>Attribute</code>.
	 *
	 * @param  <E>      The type of the <code>Element</code> to which the
	 *                  <code>Attribute</code> is attached
	 * @param  <V>      The type of the <code>Attribute</code>
	 * @param  name     The name of the <code>Attribute</code>
	 * @param  type     The type of the <code>Attribute</code>
	 * @param  required Indication if the <code>Attribute</code> is allowed to
	 *                  be null
	 * @param  mutable  Indication if the <code>Attribute</code> can be changed
	 * @param  get      Method reference to fetch the value of the
	 *                  <code>Attribute</code>
	 * @param  set      Method reference to set the value of the
	 *                  <code>Attribute</code>
	 */

	protected static <E extends Element, V> Attribute<E, V> newInstance (final String name, final Class<V> type, final boolean required, final boolean mutable, final Function<E, V> get, final BiConsumer<E, V> set)
	{
		return new Attribute<E, V> (name, type, required, mutable, get, set);
	}

	/**
	 * Create the <code>Attribute</code>.
	 *
	 * @param  <V>      The type of the <code>Attribute</code>
	 * @param  name     The name of the <code>Attribute</code>
	 * @param  type     The type of the <code>Attribute</code>
	 * @param  required Indication if the <code>Attribute</code> is allowed to
	 *                  be null
	 * @param  mutable  Indication if the <code>Attribute</code> can be changed
	 * @param  get      Method reference to fetch the value of the
	 *                  <code>Attribute</code>
	 * @param  set      Method reference to set the value of the
	 */

	private Attribute (final String name, final Class<V> type, final boolean required, final boolean mutable, final Function<E, V> get, final BiConsumer<E, V> set)
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

	/**
	 * Get the name of the <code>Attribute</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Attribute</code>
	 */

	public String getName ()
	{
		return this.name;
	}

	/**
	 * Get the type of the <code>Attribute</code>.
	 *
	 * @return The <code>Class</code> of the return type of the
	 *         <code>Attribute</code>
	 */

	public Class<V> getType ()
	{
		return this.type;
	}

	/**
	 * Determine if the value represented by the <code>Attribute</code> may be
	 * changed after the <code>Element</code> has been created.
	 *
	 * @return <code>true</code> if the value represented by the
	 *         <code>Attribute</code> may be changed, <code>false</code>
	 *         otherwise
	 */

	public boolean isMutable ()
	{
		return this.mutable;
	}

	/**
	 * Determine if the value represented by the <code>Attribute</code> is
	 * required.  If the value is not required, then it may be
	 * <code>null</code>.
	 *
	 * @return <code>true</code> if the value represented by the
	 *         <code>Attribute</code> is required, <code>false</code> otherwise
	 */

	public boolean isRequired ()
	{
		return this.required;
	}

	/**
	 * Get the value associated with the <code>Attribute</code> from the
	 * specified <code>Element</code>.
	 *
	 * @param  element The <code>Element</code> from which the value associated
	 *                 with the <code>Attribute</code> is to be retrieved, not
	 *                 null
	 *
	 * @return         The value associated with the <code>Attribute</code>
	 *                 contained in the <code>Element</code>
	 */

	public V getValue (final E element)
	{
		assert element != null : "element is NULL";

		return this.get.apply (element);
	}

	/**
	 * Set the value associated with the <code>Attribute</code> in the
	 * specified <code>Element</code>.
	 *
	 * @param  element The <code>Element</code> to which the value associated
	 *                 with the <code>Attribute</code> is to be written, not
	 *                 null
	 * @param  value   The value to write into the <code>Element</code>, not
	 *                 null if required is true
	 */

	public void setValue (final E element, final V value)
	{
		assert element != null : "element is NULL";
		assert ((this.required == false) ? value != null : true) : "value is NULL and is required";

		this.set.accept (element, value);
	}
}
