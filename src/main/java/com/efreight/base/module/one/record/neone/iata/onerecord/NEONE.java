package com.efreight.base.module.one.record.neone.iata.onerecord;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

/**
 * Namespace NEONE.
 * Prefix: {@code <https://www.openlogisticsfoundation.org/neone#>}
 */
public class NEONE {

	/** {@code https://www.openlogisticsfoundation.org/neone#} **/
	public static final String NAMESPACE = "https://www.openlogisticsfoundation.org/neone#";

	/** {@code neone} **/
	public static final String PREFIX = "neone";

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#Callback}.
	 * <p>
	 * Describes a callback to be executed for the referenced notification
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#Callback">Callback</a>
	 */
	public static final IRI Callback;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#callbackUrl}.
	 * <p>
	 * The callback url for this Callback.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#callbackUrl">callbackUrl</a>
	 */
	public static final IRI callbackUrl;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#describes}.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#describes">describes</a>
	 */
	public static final IRI describes;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#hasEvent}.
	 * <p>
	 * Link to the event the metadata refers to.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#hasEvent">hasEvent</a>
	 */
	public static final IRI hasEvent;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#hasNotification}.
	 * <p>
	 * Denotes that a subject has an associated notification.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#hasNotification">hasNotification</a>
	 */
	public static final IRI hasNotification;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#hasPredefinedIri}.
	 * <p>
	 * If true, the IRI of the subject was not internally defined by the
	 * system, but given from outside
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#hasPredefinedIri">hasPredefinedIri</a>
	 */
	public static final IRI hasPredefinedIri;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#hasRevision}.
	 * <p>
	 * Revision of this logistics object, starts with 0. If greater than 0 it
	 * has been changed.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#hasRevision">hasRevision</a>
	 */
	public static final IRI hasRevision;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#hasState}.
	 * <p>
	 * Indicates that this event has a certain state.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#hasState">hasState</a>
	 */
	public static final IRI hasState;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#hasTotalCount}.
	 * <p>
	 * The total count of elements in a collection
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#hasTotalCount">hasTotalCount</a>
	 */
	public static final IRI hasTotalCount;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#hasType}.
	 * <p>
	 * indicates that this event is of the specified type.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#hasType">hasType</a>
	 */
	public static final IRI hasType;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#includeLogisticsObject}.
	 * <p>
	 * Indicates that referenced LogisticsObjects should be included.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#includeLogisticsObject">includeLogisticsObject</a>
	 */
	public static final IRI includeLogisticsObject;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#isCreatedAt}.
	 * <p>
	 * The point in time the subject was created.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#isCreatedAt">isCreatedAt</a>
	 */
	public static final IRI isCreatedAt;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#isDescribedBy}.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#isDescribedBy">isDescribedBy</a>
	 */
	public static final IRI isDescribedBy;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#LogisticsObjectMetadata}.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#LogisticsObjectMetadata">LogisticsObjectMetadata</a>
	 */
	public static final IRI LogisticsObjectMetadata;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#loType}.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#loType">loType</a>
	 */
	public static final IRI loType;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#NeoneEvent}.
	 * <p>
	 * An event that occured during the processing of logistics objects.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#NeoneEvent">NeoneEvent</a>
	 */
	public static final IRI NeoneEvent;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#NeoneEventState}.
	 * <p>
	 * The state of an event.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#NeoneEventState">NeoneEventState</a>
	 */
	public static final IRI NeoneEventState;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#NeoneEventType}.
	 * <p>
	 * The type of an event.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#NeoneEventType">NeoneEventType</a>
	 */
	public static final IRI NeoneEventType;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#NEW}.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#NEW">NEW</a>
	 */
	public static final IRI NEW;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#NotificationMetadata}.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#NotificationMetadata">NotificationMetadata</a>
	 */
	public static final IRI NotificationMetadata;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#OBJECT_CREATED}.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#OBJECT_CREATED">OBJECT_CREATED</a>
	 */
	public static final IRI OBJECT_CREATED;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#OBJECT_UPDATED}.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#OBJECT_UPDATED">OBJECT_UPDATED</a>
	 */
	public static final IRI OBJECT_UPDATED;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#payload}.
	 * <p>
	 * e.g. string content (Json LD) of some LogisticsObject
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#payload">payload</a>
	 */
	public static final IRI payload;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#PENDING}.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#PENDING">PENDING</a>
	 */
	public static final IRI PENDING;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#PROCESSED}.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#PROCESSED">PROCESSED</a>
	 */
	public static final IRI PROCESSED;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#referencesLogisticsObject}.
	 * <p>
	 * Indicates that this subject has a reference to a logistics object.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#referencesLogisticsObject">referencesLogisticsObject</a>
	 */
	public static final IRI referencesLogisticsObject;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#Snapshot}.
	 * <p>
	 * A LogisticsObject as it was at a certain point in time
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#Snapshot">Snapshot</a>
	 */
	public static final IRI Snapshot;

	/**
	 * {@code https://www.openlogisticsfoundation.org/neone#SubscriptionMetadata}.
	 *
	 * @see <a href="https://www.openlogisticsfoundation.org/neone#SubscriptionMetadata">SubscriptionMetadata</a>
	 */
	public static final IRI SubscriptionMetadata;

	static {
		ValueFactory factory = SimpleValueFactory.getInstance();

		Callback = factory.createIRI(NEONE.NAMESPACE, "Callback");
		callbackUrl = factory.createIRI(NEONE.NAMESPACE, "callbackUrl");
		describes = factory.createIRI(NEONE.NAMESPACE, "describes");
		hasEvent = factory.createIRI(NEONE.NAMESPACE, "hasEvent");
		hasNotification = factory.createIRI(NEONE.NAMESPACE, "hasNotification");
		hasPredefinedIri = factory.createIRI(NEONE.NAMESPACE, "hasPredefinedIri");
		hasRevision = factory.createIRI(NEONE.NAMESPACE, "hasRevision");
		hasState = factory.createIRI(NEONE.NAMESPACE, "hasState");
		hasTotalCount = factory.createIRI(NEONE.NAMESPACE, "hasTotalCount");
		hasType = factory.createIRI(NEONE.NAMESPACE, "hasType");
		includeLogisticsObject = factory.createIRI(NEONE.NAMESPACE, "includeLogisticsObject");
		isCreatedAt = factory.createIRI(NEONE.NAMESPACE, "isCreatedAt");
		isDescribedBy = factory.createIRI(NEONE.NAMESPACE, "isDescribedBy");
		LogisticsObjectMetadata = factory.createIRI(NEONE.NAMESPACE, "LogisticsObjectMetadata");
		loType = factory.createIRI(NEONE.NAMESPACE, "loType");
		NeoneEvent = factory.createIRI(NEONE.NAMESPACE, "NeoneEvent");
		NeoneEventState = factory.createIRI(NEONE.NAMESPACE, "NeoneEventState");
		NeoneEventType = factory.createIRI(NEONE.NAMESPACE, "NeoneEventType");
		NEW = factory.createIRI(NEONE.NAMESPACE, "NEW");
		NotificationMetadata = factory.createIRI(NEONE.NAMESPACE, "NotificationMetadata");
		OBJECT_CREATED = factory.createIRI(NEONE.NAMESPACE, "OBJECT_CREATED");
		OBJECT_UPDATED = factory.createIRI(NEONE.NAMESPACE, "OBJECT_UPDATED");
		payload = factory.createIRI(NEONE.NAMESPACE, "payload");
		PENDING = factory.createIRI(NEONE.NAMESPACE, "PENDING");
		PROCESSED = factory.createIRI(NEONE.NAMESPACE, "PROCESSED");
		referencesLogisticsObject = factory.createIRI(NEONE.NAMESPACE, "referencesLogisticsObject");
		Snapshot = factory.createIRI(NEONE.NAMESPACE, "Snapshot");
		SubscriptionMetadata = factory.createIRI(NEONE.NAMESPACE, "SubscriptionMetadata");
	}

	private NEONE() {
		//static access only
	}

}
