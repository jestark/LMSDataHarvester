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
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.collections4.keyvalue.MultiKey;

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
	private static final String MISSING_SUBACTIVITY_NAME = "-=- MISSING SUBACTIVITY -=-";

	private static final Map<MultiKey<Object>, Class<? extends SubActivity>> tmpmap;

	private final Logger log;

	private final DomainModel dest;

	private final DomainModel source;

	private final Map<MultiKey<Object>, SubActivity> cache;

	static
	{
		tmpmap = new HashMap<> ();

		tmpmap.put (new MultiKey<Object> (Book.class, "add chapter"), BookChapter.class);
		tmpmap.put (new MultiKey<Object> (Book.class, "print chapter"), BookChapter.class);
		tmpmap.put (new MultiKey<Object> (Book.class, "update chapter"), BookChapter.class);
		tmpmap.put (new MultiKey<Object> (Book.class, "view chapter"), BookChapter.class);
		tmpmap.put (new MultiKey<Object> (Forum.class, "add discussion"), ForumDiscussion.class);
		tmpmap.put (new MultiKey<Object> (Forum.class, "move discussion"), ForumDiscussion.class);
		tmpmap.put (new MultiKey<Object> (Forum.class, "update discussion"), ForumDiscussion.class);
		tmpmap.put (new MultiKey<Object> (Forum.class, "view discussion"), ForumDiscussion.class);
		tmpmap.put (new MultiKey<Object> (Forum.class, "add post"), ForumPost.class);
		tmpmap.put (new MultiKey<Object> (Forum.class, "update post"), ForumPost.class);
		tmpmap.put (new MultiKey<Object> (Forum.class, "delete chapter"), ForumPost.class);
		tmpmap.put (new MultiKey<Object> (Lesson.class, "view"), LessonPage.class);
		tmpmap.put (new MultiKey<Object> (Workshop.class, "update example assessment"), WorkshopSubmission.class);
		tmpmap.put (new MultiKey<Object> (Workshop.class, "add example assessment"), WorkshopSubmission.class);
		tmpmap.put (new MultiKey<Object> (Workshop.class, "update reference assessment"), WorkshopSubmission.class);
		tmpmap.put (new MultiKey<Object> (Workshop.class, "add reference assessment"), WorkshopSubmission.class);
		tmpmap.put (new MultiKey<Object> (Workshop.class, "view example"), WorkshopSubmission.class);
		tmpmap.put (new MultiKey<Object> (Workshop.class, "update example"), WorkshopSubmission.class);
		tmpmap.put (new MultiKey<Object> (Workshop.class, "add example"), WorkshopSubmission.class);
		tmpmap.put (new MultiKey<Object> (Workshop.class, "update assessment"), WorkshopSubmission.class);
		tmpmap.put (new MultiKey<Object> (Workshop.class, "add assessment"), WorkshopSubmission.class);
		tmpmap.put (new MultiKey<Object> (Workshop.class, "view submission"), WorkshopSubmission.class);
		tmpmap.put (new MultiKey<Object> (Workshop.class, "update submission"), WorkshopSubmission.class);
		tmpmap.put (new MultiKey<Object> (Workshop.class, "add submission"), WorkshopSubmission.class);
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

		Class<? extends SubActivity> sclass = SubActivityConverter.tmpmap.get (new MultiKey<Object> (activity.getClass (), action.getName ()));

		if (sclass != null)
		{
			Long subId = Long.valueOf (info);
			MultiKey<Object> cacheKey = new MultiKey<Object> (sclass, subId);

			if (! this.cache.containsKey (cacheKey))
			{
				subActivity = this.source.getQuery (SubActivity.class, sclass, SubActivity.SELECTOR_ID)
					.setValue (SubActivity.ID, subId)
					.query ();

				if (subActivity == null)
				{
					subActivity = SubActivity.builder (this.dest, activity)
						.setName (SubActivityConverter.MISSING_SUBACTIVITY_NAME)
						.build ();

					this.log.info ("Created entry for missing sub-activity Class: {} id: {}", sclass.getSimpleName (), subId);
				}
				else
				{
					this.log.info ("Loaded sub-activity instance for Class: {} id: {}", sclass.getSimpleName (), subId);
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
