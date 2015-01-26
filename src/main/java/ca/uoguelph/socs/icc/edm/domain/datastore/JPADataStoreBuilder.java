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

package ca.uoguelph.socs.icc.edm.domain.datastore;

import java.util.Map;
import java.util.HashMap;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A profile for any <code>DataStore</code> which used the Java Persistence
 * API.  This class adds the connection information required to connect to a
 * database using JPA to the <code>DataStoreProfile</code>.  The additional
 * information includes the JPA unit name and the connection parameters, which
 * will be passed to the JDBC driver.
 * <p>
 * The JPA unit is the entry in persistence.xml that describes the database,
 * including the mapping information and possibly the connection parameters.
 * Connection parameters typically include the URL, username, and password
 * used to connect to the database.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class JPADataStoreBuilder implements DataStoreBuilder
{
	private final class Key
	{
		/** The JPA unit associated with the domain model */
		private final String unitname;

		/** The URL for the connected database */
		private final String url;

		/**
		 * Create the Key.
		 *
		 * @param  unitname The name of the associated JPA unit
		 * @param  url      The URL of the connected database
		 */

		protected Key (String unitname, String url)
		{
			this.unitname = unitname;
			this.url = url;
		}

		/**
		 * Override the equals method to determine if this <code>Key</code> is
		 * equal to another based on its attributes.
		 *
		 * @param  obj The object to compare to this <code>Key</code>
		 * @return     <code>true</code> if the two keys are the same,
		 *             <code>false</code> otherwise
		 */

		@Override
		public boolean equals (Object obj)
		{
			boolean result = false;

			if (obj != null)
			{
				if (obj == this)
				{
					result = true;
				}
				else if (obj.getClass () == this.getClass ())
				{
					EqualsBuilder ebuilder = new EqualsBuilder ();
					ebuilder.append (this.unitname, ((Key) obj).unitname);
					ebuilder.append (this.url, ((Key) obj).url);

					result = ebuilder.isEquals ();
				}
			}

			return result;
		}

		/**
		 * Compute a unique <code>hashCode</code> for a <code>Key</code> based on
		 * its attributes.
		 *
		 * @return The hash code
		 */

		@Override
		public int hashCode ()
		{
			final int base = 919;
			final int mult = 2;

			HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
			hbuilder.append (this.unitname);
			hbuilder.append (this.url);

			return hbuilder.toHashCode ();
		}

		/**
		 * Get the name of the JPA unit stored in the key.
		 *
		 * @return A <code>String</code> containing the name of the JPA unit
		 */

		public String getUnitName ()
		{
			return this.unitname;
		}

		/**
		 * Get the URL for the associated database connection.
		 *
		 * @return A <code>String</code> containing the URL, NULL if there is no
		 *         associated URL
		 */

		public String getURL ()
		{
			return this.url;
		}

		/**
		 * Override the toString method to print the unit name and the URL.
		 *
		 * @return A <code>String</code> containing the formatted unit name and URL
		 */

		@Override
		public String toString ()
		{
			return this.unitname + ": " + this.url;
		}
	}

	/**  */
//	private static final Map<Key, DomainModel> cache;

	/** JPA unit name */
	private String unitname;

	/** Database connection parameters */
	private Map<String, String> parameters;

	/** Logger */
	private final Log log;

	/**
	 * Create the <code>JPADataStoreBuilder</code>.
	 */

	public JPADataStoreBuilder ()
	{
		this.log = LogFactory.getLog (JPADataStoreBuilder.class);

		this.unitname = null;
		this.parameters = new HashMap<String, String> ();
	}

	/**
	 * Reset the builder to a blank state.
	 */

	public void clear ()
	{
		this.unitname = null;
		this.parameters.clear ();
	}

	/**
	 * Get the JPA unit (entry on persistence.xml) to use when connecting to the
	 * database.
	 *
	 * @return A <code>String</code> containing the name of the JPA unit
	 */

	public String getUnitName ()
	{
		return this.unitname;
	}

	/**
	 * Set the name of the JPA unit to use when connecting to the database.
	 *
	 * @param  unitname   The name of the JPA unit to use when connecting to the
	 *                    database, not null
	 */

	public void setUnitName (String unitname)
	{
		if (unitname == null)
		{
			this.log.error ("unit name is NULL");
			throw new NullPointerException ();
		}

		this.unitname = unitname;
	}

	/**
	 * Set, clear or replace the value of the specified parameter.  If the
	 * specified parameter does not exist and the specified value is not null, it
	 * will be added, or the value will be replaced if the parameter already
	 * exists.  If the value is null then the parameter will be cleared.
	 *
	 * @param  name  The name of the parameter to set
	 * @param  value The new value for the parameter
	 */

	private void setParameter (String name, String value)
	{
		if (value == null)
		{
			this.parameters.remove (name);
		}
		else
		{
			this.parameters.put (name, value);
		}
	}

	/**
	 * Get the URL to use to connect to the database.
	 *
	 * @return A <code>String</code> containing the URL, or null if the URL is not
	 *         set.
	 */

	public String getConnectionURL ()
	{
		return this.parameters.get ("url");
	}

	/**
	 * Set the URL to use when connecting to the database.  If the value of the
	 * URL is not null then the URL will be added to the connection parameters, it
	 * will replace a pre-existing value if one was already set.  The parameter
	 * will be cleared if the specified value is null.
	 *
	 * @param  url The URL to use when connecting to the database.
	 */

	public void setConnectionURL (String url)
	{
		this.setParameter ("url", url);
	}

	/**
	 * Get the username to use when connecting to the database.
	 *
	 * @return A <code>String</code> containing the username, or null
	 *         if a password has not been set
	 */

	public String getConnectionUsername ()
	{
		return this.parameters.get ("user");
	}

	/**
	 * Set the username to user when connecting to the database.   If the value of
	 * the username is not null then the username will be added to the connection
	 * parameters, it will replace a pre-existing value if one was already set.
	 * The parameter will be cleared if the specified value is null.
	 *
	 * @param  username The username to use when connecting to the database
	 */

	public void setConnectionUsername (String username)
	{
		this.setParameter ("user", username);
	}

	/**
	 * Get the database connection password.
	 *
	 * @return A <code>String</code> containing the connection password, or null
	 *         if a password has not been set
	 */

	public String getConnectionPassword ()
	{
		return this.parameters.get ("password");
	}

	/**
	 * Set the database connection password.  If the value of the password is not
	 * null then the password will be added to the connection parameters, it will
	 * replace a pre-existing value if one was already set.  The parameter will be
	 * cleared if the specified value is null.
	 *
	 * @param  password The password to use when connecting to the database
	 */

	public void setConnectionPassword (String password)
	{
		this.setParameter ("password", password);
	}

	/**
	 * Get the database connection parameters.
	 *
	 * @return a <code>Map</code> containing the database connection parameters
	 */

	public Map<String, String> getConnectionParameters ()
	{
		return new HashMap<String, String> (this.parameters);
	}

	/**
	 * Set the database connection parameters.  The provided <code>Map</code> will
	 * replace all of the currently set parameters.
	 *
	 * @param  parameters <code>Map</code> containing the new parameters, not null
	 */

	public void setConnectionParameters (Map<String, String> parameters)
	{
		if (parameters == null)
		{
			this.log.error ("Parameter map is NULL");
			throw new NullPointerException ();
		}

		this.parameters.clear ();
		this.parameters.putAll (parameters);
	}

	/**
	 * Create the <code>DataStore</code>
	 */

	@Override
	public DataStore createDataStore (DataStoreProfile profile)
	{
		return new JPADataStore (profile, this.unitname, this.parameters);
	}
}
