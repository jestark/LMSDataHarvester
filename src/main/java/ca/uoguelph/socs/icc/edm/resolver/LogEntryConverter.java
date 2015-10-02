/* Copyright (C) 2015 James E. Stark
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

package ca.uoguelph.socs.icc.edm.resolver;

import java.util.Map;

import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.ActionBuilder;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogEntryBuilder;

import ca.uoguelph.socs.icc.edm.domain.element.MoodleLogData;

/**
 *
 */

public final class LogEntryConverter
{
	/** The log */
	private final Logger log;

	/** */
	private final DomainModel source;

	/**  */
	private final ActivityConverter activityConverter;

	/**  */
	private final EnrolmentConverter enrolmentConverter;

	/**  */
	private final NetworkConverter networkConverter;

	private final SubActivityConverter subActivityConverter;

	/**  */
	private final ActionBuilder actionBuilder;

	/**  */
	private final LogEntryBuilder builder;

	/**
	 * Create the <code>LogEntryConverter</code>
	 *
	 * @param  dest   The destination <code>DomainModel</code>, not null
	 * @param  source The source <code>DomainModel</code>, not null
	 */

	public LogEntryConverter (final DomainModel dest, final DomainModel source)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.source = source;

		this.actionBuilder = Action.builder (dest);
		this.activityConverter = new ActivityConverter (dest, source);
		this.enrolmentConverter = new EnrolmentConverter (dest, source);
		this.networkConverter = new NetworkConverter (dest);
		this.subActivityConverter = new SubActivityConverter (dest, source);

		this.builder = LogEntry.builder (dest);
	}

	/**
	 *
	 */

	public LogEntry convert (final MoodleLogData entry)
	{
		this.log.trace ("convert: entry={}", entry);

		assert entry != null : "entry is NULL";

		return this.builder.clear ()
			.setAction (this.actionBuilder.setName (entry.getActionName ())
					.build ())
			.setNetwork (this.networkConverter.convert (entry.getIPAddress ()))
			.setEnrolment (this.enrolmentConverter.convert (entry.getUserId (), entry.getCourse ()))
			.setActivity (this.activityConverter.convert (entry.getActivityId (), entry.getModule (), entry.getCourse ()))
			.setSubActivity (this.subActivityConverter.convert (this.builder.getActivity (), this.builder.getAction (), entry.getInfo (), entry.getUrl ()))
			.setTime (entry.getTime ())
			.build ();
	}
}
