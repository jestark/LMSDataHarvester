/* Copyright (C) 2015, 2016 James E. Stark
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
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;

/**
 * Implementation of the <code>Activity</code> interface, for
 * <code>Activity</code> instances which do not contain additional data.  It is
 * expected that instances of this class will be accessed though the
 * <code>Activity</code> interface, along with the relevant manager, and
 * builder.  See the <code>Activity</code> interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class GenericActivity extends Activity
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
			super (GenericActivity.class, GenericActivity::new);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the <code>GenericActivity</code> with null values.
	 */

	protected GenericActivity () {}

	/**
	 * Create an <code>Activity</code> from the supplied <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected GenericActivity (final Activity.Builder builder)
	{
		super (builder);
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Element</code>
	 * instance.
	 *
	 * @return The <code>DataStore</code> identifier
	 */

	@Override
	@CheckReturnValue
	public Long getId ()
	{
		return (this.getReference () != null) ? this.getReference ().getId () : null;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used to initialize the <code>DataStore</code> identifier on a new
	 * <code>GenericActivity</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final @Nullable Long id)
	{
	}

	/**
	 * Get the name of the <code>Activity</code>.  Not all
	 * <code>Activity</code> instances have names.  For those
	 * <code>Activity</code> instances which do not have names, the name of the
	 * associated <code>ActivityType</code> will be returned.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Activity</code>
	 */

	@Override
	public String getName ()
	{
		return (this.getType () != null) ? this.getType ().getName () : null;
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
		throw new UnsupportedOperationException ();
	}

	/**
	 * Get the <code>List</code> of <code>Grade</code> instances which are
	 * associated with the <code>Activity</code>.  Not all
	 * <code>Activity</code> instances are graded.  If the
	 * <code>Activity</code> does is not graded then the <code>List</code> will
	 * be empty.
	 *
	 * @return A <code>List</code> of <code>Grade</code> instances
	 */

	@Override
	public List<Grade> getGrades ()
	{
		return Collections.emptyList ();
	}

	/**
	 * Get the <code>List</code> of <code>SubActivity</code> instances
	 * associated with the <code>Actvity</code>.
	 *
	 * @return The <code>List</code> of <code>SubActivity</code> instances
	 */

	@Override
	public List<SubActivity> getSubActivities ()
	{
		return Collections.emptyList ();
	}

	/**
	 * Initialize the <code>List</code> of <code>SubActivity</code> instances
	 * for the <code>Activity</code>.  This method is intended to be used to
	 * initialize a new <code>Activity</code> instance.
	 *
	 * @param  subactivities The <code>List</code> of <code>SubActivity</code>
	 *                       instances, not null
	 */

	@Override
	protected void setSubActivities (final List<SubActivity> subactivities)
	{
		throw new UnsupportedOperationException ("Generic activities can not have sub-activities");
	}

	/**
	 * Add the specified <code>SubActivity</code> to the
	 * <code>Activity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to add, not null
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully added, <code>False</code> otherwise
	 */

	@Override
	protected boolean addSubActivity (final SubActivity subactivity)
	{
		throw new UnsupportedOperationException ("Generic activities can not have sub-activities");
	}

	/**
	 * Remove the specified <code>SubActivity</code> from the
	 * <code>Activity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to remove, not null
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully removed, <code>False</code>
	 *                     otherwise
	 */

	@Override
	protected boolean removeSubActivity (final SubActivity subactivity)
	{
		throw new UnsupportedOperationException ("Generic activities can not have sub-activities");
	}
}
