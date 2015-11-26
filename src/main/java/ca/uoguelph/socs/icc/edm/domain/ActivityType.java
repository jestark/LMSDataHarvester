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

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uoguelph.socs.icc.edm.domain.datastore.Persister;
import ca.uoguelph.socs.icc.edm.domain.datastore.Retriever;
import ca.uoguelph.socs.icc.edm.domain.metadata.MetaData;
import ca.uoguelph.socs.icc.edm.domain.metadata.Property;
import ca.uoguelph.socs.icc.edm.domain.metadata.Selector;

/**
 * A representation of the nature of a particular <code>Activity</code>.
 * Instances of the <code>ActivityType</code> interface serve to describe the
 * nature of the instances of the <code>Activity</code> interface with which
 * they are associated.  Example <code>ActivityType</code> instances include:
 * "Assignment," "Quiz," and "Presentation."  For instances of
 * <code>Activity</code> where the data was harvested from a Learning
 * Management System (such as Moodle) the <code>ActivityType</code> will be the
 * name of the module from which the data contained in the associated
 * <code>Activity</code> instances was harvested.
 * <p>
 * Instances <code>ActvityType</code> interface has a strong dependency on the
 * associated instances of the <code>ActivitySource</code> interface.  If an
 * instance of a particular <code>ActivitySource</code> is removed from the
 * <code>DomainModel</code> then all of the associated instances of the
 * <code>ActivityType</code> interface must be removed as well.  Similarly,
 * instances of the <code>Activity</code> interface are dependent on the
 * associated instance of the <code>ActivityType</code> interface.  Removing an
 * instance of the <code>ActivityType</code> interface from the
 * <code>DomainModel</code> will require the removal of the associated instances
 * of the <code>Activity</code> interface.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public abstract class ActivityType extends Element
{
	/**
	 * Create new <code>ActivityType</code> instances.  This class extends
	 * <code>AbstractBuilder</code>, adding the functionality required to
	 * create <code>ActivityType</code> instances.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 * @see     ActivityType
	 */

	public static final class Builder implements Element.Builder<ActivityType>
	{
		/** The Logger */
		private final Logger log;

		/** Helper to substitute <code>ActivitySource</code> instances */
		private final Retriever<ActivitySource> sourceRetriever;

		/** Helper to operate on <code>ActivityType</code> instances*/
		private final Persister<ActivityType> persister;

		/** Method reference to the constructor of the implementation class */
		private final Supplier<ActivityType> supplier;

		/** The loaded of previously created <code>ActivityType</code> */
		private ActivityType type;

		/** The <code>DataStore</code> id number for the <code>ActivityType</code> */
		private Long id;

		/** The name of the <code>ActivityType</code> */
		private String name;

		/** The <code>ActivitySource</code> */
		private ActivitySource source;

		/**
		 * Create the <code>Builder</code>.
		 *
		 * @param  supplier        Method reference to the constructor of the
		 *                         implementation class, not null
		 * @param  persister       The <code>Persister</code> used to store the
		 *                         <code>ActivityType</code>, not null
		 * @param  sourceRetriever <code>Retriever</code> for
		 *                         <code>ActivitySource</code> instances, not null
		 */

		protected Builder (final Supplier<ActivityType> supplier, final Persister<ActivityType> persister, final Retriever<ActivitySource> sourceRetriever)
		{
			assert supplier != null : "supplier is NULL";
			assert persister != null : "persister is NULL";
			assert sourceRetriever != null : "sourceRetriever is NULL";

			this.log = LoggerFactory.getLogger (this.getClass ());

			this.sourceRetriever = sourceRetriever;
			this.persister = persister;
			this.supplier = supplier;

			this.type = null;
			this.id = null;
			this.name = null;
			this.source = null;
		}

		/**
		 * Create an instance of the <code>ActivityType</code>.
		 *
		 * @return                       The new <code>ActivityType</code> instance
		 * @throws IllegalStateException If any if the fields is missing
		 * @throws IllegalStateException If there isn't an active transaction
		 */

		@Override
		public ActivityType build ()
		{
			this.log.trace ("build:");

			if (this.name == null)
			{
				this.log.error ("Attempting to create an ActivityType without a name");
				throw new IllegalStateException ("name is NULL");
			}

			if (this.source == null)
			{
				this.log.error ("Attempting to create an ActivityType without an ActivitySource");
				throw new IllegalStateException ("source is NULL");
			}

			ActivityType result = this.supplier.get ();
			result.setId (this.id);
			result.setName (this.name);
			result.setSource (this.source);

			this.type = this.persister.insert (this.type, result);

			return this.type;
		}

		/**
		 * Reset the builder.  This method will set all of the fields for the
		 * <code>Element</code> to be built to <code>null</code>.
		 *
		 * @return This <code>Builder</code>
		 */

		public Builder clear ()
		{
			this.log.trace ("clear:");

			this.type = null;
			this.id = null;
			this.name = null;
			this.source = null;

			return this;
		}

		/**
		 * Load a <code>ActivityType</code> instance into the builder.  This
		 * method resets the builder and initializes all of its parameters from
		 * the specified <code>ActivityType</code> instance.  The  parameters
		 * are validated as they are set.
		 *
		 * @param  type                     The <code>ActivityType</code>, not
		 *                                  null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException If any of the fields in the
		 *                                  <code>ActivityType</code> instance
		 *                                  to be loaded are not valid
		 */

		public Builder load (final ActivityType type)
		{
			this.log.trace ("load: type={}", type);

			if (type == null)
			{
				this.log.error ("Attempting to load a NULL ActivityType");
				throw new NullPointerException ();
			}

			this.type = type;
			this.id = type.getId ();
			this.setName (type.getName ());
			this.setActivitySource (type.getSource ());

			return this;
		}

		/**
		 * Get the <code>DataStore</code> identifier for the
		 * <code>ActivityType</code> instance.
		 *
		 * @return The <code>DataStore</code> identifier
		 */

		@CheckReturnValue
		public Long getId ()
		{
			return this.id;
		}

		/**
		 * Get the name of the <code>ActivityType</code>.
		 *
		 * @return A <code>String</code> containing the name of the
		 *         <code>ActivityType</code>
		 */

		public String getName ()
		{
			return this.name;
		}

		/**
		 * Set the name of the <code>ActivityType</code>.
		 *
		 * @param  name                     The name of the
		 *                                  <code>ActivityType</code>, not null
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException If the name is empty
		 */

		public Builder setName (final String name)
		{
			this.log.trace ("setName: name={}", name);

			if (name == null)
			{
				this.log.error ("name is NULL");
				throw new NullPointerException ("name is NULL");
			}

			if (name.length () == 0)
			{
				this.log.error ("name is an empty string");
				throw new IllegalArgumentException ("name is empty");
			}

			this.name = name;

			return this;
		}

		/**
		 * Get the <code>ActivitySource</code> for the
		 * <code>ActivityType</code>.
		 *
		 * @return The <code>ActivitySource</code> instance
		 */

		public ActivitySource getActivitySource ()
		{
			return this.source;
		}

		/**
		 * Set the <code>ActivitySource</code> for the
		 * <code>ActivityType</code>.
		 *
		 * @param  source                   The <code>ActivitySource</code> for
		 *                                  the <code>ActivityType</code>
		 *
		 * @return                          This <code>Builder</code>
		 * @throws IllegalArgumentException If the <code>AcivitySourse</code>
		 *                                  does not exist in the
		 *                                  <code>DataStore</code>
		 */

		public Builder setActivitySource (final ActivitySource source)
		{
			this.log.trace ("setSource: source={}", source);

			if (source == null)
			{
				this.log.error ("source is NULL");
				throw new NullPointerException ("source is NULL");
			}

			this.source = this.sourceRetriever.fetch (source);

			if (this.source == null)
			{
				this.log.error ("The specified ActivitySource does not exist in the DataStore: {}", source);
				throw new IllegalArgumentException ("ActivitySource is not in the DataStore");
			}

			return this;
		}
	}

	/** Serial version id, required by the Serializable interface */
	private static final long serialVersionUID = 1L;

	/** The <code>MetaData</code> for the <code>ActivityType</code> */
	protected static final MetaData<ActivityType> METADATA;

	/** The <code>DataStore</code> identifier of the <code>ActivityType</code> */
	public static final Property<ActivityType, Long> ID;

	/** The <code>DomainModel</code> which contains the <code>ActivityType</code> */
	public static final Property<ActivityType, DomainModel> MODEL;

	/** The name of the <code>ActivityType</code> */
	public static final Property<ActivityType, String> NAME;

	/** The associated <code>ActivitySource</code> */
	public static final Property<ActivityType, ActivitySource> SOURCE;

	/** Select the <code>ActivityType</code> instance by its id */
	public static final Selector<ActivityType> SELECTOR_ID;

	/** Select all of the <code>ActivityType</code> instances */
	public static final Selector<ActivityType> SELECTOR_ALL;

	/** Select an <code>ActivityType</code> instance by name and <code>ActivitySource</code> */
	public static final Selector<ActivityType> SELECTOR_NAME;

	/**
	 * Initialize the <code>MetaData</code>, <code>Property</code> and
	 * <code>Selector</code> instances for the <code>ActivityType</code>.
	 */

	static
	{
		ID = Property.of (ActivityType.class, Long.class, "id",
				ActivityType::getId, ActivityType::setId);

		MODEL = Property.of (ActivityType.class, DomainModel.class, "domainmodel",
				ActivityType::getDomainModel, ActivityType::setDomainModel);

		NAME = Property.of (ActivityType.class, String.class, "name",
				ActivityType::getName, ActivityType::setName,
				Property.Flags.REQUIRED);

		SOURCE = Property.of (ActivityType.class, ActivitySource.class, "source",
				ActivityType::getSource, ActivityType::setSource,
				Property.Flags.REQUIRED);

		SELECTOR_ID = Selector.of (Selector.Cardinality.KEY, ID);

		SELECTOR_ALL = Selector.builder (ActivityType.class)
			.setCardinality (Selector.Cardinality.MULTIPLE)
			.setName ("all")
			.build ();

		SELECTOR_NAME = Selector.builder (ActivityType.class)
			.setCardinality (Selector.Cardinality.SINGLE)
			.addProperty (NAME)
			.addProperty (SOURCE)
			.build ();

		METADATA = MetaData.builder (ActivityType.class)
			.addProperty (ID)
			.addProperty (MODEL)
			.addProperty (NAME)
			.addRelationship (SOURCE, ActivitySource.METADATA, ActivitySource.TYPES)
			.addSelector (SELECTOR_ID)
			.addSelector (SELECTOR_ALL)
			.addSelector (SELECTOR_NAME)
			.build ();
	}

	/**
	 * Get an instance of the <code>Builder</code> for the specified
	 * <code>DomainModel</code>.
	 *
	 * @param  model                 The <code>DomainModel</code>, not null
	 *
	 * @return                       The <code>Builder</code> instance
	 * @throws IllegalStateException if the <code>DomainModel</code> is closed
	 * @throws IllegalStateException if the <code>DomainModel</code> does not
	 *                               have a default implementation class for
	 *                               the <code>ActivityType</code>
	 * @throws IllegalStateException if the <code>DomainModel</code> is
	 *                               immutable
	 */

	public static Builder builder (final DomainModel model)
	{
		Preconditions.checkNotNull (model, "model");

		return null;
	}

	/**
	 * Create the <code>ActivityType</code>.
	 */

	protected ActivityType ()
	{
		super ();
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
			.add ("source", this.getSource ())
			.add ("name", this.getName ());
	}

	/**
	 * Compare two <code>ActivityType</code> instances to determine if they are
	 * equal.  The <code>ActivityType</code> instances are compared based upon
	 * their associated <code>ActivitySource</code> and their names.
	 *
	 * @param  obj The <code>ActivityType</code> instance to compare to the one
	 *             represented by the called instance
	 *
	 * @return     <code>True</code> if the two <code>ActivityType</code>
	 *             instances are equal, <code>False</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof ActivityType)
			&& Objects.equals (this.getName (), ((ActivityType) obj).getName ())
			&& Objects.equals (this.getSource (), ((ActivityType) obj).getSource ());
	}

	/**
	 * Compute a <code>hashCode</code> of the <code>ActivityType</code>
	 * instance.  The hash code is computed based upon the associated
	 * <code>ActivitySource</code> and name of the instance.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.getName (), this.getSource ());
	}

	/**
	 * Get a <code>String</code> representation of the
	 * <code>ActivityType</code> instance, including the identifying fields.
	 *
	 * @return A <code>String</code> representation of the
	 *         <code>ActivityType</code> instance
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return this.toStringHelper ()
			.toString ();
	}

	/**
	 * Get the <code>Set</code> of <code>Property</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Property</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Stream<Property<? extends Element, ?>> properties ()
	{
		return ActivityType.METADATA.properties ();
	}

	/**
	 * Get the <code>Set</code> of <code>Selector</code> instances associated
	 * with the <code>Element</code> interface class.
	 *
	 * @return The <code>Set</code> of <code>Selector</code> instances
	 *         associated with the <code>Element</code> interface class
	 */

	@Override
	public Stream<Selector<? extends Element>> selectors ()
	{
		return ActivityType.METADATA.selectors ();
	}

	/**
	 * Get an <code>Builder</code> instance for the specified
	 * <code>DomainModel</code>.  This method creates an <code>Builder</code> on
	 * the specified <code>DomainModel</code> and initializes it with the
	 * contents of this <code>ActivityType</code> instance.
	 *
	 * @param  model The <code>DomainModel</code>, not null
	 *
	 * @return       The initialized <code>Builder</code>
	 */

	@Override
	public Builder getBuilder (final DomainModel model)
	{
		return ActivityType.builder (Preconditions.checkNotNull (model, "model"))
			.load (this);
	}

	/**
	 * Get the name of the <code>ActivityType</code>.
	 *
	 * @return A <code>String</code> containing the name of the
	 *         <code>ActivityType</code>
	 */

	public abstract String getName ();

	/**
	 * Set the name of the <code>ActivityType</code>.  This method is intended
	 * to be used to initialize a new <code>ActivityType</code> instance.
	 *
	 * @param  name The name of the <code>ActivityType</code>
	 */

	protected abstract void setName (String name);

	/**
	 * Get the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 *
	 * @return The <code>ActivitySource</code> instance
	 */

	public abstract ActivitySource getSource ();

	/**
	 * Set the <code>ActivitySource</code> for the <code>ActivityType</code>.
	 * This method is intended to be used initialize a new
	 * <code>ActivityType</code> instance.
	 *
	 * @param  source The <code>ActivitySource</code> for the
	 *                <code>ActivityType</code>
	 */

	protected abstract void setSource (ActivitySource source);
}
