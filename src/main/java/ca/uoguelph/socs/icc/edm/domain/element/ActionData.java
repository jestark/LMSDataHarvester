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

package ca.uoguelph.socs.icc.edm.domain.element;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.Action;
import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.datastore.Retriever;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.IdGenerator;

/**
 * Implementation of the <code>Action</code> interface.  It is expected that
 * instances of this class will be accessed though the <code>Action</code>
 * interface, along with the relevant manager, and builder.  See the
 * <code>Action</code> interface documentation for details.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public class ActionData extends Action
{
	/**
	 * <code>Builder</code> for <code>ActionData</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 * @see     ca.uoguelph.socs.icc.edm.domain.Action.Builder
	 */

	public static final class Builder extends Action.Builder
	{
		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  model       The <code>DomainModel</code>, not null
		 * @param  idGenerator The <code>IdGenerator</code>, not null
		 * @param  retriever   The <code>Retriever</code>, not null
		 */

		private Builder (
				final DomainModel model,
				final IdGenerator idGenerator,
				final Retriever<Action> retriever)
		{
			super (model, idGenerator, retriever);
		}

		/**
		 * Create an instance of the <code>Action</code>.
		 *
		 * @param  action The previously existing <code>Action</code> instance,
		 *                may be null
		 * @return        The new <code>Action</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected Action create (final @Nullable Action action)
		{
			this.log.trace ("create: action={}", action);

			return new ActionData (this);
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The primary key of the action */
	private @Nullable Long id;

	/** The name of the action */
	private String name;

	/**
	 * Create the <code>Action</code> with null values.
	 */

	protected ActionData ()
	{
		this.id= null;
		this.name = null;
	}

	/**
	 * Create an <code>Action</code> from the supplied <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected ActionData (final Builder builder)
	{
		super (builder);

		this.id = builder.getId ();
		this.name = Preconditions.checkNotNull (builder.getName (), "name");
	}

	/**
	 * Get the <code>DataStore</code> identifier for the <code>Action</code>
	 * instance.
	 *
	 * @return The <code>DataStore</code> identifier
	 */

	@Override
	@CheckReturnValue
	public Long getId ()
	{
		return this.id;
	}

	/**
	 * Set the <code>DataStore</code> identifier.  This method is intended to
	 * be used to initialize the <code>DataStore</code> identifier on a new
	 * <code>Action</code> instance.
	 *
	 * @param  id The <code>DataStore</code> identifier, not null
	 */

	@Override
	protected void setId (final @Nullable Long id)
	{
		this.id = id;
	}

	/**
	 * Get the name of the <code>Action</code>.
	 *
	 * @return A String containing the name of the <code>Action</code>
	 */

	@Override
	public String getName()
	{
		return this.name;
	}

	/**
	 * Set the name of the <code>Action</code>.  This method is intended to be
	 * used initialize a new <code>Action</code> instance.
	 *
	 * @param  name The name of the <code>Action</code>
	 */

	@Override
	protected void setName (final String name)
	{
		assert name != null : "name is NULL";

		this.name = name;
	}
}
