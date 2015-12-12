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

import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auto.value.AutoValue;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.Activity;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.SubActivity;

import ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Book;
import ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.BookChapter;
import ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Forum;
import ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.ForumDiscussion;
import ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.ForumPost;
import ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Lesson;
import ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.LessonPage;
import ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.Workshop;
import ca.uoguelph.socs.icc.edm.domain.element.activity.moodle.WorkshopSubmission;

public final class SubActivityConverter
{
	@AutoValue
	protected static abstract class ClassKey
	{
		public static ClassKey create (final Class<? extends Activity> cls, final String action)
		{
			return new AutoValue_SubActivityConverter_ClassKey (cls, action);
		}

		public abstract Class<? extends Activity> getActivityClass ();

		public abstract String getAction ();
	}

	@AutoValue
	protected static abstract class Key
	{
		public static Key create (final Long id, final Class<? extends SubActivity> cls)
		{
			return new AutoValue_SubActivityConverter_Key (id, cls);
		}

		public abstract Long getId ();

		public abstract Class<? extends SubActivity> getSubActivityClass ();
	}

	private static final String MISSING_SUBACTIVITY_NAME = "-=- MISSING SUBACTIVITY -=-";

	private static final Map<ClassKey, Class<? extends SubActivity>> tmpmap;

	private final Logger log;

	private final DomainModel dest;

	private final DomainModel source;

	private final Map<Key, SubActivity> cache;

	static
	{
		tmpmap = new HashMap<> ();

		tmpmap.put (ClassKey.create (Book.class, "add chapter"), BookChapter.class);
		tmpmap.put (ClassKey.create (Book.class, "print chapter"), BookChapter.class);
		tmpmap.put (ClassKey.create (Book.class, "update chapter"), BookChapter.class);
		tmpmap.put (ClassKey.create (Book.class, "view chapter"), BookChapter.class);
		tmpmap.put (ClassKey.create (Forum.class, "add discussion"), ForumDiscussion.class);
		tmpmap.put (ClassKey.create (Forum.class, "move discussion"), ForumDiscussion.class);
		tmpmap.put (ClassKey.create (Forum.class, "update discussion"), ForumDiscussion.class);
		tmpmap.put (ClassKey.create (Forum.class, "view discussion"), ForumDiscussion.class);
		tmpmap.put (ClassKey.create (Forum.class, "add post"), ForumPost.class);
		tmpmap.put (ClassKey.create (Forum.class, "update post"), ForumPost.class);
		tmpmap.put (ClassKey.create (Forum.class, "delete chapter"), ForumPost.class);
		tmpmap.put (ClassKey.create (Lesson.class, "view"), LessonPage.class);
		tmpmap.put (ClassKey.create (Workshop.class, "update example assessment"), WorkshopSubmission.class);
		tmpmap.put (ClassKey.create (Workshop.class, "add example assessment"), WorkshopSubmission.class);
		tmpmap.put (ClassKey.create (Workshop.class, "update reference assessment"), WorkshopSubmission.class);
		tmpmap.put (ClassKey.create (Workshop.class, "add reference assessment"), WorkshopSubmission.class);
		tmpmap.put (ClassKey.create (Workshop.class, "view example"), WorkshopSubmission.class);
		tmpmap.put (ClassKey.create (Workshop.class, "update example"), WorkshopSubmission.class);
		tmpmap.put (ClassKey.create (Workshop.class, "add example"), WorkshopSubmission.class);
		tmpmap.put (ClassKey.create (Workshop.class, "update assessment"), WorkshopSubmission.class);
		tmpmap.put (ClassKey.create (Workshop.class, "add assessment"), WorkshopSubmission.class);
		tmpmap.put (ClassKey.create (Workshop.class, "view submission"), WorkshopSubmission.class);
		tmpmap.put (ClassKey.create (Workshop.class, "update submission"), WorkshopSubmission.class);
		tmpmap.put (ClassKey.create (Workshop.class, "add submission"), WorkshopSubmission.class);
	}

	public SubActivityConverter (final DomainModel dest, final DomainModel source)
	{
		this.log = LoggerFactory.getLogger (SubActivityConverter.class);

		this.source = source;
		this.dest = dest;

		this.cache = new HashMap<> ();
	}

	public SubActivity convert (final Activity activity, final Action action, final String info, final String url)
	{
		this.log.trace ("convert: activity={}, action={}, info={}, url={}", activity, action, info, url);

		SubActivity subActivity = null;

		Class<? extends SubActivity> sclass = SubActivityConverter.tmpmap.get (ClassKey.create (activity.getClass (), action.getName ()));

		if (sclass != null)
		{
			Long subId = Long.valueOf (info);
			Key cacheKey = Key.create (subId, sclass);

			if (! this.cache.containsKey (cacheKey))
			{
				subActivity = this.source.getQuery (SubActivity.SELECTOR_ID, sclass)
					.setValue (SubActivity.ID, subId)
					.query ();

				if (subActivity == null)
				{
					subActivity = SubActivity.builder (this.dest, activity)
						.setName (SubActivityConverter.MISSING_SUBACTIVITY_NAME)
						.build ();

					this.log.debug ("Created entry for missing sub-activity Class: {} id: {}", sclass.getSimpleName (), subId);
				}
				else
				{
					this.log.debug ("Loaded sub-activity instance for Class: {} id: {}", sclass.getSimpleName (), subId);
				}

				this.cache.put (cacheKey, subActivity);
			}
			else
			{
				subActivity = this.cache.get (cacheKey);
			}
		}

		return subActivity;
	}
}
