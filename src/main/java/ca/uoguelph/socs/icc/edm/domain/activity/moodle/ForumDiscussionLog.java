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

import ca.uoguelph.socs.icc.edm.domain.ActivityGroupMember;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

import ca.uoguelph.socs.icc.edm.domain.builder.AbstractNoIdElementFactory;
import ca.uoguelph.socs.icc.edm.domain.builder.LogReferenceElementFactory;

import ca.uoguelph.socs.icc.edm.domain.core.LogReference;

/**
 * Implementation of the <code>LogEntry</code> interface for logs referencing
 * the sub-activity implemented by that <code>ForumDiscussion</code> class.
 * It is expected that this class will be accessed though the
 * <code>LogEntry</code> interface, along with the relevant manager, and
 * builder.  See the <code>LogEntry</code> interface documentation for details.
 * <p>
 * This class was generated from the <code>Log</code> template, with
 * the following values:
 * <p>
 * <ul>
 * <li>ActivitySource = moodle
 * <li>ActivityType   = Forum
 * <li>ClassName      = ForumDiscussionLog
 * <li>ActivityClass  = ForumDiscussion
 * <li>HashBase       = 2083
 * <li>HashMult       = 571
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.1
 */

class ForumDiscussionLog extends LogReference
{
	private static final class Factory extends AbstractNoIdElementFactory<LogEntry> implements LogReferenceElementFactory
	{
		/**
		 * Create a new <code>LogReference</code> instance.
		 *
		 * @param  entry    The <code>LogEntry</code> which refers to the
		 *                  sub-activity, not null
		 * @param  activity The Sub-Activity (<code>ActivityGroupMember</code>) which
		 *                  is being referenced, not null
		 *
		 * @return          The new <code>LogReference</code> (as a
		 *                  <code>LogEntry</code>)
		 */

		public LogEntry create (final LogEntry entry, final ActivityGroupMember activity)
		{
			assert entry != null : "entry is NULL";
			assert activity instanceof ForumDiscussion : "activity is not an instance of ForumDiscussion";

			return new ForumDiscussionLog (entry, activity);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/**
	 * Register the <code>ForumDiscussionLog</code> with the factories on initialization.
	 */

	static
	{
		LogReference.registerLog (ForumDiscussionLog.class, new Factory ());
	}

	/**
	 * Create the <code>LogEntry</code> instance with Null values.
	 */

	public ForumDiscussionLog ()
	{
		super ();
	}

	/**
	 * Create the <code>ForumDiscussionLog</code>.
	 *
	 * @param  entry    The <code>LogEntry</code> which refers to the
	 *                  sub-activity, not null
	 * @param  activity The Sub-Activity (<code>ActivityGroupMember</code>) which
	 *                  is being referenced, not null
	 */

	public ForumDiscussionLog (final LogEntry entry, final ActivityGroupMember activity)
	{
		super (entry, activity);
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>Activity</code> instance.
	 * The hash code is computed by the superclass, with unique values added
	 * to separate the instances of <code>ForumDiscussion</code> from the other
	 * subclasses of the superclass.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		final int base = 2083;
		final int mult = 571;

		HashCodeBuilder hbuilder = new HashCodeBuilder (base, mult);
		hbuilder.appendSuper (super.hashCode ());

		return hbuilder.toHashCode ();
	}
}
