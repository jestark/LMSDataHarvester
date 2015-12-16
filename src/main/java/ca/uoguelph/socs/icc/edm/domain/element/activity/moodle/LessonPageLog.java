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

import javax.annotation.Nullable;

import com.google.auto.service.AutoService;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogReference;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;

/**
 * Implementation of the <code>LogEntry</code> interface for logs referencing
 * the <code>SubActivity</code> implemented by that
 * <code>LessonPage</code> class.  It is expected that this class will be
 * accessed though the <code>LogEntry</code> interface, along with the relevant
 * manager, and builder.  See the <code>LogEntry</code> interface documentation
 * for details.
 * <p>
 * This class was generated from the <code>Log</code> template, with
 * the following values:
 * <p>
 * <ul>
 * <li>ActivitySource    = moodle
 * <li>ActivityType      = lesson
 * <li>ClassName         = LessonPageLog
 * <li>SubActivityClass  = LessonPage
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.0
 */

class LessonPageLog extends LogReference
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
	public static final class Definition extends LogReference.Definition
	{
		/**
		 * Create the <code>Definition</code>.
		 */

		public Definition ()
		{
			super (LessonPageLog.class, LessonPageLog::new);

			LogReference.registerImplementation (LessonPage.class, LessonPageLog.class);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The associated <code>SubActivity</code> */
	private SubActivity subActivity;

	/**
	 * Create the <code>LogReference</code> instance with Null values.
	 */

	protected LessonPageLog ()
	{
		this.subActivity = null;
	}

	/**
	 * Create an <code>LogReference</code> from the supplied
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected LessonPageLog (final LogReference.Builder builder)
	{
		super (builder);

		this.subActivity = Preconditions.checkNotNull (builder.getSubActivity (), "subActivity");
	}

	/**
	 * Get the <code>SubActivity</code> upon which the logged
	 * <code>Action</code> was performed.
	 *
	 * @return A reference to the associated <code>SubActivity</code> instance,
	 *         may be null
	 */

	@Override
	public SubActivity getSubActivity ()
	{
		return this.subActivity;
	}

	/**
	 * Set the <code>SubActivity</code> upon which the logged
	 * <code>Action</code> was performed.  This method is intended to be used
	 * to initialize a new <code>LogReference</code> instance.
	 *
	 * @param  subActivity The <code>SubActivity</code>, not null
	 */

	@Override
	protected void setSubActivity (final SubActivity subActivity)
	{
		assert subActivity != null : "subactivity is NULL";

		this.subActivity = subActivity;
	}
}
