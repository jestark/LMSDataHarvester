/* Copyright (C) 2014, 2015 James E. Stark
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
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Element;

import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.IdGenerator;

/**
 * Profile data for the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 2.0
 */

public final class Profile
{
	/** The Logger */
	private final Logger log;

	/** Is the <code>DataStore</code> mutable? */
	private final boolean mutable;

	/** The name of the <code>Profile</code> */
	private final String name;

	/** The default implementation classes for each <code>Element</code> */
	private final Map<Class<? extends Element>, Class<? extends Element>> implementations;

	/** The <code>IdGenerator</code> to be used with each <code>Element</code> */
	private final Map<Class<? extends Element>, Class<?>> generators;

	/**
	 * Create the <code>Profile</code>.  This constructor is not intended
	 * to be called directly, the profile should be created though its builder.
	 *
	 * @param  name            The name of the <code>Profile</code>, not null
	 * @param  mutable         The designed mutability of the
	 *                         <code>DataStore</code>, not null
	 * @param  implementations The <code>Map</code> of default
	 *                         <code>Element</code> implementation classes for
	 *                         the <code>DataStore</code> instance, not null
	 * @param  generators      The <code>Map</code> of <code>IdGenerator</code>
	 *                         classes to be used for each
	 *                         <code>Element</code>, not null
	 */

	protected Profile (final String name,
			final boolean mutable,
			final Map<Class<? extends Element>, Class<? extends Element>> implementations,
			final Map<Class<? extends Element>, Class<?>> generators)
	{
		assert name != null : "name is NULL";
		assert implementations != null : "implementations is NULL";
		assert generators != null : "generators is NULL";

		this.log = LoggerFactory.getLogger (Profile.class);

		this.name = name;
		this.mutable = mutable;
		this.implementations = new HashMap<> (implementations);
		this.generators = new HashMap<> (generators);
	}

	/**
	 * Get the name of the <code>Profile</code>.
	 *
	 * @return The name of the <code>Profile</code>
	 */

	public String getName ()
	{
		return this.name;
	}

	/**
	 * Determine if the <code>DataStore</code> can be changed.
	 *
	 * @return <code>true</code> if the <code>DataStore</code> can be changed,
	 *         <code>false</code> otherwise
	 */

	public boolean isMutable ()
	{
		return this.mutable;
	}

	/**
	 * Determine if there is an implementation class registered for the
	 * specified <code>Element</code> interface class.
	 *
	 * @param  type The <code>Element</code> interface class, not null
	 *
	 * @return      <code>true</code> if the <code>Element</code> has a
	 *              registered implementation class, <code>false</code>
	 *              otherwise
	 */

	public boolean hasElementClass (final Class<? extends Element> type)
	{
		this.log.trace ("hasElementClass: type={}", type);

		assert type != null : "type is NULL";

		return this.implementations.containsKey (type);
	}

	/**
	 * Get the default implementation class for the specified
	 * <code>Element</code>.
	 *
	 * @param  element The <code>Element</code> interface class, not null
	 *
	 * @return         The <code>Element</code> default implementation class
	 */

	public Class<? extends Element> getElementClass (Class<? extends Element> element)
	{
		this.log.trace ("getElementClass: element={}", element);

		assert element != null : "element is NULL";
		assert this.hasElementClass (element) : "No implementation class registered for element";

		return this.implementations.get (element);
	}

	/**
	 * Get the <code>IdGenerator</code> associated with the specified
	 * <code>Element</code> class, for the <code>DataStore</code>.  This method
	 * searches for the appropriate <code>IdGenerator</code> for the specified
	 * <code>Element</code> class.
	 *
	 * @param  element               The <code>Element</code> class, not null
	 *
	 * @return                       The associated <code>IdGenerator</code>
	 * @throws IllegalStateException if there id no <code>MetaData</code>
	 *                               registered for the specified
	 *                               <code>Element</code> class or one of its
	 *                               parents
	 */

	public Class<?> getGenerator (final Class<? extends Element> element)
	{
		this.log.trace ("getGenerator: element={}", element);

		assert element != null : "element is NULL";

		Class<?> key = element;

		while (! this.generators.containsKey (key))
		{
			key = key.getSuperclass ();

			if (! Element.class.isAssignableFrom (key))
			{
				throw new IllegalStateException (String.format ("No Generator registered for: %s", element.getSimpleName ()));
			}
		}

		return this.generators.get (key);
	}
}
