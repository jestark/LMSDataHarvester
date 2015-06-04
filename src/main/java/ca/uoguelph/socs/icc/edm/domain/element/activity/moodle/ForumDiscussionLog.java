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

import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uoguelph.socs.icc.edm.domain.SubActivity;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;

import ca.uoguelph.socs.icc.edm.domain.builder.DefaultLogEntryBuilder;

import ca.uoguelph.socs.icc.edm.domain.element.AbstractElement;
import ca.uoguelph.socs.icc.edm.domain.element.LogReference;

import ca.uoguelph.socs.icc.edm.domain.element.metadata.DefinitionBuilder;

/**
 * Implementation of the <code>LogEntry</code> interface for logs referencing
 * the <code>SubActivity</code> implemented by that
 * <code>ForumDiscussion</code> class.  It is expected that this class will be
 * accessed though the <code>LogEntry</code> interface, along with the relevant
 * manager, and builder.  See the <code>LogEntry</code> interface documentation
 * for details.
 * <p>
 * This class was generated from the <code>Log</code> template, with
 * the following values:
 * <p>
 * <ul>
 * <li>ActivitySource = moodle
 * <li>ActivityType   = forum
 * <li>ClassName      = ForumDiscussionLog
 * <li>ActivityClass  = ForumDiscussion
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.1
 */

class ForumDiscussionLog extends LogReference
{
	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/**
	 * Register the <code>ForumDiscussionLog</code> with the factories on
	 * initialization.
	 */

	static
	{
		DefinitionBuilder<ForumDiscussionLog, LogEntry.Properties> builder = DefinitionBuilder.newInstance (LogEntry.class, ForumDiscussionLog.class, LogEntry.Properties.class);
		builder.setCreateMethod (ForumDiscussionLog::new);

//		builder.addAttribute (LogEntry.Properties.ENTRY, LogEntry.class, true, false, ForumDiscussionLog::getEntry, ForumDiscussionLog::setEntry);
		builder.addAttribute (LogEntry.Properties.SUBACTIVITY, SubActivity.class, true, false, ForumDiscussionLog::getSubActivity, ForumDiscussionLog::setSubActivity);

		AbstractElement.registerElement (builder.build (), DefaultLogEntryBuilder.class);
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
	 * @param  entry       The <code>LogEntry</code> which refers to the
	 *                     <code>SubActivity</code>, not null
	 * @param  subactivity The <code>SubActivity</code> which is being
	 *                     referenced, not null
	 */

	public ForumDiscussionLog (final LogEntry entry, final SubActivity subactivity)
	{
		super (entry, subactivity);
	}
}
