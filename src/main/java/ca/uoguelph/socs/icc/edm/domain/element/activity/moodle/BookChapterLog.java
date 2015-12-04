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

import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;
import ca.uoguelph.socs.icc.edm.domain.LogEntry;
import ca.uoguelph.socs.icc.edm.domain.LogReference;
import ca.uoguelph.socs.icc.edm.domain.datastore.Retriever;

/**
 * Implementation of the <code>LogEntry</code> interface for logs referencing
 * the <code>SubActivity</code> implemented by that
 * <code>BookChapter</code> class.  It is expected that this class will be
 * accessed though the <code>LogEntry</code> interface, along with the relevant
 * manager, and builder.  See the <code>LogEntry</code> interface documentation
 * for details.
 * <p>
 * This class was generated from the <code>Log</code> template, with
 * the following values:
 * <p>
 * <ul>
 * <li>ActivitySource    = moodle
 * <li>ActivityType      = book
 * <li>ClassName         = BookChapterLog
 * <li>SubActivityClass  = BookChapter
 * </ul>
 *
 * @author  James E. Stark
 * @version 1.0
 */

class BookChapterLog extends LogReference
{
	/**
	 * <code>Builder</code> for <code>BookChapterLog</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 * @see     ca.uoguelph.socs.icc.edm.domain.LogReference.Builder
	 */

	public static final class Builder extends LogReference.Builder
	{
		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model                The <code>DomainModel</code>, not null
		 * @param  refRetriever         <code>Retriever</code> for
		 *                              <code>LogReference</code> instances, not
		 *                              null
		 * @param  entryRetriever       <code>Retriever</code> for
		 *                              <code>ActivitySource</code> instances,
		 *                              not null
		 * @param  subActivityRetriever <code>Retriever</code> for
		 *                              <code>ActivitySource</code> instances,
		 *                              not null
		 */

		private Builder (
				final DomainModel model,
				final Retriever<LogReference> refRetriever,
				final Retriever<LogEntry> entryRetriever,
				final Retriever<SubActivity> subActivityRetriever)
		{
			super (model, refRetriever, entryRetriever, subActivityRetriever);
		}

		/**
		 * Create an instance of the <code>BookChapterLog</code>.
		 *
		 * @param  reference The previously existing <code>LogReference</code>
		 *                   instance, may be null
		 * @return           The new <code>LogReference</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected LogReference create (final @Nullable LogReference reference)
		{
			this.log.trace ("create: reference={}", reference);

			return new BookChapterLog (this);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The associated <code>SubActivity</code> */
	private SubActivity subActivity;

	/**
	 * Register the <code>BookChapterLog</code> with the factories on
	 * initialization.
	 */

	static
	{
		LogReference.registerImplementation (BookChapter.class, BookChapterLog.class);
	}

	/**
	 * Create the <code>LogReference</code> instance with Null values.
	 */

	protected BookChapterLog ()
	{
		this.subActivity = null;
	}

	/**
	 * Create an <code>LogReference</code> from the supplied
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected BookChapterLog (final Builder builder)
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
