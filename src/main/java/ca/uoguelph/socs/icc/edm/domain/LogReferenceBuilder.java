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

package ca.uoguelph.socs.icc.edm.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.DataStore;

/**
 * Create new <code>LogReference</code> instances.  This is an internal class
 * used to create <code>LogReference</code> instances.  Generally, it should
 * only be used by the <code>LogEntryBuilder</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     LogReference
 */

public final class LogReferenceBuilder implements Builder<LogReference>
{
	/** The Logger */
	private final Logger log;

	/** The <code>DataStore</code> */
	private final DataStore datastore;

	/** Helper to substitute <code>LogEntry</code> instances*/
	private final DataStoreProxy<LogEntry> entryProxy;

	/** Helper to operate on <code>LogReference</code> instances */
	private final DataStoreProxy<LogReference> referenceProxy;

	/** The associated <code>LogEnty</code>*/
	private LogEntry entry;

	/** The associated <code>SubActivity</code>*/
	private SubActivity subActivity;

	/**
	 * Create the <code>LogReferenceBuilder</code>.
	 *
	 * @param  datastore The <code>DataStore</code>, not null
	 */

	protected LogReferenceBuilder (final DataStore datastore)
	{
		this.log = LoggerFactory.getLogger (this.getClass ());

		this.datastore = datastore;

		this.entryProxy = DataStoreProxy.getInstance (LogEntry.class, LogEntry.SELECTOR_ID, datastore);
		this.referenceProxy = DataStoreProxy.getInstance (LogReference.class, LogReference.SELECTOR_ENTRY, datastore);

		this.entry = null;
		this.subActivity = null;
	}

	/**
	 * Create an instance of the <code>LogReference</code>.
	 *
	 * @return                       The new <code>LogReference</code> instance
	 * @throws IllegalStateException If any if the fields is missing
	 * @throws IllegalStateException If there isn't an active transaction
	 */

	@Override
	public LogReference build ()
	{
		this.log.trace ("build:");

		if (this.entry == null)
		{
			this.log.error ("Attempting to create an LogReference without a LogEntry");
			throw new IllegalStateException ("entry is NULL");
		}

		if (this.subActivity == null)
		{
			this.log.error ("Attempting to create an LogReference without a SubActivity");
			throw new IllegalStateException ("subActivity is NULL");
		}

		LogReference result = this.referenceProxy.create ();
		result.setEntry (this.entry);
		result.setSubActivity (this.subActivity);

		return this.referenceProxy.insert (null, result);
	}

	/**
	 * Reset the builder.  This method will set all of the fields for the
	 * <code>LogReference</code> to be built to <code>null</code>.
	 *
	 * @return This <code>LogReferenceBuilder</code>
	 */

	public LogReferenceBuilder clear ()
	{
		this.log.trace ("clear:");

		this.entry = null;
		this.subActivity = null;

		return this;
	}

	/**
	 * Load a <code>LogReference</code> instance into the builder.  This method
	 * resets the builder and initializes all of its parameters from
	 * the specified <code>LogReference</code> instance.  The  parameters are
	 * validated as they are set.
	 *
	 * @param  entry                    The <code>LogReference</code>, not null
	 *
	 * @return                          This <code>LogReferenceBuilder</code>
	 * @throws IllegalArgumentException If any of the fields in the
	 *                                  <code>LogReference</code> instance to
	 *                                  be loaded are not valid
	 */

	public LogReferenceBuilder load (final LogReference reference)
	{
		this.log.trace ("load: reference={}", reference);

		if (reference == null)
		{
			this.log.error ("Attempting to load a NULL LogReference");
			throw new NullPointerException ();
		}

		this.setEntry (reference.getEntry ());
		this.setSubActivity (reference.getSubActivity ());

		return this;
	}

	/**
	 * Get the associated <code>LogEntry</code>.
	 *
	 * @return The associated <code>LogEntry</code>
	 */

	public LogEntry getEntry ()
	{
		return this.entry;
	}

	/**
	 * Set the associated <code>LogEntry</code>.
	 *
	 * @param  entry                    The <code>LogEntry</code>, not null
	 *
	 * @return                          This <code>LogReferenceBuilder</code>
	 * @throws IllegalArgumentException if the <code>LogEntry</code> is not in
	 *                                  the <code>DataStore</code>
	 */

	public LogReferenceBuilder setEntry (final LogEntry entry)
	{
		this.log.trace ("setEntry: entry={}", entry);

		if (entry == null)
		{
			this.log.error ("entry is NULL");
			throw new NullPointerException ("entry is NULL");
		}

		this.entry = this.entryProxy.fetch (entry);

		if (this.entry == null)
		{
			this.log.error ("The specified LogEntry does not exist in the DataStore");
			throw new IllegalArgumentException ("LogEntry is not in the DataStore");
		}

		return this;
	}

	/**
	 * Get the associated <code>SubActivity</code>.
	 *
	 * @return The associated <code>SubActivity</code>
	 */

	public SubActivity getSubActivity ()
	{
		return this.subActivity;
	}

	/**
	 * Set the referenced <code>SubActivity</code>.
	 *
	 * @param  subActivity              The <code>SubActivity</code>, not null
	 *
	 * @return                          This <code>LogReferenceBuilder</code>
	 * @throws IllegalArgumentException if the <code>SubActivity</code> is not
	 *                                  in the <code>DataStore</code>
	 */

	public LogReferenceBuilder setSubActivity (final SubActivity subActivity)
	{
		this.log.trace ("setSubActivity: subActivity={}", subActivity);

		if (subActivity != null)
		{
			this.log.error ("subActivity is NULL");
			throw new NullPointerException ("subActivity is NULL");
		}

		this.subActivity = DataStoreProxy.getInstance (SubActivity.class,
				subActivity.getClass (),
				SubActivity.SELECTOR_ID,
				datastore)
			.fetch (subActivity);

		if (this.subActivity == null)
		{
			this.log.error ("The specified SubActivity does not exist in the DataStore");
			throw new IllegalArgumentException ("SubActivity is not in the DataStore");
		}

		return this;
	}
}
