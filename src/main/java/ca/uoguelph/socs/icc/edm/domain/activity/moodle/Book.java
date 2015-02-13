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

package ca.uoguelph.socs.icc.edm.domain.activity.moodle;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroup;
import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMember;

import ca.uoguelph.socs.icc.edm.domain.builder.AbstractNoIdElementFactory;
import ca.uoguelph.socs.icc.edm.domain.builder.ActivityGroupElementFactory;
import ca.uoguelph.socs.icc.edm.domain.builder.NamedActivityElementFactory;
import ca.uoguelph.socs.icc.edm.domain.builder.DefaultNamedActivityBuilder;

import ca.uoguelph.socs.icc.edm.domain.core.GenericGroupedActivity;
import ca.uoguelph.socs.icc.edm.domain.core.GenericNamedActivity;

/**
 * Implementation of the <code>Activity</code> interface for the moodle/Book
 * <code>ActivitySource</code>/<code>ActivityType</code>.  It is expected that
 * this class will be accessed though the <code>ActivityGroup</code> interface,
 * along with the relevant manager, and builder.  See the
 * <code>ActivityGroup</code> interface documentation for details.
 * <p>
 * This class was generated from the <code>GroupedActivity</code> template, with
 * the following values:
 * <p>
 * <ul>
 * <li>ActivitySource = moodle
 * <li>ActivityType   = Book
 * <li>ClassName      = Book
 * <li>ChildClass     = BookChapter
 * <li>Builder        = DefaultNamedActivityBuilder
 * <li>HashBase       = 2003
 * <li>HashMult       = 691
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.1
 */

public class Book extends GenericGroupedActivity
{
	/**
	 * Implementation of the <code>NamedActivityElementFactory</code>.  Allows the
	 * builders to create instances of <code>Book</code>.
	 */

	private static final class Factory extends AbstractNoIdElementFactory<Activity> implements NamedActivityElementFactory, ActivityGroupElementFactory
	{
		/**
		 * Create a new <code>Activity</code> instance.
		 *
		 * @param  instance The <code>Activity</code> containing the instance data,
		 *                  not null
		 * @param  name     The name of the <code>Activity</code>, not null
		 *
		 * @return          The new <code>Activity</code> instance
		 */

		public Activity create (Activity instance, String name)
		{
			return new Book (instance, name);
		}

		/**
		 * Add the specified <code>ActivityGroupMember</code> to the specified
		 * <code>ActivityGroup</code>.
		 *
		 * @param  group  The <code>ActivityGroup</code> to which the
		 *                <code>ActivityGroupMember</code> is to be added, not null
		 * @param  member The <code>ActivityGroupMember</code> to add to the
		 *                <code>ActivityGroup</code>, not null
		 *
		 * @return        <code>True</code> if the <code>ActivityGroupMember</code>
		 *                was successfully added to the <code>ActvityGroup</code>,
		 *                <code>False</code> otherwise
		 */

		public boolean addChild (ActivityGroup group, ActivityGroupMember member)
		{
			if (! (member instanceof BookChapter))
			{
				throw new IllegalArgumentException ("Child is not a member of BookChapter");
			}

			return ((Book) group).addChild (member);
		}

		/**
		 * Remove the specified <code>ActivityGroupMember</code> from the specified
		 * <code>ActivityGroup</code>.
		 *
		 * @param  group  The <code>ActivityGroup</code> from which the
		 *                <code>ActivityGroupMember</code> is to be removed, not null
		 * @param  member The <code>ActivityGroupMember</code> to remove from the
		 *                <code>ActivityGroup</code>, not null
		 *
		 * @return        <code>True</code> if the <code>ActivityGroupMember</code>
		 *                was successfully removed from the <code>ActvityGroup</code>,
		 *                <code>False</code> otherwise
		 */

		public boolean removeChild (ActivityGroup group, ActivityGroupMember member)
		{
			if (! (member instanceof BookChapter))
			{
				throw new IllegalArgumentException ("Child is not a member of BookChapter");
			}

			return ((Book) group).removeChild (member);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/**
	 * Register the <code>Book</code> with the factories on initialization.
	 */

	static
	{
		GenericNamedActivity.registerActivity (Book.class, DefaultNamedActivityBuilder.class, new Factory (), "moodle", "Book");
	}

	/**
	 * Create the <code>Activity</code> instance with Null values.
	 */

	public Book ()
	{
		super ();
	}

	/**
	 * Create a new <code>Activity</code> instance.
	 *
	 * @param  instance The <code>Activity</code> containing the instance data,
	 *                  not null
	 * @param  name     The name of the <code>Activity</code>, not null
	 *
	 * @return          The new <code>Activity</code> instance
	 */

	public Book (Activity instance, String name)
	{
		super (instance, name);
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Activity</code> instance.
	 * The hash code is computed by the superclass, with unique values added
	 * to separate the instances of <code>Book</code> from the other
	 * subclasses of the superclass.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 2003;
		final int mult = 691;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}

