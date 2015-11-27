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

package ca.uoguelph.socs.icc.edm.domain.element.activity.moodle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityReference;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.NamedActivity;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;
import ca.uoguelph.socs.icc.edm.domain.datastore.Persister;

/**
 * Implementation of the <code>Activity</code> interface for the moodle/resource
 * <code>ActivitySource</code>/<code>ActivityType</code>.  It is expected that
 * this class will be accessed though the <code>Activity</code> interface,
 * along with the relevant manager, and builder.  See the <code>Activity</code>
 * interface documentation for details.
 * <p>
 * This class was generated from the <code>NamedActivity</code> template,
 * with the following values:
 * <p>
 * <ul>
 * <li>ActivitySource = moodle
 * <li>ActivityType   = resource
 * <li>ClassName      = Resource
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class Resource extends NamedActivity
{
	/**
	 * <code>Builder</code> for <code>Resource</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 * @see     ca.uoguelph.socs.icc.edm.domain.NamedActivity.Builder
	 */

	public static final class Builder extends NamedActivity.Builder
	{
		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  persister        The <code>Persister</code> used to store the
		 *                          <code>Activity</code>, not null
		 * @param  referenceBuilder Builder for the internal
		 *                          <code>ActivityReference</code> instance, not
		 *                          null
		 */

		private Builder (
				final Persister<Activity> persister,
				final ActivityReference.Builder referenceBuilder)
		{
			super (persister, referenceBuilder);
		}

		/**
		 * Create an instance of the <code>Activity</code>.
		 *
		 * @return The new <code>Activity</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected Activity createElement ()
		{
			this.log.trace ("createElement");

			return new Resource (this);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** Copy of the id number to work around bad JPA behaviour */
	private Long id;

	/** The name of the <code>Activity</code> */
	private String name;

	/** The associated <code>Grade</code> instances */
	private Set<Grade> grades;

	/** The associated <code>SubActivity</code> instances*/
	private List<SubActivity> subActivities;

	/**
	 * Register the <code>Resource</code> with the factories on
	 * initialization.
	 */

	static
	{
		Activity.registerImplementation ("moodle", "resource", Resource.class);
	}

	/**
	 * Create the <code>Activity</code> instance with Null values.
	 */

	protected Resource ()
	{
		this.name = null;

		this.grades = new HashSet<Grade> ();
		this.subActivities = new ArrayList<SubActivity> ();
	}

	/**
	 * Create an <code>Activity</code> from the supplied <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected Resource (final Builder builder)
	{
		super (builder);

		this.name = Preconditions.checkNotNull (builder.getName (), "name");

		this.grades = new HashSet<Grade> ();
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
	 * Get the <code>Set</code> of <code>Grade</code> instances which are
	 * associated with the <code>Activity</code>.  Not all
	 * <code>Activity</code> instances are graded.  If the
	 * <code>Activity</code> does is not graded then the <code>Set</code> will
	 * be empty.
	 *
	 * @return A <code>Set</code> of <code>Grade</code> instances
	 */

	@Override
	public Set<Grade> getGrades ()
	{
		this.grades.forEach (x -> this.propagateDomainModel (x));

		return Collections.unmodifiableSet (this.grades);
	}

	/**
	 * Initialize the <code>Set</code> of <code>Grade</code> instances
	 * associated with the <code>Activity</code> instance.  This method is
	 * intended to be used to initialize a new <code>Activity</code> instance.
	 *
	 * @param  grades The <code>Set</code> of <code>Grade</code> instances, not
	 *                null
	 */

	@Override
	protected void setGrades (final Set<Grade> grades)
	{
		assert grades != null : "grades is NULL";

		this.grades = grades;
	}

	/**
	 * Add the specified <code>Grade</code> to the
	 * <code>Activity</code>.
	 *
	 * @param  grade    The <code>Grade</code> to add, not null
	 *
	 * @return          <code>True</code> if the <code>Grade</code> was
	 *                  successfully added, <code>False</code> otherwise
	 */

	@Override
	protected boolean addGrade (final Grade grade)
	{
		assert grade != null : "grade is NULL";

		return this.grades.add (grade);
	}

	/**
	 * Remove the specified <code>Grade</code> from the
	 * <code>Activity</code>.
	 *
	 * @param  grade    The <code>Grade</code> to remove, not null
	 *
	 * @return          <code>True</code> if the <code>Grade</code> was
	 *                  successfully removed from, <code>False</code> otherwise
	 */

	@Override
	protected boolean removeGrade (final Grade grade)
	{
		assert grade != null : "grade is NULL";

		return this.grades.remove (grade);
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
