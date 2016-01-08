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

package ca.uoguelph.socs.icc.edm.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import javax.inject.Named;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import ca.uoguelph.socs.icc.edm.domain.datastore.Query;
import ca.uoguelph.socs.icc.edm.domain.datastore.Retriever;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.IdGenerator;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of a user within the domain model.  Classes implementing
 * this interface contain the information required to identify the person who
 * is associated with the data (<code>Enrolment</code>, <code>Grade</code>,
 * and <code>LogEntry</code> instances, etc.) contained within the domain
 * model.  As such, instances of the <code>User</code> interface are treated
 * somewhat specially.
 * <p>
 * In the domain model, <code>User</code> is a root level element, as its
 * existence is not dependent on any other components of the domain model.
 * Only <code>Enrolment</code> depends on <code>User</code>, and that
 * dependency is weak.  The domain model is designed such that
 * <code>User</code> is optional.  However, it would be wise to make sure that
 * any <code>DomainModel</code> instance that does not contain <code>User</code>
 * instances is completely immutable.
 * <p>
 * With the exception of adding and removing <code>Enrolment<code> instances,
 * <code>User</code> instances, once created, are immutable.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class User extends Element
{
	/**
	 * Create and modify <code>User</code> instances.  This class extends
	 * <code>AbstractBuilder</code>, adding the necessary functionality to
	 * handle <code>User</code> instances.  The "Firstname" and "Lastname"
	 * fields of existing <code>User</code> instances, may be modified in place.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	public static class Builder extends Element.Builder<User>
	{
		/** The <code>IdGenerator</code>*/
		private final IdGenerator idGenerator;

		/** Helper to substitute <code>Enrolment</code> instances */
		private final Retriever<Enrolment> enrolmentRetriever;

		/** Query instance to look up <code>User</code> instances by <code>Enrolment</code> */
		private final Query<User> enrolmentQuery;

		/** The <code>DataStore</code> id number for the <code>User</code> */
		private @Nullable Long id;

		/** The first name of the <code>User</code> */
		private @Nullable String firstname;

		/** The last name of the <code>User</code> */
		private @Nullable String lastname;

		/** The username of the <code>User</code> */
		private @Nullable String username;

		/** The associates <code>Enrolment</code> instances */
		private final Set<Enrolment> enrolments;

		/**
		 * Create an instance of the <code>Builder</code>.
		 *
		 * @param  model              The <code>DomainModel</code>, not null
		 * @param  definition         The <code>Definition</code>, not null
		 * @param  idGenerator        The <code>IdGenerator</code>, not null
		 * @param  UserRetriever      <code>Retriever</code> for
		 *                            <code>User</code> instances, not null
		 * @param  enrolmentRetriever <code>Retriever</code> for
		 *                            <code>Enrolment</code> instances, not null
		 * @param  enrolmentQuery     <code>Query</code> to check if an
		 *                            <code>Enrolment</code> instances is
		 *                            already associated with another
		 *                            <code>User</code> instance, not null
		 */

		private Builder (
				final DomainModel model,
				final Definition definition,
				final IdGenerator idGenerator,
				final Retriever<User> userRetriever,
				final Retriever<Enrolment> enrolmentRetriever,
				final Query<User> enrolmentQuery)
		{
			super (model, definition, userRetriever);

			assert idGenerator != null : "idGenerator is NULL";
			assert enrolmentRetriever != null : "enrolmentRetriever is NULL";
			assert enrolmentQuery != null : "enrolmentQuery is NULL";

			this.idGenerator = idGenerator;
			this.enrolmentRetriever = enrolmentRetriever;
			this.enrolmentQuery = enrolmentQuery;

			this.id = null;
			this.firstname = null;
			this.lastname = null;
			this.username = null;

			this.enrolments = new HashSet<Enrolment> ();
		}

		/**
		 * Create an instance of the <code>User</code>.
		 *
		 * @return The new <code>User</code> instance
		 *
		 * @throws NullPointerException if any required field is missing
		 */

		@Override
		protected User create ()
		{
			this.log.trace ("create:");

			return ((Definition) this.definition).creator.apply (this);
		}

		/**
		 * Implementation of the pre-insert hook to set the ID number.
		 *
		 * @param  user The <code>User</code> to be inserted, not null
		 * @return      The <code>User</code> to be inserted
		 */

		@Override
		protected User preInsert (final User user)
		{
			assert user != null : "user is NULL";

			this.log.debug ("Setting ID");
			user.setId (this.idGenerator.nextId ());

			return user;
		}

		@Override
		protected User postInsert (final User user)
		{
			if (! this.enrolments.equals (user.getEnrolments ()))
			{
				new HashSet<Enrolment> (this.enrolments)
					.stream ()
					.filter (x -> (! user.getEnrolments ().contains (x)))
					.forEach (x -> user.addEnrolment (x));
			}

			return user;
		}

		/**
		 * Reset the builder.  This method will set all of the fields for the
		 * <code>Element</code> to be built to <code>null</code>.
		 *
		 * @return This <code>Builder</code>
		 */

		@Override
		public Builder clear ()
		{
			this.log.trace ("clear:");

			super.clear ();

			this.id = null;
			this.firstname = null;
			this.lastname = null;
			this.username = null;

			this.enrolments.clear ();

			return this;
		}

		/**
		 * Load a <code>User</code> instance into the builder.  This method
		 * resets the builder and initializes all of its parameters from
		 * the specified <code>User</code> instance.  The  parameters are
		 * validated as they are set.
		 *
		 * @param  user The <code>User</code>, not null
		 * @return      This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>User</code> instance to be
		 *                                  loaded are not valid
		 */

		@Override
		public Builder load (final User user)
		{
			this.log.trace ("load: user={}", user);

			this.clear ();
			super.load (user);

			this.id = user.getId ();
			this.setUsername (user.getUsername ());
			this.setLastname (user.getLastname ());
			this.setFirstname (user.getFirstname ());

			user.getEnrolments ().forEach (x -> this.addEnrolment (x));

			return this;
		}

		/**
		 * Get the <code>DataStore</code> identifier for the <code>User</code>
		 * instance.
		 *
		 * @return The <code>DataStore</code> identifier
		 */

		@CheckReturnValue
		public final Long getId ()
		{
			return this.id;
		}

		/**
		 * Get the first name (given name) of the <code>User</code>.
		 *
		 * @return A <code>String</code> containing the given name of the
		 *         <code>User</code>
		 */

		@CheckReturnValue
		public final String getFirstname ()
		{
			return this.firstname;
		}

		/**
		 * Set the first name of the <code>User</code>.
		 *
		 * @param  firstname The first name of the <code>User</code>, not null
		 * @return           This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If the first name is empty
		 */

		public final Builder setFirstname (final String firstname)
		{
			this.log.trace ("setFirstname: firstname={}", firstname);

			Preconditions.checkNotNull (firstname);
			Preconditions.checkArgument (firstname.length () > 0, "firstname is empty");

			this.firstname = firstname;

			return this;
		}

		/**
		 * Get the last name (surname) of the <code>User</code>.
		 *
		 * @return A String containing the surname of the <code>User</code>.
		 */

		@CheckReturnValue
		public final String getLastname ()
		{
			return this.lastname;
		}

		/**
		 * Set the last name of the <code>User</code>.
		 *
		 * @param  lastname The last name of the <code>User</code>, not null
		 * @return          This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If the last name is empty
		 */

		public final Builder setLastname (final String lastname)
		{
			this.log.trace ("setLastname: lastname={}", lastname);

			Preconditions.checkNotNull (lastname);
			Preconditions.checkArgument (lastname.length () > 0, "lastname is empty");

			this.lastname = lastname;

			return this;
		}

		/**
		 * Get the username for the <code>User</code>.  This will be the
		 * username that the <code>User</code> used to access the LMS from which
		 * the data associated with the <code>User</code> was harvested.  The
		 * username is expected to be unique.
		 *
		 * @return A <code>String</code> containing the username for the
		 *         <code>User</code>
		 */

		@CheckReturnValue
		public final String getUsername ()
		{
			return this.username;
		}

		/**
		 * Set the username of the <code>User</code>.
		 *
		 * @param  username The user name of the <code>User</code>, not null
		 * @return          This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If the user name is empty
		 */

		public final Builder setUsername (final String username)
		{
			this.log.trace ("setUsername: username={}", username);

			Preconditions.checkNotNull (username);
			Preconditions.checkArgument (username.length () > 0, "username is empty");

			this.username = username;

			return this;
		}

		/**
		 * Get the <code>Set</code> of <code>Enrolment<code> instances which are
		 * associated with this <code>User</code>.  If there are no associated
		 * <code>Enrolment</code> instances, then the <code>Set</code> will be
		 * empty.
		 *
		 * @return A <code>Set</code> of <code>Enrolment</code> instances
		 */

		public final Set<Enrolment> getEnrolments ()
		{
			return Collections.unmodifiableSet (this.enrolments);
		}

		/**
		 * Create an association between the <code>User</code> and the specified
		 * <code>Enrolment</code>.  Note that only one <code>User</code> may be
		 * associated with a given <code>Enrolment</code>.
		 *
		 * @param  enrolment The <code>Enrolment</code> to be associated with
		 *                   the <code>User</code>, not null
		 * @return           This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If there is already a
		 *                                  <code>User</code> associated with
		 *                                  the <code>Enrolment</code>
		 */

		public final Builder addEnrolment (final Enrolment enrolment)
		{
			this.log.trace ("addEnrolment: enrolment={}", enrolment);

			Enrolment add = this.verifyRelationship (this.enrolmentRetriever, enrolment, "enrolment");

			Preconditions.checkArgument (this.enrolmentQuery.setValue (User.ENROLMENTS, add).queryAll ().isEmpty (),
					"The Enrolment is already assigned to another user");

			this.enrolments.add (add);

			return this;
		}

		/**
		 * Break an association between the <code>User</code> and the specified
		 * <code>Enrolment</code>.  To break an association between the
		 * <code>User</code> and the specified <code>Enrolment</code>, both
		 * the <code>User</code> and <code>Enrolment</code> must be exist in
		 * the <code>DataStore</code> associated with the <code>Builder</code>
		 * instance that is to break the association. Furthermore, there must be
		 * an existing association between the <code>User</code> and the
		 * <code>Enrolment</code>.
		 *
		 * @param  enrolment The <code>Enrolment</code> to remove from the
		 *                   <code>User</code>, not null
		 * @return           This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException If there is no association between
		 *                                  the <code>User</code> and the
		 *                                  <code>Enrolment</code>
		 */

		public final Builder removeEnrolment (final Enrolment enrolment)
		{
			this.log.trace ("removeEnrolment: enrolment={}", enrolment);

			this.enrolments.remove (this.verifyRelationship (this.enrolmentRetriever, enrolment, "enrolment"));

			return this;
		}
	}

	/**
	 * Dagger Component interface for creating <code>Builder</code> instances.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@ElementScope
	@Component (dependencies = {IdGenerator.IdGeneratorComponent.class}, modules = {UserBuilderModule.class})
	protected interface UserComponent extends Element.ElementComponent<User>
	{
		/**
		 * Create the Builder instance.
		 *
		 * @return The <code>Builder</code>
		 */

		@Override
		public abstract Builder getBuilder ();
	}

	/**
	 * Dagger module for creating <code>Retriever</code> instances.  This module
	 * contains implementation-independent information.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module
	public static final class UserModule extends Element.ElementModule<User>
	{
		/**
		 * Get the <code>Selector</code> used by the
		 * <code>QueryRetriever</code>.
		 *
		 * @return The <code>Selector</code>
		 */

		@Provides
		public Selector<User> getSelector ()
		{
			return User.SELECTOR_USERNAME;
		}
	}

	/**
	 * Dagger module for creating <code>Builder</code> instances.  This module
	 * contains implementation-dependent information.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	@Module (includes = {UserModule.class, Enrolment.EnrolmentModule.class})
	public static final class UserBuilderModule
	{
		/** The <code>Definition</code> */
		private final Definition definition;

		/**
		 * Create the <code>UserBuilderModule</code>
		 *
		 * @param  definition The <code>Definition</code>, not null
		 */

		public UserBuilderModule (final Definition definition)
		{
			assert definition != null : "definition is null";
			this.definition = definition;
		}

		/**
		 * Get a <code>Query</code> for getting a <code>User</code> which is
		 * associated with an <code>Enrolment</code>.
		 *
		 * @return The <code>Query</code>
		 */

		@Provides
		@Named ("Enrolment")
		public Query<User> getQuery (final DomainModel model)
		{
			return model.getQuery (User.SELECTOR_USERNAME);
		}

		/**
		 * Create an instance of the <code>Builder</code>.
		 *
		 * @param  model              The <code>DomainModel</code>, not null
		 * @param  idGenerator        The <code>IdGenerator</code>, not null
		 * @param  userRetriever      <code>Retriever</code> for
		 *                            <code>User</code> instances, not null
		 * @param  enrolmentRetriever <code>Retriever</code> for
		 *                            <code>Enrolment</code> instances, not null
		 * @param  enrolmentQuery     <code>Query</code> to check if an
		 *                            <code>Enrolment</code> instances is
		 *                            already associated with another
		 *                            <code>User</code> instance, not null
		 */

		@Provides
		public Builder createBuilder (
				final DomainModel model,
				final IdGenerator idGenerator,
				final @Named ("QueryRetriever") Retriever<User> userRetriever,
				final @Named ("TableRetriever") Retriever<Enrolment> enrolmentRetriever,
				final @Named ("Enrolment") Query<User> enrolmentQuery)
		{
			return new Builder (model, this.definition, idGenerator, userRetriever, enrolmentRetriever, enrolmentQuery);
		}
	}

	/**
	 * Abstract representation of an <code>Element</code> implementation class.
	 * Instances of this class are used to load the <code>Element</code>
	 * implementations into the JVM via the <code>ServiceLoader</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	protected static abstract class Definition extends Element.Definition<User>
	{
		/** Method reference to the implementation constructor  */
		private final Function<User.Builder, User> creator;

		/**
		 * Create the <code>Definition</code>.
		 *
		 * @param  impl    The implementation class, not null
		 * @param  creator Method reference to the constructor, not null
		 */

		public Definition (
				final Class<? extends User> impl,
				final Function<User.Builder, User> creator)
		{
			super (User.METADATA, impl);

			assert creator != null : "creator is NULL";
			this.creator = creator;
		}

		/**
		 * Create a new instance of the <code>Component</code> on the
		 * specified <code>DomainModel</code>.
		 *
		 * @param model The <code>DomainModel</code>, not null
		 * @return      The <code>Component</code>
		 */

		@Override
		protected User.UserComponent getComponent (final DomainModel model)
		{
			return DaggerUser_UserComponent.builder ()
				.idGeneratorComponent (model.getIdGeneratorComponent (this.impl))
				.domainModelModule (new DomainModel.DomainModelModule (User.class, model))
				.userBuilderModule (new UserBuilderModule (this))
				.build ();
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>MetaData</code> for the <code>User</code> */
	protected static final MetaData<User> METADATA;

	/** The <code>DataStore</code> identifier of the <code>User</code> */
	public static final Property<User, Long> ID;

	/** The <code>DomainModel</code> which contains the <code>User</code> */
	public static final Property<User, DomainModel> MODEL;

	/** The first name of the <code>User</code> */
	public static final Property<User, String> FIRSTNAME;

	/** The last name of the <code>User</code> */
	public static final Property<User, String> LASTNAME;

	/** The username of the <code>User</code> */
	public static final Property<User, String> USERNAME;

	/** The <code>Enrolment</code> instances associated with the <code>User</code> */
	public static final Property<User, Enrolment> ENROLMENTS;

	/** Select the <code>User</code> instance by its id */
	public static final Selector<User> SELECTOR_ID;

	/** Select all of the <code>User</code> instances */
	public static final Selector<User> SELECTOR_ALL;

	/** Select the <code>User</code> instance for a <code>Enrolment</code> */
	public static final Selector<User> SELECTOR_ENROLMENTS;

	/** Select an <code>User</code> instance by its username */
	public static final Selector<User> SELECTOR_USERNAME;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>User</code>.
	 */

	static
	{
		ID = Property.of (User.class, Long.class, "id",
				User::getId, User::setId);

		MODEL = Property.of (User.class, DomainModel.class, "domainmodel",
				User::getDomainModel, User::setDomainModel);

		FIRSTNAME = Property.of (User.class, String.class, "firstname",
				User::getFirstname, User::setFirstname,
				Property.Flags.REQUIRED);

		LASTNAME = Property.of (User.class, String.class, "lastname",
				User::getLastname, User::setLastname,
				Property.Flags.REQUIRED);

		USERNAME = Property.of (User.class, String.class, "username",
				User::getUsername, User::setUsername,
				Property.Flags.REQUIRED);

		ENROLMENTS = Property.of (User.class, Enrolment.class, "enrolments",
				User::getEnrolments, User::addEnrolment, User::removeEnrolment,
				Property.Flags.MUTABLE);

		SELECTOR_ID = Selector.of (Selector.Cardinality.KEY, ID);
		SELECTOR_USERNAME = Selector.of (Selector.Cardinality.SINGLE, USERNAME);
		SELECTOR_ENROLMENTS = Selector.of (Selector.Cardinality.SINGLE, ENROLMENTS);

		SELECTOR_ALL =  Selector.builder (User.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		METADATA = MetaData.builder (User.class)
			.addProperty (ID)
			.addProperty (MODEL)
			.addProperty (FIRSTNAME)
			.addProperty (LASTNAME)
			.addProperty (USERNAME)
			.addRelationship (ENROLMENTS, Enrolment.METADATA, SELECTOR_ENROLMENTS)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.addSelector (SELECTOR_USERNAME)
			.build ();
	}

	/**
	 * Get an instance of the <code>Builder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @return       The <code>Builder</code> instance
	 *
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> does not
	 *                               have a default implementation class for
	 *                               the <code>User</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static Builder builder (final DomainModel model)
	{
		Preconditions.checkNotNull (model, "model");

		return (User.Builder) model.getElementComponent (User.class)
			.getBuilder ();
	}

	/**
	 * Create an association between a <code>User</code> and an
	 * <code>Enrolment</code>.
	 *
	 * @param  user      The <code>User</code>, not null
	 * @param  enrolment The <code>Enrolment</code>, not null
	 * @return           <code>true</code> if the association was successfully
	 *                   created, <code>false</code> otherwise
	 */

	public static boolean enrol (final User user, final Enrolment enrolment)
	{
		Preconditions.checkNotNull (user, "user");
		Preconditions.checkNotNull (enrolment, "enrolment");

		assert user.getDomainModel ().contains (user) : "User is not a Member of its DomainModel";
		assert enrolment.getDomainModel ().contains (enrolment) : "Enrolment is not a Member of its DomainModel";

		Preconditions.checkArgument (user.getDomainModel () == enrolment.getDomainModel (),
				"User and enrolment must be members of the same DomainModel");

		Preconditions.checkArgument (! user.getDomainModel ()
				.getQuery (User.SELECTOR_ENROLMENTS)
				.setValue (User.ENROLMENTS, enrolment)
				.query ()
				.isPresent (), "Enrolment is already associated with a user");

		return user.addEnrolment (enrolment);
	}

	/**
	 * Break an association between a <code>User</code> and an
	 * <code>Enrolment</code>.
	 *
	 * @param  user      The <code>User</code>, not null
	 * @param  enrolment The <code>Enrolment</code>, not null
	 * @return           <code>true</code> if the association was successfully
	 *                   broken, <code>false</code> otherwise
	 */

	public static boolean unenrol (final User user, final Enrolment enrolment)
	{
		Preconditions.checkNotNull (user, "user");
		Preconditions.checkNotNull (enrolment, "enrolment");

		assert user.getDomainModel ().contains (user) : "User is not a Member of its DomainModel";
		assert enrolment.getDomainModel ().contains (enrolment) : "Enrolment is not a Member of its DomainModel";

		return user.removeEnrolment (enrolment);
	}

	/**
	 * Create the <code>User</code>
	 */

	protected User ()
	{
		super ();
	}

	/**
	 * Create the <code>User</code> instance from the specified
	 * <code>Builder</code>.
	 *
	 * @param  builder The <code>Builder</code>, not null
	 */

	protected User (final Builder builder)
	{
		super (builder);
	}

	/**
	 * Template method to create and initialize a <code>ToStringHelper</code>.
	 *
	 * @return The <code>ToStringHelper</code>
	 */

	@Override
	@CheckReturnValue
	protected MoreObjects.ToStringHelper toStringHelper ()
	{
		return super.toStringHelper ()
			.add ("firstname", this.getFirstname ())
			.add ("lastname", this.getLastname ())
			.add ("username", this.getUsername ());
	}

	/**
	 * Connect all of the relationships for this <code>User</code> instance.
	 * This method is intended to be used just after the <code>User</code> is
	 * inserted into the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         connected, <code>false</code> otherwise
	 */

	@Override
	protected boolean connect ()
	{
		return User.METADATA.relationships ()
				.allMatch (r -> r.connect (this));
	}

	/**
	 * Disconnect all of the relationships for this <code>User</code> instance.
	 * This method is intended to be used just before the <code>User</code> is
	 * removed from the <code>DataStore</code>.
	 *
	 * @return <code>true</code> if all of the relationships were successfully
	 *         disconnected, <code>false</code> otherwise
	 */

	@Override
	protected boolean disconnect ()
	{
		return User.METADATA.relationships ()
				.allMatch (r -> r.disconnect (this));
	}

	/**
	 * Compare two <code>User</code> instances. <code>User</code> instances are
	 * compared based upon their last name, then the first name and finally the
	 * username.
	 *
	 * @param  element The <code>Element</code> to be compared
	 * @return         The value 0 if the <code>User</code> instances are equal,
	 *                 less than 0 of the argument is is greater, and greater
	 *                 than 0 if the argument is less than the <code>User</code>
	 */

	@Override
	public int compareTo (final Element element)
	{
		Preconditions.checkNotNull (element, "element");

		int result = 0;

		if (this != element)
		{
			if (element instanceof User)
			{
				result = this.getLastname ().compareTo (((User) element).getLastname ());

				if (result == 0)
				{
					result = this.getFirstname ().compareTo (((User) element).getFirstname ());

					if (result == 0)
					{
						result = this.getUsername ().compareTo (((User) element).getUsername ());
					}
				}
			}
			else
			{
				result = this.getClass ().getName ().compareTo (element.getClass ().getName ());
			}
		}

		return result;
	}

	/**
	 * Compare two <code>User</code> instances to determine if they are
	 * equal.  The <code>User</code> instances are compared based upon the
	 * this ID number and the username.
	 *
	 * @param  obj The <code>User</code> instance to compare to the one
	 *             represented by the called instance
	 * @return     <code>True</code> if the two <code>User</code> instances
	 *             are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof User)
			&& Objects.equals (this.getUsername (), ((User) obj).getUsername ());
	}

	/**
	 * Compare two <code>User</code> instances to determine if they are equal
	 * using all of the instance fields.  For <code>User</code> the
	 * <code>equals</code> methods excludes the first name and last name fields
	 * from the comparison.  This methods compares two <code>User</code>
	 * instances using all of the fields.
	 *
	 * @param  element The <code>Element</code> instance to compare to this
	 *                 instance
	 * @return         <code>True</code> if the two <code>User</code>
	 *                 instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equalsAll (final @Nullable Element element)
	{
		return (element == this) ? true : (element instanceof User)
			&& Objects.equals (this.getUsername (), ((User) element).getUsername ())
			&& Objects.equals (this.getFirstname (), ((User) element).getFirstname ())
			&& Objects.equals (this.getLastname (), ((User) element).getLastname ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>User</code> instance.
	 * The hash code is computed based upon the following fields ID number and
	 * the username.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.getUsername ());
	}

	/**
	 * Get a <code>String</code> representation of the <code>User</code>
	 * instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the <code>User</code>
	 *         instance
	 */

	@Override
	@CheckReturnValue
	public String toString()
	{
		return this.toStringHelper ()
			.toString ();
	}

	/**
	 * Get a <code>Stream</code> containing all of the associated
	 * <code>Element</code> instances.
	 *
	 * @return The <code>Stream</code>
	 */

	@Override
	public Stream<Element> associations ()
	{
		return User.METADATA.properties ()
			.filter (p -> p.hasFlags (Property.Flags.RELATIONSHIP))
			.flatMap (p -> p.stream (this))
			.map (e -> ((Element) e));
	}

	/**
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates a <code>Builder</code>
	 * on the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>User</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 * @return       The initialized <code>Builder</code>
	 */

	@Override
	public Builder getBuilder (final DomainModel model)
	{
		return User.builder (Preconditions.checkNotNull (model, "model"))
			.load (this);
	}

	/**
	 * Get the first name (given name) of the <code>User</code>.
	 *
	 * @return A <code>String</code> containing the given name of the
	 *         <code>User</code>
	 */

	public abstract String getFirstname ();

	/**
	 * Set the first name of the <code>User</code>.  This method is intended to
	 * be used to initialize a new <code>User</code> instance.
	 *
	 * @param  firstname The first name, not null
	 */

	protected abstract void setFirstname (String firstname);

	/**
	 * Get the last name (surname) of the <code>User</code>.
	 *
	 * @return A String containing the surname of the <code>User</code>.
	 */

	public abstract String getLastname ();

	/**
	 * Set the last name of the <code>User</code>.  This method is intended to
	 * be used to initialize a new <code>User</code> instance.
	 *
	 * @param  lastname The last name, not null
	 */

	protected abstract void setLastname (String lastname);

	/**
	 * Get the username for the <code>User</code>.  This will be the username
	 * that the <code>User</code> used to access the LMS from which the data
	 * associated with the <code>User</code> was harvested.  The username is
	 * expected to be unique.
	 *
	 * @return A <code>String</code> containing the username for the
	 *         <code>User</code>
	 */

	public abstract String getUsername ();

	/**
	 * Set the username of the <code>User</code>.  This method is intended to
	 * be used to initialize a new <code>User</code> instance.
	 *
	 * @param  username The username, not null
	 */

	protected abstract void setUsername (String username);

	/**
	 * Get the full name of the <code>User</code>.  This method will return a
	 * concatenation of the <code>firstname</code> and <code>lastname</code> of
	 * the <code>User</code>.
	 *
	 * @return A <code>String</code> containing the name of the user.
	 */

	public abstract String getName ();

	/**
	 * Get the <code>Enrolment</code> instance for the <code>User</code> in the
	 * specified <code>Course</code>.
	 *
	 * @param  course The <code>Course</code> for which the
	 *                <code>Enrolment</code> instance is to be retrieved
	 * @return        The <code>Enrolment</code> instance for the
	 *                <code>User</code> in the specified <code>Course</code>,
	 *                or null
	 */

	public abstract Enrolment getEnrolment (Course course);

	/**
	 * Get the <code>Set</code> of <code>Enrolment<code> instances which are
	 * associated with this <code>User</code>.  If there are no associated
	 * <code>Enrolment</code> instances, then the <code>Set</code> will be
	 * empty.
	 *
	 * @return A <code>Set</code> of <code>Enrolment</code> instances
	 */

	public abstract Set<Enrolment> getEnrolments ();

	/**
	 * Initialize the <code>Set</code> of <code>Enrolment</code> instances
	 * associated with the <code>User</code> instance.  This method is intended
	 * to be used to initialize a new <code>User</code> instance.
	 *
	 * @param  enrolments The <code>Set</code> of <code>Enrolment</code>
	 *                    instances, not null
	 */

	protected abstract void setEnrolments (Set<Enrolment> enrolments);

	/**
	 * Add an <code>Enrolment</code> to the <code>User</code> instance.
	 *
	 * @param  enrolment The <code>Enrolment</code> to add
	 * @return           <code>True</code> if the enrolment was added to the
	 *                   <code>User</code> instance, <code>False</code>
	 *                   otherwise
	 */

	protected abstract boolean addEnrolment (Enrolment enrolment);

	/**
	 * Remove an <code>Enrolment</code> from the <code>User</code> instance.
	 *
	 * @param  enrolment The <code>Enrolment</code> to remove
	 * @return           <code>True</code> if the enrolment was removed from
	 *                   the <code>User</code> instance, <code>False</code>
	 *                   otherwise
	 */

	protected abstract boolean removeEnrolment (Enrolment enrolment);
}

