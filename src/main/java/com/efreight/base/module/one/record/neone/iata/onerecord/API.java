package com.efreight.base.module.one.record.neone.iata.onerecord;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

/**
 * Namespace API.
 * Prefix: {@code <https://onerecord.iata.org/ns/api#>}
 */
public class API {

	/** {@code https://onerecord.iata.org/ns/api#} **/
	public static final String NAMESPACE = "https://onerecord.iata.org/ns/api#";

	/** {@code api} **/
	public static final String PREFIX = "api";

	/**
	 * ACCESS_DELEGATION_REQUEST_ACCEPTED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#ACCESS_DELEGATION_REQUEST_ACCEPTED}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#ACCESS_DELEGATION_REQUEST_ACCEPTED">ACCESS_DELEGATION_REQUEST_ACCEPTED</a>
	 */
	public static final IRI ACCESS_DELEGATION_REQUEST_ACCEPTED;

	/**
	 * ACCESS_DELEGATION_REQUEST_FAILED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#ACCESS_DELEGATION_REQUEST_FAILED}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#ACCESS_DELEGATION_REQUEST_FAILED">ACCESS_DELEGATION_REQUEST_FAILED</a>
	 */
	public static final IRI ACCESS_DELEGATION_REQUEST_FAILED;

	/**
	 * ACCESS_DELEGATION_REQUEST_PENDING
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#ACCESS_DELEGATION_REQUEST_PENDING}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#ACCESS_DELEGATION_REQUEST_PENDING">ACCESS_DELEGATION_REQUEST_PENDING</a>
	 */
	public static final IRI ACCESS_DELEGATION_REQUEST_PENDING;

	/**
	 * ACCESS_DELEGATION_REQUEST_REJECTED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#ACCESS_DELEGATION_REQUEST_REJECTED}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#ACCESS_DELEGATION_REQUEST_REJECTED">ACCESS_DELEGATION_REQUEST_REJECTED</a>
	 */
	public static final IRI ACCESS_DELEGATION_REQUEST_REJECTED;

	/**
	 * ACCESS_DELEGATION_REQUEST_REVOKED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#ACCESS_DELEGATION_REQUEST_REVOKED}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#ACCESS_DELEGATION_REQUEST_REVOKED">ACCESS_DELEGATION_REQUEST_REVOKED</a>
	 */
	public static final IRI ACCESS_DELEGATION_REQUEST_REVOKED;

	/**
	 * Access Delegation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#AccessDelegation}.
	 * <p>
	 * Access to a Logistics Object delegated to an Organization
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#AccessDelegation">AccessDelegation</a>
	 */
	public static final IRI AccessDelegation;

	/**
	 * Access Delegation Request
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#AccessDelegationRequest}.
	 * <p>
	 * Delegation Request to 3rd parties
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#AccessDelegationRequest">AccessDelegationRequest</a>
	 */
	public static final IRI AccessDelegationRequest;

	/**
	 * Access Permissions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#AccessPermissions}.
	 * <p>
	 * Access permissions granted to a logistics agent for a logistics object
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#AccessPermissions">AccessPermissions</a>
	 */
	public static final IRI AccessPermissions;

	/**
	 * Action Request
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#ActionRequest}.
	 * <p>
	 * Superclass for all kinds of requests (i.e someone requsted something
	 * (e.g. subscription, access, etc.) at a publisher/holder of a logistics
	 * object)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#ActionRequest">ActionRequest</a>
	 */
	public static final IRI ActionRequest;

	/**
	 * ADD
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#ADD}.
	 * <p>
	 * Defines a :PatchOperation to be an operation that adds new triples.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#ADD">ADD</a>
	 */
	public static final IRI ADD;

	/**
	 * Audit Trail
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#AuditTrail}.
	 * <p>
	 * Audit trail of a Logistics Object
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#AuditTrail">AuditTrail</a>
	 */
	public static final IRI AuditTrail;

	/**
	 * Change
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#Change}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#Change">Change</a>
	 */
	public static final IRI Change;

	/**
	 * CHANGE_REQUEST_ACCEPTED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#CHANGE_REQUEST_ACCEPTED}.
	 * <p>
	 * :EventType for accepted :ChangeRequests
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#CHANGE_REQUEST_ACCEPTED">CHANGE_REQUEST_ACCEPTED</a>
	 */
	public static final IRI CHANGE_REQUEST_ACCEPTED;

	/**
	 * CHANGE_REQUEST_FAILED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#CHANGE_REQUEST_FAILED}.
	 * <p>
	 * :EventType for failed :ChangeRequests.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#CHANGE_REQUEST_FAILED">CHANGE_REQUEST_FAILED</a>
	 */
	public static final IRI CHANGE_REQUEST_FAILED;

	/**
	 * CHANGE_REQUEST_PENDING
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#CHANGE_REQUEST_PENDING}.
	 * <p>
	 * :EventType for pending :ChangeRequests.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#CHANGE_REQUEST_PENDING">CHANGE_REQUEST_PENDING</a>
	 */
	public static final IRI CHANGE_REQUEST_PENDING;

	/**
	 * CHANGE_REQUEST_PENDING
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#CHANGE_REQUEST_REJECTED}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#CHANGE_REQUEST_REJECTED">CHANGE_REQUEST_REJECTED</a>
	 */
	public static final IRI CHANGE_REQUEST_REJECTED;

	/**
	 * CHANGE_REQUEST_REVOKED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#CHANGE_REQUEST_REVOKED}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#CHANGE_REQUEST_REVOKED">CHANGE_REQUEST_REVOKED</a>
	 */
	public static final IRI CHANGE_REQUEST_REVOKED;

	/**
	 * Change Request
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#ChangeRequest}.
	 * <p>
	 * Change Request containing updates on a Logistics Object
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#ChangeRequest">ChangeRequest</a>
	 */
	public static final IRI ChangeRequest;

	/**
	 * Collection
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#Collection}.
	 * <p>
	 * Used as response of endpoints returning a collection of more than one
	 * graph, i.e. more than one not linked subjects.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#Collection">Collection</a>
	 */
	public static final IRI Collection;

	/**
	 * DELETE
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#DELETE}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#DELETE">DELETE</a>
	 */
	public static final IRI DELETE;

	/**
	 * Error
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#Error}.
	 * <p>
	 * Error model
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#Error">Error</a>
	 */
	public static final IRI Error;

	/**
	 * Error Detail
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#ErrorDetail}.
	 * <p>
	 * Error details that belong to an error
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#ErrorDetail">ErrorDetail</a>
	 */
	public static final IRI ErrorDetail;

	/**
	 * Expires at
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#expiresAt}.
	 * <p>
	 * Expiry date as date time of the subscription information that supports
	 * caching but does not require the ONE Record client to store the
	 * datetime when the Subscription object was received; if not given: the
	 * information does not expire
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#expiresAt">expiresAt</a>
	 */
	public static final IRI expiresAt;

	/**
	 * GET_LOGISTICS_EVENT
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#GET_LOGISTICS_EVENT}.
	 * <p>
	 * :Permission to get a :LogisticsEvent
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#GET_LOGISTICS_EVENT">GET_LOGISTICS_EVENT</a>
	 */
	public static final IRI GET_LOGISTICS_EVENT;

	/**
	 * GET_LOGISTICS_OBJECT
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#GET_LOGISTICS_OBJECT}.
	 * <p>
	 * :Permission to get a :LogisticsObject
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#GET_LOGISTICS_OBJECT">GET_LOGISTICS_OBJECT</a>
	 */
	public static final IRI GET_LOGISTICS_OBJECT;

	/**
	 * has Access Delegation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasAccessDelegation}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasAccessDelegation">hasAccessDelegation</a>
	 */
	public static final IRI hasAccessDelegation;

	/**
	 * has Change
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasChange}.
	 * <p>
	 * Contains submitted Change object
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasChange">hasChange</a>
	 */
	public static final IRI hasChange;

	/**
	 * Changed property
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasChangedProperty}.
	 * <p>
	 * List of all changed properties as IRIs after a ChangeRequest was
	 * successfully applied, e.g.
	 * [https://onerecord.iata.org/ns/cargo#hasVolumetricWeight,
	 * https://onerecord.iata.org/ns/cargo/#hasGoodsDescription]
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasChangedProperty">hasChangedProperty</a>
	 */
	public static final IRI hasChangedProperty;

	/**
	 * has Change Request
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasChangeRequest}.
	 * <p>
	 * Recorded change requests in the Audit Trail of a Logistics Object
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasChangeRequest">hasChangeRequest</a>
	 */
	public static final IRI hasChangeRequest;

	/**
	 * Code
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasCode}.
	 * <p>
	 * Error code is a numeric or alphanumeric code that can be used to
	 * determine the source of the error and why it occured.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasCode">hasCode</a>
	 */
	public static final IRI hasCode;

	/**
	 * Content type
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasContentType}.
	 * <p>
	 * Content types that the subscriber wants to receive in the
	 * notifications, e.g. application/ld+json
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasContentType">hasContentType</a>
	 */
	public static final IRI hasContentType;

	/**
	 * has data holder
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasDataHolder}.
	 * <p>
	 * The data holder of the servers data.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasDataHolder">hasDataHolder</a>
	 */
	public static final IRI hasDataHolder;

	/**
	 * Datatype
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasDatatype}.
	 * <p>
	 * Data type of the field to update, must be a valid URI, e.g.
	 * http://www.w3.org/2001/XMLSchema#int
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasDatatype">hasDatatype</a>
	 */
	public static final IRI hasDatatype;

	/**
	 * Description
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasDescription}.
	 * <p>
	 * Reason for the request (optional)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasDescription">hasDescription</a>
	 */
	public static final IRI hasDescription;

	/**
	 * has Error
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasError}.
	 * <p>
	 * Error object(s) if the processing was not successful
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasError">hasError</a>
	 */
	public static final IRI hasError;

	/**
	 * has Error Detail
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasErrorDetail}.
	 * <p>
	 * Error details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasErrorDetail">hasErrorDetail</a>
	 */
	public static final IRI hasErrorDetail;

	/**
	 * has Event Type
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasEventType}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasEventType">hasEventType</a>
	 */
	public static final IRI hasEventType;

	/**
	 * has Item
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasItem}.
	 * <p>
	 * Item that is contained in a collection
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasItem">hasItem</a>
	 */
	public static final IRI hasItem;

	/**
	 * Latest revision
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasLatestRevision}.
	 * <p>
	 * Latest revision of the Logistics Object. Starting with revision 0
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasLatestRevision">hasLatestRevision</a>
	 */
	public static final IRI hasLatestRevision;

	/**
	 * {@code https://onerecord.iata.org/ns/api#hasLogisticsAgent}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasLogisticsAgent">hasLogisticsAgent</a>
	 */
	public static final IRI hasLogisticsAgent;

	/**
	 * has Logistics Object
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasLogisticsObject}.
	 * <p>
	 * A reference to a cargo:LogisticsObject.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasLogisticsObject">hasLogisticsObject</a>
	 */
	public static final IRI hasLogisticsObject;

	/**
	 * has Logistics Object Type
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasLogisticsObjectType}.
	 * <p>
	 * The type of cargo:LogisticsObject in the notification e.g.
	 * https://onerecord.iata.org/ns/cargo#Piece
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasLogisticsObjectType">hasLogisticsObjectType</a>
	 */
	public static final IRI hasLogisticsObjectType;

	/**
	 * Message
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasMessage}.
	 * <p>
	 * Message that describes the error
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasMessage">hasMessage</a>
	 */
	public static final IRI hasMessage;

	/**
	 * has Operation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasOperation}.
	 * <p>
	 * Operation(s) to apply as PATCH on a Logistics Object
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasOperation">hasOperation</a>
	 */
	public static final IRI hasOperation;

	/**
	 * has Permission
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasPermission}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasPermission">hasPermission</a>
	 */
	public static final IRI hasPermission;

	/**
	 * Property
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasProperty}.
	 * <p>
	 * Property of the object for which the error applies in IRI format, i.e.
	 * https://onerecord.iata.org/ns/cargo#hasVolumetricWeight
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasProperty">hasProperty</a>
	 */
	public static final IRI hasProperty;

	/**
	 * {@code https://onerecord.iata.org/ns/api#hasRequestStatus}.
	 * <p>
	 * has Request Status
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasRequestStatus">hasRequestStatus</a>
	 */
	public static final IRI hasRequestStatus;

	/**
	 * Resource
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasResource}.
	 * <p>
	 * URI of the object where the error occurred
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasResource">hasResource</a>
	 */
	public static final IRI hasResource;

	/**
	 * Revision
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasRevision}.
	 * <p>
	 * Revision number of the Logistics Object, starting with 0 for changing
	 * the initial revision of a Logistics Object
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasRevision">hasRevision</a>
	 */
	public static final IRI hasRevision;

	/**
	 * Server endpoint
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasServerEndpoint}.
	 * <p>
	 * ONE Record API endpoint
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasServerEndpoint">hasServerEndpoint</a>
	 */
	public static final IRI hasServerEndpoint;

	/**
	 * has Subscriber
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasSubscriber}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasSubscriber">hasSubscriber</a>
	 */
	public static final IRI hasSubscriber;

	/**
	 * has Subscription
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasSubscription}.
	 * <p>
	 * Link to the requestors Subscription object with all subscription
	 * information
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasSubscription">hasSubscription</a>
	 */
	public static final IRI hasSubscription;

	/**
	 * Supported API version
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasSupportedApiVersion}.
	 * <p>
	 * Supported ONE Record API versions by the server, MUST include at least
	 * one supported version.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasSupportedApiVersion">hasSupportedApiVersion</a>
	 */
	public static final IRI hasSupportedApiVersion;

	/**
	 * Supported content type
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasSupportedContentType}.
	 * <p>
	 * Supported content types of the server, MUST contain at least
	 * application/ld+json
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasSupportedContentType">hasSupportedContentType</a>
	 */
	public static final IRI hasSupportedContentType;

	/**
	 * Supported encoding
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasSupportedEncoding}.
	 * <p>
	 * Optional list of supported encodings of the ONE Record server, e.g.
	 * gzip
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasSupportedEncoding">hasSupportedEncoding</a>
	 */
	public static final IRI hasSupportedEncoding;

	/**
	 * Supported language
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasSupportedLanguage}.
	 * <p>
	 * Supported languages of the ONE Record API, minimum is en-US (American
	 * English)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasSupportedLanguage">hasSupportedLanguage</a>
	 */
	public static final IRI hasSupportedLanguage;

	/**
	 * Supported ontology
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasSupportedOntology}.
	 * <p>
	 * Supported ontologies on the server, MUST be non-versioned IRIs
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasSupportedOntology">hasSupportedOntology</a>
	 */
	public static final IRI hasSupportedOntology;

	/**
	 * Supported ontology version
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasSupportedOntologyVersion}.
	 * <p>
	 * Supported ontology versions on the server, MUST be versioned IRIs
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasSupportedOntologyVersion">hasSupportedOntologyVersion</a>
	 */
	public static final IRI hasSupportedOntologyVersion;

	/**
	 * Title
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasTitle}.
	 * <p>
	 * Short summary of the error
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasTitle">hasTitle</a>
	 */
	public static final IRI hasTitle;

	/**
	 * Topic
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasTopic}.
	 * <p>
	 * The Logistics Object type or specific Logistics Object to which the
	 * subscription belongs to e.g. https://onerecord.iata.org/Piece or
	 * https://1r.example.com/7f01363f-0c6a-4414-be48-d3692e219b91
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasTopic">hasTopic</a>
	 */
	public static final IRI hasTopic;

	/**
	 * {@code https://onerecord.iata.org/ns/api#hasTopicType}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasTopicType">hasTopicType</a>
	 */
	public static final IRI hasTopicType;

	/**
	 * Total items
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasTotalItems}.
	 * <p>
	 * The number of total items contained in a collection
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasTotalItems">hasTotalItems</a>
	 */
	public static final IRI hasTotalItems;

	/**
	 * Value
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#hasValue}.
	 * <p>
	 * Updated value for the field
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#hasValue">hasValue</a>
	 */
	public static final IRI hasValue;

	/**
	 * Include SubscriptionEventType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#includeSubscriptionEventType}.
	 * <p>
	 * An array used to indicate the specific types of notifications that the
	 * subscriber desires to receive from the publisher. The subscriber is
	 * required to specify their preferences on a per-type basis
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#includeSubscriptionEventType">includeSubscriptionEventType</a>
	 */
	public static final IRI includeSubscriptionEventType;

	/**
	 * Requested at
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#isRequestedAt}.
	 * <p>
	 * Datetime when the request was created
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#isRequestedAt">isRequestedAt</a>
	 */
	public static final IRI isRequestedAt;

	/**
	 * is Requested By
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#isRequestedBy}.
	 * <p>
	 * Organization Identifier that represents the Organization that has
	 * requested the action
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#isRequestedBy">isRequestedBy</a>
	 */
	public static final IRI isRequestedBy;

	/**
	 * is requested for
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#isRequestedFor}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#isRequestedFor">isRequestedFor</a>
	 */
	public static final IRI isRequestedFor;

	/**
	 * Revoked at
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#isRevokedAt}.
	 * <p>
	 * The datetime when the action request was revoked.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#isRevokedAt">isRevokedAt</a>
	 */
	public static final IRI isRevokedAt;

	/**
	 * is revoked by
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#isRevokedBy}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#isRevokedBy">isRevokedBy</a>
	 */
	public static final IRI isRevokedBy;

	/**
	 * is triggered by
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#isTriggeredBy}.
	 * <p>
	 * Optional URI to the ChangeRequest that triggered a Notification if the
	 * eventType is one of CHANGE_REQUEST_ACCEPTED, CHANGE_REQUEST_REJECT, or
	 * CHANGE_REQUEST_FAILED
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#isTriggeredBy">isTriggeredBy</a>
	 */
	public static final IRI isTriggeredBy;

	/**
	 * LOGISTICS_EVENT_RECEIVED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#LOGISTICS_EVENT_RECEIVED}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#LOGISTICS_EVENT_RECEIVED">LOGISTICS_EVENT_RECEIVED</a>
	 */
	public static final IRI LOGISTICS_EVENT_RECEIVED;

	/**
	 * LOGISTICS_OBJECT_CREATED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#LOGISTICS_OBJECT_CREATED}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#LOGISTICS_OBJECT_CREATED">LOGISTICS_OBJECT_CREATED</a>
	 */
	public static final IRI LOGISTICS_OBJECT_CREATED;

	/**
	 * LOGISTICS_OBJECT_IDENTIFIER
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#LOGISTICS_OBJECT_IDENTIFIER}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#LOGISTICS_OBJECT_IDENTIFIER">LOGISTICS_OBJECT_IDENTIFIER</a>
	 */
	public static final IRI LOGISTICS_OBJECT_IDENTIFIER;

	/**
	 * LOGISTICS_OBJECT_TYPE
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#LOGISTICS_OBJECT_TYPE}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#LOGISTICS_OBJECT_TYPE">LOGISTICS_OBJECT_TYPE</a>
	 */
	public static final IRI LOGISTICS_OBJECT_TYPE;

	/**
	 * LOGISTICS_OBJECT_UPDATED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#LOGISTICS_OBJECT_UPDATED}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#LOGISTICS_OBJECT_UPDATED">LOGISTICS_OBJECT_UPDATED</a>
	 */
	public static final IRI LOGISTICS_OBJECT_UPDATED;

	/**
	 * Notification
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#Notification}.
	 * <p>
	 * Notification sent by the publisher to the subscriber
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#Notification">Notification</a>
	 */
	public static final IRI Notification;

	/**
	 * Notification Event Type
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#NotificationEventType}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#NotificationEventType">NotificationEventType</a>
	 */
	public static final IRI NotificationEventType;

	/**
	 * notify RequestStatus Change
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#notifyRequestStatusChange}.
	 * <p>
	 * Flag specifying if the requestor wants to receive Notification from
	 * the publisher when the status of an action request changed,
	 * default=FALSE
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#notifyRequestStatusChange">notifyRequestStatusChange</a>
	 */
	public static final IRI notifyRequestStatusChange;

	/**
	 * {@code https://onerecord.iata.org/ns/api#o}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#o">o</a>
	 */
	public static final IRI o;

	/**
	 * {@code https://onerecord.iata.org/ns/api#op}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#op">op</a>
	 */
	public static final IRI op;

	/**
	 * Operation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#Operation}.
	 * <p>
	 * Operation Request contained in the PATCH body
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#Operation">Operation</a>
	 */
	public static final IRI Operation;

	/**
	 * Operation Object
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#OperationObject}.
	 * <p>
	 * Object to modify in the PATCH request
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#OperationObject">OperationObject</a>
	 */
	public static final IRI OperationObject;

	/**
	 * p
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#p}.
	 * <p>
	 * Operations objects must have exactly one p, predicate, member. The
	 * value of this member must be an URI, e.g.
	 * https://onerecord.iata.org/ns/cargo#hasGoodsDescription
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#p">p</a>
	 */
	public static final IRI p;

	/**
	 * PATCH_LOGISTICS_OBJECT
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#PATCH_LOGISTICS_OBJECT}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#PATCH_LOGISTICS_OBJECT">PATCH_LOGISTICS_OBJECT</a>
	 */
	public static final IRI PATCH_LOGISTICS_OBJECT;

	/**
	 * Patch Operation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#PatchOperation}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#PatchOperation">PatchOperation</a>
	 */
	public static final IRI PatchOperation;

	/**
	 * Permission
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#Permission}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#Permission">Permission</a>
	 */
	public static final IRI Permission;

	/**
	 * POST_LOGISTICS_EVENT
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#POST_LOGISTICS_EVENT}.
	 * <p>
	 * :Permission to add a logistics event.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#POST_LOGISTICS_EVENT">POST_LOGISTICS_EVENT</a>
	 */
	public static final IRI POST_LOGISTICS_EVENT;

	/**
	 * REQUEST_ACCEPTED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#REQUEST_ACCEPTED}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#REQUEST_ACCEPTED">REQUEST_ACCEPTED</a>
	 */
	public static final IRI REQUEST_ACCEPTED;

	/**
	 * REQUEST_FAILED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#REQUEST_FAILED}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#REQUEST_FAILED">REQUEST_FAILED</a>
	 */
	public static final IRI REQUEST_FAILED;

	/**
	 * REQUEST_PENDING
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#REQUEST_PENDING}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#REQUEST_PENDING">REQUEST_PENDING</a>
	 */
	public static final IRI REQUEST_PENDING;

	/**
	 * REQUEST_REJECTED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#REQUEST_REJECTED}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#REQUEST_REJECTED">REQUEST_REJECTED</a>
	 */
	public static final IRI REQUEST_REJECTED;

	/**
	 * REQUEST_REVOKED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#REQUEST_REVOKED}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#REQUEST_REVOKED">REQUEST_REVOKED</a>
	 */
	public static final IRI REQUEST_REVOKED;

	/**
	 * RequestStatus
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#RequestStatus}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#RequestStatus">RequestStatus</a>
	 */
	public static final IRI RequestStatus;

	/**
	 * s
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#s}.
	 * <p>
	 * Operation objects MUST have exactly one "s", subject, member. The
	 * value of this member MUST be one of IRI or blank node.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#s">s</a>
	 */
	public static final IRI s;

	/**
	 * Send LogisticsObject body
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#sendLogisticsObjectBody}.
	 * <p>
	 * Flag specifying if the publisher should send the whole logistics
	 * object or not in the notification object
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#sendLogisticsObjectBody">sendLogisticsObjectBody</a>
	 */
	public static final IRI sendLogisticsObjectBody;

	/**
	 * Server Information
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#ServerInformation}.
	 * <p>
	 * Information about the ONE Record server
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#ServerInformation">ServerInformation</a>
	 */
	public static final IRI ServerInformation;

	/**
	 * Subscription
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#Subscription}.
	 * <p>
	 * Subscription information sent to the publisher
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#Subscription">Subscription</a>
	 */
	public static final IRI Subscription;

	/**
	 * SUBSCRIPTION_REQUEST_ACCEPTED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#SUBSCRIPTION_REQUEST_ACCEPTED}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#SUBSCRIPTION_REQUEST_ACCEPTED">SUBSCRIPTION_REQUEST_ACCEPTED</a>
	 */
	public static final IRI SUBSCRIPTION_REQUEST_ACCEPTED;

	/**
	 * SUBSCRIPTION_REQUEST_FAILED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#SUBSCRIPTION_REQUEST_FAILED}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#SUBSCRIPTION_REQUEST_FAILED">SUBSCRIPTION_REQUEST_FAILED</a>
	 */
	public static final IRI SUBSCRIPTION_REQUEST_FAILED;

	/**
	 * SUBSCRIPTION_REQUEST_PENDING
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#SUBSCRIPTION_REQUEST_PENDING}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#SUBSCRIPTION_REQUEST_PENDING">SUBSCRIPTION_REQUEST_PENDING</a>
	 */
	public static final IRI SUBSCRIPTION_REQUEST_PENDING;

	/**
	 * SUBSCRIPTION_REQUEST_REJECTED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#SUBSCRIPTION_REQUEST_REJECTED}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#SUBSCRIPTION_REQUEST_REJECTED">SUBSCRIPTION_REQUEST_REJECTED</a>
	 */
	public static final IRI SUBSCRIPTION_REQUEST_REJECTED;

	/**
	 * SUBSCRIPTION_REQUEST_REVOKED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#SUBSCRIPTION_REQUEST_REVOKED}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#SUBSCRIPTION_REQUEST_REVOKED">SUBSCRIPTION_REQUEST_REVOKED</a>
	 */
	public static final IRI SUBSCRIPTION_REQUEST_REVOKED;

	/**
	 * Subscription Event Type
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#SubscriptionEventType}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#SubscriptionEventType">SubscriptionEventType</a>
	 */
	public static final IRI SubscriptionEventType;

	/**
	 * Subscription Request
	 * <p>
	 * {@code https://onerecord.iata.org/ns/api#SubscriptionRequest}.
	 * <p>
	 * SubscriptionRequest initiated by subscribers to publisher (data
	 * holder) for themselves or for a third party subscriber.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#SubscriptionRequest">SubscriptionRequest</a>
	 */
	public static final IRI SubscriptionRequest;

	/**
	 * {@code https://onerecord.iata.org/ns/api#TopicType}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/api#TopicType">TopicType</a>
	 */
	public static final IRI TopicType;

	static {
		ValueFactory factory = SimpleValueFactory.getInstance();

		ACCESS_DELEGATION_REQUEST_ACCEPTED = factory.createIRI(API.NAMESPACE, "ACCESS_DELEGATION_REQUEST_ACCEPTED");
		ACCESS_DELEGATION_REQUEST_FAILED = factory.createIRI(API.NAMESPACE, "ACCESS_DELEGATION_REQUEST_FAILED");
		ACCESS_DELEGATION_REQUEST_PENDING = factory.createIRI(API.NAMESPACE, "ACCESS_DELEGATION_REQUEST_PENDING");
		ACCESS_DELEGATION_REQUEST_REJECTED = factory.createIRI(API.NAMESPACE, "ACCESS_DELEGATION_REQUEST_REJECTED");
		ACCESS_DELEGATION_REQUEST_REVOKED = factory.createIRI(API.NAMESPACE, "ACCESS_DELEGATION_REQUEST_REVOKED");
		AccessDelegation = factory.createIRI(API.NAMESPACE, "AccessDelegation");
		AccessDelegationRequest = factory.createIRI(API.NAMESPACE, "AccessDelegationRequest");
		AccessPermissions = factory.createIRI(API.NAMESPACE, "AccessPermissions");
		ActionRequest = factory.createIRI(API.NAMESPACE, "ActionRequest");
		ADD = factory.createIRI(API.NAMESPACE, "ADD");
		AuditTrail = factory.createIRI(API.NAMESPACE, "AuditTrail");
		Change = factory.createIRI(API.NAMESPACE, "Change");
		CHANGE_REQUEST_ACCEPTED = factory.createIRI(API.NAMESPACE, "CHANGE_REQUEST_ACCEPTED");
		CHANGE_REQUEST_FAILED = factory.createIRI(API.NAMESPACE, "CHANGE_REQUEST_FAILED");
		CHANGE_REQUEST_PENDING = factory.createIRI(API.NAMESPACE, "CHANGE_REQUEST_PENDING");
		CHANGE_REQUEST_REJECTED = factory.createIRI(API.NAMESPACE, "CHANGE_REQUEST_REJECTED");
		CHANGE_REQUEST_REVOKED = factory.createIRI(API.NAMESPACE, "CHANGE_REQUEST_REVOKED");
		ChangeRequest = factory.createIRI(API.NAMESPACE, "ChangeRequest");
		Collection = factory.createIRI(API.NAMESPACE, "Collection");
		DELETE = factory.createIRI(API.NAMESPACE, "DELETE");
		Error = factory.createIRI(API.NAMESPACE, "Error");
		ErrorDetail = factory.createIRI(API.NAMESPACE, "ErrorDetail");
		expiresAt = factory.createIRI(API.NAMESPACE, "expiresAt");
		GET_LOGISTICS_EVENT = factory.createIRI(API.NAMESPACE, "GET_LOGISTICS_EVENT");
		GET_LOGISTICS_OBJECT = factory.createIRI(API.NAMESPACE, "GET_LOGISTICS_OBJECT");
		hasAccessDelegation = factory.createIRI(API.NAMESPACE, "hasAccessDelegation");
		hasChange = factory.createIRI(API.NAMESPACE, "hasChange");
		hasChangedProperty = factory.createIRI(API.NAMESPACE, "hasChangedProperty");
		hasChangeRequest = factory.createIRI(API.NAMESPACE, "hasChangeRequest");
		hasCode = factory.createIRI(API.NAMESPACE, "hasCode");
		hasContentType = factory.createIRI(API.NAMESPACE, "hasContentType");
		hasDataHolder = factory.createIRI(API.NAMESPACE, "hasDataHolder");
		hasDatatype = factory.createIRI(API.NAMESPACE, "hasDatatype");
		hasDescription = factory.createIRI(API.NAMESPACE, "hasDescription");
		hasError = factory.createIRI(API.NAMESPACE, "hasError");
		hasErrorDetail = factory.createIRI(API.NAMESPACE, "hasErrorDetail");
		hasEventType = factory.createIRI(API.NAMESPACE, "hasEventType");
		hasItem = factory.createIRI(API.NAMESPACE, "hasItem");
		hasLatestRevision = factory.createIRI(API.NAMESPACE, "hasLatestRevision");
		hasLogisticsAgent = factory.createIRI(API.NAMESPACE, "hasLogisticsAgent");
		hasLogisticsObject = factory.createIRI(API.NAMESPACE, "hasLogisticsObject");
		hasLogisticsObjectType = factory.createIRI(API.NAMESPACE, "hasLogisticsObjectType");
		hasMessage = factory.createIRI(API.NAMESPACE, "hasMessage");
		hasOperation = factory.createIRI(API.NAMESPACE, "hasOperation");
		hasPermission = factory.createIRI(API.NAMESPACE, "hasPermission");
		hasProperty = factory.createIRI(API.NAMESPACE, "hasProperty");
		hasRequestStatus = factory.createIRI(API.NAMESPACE, "hasRequestStatus");
		hasResource = factory.createIRI(API.NAMESPACE, "hasResource");
		hasRevision = factory.createIRI(API.NAMESPACE, "hasRevision");
		hasServerEndpoint = factory.createIRI(API.NAMESPACE, "hasServerEndpoint");
		hasSubscriber = factory.createIRI(API.NAMESPACE, "hasSubscriber");
		hasSubscription = factory.createIRI(API.NAMESPACE, "hasSubscription");
		hasSupportedApiVersion = factory.createIRI(API.NAMESPACE, "hasSupportedApiVersion");
		hasSupportedContentType = factory.createIRI(API.NAMESPACE, "hasSupportedContentType");
		hasSupportedEncoding = factory.createIRI(API.NAMESPACE, "hasSupportedEncoding");
		hasSupportedLanguage = factory.createIRI(API.NAMESPACE, "hasSupportedLanguage");
		hasSupportedOntology = factory.createIRI(API.NAMESPACE, "hasSupportedOntology");
		hasSupportedOntologyVersion = factory.createIRI(API.NAMESPACE, "hasSupportedOntologyVersion");
		hasTitle = factory.createIRI(API.NAMESPACE, "hasTitle");
		hasTopic = factory.createIRI(API.NAMESPACE, "hasTopic");
		hasTopicType = factory.createIRI(API.NAMESPACE, "hasTopicType");
		hasTotalItems = factory.createIRI(API.NAMESPACE, "hasTotalItems");
		hasValue = factory.createIRI(API.NAMESPACE, "hasValue");
		includeSubscriptionEventType = factory.createIRI(API.NAMESPACE, "includeSubscriptionEventType");
		isRequestedAt = factory.createIRI(API.NAMESPACE, "isRequestedAt");
		isRequestedBy = factory.createIRI(API.NAMESPACE, "isRequestedBy");
		isRequestedFor = factory.createIRI(API.NAMESPACE, "isRequestedFor");
		isRevokedAt = factory.createIRI(API.NAMESPACE, "isRevokedAt");
		isRevokedBy = factory.createIRI(API.NAMESPACE, "isRevokedBy");
		isTriggeredBy = factory.createIRI(API.NAMESPACE, "isTriggeredBy");
		LOGISTICS_EVENT_RECEIVED = factory.createIRI(API.NAMESPACE, "LOGISTICS_EVENT_RECEIVED");
		LOGISTICS_OBJECT_CREATED = factory.createIRI(API.NAMESPACE, "LOGISTICS_OBJECT_CREATED");
		LOGISTICS_OBJECT_IDENTIFIER = factory.createIRI(API.NAMESPACE, "LOGISTICS_OBJECT_IDENTIFIER");
		LOGISTICS_OBJECT_TYPE = factory.createIRI(API.NAMESPACE, "LOGISTICS_OBJECT_TYPE");
		LOGISTICS_OBJECT_UPDATED = factory.createIRI(API.NAMESPACE, "LOGISTICS_OBJECT_UPDATED");
		Notification = factory.createIRI(API.NAMESPACE, "Notification");
		NotificationEventType = factory.createIRI(API.NAMESPACE, "NotificationEventType");
		notifyRequestStatusChange = factory.createIRI(API.NAMESPACE, "notifyRequestStatusChange");
		o = factory.createIRI(API.NAMESPACE, "o");
		op = factory.createIRI(API.NAMESPACE, "op");
		Operation = factory.createIRI(API.NAMESPACE, "Operation");
		OperationObject = factory.createIRI(API.NAMESPACE, "OperationObject");
		p = factory.createIRI(API.NAMESPACE, "p");
		PATCH_LOGISTICS_OBJECT = factory.createIRI(API.NAMESPACE, "PATCH_LOGISTICS_OBJECT");
		PatchOperation = factory.createIRI(API.NAMESPACE, "PatchOperation");
		Permission = factory.createIRI(API.NAMESPACE, "Permission");
		POST_LOGISTICS_EVENT = factory.createIRI(API.NAMESPACE, "POST_LOGISTICS_EVENT");
		REQUEST_ACCEPTED = factory.createIRI(API.NAMESPACE, "REQUEST_ACCEPTED");
		REQUEST_FAILED = factory.createIRI(API.NAMESPACE, "REQUEST_FAILED");
		REQUEST_PENDING = factory.createIRI(API.NAMESPACE, "REQUEST_PENDING");
		REQUEST_REJECTED = factory.createIRI(API.NAMESPACE, "REQUEST_REJECTED");
		REQUEST_REVOKED = factory.createIRI(API.NAMESPACE, "REQUEST_REVOKED");
		RequestStatus = factory.createIRI(API.NAMESPACE, "RequestStatus");
		s = factory.createIRI(API.NAMESPACE, "s");
		sendLogisticsObjectBody = factory.createIRI(API.NAMESPACE, "sendLogisticsObjectBody");
		ServerInformation = factory.createIRI(API.NAMESPACE, "ServerInformation");
		Subscription = factory.createIRI(API.NAMESPACE, "Subscription");
		SUBSCRIPTION_REQUEST_ACCEPTED = factory.createIRI(API.NAMESPACE, "SUBSCRIPTION_REQUEST_ACCEPTED");
		SUBSCRIPTION_REQUEST_FAILED = factory.createIRI(API.NAMESPACE, "SUBSCRIPTION_REQUEST_FAILED");
		SUBSCRIPTION_REQUEST_PENDING = factory.createIRI(API.NAMESPACE, "SUBSCRIPTION_REQUEST_PENDING");
		SUBSCRIPTION_REQUEST_REJECTED = factory.createIRI(API.NAMESPACE, "SUBSCRIPTION_REQUEST_REJECTED");
		SUBSCRIPTION_REQUEST_REVOKED = factory.createIRI(API.NAMESPACE, "SUBSCRIPTION_REQUEST_REVOKED");
		SubscriptionEventType = factory.createIRI(API.NAMESPACE, "SubscriptionEventType");
		SubscriptionRequest = factory.createIRI(API.NAMESPACE, "SubscriptionRequest");
		TopicType = factory.createIRI(API.NAMESPACE, "TopicType");
	}

	private API() {
		//static access only
	}

}
