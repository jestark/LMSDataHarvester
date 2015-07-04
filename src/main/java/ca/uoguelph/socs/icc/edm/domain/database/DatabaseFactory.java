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

package ca.uoguelph.socs.icc.edm.domain.database;

import java.util.Map;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.DomainModelBuilder;
import ca.uoguelph.socs.icc.edm.domain.datastore.Profile;
import ca.uoguelph.socs.icc.edm.domain.datastore.jpa.JPADataStoreBuilder;

/**
 * Create <code>Profile</code>'s
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class DatabaseFactory
{
	/** The default JPA unit name */
	private final String unitname;

	/** Internal <code>DataStore</code> profile builder */
	private final JPADataStoreBuilder builder;

	/**
	 * Create the <code>DatabaseFactory</code>.
	 *
	 * @param  unitname The name of the default JPA unit for this factory, not null
	 */

	protected DatabaseFactory (String unitname)
	{
		this.unitname = unitname;
		this.builder = new JPADataStoreBuilder ();
	}

	/**
	 *
	 *
	 * @param builder
	 */

	protected abstract void buildProfile (DomainModelBuilder builder);

	/**
	 * Create a <code>Profile</code> for connecting to a database using
	 * the default connection parameters.  The unit name is default specified by
	 * the factory implementation and the database connection parameters are
	 * assumed to be stored in the JPA configuration (persistence.xml).
	 *
	 * @return A complete profile using default connection parameters
	 */

	public Profile createProfile ()
	{
		return this.createProfile (this.unitname);
	}

	/**
	 * Create a <code>Profile</code> for connecting to a database using
	 * the specified JPA unit and default connection parameters.  The database
	 * connection parameters are assumed to be stored in the JPA configuration
	 * (persistence.xml).
	 *
	 * @param  unitname The name of the JPA configuration unit, not null
	 * @return          A complete profile using the specified JPA configuration
	 *                  unit.
	 */

	public Profile createProfile (String unitname)
	{
		return this.createProfile (unitname, null, null, null);
	}

	/**
	 * Create a <code>Profile</code> for connecting to a database using
	 * the specified connection parameters.  Each of the parameters is optional.
	 * If a parameter is NULL it will be ignored.  If the specified
	 * <code>unitname</code> is null, then the default <code>unitname</code> will
	 * be used instead.  Other parameters will not be included in the profile if
	 * the specified value is NULL.
	 * <p>
	 * Any parameters which are specified as NULL are assumed to be included in
	 * the JPA unit configuration.  If a given parameter is no in the JPA unit
	 * configuration, then it needs to be included here.  Otherwise it can be
	 * safely set th NULL here, or if it is specified then the parameter set
	 * here will override the value in the JPA unit configuration.
	 *
	 * @param  unitname The name of the JPA configuration unit
	 * @param  url      The JDBC URL to use to connect to the database
	 * @param  username The username to use to connect to the database
	 * @param  password The password to use to connect to the database
	 * @return          A complete profile using the specified parameters
	 */

	public Profile createProfile (String unitname, String url, String username, String password)
	{
		this.builder.clear ();

		// Use the default unitname if the parameter is null
		if (unitname != null)
		{
			this.builder.setUnitName (unitname);
		}
		else
		{
			this.builder.setUnitName (this.unitname);
		}

		if (url != null)
		{
			this.builder.setConnectionURL (url);
		}

		if (username != null)
		{
			this.builder.setConnectionUsername (username);
		}

		if (password != null)
		{
			this.builder.setConnectionPassword (password);
		}

		DomainModelBuilder modelbuilder = new DomainModelBuilder ();

		this.buildProfile (modelbuilder);

		return modelbuilder.createProfile ();
	}

	/**
	 * Get an instance of the <code>DomainModel</code> for the database.  This
	 * method will generate a profile using the default connection parameters then
	 * use that profile to create the <code>DomainModel</code>.
	 *
	 * @return An instance of the <code>DomainModel</code> for the database
	 * @see    #createProfile
	 */

	public DomainModel createDomainModel ()
	{
		return (new DomainModelBuilder (this.createProfile ())).createDomainModel (this.builder);
	}

	/**
	 * Get an instance of the <code>DomainModel</code> for the database.  This
	 * method will generate a profile using the specified JPA unit and the default
	 * connection parameters then use that profile to create the
	 * <code>DomainModel</code>.
	 *
	 * @param  unitname The name of the JPA configuration unit, not null
	 * @return          An instance of the <code>DomainModel</code> for the
	 *                  database
	 * @see    #createProfile(String)
	 */

	public DomainModel createDomainModel (String unitname)
	{
		this.createProfile (unitname);
		return (new DomainModelBuilder (this.createProfile (unitname))).createDomainModel (this.builder);
	}

	/**
	 * Get an instance of the <code>DomainModel</code> for the database.  This
	 * method will generate a profile using the specified connection parameters
	 * then use that profile to create the <code>DomainModel</code>.
	 *
	 * @param  unitname The name of the JPA configuration unit
	 * @param  url      The JDBC URL to use to connect to the database
	 * @param  username The username to use to connect to the database
	 * @param  password The password to use to connect to the database
	 * @return          An instance of the <code>DomainModel</code> for the
	 *                  database
	 * @see    #createProfile(String, String, String, String)
	 */

	public DomainModel createDomainModel (String unitname, String url, String username, String password)
	{
		return (new DomainModelBuilder (this.createProfile (unitname, url, username, password))).createDomainModel (this.builder);
	}
}
