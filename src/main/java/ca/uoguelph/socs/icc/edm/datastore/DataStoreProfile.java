/* Copyright (C) 2014 James E. Stark
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

package ca.uoguelph.socs.icc.edm.datastore;

import java.util.Map;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uoguelph.socs.icc.edm.domain.DomainModelElement;

/**
 * Meta-data describing the location and capabilities of a
 * <code>DataStore</code>.  This class and its subclasses describe a profile
 * containing the implementation details of particular instances of a 
 * <code>DataStore</code>.  The information contained in the profile includes:
 * <ul>
 * 	<li>Any necessary connection details
 * 	<li>An indication if the <code>DataStore</code> is mutable (writable)
 * </ul>
 * <p>
 * For each of the domain model interfaces the profile includes the following
 * information:
 * <ul>
 * 	<li>An indication if the interface is represented in the <code>DataStore</code>
 * 	<li>The class that implements the interface for the <code>DataStore</code>
 * 	<li>The class used to generate ID numbers for the interface in the
 * 	    <code>DataStore</code>
 * </ul>
 * <p>
 * All of the per-interface fields are required.  Even if a
 * <code>DataStore</code> does not internally represent an interface, an
 * implementation must be provided.  Similarly, an ID generator must be
 * specified, even if the <code>DataStore</code> is read-only.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     DataStoreProfileBuilder
 */

public abstract class DataStoreProfile
{
	/**
	 * <code>DataStore</code> profile data for a single domain model interface.
	 * This class is intended to be used internally in
	 * <code>DataStoreProfile</code> to hold the profile data for a single domain
	 * model interface.  All access to this class should come though the enclosing
	 * class.
	 */

	protected final class Entry
	{
		/** Is this interface represented in the <code>DataStore</code>? */
		private final Boolean available;
		
		/** <code>DataStore</code> generator for this interface */
		private final Class<? extends IdGenerator> generator;
		
		/** Class implementing the interface */
		private final Class<? extends DomainModelElement> implementation;

		/**
		 * Create the <code>Entry</code>.
		 *
		 * @param  available      An indication if the <code>DataStore</code>
		 *                        represents the interface represented by this entry,
		 *                        not null
		 * @param  generator      The ID generator class used by with the interface
		 *                        represented by this entry, not null
		 * @param  implementation The class implementing the interface represented by
		 *                        this entry, not null
		 */

		protected Entry (Boolean available, Class<? extends IdGenerator> generator, Class<? extends DomainModelElement> implementation)
		{
			this.available = available;
			this.generator = generator;
			this.implementation = implementation;
		}

		/**
		 * Determine if the interface represented by this entry is represented in the
		 * <code>DataStore</code>.
		 *
		 * @return <code>true</code> if the <code>DataStore</code> contains a
		 *         representation of the specified domain model interface,
		 *         <code>false</code> otherwise
		 */

		public Boolean isAvailable ()
		{
			return this.available;
		}

		/**
		 * Get the ID generator class to be used with the <code>DataStore</code>.
		 *
		 * @return The associated ID generator class
		 */

		public Class<? extends IdGenerator> getGenerator ()
		{
			return this.generator;
		}

		/**
		 * Get the implementation class.
		 *
		 * @return The class used to represent the interface in the
		 *         <code>DataStore</code>
		 */

		public Class<? extends DomainModelElement> getImplClass ()
		{
			return this.implementation;
		}
	}

	/** Is the <code>DataStore</code> mutable? */
	private final Boolean mutable;

	/** Interface class to Implementation class mapping */
	private final Map<Class<?>, Entry> entries;

	/**
	 * Create the <code>DataStoreProfile</code>.  This constructor is not intended
	 * to be called directly, the profile should be created though its builder.
	 *
	 * @param  mutable    Indication if the database (and domain model) can be
	 *                    changed, not null
	 * @param  entries    Domain model interface class to implementation class
	 *                    mapping, not null
	 */

	protected DataStoreProfile (Boolean mutable, Map<Class<? extends DomainModelElement>, Entry> entries)
	{
		this.mutable = mutable;
		this.entries = new HashMap<Class<?>, Entry> (entries);
	}

	/**
	 * Determine if the <code>DomainModel</code> and its underlying
	 * <code>DataStore</code> can be changed.
	 *
	 * @return <code>true</code> if the <code>DomainModel</code> and its
	 *         underlying <code>DataStore</code> can be changed,
	 *         <code>false</code> otherwise
	 */

	public final Boolean isMutable ()
	{
		return this.mutable;
	}

	/**
	 * Determine if the <code>DataStore</code> contains a representation of a
	 * given domain model interface.
	 *
	 * @param  element Domain model interface class, not null
	 * @return         <code>true</code> if the <code>DataStore</code> contains a
	 *                 representation of the specified domain model interface,
	 *                 <code>false</code> otherwise
	 */

	public final Boolean isAvailable (Class<? extends DomainModelElement> element)
	{
		if (element == null)
		{
			throw new NullPointerException ();
		}

		if (! this.entries.containsKey (element))
		{
			throw new IllegalArgumentException ();
		}

		return (this.entries.get (element)).isAvailable ();
	}

	/**
	 * Get the <code>DataStore</code> ID generation class for the specified domain
	 * model interface.
	 *
	 * @param  element Domain model interface class, not null
	 * @return         The associated ID generator class
	 */

	public final Class<? extends IdGenerator> getGenerator (Class<? extends DomainModelElement> element)
	{
		return (this.entries.get (element)).getGenerator ();
	}

	/**
	 * Get the implementation class to be used for the specified domain model
	 * interface.
	 *
	 * @param  element Domain model interface class, not null
	 * @return         The class used to represent the interface in the
	 *                 <code>DataStore</code>
	 */

	public final Class<? extends DomainModelElement> getImplClass (Class<? extends DomainModelElement> element)
	{
		return (this.entries.get (element)).getImplClass ();
	}

	/**
	 * Get the implementation class for the <code>DataStore</code>. 
	 *
	 * @return The <code>DataStore</code> implementation class
	 */

	public abstract Class<? extends DataStore> getDataStoreClass ();
}
