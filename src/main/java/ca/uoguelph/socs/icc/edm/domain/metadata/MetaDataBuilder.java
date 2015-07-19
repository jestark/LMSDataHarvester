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

public final class MetaDataBuilder<T extends Element>
{
	/** The Logger */
	private final Logger log;

	/** The <code>Element</code> type */
	private final Class<T> type;

	/** The <code>MetaData</code> for the parent <code>Element</code> */
	private final MetaData<? super T> parent;

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

	public MetaDataBuilder (final Class<T> type, final MetaData<? super T> parent)
	{
		assert type != null : "type is NULL";

		this.log = LoggerFactory.getLogger (MetaDataBuilder.class);

		this.type = type;
		this.parent = parent;

		this.properties = new HashMap<String, Property<?>> ();
		this.selectors = new HashMap<String, Selector> ();
		this.references = new HashMap<Property<?>, PropertyReference<T, ?>> ();
	}

	/**
	 * Get the Java type of the <code>Element</code> interface.
	 *
	 * @return The <code>Class</code> representing the interface type
	 */

	public Class<T> getElementType ()
	{
		return this.type;
	}

	/**
	 * Set the method references for getting and setting the value associated
	 * with the specified <code>Property</code>.  All of the
	 * <code>Property</code> instances associated with the <code>Element</code>
	 * must have references attached to them in the implementation, however the
	 * <code>set</code> reference is allowed to be <code>null</code> which will
	 * result in a read-only <code>Property</code> for the <code>Element</code>
	 * implementation.  The specified <code>Property</code> instance must
	 * already exist in the <code>Definition</code> for the
	 * <code>Element</code>.
	 *
	 * @param  <V>      The <code>Element</code> type of the property
	 * @param  property The <code>Property</code>, not null
	 * @param  get      Method reference to get the value, not null
	 * @param  set      Method reference to set the value, may be null
	 */

	public <V> Property<V> addProperty (final Class<V> type, final Function<T, V> get, final BiConsumer<T, V> set, final String name, final boolean mutable, final boolean required)
	{
		this.log.trace ("addProperty: type={}, get={}, set={}, name={}, mutable={}, required={}", type, get, set, name, mutable, required);

		assert type != null : "type is NULL";
		assert get != null : "get is NULL";
		assert name != null : "name is NULL";
		assert name.length () > 0 : "name can not be empty";
		assert ! this.properties.containsKey (name) : "Property already exists";

		Property<V> property = new Property<V> (name, type, this.type, mutable, required);

		this.properties.put (name, property);
		this.references.put (property, new PropertyReference<T, V> (get, set));

		return property;
	}

	public <V> Property<V> addProperty (final Class<V> type, final Function<T, V> get, final String name, final boolean mutable, final boolean required)
	{
		this.log.trace ("addProperty: type={}, get={}, name={}, mutable={}, required={}", type, get, name, mutable, required);

		return this.addProperty (type, get, null, name, mutable, required);
	}

	/**
	 * Create the <code>Selector</code> using multiple <code>Property</code>
	 * instances.
	 *
	 * @param  name                     The name of the <code>Selector</code>,
	 *                                  not null
	 * @param  unique                   An indication if the
	 *                                  <code>Selector</code> uniquely
	 *                                  identifies an <code>Element</code>
	 *                                  instance
	 * @param  properties               The properties to be used to create the
	 *                                  <code>Selector</code>, not null
	 *
	 * @return                          The <code>Selector</code>
	 * @throws IllegalArgumentException if a different <code>Selector</code>
	 *                                  already exists in the definition with
	 *                                  the same name
	 */

	public Selector addSelector (final String name, final boolean unique, final Property<?>... properties)
	{
		if (name == null)
		{
			throw new NullPointerException ();
		}

		if (name.length () == 0)
		{
			throw new IllegalArgumentException ("name is an empty String");
		}

		Set<Property<?>> props = new HashSet<Property<?>> ();

		for (Property<?> property : properties)
		{
			if (this.type != property.getElementType ())
			{
				throw new IllegalArgumentException ("Type mismatch, property does not match selector");
			}

			props.add (property);
		}

		this.selectors.put (name, new Selector (this.type, name, unique, props));

		return this.selectors.get (name);
	}

	/**
	 * Create the <code>Selector</code> using a single <code>Property</code>.
	 *
	 * @param  property                 The property to be represented by the
	 *                                  <code>Selector</code>, not null
	 * @param  unique                   An indication if the
	 *                                  <code>Selector</code> uniquely
	 *                                  identifies an <code>Element</code>
	 *                                  instance
	 *
	 * @return                          The <code>Selector</code>
	 * @throws IllegalArgumentException if a different <code>Selector</code>
	 *                                  already exists in the definition with
	 *                                  the same name
	 */

	public Selector addSelector (final Property<?> property, final boolean unique)
	{
		assert property != null : "property is NULL";
		assert this.type == property.getElementType () : "Type mismatch, property does not match selector";

		return this.addSelector (property.getName (), unique, property);
	}

	/**
	 * Build the <code>MetaData</code> for the <code>Element</code>
	 * implementation class.
	 *
	 * @return The <code>MetaData</code>
	 */

	public MetaData<T> build ()
	{
		this.log.trace ("build:");

		MetaData<T> metadata = new MetaData<T> (this.type, this.parent, this.properties, this.references, this.selectors);
		DataStore.registerMetaData (metadata);

		return metadata;
	}
}
