/* Copyright (C) 2014, 2015, 2016 James E. Stark
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.auto.service.AutoService;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.ActivitySource;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Element;

/**
 * Implementation of the <code>ActivitySource</code> interface.  It is expected
 * that instances of this class will be accessed though the
 * <code>ActivitySource</code> interface, along with the relevant manager, and
 * builder.  See the <code>ActivitySource</code> interface documentation for
 * details.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class ActivitySourceData extends ActivitySource
{
	/**
	 * Representation of an <code>Element</code> implementation class.
	 * Instances of this class are used to load the <code>Element</code>
	 * implementations into the JVM via the <code>ServiceLoader</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@AutoService (Element.Definition.class)
	public static final class Definition extends ActivitySource.Definition
	{
		/**
		 * Create the <code>Definition</code>.
		 */

		public Definition ()
		{
			super (ActivitySourceData.class, ActivitySourceData::new);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key of the activity source */
	private @Nullable Long id;

	/** The name of the activity source */
	private String name;

	/** The set of activity types which are associated with the source */
	private List<ActivityType> types;

	/**
	 * Create the <code>ActivitySource</code> with null values.
	 */

	protected ActivitySourceData ()
	{
		this.id = null;
		this.name = null;

		this.types = new ArrayList<ActivityType> ();
	}

	/**
	 * Create an <code>ActivitySource</code> from the supplied
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected ActivitySourceData (final ActivitySource.Builder builder)
	{
		super (builder);

		this.id = builder.getId ();
		this.name = Preconditions.checkNotNull (builder.getName (), "name");

		this.types = new ArrayList<ActivityType> ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the
	 * <code>ActivitySource</code> instance.
	 *
	 * @return a Long integer containing <code>DataStore</code> identifier
	 */

	@Override
	@CheckReturnValue
	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used to initialize the <code>DataStore</code> identifier on a new
	 * <code>ActivitySource</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final @Nullable Long id)
	{
		this.id = id;
	}

	/**
	 * Get the name of the <code>ActivitySource</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivitySource</code>
	 */

	@Override
	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>ActivitySource</code>.  This method is
	 * intended to be used to initialize a new <code>ActivitySource</code>
	 * instance.
	 *
	 * @param name The name of the <code>ActivitySource</code>
	 */

	@Override
	protected void setName (final String name)
	{
		assert name != null : "name is NULL";

		this.name = name;
	}

	/**
	 * Get the <code>List</code> of <code>ActivityType</code> instances for the
	 * <code>ActivitySource</code>.  If there are no <code>ActivityType</code>
	 * instances associated with the <code>ActivitySource</code> then the
	 * <code>List</code> will be empty.
	 *
	 * @return A <code>List</code> of <code>ActivityType</code> instances
	 */

	@Override
	public List<ActivityType> getTypes ()
	{
		this.types.forEach (x -> this.propagateDomainModel (x));

		return Collections.unmodifiableList (this.types);
	}

	/**
	 * Initialize the <code>List</code> of dependent <code>ActivityType</code>
	 * instances.  This method is intended to be used to initialize a new
	 * <code>ActivitySource</code> instance.
	 *
	 * @param  types The <code>List</code> of <code>ActivityType</code>
	 *               instances to be associated with the
	 *               <code>ActivitySource</code>
	 */

	@Override
	protected void setTypes (final List<ActivityType> types)
	{
		assert types != null : "types is NULL";

		this.types = types;
	}

	/**
	 * Add the specified <code>ActivityType</code> to the
	 * <code>ActivitySource</code>.
	 *
	 * @param  type   The <code>ActivityType</code> to add, not null
	 * @return        <code>True</code> if the <code>ActivityType</code> was
	 *                successfully added, <code>False</code> otherwise
	 */

	@Override
	protected boolean addType (final ActivityType type)
	{
		assert type != null : "type is NULL";

		return this.types.add (type);
	}

	/**
	 * Remove the specified <code>ActivityType</code> from the
	 * <code>ActivitySource</code>.
	 *
	 * @param  type   The <code>ActivityType</code> to remove, not null
	 * @return        <code>True</code> if the <code>ActivityType</code> was
	 *                successfully removed, <code>False</code> otherwise
	 */

	@Override
	protected boolean removeType (final ActivityType type)
	{
		assert type != null : "type is NULL";

		return this.types.remove (type);
	}
}
