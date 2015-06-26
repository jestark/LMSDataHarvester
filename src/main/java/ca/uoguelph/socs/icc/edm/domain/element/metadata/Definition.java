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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Meta-data definition for an <code>Element</code> interface.  This class
 * collects all of the <code>Property</code> and <code>Selector</code>
 * instances which are associated with an <code>Element</code> interface.  When
 * The <code>Property</code> or <code>Selector</code> is defined it is
 * automatically registered with the appropriate instance of this class.
 * <p>
 * Since the <code>Element</code> interfaces are defined hierarchically with
 * all of the <code>Element</code> definitions inheriting <code>Element</code>,
 * this class processes the definitions recursively.  When a
 * <code>Property</code> or <code>Selector</code> is added or retrieved, this
 * class will look at the instances associated it, and the instance for the
 * super-interface for the <code>Element</code> if it exists.
 * <p>
 * Recursively processing the element definitions imposes the limitation that
 * the <code>Element</code> interface may not be inherited twice (either
 * directly or indirectly) by any <code>Element</code> definition.  Element
 * definitions must be derived from the <code>Element</code> interface, and
 * may also inherit from other non-<code>Element</code> interfaces.
 *
 * @author  James E. Stark
 * @version 1.0
 * @param   <T> The <code>Element</code> type of the <code>Definition</code>
 * @see     Property
 * @see     Selector
 */

public final class Definition<T extends Element>
{
	/** The logger */
	private final Logger log;

	/** The <code>Definition</code> for any super interface */
	private final Definition<?> parent;

	/** The interface represented by the definition */
	private final Class<T> type;

	/** The <code>Property</code> instances associated with the interface */
	private final Map<String, Property<?>> properties;

	/** The <code>Selector</code> instances for the interface */
	private final Map<String, Selector> selectors;

	/**
	 * Create the <code>Definition</code>.
	 *
	 * @param  type                  The <code>Element</code> interface, not
	 *                               null
	 *
	 * @throws IllegalStateException if <code>Element</code> is assignable from
	 *                               more than one super-interface or if the
	 *                               parent <code>Element</code> is not
	 *                               registered
	 */

	protected Definition (final Class<T> type)
	{
		assert type != null : "type is NULL";
		assert type.isInterface () : "type must be an interface";

		this.log = LoggerFactory.getLogger (Definition.class);

		this.type = type;
		this.properties = new HashMap<String, Property<?>> ();
		this.selectors = new HashMap<String, Selector> ();

		Class<?> def = null;

		for (Class<?> cls : type.getInterfaces ())
		{
			if (Element.class.isAssignableFrom (cls))
			{
				if (def != null)
				{
					this.log.error ("{} inherits Element multiple times: {}, {}, ...", type.getSimpleName (), def.getSimpleName (), cls.getSimpleName ());
					throw new IllegalStateException ("Element can no be inherited more then once");
				}

				def = cls;
			}
		}

		if (def != null)
		{
			this.parent = MetaData.getDefinition (def);

			if (this.parent == null)
			{
				this.log.error ("Super-interface for {} is not registered: {}", this.type.getSimpleName (), def.getSimpleName ());
				throw new IllegalStateException ("Super-interface is not registered");
			}
			else
			{
				this.log.debug ("Element: {} has Parent: {}", this.type.getSimpleName (), (this.parent.getElementType ()).getSimpleName ());
			}
		}
		else
		{
			this.parent = null;
			this.log.debug ("Element: {} has Parent: null", this.type.getSimpleName ());
		}
	}

	/**
	 * Add a <code>Property</code> to the <code>Definition</code>.
	 *
	 * @param  property                 The <code>Property</code> to add, not
	 *                                  null
	 *
	 * @return                          The <code>Property</code> in the
	 *                                  <code>Definition</code>
	 * @throws IllegalArgumentException if a different <code>Property</code>
	 *                                  already exists in the definition with
	 *                                  the same name
	 */

	@SuppressWarnings ("unchecked")
	protected <V> Property<V> addProperty (final Property<V> property)
	{
		this.log.trace ("addProperty: property={}", property);

		assert property != null : "property is NULL";

		Property<?> result = this.getProperty (property.getName ());

		if (result == null)
		{
			this.properties.put (property.getName (), property);
			result = property;
		}
		else if (! property.equals (result))
		{
			this.log.error ("Conflicts for Property {}, existing: {}, new: {}", property.getName (), result, property);
			throw new IllegalArgumentException ("A different property already exists with the specified name");
		}

		return (Property<V>) result;
	}

	/**
	 * Add a <code>Selector</code> to the <code>Definition</code>.
	 *
	 * @param  selector                 The <code>Selector</code> to add, not
	 *                                  null
	 *
	 * @return                          The <code>Selector</code> in the
	 *                                  <code>Definition</code>
	 * @throws IllegalArgumentException if a different <code>Selector</code>
	 *                                  already exists in the definition with
	 *                                  the same name
	 */

	protected Selector addSelector (final Selector selector)
	{
		this.log.trace ("addSelector: selector={}", selector);

		assert selector != null : "selector is NULL";

		Selector result = this.getSelector (selector.getName ());

		if (result == null)
		{
			this.selectors.put (selector.getName (), selector);
			result = selector;
		}
		else if (! result.equals (selector))
		{
			this.log.error ("Conflicts for Selector {}, existing: {}, new: {}", selector.getName (), result, selector);
			throw new IllegalArgumentException ("A different selector already exists with the specified name");
		}

		return result;
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
	 * Get the <code>Property</code> instance with the specified name.
	 *
	 * @param  name The name of the <code>Property</code> to retrieve, not null
	 *
	 * @return      The <code>Property</code>, may be null
	 */

	public Property<?> getProperty (final String name)
	{
		assert name != null : "name is NULL";

		Property<?> result = this.properties.get (name);

		if ((result == null) && (this.parent != null))
		{
			result = this.parent.getProperty (name);
		}

		return result;
	}

	/**
	 * Get the <code>Property</code> instance with the specified name and type.
	 *
	 * @param  name                     The name, not null
	 * @param  type                     The type, not null
	 *
	 * @return                          The <code>Property</code>, may be null
	 * @throws IllegalArgumentException if the type specified does not match
	 *                                  the type of the <code>Property</code>
	 */

	@SuppressWarnings ("unchecked")
	public <V> Property<V> getProperty (final String name, final Class<V> type)
	{
		assert name != null : "name is NULL";
		assert type != null : "type is NULL";

		Property<?> result = this.getProperty (name);

		if ((result != null) && (type != result.getPropertyType ()))
		{
			throw new IllegalArgumentException ("The type specified and the Type of the property do not match");
		}

		return (Property<V>) result;
	}

	/**
	 * Get the <code>Set</code> of <code>Property</code> instances which are
	 * associated with the <code>Element</code>.
	 *
	 * @return A <code>Set</code> of <code>Property</code> instances
	 */

	public Set<Property<?>> getProperties ()
	{
		Set<Property<?>> result = new HashSet<Property<?>> (this.properties.values ());

		if (this.parent != null)
		{
			result.addAll (this.parent.getProperties ());
		}

		return result;
	}

	/**
	 * Get the <code>Selector</code> instance with the specified name.
	 *
	 * @param  name The name of the <code>Selector</code> to retrieve, not null
	 *
	 * @return      The <code>Property</code>, may be null
	 */

	public Selector getSelector (final String name)
	{
		assert name != null : "name is NULL";

		Selector result = this.selectors.get (name);

		if ((result == null) && (this.parent != null))
		{
			result = this.parent.getSelector (name);
		}

		return result;
	}

	/**
	 * Get the <code>Set</code> of <code>Selector</code> instances which are
	 * associated with the <code>Element</code>.
	 *
	 * @return A <code>Set</code> of <code>Selector</code> instances
	 */

	public Set<Selector> getSelectors ()
	{
		Set<Selector> result = new HashSet<Selector> (this.selectors.values ());

		if (this.parent != null)
		{
			result.addAll (this.parent.getSelectors ());
		}

		return result;
	}
}
