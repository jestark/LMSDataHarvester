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

package ca.uoguelph.socs.icc.edm.domain.element;

import java.io.Serializable;

import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;

import ca.uoguelph.socs.icc.edm.domain.metadata.Implementation;

/**
 * Implementation of the <code>ActivityType</code> interface.  It is expected
 * that instances of this class will be accessed though the
 * <code>ActivityType</code> interface, along with the relevant manager, and
 * builder.  See the <code>ActivityType</code> interface documentation for
 * details.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class ActivityTypeData extends ActivityType implements Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key of the activity type */
	private Long id;

	/** The name of the activity type */
	private String name;

	/** The source of the activity type */
	private ActivitySource source;

	/**
	 * Static initializer to register the <code>ActivityTypeData</code> class
	 * with the factories.
	 */

	static
	{
		Implementation.getInstance (ActivityType.class, ActivityTypeData.class, ActivityTypeData::new);
	}

	/**
	 * Create the <code>ActivityType</code> with null values.
	 */

	public ActivityTypeData ()
//	protected ActivityTypeData ()
	{
		this.id = null;
		this.name = null;
		this.source = null;
	}

	/**
	 * Get the <code>DataStore</code> identifier for the
	 * <code>ActivityType</code> instance.
	 *
	 * @return a Long integer containing <code>DataStore</code> identifier
	 */

	@Override
	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used by a <code>DataStore</code> when the <code>ActivityType</code>
	 * instance is loaded, or by the <code>ActivityTypeBuilder</code>
	 * implementation to set the <code>DataStore</code> identifier, prior to
	 * storing a new <code>ActivityType</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final Long id)
	{
		this.id = id;
	}

	/**
	 * Get the name of the <code>ActivityType</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivityType</code>
	 */

	@Override
	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>ActivityType</code>.  This method is intended
	 * to be used by a <code>DataStore</code> when the
	 * <code>ActivityType</code> instance is loaded.
	 *
	 * @param  name The name of the <code>ActivityType</code>
	 */

	@Override
	protected void setName (final String name)
	{
		assert name != null : "name is NULL";

		this.name = name;
	}

	/**
	 * Get the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 *
	 * @return The <code>ActivitySource</code> instance
	 */

	@Override
	public ActivitySource getSource ()
	{
		return this.propagateDomainModel (this.source);
	}

	/**
	 * Set the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 * This method is intended to be used by a <code>DataStore</code> when the
	 * <code>ActivityType</code> instance is loaded.
	 *
	 * @param  source The <code>ActivitySource</code> for the
	 *                <code>ActivityType</code>
	 */

	@Override
	protected void setSource (final ActivitySource source)
	{
		assert source != null : "source is NULL";

		this.source = source;
	}
}
