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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.List;

import dagger.Module;

import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;

/**
 * Common base class for <code>Activity</code> and <code>SubActivity</code>.
 * While most <code>SubActivity</code> implementations will have an
 * <code>Activity</code> as the parent, some will have a
 * <code>SubActivity</code> as the parent.  This class exists to abstract the
 * difference between <code>Activity</code> and <code>SubActivity</code> such
 * that <code>SubActivity</code> can point to it as the parent.  Otherwise,
 * there would need to be multiple <code>SubActivity</code> interface classes to
 * represent the different levels of nesting.
 * <p>
 * This class should only be reference in the context of a
 * <code>SubActivity</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class ParentActivity extends Element
{
	/**
	 * Dagger module for creating <code>Retriever</code> instances.  This module
	 * contains implementation-independent information.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module
	public static final class ParentActivityModule extends Element.ElementModule<ParentActivity> {}

	/** The <code>MetaData</code> for the <code>SubActivity</code> */
	protected static final MetaData<ParentActivity> METADATA;

	/** The <code>SubActivity</code> instances for the <code>SubActivity</code> */
	public static final Property<ParentActivity, SubActivity> SUBACTIVITIES;

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>ParentActivity</code>.
	 */

	static
	{
		SUBACTIVITIES = Property.of (ParentActivity.class, SubActivity.class, "subactivities",
				ParentActivity::getSubActivities, ParentActivity::addSubActivity, ParentActivity::removeSubActivity,
				Property.Flags.RECOMMENDED);

		METADATA = MetaData.builder (ParentActivity.class)
			.addDependency (ActivityReference.METADATA)
			.addProperty (SUBACTIVITIES)
			.build ();
	}

	/**
	 * Create the <code>ParentActivity</code>
	 */

	protected ParentActivity ()
	{
		super ();
	}

	/**
	 * Create the <code>ParentActivity</code> instance from the specified
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected ParentActivity (final Element.Builder<? extends ParentActivity> builder)
	{
		super (builder);
	}

	/**
	 * Get the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 *
	 * @return The <code>Course</code> instance
	 */

	public abstract Course getCourse ();

	/**
	 * Get the name of the <code>Activity</code>.  Not all
	 * <code>Activity</code> instances have names.  For those
	 * <code>Activity</code> instances which do not have names, the name of the
	 * associated <code>ActivityType</code> will be returned.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>Activity</code>
	 */

	public abstract String getName();

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	public abstract ActivityType getType();

	/**
	 * Get the <code>List</code> of <code>Grade</code> instances which are
	 * associated with the <code>Activity</code>.  Not all
	 * <code>Activity</code> instances are graded.  If the
	 * <code>Activity</code> does is not graded then the <code>List</code> will
	 * be empty.
	 *
	 * @return A <code>List</code> of <code>Grade</code> instances
	 */

	public abstract List<Grade> getGrades ();

	/**
	 * Get a <code>List</code> of all of the <code>LogEntry</code> instances
	 * which act upon the <code>Activity</code>.
	 *
	 * @return A <code>List</code> of <code>LogEntry</code> instances
	 */

	public abstract List<LogEntry> getLog ();

	/**
	 * Get the <code>List</code> of <code>SubActivity</code> instances
	 * associated with the <code>Actvity</code>.
	 *
	 * @return The <code>List</code> of <code>SubActivity</code> instances
	 */

	public abstract List<SubActivity> getSubActivities ();

		/**
	 * Initialize the <code>List</code> of <code>SubActivity</code> instances
	 * for the <code>Activity</code>.  This method is intended to be used to
	 * initialize a new <code>Activity</code> instance.
	 *
	 * @param  subactivities The <code>List</code> of <code>SubActivity</code>
	 *                       instances, not null
	 */

	protected abstract void setSubActivities (List<SubActivity> subactivities);

	/**
	 * Add the specified <code>SubActivity</code> to the
	 * <code>Activity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to add, not null
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully added, <code>False</code> otherwise
	 */

	protected abstract boolean addSubActivity (SubActivity subactivity);

	/**
	 * Remove the specified <code>SubActivity</code> from the
	 * <code>Activity</code>.
	 *
	 * @param  subactivity The <code>SubActivity</code> to remove, not null
	 * @return             <code>True</code> if the <code>SubActivity</code>
	 *                     was successfully removed, <code>False</code>
	 *                     otherwise
	 */

	protected abstract boolean removeSubActivity (SubActivity subactivity);
}
