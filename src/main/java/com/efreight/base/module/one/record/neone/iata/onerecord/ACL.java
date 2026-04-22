package com.efreight.base.module.one.record.neone.iata.onerecord;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

/**
 * Namespace ACL.
 * Prefix: {@code <http://www.w3.org/ns/auth/acl#>}
 */
public class ACL {

	/** {@code http://www.w3.org/ns/auth/acl#} **/
	public static final String NAMESPACE = "http://www.w3.org/ns/auth/acl#";

	/** {@code acl} **/
	public static final String PREFIX = "acl";

	/**
	 * access
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#Access}.
	 * <p>
	 * Any kind of access to a resource. Don't use this, use R W and RW
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#Access">Access</a>
	 */
	public static final IRI Access;

	/**
	 * access control
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#accessControl}.
	 * <p>
	 * The Access Control file for this information resource. This may of
	 * course be a virtual resource implemented by the access control system.
	 * Note that HTTP header `Link: <foo.acl>; rel="acl"` can also be used
	 * for this.
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#accessControl">accessControl</a>
	 */
	public static final IRI accessControl;

	/**
	 * to
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#accessTo}.
	 * <p>
	 * The information resource to which access is being granted.
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#accessTo">accessTo</a>
	 */
	public static final IRI accessTo;

	/**
	 * to all in
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#accessToClass}.
	 * <p>
	 * A class of information resources to which access is being granted.
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#accessToClass">accessToClass</a>
	 */
	public static final IRI accessToClass;

	/**
	 * agent
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#agent}.
	 * <p>
	 * A person or social entity to being given the right
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#agent">agent</a>
	 */
	public static final IRI agent;

	/**
	 * agent class
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#agentClass}.
	 * <p>
	 * A class of persons or social entities to being given the right
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#agentClass">agentClass</a>
	 */
	public static final IRI agentClass;

	/**
	 * agent group
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#agentGroup}.
	 * <p>
	 * A group of persons or social entities to being given the right. The
	 * right is given to any entity which is a vcard:member of the group, as
	 * defined by the document received when the Group is dereferenced.
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#agentGroup">agentGroup</a>
	 */
	public static final IRI agentGroup;

	/**
	 * append
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#Append}.
	 * <p>
	 * Append accesses are specific write access which only add information,
	 * and do not remove information. For text files, for example, append
	 * access allows bytes to be added onto the end of the file. For RDF
	 * graphs, Append access allows adds triples to the graph but does not
	 * remove any. Append access is useful for dropbox functionality. Dropbox
	 * can be used for link notification, which the information added is a
	 * notification that a some link has been made elsewhere relevant to the
	 * given resource. 
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#Append">Append</a>
	 */
	public static final IRI Append;

	/**
	 * Anyone authenticated
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#AuthenticatedAgent}.
	 * <p>
	 * A class of agents who have been authenticated. In other words, anyone
	 * can access this resource, but not anonymously. The social expectation
	 * is that the authentication process will provide an identify and a
	 * name, or pseudonym. (A new ID should not be minted for every access:
	 * the intent is that the user is able to continue to use the ID for
	 * continues interactions with peers, and for example to develop a
	 * reputation) 
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#AuthenticatedAgent">AuthenticatedAgent</a>
	 */
	public static final IRI AuthenticatedAgent;

	/**
	 * authorization
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#Authorization}.
	 * <p>
	 * An element of access control, allowing agent to agents access of some
	 * kind to resources or classes of resources
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#Authorization">Authorization</a>
	 */
	public static final IRI Authorization;

	/**
	 * control
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#Control}.
	 * <p>
	 * Allows read/write access to the ACL for the resource(s)
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#Control">Control</a>
	 */
	public static final IRI Control;

	/**
	 * default access for things in this
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#default}.
	 * <p>
	 * If a resource has no ACL file (it is 404), then access to the resource
	 * is given by the ACL of the immediately containing directory, or
	 * failing that (404) the ACL of the recursively next containing
	 * directory which has an ACL file. Within that ACL file, any
	 * Authorization which has that directory as its acl:default applies to
	 * the resource. (The highest directory must have an ACL file.) 
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#default">default</a>
	 */
	public static final IRI _default;

	/**
	 * default access for new things in the object
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#defaultForNew}.
	 * <p>
	 * THIS IS OBSOLETE AS OF 2017-08-01. See 'default'. Was: A directory for
	 * which this authorization is used for new files in the directory.
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#defaultForNew">defaultForNew</a>
	 */
	public static final IRI defaultForNew;

	/**
	 * delegates
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#delegates}.
	 * <p>
	 * Delegates a person or another agent to act on behalf of the agent. For
	 * example, Alice delegates Bob to act on behalf of Alice for ACL
	 * purposes.
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#delegates">delegates</a>
	 */
	public static final IRI delegates;

	/**
	 * access mode
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#mode}.
	 * <p>
	 * A mode of access such as read or write.
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#mode">mode</a>
	 */
	public static final IRI mode;

	/**
	 * Origin
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#Origin}.
	 * <p>
	 * An Origin is basically a web site (Note WITHOUT the trailing slash
	 * after the domain name and port in its URI) and is the basis for
	 * controlling access to data by web apps in the Same Origin Model of web
	 * security. All scripts from the same origin are given the same right.
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#Origin">Origin</a>
	 */
	public static final IRI Origin;

	/**
	 * origin
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#origin}.
	 * <p>
	 * A web application, identified by its Origin, such as
	 * <https://scripts.example.com>, being given the right. When a user of
	 * the web application at a certain origin accesses the server, then the
	 * browser sets the Origin: header to warn that a possibly untrusted
	 * webapp is being used. Then, BOTH the user AND the origin must have the
	 * required access.
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#origin">origin</a>
	 */
	public static final IRI origin;

	/**
	 * owner
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#owner}.
	 * <p>
	 * The person or other agent which owns this. For example, the owner of a
	 * file in a filesystem. There is a sense of "right to control".
	 * Typically defaults to the agent who created something, but can be
	 * changed.
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#owner">owner</a>
	 */
	public static final IRI owner;

	/**
	 * read
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#Read}.
	 * <p>
	 * The class of read operations
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#Read">Read</a>
	 */
	public static final IRI Read;

	/**
	 * write
	 * <p>
	 * {@code http://www.w3.org/ns/auth/acl#Write}.
	 *
	 * @see <a href="http://www.w3.org/ns/auth/acl#Write">Write</a>
	 */
	public static final IRI Write;

	static {
		ValueFactory factory = SimpleValueFactory.getInstance();

		Access = factory.createIRI(ACL.NAMESPACE, "Access");
		accessControl = factory.createIRI(ACL.NAMESPACE, "accessControl");
		accessTo = factory.createIRI(ACL.NAMESPACE, "accessTo");
		accessToClass = factory.createIRI(ACL.NAMESPACE, "accessToClass");
		agent = factory.createIRI(ACL.NAMESPACE, "agent");
		agentClass = factory.createIRI(ACL.NAMESPACE, "agentClass");
		agentGroup = factory.createIRI(ACL.NAMESPACE, "agentGroup");
		Append = factory.createIRI(ACL.NAMESPACE, "Append");
		AuthenticatedAgent = factory.createIRI(ACL.NAMESPACE, "AuthenticatedAgent");
		Authorization = factory.createIRI(ACL.NAMESPACE, "Authorization");
		Control = factory.createIRI(ACL.NAMESPACE, "Control");
		_default = factory.createIRI(ACL.NAMESPACE, "default");
		defaultForNew = factory.createIRI(ACL.NAMESPACE, "defaultForNew");
		delegates = factory.createIRI(ACL.NAMESPACE, "delegates");
		mode = factory.createIRI(ACL.NAMESPACE, "mode");
		Origin = factory.createIRI(ACL.NAMESPACE, "Origin");
		origin = factory.createIRI(ACL.NAMESPACE, "origin");
		owner = factory.createIRI(ACL.NAMESPACE, "owner");
		Read = factory.createIRI(ACL.NAMESPACE, "Read");
		Write = factory.createIRI(ACL.NAMESPACE, "Write");
	}

	private ACL() {
		//static access only
	}

}
