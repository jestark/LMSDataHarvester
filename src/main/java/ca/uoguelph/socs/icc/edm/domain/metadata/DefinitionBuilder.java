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

package ca.uoguelph.socs.icc.edm.domain.metadata;

import java.util.Map;
import java.util.Set;

import java.util.HashMap;
import java.util.HashSet;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * <code>MetaData</code> builder for <code>Element</code> implementation
 * classes.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> type
 */

public final class DefinitionBuilder<T extends Element>
{
	/** The Logger */
	private final Logger log;

	/** The <code>Element</code> type */
	private final Class<T> type;

	/** The <code>MetaData</code> definition for the parent <code>Element</code> */
	private final Definition<? super T> parent;

	/** Set of all of the associated <code>Property</code> instances */
	private final Set<Property<?>> allprops;

	/** The <code>Set</code> of <code>Property</code> instances */
	private final Map<String, Property<?>> properties;

	/** The <code>Set</code> of <code>Selector</code> instances */
	private final Map<String, Selector> selectors;

	/** <code>Property</code> to <code>PropertyReference</code> mapping */
	private final Map<Property<?>, PropertyReference<T, ?>> references;

	/**
	 * Create the <code>MetaDataBuilder</code>.
	 *
	 * @param  type The <code>Element</code> interface class, not null
	 */

	protected DefinitionBuilder (final Class<T> type, final Definition<? super T> parent)
	{
		assert type != null : "type is NULL";

		this.log = LoggerFactory.getLogger (DefinitionBuilder.class);

		this.type = type;
		this.parent = parent;

		this.properties = new HashMap<String, Property<?>> ();
		this.selectors = new HashMap<String, Selector> ();
		this.references = new HashMap<Property<?>, PropertyReference<T, ?>> ();

		this.allprops = (this.parent != null) ? this.parent.getProperties () : new HashSet<Property<?>> ();
	}

	/**
	 * Set the method references for getting and setting the value associated
	 * with the specified <code>Property</code>.  All of the
	 * <code>Property</code> instances associated with the <code>Element</code>
	 * must have references attached to them in the implementation, however the
	 * <code>set</code> reference is allowed to be <code>null</code> which will
	 * result in a read-only <code>Property</code> for the <code>Element</code>
	 * implementation.
	 *
	 * @param  <V>      The <code>Element</code> type of the property
	 * @param  property The <code>Property</code>, not null
	 * @param  get      Method reference to get the value, not null
	 * @param  set      Method reference to set the value, may be null
	 *
	 * @return          This <code>DefinitionBuilder</code>
	 */

	public <V> DefinitionBuilder<T> addProperty (final Property<V> property, final Function<T, V> get, final BiConsumer<T, V> set)
	{
		this.log.trace ("addProperty: property={}, get={}, set={}", property, get, set);

		assert property != null : "property is NULL";
		assert get != null : "get is NULL";
		assert ! this.properties.containsKey (property.getName ()) : "property is already registered";

		this.allprops.add (property);
		this.properties.put (property.getName (), property);
		this.references.put (property, new PropertyReference<T, V> (get, set));

		return this;
	}

	/**
	 * Set the method references for getting and setting the value associated
	 * with the specified <code>Property</code>.  All of the
	 * <code>Property</code> instances associated with the <code>Element</code>
	 * must have references attached to them in the implementation.  This
	 * method creates read-only references.
	 *
	 * @param  <V>      The <code>Element</code> type of the property
	 * @param  property The <code>Property</code>, not null
	 * @param  get      Method reference to get the value, not null
	 *
	 * @return          This <code>DefinitionBuilder</code>
	 */

	public <V> DefinitionBuilder<T> addProperty (final Property<V> property, final Function<T, V> get)
	{
		this.log.trace ("addProperty: property={} get={}", property, get);

		return this.addProperty (property, get, null);
	}

	/**
	 * Create the <code>Selector</code> using a single <code>Property</code>.
	 *
	 * @param  selector The <code>Selector</code>, not null
	 *
	 * @return          This <code>DefinitionBuilder</code>
	 */

	public DefinitionBuilder<T> addSelector (final Selector selector)
	{
		this.log.trace ("addSelector: selector={}", selector);

		assert selector != null : "selector is NULL";
		assert selector.getElementType () == this.type : "Type mismatch, property does not match selector";
		assert ! this.selectors.containsKey (selector.getName ()) : "selector is already registered";
		assert this.allprops.containsAll (selector.getProperties ()) : "Properties in selector missing from definition";

		this.selectors.put (selector.getName (), selector);

		return this;
	}

	/**
	 * Build the <code>MetaData</code> for the <code>Element</code>
	 * implementation class.
	 *
	 * @return The <code>MetaData</code>
	 */

	public Definition<T> build ()
	{
		this.log.trace ("build:");

		return new Definition<T> (this.type, this.parent, this.properties, this.references, this.selectors);
	}
}
