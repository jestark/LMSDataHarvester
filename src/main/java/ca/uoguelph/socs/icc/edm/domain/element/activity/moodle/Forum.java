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

package ca.uoguelph.socs.icc.edm.domain.element.activity.moodle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.auto.service.AutoService;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityReference;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;

/**
 * Implementation of the <code>Activity</code> interface for the moodle/forum
 * <code>ActivitySource</code>/<code>ActivityType</code>.  It is expected that
 * this class will be accessed though the <code>Activity</code> interface,
 * along with the relevant manager, and builder.  See the <code>Activity</code>
 * interface documentation for details.
 * <p>
 * This class was generated from the <code>Activity</code> template,
 * with the following values:
 * <p>
 * <ul>
 * <li>ActivitySource = moodle
 * <li>ActivityType   = forum
 * <li>ClassName      = Forum
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class Forum extends Activity
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
	public static final class Definition extends Activity.Definition
	{
		/**
		 * Create the <code>Definition</code>.
		 */

		public Definition ()
		{
			super (Forum.class, Forum::new);

			Activity.registerImplementation ("moodle", "forum", Forum.class);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** Copy of the id number to work around bad JPA behaviour */
	private Long id;

	/** The name of the <code>Activity</code> */
	private String name;

	/** The associated <code>SubActivity</code> instances*/
	private List<SubActivity> subActivities;

	/**
	 * Create the <code>Activity</code> instance with Null values.
	 */

	protected Forum ()
	{
		this.name = null;

		this.subActivities = new ArrayList<SubActivity> ();
	}

	/**
	 * Create an <code>Activity</code> from the supplied <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected Forum (final Activity.Builder builder)
	{
		super (builder);

		this.name = Preconditions.checkNotNull (builder.getName (), "name");

		this.subActivities = new ArrayList<SubActivity> ();
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Element</code>
	 * instance.
	 *
	 * @return The <code>DataStore</code> identifier
	 */

	@Override
	public Long getId ()
	{
		return (this.getReference () != null) ? this.getReference ().getId () : this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used to initialize the <code>DataStore</code> identifier for a new
	 * <code>Activity</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final @Nullable Long id)
	{
		this.id = id;
	}

	/**
	 * Get the name of the <code>Activity</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Activity</code>
	 */

	@Override
	public String getName ()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>Activity</code>.  This method is intended to
	 * be used to initialize a new <code>Activity</code> instance.
	 *
	 * @param  name The name of the <code>Activity</code>, not null
	 */

	@Override
	protected void setName (final String name)
	{
		assert name != null : "name is NULL";

		this.name = name;
	}

	/**
	 * Get the <code>List</code> of <code>SubActivity</code> instances
	 * associated with the <code>Activity</code>.
	 * <p>
	 * This method is a redefinition of the same method in the superclass.  It
	 * exists solely to allow JPA to map the relationship to the instances of
	 * the child class.
	 *
	 * @return The <code>List</code> of <code>SubActivity</code> instances
	 */

	@Override
	public List<SubActivity> getSubActivities ()
	{
		this.subActivities.forEach (x -> this.propagateDomainModel (x));

		return Collections.unmodifiableList (this.subActivities);
	}

	/**
	 * Initialize the <code>List</code> of <code>SubActivity</code> instances
	 * for the <code>Activity</code>.  This method is intended to be used to
	 * initialize a new <code>Activity</code> instance.
	 *
	 * @param  subActivities The <code>List</code> of <code>SubActivity</code>
	 *                       instances, not null
	 */

	@Override
	protected void setSubActivities (final List<SubActivity> subActivities)
	{
		assert subActivities != null : "subActivities is NULL";

		this.subActivities = subActivities;
	}

	/**
	 * Add the specified <code>SubActivity</code> to the <code>Activity</code>.
	 *
	 * @param  subActivity The <code>SubActivity</code> to add, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully added, <code>False</code> otherwise
	 */

	@Override
	protected boolean addSubActivity (final SubActivity subActivity)
	{
		assert subActivity != null : "subActivity is NULL";

		return this.subActivities.add (subActivity);
	}

	/**
	 * Remove the specified <code>SubActivity</code> from the
	 * <code>Activity</code>.
	 *
	 * @param  subActivity The <code>SubActivity</code> to remove, not null
	 *
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully removed, <code>False</code>
	 *                     otherwise
	 */

	@Override
	protected boolean removeSubActivity (final SubActivity subActivity)
	{
		assert subActivity != null : "subActivity is NULL";

		return this.subActivities.remove (subActivity);
	}
}
