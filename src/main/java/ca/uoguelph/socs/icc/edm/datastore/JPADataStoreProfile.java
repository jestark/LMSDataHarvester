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
 * @see     JPADataStoreProfileBuilder
 */

public final class JPADataStoreProfile extends DataStoreProfile
{
	/** JPA unit name */
	private final String unitname;

	/** Database connection parameters */
	private final Map<String, String> parameters;

	/**
	 * Create the <code>JPADataStoreProfile</code>.  This constructor is not
	 * intended to be called directly, the profile should be created though its
	 * builder.
	 *
	 * @param  mutable    Indication if the database (and domain model) can be
	 *                    changed, not null
	 * @param  entries    Domain model interface class to implementation class
	 *                    mapping, not null
	 * @param  unitname   The name of the JPA unit to use when connecting to the
	 *                    database, not null
	 * @param  parameters Map containing the JPA connection parameters.
	 */

	protected JPADataStoreProfile (Boolean mutable, Map<Class<? extends DomainModelElement>, Entry> entries, String unitname, Map<String, String> parameters)
	{
		super (mutable, entries);

		this.unitname = unitname;
		this.parameters = new HashMap<String, String> (parameters);
	}

	/**
	 * Get the implementation class for the <code>DataStore</code>. 
	 *
	 * @return The <code>DataStore</code> implementation class
	 */

	public Class<? extends DataStore> getDataStoreClass ()
	{
		return JPADataStore.class;
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
	 * Get the database connection parameters.
	 *
	 * @return A Map containing the specified database connection parameters.
	 */

	public Map<String, String> getParameters ()
	{
		return new HashMap<String, String> (this.parameters);
	}
}
