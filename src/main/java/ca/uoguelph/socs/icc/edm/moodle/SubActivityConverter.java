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

package ca.uoguelph.socs.icc.edm.moodle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;
import ca.uoguelph.socs.icc.edm.domain.element.MoodleLogData;

/**
 * Process <code>SubActivity</code> instances which are associated with
 * <code>MoodleLogData</code> instances.  This class is responsible for
 * determining if a <code>MoodleLogData</code> instance has an associated
 * <code>SubActivity</code> instance and importing that <code>SubActivity</code>
 * instance into the destination <code>DomainModel</code>.  To ensure integrity
 * and consistency on the destination <code>DataStore</code>, if a nonexistent
 * <code>SubActivity</code> instance is referenced in the
 * <code>MoodleLogData</code>, then a placeholder will be created.
 * <p>
 * A cache of all of the loaded and created <code>SubActivity</code> instances
 * is kept by this class to make sure that duplicates placeholders are not
 * created for missing <code>SubActivity</code> instances.  The cache is indexed
 * using the <code>DataStore</code> ID of the <code>SubActivity</code> from the
 * source <code>DataStore</code> and the <code>SubActivity</code> implementation
 * class.
 *
 * @author  James E. Stark
 * @version 1.0
 * @see     Matcher
 */

public final class SubActivityConverter
{
	/**
	 * Key for the cache of <code>SubActivity</code> instances.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@AutoValue
	protected static abstract class Key
	{
		/**
		 * Create the <code>Key</code>.
		 *
		 * @param  id          The <code>DataStore</code> ID, not null
		 * @param  subActivity The <code>SubActivity</code> class, not null
		 */

		public static Key create (final Long id, final Class<? extends SubActivity> subActivity)
		{
			assert id != null : "is is NULL";
			assert subActivity != null : "subActivity is NULL";

			return new AutoValue_SubActivityConverter_Key (id, subActivity);
		}

		/**
		 * Get the <code>DataStore</code> ID of the <code>SubActivity</code>.
		 *
		 * @return The <code>DataStore</code> ID
		 */

		public abstract Long getId ();

		/**
		 * Get the <code>SubActivity</code> class.
		 *
		 * @return The <code>SubActivity</code> implementation class
		 */

		public abstract Class<? extends SubActivity> getSubActivityClass ();
	}

	/** The name of the given to missing <code>SubActivity</code> instances */
	private static final String MISSING_SUBACTIVITY_NAME = "-=- MISSING SUBACTIVITY -=-";

	/** The log */
	private final Logger log;

	/** <code>Activity</code> to <code>SubActivity</code> mapping */
	private final Map<Class<? extends Activity>, List<Matcher>> subActivities;

	/** Cache of <code>SubActivity</code> instances */
	private final Map<Key, SubActivity> subActivityCache;

	/** The source <code>DomainModel</code> */
	private final DomainModel source;

	/** The destination <code>DomainModel</code> */
	private final DomainModel dest;

	/**
	 * Create the <code>SubActivityConverter</code>.
	 *
	 * @param  dest          The destination <code>DomainModel</code>, not null
	 * @param  source        The source <code>DomainModel</code>, not null
	 * @param  subActivities <code>Activity</code> to <code>SubActivity</code>
	 *                       mapping, not null
	 */

	SubActivityConverter (
			final DomainModel dest,
			final DomainModel source,
			final Map<Class<? extends Activity>, List<Matcher>> subActivities)
	{
		assert dest != null : "dest is null";
		assert source != null : "source is NULL";
		assert subActivities != null : "subActivities is NULL";

		this.log = LoggerFactory.getLogger (this.getClass ());

		this.dest = dest;
		this.source = source;
		this.subActivities = ImmutableMap.copyOf (subActivities);

		this.subActivityCache = new HashMap<> ();
	}

	/**
	 * Import a <code>SubActivity</code> instance into the destination
	 * <code>DomainModel</code>.
	 *
	 * @param  subActivity The <code>SubActivity</code>, not null
	 * @return             The <code>SubActivity</code>
	 */

	private SubActivity importSubActivity (final SubActivity subActivity)
	{
		this.log.debug ("Loaded sub-activity instance: {}", subActivity);

		assert subActivity != null : "subActivity is NULL";

		return subActivity.getBuilder (this.dest)
			.build ();
	}

	/**
	 * Create a placeholder <code>SubActivity</code> instance.  This method
	 * creates placeholders for missing <code>SubActivity</code> instances to
	 * ensure log consistency.
	 *
	 * @param  parent The parent <code>Activity</code>, not null
	 * @return        The <code>SubActivity</code>
	 */

	private SubActivity createSubActivity (final Activity parent)
	{
		this.log.debug ("Create entry for missing sub-activity Class: {} id: {}");

		assert parent != null : "parent is NULL";

		return SubActivity.builder (this.dest, parent)
			.setName (SubActivityConverter.MISSING_SUBACTIVITY_NAME)
			.build ();
	}

	/**
	 * Load the <code>SubActivity</code> instance.  This method loads the
	 * <code>SubActivity</code> instance, identified by the <code>Key</code> and
	 * parent <code>Activity</code>, from the source <code>DataStore</code> and
	 * copies it to the destination <code>DataStore</code> before returning a
	 * reference to it.  If the <code>SubActivity</code> instance does not exist
	 * in the source <code>DataStore</code> then a placeholder will be created
	 * on the destination <code>DataStore</code>.  All <code>SubActivity</code>
	 * instances created on the destination <code>DataStore</code> are cached.
	 * On subsequent calls for the same <code>SubActivity</code>, the cached
	 * version will be returned.
	 *
	 * @param  key    The <code>Key</code>, not null
	 * @param  parent The parent <code>Activity</code>, not null
	 * @return        The <code>SubActivity</code>
	 */

	private SubActivity loadSubActivity (final Key key, final Activity parent)
	{
		this.log.trace ("getSubActivity: key={}, parent={}", key, parent);

		assert key != null : "key is NULL";
		assert parent != null : "parent is NULL";

		if (! this.subActivityCache.containsKey (key))
		{
			this.subActivityCache.put (key, this.source.getQuery (SubActivity.SELECTOR_ID, key.getSubActivityClass ())
				.setValue (SubActivity.ID, key.getId ())
				.query ()
				.map (x -> this.importSubActivity (x))
				.orElseGet (() -> this.createSubActivity (parent)));
		}

		return this.subActivityCache.get (key);
	}

	/**
	 * Get the <code>SubActivity</code> associated with the specified
	 * <code>MoodleLogData</code> instance.
	 *
	 * @param  activity The parent <code>Activity</code>, not null
	 * @param  entry    The <code>MoodleLogData</code> to process, not null
	 * @return          An <code>Optional</code> containing the
	 *                  <code>SubActivity</code>
	 */

	public Optional<SubActivity> getSubActivity (final Activity activity, final MoodleLogData entry)
	{
		this.log.trace ("getSubActivity: activity={}, entry={}", activity, entry);

		return Optional.ofNullable (this.subActivities.get (activity.getClass ()))
			.flatMap (x -> x.stream ()
					.filter (m -> m.matches (entry))
					.findAny ())
			.map (Matcher::getSubActivityClass)
			.map (x -> Key.create (Long.valueOf (entry.getInfo ()), x))
			.map (x -> this.loadSubActivity (x, activity));
	}
}
