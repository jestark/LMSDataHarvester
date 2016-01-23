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

package ca.uoguelph.socs.icc.edm.domain.datastore;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.CheckReturnValue;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import ca.uoguelph.socs.icc.edm.domain.DomainModel;
import ca.uoguelph.socs.icc.edm.domain.Element;
import ca.uoguelph.socs.icc.edm.domain.datastore.idgenerator.IdGenerator;

/**
 * Profile data for the <code>DataStore</code>.
 *
 * @author  James E. Stark
 * @version 1.0
 */

public final class Profile
{
	/**
	 * Load <code>Profile</code> data from a configuration file.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	private static final class Loader
	{
		/** The log */
		private final Logger log;

		/** The <code>Activity</code> class */
		private Class<? extends Element> elementClass;

		/** The config file parser */
		private final ConfigLoader loader;

		/** The profile builder */
		private final Profile.Builder builder;

		/**
		 * Create the <code>ProfileLoader</code>.
		 */

		private Loader ()
		{
			this.log = LoggerFactory.getLogger (this.getClass ());

			this.elementClass = null;

			this.builder = Profile.builder ();

			this.loader = ConfigLoader.create (this.getClass ().getResource ("/Profile.xsd"))
				.registerProcessor ("element", (n -> this.processElement (n)))
				.registerProcessor ("generator", (n -> this.processGenerator (n)))
				.registerProcessor ("implementation", (n -> this.processImplementation (n)))
				.registerProcessor ("name", (n -> this.processName (n)))
				.registerProcessor ("mutable", (n -> this.processMutable (n)))
				.registerProcessor ("parameter", (n -> this.processParameter (n)));
		}

		/**
		 * Load a class with the specified name.  This method will load a class
		 * with the specified name and ensure that it extends the specified
		 * superclass.
		 *
		 * @param  <T>        The type of the superclass
		 * @param  superclass The superclass, not null
		 * @param  name       The name of the class, not null
		 * @return            The loaded class
		 *
		 * @throws IllegalstateException if the loaded class does not extends the
		 *                               superclass
		 * @throws RuntimeException      if the named class can not be loaded
		 */

		@SuppressWarnings ("unchecked")
		private <T> Class<? extends T> processClass (final Class<T> superclass, final String name)
		{
			this.log.trace ("processElementClass: superclass={}, name={}", superclass, name);

			assert superclass != null : "superclass is NULL";
			assert name != null : "name is NULL";

			try
			{
				Class<?> element = Class.forName (name);

				if (! superclass.isAssignableFrom (element))
				{
					throw new IllegalStateException ("The loaded class does not extend the specified superclass");
				}

				return (Class<? extends T>) element;
			}
			catch (ClassNotFoundException ex)
			{
				throw new RuntimeException ("Class does not exist", ex);
			}
		}

		/**
		 * Process a "name" configuration element.
		 *
		 * @param  node The DOM tree node for the name, not null
		 */

		private void processName (final Node node)
		{
			this.log.trace ("processName: node={}", node);

			this.builder.setName (node.getChildNodes ().item (0).getNodeValue ());
		}

		/**
		 * Process a "mutable" configuration element.
		 *
		 * @param  node The DOM tree node for the mutable, not null
		 */

		private void processMutable (final Node node)
		{
			this.log.trace ("processMutable: node={}", node);

			this.builder.setMutable ((node.getChildNodes ().item (0).getNodeValue ().equalsIgnoreCase ("true")) ? true : false);
		}

		/**
		 * Process an "element" configuration element.
		 *
		 * @param  node The DOM tree node for the element, not null
		 */

		private void processElement (final Node node)
		{
			this.log.trace ("processElement: node={}", node);

			this.elementClass = this.processClass (Element.class, node.getAttributes ().getNamedItem ("class").getNodeValue ());
		}

		/**
		 * Process a "generator" configuration element.
		 *
		 * @param  node The DOM tree node for the generator, not null
		 */

		private void processGenerator (final Node node)
		{
			this.log.trace ("processGenerator: node={}", node);

			this.builder.setGenerator (this.elementClass, this.processClass (IdGenerator.class, node.getChildNodes ().item (0).getNodeValue ()));
		}

		/**
		 * Process a "implementation" configuration element.
		 *
		 * @param  node The DOM tree node for the implementation, not null
		 */

		private void processImplementation (final Node node)
		{
			this.log.trace ("processImplementation: node={}", node);

			this.builder.setElement (Profile.ELEMENT_DEFINITIONS.get (this.processClass (this.elementClass, node.getChildNodes ().item (0).getNodeValue ())));
		}

		/**
		 * Process a "parameter" configuration element.
		 *
		 * @param  node The DOM tree node for the parameter, not null
		 */

		private void processParameter (final Node node)
		{
			this.log.trace ("processParameter: node={}", node);

			this.builder.setParameter (node.getAttributes ().getNamedItem ("name").getNodeValue (),
				node.getChildNodes ().item (0).getNodeValue ());
		}

		/**
		 * Load the configuration from the specified URL and use it to build a
		 * <code>Profile</code>.
		 *
		 * @param  url The <code>URL</code>, not null
		 * @return     The <code>Profile</code>
		 */

		public Profile load (final URL url)
		{
			this.log.trace ("load: url={}", url);

			assert url != null : "url is NULL";

			this.loader.load (url);

			return this.builder.build ();
		}
	}

	/**
	 * Builder for the <code>Profile</code>.
	 *
	 * @author  James E. Stark
	 * @version 1.0
	 */

	public static final class Builder
	{
		/** The logger */
		private final Logger log;

		/** Can the <code>DataStore</code> be modified */
		private boolean mutable;

		/** The name of the <code>Profile</code> */
		private String name;

		/** Parameters for the <code>DataStore</code> */
		private final Map<String, String> parameters;

		/** Map of <code>Element</code> implementation classes */
		private final Map<Class<? extends Element>, Class<? extends Element>> implementations;

		/** Map of <code>IdGenerator</code> classes to <code>Element</code> classes */
		private final Map<Class<? extends Element>, Class<? extends IdGenerator>> generators;

		/**
		 * Create the <code>DomainModelBuilder</code>.
		 */

		private Builder ()
		{
			this.log = LoggerFactory.getLogger (this.getClass ());

			this.mutable = false;
			this.name = "";

			this.parameters = new HashMap<> ();
			this.generators = new HashMap<> ();
			this.implementations = new HashMap<> ();
		}

		/**
		 * Load an existing profile into the <code>Builder</code>.
		 *
		 * @param profile The <code>Profile</code>, not null
		 * @return        This <code>Builder</code>
		 */

		public Builder load (final Profile profile)
		{
			this.log.trace ("load: profile={}");

			Preconditions.checkNotNull (profile);

			this.clear ();

			this.mutable = profile.mutable;
			this.name = profile.name;
			this.generators.putAll (profile.generators);
			this.implementations.putAll (profile.implementations);

			return this;
		}

		/**
		 * Reset the builder to a blank state.
		 *
		 * @return This <code>ProfileBuilder</code>
		 */

		public Builder clear ()
		{
			this.log.trace ("clear:");

			this.mutable = false;
			this.implementations.clear ();
			this.generators.clear ();

			return this;
		}

		/**
		 * Determine if the <code>DomainModel</code> and its underlying
		 * <code>DataStore</code> can be changed.
		 *
		 * @return <code>true</code> if the <code>DomainModel</code> is mutable,
		 *         <code>false</code> otherwise
		 */

		public boolean isMutable ()
		{
			return this.mutable;
		}

		/**
		 * Set the mutability of the <code>DomainModel</code>.
		 *
		 * @param  mutable The mutability of the <code>DomainModel</code>
		 * @return         This <code>ProfileBuilder</code>
		 */

		public Builder setMutable (final boolean mutable)
		{
			this.log.trace ("setMutable: mutable={}", mutable);

			this.mutable = mutable;

			return this;
		}

		/**
		 * Get the name of the <code>Profile</code>.  This is also the name of any
		 * associated <code>DataStore</code> instances.
		 *
		 * @return The name of the <code>Profile</code>.
		 */

		public String getName ()
		{
			return this.name;
		}

		/**
		 * Set the name of the <code>Profile</code>.
		 *
		 * @param  name The name of the <code>Profile</code>, not null and must
		 *              not be empty
		 * @return      This <code>ProfileBuilder</code>
		 *
		 * @throws IllegalArgumentException if the name is empty
		 */

		public Builder setName (final String name)
		{
			this.log.trace ("setName: name={}", name);

			Preconditions.checkNotNull (name, "name");
			Preconditions.checkArgument (name.length () > 0, "name is Empty");

			this.name = name;

			return this;
		}

		/**
		 * Get the <code>Set</code> of <code>Element</code> interfaces for which a
		 * default implementation is set.
		 *
		 * @return A <code>Set</code> of <code>Element</code> classes
		 */

		public Set<Class<? extends Element>> getElements ()
		{
			return Collections.unmodifiableSet (this.implementations.keySet ());
		}

		/**
		 * Get the default implementation class for the specified
		 * <code>Element</code>.
		 *
		 * @param  element The <code>Element</code> interface class, not null
		 * @return         The default <code>Element</code> implementation
		 *                 class, may be null
		 */

		@CheckReturnValue
		public Class<? extends Element> getElement (Class<? extends Element> element)
		{
			this.log.trace ("getElementClass: element={}", element);

			Preconditions.checkNotNull (element, "element");

			return this.implementations.get (element);
		}

		/**
		 * Set the default implementation class for the specified
		 * <code>Element</code> interface.
		 *
		 * @param  <T>  The <code>Element</code> interface type
		 * @param  type The <code>Element</code> interface class, not null
		 * @param  impl The <code>Element</code> implementation class, not null
		 * @return      This <code>Builder</code>
		 *
		 * @throws IllegalArgumentException if the there is no
		 *                                  <code>Definition</code> registered
		 *                                  for the implementation class
		 */

		public <T extends Element> Builder setElement (final Class<T> type, final Class<? extends T> impl)
		{
			this.log.trace ("setElement: type={}, impl={}", type, impl);

			Preconditions.checkNotNull (type, "type");
			Preconditions.checkNotNull (impl, "impl");
			Preconditions.checkArgument (Profile.ELEMENT_DEFINITIONS.containsKey (impl), "Element implementation is not registered");

			this.implementations.put (type, impl);

			return this;
		}

		/**
		 * Set the default implementation class for the specified
		 * <code>Element</code> interface from the <code>Definition</code>.
		 *
		 * @param  <T>        The <code>Element</code> interface type
		 * @param  definition The <code>Definition</code>
		 * @return            This <code>Builder</code>
		 */

		public <T extends Element> Builder setElement (final Element.Definition<T> definition)
		{
			this.log.trace ("setElement: definition={}", definition);

			Preconditions.checkNotNull (definition, "definition");

			return this.setElement (definition.getElementType (), definition.getElementClass ());
		}

		/**
		 * Remove the entry for the specified <code>Element</code> class.
		 *
		 * @param  element The <code>Element</code> class, not null
		 * @return         This <code>ProfileBuilder</code>
		 */

		public Builder removeElement (Class<? extends Element> element)
		{
			this.log.trace ("removeElementClass: element={}", element);

			this.implementations.remove (Preconditions.checkNotNull (element, "element"));

			return this;
		}

		/**
		 * Get the <code>Set</code> of <code>Element</code> classes for which an
		 * <code>IdGenerator</code> has been assigned.
		 *
		 * @return A <code>Set</code> of <code>Element</code> classes
		 */

		public Set<Class<? extends Element>> getGenerators ()
		{
			return Collections.unmodifiableSet (this.generators.keySet ());
		}

		/**
		 * Get the <code>IdGenerator</code> associated with the specified
		 * <code>Element</code> class, for the <code>DataStore</code>.
		 *
		 * @param  element The <code>Element</code> class, not null
		 * @return         The associated <code>IdGenerator</code>
		 */

		@CheckReturnValue
		public Class<? extends IdGenerator> getGenerator (final Class<? extends Element> element)
		{
			this.log.trace ("getGenerator: element={}", element);

			return this.generators.get (Preconditions.checkNotNull (element, "element"));
		}

		/**
		 * Register an <code>IdGenerator</code> class to be used to provide ID
		 * numbers for the specified <code>Element</code> class and its
		 * descendants.
		 *
		 * @param  element   The <code>Element</code>, not null
		 * @param  generator The <code>IdGenerator</code>, not null
		 * @return           This <code>ProfileBuilder</code>
		 *
		 * @throws IllegalArgumentException if the there is no
		 *                                  <code>Definition</code> registered
		 *                                  for the <code>IdGenerator</code>
		 */

		public Builder setGenerator (final Class<? extends Element> element, final Class<? extends IdGenerator> generator)
		{
			this.log.trace ("setGenerator: element={}, generator={}", element, generator);

			Preconditions.checkNotNull (element, "element");
			Preconditions.checkNotNull (generator, "generator");
			Preconditions.checkArgument (Profile.GENERATOR_DEFINITIONS.containsKey (generator), "ID Generator is not registered");

			this.generators.put (element, generator);

			return this;
		}

		/**
		 * Remove the <code>IdGenerator</code> for the specified
		 * <code>Element</code> class.  The <code>IdGenerator</code> implementation
		 * assigned the <code>Element</code> is the default <code>IdGnerator</code>
		 * for the <code>DataStore</code> and can not be removed.
		 *
		 * @param  element The <code>Element</code> class, not null
		 * @return         The <code>ProfileBuilder</code>
		 *
		 * @throws IllegalArgumentException if the <code>IdGenerator</code> for
		 *                                  <code>Element</code> is to be removed
		 */

		public Builder removeGenerator (final Class<? extends Element> element)
		{
			this.log.trace ("removeGenerator: element={}", element);

			Preconditions.checkNotNull (element, "element");
			Preconditions.checkArgument (element != Element.class, "Can not remove the IdGenerator for Element");

			this.generators.remove (element);

			return this;
		}

		/**
		 * Get the <code>Set</code> of <code>Parameters</code>.
		 *
		 * @return A <code>Set</code> of <code>Parameter</code> names
		 */

		public Set<String> getParameters ()
		{
			return Collections.unmodifiableSet (this.parameters.keySet ());
		}

		/**
		 * Get the value for the specified parameter.
		 *
		 * @param  parameter The parameter, not null
		 * @return           The associated value
		 */

		@CheckReturnValue
		public String getParameter (final String parameter)
		{
			this.log.trace ("getParameter: parameter={}", parameter);

			return this.parameters.get (Preconditions.checkNotNull (parameter, "parameter"));
		}

		/**
		 * Set the specified parameter to the specified value.
		 *
		 * @param  parameter The parameter, not null
		 * @param  value     The value of the parameter, not null
		 * @return           This <code>Builder</code>.
		 */

		public Builder setParameter (final String parameter, final String value)
		{
			this.log.trace ("setParameter: parameter={}, value={}", parameter, value);

			Preconditions.checkNotNull (parameter, "parameter");
			Preconditions.checkNotNull (value, "value");

			this.parameters.put (parameter, value);

			return this;
		}

		/**
		 * Remove the specified parameter.
		 *
		 * @param  parameter The parameter to remove, not null
		 * @return           This <code>Builder</code>.
		 */

		public Builder removeParameter (final String parameter)
		{
			this.log.trace ("removeParameter: parameter={}", parameter);

			this.parameters.remove (Preconditions.checkNotNull (parameter, "parameter"));

			return this;
		}

		/**
		 * Create the <code>Profile</code>.
		 *
		 * @return The <code>Profile</code>
		 *
		 * @throws IllegalStateException if there is not <code>IdGenerator</code>
		 *                               set for <code>Element</code>
		 * @throws IllegalStateException if the name is empty
		 */

		public Profile build ()
		{
			this.log.trace ("build:");

			Preconditions.checkState (name.length () > 0, "name is Empty");
			Preconditions.checkState (this.generators.containsKey (Element.class), "Missing IDGenerator for Element");

			return new Profile (this);
		}
	}

	/** Definitions for all of the <code>Element</code> implementation classes */
	public static final Map<Class<? extends Element>, Element.Definition<? extends Element>> ELEMENT_DEFINITIONS;

	/** Definitions for all of the <code>IdGenerator</code> implementations */
	public static final Map<Class<? extends IdGenerator>, IdGenerator.Definition> GENERATOR_DEFINITIONS;

	/** The Logger */
	private final Logger log;

	/** Is the <code>DataStore</code> mutable? */
	private final boolean mutable;

	/** The name of the <code>Profile</code> */
	private final String name;

	/** Parameters for the <code>DataStore</code> */
	private final Map<String, String> parameters;

	/** The default implementation classes for each <code>Element</code> */
	private final Map<Class<? extends Element>, Class<? extends Element>> implementations;

	/** The <code>IdGenerator</code> to be used with each <code>Element</code> */
	private final Map<Class<? extends Element>, Class<? extends IdGenerator>> generators;

	/**
	 * static initializer to load the <code>Element</code> and
	 * <code>IdGenerator</code> definitions.
	 */

	static
	{
		GENERATOR_DEFINITIONS = Collections.unmodifiableMap (
				StreamSupport.stream (ServiceLoader.load (IdGenerator.Definition.class).spliterator (), false)
				.collect (Collectors.toMap (x -> x.getIdGeneratorClass (), Function.identity ())));

		ELEMENT_DEFINITIONS = Collections.unmodifiableMap (
				StreamSupport.stream (ServiceLoader.load (Element.Definition.class).spliterator (), false)
				.map (x -> (Element.Definition<? extends Element>) x)
				.collect (Collectors.toMap (x -> x.getElementClass (), Function.identity ())));
	}

	/**
	 * Get the <code>Builder</code> for the <code>Profile</code>.
	 *
	 * @return The <code>Builder</code>
	 */

	public static Builder builder ()
	{
		return new Builder ();
	}

	/**
	 * Get the <code>Builder</code> for the <code>Profile</code>, initialized
	 * with the contents of the specified <code>Profile</code>.
	 *
	 * @param  profile The <code>Profile</code>, not null
	 * @return         The <code>Builder</code>
	 */

	public static Builder builder (final Profile profile)
	{
		return new Builder ()
			.load (Preconditions.checkNotNull (profile, "profile"));
	}

	/**
	 * Load a <code>Profile</code> instance from a <code>URL</code>.
	 *
	 * @param  url The <code>URL</code>, not null
	 * @return     The <code>Profile</code>
	 */

	public static Profile load (final URL url)
	{
		return new Loader ()
			.load (Preconditions.checkNotNull (url, "url"));
	}

	/**
	 * Create the <code>Profile</code>.  This constructor is not intended
	 * to be called directly, the profile should be created though its builder.
	 *
	 * @param  name            The name of the <code>Profile</code>, not null
	 * @param  mutable         The designed mutability of the
	 *                         <code>DataStore</code>, not null
	 * @param  implementations The <code>Map</code> of default
	 *                         <code>Element</code> implementation classes for
	 *                         the <code>DataStore</code> instance, not null
	 * @param  generators      The <code>Map</code> of <code>IdGenerator</code>
	 *                         classes to be used for each
	 *                         <code>Element</code>, not null
	 */

	private Profile (final Builder builder)
	{
		assert builder != null : "builder is NULL";

		this.log = LoggerFactory.getLogger (Profile.class);

		this.name = builder.name;
		this.mutable = builder.mutable;

		this.parameters = Collections.unmodifiableMap (new HashMap<> (builder.parameters));
		this.implementations = Collections.unmodifiableMap (new HashMap<> (builder.implementations));
		this.generators = Collections.unmodifiableMap (new HashMap<> (builder.generators));
	}

	/**
	 * Compare two <code>Profile</code> instances to determine if they are
	 * equal.
	 *
	 * @param  obj The <code>Profile</code> instance to compare to
	 *             the one represented by the called instance
	 *
	 * @return     <code>true</code> if the two <code>Profile</code>
	 *             instances are equal, <code>false</code> otherwise
	 */

	@Override
	public boolean equals (final Object obj)
	{
		return (obj == this) ? true : (obj instanceof Profile)
			&& Objects.equals (this.name, ((Profile) obj).name)
			&& Objects.equals (this.mutable, ((Profile) obj).mutable)
			&& Objects.equals (this.parameters, ((Profile) obj).parameters)
			&& Objects.equals (this.implementations, ((Profile) obj).implementations)
			&& Objects.equals (this.generators, ((Profile) obj).generators);
	}

	/**
	 * Compute a hashCode for the <code>Profile</code>.
	 *
	 * @return An <code>Integer</code> containing the hash code
	 */

	@Override
	public int hashCode ()
	{
		return Objects.hash (this.name, this.mutable, this.parameters, this.implementations, this.generators);
	}

	/**
	 * Get a <code>String</code> representation of the <code>Profile</code>.
	 *
	 * @return A <code>String</code> representation of the <code>Profile</code>
	 */

	@Override
	@CheckReturnValue
	public String toString ()
	{
		return MoreObjects.toStringHelper (this)
			.add ("name", this.name)
			.add ("mutable", this.mutable)
			.add ("parameters", this.parameters)
			.add ("implementations", this.implementations)
			.add ("generators", this.generators)
			.toString ();
	}

	/**
	 * Get the name of the <code>Profile</code>.
	 *
	 * @return The name of the <code>Profile</code>
	 */

	public String getName ()
	{
		return this.name;
	}

	/**
	 * Determine if the <code>DataStore</code> can be changed.
	 *
	 * @return <code>true</code> if the <code>DataStore</code> can be changed,
	 *         <code>false</code> otherwise
	 */

	public boolean isMutable ()
	{
		return this.mutable;
	}

	/**
	 * Determine if there is an implementation class registered for the
	 * specified <code>Element</code> interface class.
	 *
	 * @param  element The <code>Element</code> interface class, not null
	 * @return         <code>true</code> if the <code>Element</code> has a
	 *                 registered implementation class, <code>false</code>
	 *                 otherwise
	 */

	public boolean hasElement (final Class<? extends Element> element)
	{
		this.log.trace ("hasElement: element={}", element);

		return this.implementations.containsKey (Preconditions.checkNotNull (element, "element"));
	}

	/**
	 * Determine if there is a <code>Definition</code> registered for the
	 * specified <code>Element</code> class.  The specified <code>Element</code>
	 * class may be either an interface class or an implementation class.
	 *
	 * @param  element The <code>Element</code> class, not null
	 * @return         <code>true</code> if the <code>Element</code> has a
	 *                 <code>Definition</code>, <code>false</code> otherwise
	 */

	public boolean hasDefinition (final Class<? extends Element> element)
	{
		this.log.trace ("hasDefinition: element={}", element);

		Preconditions.checkNotNull (element, "element");

		return (Profile.ELEMENT_DEFINITIONS.containsKey (element))
			? true
			: this.implementations.containsKey (element)
				&& Profile.ELEMENT_DEFINITIONS.containsKey (this.implementations.get (element));
	}

	/**
	 * Get a <code>Map</code> containing parameters for the
	 * <code>DataStore</code>.
	 *
	 * @return The parameter <code>Map</code>
	 */

	public Map<String, String> getParameters ()
	{
		return this.parameters;
	}

	/**
	 * Get the <code>Definition</code> for the specified <code>Element</code>.
	 * The specified <code>Element</code> class may be either an interface class
	 * or an implementation class.  For an interface class the
	 * <code>Definition</code> for the default implementation will be returned.
	 *
	 * @param  <T>     The type of the <code>Element</code>
	 * @param  element The <code>Element</code> class, not null
	 * @return         The <code>Definition</code> for the <code>Element</code>
	 *
	 * @throws IllegalArgumentException If the specified <code>Element</code>
	 *                                  class does not have a registered
	 *                                  implementation
	 */

	@SuppressWarnings ("unchecked")
	public <T extends Element> Element.Definition<T> getDefinition (final Class<T> element)
	{
		this.log.trace ("getDefinition: element={}", element);

		Preconditions.checkNotNull (element, "element");

		Element.Definition<T> result = (Element.Definition<T>) Profile.ELEMENT_DEFINITIONS.get (element);

		if (result == null)
		{
			Preconditions.checkArgument (this.implementations.containsKey (element), "No implementation registered for: %s", element.getSimpleName ());

			assert Profile.ELEMENT_DEFINITIONS.containsKey (this.implementations.get (element)) : "implementation class is not registered";

			result = (Element.Definition<T>) Profile.ELEMENT_DEFINITIONS.get (this.implementations.get (element));
		}

		return result;
	}

	/**
	 * Get the <code>Definition</code> for the specified <code>Element</code>
	 * implementation.
	 *
	 * @param  <T>     The type of the <code>Element</code>
	 * @param  element The <code>Element</code> interface class, not null
	 * @param  impl    The <code>Element</code> implementation class, not null
	 * @return         The <code>Definition</code> for the <code>Element</code>
	 *
	 * @throws IllegalArgumentException If the implementation class does not
	 *                                  have a <code>Definition</code>
	 */

	@SuppressWarnings ("unchecked")
	public <T extends Element> Element.Definition<T> getDefinition (final Class<T> element, final Class<? extends T> impl)
	{
		this.log.trace ("getDefinition: element={}, impl={}", element, impl);

		Preconditions.checkNotNull (element, "element");
		Preconditions.checkNotNull (impl, "impl");
		Preconditions.checkArgument (Profile.ELEMENT_DEFINITIONS.containsKey (impl), "implementation class does not have a definition");

		return (Element.Definition<T>) Profile.ELEMENT_DEFINITIONS.get (impl);
	}

	/**
	 * Determine if there is an <code>IdGenerator</code> registered for the
	 * specified <code>Element</code> class.
	 *
	 * @param  element The <code>Element</code> class, not null
	 * @return         <code>true</code> if there is an <code>IdGenerator</code>
	 *                 registered for the <code>Element</code>,
	 *                 <code>false</code> otherwise
	 */

	public boolean hasGenerator (final Class<? extends Element> element)
	{
		return this.generators.containsKey (Preconditions.checkNotNull (element, "element"));
	}

	/**
	 * Get the <code>IdGeneratorComponent</code> associated with the specified
	 * <code>Element</code> class, for the <code>DomainModel</code>.
	 *
	 * @param  model   The <code>DomainModel</code>, not null
	 * @param  element The <code>Element</code> class, not null
	 * @return         The associated <code>IdGenerator</code>
	 *
	 * @throws IllegalArgumentException if there is no <code>IdGenerator</code>
	 *                                  associated with the specified
	 *                                  <code>Element</code> class
	 */

	public IdGenerator.IdGeneratorComponent getGenerator (final DomainModel model, final Class<? extends Element> element)
	{
		this.log.trace ("getGenerator: model={}, element={}", model, element);

		Preconditions.checkNotNull (model, "model");
		Preconditions.checkNotNull (element, "element");
		Preconditions.checkArgument (this.generators.containsKey (element),
				"No Generator registered for Element: %s", element.getSimpleName ());

		assert Profile.GENERATOR_DEFINITIONS.containsKey (this.generators.get (element))
			: "Generator does not have a definition";

		return Profile.GENERATOR_DEFINITIONS.get (this.generators.get (element))
			.createComponent (model, element);
	}
}
