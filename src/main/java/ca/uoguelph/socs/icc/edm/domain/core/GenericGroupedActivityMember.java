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

package ca.uoguelph.socs.icc.edm.domain.core;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMember;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMemberBuilder;
import ca.uoguelph.socs.icc.edm.domain.ActivityType;
import ca.uoguelph.socs.icc.edm.domain.Course;
import ca.uoguelph.socs.icc.edm.domain.Grade;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

import ca.uoguelph.socs.icc.edm.domain.builder.ActivityGroupMemberElementFactory;

/**
 * An generic representation of a sub-activity in the domain model.  This class
 * acts as an abstract base class for all of the sub-activities by implementing
 * the <code>ActivityGroupMember</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class GenericGroupedActivityMember extends AbstractActivity implements ActivityGroupMember, Serializable
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The name of the activity */
	private String name;

	/** The sub-activity's parent activity */
	private Activity parent;

	/**
	 * Register the activity with the <code>ActivityDataMap</code> and the
	 * factories.  This method handles the registrations for the subclasses to
	 * reduce code duplication.
	 *
	 * @param  elementImpl The implementation class, not null
	 * @param  parent      The parent <code>Activity</code> class, not null
	 * @param  builder     The <code>ActivityGroupMemberBuilder</code>
	 *                     implementation, not null
	 * @param  factory     The <code>ElementFactory</code> interface, not null
	 * @param  factoryImpl The <code>ElementFactory</code>, not null
	 */

	protected static final <S extends ActivityGroupMember, T extends ActivityGroupMemberBuilder, U extends ActivityGroupMemberElementFactory> void registerActivity (final Class<S> elementImpl, final Class<? extends Activity> parent, final Class<T> builder, final Class<U> factory, final U factoryImpl)
	{
		assert elementImpl != null : "elementImpl is NULL";
		assert parent != null : "parent Class is NULL";
		assert builder != null : "builder is NULL";
		assert factory != null : "factory is NULL";
		assert factoryImpl != null : "factoryImpl is NULL";

		AbstractActivity.registerRelationship (parent, elementImpl);

		AbstractElement.registerBuilder (Activity.class, elementImpl, builder);
		AbstractElement.registerFactory (Activity.class, elementImpl, factory, factoryImpl);
	}

	/**
	 * Create the <code>Activity</code> with null values.
	 */

	public GenericGroupedActivityMember ()
	{
		super ();
		this.name = null;
		this.parent = null;
	}

	/**
	 * Create the <code>Activity</code>
	 *
	 * @param  parent The <code>Activity</code> containing this
	 *                <code>Activity</code> instance
	 * @param  name   The name of the <code>Activity</code>
	 */

	public GenericGroupedActivityMember (final Activity parent, final String name)
	{
		super ();

		assert parent != null : "Parent element is null";
		assert name != null : "name is null";

		this.parent = parent;
		this.name = name;
	}

	/**
	 * Compare two <code>Activity</code> instances to determine if they are
	 * equal.  The <code>Activity</code> instances are compared based upon the
	 * data contained in the superclass and the parent <code>Activity</code>.
	 *
	 * @param  obj The <code>Activity</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>Activity</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		boolean result = false;

		if (obj == this)
		{
			result = true;
		}
		else if (obj instanceof GenericGroupedActivityMember)
		{
			EqualsBuilder ebuilder = new EqualsBuilder ();
			ebuilder.append (this.name, ((GenericGroupedActivityMember) obj).name);
			ebuilder.append (this.parent, ((GenericGroupedActivityMember) obj).parent);

			result = ebuilder.isEquals ();
		}

		return result;
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Activity</code> instance.
	 * The hash code is computed based upon the data contained in the superclass
	 * and the parent <code>Activity</code>.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 1031;
		final int mult = 971;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.append (this.name);
		hbuilder.append (this.parent);

		return hbuilder.toHashCode ();
	}

	/**
	 * Get the <code>Course</code> with which the <code>Activity</code> is
	 * associated.
	 *
	 * @return The <code>Course</code> instance
	 */

	@Override
	public Course getCourse ()
	{
		return this.parent.getCourse ();
	}

	/**
	 * Get the <code>ActivityType</code> for the <code>Activity</code>.
	 *
	 * @return The <code>ActivityType</code> instance
	 */

	@Override
	public ActivityType getType ()
	{
		return this.parent.getType ();
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
	 * Set the name of the <code>Activity</code>.  This method is intended to be
	 * used by a <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  name The name of the <code>Activity</code>, not null
	 */

	protected void setName (final String name)
	{
		assert name != null : "name is NULL";

		this.name = name;
	}

	/**
	 * Get the <code>Set</code> of <code>Grade</code> instances which are
	 * associated with the <code>Activity</code>.  Not all <code>Activity</code>
	 * instances are graded.  If the <code>Activity</code> does is not graded
	 * then the <code>Set</code> will be empty.
	 *
	 * @return A <code>Set</code> of <code>Grade</code> instances
	 */

	@Override
	public Set<Grade> getGrades ()
	{
		return this.parent.getGrades ();
	}

	/**
	 * Get the parent <code>Activity</code> instance for the sub-activity.
	 *
	 * @return The parent <code>Activity</code>
	 */

	@Override
	public Activity getParent ()
	{
		return this.parent;
	}

	/**
	 * Set the <code>Activity</code> instance which contains the
	 * sub-activity.  This method is intended to be used by a 
	 * <code>DataStore</code> when the <code>Activity</code> instance is
	 * loaded.
	 *
	 * @param  parent The <code>Activity</code> containing this
	 *                <code>Activity</code> instance
	 */

	protected void setParent (final Activity parent)
	{
		assert parent != null : "parent is NULL";

		this.parent = parent;
	}

	/**
	 * Get a <code>String</code> representation of the <code>Activity</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>Activity</code>
	 *         instance
	 */

	@Override
	public String toString()
	{
		ToStringBuilder builder = new ToStringBuilder (this);

		builder.append ("name", this.name);
		builder.append ("parent", this.parent);

		return builder.toString ();
	}
}
