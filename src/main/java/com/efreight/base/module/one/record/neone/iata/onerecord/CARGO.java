package com.efreight.base.module.one.record.neone.iata.onerecord;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

/**
 * Namespace CARGO.
 * Prefix: {@code <https://onerecord.iata.org/ns/cargo#>}
 */
public class CARGO {

	/** {@code https://onerecord.iata.org/ns/cargo#} **/
	public static final String NAMESPACE = "https://onerecord.iata.org/ns/cargo#";

	/** {@code cargo} **/
	public static final String PREFIX = "cargo";

	/**
	 * ACCELEROMETER
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ACCELEROMETER}.
	 * <p>
	 * Indicates the sensor type as accelerometer
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ACCELEROMETER">ACCELEROMETER</a>
	 */
	public static final IRI ACCELEROMETER;

	/**
	 * accountingInformation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#accountingInformation}.
	 * <p>
	 * Indicates the details of accounting information. Free text e.g.
	 * PAYMENT BY CERTIFIED CHEQUE etc.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#accountingInformation">accountingInformation</a>
	 */
	public static final IRI accountingInformation;

	/**
	 * acquisitionDateTime
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#acquisitionDateTime}.
	 * <p>
	 * Defined in Resolution Conf. 13.6 and is required for pre-Convention
	 * specimens (box 12b)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#acquisitionDateTime">acquisitionDateTime</a>
	 */
	public static final IRI acquisitionDateTime;

	/**
	 * actionEndTime
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#actionEndTime}.
	 * <p>
	 * DateTime holding the end time of the Action; Type is indicated through
	 * ActionType property
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#actionEndTime">actionEndTime</a>
	 */
	public static final IRI actionEndTime;

	/**
	 * actionStartTime
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#actionStartTime}.
	 * <p>
	 * DateTime holding the start time of the Action; Type is indicated
	 * through ActionType property
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#actionStartTime">actionStartTime</a>
	 */
	public static final IRI actionStartTime;

	/**
	 * ActionTimeType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ActionTimeType}.
	 * <p>
	 * Restricted code list for acceptable action times
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ActionTimeType">ActionTimeType</a>
	 */
	public static final IRI ActionTimeType;

	/**
	 * actionTimeType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#actionTimeType}.
	 * <p>
	 * Enum stating the type of the Action
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#actionTimeType">actionTimeType</a>
	 */
	public static final IRI actionTimeType;

	/**
	 * ACTIVE
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ACTIVE}.
	 * <p>
	 * Used when a LogisticsActivity is active
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ACTIVE">ACTIVE</a>
	 */
	public static final IRI ACTIVE;

	/**
	 * activity
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#activity}.
	 * <p>
	 * Reference to the Activity that is performed as part of a Service
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#activity">activity</a>
	 */
	public static final IRI activity;

	/**
	 * activityLevelMeasure
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#activityLevelMeasure}.
	 * <p>
	 * Numeric expression of the activity of a radioactive Item
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#activityLevelMeasure">activityLevelMeasure</a>
	 */
	public static final IRI activityLevelMeasure;

	/**
	 * ActivitySequence
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ActivitySequence}.
	 * <p>
	 * Embedded object to create a sequence of Activities in the context of a
	 * Service
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ActivitySequence">ActivitySequence</a>
	 */
	public static final IRI ActivitySequence;

	/**
	 * activitySequences
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#activitySequences}.
	 * <p>
	 * Information about the Activities that are part of the Service and
	 * their sequence
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#activitySequences">activitySequences</a>
	 */
	public static final IRI activitySequences;

	/**
	 * Actor
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Actor}.
	 * <p>
	 * Superclass: Actors are Persons or entities acting like a single person
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Actor">Actor</a>
	 */
	public static final IRI Actor;

	/**
	 * ACTUAL
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ACTUAL}.
	 * <p>
	 * Used when a time is actual
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ACTUAL">ACTUAL</a>
	 */
	public static final IRI ACTUAL;

	/**
	 * additionalHazardClassificationId
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#additionalHazardClassificationId}.
	 * <p>
	 * Identifies the subsidiary hazard class / division identification
	 * containing a numeric field separated by a decimal. There may be , 1 or
	 * 2 subsidiary risk classes or divisions. If there is more than one,
	 * each should be separated by a comma. The subsidiary risk must be shown
	 * in parentheses. 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#additionalHazardClassificationId">additionalHazardClassificationId</a>
	 */
	public static final IRI additionalHazardClassificationId;

	/**
	 * additionalInformation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#additionalInformation}.
	 * <p>
	 * Additional infromation related to the Booking Option, e.g. sales
	 * details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#additionalInformation">additionalInformation</a>
	 */
	public static final IRI additionalInformation;

	/**
	 * additionalSecurityInformation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#additionalSecurityInformation}.
	 * <p>
	 * Any additional information that may be required by an ICAO Member
	 * State
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#additionalSecurityInformation">additionalSecurityInformation</a>
	 */
	public static final IRI additionalSecurityInformation;

	/**
	 * address
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#address}.
	 * <p>
	 * Address details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#address">address</a>
	 */
	public static final IRI address;

	/**
	 * Address
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Address}.
	 * <p>
	 * Address details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Address">Address</a>
	 */
	public static final IRI Address;

	/**
	 * addressCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#addressCode}.
	 * <p>
	 * Address identifier using special coding systems e.g. US CBP FIRMS code
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#addressCode">addressCode</a>
	 */
	public static final IRI addressCode;

	/**
	 * Adjustments
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Adjustments}.
	 * <p>
	 * Adjustments in the context of CASS records
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Adjustments">Adjustments</a>
	 */
	public static final IRI Adjustments;

	/**
	 * adjustments
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#adjustments}.
	 * <p>
	 * Information about Adjustments performed on the BillingDetails
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#adjustments">adjustments</a>
	 */
	public static final IRI adjustments;

	/**
	 * aircraftLimitationInformation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#aircraftLimitationInformation}.
	 * <p>
	 * Contains the Special Handling Code related to the prescribed
	 * limitation. Hardcoded to PASSENGER AND CARGO AIRCRAFT or CARGO
	 * AIRCRAFT ONLY. This field is mandatory for air (Air) 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#aircraftLimitationInformation">aircraftLimitationInformation</a>
	 */
	public static final IRI aircraftLimitationInformation;

	/**
	 * aircraftPossibilityCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#aircraftPossibilityCode}.
	 * <p>
	 * Type of aircraft to be used if any specific requirements (e.g. Pure
	 * freighter, etc.)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#aircraftPossibilityCode">aircraftPossibilityCode</a>
	 */
	public static final IRI aircraftPossibilityCode;

	/**
	 * airlineCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#airlineCode}.
	 * <p>
	 * IATA two-character airline code
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#airlineCode">airlineCode</a>
	 */
	public static final IRI airlineCode;

	/**
	 * allPackedInOneIndicator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#allPackedInOneIndicator}.
	 * <p>
	 * A statement identifying that the dangerous goods listed above are all
	 * contained in the same outer packaging. Takes the form All packed in
	 * one aaaa (description of packaging type) x nn (number of packages).
	 * Applies to air transport only. (Air) 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#allPackedInOneIndicator">allPackedInOneIndicator</a>
	 */
	public static final IRI allPackedInOneIndicator;

	/**
	 * ALTERNATE_EMAIL_ADDRESS
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ALTERNATE_EMAIL_ADDRESS}.
	 * <p>
	 * Indicates a contact detail as alternate email address
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ALTERNATE_EMAIL_ADDRESS">ALTERNATE_EMAIL_ADDRESS</a>
	 */
	public static final IRI ALTERNATE_EMAIL_ADDRESS;

	/**
	 * ALTERNATE_PHONE_NUMBER
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ALTERNATE_PHONE_NUMBER}.
	 * <p>
	 * Indicates a contact detail as alternate phone number
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ALTERNATE_PHONE_NUMBER">ALTERNATE_PHONE_NUMBER</a>
	 */
	public static final IRI ALTERNATE_PHONE_NUMBER;

	/**
	 * alternatives
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#alternatives}.
	 * <p>
	 * Description of the alternatives proposed that do not match the Booking
	 * Option Request
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#alternatives">alternatives</a>
	 */
	public static final IRI alternatives;

	/**
	 * annualQuotaQuantity
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#annualQuotaQuantity}.
	 * <p>
	 * total number of specimens exported in the current calendar year and
	 * the current annuela quota for the species concerned (box 11a)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#annualQuotaQuantity">annualQuotaQuantity</a>
	 */
	public static final IRI annualQuotaQuantity;

	/**
	 * answer
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#answer}.
	 * <p>
	 * Reference to the Answer to the Question
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#answer">answer</a>
	 */
	public static final IRI answer;

	/**
	 * Answer
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Answer}.
	 * <p>
	 * Answer holds the answer to one Question and is provided by the
	 * executioner of the check
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Answer">Answer</a>
	 */
	public static final IRI Answer;

	/**
	 * answerActor
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#answerActor}.
	 * <p>
	 * Reference to the Actor giving the Answer
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#answerActor">answerActor</a>
	 */
	public static final IRI answerActor;

	/**
	 * answerOptionsText
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#answerOptionsText}.
	 * <p>
	 * Text restrictions to the Answer
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#answerOptionsText">answerOptionsText</a>
	 */
	public static final IRI answerOptionsText;

	/**
	 * answerOptionsValue
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#answerOptionsValue}.
	 * <p>
	 * Value restrictions to the answer
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#answerOptionsValue">answerOptionsValue</a>
	 */
	public static final IRI answerOptionsValue;

	/**
	 * answerValue
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#answerValue}.
	 * <p>
	 * Information about an answer Value of any kind of the Answer
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#answerValue">answerValue</a>
	 */
	public static final IRI answerValue;

	/**
	 * appliedOnPieces
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#appliedOnPieces}.
	 * <p>
	 * Piece on which the Packaging type is applicable
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#appliedOnPieces">appliedOnPieces</a>
	 */
	public static final IRI appliedOnPieces;

	/**
	 * arrivalDate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#arrivalDate}.
	 * <p>
	 * Arrival date and time of the leg
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#arrivalDate">arrivalDate</a>
	 */
	public static final IRI arrivalDate;

	/**
	 * arrivalLocation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#arrivalLocation}.
	 * <p>
	 * Reference to the arrival Location
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#arrivalLocation">arrivalLocation</a>
	 */
	public static final IRI arrivalLocation;

	/**
	 * associatedEpermit
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#associatedEpermit}.
	 * <p>
	 * Reference to the permits associated with the Live Animals
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#associatedEpermit">associatedEpermit</a>
	 */
	public static final IRI associatedEpermit;

	/**
	 * associatedOrganization
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#associatedOrganization}.
	 * <p>
	 * Reference to the Organization the Actor is associated with
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#associatedOrganization">associatedOrganization</a>
	 */
	public static final IRI associatedOrganization;

	/**
	 * ataDesignator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ataDesignator}.
	 * <p>
	 * US / ATA Unit Load Device type code e.g. M2
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ataDesignator">ataDesignator</a>
	 */
	public static final IRI ataDesignator;

	/**
	 * attachedIotDevices
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#attachedIotDevices}.
	 * <p>
	 * References to all connected IotDevices
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#attachedIotDevices">attachedIotDevices</a>
	 */
	public static final IRI attachedIotDevices;

	/**
	 * attachedToObject
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#attachedToObject}.
	 * <p>
	 * Reference to the PhysicalLogisticsObject the IotDevice is attached to
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#attachedToObject">attachedToObject</a>
	 */
	public static final IRI attachedToObject;

	/**
	 * authorizationInformation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#authorizationInformation}.
	 * <p>
	 * Contains additional information relating to an approval, permission or
	 * other specific detail applicable to the commodity (e.g. Dangerous
	 * Goods in excepted quantities) 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#authorizationInformation">authorizationInformation</a>
	 */
	public static final IRI authorizationInformation;

	/**
	 * awbAcceptanceDate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#awbAcceptanceDate}.
	 * <p>
	 * The Date AWB Acceptance should be the same as the Date AWB Delivery.
	 * (beginning of the process)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#awbAcceptanceDate">awbAcceptanceDate</a>
	 */
	public static final IRI awbAcceptanceDate;

	/**
	 * awbDeliveryDate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#awbDeliveryDate}.
	 * <p>
	 * The Date AWB Delivery is also used as the AWB Execution date which
	 * will determine which billing period it will be processed and billed
	 * in.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#awbDeliveryDate">awbDeliveryDate</a>
	 */
	public static final IRI awbDeliveryDate;

	/**
	 * awbExecutionDate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#awbExecutionDate}.
	 * <p>
	 * The AWB execution date determines which billing period the document
	 * will be processed and billed in.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#awbExecutionDate">awbExecutionDate</a>
	 */
	public static final IRI awbExecutionDate;

	/**
	 * awbUseIndicator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#awbUseIndicator}.
	 * <p>
	 * It must either contain the values of R for Revenue AWB, V for Void AWB
	 * or S for Service AWB.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#awbUseIndicator">awbUseIndicator</a>
	 */
	public static final IRI awbUseIndicator;

	/**
	 * basedAtLocation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#basedAtLocation}.
	 * <p>
	 * Reference to the Location where the Organization is based at or
	 * headquartered
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#basedAtLocation">basedAtLocation</a>
	 */
	public static final IRI basedAtLocation;

	/**
	 * batchNumber
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#batchNumber}.
	 * <p>
	 * Production batch number / reference
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#batchNumber">batchNumber</a>
	 */
	public static final IRI batchNumber;

	/**
	 * billingChargeIdentifier
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#billingChargeIdentifier}.
	 * <p>
	 * Billing charge identifiers to be used for CASS. Refer to CargoXML Code
	 * List 1.33
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#billingChargeIdentifier">billingChargeIdentifier</a>
	 */
	public static final IRI billingChargeIdentifier;

	/**
	 * billingDetails
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#billingDetails}.
	 * <p>
	 * Reference to the BillingDetails of the Waybill
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#billingDetails">billingDetails</a>
	 */
	public static final IRI billingDetails;

	/**
	 * BillingDetails
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#BillingDetails}.
	 * <p>
	 * In the context of CASS2. process, BillingDetails object is used to
	 * integrate specific Billing and Settlement data requirements
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#BillingDetails">BillingDetails</a>
	 */
	public static final IRI BillingDetails;

	/**
	 * BOOKABLE
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#BOOKABLE}.
	 * <p>
	 * Used when a booking option (or proposal) is bookable
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#BOOKABLE">BOOKABLE</a>
	 */
	public static final IRI BOOKABLE;

	/**
	 * BOOKED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#BOOKED}.
	 * <p>
	 * Used when a booking option proposal is booked
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#BOOKED">BOOKED</a>
	 */
	public static final IRI BOOKED;

	/**
	 * Booking
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Booking}.
	 * <p>
	 * Booking object refers to a confirmed booking
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Booking">Booking</a>
	 */
	public static final IRI Booking;

	/**
	 * booking
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#booking}.
	 * <p>
	 * Reference to the Booking
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#booking">booking</a>
	 */
	public static final IRI booking;

	/**
	 * BookingOption
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#BookingOption}.
	 * <p>
	 * Booking details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#BookingOption">BookingOption</a>
	 */
	public static final IRI BookingOption;

	/**
	 * BookingOptionRequest
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#BookingOptionRequest}.
	 * <p>
	 * Request object, refers to the Quote request or Booking request 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#BookingOptionRequest">BookingOptionRequest</a>
	 */
	public static final IRI BookingOptionRequest;

	/**
	 * bookingOptions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#bookingOptions}.
	 * <p>
	 * Reference to all Booking Options
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#bookingOptions">bookingOptions</a>
	 */
	public static final IRI bookingOptions;

	/**
	 * BookingOptionStatus
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#BookingOptionStatus}.
	 * <p>
	 * Restricted code list containing the statuses of a booking option
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#BookingOptionStatus">BookingOptionStatus</a>
	 */
	public static final IRI BookingOptionStatus;

	/**
	 * bookingPreference
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#bookingPreference}.
	 * <p>
	 * Reference to the Booking preferences
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#bookingPreference">bookingPreference</a>
	 */
	public static final IRI bookingPreference;

	/**
	 * BookingPreferences
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#BookingPreferences}.
	 * <p>
	 * BookingPreferences details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#BookingPreferences">BookingPreferences</a>
	 */
	public static final IRI BookingPreferences;

	/**
	 * bookingRequest
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#bookingRequest}.
	 * <p>
	 * Reference to the Booking Request
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#bookingRequest">bookingRequest</a>
	 */
	public static final IRI bookingRequest;

	/**
	 * BookingRequest
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#BookingRequest}.
	 * <p>
	 * A party, usually the freight forwarder, creates the BookingRequest in
	 * order to confirm the booking to the Carrier
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#BookingRequest">BookingRequest</a>
	 */
	public static final IRI BookingRequest;

	/**
	 * BookingShipment
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#BookingShipment}.
	 * <p>
	 * Simplified shipment object that is to be used only for the
	 * distribution scope where only a subset of data is known priori to
	 * operational phase.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#BookingShipment">BookingShipment</a>
	 */
	public static final IRI BookingShipment;

	/**
	 * bookingShipmentDetails
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#bookingShipmentDetails}.
	 * <p>
	 * Reference to the BookingShipment if required
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#bookingShipmentDetails">bookingShipmentDetails</a>
	 */
	public static final IRI bookingShipmentDetails;

	/**
	 * bookingStatus
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#bookingStatus}.
	 * <p>
	 * Status of the Booking
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#bookingStatus">bookingStatus</a>
	 */
	public static final IRI bookingStatus;

	/**
	 * BookingStatus
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#BookingStatus}.
	 * <p>
	 * Restricted code list containing the possible statuses of a booking
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#BookingStatus">BookingStatus</a>
	 */
	public static final IRI BookingStatus;

	/**
	 * BookingTimes
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#BookingTimes}.
	 * <p>
	 * Previsouly called Schedule. This object refers to times used for the
	 * Booking Option Request (preferences part of the request) or the
	 * Booking Option (times sur as LAT where there is a commitment from the
	 * carrier)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#BookingTimes">BookingTimes</a>
	 */
	public static final IRI BookingTimes;

	/**
	 * bookingTimes
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#bookingTimes}.
	 * <p>
	 * Information about the Booking Times of a privded Booking Option
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#bookingTimes">bookingTimes</a>
	 */
	public static final IRI bookingTimes;

	/**
	 * bookingToUpdate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#bookingToUpdate}.
	 * <p>
	 * Reference to the Booking to update
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#bookingToUpdate">bookingToUpdate</a>
	 */
	public static final IRI bookingToUpdate;

	/**
	 * BULK
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#BULK}.
	 * <p>
	 * Indicates the load type as bulk
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#BULK">BULK</a>
	 */
	public static final IRI BULK;

	/**
	 * calculatedEmissions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#calculatedEmissions}.
	 * <p>
	 * CO2 emissions calculated
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#calculatedEmissions">calculatedEmissions</a>
	 */
	public static final IRI calculatedEmissions;

	/**
	 * calculationFor
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#calculationFor}.
	 * <p>
	 * Reference to the TransportMovement or TransportLegs the CO2Emissions
	 * have been calculated for
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#calculationFor">calculationFor</a>
	 */
	public static final IRI calculationFor;

	/**
	 * CANCELLED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#CANCELLED}.
	 * <p>
	 * Used when a LogisticsActivity is cancelled
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#CANCELLED">CANCELLED</a>
	 */
	public static final IRI CANCELLED;

	/**
	 * Carrier
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Carrier}.
	 * <p>
	 * Company details of carriers
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Carrier">Carrier</a>
	 */
	public static final IRI Carrier;

	/**
	 * carrier
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#carrier}.
	 * <p>
	 * Reference to the operating carrier
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#carrier">carrier</a>
	 */
	public static final IRI carrier;

	/**
	 * carrierChargeCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#carrierChargeCode}.
	 * <p>
	 * One letter charge code as per bullet point 12 - data element 13 from
	 * AWB
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#carrierChargeCode">carrierChargeCode</a>
	 */
	public static final IRI carrierChargeCode;

	/**
	 * carrierDeclarationDate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#carrierDeclarationDate}.
	 * <p>
	 * Date upon which the certification is made by the carrier
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#carrierDeclarationDate">carrierDeclarationDate</a>
	 */
	public static final IRI carrierDeclarationDate;

	/**
	 * carrierDeclarationPlace
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#carrierDeclarationPlace}.
	 * <p>
	 * Location of individual or company involved in the movement of a
	 * consignment or Coded representation of a specific airport/city code
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#carrierDeclarationPlace">carrierDeclarationPlace</a>
	 */
	public static final IRI carrierDeclarationPlace;

	/**
	 * carrierDeclarationSignature
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#carrierDeclarationSignature}.
	 * <p>
	 * Contains the authentication of the Carrier
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#carrierDeclarationSignature">carrierDeclarationSignature</a>
	 */
	public static final IRI carrierDeclarationSignature;

	/**
	 * CarrierProduct
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#CarrierProduct}.
	 * <p>
	 * Carrier product details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#CarrierProduct">CarrierProduct</a>
	 */
	public static final IRI CarrierProduct;

	/**
	 * carrierProduct
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#carrierProduct}.
	 * <p>
	 * Reference to the Carrier product if known
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#carrierProduct">carrierProduct</a>
	 */
	public static final IRI carrierProduct;

	/**
	 * categoryCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#categoryCode}.
	 * <p>
	 * Operations code ID. Refers to the number of the registered
	 * captive-breeding or artifical propagation operation (box 12b)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#categoryCode">categoryCode</a>
	 */
	public static final IRI categoryCode;

	/**
	 * certifiedByActor
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#certifiedByActor}.
	 * <p>
	 * Reference to the Actor certifying the result of the Check if required
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#certifiedByActor">certifiedByActor</a>
	 */
	public static final IRI certifiedByActor;

	/**
	 * Characteristic
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Characteristic}.
	 * <p>
	 * Product additional details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Characteristic">Characteristic</a>
	 */
	public static final IRI Characteristic;

	/**
	 * characteristicType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#characteristicType}.
	 * <p>
	 * Product characteristics code - e.g. CLR - Color. Not restricted to a
	 * list.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#characteristicType">characteristicType</a>
	 */
	public static final IRI characteristicType;

	/**
	 * chargeableWeight
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#chargeableWeight}.
	 * <p>
	 * Chargeable weight
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#chargeableWeight">chargeableWeight</a>
	 */
	public static final IRI chargeableWeight;

	/**
	 * chargeableWeightForRate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#chargeableWeightForRate}.
	 * <p>
	 * Chargeable weight for which the rate description details apply
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#chargeableWeightForRate">chargeableWeightForRate</a>
	 */
	public static final IRI chargeableWeightForRate;

	/**
	 * chargeCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#chargeCode}.
	 * <p>
	 * Charge code, refer to CargoXML Code List 1.1
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#chargeCode">chargeCode</a>
	 */
	public static final IRI chargeCode;

	/**
	 * chargeDescription
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#chargeDescription}.
	 * <p>
	 * Description of the charge e.g. Airfreight, fuel, etc.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#chargeDescription">chargeDescription</a>
	 */
	public static final IRI chargeDescription;

	/**
	 * chargePaymentType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#chargePaymentType}.
	 * <p>
	 * Indicates if charge is prepaid or collect (P, C)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#chargePaymentType">chargePaymentType</a>
	 */
	public static final IRI chargePaymentType;

	/**
	 * chargeType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#chargeType}.
	 * <p>
	 * Charge type related to amount total as per bullet points 2/21 - data
	 * elements 24A - 3B from AWB
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#chargeType">chargeType</a>
	 */
	public static final IRI chargeType;

	/**
	 * Check
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Check}.
	 * <p>
	 * Action to describe a check
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Check">Check</a>
	 */
	public static final IRI Check;

	/**
	 * checkActions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#checkActions}.
	 * <p>
	 * References to CheckActions performed for the Activity
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#checkActions">checkActions</a>
	 */
	public static final IRI checkActions;

	/**
	 * checkedObject
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#checkedObject}.
	 * <p>
	 * Reference to the checked Object
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#checkedObject">checkedObject</a>
	 */
	public static final IRI checkedObject;

	/**
	 * checker
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#checker}.
	 * <p>
	 * Reference to the Actor performing the Check
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#checker">checker</a>
	 */
	public static final IRI checker;

	/**
	 * checkRemark
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#checkRemark}.
	 * <p>
	 * Free text remarks to the check result
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#checkRemark">checkRemark</a>
	 */
	public static final IRI checkRemark;

	/**
	 * checks
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#checks}.
	 * <p>
	 * References to the CheckActions performed on the object
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#checks">checks</a>
	 */
	public static final IRI checks;

	/**
	 * checksum
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#checksum}.
	 * <p>
	 * Checksum of the document to validate its integrity
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#checksum">checksum</a>
	 */
	public static final IRI checksum;

	/**
	 * checkTemplate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#checkTemplate}.
	 * <p>
	 * Reference to the CheckTemplate the Question is from
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#checkTemplate">checkTemplate</a>
	 */
	public static final IRI checkTemplate;

	/**
	 * CheckTemplate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#CheckTemplate}.
	 * <p>
	 * CheckTemplate is the body of a check, holding links to multiple
	 * Question LOs and is provided by the party in charge of the template
	 * that is used 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#CheckTemplate">CheckTemplate</a>
	 */
	public static final IRI CheckTemplate;

	/**
	 * checkTotalResult
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#checkTotalResult}.
	 * <p>
	 * Reference to the result of the Check
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#checkTotalResult">checkTotalResult</a>
	 */
	public static final IRI checkTotalResult;

	/**
	 * CheckTotalResult
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#CheckTotalResult}.
	 * <p>
	 * CheckTotalResult holds the result of a Check and should be provided by
	 * the party executing and accounting for the check result
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#CheckTotalResult">CheckTotalResult</a>
	 */
	public static final IRI CheckTotalResult;

	/**
	 * cityCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#cityCode}.
	 * <p>
	 * UN/LOCODE city code (5 letter) or IATA city code (3 letter)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#cityCode">cityCode</a>
	 */
	public static final IRI cityCode;

	/**
	 * co2Emissions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#co2Emissions}.
	 * <p>
	 * References to CO2Emissions
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#co2Emissions">co2Emissions</a>
	 */
	public static final IRI co2Emissions;

	/**
	 * CO2Emissions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#CO2Emissions}.
	 * <p>
	 * CO2 Calculation
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#CO2Emissions">CO2Emissions</a>
	 */
	public static final IRI CO2Emissions;

	/**
	 * code
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#code}.
	 * <p>
	 * Code or short version of a code, for example "CH" for Switzerland when
	 * referring to the UN/LOCODE code list
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#code">code</a>
	 */
	public static final IRI code;

	/**
	 * codeDescription
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#codeDescription}.
	 * <p>
	 * Description or long version of the code, for example "Switzerland" for
	 * Switzerland when referring to the UN/LOCODE code list
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#codeDescription">codeDescription</a>
	 */
	public static final IRI codeDescription;

	/**
	 * codeLevel
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#codeLevel}.
	 * <p>
	 * Integer indicating the level of a code if a codelists is hierarchical,
	 * for example HS-Codes
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#codeLevel">codeLevel</a>
	 */
	public static final IRI codeLevel;

	/**
	 * CodeListElement
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#CodeListElement}.
	 * <p>
	 * Embedded object to transmit codes from non-RDF code lists in 1R in a
	 * semi-structured way. Code lists may be externally maintained codes
	 * (such as HS codes) or carrier-specific codes. If a code is present in
	 * RDF-form as Named Individual (like in the 1R core code lists
	 * ontology), it suffices to put in its IRI
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#CodeListElement">CodeListElement</a>
	 */
	public static final IRI CodeListElement;

	/**
	 * codeListName
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#codeListName}.
	 * <p>
	 * Official name of the code list without version number when direct
	 * reference is not possible, for example "UN/LOCODE" when referring to
	 * the UN/LOCODE code list
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#codeListName">codeListName</a>
	 */
	public static final IRI codeListName;

	/**
	 * codeListReference
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#codeListReference}.
	 * <p>
	 * URL to access the code list the code is taken from, for example
	 * "https://unece.org/trade/cefact/unlocode-code-list-country-and-territory"
	 * for UN/LOCODE.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#codeListReference">codeListReference</a>
	 */
	public static final IRI codeListReference;

	/**
	 * codeListVersion
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#codeListVersion}.
	 * <p>
	 * Version of the code list, for example "223-1" for UN/LOCODE. Used if
	 * the property codeListName is used or the version is not apparent from
	 * the resource referred to in property codeListReference.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#codeListVersion">codeListVersion</a>
	 */
	public static final IRI codeListVersion;

	/**
	 * coload
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#coload}.
	 * <p>
	 * Coload indicator for the pieces (boolean)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#coload">coload</a>
	 */
	public static final IRI coload;

	/**
	 * commission
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#commission}.
	 * <p>
	 * The commission amount in favour of the Cargo Agent/Associate,
	 * applicable for the shipment concerned
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#commission">commission</a>
	 */
	public static final IRI commission;

	/**
	 * commissionIndicator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#commissionIndicator}.
	 * <p>
	 * Indicates if commission is applied. Boolean
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#commissionIndicator">commissionIndicator</a>
	 */
	public static final IRI commissionIndicator;

	/**
	 * commissionPercentage
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#commissionPercentage}.
	 * <p>
	 * The commission percentage in favour of the Cargo Agent/Associate,
	 * applicable for the shipment concerned
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#commissionPercentage">commissionPercentage</a>
	 */
	public static final IRI commissionPercentage;

	/**
	 * commodityItemNumber
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#commodityItemNumber}.
	 * <p>
	 * Indicates the specific commodity on which the rate class code is
	 * applied
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#commodityItemNumber">commodityItemNumber</a>
	 */
	public static final IRI commodityItemNumber;

	/**
	 * {@code https://onerecord.iata.org/ns/cargo#commodityItemNumberForRate}.
	 * <p>
	 * Indicates the specific commodity on which the rate class code is
	 * applied
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#commodityItemNumberForRate">commodityItemNumberForRate</a>
	 */
	public static final IRI commodityItemNumberForRate;

	/**
	 * Company
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Company}.
	 * <p>
	 * Company details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Company">Company</a>
	 */
	public static final IRI Company;

	/**
	 * COMPLETE
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#COMPLETE}.
	 * <p>
	 * Used when a LogisticsActivity is complete
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#COMPLETE">COMPLETE</a>
	 */
	public static final IRI COMPLETE;

	/**
	 * complianceDeclarationText
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#complianceDeclarationText}.
	 * <p>
	 * Contains the warning message complying with the regulations text note.
	 * This field is mandatory for air (Air) 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#complianceDeclarationText">complianceDeclarationText</a>
	 */
	public static final IRI complianceDeclarationText;

	/**
	 * composedMaterials
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#composedMaterials}.
	 * <p>
	 * References to the Materials being built-up or broken-down
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#composedMaterials">composedMaterials</a>
	 */
	public static final IRI composedMaterials;

	/**
	 * composedPieces
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#composedPieces}.
	 * <p>
	 * References to the Pieces being built-up or broken-down
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#composedPieces">composedPieces</a>
	 */
	public static final IRI composedPieces;

	/**
	 * Composing
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Composing}.
	 * <p>
	 * Action to describe build-up or break-down of LoadingUnits
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Composing">Composing</a>
	 */
	public static final IRI Composing;

	/**
	 * COMPOSITION
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#COMPOSITION}.
	 * <p>
	 * Describes a composition, for example the loading of a container or the
	 * build-up of an ULD
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#COMPOSITION">COMPOSITION</a>
	 */
	public static final IRI COMPOSITION;

	/**
	 * compositionActions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#compositionActions}.
	 * <p>
	 * References to all CompositionActions performed for the UnitComposition
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#compositionActions">compositionActions</a>
	 */
	public static final IRI compositionActions;

	/**
	 * compositionIdentifier
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#compositionIdentifier}.
	 * <p>
	 * Short text holding the process number if necessary
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#compositionIdentifier">compositionIdentifier</a>
	 */
	public static final IRI compositionIdentifier;

	/**
	 * CompositionType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#CompositionType}.
	 * <p>
	 * Restricted code list for Composing subtypes
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#CompositionType">CompositionType</a>
	 */
	public static final IRI CompositionType;

	/**
	 * compositionType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#compositionType}.
	 * <p>
	 * Enum stating whether the CompositionAction describes build-up or
	 * break-down
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#compositionType">compositionType</a>
	 */
	public static final IRI compositionType;

	/**
	 * CONFIRMED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#CONFIRMED}.
	 * <p>
	 * Used when a booking is confirmed
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#CONFIRMED">CONFIRMED</a>
	 */
	public static final IRI CONFIRMED;

	/**
	 * connectedSensors
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#connectedSensors}.
	 * <p>
	 * Reference to the sensors linked to the device
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#connectedSensors">connectedSensors</a>
	 */
	public static final IRI connectedSensors;

	/**
	 * consignee
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#consignee}.
	 * <p>
	 * Reference to the Organization that fulfills the role of the consignee,
	 * for a LiveAnimalsEpermit it has to include complete name and address
	 * (box 3)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#consignee">consignee</a>
	 */
	public static final IRI consignee;

	/**
	 * consignmentItems
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#consignmentItems}.
	 * <p>
	 * Reference to te pieces (Live Animals) of the permit
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#consignmentItems">consignmentItems</a>
	 */
	public static final IRI consignmentItems;

	/**
	 * consignments
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#consignments}.
	 * <p>
	 * Reference to the pieces and properties linked to the Permit (box 7 to
	 * 12)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#consignments">consignments</a>
	 */
	public static final IRI consignments;

	/**
	 * consignorDeclarationSignature
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#consignorDeclarationSignature}.
	 * <p>
	 * Name of consignor signatory
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#consignorDeclarationSignature">consignorDeclarationSignature</a>
	 */
	public static final IRI consignorDeclarationSignature;

	/**
	 * consolidationIndicator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#consolidationIndicator}.
	 * <p>
	 * Indication if the shipment is a consolidation
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#consolidationIndicator">consolidationIndicator</a>
	 */
	public static final IRI consolidationIndicator;

	/**
	 * ContactDetail
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ContactDetail}.
	 * <p>
	 * Contact details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ContactDetail">ContactDetail</a>
	 */
	public static final IRI ContactDetail;

	/**
	 * contactDetails
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#contactDetails}.
	 * <p>
	 * Information about contactDetails
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#contactDetails">contactDetails</a>
	 */
	public static final IRI contactDetails;

	/**
	 * contactDetailType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#contactDetailType}.
	 * <p>
	 * Type of the contact details, e.g. Phone number, Mail address
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#contactDetailType">contactDetailType</a>
	 */
	public static final IRI contactDetailType;

	/**
	 * ContactDetailType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ContactDetailType}.
	 * <p>
	 * Open code list for types of contact details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ContactDetailType">ContactDetailType</a>
	 */
	public static final IRI ContactDetailType;

	/**
	 * contactPersons
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#contactPersons}.
	 * <p>
	 * References to Actors (Person, NonHumanActor) acting as contacts
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#contactPersons">contactPersons</a>
	 */
	public static final IRI contactPersons;

	/**
	 * ContactRole
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ContactRole}.
	 * <p>
	 * Open code list for roles of a contact
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ContactRole">ContactRole</a>
	 */
	public static final IRI ContactRole;

	/**
	 * contactRole
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#contactRole}.
	 * <p>
	 * Contact type - e.g. Emergency contact, Customs contact, Customer
	 * contact
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#contactRole">contactRole</a>
	 */
	public static final IRI contactRole;

	/**
	 * containedItems
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#containedItems}.
	 * <p>
	 * Reference to the item(s) contained in the piece
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#containedItems">containedItems</a>
	 */
	public static final IRI containedItems;

	/**
	 * containedPieces
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#containedPieces}.
	 * <p>
	 * Details of contained piece(s)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#containedPieces">containedPieces</a>
	 */
	public static final IRI containedPieces;

	/**
	 * contentCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#contentCode}.
	 * <p>
	 * Customs, Security and Regulatory Control Information Identifier. Coded
	 * indicator qualifying Customs related information: Item Number "I",
	 * Exemption Legend "L", System Downtime Reference "S", Unique
	 * Consignment Reference Number "U", Movement Reference Number "M" .
	 * Refers to Code List 1.1 Condition: At least one of the three elements
	 * (Country Code, Information Identifier or Customs, Security and
	 * Regulatory Control Information Identifier) must be completed
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#contentCode">contentCode</a>
	 */
	public static final IRI contentCode;

	/**
	 * contentOfDgProductRadioactive
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#contentOfDgProductRadioactive}.
	 * <p>
	 * Reference to the DgProductRadioactive this Isotope is contained in
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#contentOfDgProductRadioactive">contentOfDgProductRadioactive</a>
	 */
	public static final IRI contentOfDgProductRadioactive;

	/**
	 * contentProductionCountry
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#contentProductionCountry}.
	 * <p>
	 * Goods production country, mandatory when there are no Items. Refer ISO
	 * 3166-2
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#contentProductionCountry">contentProductionCountry</a>
	 */
	public static final IRI contentProductionCountry;

	/**
	 * contentProducts
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#contentProducts}.
	 * <p>
	 * Reference to the Products describing the content of the Piece,
	 * mandatory if no data on Item level is used
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#contentProducts">contentProducts</a>
	 */
	public static final IRI contentProducts;

	/**
	 * conversionFactor
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#conversionFactor}.
	 * <p>
	 * Volume to weight conversion factor
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#conversionFactor">conversionFactor</a>
	 */
	public static final IRI conversionFactor;

	/**
	 * copyIndicator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#copyIndicator}.
	 * <p>
	 * Indicates if the permit is a copy (true) or an original (false) (box
	 * 1)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#copyIndicator">copyIndicator</a>
	 */
	public static final IRI copyIndicator;

	/**
	 * correctionNumber
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#correctionNumber}.
	 * <p>
	 * Number of the adjustment
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#correctionNumber">correctionNumber</a>
	 */
	public static final IRI correctionNumber;

	/**
	 * correctionSerialNumber
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#correctionSerialNumber}.
	 * <p>
	 * Serial Number of the correction
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#correctionSerialNumber">correctionSerialNumber</a>
	 */
	public static final IRI correctionSerialNumber;

	/**
	 * country
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#country}.
	 * <p>
	 * Country details. Refer ISO 3166-2
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#country">country</a>
	 */
	public static final IRI country;

	/**
	 * coveringOrganization
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#coveringOrganization}.
	 * <p>
	 * Party covering the insurance 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#coveringOrganization">coveringOrganization</a>
	 */
	public static final IRI coveringOrganization;

	/**
	 * createdAtLocation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#createdAtLocation}.
	 * <p>
	 * Location of the document, e.g. location where the document was emitted
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#createdAtLocation">createdAtLocation</a>
	 */
	public static final IRI createdAtLocation;

	/**
	 * creationDate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#creationDate}.
	 * <p>
	 * DateTime at which the LogisticsEvent was posted
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#creationDate">creationDate</a>
	 */
	public static final IRI creationDate;

	/**
	 * criticalitySafetyIndexNumeric
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#criticalitySafetyIndexNumeric}.
	 * <p>
	 * Applies to fissile material only, other than fissile excepted. A
	 * numeric value expressed to one decimal place preceded by the letters
	 * CSI.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#criticalitySafetyIndexNumeric">criticalitySafetyIndexNumeric</a>
	 */
	public static final IRI criticalitySafetyIndexNumeric;

	/**
	 * currency
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#currency}.
	 * <p>
	 * Preferred unit for currency
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#currency">currency</a>
	 */
	public static final IRI currency;

	/**
	 * currencyUnit
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#currencyUnit}.
	 * <p>
	 * Information about the currency used in a CurrencyValue. Create an
	 * instance of CurrencyCode based on ISO 4217
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#currencyUnit">currencyUnit</a>
	 */
	public static final IRI currencyUnit;

	/**
	 * CurrencyValue
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#CurrencyValue}.
	 * <p>
	 * Embedded object to transmit currencies
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#CurrencyValue">CurrencyValue</a>
	 */
	public static final IRI CurrencyValue;

	/**
	 * CUSTOMER_CONTACT
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#CUSTOMER_CONTACT}.
	 * <p>
	 * Indicates a contact person as customer contact
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#CUSTOMER_CONTACT">CUSTOMER_CONTACT</a>
	 */
	public static final IRI CUSTOMER_CONTACT;

	/**
	 * CUSTOMS_CONTACT
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#CUSTOMS_CONTACT}.
	 * <p>
	 * Indicates a contact person as customs contact
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#CUSTOMS_CONTACT">CUSTOMS_CONTACT</a>
	 */
	public static final IRI CUSTOMS_CONTACT;

	/**
	 * customsInformation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#customsInformation}.
	 * <p>
	 * Customs details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#customsInformation">customsInformation</a>
	 */
	public static final IRI customsInformation;

	/**
	 * CustomsInformation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#CustomsInformation}.
	 * <p>
	 * Customs information details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#CustomsInformation">CustomsInformation</a>
	 */
	public static final IRI CustomsInformation;

	/**
	 * customsOriginCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#customsOriginCode}.
	 * <p>
	 * Code indicating the origin of goods for Customs purposes (e.g. For
	 * goods in free circulation in the EU) List to be provided by local
	 * authorities
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#customsOriginCode">customsOriginCode</a>
	 */
	public static final IRI customsOriginCode;

	/**
	 * damageFlag
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#damageFlag}.
	 * <p>
	 * Indicates if the ULD is Damaged
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#damageFlag">damageFlag</a>
	 */
	public static final IRI damageFlag;

	/**
	 * date
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#date}.
	 * <p>
	 * DateTime on which the CheckTemplate was released
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#date">date</a>
	 */
	public static final IRI date;

	/**
	 * declaredValueForCarriage
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#declaredValueForCarriage}.
	 * <p>
	 * The value of a shipment declared for carriage purposes
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#declaredValueForCarriage">declaredValueForCarriage</a>
	 */
	public static final IRI declaredValueForCarriage;

	/**
	 * declaredValueForCustoms
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#declaredValueForCustoms}.
	 * <p>
	 * The value of a shipment declared for customs purposes
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#declaredValueForCustoms">declaredValueForCustoms</a>
	 */
	public static final IRI declaredValueForCustoms;

	/**
	 * DECOMPOSITION
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#DECOMPOSITION}.
	 * <p>
	 * Describes a decomposition, for example the unloading of a container or
	 * the break-down of an ULD
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#DECOMPOSITION">DECOMPOSITION</a>
	 */
	public static final IRI DECOMPOSITION;

	/**
	 * DELETED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#DELETED}.
	 * <p>
	 * Used when a booking is deleted
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#DELETED">DELETED</a>
	 */
	public static final IRI DELETED;

	/**
	 * demurrageCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#demurrageCode}.
	 * <p>
	 * Contains three designator of demurrage code, refer to RP 1654 (BCC,
	 * HHH, XXX, ZZZ)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#demurrageCode">demurrageCode</a>
	 */
	public static final IRI demurrageCode;

	/**
	 * department
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#department}.
	 * <p>
	 * Department / Division / Unit
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#department">department</a>
	 */
	public static final IRI department;

	/**
	 * departureDate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#departureDate}.
	 * <p>
	 * Departure date and time of the leg
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#departureDate">departureDate</a>
	 */
	public static final IRI departureDate;

	/**
	 * departureLocation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#departureLocation}.
	 * <p>
	 * Reference to the depature Location
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#departureLocation">departureLocation</a>
	 */
	public static final IRI departureLocation;

	/**
	 * describedObjects
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#describedObjects}.
	 * <p>
	 * Reference to the Items or Pieces in which the product can be found.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#describedObjects">describedObjects</a>
	 */
	public static final IRI describedObjects;

	/**
	 * description
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#description}.
	 * <p>
	 * Natural language description if required
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#description">description</a>
	 */
	public static final IRI description;

	/**
	 * destinationCharges
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#destinationCharges}.
	 * <p>
	 * Charges levied at destination accruing to the last carrier, in
	 * destination currency
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#destinationCharges">destinationCharges</a>
	 */
	public static final IRI destinationCharges;

	/**
	 * destinationCurrencyRate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#destinationCurrencyRate}.
	 * <p>
	 * Conversion rate applied
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#destinationCurrencyRate">destinationCurrencyRate</a>
	 */
	public static final IRI destinationCurrencyRate;

	/**
	 * detailedWaybill
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#detailedWaybill}.
	 * <p>
	 * Reference to the Waybill
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#detailedWaybill">detailedWaybill</a>
	 */
	public static final IRI detailedWaybill;

	/**
	 * deviceModel
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#deviceModel}.
	 * <p>
	 * Commercial denomination of the device
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#deviceModel">deviceModel</a>
	 */
	public static final IRI deviceModel;

	/**
	 * DgDeclaration
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#DgDeclaration}.
	 * <p>
	 * Dangerous goods declaration
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#DgDeclaration">DgDeclaration</a>
	 */
	public static final IRI DgDeclaration;

	/**
	 * dgDeclaration
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#dgDeclaration}.
	 * <p>
	 * Reference to the Dangerous Goods declaration
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#dgDeclaration">dgDeclaration</a>
	 */
	public static final IRI dgDeclaration;

	/**
	 * DgProductRadioactive
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#DgProductRadioactive}.
	 * <p>
	 * Details of the radioactive products 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#DgProductRadioactive">DgProductRadioactive</a>
	 */
	public static final IRI DgProductRadioactive;

	/**
	 * DgRadioactiveIsotope
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#DgRadioactiveIsotope}.
	 * <p>
	 * Details of the radioactive isotope contained in the product
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#DgRadioactiveIsotope">DgRadioactiveIsotope</a>
	 */
	public static final IRI DgRadioactiveIsotope;

	/**
	 * dgRadioactiveMaterial
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#dgRadioactiveMaterial}.
	 * <p>
	 * Dg Radioactive Material
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#dgRadioactiveMaterial">dgRadioactiveMaterial</a>
	 */
	public static final IRI dgRadioactiveMaterial;

	/**
	 * dgRaTypeCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#dgRaTypeCode}.
	 * <p>
	 * The category of the package or all packed in one. Complete text to be
	 * transmitted: I-White, II-Yellow, III-Yellow instead of I, II, III
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#dgRaTypeCode">dgRaTypeCode</a>
	 */
	public static final IRI dgRaTypeCode;

	/**
	 * Dimensions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Dimensions}.
	 * <p>
	 * Dimension details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Dimensions">Dimensions</a>
	 */
	public static final IRI Dimensions;

	/**
	 * dimensions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#dimensions}.
	 * <p>
	 * Dimensions details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#dimensions">dimensions</a>
	 */
	public static final IRI dimensions;

	/**
	 * dimensionsForRate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#dimensionsForRate}.
	 * <p>
	 * Information about the Dimensions used for the rate descbribed by the
	 * Line Item
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#dimensionsForRate">dimensionsForRate</a>
	 */
	public static final IRI dimensionsForRate;

	/**
	 * dimensionsUnit
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#dimensionsUnit}.
	 * <p>
	 * Preferred unit for measurement and dimensions
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#dimensionsUnit">dimensionsUnit</a>
	 */
	public static final IRI dimensionsUnit;

	/**
	 * DIRECT
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#DIRECT}.
	 * <p>
	 * Indicates a Direct waybill
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#DIRECT">DIRECT</a>
	 */
	public static final IRI DIRECT;

	/**
	 * direction
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#direction}.
	 * <p>
	 * Direction to indicate if it's Inbound or Outbound
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#direction">direction</a>
	 */
	public static final IRI direction;

	/**
	 * DirectionType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#DirectionType}.
	 * <p>
	 * Restricted code list for the direction of a MovementTime
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#DirectionType">DirectionType</a>
	 */
	public static final IRI DirectionType;

	/**
	 * discount
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#discount}.
	 * <p>
	 * This is used as a discount to the “official” transportation charge on
	 * AWB to arrive at actual selling price
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#discount">discount</a>
	 */
	public static final IRI discount;

	/**
	 * distanceCalculated
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#distanceCalculated}.
	 * <p>
	 * Information about the calculated distance
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#distanceCalculated">distanceCalculated</a>
	 */
	public static final IRI distanceCalculated;

	/**
	 * distanceMeasured
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#distanceMeasured}.
	 * <p>
	 * Information about the measured distance
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#distanceMeasured">distanceMeasured</a>
	 */
	public static final IRI distanceMeasured;

	/**
	 * documentIdentifier
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#documentIdentifier}.
	 * <p>
	 * Unique document identifier
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#documentIdentifier">documentIdentifier</a>
	 */
	public static final IRI documentIdentifier;

	/**
	 * documentLink
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#documentLink}.
	 * <p>
	 * Link to the document, e.g. URL of the file where it is hosted
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#documentLink">documentLink</a>
	 */
	public static final IRI documentLink;

	/**
	 * documentName
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#documentName}.
	 * <p>
	 * If no DocumentType provided, name of the referenced document 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#documentName">documentName</a>
	 */
	public static final IRI documentName;

	/**
	 * documents
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#documents}.
	 * <p>
	 * Linked documents to the person, e.g. driver's license, ID, etc.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#documents">documents</a>
	 */
	public static final IRI documents;

	/**
	 * documentType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#documentType}.
	 * <p>
	 * Type of the referenced document . Can refer UNEDIFACT 11 e.g. 74 - Air
	 * Waybill, but not limited to
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#documentType">documentType</a>
	 */
	public static final IRI documentType;

	/**
	 * documentVersion
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#documentVersion}.
	 * <p>
	 * Document version number
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#documentVersion">documentVersion</a>
	 */
	public static final IRI documentVersion;

	/**
	 * dryIceWeight
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#dryIceWeight}.
	 * <p>
	 * Weight of dry ice
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#dryIceWeight">dryIceWeight</a>
	 */
	public static final IRI dryIceWeight;

	/**
	 * earliestAcceptanceTime
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#earliestAcceptanceTime}.
	 * <p>
	 * Earliest acceptance date time (requested or proposed)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#earliestAcceptanceTime">earliestAcceptanceTime</a>
	 */
	public static final IRI earliestAcceptanceTime;

	/**
	 * elevation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#elevation}.
	 * <p>
	 * Elevation from sea level - Change of data type to Value as of ontology
	 * v1.1
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#elevation">elevation</a>
	 */
	public static final IRI elevation;

	/**
	 * EMAIL_ADDRESS
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#EMAIL_ADDRESS}.
	 * <p>
	 * Indicates a contact detail as email address
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#EMAIL_ADDRESS">EMAIL_ADDRESS</a>
	 */
	public static final IRI EMAIL_ADDRESS;

	/**
	 * EMERGENCY_CONTACT
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#EMERGENCY_CONTACT}.
	 * <p>
	 * Indicates a contact person as emergency contact
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#EMERGENCY_CONTACT">EMERGENCY_CONTACT</a>
	 */
	public static final IRI EMERGENCY_CONTACT;

	/**
	 * emergencyContact
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#emergencyContact}.
	 * <p>
	 * Contains the Emergency contact name (e.g. the name of the agency) and
	 * phone number (min required)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#emergencyContact">emergencyContact</a>
	 */
	public static final IRI emergencyContact;

	/**
	 * employeeId
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#employeeId}.
	 * <p>
	 * Employee ID
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#employeeId">employeeId</a>
	 */
	public static final IRI employeeId;

	/**
	 * entitlement
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#entitlement}.
	 * <p>
	 * Entitlement code to define if charges are Due carrier (C) or Due agent
	 * (A). Refer to CXML Code List 1.3
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#entitlement">entitlement</a>
	 */
	public static final IRI entitlement;

	/**
	 * epermit
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#epermit}.
	 * <p>
	 * Reference to the Epermit of the consignment
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#epermit">epermit</a>
	 */
	public static final IRI epermit;

	/**
	 * EpermitConsignment
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#EpermitConsignment}.
	 * <p>
	 * Details of the pieces (Live animals) of the permit and specific
	 * information such as quantity measured and used to date quota
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#EpermitConsignment">EpermitConsignment</a>
	 */
	public static final IRI EpermitConsignment;

	/**
	 * epermitNumber
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#epermitNumber}.
	 * <p>
	 * The original number is a unique number allocated to each document by
	 * the relevant Management Authority. (box 1)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#epermitNumber">epermitNumber</a>
	 */
	public static final IRI epermitNumber;

	/**
	 * EpermitSignature
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#EpermitSignature}.
	 * <p>
	 * Signature details of the Epermit for Live Animals
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#EpermitSignature">EpermitSignature</a>
	 */
	public static final IRI EpermitSignature;

	/**
	 * ESTIMATED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ESTIMATED}.
	 * <p>
	 * Used when a time is estimated
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ESTIMATED">ESTIMATED</a>
	 */
	public static final IRI ESTIMATED;

	/**
	 * eventCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#eventCode}.
	 * <p>
	 * Movement or milestone code. Can hold a named individual of the
	 * StatusCode core code list (corresponding to cXML code list 1.18), but
	 * can also be referring to different code lists.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#eventCode">eventCode</a>
	 */
	public static final IRI eventCode;

	/**
	 * eventDate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#eventDate}.
	 * <p>
	 * Date and time of the event
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#eventDate">eventDate</a>
	 */
	public static final IRI eventDate;

	/**
	 * eventFor
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#eventFor}.
	 * <p>
	 * Refers to the URI of the linked object(s)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#eventFor">eventFor</a>
	 */
	public static final IRI eventFor;

	/**
	 * eventLocation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#eventLocation}.
	 * <p>
	 * Location of event
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#eventLocation">eventLocation</a>
	 */
	public static final IRI eventLocation;

	/**
	 * eventName
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#eventName}.
	 * <p>
	 * If no EventCode provided, event name - e.g. Security clearance
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#eventName">eventName</a>
	 */
	public static final IRI eventName;

	/**
	 * events
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#events}.
	 * <p>
	 * Events object
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#events">events</a>
	 */
	public static final IRI events;

	/**
	 * eventTimeType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#eventTimeType}.
	 * <p>
	 * Indicates type of event e.g. Scheduled, Estimated, Actual
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#eventTimeType">eventTimeType</a>
	 */
	public static final IRI eventTimeType;

	/**
	 * EventTimeType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#EventTimeType}.
	 * <p>
	 * Restricted code list for acceptable event times
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#EventTimeType">EventTimeType</a>
	 */
	public static final IRI EventTimeType;

	/**
	 * examiningQuantity
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#examiningQuantity}.
	 * <p>
	 * Quatity measured by the examining authority (box 14)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#examiningQuantity">examiningQuantity</a>
	 */
	public static final IRI examiningQuantity;

	/**
	 * exchangeRate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#exchangeRate}.
	 * <p>
	 * The Rate at which the Air Waybill Amount has been multiplied to arrive
	 * at the amount of settlement.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#exchangeRate">exchangeRate</a>
	 */
	public static final IRI exchangeRate;

	/**
	 * excludedViaPoints
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#excludedViaPoints}.
	 * <p>
	 * Locations of excluded Via Points
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#excludedViaPoints">excludedViaPoints</a>
	 */
	public static final IRI excludedViaPoints;

	/**
	 * exclusiveUseIndicator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#exclusiveUseIndicator}.
	 * <p>
	 * Indicates an exclusive use shipment
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#exclusiveUseIndicator">exclusiveUseIndicator</a>
	 */
	public static final IRI exclusiveUseIndicator;

	/**
	 * ExecutionStatus
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ExecutionStatus}.
	 * <p>
	 * Restricted code list for the execution status of activities
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ExecutionStatus">ExecutionStatus</a>
	 */
	public static final IRI ExecutionStatus;

	/**
	 * executionStatus
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#executionStatus}.
	 * <p>
	 * Enum stating the status of the Activity
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#executionStatus">executionStatus</a>
	 */
	public static final IRI executionStatus;

	/**
	 * EXPECTED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#EXPECTED}.
	 * <p>
	 * Used when a time is expected
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#EXPECTED">EXPECTED</a>
	 */
	public static final IRI EXPECTED;

	/**
	 * expectedCommodity
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#expectedCommodity}.
	 * <p>
	 * Expected commodity of the shipment as per Commodity Code list. Either
	 * this or expected HS code required
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#expectedCommodity">expectedCommodity</a>
	 */
	public static final IRI expectedCommodity;

	/**
	 * expectedHScode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#expectedHScode}.
	 * <p>
	 * Expected commodity of the shipment as per HS code (at least 6 digits).
	 * Either this or expectedCommodityCode required
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#expectedHScode">expectedHScode</a>
	 */
	public static final IRI expectedHScode;

	/**
	 * EXPIRED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#EXPIRED}.
	 * <p>
	 * Used when a booking option proposal is expired
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#EXPIRED">EXPIRED</a>
	 */
	public static final IRI EXPIRED;

	/**
	 * expiryDate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#expiryDate}.
	 * <p>
	 * Product expiry date - e.g. for perishables goods or goods with
	 * programmed obsolescence
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#expiryDate">expiryDate</a>
	 */
	public static final IRI expiryDate;

	/**
	 * explosiveCompatibilityGroupCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#explosiveCompatibilityGroupCode}.
	 * <p>
	 * Specifies the reference to the group which identifies the kind of
	 * substances and articles that are deemed to be compatible. Mandatory
	 * field in case of transport of explosive articles or substances
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#explosiveCompatibilityGroupCode">explosiveCompatibilityGroupCode</a>
	 */
	public static final IRI explosiveCompatibilityGroupCode;

	/**
	 * exportTradeCountry
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#exportTradeCountry}.
	 * <p>
	 * Country of last re-export (box 12a). Refer ISO 3166-2
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#exportTradeCountry">exportTradeCountry</a>
	 */
	public static final IRI exportTradeCountry;

	/**
	 * ExternalReference
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ExternalReference}.
	 * <p>
	 * Reference documents details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ExternalReference">ExternalReference</a>
	 */
	public static final IRI ExternalReference;

	/**
	 * externalReferences
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#externalReferences}.
	 * <p>
	 * References to all associated ExternalReferences
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#externalReferences">externalReferences</a>
	 */
	public static final IRI externalReferences;

	/**
	 * FAX_NUMBER
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#FAX_NUMBER}.
	 * <p>
	 * Indicates a contact detail as fax number
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#FAX_NUMBER">FAX_NUMBER</a>
	 */
	public static final IRI FAX_NUMBER;

	/**
	 * firstName
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#firstName}.
	 * <p>
	 * First name / given name
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#firstName">firstName</a>
	 */
	public static final IRI firstName;

	/**
	 * fissileExceptionIndicator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#fissileExceptionIndicator}.
	 * <p>
	 * Indicates if Fissile is excepted
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#fissileExceptionIndicator">fissileExceptionIndicator</a>
	 */
	public static final IRI fissileExceptionIndicator;

	/**
	 * fissileExceptionReference
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#fissileExceptionReference}.
	 * <p>
	 * Fissile exception reference, mandatory if Fissile Exception Indicator
	 * is true.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#fissileExceptionReference">fissileExceptionReference</a>
	 */
	public static final IRI fissileExceptionReference;

	/**
	 * forBookingOption
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#forBookingOption}.
	 * <p>
	 * Reference to the BookingOption the LogisticsObject is detailling
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#forBookingOption">forBookingOption</a>
	 */
	public static final IRI forBookingOption;

	/**
	 * forBookingOptionRequest
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#forBookingOptionRequest}.
	 * <p>
	 * Reference to the BookingOptionRequest the information of the
	 * LogisticsObject is detailling
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#forBookingOptionRequest">forBookingOptionRequest</a>
	 */
	public static final IRI forBookingOptionRequest;

	/**
	 * forBookingRequest
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#forBookingRequest}.
	 * <p>
	 * Reference to the Booking Request the of the Booking Option
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#forBookingRequest">forBookingRequest</a>
	 */
	public static final IRI forBookingRequest;

	/**
	 * forEpermit
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#forEpermit}.
	 * <p>
	 * Reference to the LiveAnimalsEpermit this Signature applies to
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#forEpermit">forEpermit</a>
	 */
	public static final IRI forEpermit;

	/**
	 * forPrices
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#forPrices}.
	 * <p>
	 * Reference to the Prices based on this Ratings
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#forPrices">forPrices</a>
	 */
	public static final IRI forPrices;

	/**
	 * forProductDg
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#forProductDg}.
	 * <p>
	 * Reference to the ProductDg this DgProductRadiosctive details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#forProductDg">forProductDg</a>
	 */
	public static final IRI forProductDg;

	/**
	 * fuelAmountCalculated
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#fuelAmountCalculated}.
	 * <p>
	 * Information about the calculated fuel amount
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#fuelAmountCalculated">fuelAmountCalculated</a>
	 */
	public static final IRI fuelAmountCalculated;

	/**
	 * fuelAmountMeasured
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#fuelAmountMeasured}.
	 * <p>
	 * Information about the measured fuel amount
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#fuelAmountMeasured">fuelAmountMeasured</a>
	 */
	public static final IRI fuelAmountMeasured;

	/**
	 * fuelType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#fuelType}.
	 * <p>
	 * e.g. Kerosene, Diesel, SAF, Electricity [renewable], Electricity
	 * [non-renewable]
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#fuelType">fuelType</a>
	 */
	public static final IRI fuelType;

	/**
	 * fulfillsUldTypeCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#fulfillsUldTypeCode}.
	 * <p>
	 * Text holding an ULD Type Code if the Piece fulfills it before
	 * UnitComposition
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#fulfillsUldTypeCode">fulfillsUldTypeCode</a>
	 */
	public static final IRI fulfillsUldTypeCode;

	/**
	 * GEOLOCATION
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#GEOLOCATION}.
	 * <p>
	 * Indicates the sensor type as geolocation
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#GEOLOCATION">GEOLOCATION</a>
	 */
	public static final IRI GEOLOCATION;

	/**
	 * geolocation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#geolocation}.
	 * <p>
	 * Geolocation details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#geolocation">geolocation</a>
	 */
	public static final IRI geolocation;

	/**
	 * Geolocation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Geolocation}.
	 * <p>
	 * Geolocation details - e.g. for drones, automated vehicles
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Geolocation">Geolocation</a>
	 */
	public static final IRI Geolocation;

	/**
	 * givenAtLocation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#givenAtLocation}.
	 * <p>
	 * Reference to the Location from which the Question was answered,
	 * relevant for split checks with documentary and physical elements
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#givenAtLocation">givenAtLocation</a>
	 */
	public static final IRI givenAtLocation;

	/**
	 * goodsDescription
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#goodsDescription}.
	 * <p>
	 * Description of goods, for the BookingShipment the commodity list
	 * defined by Modernizing Cargo Distribution MCD working group can be
	 * used as a referential.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#goodsDescription">goodsDescription</a>
	 */
	public static final IRI goodsDescription;

	/**
	 * goodsDescriptionForRate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#goodsDescriptionForRate}.
	 * <p>
	 * Goods description used in the rate described by the Line Item
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#goodsDescriptionForRate">goodsDescriptionForRate</a>
	 */
	public static final IRI goodsDescriptionForRate;

	/**
	 * goodsTypeCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#goodsTypeCode}.
	 * <p>
	 * Appendix number of the convention (I, II or III) (box 1)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#goodsTypeCode">goodsTypeCode</a>
	 */
	public static final IRI goodsTypeCode;

	/**
	 * goodsTypeExtensionCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#goodsTypeExtensionCode}.
	 * <p>
	 * Appendix number of the convention (I, II or III) (box 1)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#goodsTypeExtensionCode">goodsTypeExtensionCode</a>
	 */
	public static final IRI goodsTypeExtensionCode;

	/**
	 * grandTotal
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#grandTotal}.
	 * <p>
	 * Total price
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#grandTotal">grandTotal</a>
	 */
	public static final IRI grandTotal;

	/**
	 * grossWeight
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#grossWeight}.
	 * <p>
	 * Weight details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#grossWeight">grossWeight</a>
	 */
	public static final IRI grossWeight;

	/**
	 * grossWeightForRate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#grossWeightForRate}.
	 * <p>
	 * Gross weight for which the rate description details apply
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#grossWeightForRate">grossWeightForRate</a>
	 */
	public static final IRI grossWeightForRate;

	/**
	 * groundsForExemption
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#groundsForExemption}.
	 * <p>
	 * Exemption code - e.g. BIOM- Bio-Medical Samples SMUS - small
	 * undersized shipments MAIL - mail BIOM - bio-medical samples DIPL -
	 * diplomatic bags or diplomatic mail LFSM - life-saving materials NUCL -
	 * nuclear materials TRNS - transfer or transshipment
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#groundsForExemption">groundsForExemption</a>
	 */
	public static final IRI groundsForExemption;

	/**
	 * handlingInformation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#handlingInformation}.
	 * <p>
	 * Free text. This may include items such as Control temperature for
	 * substances stabilized by temperature control, name and telephone
	 * number of a responsible person for infectious substances. 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#handlingInformation">handlingInformation</a>
	 */
	public static final IRI handlingInformation;

	/**
	 * hazardClassificationId
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#hazardClassificationId}.
	 * <p>
	 * Identifies the hazard class / division identification containing a
	 * numeric field separated by a decimal
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#hazardClassificationId">hazardClassificationId</a>
	 */
	public static final IRI hazardClassificationId;

	/**
	 * height
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#height}.
	 * <p>
	 * Height
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#height">height</a>
	 */
	public static final IRI height;

	/**
	 * HOUSE
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#HOUSE}.
	 * <p>
	 * Indicates a House Waybill
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#HOUSE">HOUSE</a>
	 */
	public static final IRI HOUSE;

	/**
	 * houseWaybills
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#houseWaybills}.
	 * <p>
	 * Refers to the Waybill(s) contained
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#houseWaybills">houseWaybills</a>
	 */
	public static final IRI houseWaybills;

	/**
	 * hsCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#hsCode}.
	 * <p>
	 * Harmonized Commodity code, refer to hsType used. 6 minimum digits are
	 * expected.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#hsCode">hsCode</a>
	 */
	public static final IRI hsCode;

	/**
	 * hsCodeForRate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#hsCodeForRate}.
	 * <p>
	 * Harmonized Commodity code, refer to hsType used. 6 minimum digits are
	 * expected.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#hsCodeForRate">hsCodeForRate</a>
	 */
	public static final IRI hsCodeForRate;

	/**
	 * hsCommodityDescription
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#hsCommodityDescription}.
	 * <p>
	 * Commodity description
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#hsCommodityDescription">hsCommodityDescription</a>
	 */
	public static final IRI hsCommodityDescription;

	/**
	 * hsCommodityName
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#hsCommodityName}.
	 * <p>
	 * If no Code provided, name of commodity
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#hsCommodityName">hsCommodityName</a>
	 */
	public static final IRI hsCommodityName;

	/**
	 * hsType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#hsType}.
	 * <p>
	 * Reference identifying the type of standard code to be used for the
	 * Commodity Classification (Brussels Tariff Nomenclature, EU Harmonized
	 * System Code, UN Standard International Trade Classification).
	 * Mandatory if the commodity code is more than 6 digits
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#hsType">hsType</a>
	 */
	public static final IRI hsType;

	/**
	 * HUMIDITY
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#HUMIDITY}.
	 * <p>
	 * Indicates the sensor type as humidity
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#HUMIDITY">HUMIDITY</a>
	 */
	public static final IRI HUMIDITY;

	/**
	 * iataCargoAgentCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#iataCargoAgentCode}.
	 * <p>
	 * IATA accredited cargo agent 7 digit number
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#iataCargoAgentCode">iataCargoAgentCode</a>
	 */
	public static final IRI iataCargoAgentCode;

	/**
	 * iataCargoAgentLocationIdentifier
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#iataCargoAgentLocationIdentifier}.
	 * <p>
	 * IATA CASS cargo agent 4 digit branch number / location identifier
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#iataCargoAgentLocationIdentifier">iataCargoAgentLocationIdentifier</a>
	 */
	public static final IRI iataCargoAgentLocationIdentifier;

	/**
	 * INBOUND
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#INBOUND}.
	 * <p>
	 * Indicates the described direction in a movement time as inbound
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#INBOUND">INBOUND</a>
	 */
	public static final IRI INBOUND;

	/**
	 * includedViaPoints
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#includedViaPoints}.
	 * <p>
	 * Locations or stations to included in the routing
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#includedViaPoints">includedViaPoints</a>
	 */
	public static final IRI includedViaPoints;

	/**
	 * incoterms
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#incoterms}.
	 * <p>
	 * Standard codes as defined by UNCEFACT and ICC that correspond to
	 * international rules for the interpretation of the most commonly used
	 * trade terms in different countries. UNECE recommendation n. 5
	 * Incoterms 2.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#incoterms">incoterms</a>
	 */
	public static final IRI incoterms;

	/**
	 * inPiece
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#inPiece}.
	 * <p>
	 * Reference to the Piece this Item or Piece is contained in
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#inPiece">inPiece</a>
	 */
	public static final IRI inPiece;

	/**
	 * Insurance
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Insurance}.
	 * <p>
	 * Insurance details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Insurance">Insurance</a>
	 */
	public static final IRI Insurance;

	/**
	 * insurance
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#insurance}.
	 * <p>
	 * Insurance details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#insurance">insurance</a>
	 */
	public static final IRI insurance;

	/**
	 * insuredAmount
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#insuredAmount}.
	 * <p>
	 * Insured amount - amount covered by the insurance policy
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#insuredAmount">insuredAmount</a>
	 */
	public static final IRI insuredAmount;

	/**
	 * insuredShipments
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#insuredShipments}.
	 * <p>
	 * Reference to the shipments insured
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#insuredShipments">insuredShipments</a>
	 */
	public static final IRI insuredShipments;

	/**
	 * inUnitComposition
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#inUnitComposition}.
	 * <p>
	 * Reference to the Unit Composition the LoadingUnit is in
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#inUnitComposition">inUnitComposition</a>
	 */
	public static final IRI inUnitComposition;

	/**
	 * involvedInActions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#involvedInActions}.
	 * <p>
	 * References to the Actions the object is involved in
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#involvedInActions">involvedInActions</a>
	 */
	public static final IRI involvedInActions;

	/**
	 * involvedParties
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#involvedParties}.
	 * <p>
	 * Information about other Parties involved depending on the context of
	 * use
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#involvedParties">involvedParties</a>
	 */
	public static final IRI involvedParties;

	/**
	 * IotDevice
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#IotDevice}.
	 * <p>
	 * IoT Device details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#IotDevice">IotDevice</a>
	 */
	public static final IRI IotDevice;

	/**
	 * isotopeId
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#isotopeId}.
	 * <p>
	 * Id of each radionuclide or for mixtures of radionuclides.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#isotopeId">isotopeId</a>
	 */
	public static final IRI isotopeId;

	/**
	 * isotopeName
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#isotopeName}.
	 * <p>
	 * The name or symbol of each radionuclide or for mixtures of
	 * radionuclides, an appropriate general description, or a list of the
	 * most restrictive radionuclides. 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#isotopeName">isotopeName</a>
	 */
	public static final IRI isotopeName;

	/**
	 * isotopes
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#isotopes}.
	 * <p>
	 * DgRadioactiveIsotope.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#isotopes">isotopes</a>
	 */
	public static final IRI isotopes;

	/**
	 * issuedBy
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#issuedBy}.
	 * <p>
	 * Name of person (or employee ID) who issued the security status
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#issuedBy">issuedBy</a>
	 */
	public static final IRI issuedBy;

	/**
	 * issuedForPiece
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#issuedForPiece}.
	 * <p>
	 * Reference to the Piece the document was issued for
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#issuedForPiece">issuedForPiece</a>
	 */
	public static final IRI issuedForPiece;

	/**
	 * issuedForShipment
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#issuedForShipment}.
	 * <p>
	 * Reference to the shipment the document was issued for
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#issuedForShipment">issuedForShipment</a>
	 */
	public static final IRI issuedForShipment;

	/**
	 * issuedForWaybill
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#issuedForWaybill}.
	 * <p>
	 * Reference to the Waybill object
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#issuedForWaybill">issuedForWaybill</a>
	 */
	public static final IRI issuedForWaybill;

	/**
	 * issuedOn
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#issuedOn}.
	 * <p>
	 * Date and time when the security status was issued
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#issuedOn">issuedOn</a>
	 */
	public static final IRI issuedOn;

	/**
	 * Item
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Item}.
	 * <p>
	 * Item details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Item">Item</a>
	 */
	public static final IRI Item;

	/**
	 * ItemDg
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ItemDg}.
	 * <p>
	 * Dangerous Goods subtype of Item
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ItemDg">ItemDg</a>
	 */
	public static final IRI ItemDg;

	/**
	 * itemQuantity
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#itemQuantity}.
	 * <p>
	 * Quantity of the item when applicable, with associated units of measure
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#itemQuantity">itemQuantity</a>
	 */
	public static final IRI itemQuantity;

	/**
	 * jobTitle
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#jobTitle}.
	 * <p>
	 * Job title / position
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#jobTitle">jobTitle</a>
	 */
	public static final IRI jobTitle;

	/**
	 * knownShipper
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#knownShipper}.
	 * <p>
	 * Indication if shipper is a Known Shipper as per TSA grant
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#knownShipper">knownShipper</a>
	 */
	public static final IRI knownShipper;

	/**
	 * lastName
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#lastName}.
	 * <p>
	 * Last name / family name / surname
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#lastName">lastName</a>
	 */
	public static final IRI lastName;

	/**
	 * latestAcceptanceTime
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#latestAcceptanceTime}.
	 * <p>
	 * Latest Acceptance time as per CargoIQ definition (requested, proposed
	 * or actual)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#latestAcceptanceTime">latestAcceptanceTime</a>
	 */
	public static final IRI latestAcceptanceTime;

	/**
	 * latestArrivalTime
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#latestArrivalTime}.
	 * <p>
	 * Latest arrival time at destination
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#latestArrivalTime">latestArrivalTime</a>
	 */
	public static final IRI latestArrivalTime;

	/**
	 * latitude
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#latitude}.
	 * <p>
	 * Location latitude decimal
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#latitude">latitude</a>
	 */
	public static final IRI latitude;

	/**
	 * legacyTemplate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#legacyTemplate}.
	 * <p>
	 * Reference to an ExternalReference holding a legacy templats outside of
	 * ONE Record, such as a photo or pdf of a checksheet
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#legacyTemplate">legacyTemplate</a>
	 */
	public static final IRI legacyTemplate;

	/**
	 * TransportLegs
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#legNumber}.
	 * <p>
	 * Leg number
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#legNumber">legNumber</a>
	 */
	public static final IRI legNumber;

	/**
	 * length
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#length}.
	 * <p>
	 * Length
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#length">length</a>
	 */
	public static final IRI length;

	/**
	 * LIGHT
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#LIGHT}.
	 * <p>
	 * Indicates the sensor type as light
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#LIGHT">LIGHT</a>
	 */
	public static final IRI LIGHT;

	/**
	 * lineItemNumber
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#lineItemNumber}.
	 * <p>
	 * Number of the line item
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#lineItemNumber">lineItemNumber</a>
	 */
	public static final IRI lineItemNumber;

	/**
	 * LiveAnimalsEpermit
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#LiveAnimalsEpermit}.
	 * <p>
	 * Epermit for Live Animals details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#LiveAnimalsEpermit">LiveAnimalsEpermit</a>
	 */
	public static final IRI LiveAnimalsEpermit;

	/**
	 * loadedMaterials
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#loadedMaterials}.
	 * <p>
	 * References to Materials onloaded or offloaded
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#loadedMaterials">loadedMaterials</a>
	 */
	public static final IRI loadedMaterials;

	/**
	 * loadedPieces
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#loadedPieces}.
	 * <p>
	 * References to Pieces onloaded or offloaded
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#loadedPieces">loadedPieces</a>
	 */
	public static final IRI loadedPieces;

	/**
	 * loadedUnits
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#loadedUnits}.
	 * <p>
	 * References to LoadingUnits onloaded or offloaded
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#loadedUnits">loadedUnits</a>
	 */
	public static final IRI loadedUnits;

	/**
	 * Loading
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Loading}.
	 * <p>
	 * Action to describe onloading or offloading TransportMeans
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Loading">Loading</a>
	 */
	public static final IRI Loading;

	/**
	 * LOADING
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#LOADING}.
	 * <p>
	 * Describes a loading process, for example putting an ULD on an aircraft
	 * or a piece in a truck
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#LOADING">LOADING</a>
	 */
	public static final IRI LOADING;

	/**
	 * loadingActions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#loadingActions}.
	 * <p>
	 * References to all actions of type Loading performed for the
	 * TransportMovement
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#loadingActions">loadingActions</a>
	 */
	public static final IRI loadingActions;

	/**
	 * loadingIndicator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#loadingIndicator}.
	 * <p>
	 * ULD height or loading limitation code. Refer CXML Code List 1.47, e.g.
	 * R - ULD Height above 244 centimetres
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#loadingIndicator">loadingIndicator</a>
	 */
	public static final IRI loadingIndicator;

	/**
	 * LoadingMaterial
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#LoadingMaterial}.
	 * <p>
	 * LoadingMaterial describes transportable, complementary non-Piece
	 * objects such as dry ice or nets
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#LoadingMaterial">LoadingMaterial</a>
	 */
	public static final IRI LoadingMaterial;

	/**
	 * loadingPositionIdentifier
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#loadingPositionIdentifier}.
	 * <p>
	 * Short text stating the loading position in the TransportMeans
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#loadingPositionIdentifier">loadingPositionIdentifier</a>
	 */
	public static final IRI loadingPositionIdentifier;

	/**
	 * loadingType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#loadingType}.
	 * <p>
	 * Enum stating whether the LoadingAction describes onloading or
	 * offloading
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#loadingType">loadingType</a>
	 */
	public static final IRI loadingType;

	/**
	 * LoadingType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#LoadingType}.
	 * <p>
	 * Restricted code list for Loading subtypes
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#LoadingType">LoadingType</a>
	 */
	public static final IRI LoadingType;

	/**
	 * LoadingUnit
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#LoadingUnit}.
	 * <p>
	 * Common loading unit/container details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#LoadingUnit">LoadingUnit</a>
	 */
	public static final IRI LoadingUnit;

	/**
	 * onLoadingUnit
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#loadingUnit}.
	 * <p>
	 * Reference to the LoadingUnit composed in the Unit Composition or
	 * referenced in Composing actions
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#loadingUnit">loadingUnit</a>
	 */
	public static final IRI loadingUnit;

	/**
	 * LoadType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#LoadType}.
	 * <p>
	 * Restricted code list for the Load Type of a piece or shipment
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#LoadType">LoadType</a>
	 */
	public static final IRI LoadType;

	/**
	 * loadType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#loadType}.
	 * <p>
	 * Load type of the shipment or piece (Bulk, ULD, Pallet, Loose)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#loadType">loadType</a>
	 */
	public static final IRI loadType;

	/**
	 * Location
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Location}.
	 * <p>
	 * Location describes a physical location, e.g. an airport, a warehouse
	 * or a truck deck
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Location">Location</a>
	 */
	public static final IRI Location;

	/**
	 * locationCodes
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#locationCodes}.
	 * <p>
	 * Location code of airport, freight terminal, seaport, rail station.
	 * UN/LOCODE city code (5 letter) or IATA airport code (3 letter)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#locationCodes">locationCodes</a>
	 */
	public static final IRI locationCodes;

	/**
	 * locationName
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#locationName}.
	 * <p>
	 * Full name of the location
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#locationName">locationName</a>
	 */
	public static final IRI locationName;

	/**
	 * locationType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#locationType}.
	 * <p>
	 * Location type - e.g. Airport, Freight terminal, Rail station, Seaport,
	 * etc
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#locationType">locationType</a>
	 */
	public static final IRI locationType;

	/**
	 * LogisticsAction
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#LogisticsAction}.
	 * <p>
	 * Superclass: LogisticsAction is a specific task with a specific result
	 * performed on one or more physical LOs by one party in the context of
	 * an Activity
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#LogisticsAction">LogisticsAction</a>
	 */
	public static final IRI LogisticsAction;

	/**
	 * LogisticsActivity
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#LogisticsActivity}.
	 * <p>
	 * Superclass: LogisticsActivity is a scheduled set of tasks that is
	 * executed as part of one or more Services
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#LogisticsActivity">LogisticsActivity</a>
	 */
	public static final IRI LogisticsActivity;

	/**
	 * LogisticsAgent
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#LogisticsAgent}.
	 * <p>
	 * Superclass: LogisticsAgents describe acting entities in the logistics
	 * supply chain such as persons and organizations
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#LogisticsAgent">LogisticsAgent</a>
	 */
	public static final IRI LogisticsAgent;

	/**
	 * LogisticsEvent
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#LogisticsEvent}.
	 * <p>
	 * Event details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#LogisticsEvent">LogisticsEvent</a>
	 */
	public static final IRI LogisticsEvent;

	/**
	 * LogisticsObject
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#LogisticsObject}.
	 * <p>
	 * Logistics Object parent class, containing all common properties for
	 * logistics objects.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#LogisticsObject">LogisticsObject</a>
	 */
	public static final IRI LogisticsObject;

	/**
	 * LogisticsService
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#LogisticsService}.
	 * <p>
	 * Superclass: LogisticsService is a sequence of Activities provided by
	 * one Party to another
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#LogisticsService">LogisticsService</a>
	 */
	public static final IRI LogisticsService;

	/**
	 * longitude
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#longitude}.
	 * <p>
	 * Location longitude decimal
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#longitude">longitude</a>
	 */
	public static final IRI longitude;

	/**
	 * longText
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#longText}.
	 * <p>
	 * Long text of the question
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#longText">longText</a>
	 */
	public static final IRI longText;

	/**
	 * LOOSE
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#LOOSE}.
	 * <p>
	 * Indicates the load type as loose
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#LOOSE">LOOSE</a>
	 */
	public static final IRI LOOSE;

	/**
	 * LoosePiece
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#LoosePiece}.
	 * <p>
	 * LoosePiece details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#LoosePiece">LoosePiece</a>
	 */
	public static final IRI LoosePiece;

	/**
	 * lotNumber
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#lotNumber}.
	 * <p>
	 * Production lot number / reference
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#lotNumber">lotNumber</a>
	 */
	public static final IRI lotNumber;

	/**
	 * lowDispersibleIndicator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#lowDispersibleIndicator}.
	 * <p>
	 * A notation that the material is low dispersible radioactive material.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#lowDispersibleIndicator">lowDispersibleIndicator</a>
	 */
	public static final IRI lowDispersibleIndicator;

	/**
	 * MAIN_CARRIAGE
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#MAIN_CARRIAGE}.
	 * <p>
	 * Indicates the mode qualifier as main carriage
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#MAIN_CARRIAGE">MAIN_CARRIAGE</a>
	 */
	public static final IRI MAIN_CARRIAGE;

	/**
	 * manufacturer
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#manufacturer}.
	 * <p>
	 * Manufacturing company details and contacts
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#manufacturer">manufacturer</a>
	 */
	public static final IRI manufacturer;

	/**
	 * MASTER
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#MASTER}.
	 * <p>
	 * Indicates a Master Waybill
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#MASTER">MASTER</a>
	 */
	public static final IRI MASTER;

	/**
	 * masterWaybill
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#masterWaybill}.
	 * <p>
	 * Reference to the master Waybill if it is contained in one
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#masterWaybill">masterWaybill</a>
	 */
	public static final IRI masterWaybill;

	/**
	 * materialModel
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#materialModel}.
	 * <p>
	 * Model of the LoadingMaterial if any
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#materialModel">materialModel</a>
	 */
	public static final IRI materialModel;

	/**
	 * materialType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#materialType}.
	 * <p>
	 * Type of the LoadingMaterial
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#materialType">materialType</a>
	 */
	public static final IRI materialType;

	/**
	 * maximumQuantity
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#maximumQuantity}.
	 * <p>
	 * Maximum quantity
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#maximumQuantity">maximumQuantity</a>
	 */
	public static final IRI maximumQuantity;

	/**
	 * maxSegments
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#maxSegments}.
	 * <p>
	 * Maximum number of segments for the transportation of the goods. 1
	 * means direct flight
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#maxSegments">maxSegments</a>
	 */
	public static final IRI maxSegments;

	/**
	 * maxTemperature
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#maxTemperature}.
	 * <p>
	 * Maximum temperature of the range
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#maxTemperature">maxTemperature</a>
	 */
	public static final IRI maxTemperature;

	/**
	 * Measurement
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Measurement}.
	 * <p>
	 * Measurements details for Sensors, either generic or geolocation
	 * measurements are recorded
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Measurement">Measurement</a>
	 */
	public static final IRI Measurement;

	/**
	 * measurements
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#measurements}.
	 * <p>
	 * Reference to the Measurements recorded by the Sensor
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#measurements">measurements</a>
	 */
	public static final IRI measurements;

	/**
	 * measurementTimestamp
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#measurementTimestamp}.
	 * <p>
	 * Timestamp for the measurement
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#measurementTimestamp">measurementTimestamp</a>
	 */
	public static final IRI measurementTimestamp;

	/**
	 * measurementValue
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#measurementValue}.
	 * <p>
	 * Information about all non-Geolocation values of the measurement
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#measurementValue">measurementValue</a>
	 */
	public static final IRI measurementValue;

	/**
	 * methodName
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#methodName}.
	 * <p>
	 * Name of the CO2 calculation method
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#methodName">methodName</a>
	 */
	public static final IRI methodName;

	/**
	 * methodVersion
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#methodVersion}.
	 * <p>
	 * Version used for the calculation
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#methodVersion">methodVersion</a>
	 */
	public static final IRI methodVersion;

	/**
	 * middleName
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#middleName}.
	 * <p>
	 * Middle name/ other name
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#middleName">middleName</a>
	 */
	public static final IRI middleName;

	/**
	 * minimumQuantity
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#minimumQuantity}.
	 * <p>
	 * Minimum quantity
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#minimumQuantity">minimumQuantity</a>
	 */
	public static final IRI minimumQuantity;

	/**
	 * minTemperature
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#minTemperature}.
	 * <p>
	 * Minimum temperature of the range
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#minTemperature">minTemperature</a>
	 */
	public static final IRI minTemperature;

	/**
	 * modeCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#modeCode}.
	 * <p>
	 * Mode of transport code, refer to UNECE Rec. 19
	 * https://unece.org/fileadmin/DAM/cefact/recommendations/rec19/rec19_1cf19e.pdf
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#modeCode">modeCode</a>
	 */
	public static final IRI modeCode;

	/**
	 * ModeQualifier
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ModeQualifier}.
	 * <p>
	 * Open code list for transport modes
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ModeQualifier">ModeQualifier</a>
	 */
	public static final IRI ModeQualifier;

	/**
	 * modeQualifier
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#modeQualifier}.
	 * <p>
	 * Pre-Carriage, Main-Carriage or On-Carriage
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#modeQualifier">modeQualifier</a>
	 */
	public static final IRI modeQualifier;

	/**
	 * modularCheckNumber
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#modularCheckNumber}.
	 * <p>
	 * The check is a Modular 7 validation on the AWB number, recorded as a
	 * boolean.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#modularCheckNumber">modularCheckNumber</a>
	 */
	public static final IRI modularCheckNumber;

	/**
	 * movementMilestone
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#movementMilestone}.
	 * <p>
	 * The milestone list still needs to be defined, it includes elements
	 * from CXML Code List 1.92 but is not limited to those values, e.g.
	 * block-on and block-off times might be added as a comparison to wheels
	 * off and touchdown.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#movementMilestone">movementMilestone</a>
	 */
	public static final IRI movementMilestone;

	/**
	 * MovementTime
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#MovementTime}.
	 * <p>
	 * Times refering to Transport Movements, used to describe specfic times
	 * such as Actual Departure time, etc.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#MovementTime">MovementTime</a>
	 */
	public static final IRI MovementTime;

	/**
	 * movementTimes
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#movementTimes}.
	 * <p>
	 * Information about times related to the movement (milestone list to be
	 * defined)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#movementTimes">movementTimes</a>
	 */
	public static final IRI movementTimes;

	/**
	 * movementTimestamp
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#movementTimestamp}.
	 * <p>
	 * Timestamp (date and time) of the movement time. If the movement time
	 * is recorded asynchronously, the timestamp should reflect the actual
	 * time, not when the data was created.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#movementTimestamp">movementTimestamp</a>
	 */
	public static final IRI movementTimestamp;

	/**
	 * MovementTimeType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#MovementTimeType}.
	 * <p>
	 * Restricted code list for MovementTime subtypes
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#MovementTimeType">MovementTimeType</a>
	 */
	public static final IRI MovementTimeType;

	/**
	 * movementTimeType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#movementTimeType}.
	 * <p>
	 * The type of time can be Actual, Estimated ot Scheduled
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#movementTimeType">movementTimeType</a>
	 */
	public static final IRI movementTimeType;

	/**
	 * name
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#name}.
	 * <p>
	 * Human-understandable name of object depending on the context
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#name">name</a>
	 */
	public static final IRI name;

	/**
	 * nbCorrections
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#nbCorrections}.
	 * <p>
	 * Number of corrections to CASS records
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#nbCorrections">nbCorrections</a>
	 */
	public static final IRI nbCorrections;

	/**
	 * netWeightMeasure
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#netWeightMeasure}.
	 * <p>
	 * The total net weight of dangerous goods transported of this line item.
	 * For air transport the value must be the volume or mass in each
	 * package.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#netWeightMeasure">netWeightMeasure</a>
	 */
	public static final IRI netWeightMeasure;

	/**
	 * NONBOOKABLE
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#NONBOOKABLE}.
	 * <p>
	 * Used when a booking option is nonbookable
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#NONBOOKABLE">NONBOOKABLE</a>
	 */
	public static final IRI NONBOOKABLE;

	/**
	 * NonHumanActor
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#NonHumanActor}.
	 * <p>
	 * Non-human actors are actors which are not a person, such as robots
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#NonHumanActor">NonHumanActor</a>
	 */
	public static final IRI NonHumanActor;

	/**
	 * NOT_BOOKABLE
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#NOT_BOOKABLE}.
	 * <p>
	 * Used when a booking option proposal is not bookable
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#NOT_BOOKABLE">NOT_BOOKABLE</a>
	 */
	public static final IRI NOT_BOOKABLE;

	/**
	 * note
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#note}.
	 * <p>
	 * Free text for customs remarks, not used in OCI Composition Rules Table
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#note">note</a>
	 */
	public static final IRI note;

	/**
	 * numberOfDoors
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#numberOfDoors}.
	 * <p>
	 * Number of doors
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#numberOfDoors">numberOfDoors</a>
	 */
	public static final IRI numberOfDoors;

	/**
	 * numberOfFittings
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#numberOfFittings}.
	 * <p>
	 * Number of fittings
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#numberOfFittings">numberOfFittings</a>
	 */
	public static final IRI numberOfFittings;

	/**
	 * numberOfNets
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#numberOfNets}.
	 * <p>
	 * Number of nets
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#numberOfNets">numberOfNets</a>
	 */
	public static final IRI numberOfNets;

	/**
	 * numberOfStraps
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#numberOfStraps}.
	 * <p>
	 * Number of straps
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#numberOfStraps">numberOfStraps</a>
	 */
	public static final IRI numberOfStraps;

	/**
	 * numericalValue
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#numericalValue}.
	 * <p>
	 * Numerical value
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#numericalValue">numericalValue</a>
	 */
	public static final IRI numericalValue;

	/**
	 * nvdForCarriage
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#nvdForCarriage}.
	 * <p>
	 * When no value is declared for Carriage, this field may be completed
	 * with the value TRUE otherwise FALSE
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#nvdForCarriage">nvdForCarriage</a>
	 */
	public static final IRI nvdForCarriage;

	/**
	 * nvdForCustoms
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#nvdForCustoms}.
	 * <p>
	 * When no value is declared for Customs, this field may be completed
	 * with the value TRUE otherwise FALSE
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#nvdForCustoms">nvdForCustoms</a>
	 */
	public static final IRI nvdForCustoms;

	/**
	 * odlnCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#odlnCode}.
	 * <p>
	 * Contains two designator codes of ODLN or Operational Damage Limit
	 * Notices. ODLN code is used to define type of damage after visually
	 * check the serviceability of ULDs section 7, Standard Specifications
	 * 4/3 or 4/4 in ULD Regulations
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#odlnCode">odlnCode</a>
	 */
	public static final IRI odlnCode;

	/**
	 * offerValidFrom
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#offerValidFrom}.
	 * <p>
	 * Date and time of beginning of offer validity
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#offerValidFrom">offerValidFrom</a>
	 */
	public static final IRI offerValidFrom;

	/**
	 * offerValidTo
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#offerValidTo}.
	 * <p>
	 * Date and time of end of offer validity
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#offerValidTo">offerValidTo</a>
	 */
	public static final IRI offerValidTo;

	/**
	 * ofProduct
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ofProduct}.
	 * <p>
	 * Reference to the Product describing the Item
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ofProduct">ofProduct</a>
	 */
	public static final IRI ofProduct;

	/**
	 * ofShipment
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ofShipment}.
	 * <p>
	 * Reference to the Shipment the Piece is assigned to
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ofShipment">ofShipment</a>
	 */
	public static final IRI ofShipment;

	/**
	 * ON_CARRIAGE
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ON_CARRIAGE}.
	 * <p>
	 * Indicates the mode qualifier as on carriage
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ON_CARRIAGE">ON_CARRIAGE</a>
	 */
	public static final IRI ON_CARRIAGE;

	/**
	 * ON_REQUEST
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ON_REQUEST}.
	 * <p>
	 * Used when a booking option proposal is on request
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ON_REQUEST">ON_REQUEST</a>
	 */
	public static final IRI ON_REQUEST;

	/**
	 * onsiteActions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#onsiteActions}.
	 * <p>
	 * References to the Actions happening at the Location
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#onsiteActions">onsiteActions</a>
	 */
	public static final IRI onsiteActions;

	/**
	 * onTransportMeans
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#onTransportMeans}.
	 * <p>
	 * Reference to the TransportMeans that is being onloaded or offloaded
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#onTransportMeans">onTransportMeans</a>
	 */
	public static final IRI onTransportMeans;

	/**
	 * operatedTransportMovement
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#operatedTransportMovement}.
	 * <p>
	 * Transport Movement on which the Transport Means is used
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#operatedTransportMovement">operatedTransportMovement</a>
	 */
	public static final IRI operatedTransportMovement;

	/**
	 * operatingTransportMeans
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#operatingTransportMeans}.
	 * <p>
	 * Reference to the TransportMeans operating the TransportMovement
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#operatingTransportMeans">operatingTransportMeans</a>
	 */
	public static final IRI operatingTransportMeans;

	/**
	 * Organization
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Organization}.
	 * <p>
	 * Superclass: Organizations represent a kind of Agent corresponding to
	 * social instititutions such as companies, societies, etc
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Organization">Organization</a>
	 */
	public static final IRI Organization;

	/**
	 * originator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#originator}.
	 * <p>
	 * Document originator details and contacts
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#originator">originator</a>
	 */
	public static final IRI originator;

	/**
	 * originReferencePermitDateTime
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#originReferencePermitDateTime}.
	 * <p>
	 * Issuing date for Origin reference permit or re-export reference
	 * Certificate (box 12)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#originReferencePermitDateTime">originReferencePermitDateTime</a>
	 */
	public static final IRI originReferencePermitDateTime;

	/**
	 * originReferencePermitId
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#originReferencePermitId}.
	 * <p>
	 * identifier of Origin reference permit or re-export reference
	 * Certificate (box 12/12a)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#originReferencePermitId">originReferencePermitId</a>
	 */
	public static final IRI originReferencePermitId;

	/**
	 * originReferencePermitTypeCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#originReferencePermitTypeCode}.
	 * <p>
	 * Document type code of origin reference permit or re-export reference
	 * Certificate (box 12/12a)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#originReferencePermitTypeCode">originReferencePermitTypeCode</a>
	 */
	public static final IRI originReferencePermitTypeCode;

	/**
	 * originTradeCountry
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#originTradeCountry}.
	 * <p>
	 * country of origin (box 12). Refer ISO 3166-2
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#originTradeCountry">originTradeCountry</a>
	 */
	public static final IRI originTradeCountry;

	/**
	 * otherCharacteristics
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#otherCharacteristics}.
	 * <p>
	 * Charateristics of the product
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#otherCharacteristics">otherCharacteristics</a>
	 */
	public static final IRI otherCharacteristics;

	/**
	 * OtherCharge
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#OtherCharge}.
	 * <p>
	 * Other Charge details from AWB as per bullet point 19 - data element 23
	 * from AWB
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#OtherCharge">OtherCharge</a>
	 */
	public static final IRI OtherCharge;

	/**
	 * otherChargeAmount
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#otherChargeAmount}.
	 * <p>
	 * Other Charge amount
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#otherChargeAmount">otherChargeAmount</a>
	 */
	public static final IRI otherChargeAmount;

	/**
	 * otherChargeCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#otherChargeCode}.
	 * <p>
	 * Refer to CargoXML Code List 1.2 for Other Charges
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#otherChargeCode">otherChargeCode</a>
	 */
	public static final IRI otherChargeCode;

	/**
	 * otherCharges
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#otherCharges}.
	 * <p>
	 * Information about Other Charges applying to this Waybill
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#otherCharges">otherCharges</a>
	 */
	public static final IRI otherCharges;

	/**
	 * otherChargesIndicator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#otherChargesIndicator}.
	 * <p>
	 * Indicator whether the payment of Other Charges is to be made at origin
	 * (prepaid) or at destination (collect) as per bullet point 13 - data
	 * element 15a/15b from AWB
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#otherChargesIndicator">otherChargesIndicator</a>
	 */
	public static final IRI otherChargesIndicator;

	/**
	 * otherCustomsInformation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#otherCustomsInformation}.
	 * <p>
	 * Supplementary Customs, Security and Regulatory Control Information
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#otherCustomsInformation">otherCustomsInformation</a>
	 */
	public static final IRI otherCustomsInformation;

	/**
	 * OtherIdentifier
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#OtherIdentifier}.
	 * <p>
	 * Other identifiers
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#OtherIdentifier">OtherIdentifier</a>
	 */
	public static final IRI OtherIdentifier;

	/**
	 * otherIdentifiers
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#otherIdentifiers}.
	 * <p>
	 * Details about any other identifier, depending on the context of use
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#otherIdentifiers">otherIdentifiers</a>
	 */
	public static final IRI otherIdentifiers;

	/**
	 * otherIdentifierType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#otherIdentifierType}.
	 * <p>
	 * Identifier type or description
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#otherIdentifierType">otherIdentifierType</a>
	 */
	public static final IRI otherIdentifierType;

	/**
	 * otherRegulatedEntities
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#otherRegulatedEntities}.
	 * <p>
	 * Any other regulated entity that accepts custody of the cargo and
	 * accepts the security status originally issued
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#otherRegulatedEntities">otherRegulatedEntities</a>
	 */
	public static final IRI otherRegulatedEntities;

	/**
	 * otherScreeningMethods
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#otherScreeningMethods}.
	 * <p>
	 * Other methods used to secure the cargo
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#otherScreeningMethods">otherScreeningMethods</a>
	 */
	public static final IRI otherScreeningMethods;

	/**
	 * OUTBOUND
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#OUTBOUND}.
	 * <p>
	 * Indicates the described direction in a movement time as outbound
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#OUTBOUND">OUTBOUND</a>
	 */
	public static final IRI OUTBOUND;

	/**
	 * overpackCriticalitySafetyIndexNumeric
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#overpackCriticalitySafetyIndexNumeric}.
	 * <p>
	 * Applies to fissile material only, other than fissile excepted. A
	 * numeric value expressed to one decimal place preceded by the letters
	 * CSI. 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#overpackCriticalitySafetyIndexNumeric">overpackCriticalitySafetyIndexNumeric</a>
	 */
	public static final IRI overpackCriticalitySafetyIndexNumeric;

	/**
	 * overpackIndicator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#overpackIndicator}.
	 * <p>
	 * Overpack indicator 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#overpackIndicator">overpackIndicator</a>
	 */
	public static final IRI overpackIndicator;

	/**
	 * overpackT1
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#overpackT1}.
	 * <p>
	 * A single number assigned to a package, overpack or freight container
	 * to provide control over radiation exposure. 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#overpackT1">overpackT1</a>
	 */
	public static final IRI overpackT1;

	/**
	 * overpackTypeCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#overpackTypeCode}.
	 * <p>
	 * Identifies the Logistic Unit package type. UN Recommendation on
	 * Transport of Dangerous Goods, Model Regulations 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#overpackTypeCode">overpackTypeCode</a>
	 */
	public static final IRI overpackTypeCode;

	/**
	 * ownerCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ownerCode}.
	 * <p>
	 * Owner code of the ULD in aa, an or na format - owner can be an airline
	 * or leasing company
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ownerCode">ownerCode</a>
	 */
	public static final IRI ownerCode;

	/**
	 * owningOrganization
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#owningOrganization}.
	 * <p>
	 * Reference to the Organization for which the RegulatedEntity
	 * information is valid
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#owningOrganization">owningOrganization</a>
	 */
	public static final IRI owningOrganization;

	/**
	 * packagedeIdentifier
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#packagedeIdentifier}.
	 * <p>
	 * SSCC-18 code for the value of the package mark, company or bar code,
	 * free text, pallet code, etc.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#packagedeIdentifier">packagedeIdentifier</a>
	 */
	public static final IRI packagedeIdentifier;

	/**
	 * packageMarkCoded
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#packageMarkCoded}.
	 * <p>
	 * Reference identifying how the package is marked. Field is hardcode to
	 * "SSCC-18", "UPC" or "Other"
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#packageMarkCoded">packageMarkCoded</a>
	 */
	public static final IRI packageMarkCoded;

	/**
	 * packagingDangerLevelCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#packagingDangerLevelCode}.
	 * <p>
	 * Packing group, If used must reference I, II or III
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#packagingDangerLevelCode">packagingDangerLevelCode</a>
	 */
	public static final IRI packagingDangerLevelCode;

	/**
	 * packagingType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#packagingType}.
	 * <p>
	 * Packaging details 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#packagingType">packagingType</a>
	 */
	public static final IRI packagingType;

	/**
	 * PackagingType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#PackagingType}.
	 * <p>
	 * Packaging details 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#PackagingType">PackagingType</a>
	 */
	public static final IRI PackagingType;

	/**
	 * packingInstructionNumber
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#packingInstructionNumber}.
	 * <p>
	 * The packing instruction number applicable to the UN number / proper
	 * shipping name entry. A three-numeric value which may be preceded by
	 * the letter Y. Mandatory field for air transport (Air) 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#packingInstructionNumber">packingInstructionNumber</a>
	 */
	public static final IRI packingInstructionNumber;

	/**
	 * PALLET
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#PALLET}.
	 * <p>
	 * Indicates the load type as pallet
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#PALLET">PALLET</a>
	 */
	public static final IRI PALLET;

	/**
	 * parentOrganization
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#parentOrganization}.
	 * <p>
	 * Reference to the parent Organization
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#parentOrganization">parentOrganization</a>
	 */
	public static final IRI parentOrganization;

	/**
	 * partialEventIndicator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#partialEventIndicator}.
	 * <p>
	 * Boolean indicating that the LogisticsEvent is only applicable for
	 * parts of the LogisticObject it was recorded for, for example for some
	 * Pieces of a Shipment
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#partialEventIndicator">partialEventIndicator</a>
	 */
	public static final IRI partialEventIndicator;

	/**
	 * partOfIotDevice
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#partOfIotDevice}.
	 * <p>
	 * Reference to the IoT Device to which the sensor is linked
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#partOfIotDevice">partOfIotDevice</a>
	 */
	public static final IRI partOfIotDevice;

	/**
	 * Party
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Party}.
	 * <p>
	 * Refers to a Company and its role in a specific context, e.g Company A
	 * as shipper. Cargo-XML Code List 1.15 can be used as a reference with
	 * the addition of "Notify Party"
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Party">Party</a>
	 */
	public static final IRI Party;

	/**
	 * partyDetails
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#partyDetails}.
	 * <p>
	 * Reference to the Agent described by the role of the Party
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#partyDetails">partyDetails</a>
	 */
	public static final IRI partyDetails;

	/**
	 * partyRole
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#partyRole}.
	 * <p>
	 * Role fo the Company in the context. Can refer to Code List 1.36 in the
	 * CXML Toolkit
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#partyRole">partyRole</a>
	 */
	public static final IRI partyRole;

	/**
	 * passed
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#passed}.
	 * <p>
	 * Boolean indicating whether the Check was passed
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#passed">passed</a>
	 */
	public static final IRI passed;

	/**
	 * PENDING
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#PENDING}.
	 * <p>
	 * Used when a LogisticsActivity is pending
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#PENDING">PENDING</a>
	 */
	public static final IRI PENDING;

	/**
	 * perfomedAt
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#performedAt}.
	 * <p>
	 * Reference to the Location the Action was performed at
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#performedAt">performedAt</a>
	 */
	public static final IRI performedAt;

	/**
	 * permitTypeCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#permitTypeCode}.
	 * <p>
	 * Code specifying the document name. (box 1)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#permitTypeCode">permitTypeCode</a>
	 */
	public static final IRI permitTypeCode;

	/**
	 * permitTypeOtherDescription
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#permitTypeOtherDescription}.
	 * <p>
	 * Description if TypeCode is Other (box 1)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#permitTypeOtherDescription">permitTypeOtherDescription</a>
	 */
	public static final IRI permitTypeOtherDescription;

	/**
	 * Person
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Person}.
	 * <p>
	 * Person details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Person">Person</a>
	 */
	public static final IRI Person;

	/**
	 * PHONE_NUMBER
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#PHONE_NUMBER}.
	 * <p>
	 * Indicates a contact detail as phone number
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#PHONE_NUMBER">PHONE_NUMBER</a>
	 */
	public static final IRI PHONE_NUMBER;

	/**
	 * physicalChemicalForm
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#physicalChemicalForm}.
	 * <p>
	 * A description of the physical and chemical form of the material.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#physicalChemicalForm">physicalChemicalForm</a>
	 */
	public static final IRI physicalChemicalForm;

	/**
	 * PhysicalLogisticsObject
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#PhysicalLogisticsObject}.
	 * <p>
	 * Superclass: PhysicalLogisticObjects represent the digital twin of an
	 * object in the logistics suppy chain that physically exist
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#PhysicalLogisticsObject">PhysicalLogisticsObject</a>
	 */
	public static final IRI PhysicalLogisticsObject;

	/**
	 * Piece
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Piece}.
	 * <p>
	 * Individual piece or virtual grouping of pieces
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Piece">Piece</a>
	 */
	public static final IRI Piece;

	/**
	 * pieceCountForRate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#pieceCountForRate}.
	 * <p>
	 * Number of pieces for which the rate description details apply
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#pieceCountForRate">pieceCountForRate</a>
	 */
	public static final IRI pieceCountForRate;

	/**
	 * PieceDg
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#PieceDg}.
	 * <p>
	 * Dangerous Goods subtype of Piece
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#PieceDg">PieceDg</a>
	 */
	public static final IRI PieceDg;

	/**
	 * PieceGroup
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#PieceGroup}.
	 * <p>
	 * PieceGroup details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#PieceGroup">PieceGroup</a>
	 */
	public static final IRI PieceGroup;

	/**
	 * pieceGroupCount
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#pieceGroupCount}.
	 * <p>
	 * Number of pieces in the piece group
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#pieceGroupCount">pieceGroupCount</a>
	 */
	public static final IRI pieceGroupCount;

	/**
	 * pieceGroupGrossWeight
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#pieceGroupGrossWeight}.
	 * <p>
	 * Total gross weight of the piece group
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#pieceGroupGrossWeight">pieceGroupGrossWeight</a>
	 */
	public static final IRI pieceGroupGrossWeight;

	/**
	 * pieceGroupId
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#pieceGroupId}.
	 * <p>
	 * Identifier of the piece group, increasing integers
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#pieceGroupId">pieceGroupId</a>
	 */
	public static final IRI pieceGroupId;

	/**
	 * pieceGroups
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#pieceGroups}.
	 * <p>
	 * Reference to the Piece groups of the shipment
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#pieceGroups">pieceGroups</a>
	 */
	public static final IRI pieceGroups;

	/**
	 * pieceHeight
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#pieceHeight}.
	 * <p>
	 * Height of a single piece
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#pieceHeight">pieceHeight</a>
	 */
	public static final IRI pieceHeight;

	/**
	 * pieceLength
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#pieceLength}.
	 * <p>
	 * Length of a single piece
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#pieceLength">pieceLength</a>
	 */
	public static final IRI pieceLength;

	/**
	 * PieceLiveAnimals
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#PieceLiveAnimals}.
	 * <p>
	 * LiveAnimals subclass of Piece
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#PieceLiveAnimals">PieceLiveAnimals</a>
	 */
	public static final IRI PieceLiveAnimals;

	/**
	 * pieces
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#pieces}.
	 * <p>
	 * References to the Pieces that are part of this Shipment
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#pieces">pieces</a>
	 */
	public static final IRI pieces;

	/**
	 * pieceWeight
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#pieceWeight}.
	 * <p>
	 * Weight of a single piece
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#pieceWeight">pieceWeight</a>
	 */
	public static final IRI pieceWeight;

	/**
	 * pieceWidth
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#pieceWidth}.
	 * <p>
	 * Width of a single piece
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#pieceWidth">pieceWidth</a>
	 */
	public static final IRI pieceWidth;

	/**
	 * PLANNED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#PLANNED}.
	 * <p>
	 * Used when a time is planned
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#PLANNED">PLANNED</a>
	 */
	public static final IRI PLANNED;

	/**
	 * postalCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#postalCode}.
	 * <p>
	 * Postal / ZIP code
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#postalCode">postalCode</a>
	 */
	public static final IRI postalCode;

	/**
	 * postOfficeBox
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#postOfficeBox}.
	 * <p>
	 * Post Office box number / code
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#postOfficeBox">postOfficeBox</a>
	 */
	public static final IRI postOfficeBox;

	/**
	 * PRE_CARRIAGE
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#PRE_CARRIAGE}.
	 * <p>
	 * Indicates the mode qualifier as pre carriage
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#PRE_CARRIAGE">PRE_CARRIAGE</a>
	 */
	public static final IRI PRE_CARRIAGE;

	/**
	 * preferredTransportId
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#preferredTransportId}.
	 * <p>
	 * When part of the Request it refers to the preferred Transport ID from
	 * the customer. When part of the BookingOption (offer or actual booking)
	 * it refers to the expected Transport ID or flight
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#preferredTransportId">preferredTransportId</a>
	 */
	public static final IRI preferredTransportId;

	/**
	 * prefix
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#prefix}.
	 * <p>
	 * IATA three-numeric airline prefix number
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#prefix">prefix</a>
	 */
	public static final IRI prefix;

	/**
	 * PRESSURE
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#PRESSURE}.
	 * <p>
	 * Indicates the sensor type as pressure
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#PRESSURE">PRESSURE</a>
	 */
	public static final IRI PRESSURE;

	/**
	 * Price
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Price}.
	 * <p>
	 * Price associated to the offer/booking
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Price">Price</a>
	 */
	public static final IRI Price;

	/**
	 * price
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#price}.
	 * <p>
	 * Price of the Booking (if different from the offer)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#price">price</a>
	 */
	public static final IRI price;

	/**
	 * priceReferenceId
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#priceReferenceId}.
	 * <p>
	 * Reference to a price reference if existing (e.g. Allotment number,
	 * contract reference, etc.)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#priceReferenceId">priceReferenceId</a>
	 */
	public static final IRI priceReferenceId;

	/**
	 * priceSpecification
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#priceSpecification}.
	 * <p>
	 * Specification of the price e.g. Street, Group, Spot, etc.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#priceSpecification">priceSpecification</a>
	 */
	public static final IRI priceSpecification;

	/**
	 * Product
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Product}.
	 * <p>
	 * Product details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Product">Product</a>
	 */
	public static final IRI Product;

	/**
	 * productCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#productCode}.
	 * <p>
	 * Carrier's product code
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#productCode">productCode</a>
	 */
	public static final IRI productCode;

	/**
	 * productDescription
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#productDescription}.
	 * <p>
	 * Carrier's product description
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#productDescription">productDescription</a>
	 */
	public static final IRI productDescription;

	/**
	 * ProductDg
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ProductDg}.
	 * <p>
	 * Dangerous Goods subtype of Product
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ProductDg">ProductDg</a>
	 */
	public static final IRI ProductDg;

	/**
	 * productionCountry
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#productionCountry}.
	 * <p>
	 * Production country details. Refer ISO 3166-2
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#productionCountry">productionCountry</a>
	 */
	public static final IRI productionCountry;

	/**
	 * productionCountryForRate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#productionCountryForRate}.
	 * <p>
	 * Production country for the rate described by this Line Item. Refer ISO
	 * 3166-2
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#productionCountryForRate">productionCountryForRate</a>
	 */
	public static final IRI productionCountryForRate;

	/**
	 * productionDate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#productionDate}.
	 * <p>
	 * Production date
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#productionDate">productionDate</a>
	 */
	public static final IRI productionDate;

	/**
	 * properShippingName
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#properShippingName}.
	 * <p>
	 * The name used to describe the particular article or substance as shown
	 * in the UN Model Regulations Dangerous Goods List
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#properShippingName">properShippingName</a>
	 */
	public static final IRI properShippingName;

	/**
	 * PublicAuthority
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#PublicAuthority}.
	 * <p>
	 * PublicAuthorities are Organizations of the state on public interests,
	 * such as customs
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#PublicAuthority">PublicAuthority</a>
	 */
	public static final IRI PublicAuthority;

	/**
	 * quantity
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#quantity}.
	 * <p>
	 * Quantity for the charge if applicable
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#quantity">quantity</a>
	 */
	public static final IRI quantity;

	/**
	 * quantityAnimals
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#quantityAnimals}.
	 * <p>
	 * Quantity including units (box 11)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#quantityAnimals">quantityAnimals</a>
	 */
	public static final IRI quantityAnimals;

	/**
	 * quantityForUnitPrice
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#quantityForUnitPrice}.
	 * <p>
	 * Product quantity for unit price - e.g. 12 (eggs for one USD 1)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#quantityForUnitPrice">quantityForUnitPrice</a>
	 */
	public static final IRI quantityForUnitPrice;

	/**
	 * question
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#question}.
	 * <p>
	 * Reference to the Question the Answer is for
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#question">question</a>
	 */
	public static final IRI question;

	/**
	 * Question
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Question}.
	 * <p>
	 * Question holds one question and a link to an Answer The Question is
	 * provided by the party in charge of the template used
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Question">Question</a>
	 */
	public static final IRI Question;

	/**
	 * questionNumber
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#questionNumber}.
	 * <p>
	 * Number of the Question within the template (alphanumeric)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#questionNumber">questionNumber</a>
	 */
	public static final IRI questionNumber;

	/**
	 * questions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#questions}.
	 * <p>
	 * References to all Questions that are part of this template
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#questions">questions</a>
	 */
	public static final IRI questions;

	/**
	 * questionSection
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#questionSection}.
	 * <p>
	 * Section of the CheckTemplate this Question is part of
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#questionSection">questionSection</a>
	 */
	public static final IRI questionSection;

	/**
	 * QUEUED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#QUEUED}.
	 * <p>
	 * Used when a booking or booking option is queued or pending
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#QUEUED">QUEUED</a>
	 */
	public static final IRI QUEUED;

	/**
	 * qValueNumeric
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#qValueNumeric}.
	 * <p>
	 * Most instances of all packed in one will require the addition of the Q
	 * value which 1. Applies to air transport only. (Air) 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#qValueNumeric">qValueNumeric</a>
	 */
	public static final IRI qValueNumeric;

	/**
	 * ranges
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ranges}.
	 * <p>
	 * Reference to the ranges
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ranges">ranges</a>
	 */
	public static final IRI ranges;

	/**
	 * Ranges
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Ranges}.
	 * <p>
	 * Ranges details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Ranges">Ranges</a>
	 */
	public static final IRI Ranges;

	/**
	 * rateCharge
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#rateCharge}.
	 * <p>
	 * TACT Rate for rate description details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#rateCharge">rateCharge</a>
	 */
	public static final IRI rateCharge;

	/**
	 * rateClassCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#rateClassCode}.
	 * <p>
	 * Rate class code e.g. Q. Refer to CXML Code List 1.4 Rate Class Codes
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#rateClassCode">rateClassCode</a>
	 */
	public static final IRI rateClassCode;

	/**
	 * rateClassCodeBasic
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#rateClassCodeBasic}.
	 * <p>
	 * Rate Surcharge/Reduction - Basic Rate Class Code
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#rateClassCodeBasic">rateClassCodeBasic</a>
	 */
	public static final IRI rateClassCodeBasic;

	/**
	 * ratePercentage
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ratePercentage}.
	 * <p>
	 * Rate Surcharge/Reduction - Percebtage of red. / surcharge
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ratePercentage">ratePercentage</a>
	 */
	public static final IRI ratePercentage;

	/**
	 * ratings
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ratings}.
	 * <p>
	 * Rating used for pricing
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ratings">ratings</a>
	 */
	public static final IRI ratings;

	/**
	 * Ratings
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Ratings}.
	 * <p>
	 * Ratings details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Ratings">Ratings</a>
	 */
	public static final IRI Ratings;

	/**
	 * rcp
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#rcp}.
	 * <p>
	 * IATA 3-letter city code of the rate combination point as defined in
	 * TACT
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#rcp">rcp</a>
	 */
	public static final IRI rcp;

	/**
	 * reasonsForAdjustments
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#reasonsForAdjustments}.
	 * <p>
	 * A free text for user to include a reason for correction
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#reasonsForAdjustments">reasonsForAdjustments</a>
	 */
	public static final IRI reasonsForAdjustments;

	/**
	 * receivedFrom
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#receivedFrom}.
	 * <p>
	 * Regulated entity that tendered the consignment
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#receivedFrom">receivedFrom</a>
	 */
	public static final IRI receivedFrom;

	/**
	 * recordedGeolocation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#recordedGeolocation}.
	 * <p>
	 * Reference to the Geolocation recorded of the measurement
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#recordedGeolocation">recordedGeolocation</a>
	 */
	public static final IRI recordedGeolocation;

	/**
	 * recordingActor
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#recordingActor}.
	 * <p>
	 * Reference to the Actor recording the LogisticsEvent
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#recordingActor">recordingActor</a>
	 */
	public static final IRI recordingActor;

	/**
	 * recordingOrganization
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#recordingOrganization}.
	 * <p>
	 * Organization recording the LogisticsEvent
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#recordingOrganization">recordingOrganization</a>
	 */
	public static final IRI recordingOrganization;

	/**
	 * referenceForObjects
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#referenceForObjects}.
	 * <p>
	 * References to the LogisticsObjects referring to this external
	 * reference
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#referenceForObjects">referenceForObjects</a>
	 */
	public static final IRI referenceForObjects;

	/**
	 * referredBookingOption
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#referredBookingOption}.
	 * <p>
	 * Refers to the Booking
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#referredBookingOption">referredBookingOption</a>
	 */
	public static final IRI referredBookingOption;

	/**
	 * regionCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#regionCode}.
	 * <p>
	 * Region/ State / Department. Refer ISO 3166-2
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#regionCode">regionCode</a>
	 */
	public static final IRI regionCode;

	/**
	 * RegulatedEntity
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#RegulatedEntity}.
	 * <p>
	 * Regulated Entity
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#RegulatedEntity">RegulatedEntity</a>
	 */
	public static final IRI RegulatedEntity;

	/**
	 * regulatedEntityAcceptor
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#regulatedEntityAcceptor}.
	 * <p>
	 * Information about the accepting regulated entity of the Security
	 * Declaration
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#regulatedEntityAcceptor">regulatedEntityAcceptor</a>
	 */
	public static final IRI regulatedEntityAcceptor;

	/**
	 * regulatedEntityCategory
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#regulatedEntityCategory}.
	 * <p>
	 * Category code of the Regulated Entity
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#regulatedEntityCategory">regulatedEntityCategory</a>
	 */
	public static final IRI regulatedEntityCategory;

	/**
	 * regulatedEntityExpiryDate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#regulatedEntityExpiryDate}.
	 * <p>
	 * Expiry date 4 digits month/year
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#regulatedEntityExpiryDate">regulatedEntityExpiryDate</a>
	 */
	public static final IRI regulatedEntityExpiryDate;

	/**
	 * regulatedEntityIdentifier
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#regulatedEntityIdentifier}.
	 * <p>
	 * Regulated entity identifier as per IATA e-CSD/CSD Resolution 65
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#regulatedEntityIdentifier">regulatedEntityIdentifier</a>
	 */
	public static final IRI regulatedEntityIdentifier;

	/**
	 * regulatedEntityIssuer
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#regulatedEntityIssuer}.
	 * <p>
	 * Regulated entity issuing the Security Declaration
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#regulatedEntityIssuer">regulatedEntityIssuer</a>
	 */
	public static final IRI regulatedEntityIssuer;

	/**
	 * REJECTED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#REJECTED}.
	 * <p>
	 * Used when a booking is rejected
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#REJECTED">REJECTED</a>
	 */
	public static final IRI REJECTED;

	/**
	 * remarks
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#remarks}.
	 * <p>
	 * Remarks or Supplement Information
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#remarks">remarks</a>
	 */
	public static final IRI remarks;

	/**
	 * remarksText
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#remarksText}.
	 * <p>
	 * Details of the remarks, mandatory
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#remarksText">remarksText</a>
	 */
	public static final IRI remarksText;

	/**
	 * reportableQuantity
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#reportableQuantity}.
	 * <p>
	 * Reportable quantities, To and from the USA only
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#reportableQuantity">reportableQuantity</a>
	 */
	public static final IRI reportableQuantity;

	/**
	 * REQUESTED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#REQUESTED}.
	 * <p>
	 * Used when a time is requested
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#REQUESTED">REQUESTED</a>
	 */
	public static final IRI REQUESTED;

	/**
	 * requestMatch
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#requestMatch}.
	 * <p>
	 * Indicates if the Booking Option is a match to the Booking Option
	 * Request preferences
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#requestMatch">requestMatch</a>
	 */
	public static final IRI requestMatch;

	/**
	 * resultOfCheck
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#resultOfCheck}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#resultOfCheck">resultOfCheck</a>
	 */
	public static final IRI resultOfCheck;

	/**
	 * resultValue
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#resultValue}.
	 * <p>
	 * Information about a result Value of any kind of the Check
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#resultValue">resultValue</a>
	 */
	public static final IRI resultValue;

	/**
	 * salutation
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#salutation}.
	 * <p>
	 * Salutation 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#salutation">salutation</a>
	 */
	public static final IRI salutation;

	/**
	 * SCHEDULED
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#SCHEDULED}.
	 * <p>
	 * Used when a time is scheduled
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#SCHEDULED">SCHEDULED</a>
	 */
	public static final IRI SCHEDULED;

	/**
	 * screeningMethods
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#screeningMethods}.
	 * <p>
	 * Screening methods which have been used to secure the cargo PHS –
	 * Physical Inspection and/or hand search VCK - Visual check XRY- X-ray
	 * equipment EDS - Explosive detection system EDD - Explosive detection
	 * dogs ETD - Explosive trace detection equipment - particles or vapor
	 * CMD - Cargo metal detection AOM - Subjected to any other means: this
	 * entry should be followed by free text specifying what other mean was
	 * used to secure the cargo
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#screeningMethods">screeningMethods</a>
	 */
	public static final IRI screeningMethods;

	/**
	 * seal
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#seal}.
	 * <p>
	 * Seal identifier
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#seal">seal</a>
	 */
	public static final IRI seal;

	/**
	 * sealNumber
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#sealNumber}.
	 * <p>
	 * ULD seal number if applicable
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#sealNumber">sealNumber</a>
	 */
	public static final IRI sealNumber;

	/**
	 * SecurityDeclaration
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#SecurityDeclaration}.
	 * <p>
	 * Security declaration details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#SecurityDeclaration">SecurityDeclaration</a>
	 */
	public static final IRI SecurityDeclaration;

	/**
	 * securityDeclarations
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#securityDeclarations}.
	 * <p>
	 * Security details of the piece
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#securityDeclarations">securityDeclarations</a>
	 */
	public static final IRI securityDeclarations;

	/**
	 * securityStampId
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#securityStampId}.
	 * <p>
	 * Security Stamp ID
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#securityStampId">securityStampId</a>
	 */
	public static final IRI securityStampId;

	/**
	 * securityStatus
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#securityStatus}.
	 * <p>
	 * Security status indicator (CXML 1.13) - e.g. SPX- Cargo Secure for
	 * Passenger and All-Cargo Aircraft 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#securityStatus">securityStatus</a>
	 */
	public static final IRI securityStatus;

	/**
	 * Sensor
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Sensor}.
	 * <p>
	 * Sensor details and measurements, linked to Connected Devices
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Sensor">Sensor</a>
	 */
	public static final IRI Sensor;

	/**
	 * sensorType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#sensorType}.
	 * <p>
	 * Type of sensor as described in Interactive Cargo Recommended Practice
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#sensorType">sensorType</a>
	 */
	public static final IRI sensorType;

	/**
	 * SensorType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#SensorType}.
	 * <p>
	 * Open code list for sensor types
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#SensorType">SensorType</a>
	 */
	public static final IRI SensorType;

	/**
	 * sequenceNumber
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#sequenceNumber}.
	 * <p>
	 * Short text to detail sequence number (alphanumeric)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#sequenceNumber">sequenceNumber</a>
	 */
	public static final IRI sequenceNumber;

	/**
	 * serialNumber
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#serialNumber}.
	 * <p>
	 * Serial number that allows to uniquely identify the object
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#serialNumber">serialNumber</a>
	 */
	public static final IRI serialNumber;

	/**
	 * servedActivity
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#servedActivity}.
	 * <p>
	 * Reference to the Activity the Action was performed for
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#servedActivity">servedActivity</a>
	 */
	public static final IRI servedActivity;

	/**
	 * servedServices
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#servedServices}.
	 * <p>
	 * Reference to Services this Activity is executed for
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#servedServices">servedServices</a>
	 */
	public static final IRI servedServices;

	/**
	 * serviceabilityCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#serviceabilityCode}.
	 * <p>
	 * Designator of serviceablity condition e.g. SER or DAM 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#serviceabilityCode">serviceabilityCode</a>
	 */
	public static final IRI serviceabilityCode;

	/**
	 * serviceCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#serviceCode}.
	 * <p>
	 * One letter service code as per bullet point 18.4 - data element 22Z
	 * from AWB
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#serviceCode">serviceCode</a>
	 */
	public static final IRI serviceCode;

	/**
	 * serviceLevelCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#serviceLevelCode}.
	 * <p>
	 * Service level code
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#serviceLevelCode">serviceLevelCode</a>
	 */
	public static final IRI serviceLevelCode;

	/**
	 * shipment
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#shipment}.
	 * <p>
	 * Reference to the Shipment
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#shipment">shipment</a>
	 */
	public static final IRI shipment;

	/**
	 * Shipment
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Shipment}.
	 * <p>
	 * Shipment details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Shipment">Shipment</a>
	 */
	public static final IRI Shipment;

	/**
	 * shipperDeclarationText
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#shipperDeclarationText}.
	 * <p>
	 * Contains the shipper's declaration to comply with the regulations text
	 * note. Free text . This field is mandatory for air (Air)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#shipperDeclarationText">shipperDeclarationText</a>
	 */
	public static final IRI shipperDeclarationText;

	/**
	 * shippingInfo
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#shippingInfo}.
	 * <p>
	 * The shipper or its Agent may enter the appropriate optional shipping
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#shippingInfo">shippingInfo</a>
	 */
	public static final IRI shippingInfo;

	/**
	 * shippingMarks
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#shippingMarks}.
	 * <p>
	 * Shipping marks
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#shippingMarks">shippingMarks</a>
	 */
	public static final IRI shippingMarks;

	/**
	 * shippingRefNo
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#shippingRefNo}.
	 * <p>
	 * Optional shipping reference number if any
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#shippingRefNo">shippingRefNo</a>
	 */
	public static final IRI shippingRefNo;

	/**
	 * shortName
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#shortName}.
	 * <p>
	 * Short name of the Organization if any
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#shortName">shortName</a>
	 */
	public static final IRI shortName;

	/**
	 * shortText
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#shortText}.
	 * <p>
	 * Short text of the Question
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#shortText">shortText</a>
	 */
	public static final IRI shortText;

	/**
	 * signatoryCompany
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#signatoryCompany}.
	 * <p>
	 * Signatory company name
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#signatoryCompany">signatoryCompany</a>
	 */
	public static final IRI signatoryCompany;

	/**
	 * signatoryRole
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#signatoryRole}.
	 * <p>
	 * Role of the signatory with regards to the ePermit: Applicant, Permit
	 * issuer, Issuing Authority or Examining authority
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#signatoryRole">signatoryRole</a>
	 */
	public static final IRI signatoryRole;

	/**
	 * signatureDate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#signatureDate}.
	 * <p>
	 * Date and time of the signature
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#signatureDate">signatureDate</a>
	 */
	public static final IRI signatureDate;

	/**
	 * signatures
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#signatures}.
	 * <p>
	 * List of all the signatures of the Epermit (applicant box 4, issuing
	 * authority box 6, issuer box 13 and examining authority box 14)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#signatures">signatures</a>
	 */
	public static final IRI signatures;

	/**
	 * signatureStatement
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#signatureStatement}.
	 * <p>
	 * Signatory signature authentication text
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#signatureStatement">signatureStatement</a>
	 */
	public static final IRI signatureStatement;

	/**
	 * signatureTypeCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#signatureTypeCode}.
	 * <p>
	 * Code specifying a type of government action such as inspection,
	 * detention, fumigation, security.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#signatureTypeCode">signatureTypeCode</a>
	 */
	public static final IRI signatureTypeCode;

	/**
	 * skeletonIndicator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#skeletonIndicator}.
	 * <p>
	 * Indicator whether a logistics object is a skeleton object
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#skeletonIndicator">skeletonIndicator</a>
	 */
	public static final IRI skeletonIndicator;

	/**
	 * slac
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#slac}.
	 * <p>
	 * Shipper's Load And Count ( total contained piece count as provided by
	 * shipper)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#slac">slac</a>
	 */
	public static final IRI slac;

	/**
	 * slacForRate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#slacForRate}.
	 * <p>
	 * Slac used for the rate described by the Line item
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#slacForRate">slacForRate</a>
	 */
	public static final IRI slacForRate;

	/**
	 * specialConditions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#specialConditions}.
	 * <p>
	 * Special conditions (box 5)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#specialConditions">specialConditions</a>
	 */
	public static final IRI specialConditions;

	/**
	 * specialFormIndicator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#specialFormIndicator}.
	 * <p>
	 * A notation that the material is special form
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#specialFormIndicator">specialFormIndicator</a>
	 */
	public static final IRI specialFormIndicator;

	/**
	 * specialHandlingCodes
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#specialHandlingCodes}.
	 * <p>
	 * Three-letter special handling code (SPH)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#specialHandlingCodes">specialHandlingCodes</a>
	 */
	public static final IRI specialHandlingCodes;

	/**
	 * specialProvisionId
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#specialProvisionId}.
	 * <p>
	 * For Air Mode: Special Provision may show a single, double or triple
	 * digit number preceded by the letter A, against appropriate entries in
	 * the List of Dangerous Goods
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#specialProvisionId">specialProvisionId</a>
	 */
	public static final IRI specialProvisionId;

	/**
	 * specialServiceRequests
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#specialServiceRequests}.
	 * <p>
	 * Special service requests
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#specialServiceRequests">specialServiceRequests</a>
	 */
	public static final IRI specialServiceRequests;

	/**
	 * speciesCommonName
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#speciesCommonName}.
	 * <p>
	 * Species common name (box 8)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#speciesCommonName">speciesCommonName</a>
	 */
	public static final IRI speciesCommonName;

	/**
	 * speciesScientificName
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#speciesScientificName}.
	 * <p>
	 * Species scientific name (box 7)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#speciesScientificName">speciesScientificName</a>
	 */
	public static final IRI speciesScientificName;

	/**
	 * specimenDescription
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#specimenDescription}.
	 * <p>
	 * Description of specimens, including age and sex if LA (box 9)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#specimenDescription">specimenDescription</a>
	 */
	public static final IRI specimenDescription;

	/**
	 * specimenTypeCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#specimenTypeCode}.
	 * <p>
	 * Description of specimens, CITES type code (box 9)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#specimenTypeCode">specimenTypeCode</a>
	 */
	public static final IRI specimenTypeCode;

	/**
	 * stackable
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#stackable}.
	 * <p>
	 * Stackable indicator for the pieces (boolean)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#stackable">stackable</a>
	 */
	public static final IRI stackable;

	/**
	 * station
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#station}.
	 * <p>
	 * Reference to the station (Airport), mandatory 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#station">station</a>
	 */
	public static final IRI station;

	/**
	 * StationRemarks
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#StationRemarks}.
	 * <p>
	 * StationRemarks details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#StationRemarks">StationRemarks</a>
	 */
	public static final IRI StationRemarks;

	/**
	 * stationRemarks
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#stationRemarks}.
	 * <p>
	 * Remarks related to specific stations in the routing (e.g. Embargo in
	 * XXX)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#stationRemarks">stationRemarks</a>
	 */
	public static final IRI stationRemarks;

	/**
	 * statusBookingOption
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#statusBookingOption}.
	 * <p>
	 * Status of the Booking Option
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#statusBookingOption">statusBookingOption</a>
	 */
	public static final IRI statusBookingOption;

	/**
	 * Storage
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Storage}.
	 * <p>
	 * Activity to describe storing processes
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Storage">Storage</a>
	 */
	public static final IRI Storage;

	/**
	 * storagePlaceIdentifier
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#storagePlaceIdentifier}.
	 * <p>
	 * Short text stating the exact place of storage
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#storagePlaceIdentifier">storagePlaceIdentifier</a>
	 */
	public static final IRI storagePlaceIdentifier;

	/**
	 * STORE_IN
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#STORE_IN}.
	 * <p>
	 * Describes a store-in process, where a physical object is assigned to a
	 * specific location
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#STORE_IN">STORE_IN</a>
	 */
	public static final IRI STORE_IN;

	/**
	 * STORE_OUT
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#STORE_OUT}.
	 * <p>
	 * Describes a store-out process, where a physical object leaves a
	 * specific location
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#STORE_OUT">STORE_OUT</a>
	 */
	public static final IRI STORE_OUT;

	/**
	 * storedObjects
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#storedObjects}.
	 * <p>
	 * Reference to the Objects being stored in or stored out
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#storedObjects">storedObjects</a>
	 */
	public static final IRI storedObjects;

	/**
	 * Storing
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Storing}.
	 * <p>
	 * Action to describe store-in or store-out
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Storing">Storing</a>
	 */
	public static final IRI Storing;

	/**
	 * storingActions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#storingActions}.
	 * <p>
	 * References to all StoringActions performed for the Storing Activity
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#storingActions">storingActions</a>
	 */
	public static final IRI storingActions;

	/**
	 * storingIdentifier
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#storingIdentifier}.
	 * <p>
	 * Short text holding the process number if necessary
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#storingIdentifier">storingIdentifier</a>
	 */
	public static final IRI storingIdentifier;

	/**
	 * storingType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#storingType}.
	 * <p>
	 * Enum stating whether the StoringAction describes the store-in or the
	 * store-out
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#storingType">storingType</a>
	 */
	public static final IRI storingType;

	/**
	 * StoringType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#StoringType}.
	 * <p>
	 * Restricted code list for Storing subtypes
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#StoringType">StoringType</a>
	 */
	public static final IRI StoringType;

	/**
	 * streetAddressLines
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#streetAddressLines}.
	 * <p>
	 * Street address including street name, street number, building number,
	 * apartment etc
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#streetAddressLines">streetAddressLines</a>
	 */
	public static final IRI streetAddressLines;

	/**
	 * subjectCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#subjectCode}.
	 * <p>
	 * Information Identifier. Code identifying a piece of information/entity
	 * e.g. "IMP" for import, "EXP" for export, "AGT" for Agent, "ISS" for
	 * The Regulated Agent Issuing the Security Status for a Consignment etc.
	 * Condition: At least one of the three elements (Country Code,
	 * Information Identifier or Customs, Security and Regulatory Control
	 * Information Identifier) must be completed
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#subjectCode">subjectCode</a>
	 */
	public static final IRI subjectCode;

	/**
	 * subLocationOf
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#subLocationOf}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#subLocationOf">subLocationOf</a>
	 */
	public static final IRI subLocationOf;

	/**
	 * subLocations
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#subLocations}.
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#subLocations">subLocations</a>
	 */
	public static final IRI subLocations;

	/**
	 * subOrganization
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#subOrganization}.
	 * <p>
	 * References to all sub-Organizations
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#subOrganization">subOrganization</a>
	 */
	public static final IRI subOrganization;

	/**
	 * subTotal
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#subTotal}.
	 * <p>
	 * Subtotal of the charge
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#subTotal">subTotal</a>
	 */
	public static final IRI subTotal;

	/**
	 * supplementaryInfoPrefix
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#supplementaryInfoPrefix}.
	 * <p>
	 * Additional information that may be added in addition to the proper
	 * shipping name to more fully describe the goods or to identify a
	 * particular condition
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#supplementaryInfoPrefix">supplementaryInfoPrefix</a>
	 */
	public static final IRI supplementaryInfoPrefix;

	/**
	 * supplementaryInfoSuffix
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#supplementaryInfoSuffix}.
	 * <p>
	 * Additional information that may be added in addition to the proper
	 * shipping to more fully describe the goods or to identify a particular
	 * condition
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#supplementaryInfoSuffix">supplementaryInfoSuffix</a>
	 */
	public static final IRI supplementaryInfoSuffix;

	/**
	 * tareWeight
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#tareWeight}.
	 * <p>
	 * Tare weight of the empty ULD
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#tareWeight">tareWeight</a>
	 */
	public static final IRI tareWeight;

	/**
	 * targetCountry
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#targetCountry}.
	 * <p>
	 * Item target country. Refer ISO 3166-2
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#targetCountry">targetCountry</a>
	 */
	public static final IRI targetCountry;

	/**
	 * taxDueAgent
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#taxDueAgent}.
	 * <p>
	 * Tax due Agent (VAT/GST on Commission). Total VAT/TAX amount payable by
	 * airline to agent
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#taxDueAgent">taxDueAgent</a>
	 */
	public static final IRI taxDueAgent;

	/**
	 * taxDueAirline
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#taxDueAirline}.
	 * <p>
	 * Tax due Airline (as per AWB, or VAT/GST as per invoice). Total VAT/TAX
	 * amount payable by agent to airline
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#taxDueAirline">taxDueAirline</a>
	 */
	public static final IRI taxDueAirline;

	/**
	 * technicalName
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#technicalName}.
	 * <p>
	 * This is additional chemical name(s) required for some proper shipping
	 * names. When added the technical must be shown in parentheses
	 * immediately following the proper shipping name. 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#technicalName">technicalName</a>
	 */
	public static final IRI technicalName;

	/**
	 * TELEX
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#TELEX}.
	 * <p>
	 * Indicates a contact detail as telex
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#TELEX">TELEX</a>
	 */
	public static final IRI TELEX;

	/**
	 * TemperatureInstructions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#TemperatureInstructions}.
	 * <p>
	 * TemperatureInstructions details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#TemperatureInstructions">TemperatureInstructions</a>
	 */
	public static final IRI TemperatureInstructions;

	/**
	 * temperatureInstructions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#temperatureInstructions}.
	 * <p>
	 * Temperature instructions if a specific range
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#temperatureInstructions">temperatureInstructions</a>
	 */
	public static final IRI temperatureInstructions;

	/**
	 * temperatureUnit
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#temperatureUnit}.
	 * <p>
	 * Preferred unit for temperature
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#temperatureUnit">temperatureUnit</a>
	 */
	public static final IRI temperatureUnit;

	/**
	 * templatePurpose
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#templatePurpose}.
	 * <p>
	 * Purpose of the template
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#templatePurpose">templatePurpose</a>
	 */
	public static final IRI templatePurpose;

	/**
	 * text
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#text}.
	 * <p>
	 * Text for the Answer
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#text">text</a>
	 */
	public static final IRI text;

	/**
	 * textualHandlingInstructions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#textualHandlingInstructions}.
	 * <p>
	 * Strings to provide free text handling instructions such as SSR and OSI
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#textualHandlingInstructions">textualHandlingInstructions</a>
	 */
	public static final IRI textualHandlingInstructions;

	/**
	 * textualValue
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#textualValue}.
	 * <p>
	 * Textual value filled on use context (eg. characteristic colour,
	 * contactDetail mail adress, etc.)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#textualValue">textualValue</a>
	 */
	public static final IRI textualValue;

	/**
	 * THERMOMETER
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#THERMOMETER}.
	 * <p>
	 * Indicates the sensor type as thermometer
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#THERMOMETER">THERMOMETER</a>
	 */
	public static final IRI THERMOMETER;

	/**
	 * TILT
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#TILT}.
	 * <p>
	 * Indicates the sensor type as tilt
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#TILT">TILT</a>
	 */
	public static final IRI TILT;

	/**
	 * timeOfAvailability
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#timeOfAvailability}.
	 * <p>
	 * Time of availability of the shipment as per CargoIQ definition
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#timeOfAvailability">timeOfAvailability</a>
	 */
	public static final IRI timeOfAvailability;

	/**
	 * timePreferences
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#timePreferences}.
	 * <p>
	 * Schedule preferences of the request
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#timePreferences">timePreferences</a>
	 */
	public static final IRI timePreferences;

	/**
	 * totalDimensions
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#totalDimensions}.
	 * <p>
	 * Dimensions of the whole shipment
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#totalDimensions">totalDimensions</a>
	 */
	public static final IRI totalDimensions;

	/**
	 * totalGrossWeight
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#totalGrossWeight}.
	 * <p>
	 * Total gross weight of the whole shipment
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#totalGrossWeight">totalGrossWeight</a>
	 */
	public static final IRI totalGrossWeight;

	/**
	 * totalTransitTime
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#totalTransitTime}.
	 * <p>
	 * Total transit time as per CargoIQ definition, expressed as a duration
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#totalTransitTime">totalTransitTime</a>
	 */
	public static final IRI totalTransitTime;

	/**
	 * totalVolume
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#totalVolume}.
	 * <p>
	 * Total volume fo the volume piece group
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#totalVolume">totalVolume</a>
	 */
	public static final IRI totalVolume;

	/**
	 * totalVolumetricWeight
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#totalVolumetricWeight}.
	 * <p>
	 * Volumetric weight of the whole shipment
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#totalVolumetricWeight">totalVolumetricWeight</a>
	 */
	public static final IRI totalVolumetricWeight;

	/**
	 * transactionPurpose
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#transactionPurpose}.
	 * <p>
	 * Purpose of the transaction in free text (box 5a)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#transactionPurpose">transactionPurpose</a>
	 */
	public static final IRI transactionPurpose;

	/**
	 * transactionPurposeCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#transactionPurposeCode}.
	 * <p>
	 * Code indicating the purpose of the transaction (box 5a)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#transactionPurposeCode">transactionPurposeCode</a>
	 */
	public static final IRI transactionPurposeCode;

	/**
	 * transportContractId
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#transportContractId}.
	 * <p>
	 * Reference to the Air Waybill or other transport contract document (box
	 * 15)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#transportContractId">transportContractId</a>
	 */
	public static final IRI transportContractId;

	/**
	 * transportContractTypeCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#transportContractTypeCode}.
	 * <p>
	 * Code specifying the transport document name (box 15)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#transportContractTypeCode">transportContractTypeCode</a>
	 */
	public static final IRI transportContractTypeCode;

	/**
	 * transportIdentifier
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#transportIdentifier}.
	 * <p>
	 * Airline flight number, or rail/truck/maritime line id
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#transportIdentifier">transportIdentifier</a>
	 */
	public static final IRI transportIdentifier;

	/**
	 * transportIndexNumeric
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#transportIndexNumeric}.
	 * <p>
	 * Radioactive Transport-Index value of the package or all packed in one.
	 * Conditionally mandator and applies to categories II-Yellow and
	 * III-Yellow only; field only contains the value, if printed, TI must be
	 * added as a prefix to the value to be printed in the Packing
	 * Instructions column
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#transportIndexNumeric">transportIndexNumeric</a>
	 */
	public static final IRI transportIndexNumeric;

	/**
	 * transportLegs
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#transportLegs}.
	 * <p>
	 * Reference to the Transport Legs of the proposed routing
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#transportLegs">transportLegs</a>
	 */
	public static final IRI transportLegs;

	/**
	 * TransportLegs
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#TransportLegs}.
	 * <p>
	 * TransportLegs details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#TransportLegs">TransportLegs</a>
	 */
	public static final IRI TransportLegs;

	/**
	 * TransportMeans
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#TransportMeans}.
	 * <p>
	 * Transport means details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#TransportMeans">TransportMeans</a>
	 */
	public static final IRI TransportMeans;

	/**
	 * transportMeansServiceType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#transportMeansServiceType}.
	 * <p>
	 * Type of transport means service, e.g. Aircraftor Truck
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#transportMeansServiceType">transportMeansServiceType</a>
	 */
	public static final IRI transportMeansServiceType;

	/**
	 * transportMeansType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#transportMeansType}.
	 * <p>
	 * Type of transport means, e.g. 744, RFS
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#transportMeansType">transportMeansType</a>
	 */
	public static final IRI transportMeansType;

	/**
	 * TransportMovement
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#TransportMovement}.
	 * <p>
	 * Activity to describe transports, replaces the TransportSegment in v1.1
	 * and above
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#TransportMovement">TransportMovement</a>
	 */
	public static final IRI TransportMovement;

	/**
	 * transportOrganization
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#transportOrganization}.
	 * <p>
	 * Company operating the transport means
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#transportOrganization">transportOrganization</a>
	 */
	public static final IRI transportOrganization;

	/**
	 * turnable
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#turnable}.
	 * <p>
	 * Turnable indicator for the pieces (boolean)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#turnable">turnable</a>
	 */
	public static final IRI turnable;

	/**
	 * typeCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#typeCode}.
	 * <p>
	 * Packaging type identifier as per UNECE Rec 21 Annex V and VI e.g. 1A -
	 * Drum, steel - Packaging material code. Identifies the Logistic Unit
	 * package type. UN Recommendation on Transport of Dangerous Goods, Model
	 * Regulations 
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#typeCode">typeCode</a>
	 */
	public static final IRI typeCode;

	/**
	 * typicalCo2Coefficient
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#typicalCo2Coefficient}.
	 * <p>
	 * Required for some CO2 calculations
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#typicalCo2Coefficient">typicalCo2Coefficient</a>
	 */
	public static final IRI typicalCo2Coefficient;

	/**
	 * typicalFuelConsumption
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#typicalFuelConsumption}.
	 * <p>
	 * Typical fuel comsumption (e.g. 2 L / 1 nm)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#typicalFuelConsumption">typicalFuelConsumption</a>
	 */
	public static final IRI typicalFuelConsumption;

	/**
	 * ULD
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ULD}.
	 * <p>
	 * Unit Load Device details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ULD">ULD</a>
	 */
	public static final IRI ULD;

	/**
	 * ULDBasicPiece
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ULDBasicPiece}.
	 * <p>
	 * ULDBasicPiece details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ULDBasicPiece">ULDBasicPiece</a>
	 */
	public static final IRI ULDBasicPiece;

	/**
	 * uldContourCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#uldContourCode}.
	 * <p>
	 * Contour code as per IATA ULD Regulation
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#uldContourCode">uldContourCode</a>
	 */
	public static final IRI uldContourCode;

	/**
	 * uldLoadingIndicator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#uldLoadingIndicator}.
	 * <p>
	 * Indicator related to ULD loading (e.g. Main deck only)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#uldLoadingIndicator">uldLoadingIndicator</a>
	 */
	public static final IRI uldLoadingIndicator;

	/**
	 * uldOwnerCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#uldOwnerCode}.
	 * <p>
	 * Information about the ULD owner code described in a ULD specific piece
	 * or used for a rate in a Line Item
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#uldOwnerCode">uldOwnerCode</a>
	 */
	public static final IRI uldOwnerCode;

	/**
	 * uldRateClassType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#uldRateClassType}.
	 * <p>
	 * ULD Rate information - ULD Rate Class Type
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#uldRateClassType">uldRateClassType</a>
	 */
	public static final IRI uldRateClassType;

	/**
	 * uldSerialNumber
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#uldSerialNumber}.
	 * <p>
	 * Serial number that allows to uniquely identify the ULD
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#uldSerialNumber">uldSerialNumber</a>
	 */
	public static final IRI uldSerialNumber;

	/**
	 * ULDSpecificPiece
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#ULDSpecificPiece}.
	 * <p>
	 * ULDSpecificPiece details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#ULDSpecificPiece">ULDSpecificPiece</a>
	 */
	public static final IRI ULDSpecificPiece;

	/**
	 * uldTareWeightForRate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#uldTareWeightForRate}.
	 * <p>
	 * Information about the ULD tare weight used for the rate descbribed by
	 * the Line Item
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#uldTareWeightForRate">uldTareWeightForRate</a>
	 */
	public static final IRI uldTareWeightForRate;

	/**
	 * uldType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#uldType}.
	 * <p>
	 * Type of ULD as per IATA ULD Regulation
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#uldType">uldType</a>
	 */
	public static final IRI uldType;

	/**
	 * uldTypeCode
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#uldTypeCode}.
	 * <p>
	 * Standard Unit Load Device type code e.g. AKE - Certified Container -
	 * Contoured. Refer to IATA ULD Technical Manual
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#uldTypeCode">uldTypeCode</a>
	 */
	public static final IRI uldTypeCode;

	/**
	 * uniqueIdentifier
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#uniqueIdentifier}.
	 * <p>
	 * Manufacturer's unique product identifier
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#uniqueIdentifier">uniqueIdentifier</a>
	 */
	public static final IRI uniqueIdentifier;

	/**
	 * unit
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#unit}.
	 * <p>
	 * Unit of measurement as per MeasurementUnitCode codelist. If the code
	 * is not present, create an instance of MeasurementUnitCode based on
	 * UNECE Rec. 20 Rev. 17e-2021
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#unit">unit</a>
	 */
	public static final IRI unit;

	/**
	 * UNIT_LOAD_DEVICE
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#UNIT_LOAD_DEVICE}.
	 * <p>
	 * Indicates the load type as uld
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#UNIT_LOAD_DEVICE">UNIT_LOAD_DEVICE</a>
	 */
	public static final IRI UNIT_LOAD_DEVICE;

	/**
	 * unitBasis
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#unitBasis}.
	 * <p>
	 * Specific commodity code linked to commodity
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#unitBasis">unitBasis</a>
	 */
	public static final IRI unitBasis;

	/**
	 * UnitComposition
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#UnitComposition}.
	 * <p>
	 * Activity to describe composition and decomposition of LoadingUnits
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#UnitComposition">UnitComposition</a>
	 */
	public static final IRI UnitComposition;

	/**
	 * unitPrice
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#unitPrice}.
	 * <p>
	 * Product price per unit in the base
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#unitPrice">unitPrice</a>
	 */
	public static final IRI unitPrice;

	/**
	 * UnitsPreference
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#UnitsPreference}.
	 * <p>
	 * UnitsPreference details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#UnitsPreference">UnitsPreference</a>
	 */
	public static final IRI UnitsPreference;

	/**
	 * unitsPreference
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#unitsPreference}.
	 * <p>
	 * Reference to unit preferences of the request (e.g. kg or cm)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#unitsPreference">unitsPreference</a>
	 */
	public static final IRI unitsPreference;

	/**
	 * UNLOADING
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#UNLOADING}.
	 * <p>
	 * Describes an unloading process, for example removing an ULD from an
	 * aircraft or a piece from a truck
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#UNLOADING">UNLOADING</a>
	 */
	public static final IRI UNLOADING;

	/**
	 * unNumber
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#unNumber}.
	 * <p>
	 * Reference identifying the United Nations Dangerous Goods serial number
	 * assigned within the UN to substances and articles contained in a list
	 * of the dangerous goods most commonly carried. e.g. 1189 - Ethylene
	 * glycol monomethyl ether acetate
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#unNumber">unNumber</a>
	 */
	public static final IRI unNumber;

	/**
	 * UNPLANNED_STOP
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#UNPLANNED_STOP}.
	 * <p>
	 * Indicates the that the movement time describes an unplanned stop
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#UNPLANNED_STOP">UNPLANNED_STOP</a>
	 */
	public static final IRI UNPLANNED_STOP;

	/**
	 * updateBookingOptionRequests
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#updateBookingOptionRequests}.
	 * <p>
	 * References to BookingOptionRequests that request to update the Booking
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#updateBookingOptionRequests">updateBookingOptionRequests</a>
	 */
	public static final IRI updateBookingOptionRequests;

	/**
	 * upid
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#upid}.
	 * <p>
	 * Unique Piece Identifier (UPID) of the piece. Refer IATA Recommended
	 * Practice 1689
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#upid">upid</a>
	 */
	public static final IRI upid;

	/**
	 * usedInCheck
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#usedInCheck}.
	 * <p>
	 * Reference to the Check the template was used in
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#usedInCheck">usedInCheck</a>
	 */
	public static final IRI usedInCheck;

	/**
	 * usedTemplate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#usedTemplate}.
	 * <p>
	 * Reference to the Template used in the Check
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#usedTemplate">usedTemplate</a>
	 */
	public static final IRI usedTemplate;

	/**
	 * usedToDateQuotaQuantity
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#usedToDateQuotaQuantity}.
	 * <p>
	 * total number of specimens exported in the current calendar year and
	 * the current annuela quota for the species concerned (box 11a)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#usedToDateQuotaQuantity">usedToDateQuotaQuantity</a>
	 */
	public static final IRI usedToDateQuotaQuantity;

	/**
	 * validFrom
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#validFrom}.
	 * <p>
	 * Validity start date based on usage context
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#validFrom">validFrom</a>
	 */
	public static final IRI validFrom;

	/**
	 * validUntil
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#validUntil}.
	 * <p>
	 * Validity end date (date of expiry) based on usage context
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#validUntil">validUntil</a>
	 */
	public static final IRI validUntil;

	/**
	 * Value
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Value}.
	 * <p>
	 * Unit of measurement details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Value">Value</a>
	 */
	public static final IRI Value;

	/**
	 * vatIndicator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#vatIndicator}.
	 * <p>
	 * Indicate if subject to VAT (boolean)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#vatIndicator">vatIndicator</a>
	 */
	public static final IRI vatIndicator;

	/**
	 * vehicleModel
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#vehicleModel}.
	 * <p>
	 * Model or make of the vehicle (e.g. A33-3)
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#vehicleModel">vehicleModel</a>
	 */
	public static final IRI vehicleModel;

	/**
	 * vehicleRegistration
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#vehicleRegistration}.
	 * <p>
	 * Vehicle identification - e.g. aircraft registration number
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#vehicleRegistration">vehicleRegistration</a>
	 */
	public static final IRI vehicleRegistration;

	/**
	 * vehicleSize
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#vehicleSize}.
	 * <p>
	 * Size of the vehicle - free text
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#vehicleSize">vehicleSize</a>
	 */
	public static final IRI vehicleSize;

	/**
	 * vehicleType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#vehicleType}.
	 * <p>
	 * Vehicle or container type. Refer UNECE28, e.g. 4.. - Aircraft, type
	 * unknown.For Air refer to IATA Standard Schedules Information Manua in
	 * section ATA/IATA Aircraft Types
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#vehicleType">vehicleType</a>
	 */
	public static final IRI vehicleType;

	/**
	 * version
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#version}.
	 * <p>
	 * Version of the template
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#version">version</a>
	 */
	public static final IRI version;

	/**
	 * VIBRATION
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#VIBRATION}.
	 * <p>
	 * Indicates the sensor type as vibration
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#VIBRATION">VIBRATION</a>
	 */
	public static final IRI VIBRATION;

	/**
	 * volume
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#volume}.
	 * <p>
	 * Volume
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#volume">volume</a>
	 */
	public static final IRI volume;

	/**
	 * VolumePieceGroup
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#VolumePieceGroup}.
	 * <p>
	 * VolumePieceGroup details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#VolumePieceGroup">VolumePieceGroup</a>
	 */
	public static final IRI VolumePieceGroup;

	/**
	 * VolumetricWeight
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#VolumetricWeight}.
	 * <p>
	 * Volumetric weight details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#VolumetricWeight">VolumetricWeight</a>
	 */
	public static final IRI VolumetricWeight;

	/**
	 * volumetricWeight
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#volumetricWeight}.
	 * <p>
	 * Volumetric weight details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#volumetricWeight">volumetricWeight</a>
	 */
	public static final IRI volumetricWeight;

	/**
	 * volumetricWeightForRate
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#volumetricWeightForRate}.
	 * <p>
	 * Volumetric weight used for the rate described by this line item
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#volumetricWeightForRate">volumetricWeightForRate</a>
	 */
	public static final IRI volumetricWeightForRate;

	/**
	 * volumeUnit
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#volumeUnit}.
	 * <p>
	 * Preferred unit for volume
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#volumeUnit">volumeUnit</a>
	 */
	public static final IRI volumeUnit;

	/**
	 * waybill
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#waybill}.
	 * <p>
	 * Reference to the Waybill of the shipment
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#waybill">waybill</a>
	 */
	public static final IRI waybill;

	/**
	 * Waybill
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#Waybill}.
	 * <p>
	 * Waybill details
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#Waybill">Waybill</a>
	 */
	public static final IRI Waybill;

	/**
	 * TACTRateDescription
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#WaybillLineItem}.
	 * <p>
	 * Information from AWB Rate Description section as per bullet point 18 -
	 * data elements 22A - 22Z from AWB
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#WaybillLineItem">WaybillLineItem</a>
	 */
	public static final IRI WaybillLineItem;

	/**
	 * waybillLineItems
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#waybillLineItems}.
	 * <p>
	 * Information about rates applying to this Waybill handled as line item
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#waybillLineItems">waybillLineItems</a>
	 */
	public static final IRI waybillLineItems;

	/**
	 * waybillNumber
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#waybillNumber}.
	 * <p>
	 * House or Master Waybill unique identifier
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#waybillNumber">waybillNumber</a>
	 */
	public static final IRI waybillNumber;

	/**
	 * waybillPrefix
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#waybillPrefix}.
	 * <p>
	 * Prefix used for the Waybill Number. Refer to IATA Airlines Codes
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#waybillPrefix">waybillPrefix</a>
	 */
	public static final IRI waybillPrefix;

	/**
	 * waybillType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#waybillType}.
	 * <p>
	 * Type of the Waybill: House, Direct or Master
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#waybillType">waybillType</a>
	 */
	public static final IRI waybillType;

	/**
	 * WaybillType
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#WaybillType}.
	 * <p>
	 * Restricted code list for Waybill types
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#WaybillType">WaybillType</a>
	 */
	public static final IRI WaybillType;

	/**
	 * WEBSITE
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#WEBSITE}.
	 * <p>
	 * Indicates a contact detail as website
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#WEBSITE">WEBSITE</a>
	 */
	public static final IRI WEBSITE;

	/**
	 * weight
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#weight}.
	 * <p>
	 * Weight of the item
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#weight">weight</a>
	 */
	public static final IRI weight;

	/**
	 * weightUnit
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#weightUnit}.
	 * <p>
	 * Preferred unit for weight
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#weightUnit">weightUnit</a>
	 */
	public static final IRI weightUnit;

	/**
	 * weightValuationIndicator
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#weightValuationIndicator}.
	 * <p>
	 * Indicator whether the payment for the Weight/Valuation is to be made
	 * at origin (prepaid) or at destination (collect) as per bullet point 13
	 * - data element 14a/14b from AWB
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#weightValuationIndicator">weightValuationIndicator</a>
	 */
	public static final IRI weightValuationIndicator;

	/**
	 * width
	 * <p>
	 * {@code https://onerecord.iata.org/ns/cargo#width}.
	 * <p>
	 * Width
	 *
	 * @see <a href="https://onerecord.iata.org/ns/cargo#width">width</a>
	 */
	public static final IRI width;

	static {
		ValueFactory factory = SimpleValueFactory.getInstance();

		ACCELEROMETER = factory.createIRI(CARGO.NAMESPACE, "ACCELEROMETER");
		accountingInformation = factory.createIRI(CARGO.NAMESPACE, "accountingInformation");
		acquisitionDateTime = factory.createIRI(CARGO.NAMESPACE, "acquisitionDateTime");
		actionEndTime = factory.createIRI(CARGO.NAMESPACE, "actionEndTime");
		actionStartTime = factory.createIRI(CARGO.NAMESPACE, "actionStartTime");
		ActionTimeType = factory.createIRI(CARGO.NAMESPACE, "ActionTimeType");
		actionTimeType = factory.createIRI(CARGO.NAMESPACE, "actionTimeType");
		ACTIVE = factory.createIRI(CARGO.NAMESPACE, "ACTIVE");
		activity = factory.createIRI(CARGO.NAMESPACE, "activity");
		activityLevelMeasure = factory.createIRI(CARGO.NAMESPACE, "activityLevelMeasure");
		ActivitySequence = factory.createIRI(CARGO.NAMESPACE, "ActivitySequence");
		activitySequences = factory.createIRI(CARGO.NAMESPACE, "activitySequences");
		Actor = factory.createIRI(CARGO.NAMESPACE, "Actor");
		ACTUAL = factory.createIRI(CARGO.NAMESPACE, "ACTUAL");
		additionalHazardClassificationId = factory.createIRI(CARGO.NAMESPACE, "additionalHazardClassificationId");
		additionalInformation = factory.createIRI(CARGO.NAMESPACE, "additionalInformation");
		additionalSecurityInformation = factory.createIRI(CARGO.NAMESPACE, "additionalSecurityInformation");
		address = factory.createIRI(CARGO.NAMESPACE, "address");
		Address = factory.createIRI(CARGO.NAMESPACE, "Address");
		addressCode = factory.createIRI(CARGO.NAMESPACE, "addressCode");
		Adjustments = factory.createIRI(CARGO.NAMESPACE, "Adjustments");
		adjustments = factory.createIRI(CARGO.NAMESPACE, "adjustments");
		aircraftLimitationInformation = factory.createIRI(CARGO.NAMESPACE, "aircraftLimitationInformation");
		aircraftPossibilityCode = factory.createIRI(CARGO.NAMESPACE, "aircraftPossibilityCode");
		airlineCode = factory.createIRI(CARGO.NAMESPACE, "airlineCode");
		allPackedInOneIndicator = factory.createIRI(CARGO.NAMESPACE, "allPackedInOneIndicator");
		ALTERNATE_EMAIL_ADDRESS = factory.createIRI(CARGO.NAMESPACE, "ALTERNATE_EMAIL_ADDRESS");
		ALTERNATE_PHONE_NUMBER = factory.createIRI(CARGO.NAMESPACE, "ALTERNATE_PHONE_NUMBER");
		alternatives = factory.createIRI(CARGO.NAMESPACE, "alternatives");
		annualQuotaQuantity = factory.createIRI(CARGO.NAMESPACE, "annualQuotaQuantity");
		answer = factory.createIRI(CARGO.NAMESPACE, "answer");
		Answer = factory.createIRI(CARGO.NAMESPACE, "Answer");
		answerActor = factory.createIRI(CARGO.NAMESPACE, "answerActor");
		answerOptionsText = factory.createIRI(CARGO.NAMESPACE, "answerOptionsText");
		answerOptionsValue = factory.createIRI(CARGO.NAMESPACE, "answerOptionsValue");
		answerValue = factory.createIRI(CARGO.NAMESPACE, "answerValue");
		appliedOnPieces = factory.createIRI(CARGO.NAMESPACE, "appliedOnPieces");
		arrivalDate = factory.createIRI(CARGO.NAMESPACE, "arrivalDate");
		arrivalLocation = factory.createIRI(CARGO.NAMESPACE, "arrivalLocation");
		associatedEpermit = factory.createIRI(CARGO.NAMESPACE, "associatedEpermit");
		associatedOrganization = factory.createIRI(CARGO.NAMESPACE, "associatedOrganization");
		ataDesignator = factory.createIRI(CARGO.NAMESPACE, "ataDesignator");
		attachedIotDevices = factory.createIRI(CARGO.NAMESPACE, "attachedIotDevices");
		attachedToObject = factory.createIRI(CARGO.NAMESPACE, "attachedToObject");
		authorizationInformation = factory.createIRI(CARGO.NAMESPACE, "authorizationInformation");
		awbAcceptanceDate = factory.createIRI(CARGO.NAMESPACE, "awbAcceptanceDate");
		awbDeliveryDate = factory.createIRI(CARGO.NAMESPACE, "awbDeliveryDate");
		awbExecutionDate = factory.createIRI(CARGO.NAMESPACE, "awbExecutionDate");
		awbUseIndicator = factory.createIRI(CARGO.NAMESPACE, "awbUseIndicator");
		basedAtLocation = factory.createIRI(CARGO.NAMESPACE, "basedAtLocation");
		batchNumber = factory.createIRI(CARGO.NAMESPACE, "batchNumber");
		billingChargeIdentifier = factory.createIRI(CARGO.NAMESPACE, "billingChargeIdentifier");
		billingDetails = factory.createIRI(CARGO.NAMESPACE, "billingDetails");
		BillingDetails = factory.createIRI(CARGO.NAMESPACE, "BillingDetails");
		BOOKABLE = factory.createIRI(CARGO.NAMESPACE, "BOOKABLE");
		BOOKED = factory.createIRI(CARGO.NAMESPACE, "BOOKED");
		Booking = factory.createIRI(CARGO.NAMESPACE, "Booking");
		booking = factory.createIRI(CARGO.NAMESPACE, "booking");
		BookingOption = factory.createIRI(CARGO.NAMESPACE, "BookingOption");
		BookingOptionRequest = factory.createIRI(CARGO.NAMESPACE, "BookingOptionRequest");
		bookingOptions = factory.createIRI(CARGO.NAMESPACE, "bookingOptions");
		BookingOptionStatus = factory.createIRI(CARGO.NAMESPACE, "BookingOptionStatus");
		bookingPreference = factory.createIRI(CARGO.NAMESPACE, "bookingPreference");
		BookingPreferences = factory.createIRI(CARGO.NAMESPACE, "BookingPreferences");
		bookingRequest = factory.createIRI(CARGO.NAMESPACE, "bookingRequest");
		BookingRequest = factory.createIRI(CARGO.NAMESPACE, "BookingRequest");
		BookingShipment = factory.createIRI(CARGO.NAMESPACE, "BookingShipment");
		bookingShipmentDetails = factory.createIRI(CARGO.NAMESPACE, "bookingShipmentDetails");
		bookingStatus = factory.createIRI(CARGO.NAMESPACE, "bookingStatus");
		BookingStatus = factory.createIRI(CARGO.NAMESPACE, "BookingStatus");
		BookingTimes = factory.createIRI(CARGO.NAMESPACE, "BookingTimes");
		bookingTimes = factory.createIRI(CARGO.NAMESPACE, "bookingTimes");
		bookingToUpdate = factory.createIRI(CARGO.NAMESPACE, "bookingToUpdate");
		BULK = factory.createIRI(CARGO.NAMESPACE, "BULK");
		calculatedEmissions = factory.createIRI(CARGO.NAMESPACE, "calculatedEmissions");
		calculationFor = factory.createIRI(CARGO.NAMESPACE, "calculationFor");
		CANCELLED = factory.createIRI(CARGO.NAMESPACE, "CANCELLED");
		Carrier = factory.createIRI(CARGO.NAMESPACE, "Carrier");
		carrier = factory.createIRI(CARGO.NAMESPACE, "carrier");
		carrierChargeCode = factory.createIRI(CARGO.NAMESPACE, "carrierChargeCode");
		carrierDeclarationDate = factory.createIRI(CARGO.NAMESPACE, "carrierDeclarationDate");
		carrierDeclarationPlace = factory.createIRI(CARGO.NAMESPACE, "carrierDeclarationPlace");
		carrierDeclarationSignature = factory.createIRI(CARGO.NAMESPACE, "carrierDeclarationSignature");
		CarrierProduct = factory.createIRI(CARGO.NAMESPACE, "CarrierProduct");
		carrierProduct = factory.createIRI(CARGO.NAMESPACE, "carrierProduct");
		categoryCode = factory.createIRI(CARGO.NAMESPACE, "categoryCode");
		certifiedByActor = factory.createIRI(CARGO.NAMESPACE, "certifiedByActor");
		Characteristic = factory.createIRI(CARGO.NAMESPACE, "Characteristic");
		characteristicType = factory.createIRI(CARGO.NAMESPACE, "characteristicType");
		chargeableWeight = factory.createIRI(CARGO.NAMESPACE, "chargeableWeight");
		chargeableWeightForRate = factory.createIRI(CARGO.NAMESPACE, "chargeableWeightForRate");
		chargeCode = factory.createIRI(CARGO.NAMESPACE, "chargeCode");
		chargeDescription = factory.createIRI(CARGO.NAMESPACE, "chargeDescription");
		chargePaymentType = factory.createIRI(CARGO.NAMESPACE, "chargePaymentType");
		chargeType = factory.createIRI(CARGO.NAMESPACE, "chargeType");
		Check = factory.createIRI(CARGO.NAMESPACE, "Check");
		checkActions = factory.createIRI(CARGO.NAMESPACE, "checkActions");
		checkedObject = factory.createIRI(CARGO.NAMESPACE, "checkedObject");
		checker = factory.createIRI(CARGO.NAMESPACE, "checker");
		checkRemark = factory.createIRI(CARGO.NAMESPACE, "checkRemark");
		checks = factory.createIRI(CARGO.NAMESPACE, "checks");
		checksum = factory.createIRI(CARGO.NAMESPACE, "checksum");
		checkTemplate = factory.createIRI(CARGO.NAMESPACE, "checkTemplate");
		CheckTemplate = factory.createIRI(CARGO.NAMESPACE, "CheckTemplate");
		checkTotalResult = factory.createIRI(CARGO.NAMESPACE, "checkTotalResult");
		CheckTotalResult = factory.createIRI(CARGO.NAMESPACE, "CheckTotalResult");
		cityCode = factory.createIRI(CARGO.NAMESPACE, "cityCode");
		co2Emissions = factory.createIRI(CARGO.NAMESPACE, "co2Emissions");
		CO2Emissions = factory.createIRI(CARGO.NAMESPACE, "CO2Emissions");
		code = factory.createIRI(CARGO.NAMESPACE, "code");
		codeDescription = factory.createIRI(CARGO.NAMESPACE, "codeDescription");
		codeLevel = factory.createIRI(CARGO.NAMESPACE, "codeLevel");
		CodeListElement = factory.createIRI(CARGO.NAMESPACE, "CodeListElement");
		codeListName = factory.createIRI(CARGO.NAMESPACE, "codeListName");
		codeListReference = factory.createIRI(CARGO.NAMESPACE, "codeListReference");
		codeListVersion = factory.createIRI(CARGO.NAMESPACE, "codeListVersion");
		coload = factory.createIRI(CARGO.NAMESPACE, "coload");
		commission = factory.createIRI(CARGO.NAMESPACE, "commission");
		commissionIndicator = factory.createIRI(CARGO.NAMESPACE, "commissionIndicator");
		commissionPercentage = factory.createIRI(CARGO.NAMESPACE, "commissionPercentage");
		commodityItemNumber = factory.createIRI(CARGO.NAMESPACE, "commodityItemNumber");
		commodityItemNumberForRate = factory.createIRI(CARGO.NAMESPACE, "commodityItemNumberForRate");
		Company = factory.createIRI(CARGO.NAMESPACE, "Company");
		COMPLETE = factory.createIRI(CARGO.NAMESPACE, "COMPLETE");
		complianceDeclarationText = factory.createIRI(CARGO.NAMESPACE, "complianceDeclarationText");
		composedMaterials = factory.createIRI(CARGO.NAMESPACE, "composedMaterials");
		composedPieces = factory.createIRI(CARGO.NAMESPACE, "composedPieces");
		Composing = factory.createIRI(CARGO.NAMESPACE, "Composing");
		COMPOSITION = factory.createIRI(CARGO.NAMESPACE, "COMPOSITION");
		compositionActions = factory.createIRI(CARGO.NAMESPACE, "compositionActions");
		compositionIdentifier = factory.createIRI(CARGO.NAMESPACE, "compositionIdentifier");
		CompositionType = factory.createIRI(CARGO.NAMESPACE, "CompositionType");
		compositionType = factory.createIRI(CARGO.NAMESPACE, "compositionType");
		CONFIRMED = factory.createIRI(CARGO.NAMESPACE, "CONFIRMED");
		connectedSensors = factory.createIRI(CARGO.NAMESPACE, "connectedSensors");
		consignee = factory.createIRI(CARGO.NAMESPACE, "consignee");
		consignmentItems = factory.createIRI(CARGO.NAMESPACE, "consignmentItems");
		consignments = factory.createIRI(CARGO.NAMESPACE, "consignments");
		consignorDeclarationSignature = factory.createIRI(CARGO.NAMESPACE, "consignorDeclarationSignature");
		consolidationIndicator = factory.createIRI(CARGO.NAMESPACE, "consolidationIndicator");
		ContactDetail = factory.createIRI(CARGO.NAMESPACE, "ContactDetail");
		contactDetails = factory.createIRI(CARGO.NAMESPACE, "contactDetails");
		contactDetailType = factory.createIRI(CARGO.NAMESPACE, "contactDetailType");
		ContactDetailType = factory.createIRI(CARGO.NAMESPACE, "ContactDetailType");
		contactPersons = factory.createIRI(CARGO.NAMESPACE, "contactPersons");
		ContactRole = factory.createIRI(CARGO.NAMESPACE, "ContactRole");
		contactRole = factory.createIRI(CARGO.NAMESPACE, "contactRole");
		containedItems = factory.createIRI(CARGO.NAMESPACE, "containedItems");
		containedPieces = factory.createIRI(CARGO.NAMESPACE, "containedPieces");
		contentCode = factory.createIRI(CARGO.NAMESPACE, "contentCode");
		contentOfDgProductRadioactive = factory.createIRI(CARGO.NAMESPACE, "contentOfDgProductRadioactive");
		contentProductionCountry = factory.createIRI(CARGO.NAMESPACE, "contentProductionCountry");
		contentProducts = factory.createIRI(CARGO.NAMESPACE, "contentProducts");
		conversionFactor = factory.createIRI(CARGO.NAMESPACE, "conversionFactor");
		copyIndicator = factory.createIRI(CARGO.NAMESPACE, "copyIndicator");
		correctionNumber = factory.createIRI(CARGO.NAMESPACE, "correctionNumber");
		correctionSerialNumber = factory.createIRI(CARGO.NAMESPACE, "correctionSerialNumber");
		country = factory.createIRI(CARGO.NAMESPACE, "country");
		coveringOrganization = factory.createIRI(CARGO.NAMESPACE, "coveringOrganization");
		createdAtLocation = factory.createIRI(CARGO.NAMESPACE, "createdAtLocation");
		creationDate = factory.createIRI(CARGO.NAMESPACE, "creationDate");
		criticalitySafetyIndexNumeric = factory.createIRI(CARGO.NAMESPACE, "criticalitySafetyIndexNumeric");
		currency = factory.createIRI(CARGO.NAMESPACE, "currency");
		currencyUnit = factory.createIRI(CARGO.NAMESPACE, "currencyUnit");
		CurrencyValue = factory.createIRI(CARGO.NAMESPACE, "CurrencyValue");
		CUSTOMER_CONTACT = factory.createIRI(CARGO.NAMESPACE, "CUSTOMER_CONTACT");
		CUSTOMS_CONTACT = factory.createIRI(CARGO.NAMESPACE, "CUSTOMS_CONTACT");
		customsInformation = factory.createIRI(CARGO.NAMESPACE, "customsInformation");
		CustomsInformation = factory.createIRI(CARGO.NAMESPACE, "CustomsInformation");
		customsOriginCode = factory.createIRI(CARGO.NAMESPACE, "customsOriginCode");
		damageFlag = factory.createIRI(CARGO.NAMESPACE, "damageFlag");
		date = factory.createIRI(CARGO.NAMESPACE, "date");
		declaredValueForCarriage = factory.createIRI(CARGO.NAMESPACE, "declaredValueForCarriage");
		declaredValueForCustoms = factory.createIRI(CARGO.NAMESPACE, "declaredValueForCustoms");
		DECOMPOSITION = factory.createIRI(CARGO.NAMESPACE, "DECOMPOSITION");
		DELETED = factory.createIRI(CARGO.NAMESPACE, "DELETED");
		demurrageCode = factory.createIRI(CARGO.NAMESPACE, "demurrageCode");
		department = factory.createIRI(CARGO.NAMESPACE, "department");
		departureDate = factory.createIRI(CARGO.NAMESPACE, "departureDate");
		departureLocation = factory.createIRI(CARGO.NAMESPACE, "departureLocation");
		describedObjects = factory.createIRI(CARGO.NAMESPACE, "describedObjects");
		description = factory.createIRI(CARGO.NAMESPACE, "description");
		destinationCharges = factory.createIRI(CARGO.NAMESPACE, "destinationCharges");
		destinationCurrencyRate = factory.createIRI(CARGO.NAMESPACE, "destinationCurrencyRate");
		detailedWaybill = factory.createIRI(CARGO.NAMESPACE, "detailedWaybill");
		deviceModel = factory.createIRI(CARGO.NAMESPACE, "deviceModel");
		DgDeclaration = factory.createIRI(CARGO.NAMESPACE, "DgDeclaration");
		dgDeclaration = factory.createIRI(CARGO.NAMESPACE, "dgDeclaration");
		DgProductRadioactive = factory.createIRI(CARGO.NAMESPACE, "DgProductRadioactive");
		DgRadioactiveIsotope = factory.createIRI(CARGO.NAMESPACE, "DgRadioactiveIsotope");
		dgRadioactiveMaterial = factory.createIRI(CARGO.NAMESPACE, "dgRadioactiveMaterial");
		dgRaTypeCode = factory.createIRI(CARGO.NAMESPACE, "dgRaTypeCode");
		Dimensions = factory.createIRI(CARGO.NAMESPACE, "Dimensions");
		dimensions = factory.createIRI(CARGO.NAMESPACE, "dimensions");
		dimensionsForRate = factory.createIRI(CARGO.NAMESPACE, "dimensionsForRate");
		dimensionsUnit = factory.createIRI(CARGO.NAMESPACE, "dimensionsUnit");
		DIRECT = factory.createIRI(CARGO.NAMESPACE, "DIRECT");
		direction = factory.createIRI(CARGO.NAMESPACE, "direction");
		DirectionType = factory.createIRI(CARGO.NAMESPACE, "DirectionType");
		discount = factory.createIRI(CARGO.NAMESPACE, "discount");
		distanceCalculated = factory.createIRI(CARGO.NAMESPACE, "distanceCalculated");
		distanceMeasured = factory.createIRI(CARGO.NAMESPACE, "distanceMeasured");
		documentIdentifier = factory.createIRI(CARGO.NAMESPACE, "documentIdentifier");
		documentLink = factory.createIRI(CARGO.NAMESPACE, "documentLink");
		documentName = factory.createIRI(CARGO.NAMESPACE, "documentName");
		documents = factory.createIRI(CARGO.NAMESPACE, "documents");
		documentType = factory.createIRI(CARGO.NAMESPACE, "documentType");
		documentVersion = factory.createIRI(CARGO.NAMESPACE, "documentVersion");
		dryIceWeight = factory.createIRI(CARGO.NAMESPACE, "dryIceWeight");
		earliestAcceptanceTime = factory.createIRI(CARGO.NAMESPACE, "earliestAcceptanceTime");
		elevation = factory.createIRI(CARGO.NAMESPACE, "elevation");
		EMAIL_ADDRESS = factory.createIRI(CARGO.NAMESPACE, "EMAIL_ADDRESS");
		EMERGENCY_CONTACT = factory.createIRI(CARGO.NAMESPACE, "EMERGENCY_CONTACT");
		emergencyContact = factory.createIRI(CARGO.NAMESPACE, "emergencyContact");
		employeeId = factory.createIRI(CARGO.NAMESPACE, "employeeId");
		entitlement = factory.createIRI(CARGO.NAMESPACE, "entitlement");
		epermit = factory.createIRI(CARGO.NAMESPACE, "epermit");
		EpermitConsignment = factory.createIRI(CARGO.NAMESPACE, "EpermitConsignment");
		epermitNumber = factory.createIRI(CARGO.NAMESPACE, "epermitNumber");
		EpermitSignature = factory.createIRI(CARGO.NAMESPACE, "EpermitSignature");
		ESTIMATED = factory.createIRI(CARGO.NAMESPACE, "ESTIMATED");
		eventCode = factory.createIRI(CARGO.NAMESPACE, "eventCode");
		eventDate = factory.createIRI(CARGO.NAMESPACE, "eventDate");
		eventFor = factory.createIRI(CARGO.NAMESPACE, "eventFor");
		eventLocation = factory.createIRI(CARGO.NAMESPACE, "eventLocation");
		eventName = factory.createIRI(CARGO.NAMESPACE, "eventName");
		events = factory.createIRI(CARGO.NAMESPACE, "events");
		eventTimeType = factory.createIRI(CARGO.NAMESPACE, "eventTimeType");
		EventTimeType = factory.createIRI(CARGO.NAMESPACE, "EventTimeType");
		examiningQuantity = factory.createIRI(CARGO.NAMESPACE, "examiningQuantity");
		exchangeRate = factory.createIRI(CARGO.NAMESPACE, "exchangeRate");
		excludedViaPoints = factory.createIRI(CARGO.NAMESPACE, "excludedViaPoints");
		exclusiveUseIndicator = factory.createIRI(CARGO.NAMESPACE, "exclusiveUseIndicator");
		ExecutionStatus = factory.createIRI(CARGO.NAMESPACE, "ExecutionStatus");
		executionStatus = factory.createIRI(CARGO.NAMESPACE, "executionStatus");
		EXPECTED = factory.createIRI(CARGO.NAMESPACE, "EXPECTED");
		expectedCommodity = factory.createIRI(CARGO.NAMESPACE, "expectedCommodity");
		expectedHScode = factory.createIRI(CARGO.NAMESPACE, "expectedHScode");
		EXPIRED = factory.createIRI(CARGO.NAMESPACE, "EXPIRED");
		expiryDate = factory.createIRI(CARGO.NAMESPACE, "expiryDate");
		explosiveCompatibilityGroupCode = factory.createIRI(CARGO.NAMESPACE, "explosiveCompatibilityGroupCode");
		exportTradeCountry = factory.createIRI(CARGO.NAMESPACE, "exportTradeCountry");
		ExternalReference = factory.createIRI(CARGO.NAMESPACE, "ExternalReference");
		externalReferences = factory.createIRI(CARGO.NAMESPACE, "externalReferences");
		FAX_NUMBER = factory.createIRI(CARGO.NAMESPACE, "FAX_NUMBER");
		firstName = factory.createIRI(CARGO.NAMESPACE, "firstName");
		fissileExceptionIndicator = factory.createIRI(CARGO.NAMESPACE, "fissileExceptionIndicator");
		fissileExceptionReference = factory.createIRI(CARGO.NAMESPACE, "fissileExceptionReference");
		forBookingOption = factory.createIRI(CARGO.NAMESPACE, "forBookingOption");
		forBookingOptionRequest = factory.createIRI(CARGO.NAMESPACE, "forBookingOptionRequest");
		forBookingRequest = factory.createIRI(CARGO.NAMESPACE, "forBookingRequest");
		forEpermit = factory.createIRI(CARGO.NAMESPACE, "forEpermit");
		forPrices = factory.createIRI(CARGO.NAMESPACE, "forPrices");
		forProductDg = factory.createIRI(CARGO.NAMESPACE, "forProductDg");
		fuelAmountCalculated = factory.createIRI(CARGO.NAMESPACE, "fuelAmountCalculated");
		fuelAmountMeasured = factory.createIRI(CARGO.NAMESPACE, "fuelAmountMeasured");
		fuelType = factory.createIRI(CARGO.NAMESPACE, "fuelType");
		fulfillsUldTypeCode = factory.createIRI(CARGO.NAMESPACE, "fulfillsUldTypeCode");
		GEOLOCATION = factory.createIRI(CARGO.NAMESPACE, "GEOLOCATION");
		geolocation = factory.createIRI(CARGO.NAMESPACE, "geolocation");
		Geolocation = factory.createIRI(CARGO.NAMESPACE, "Geolocation");
		givenAtLocation = factory.createIRI(CARGO.NAMESPACE, "givenAtLocation");
		goodsDescription = factory.createIRI(CARGO.NAMESPACE, "goodsDescription");
		goodsDescriptionForRate = factory.createIRI(CARGO.NAMESPACE, "goodsDescriptionForRate");
		goodsTypeCode = factory.createIRI(CARGO.NAMESPACE, "goodsTypeCode");
		goodsTypeExtensionCode = factory.createIRI(CARGO.NAMESPACE, "goodsTypeExtensionCode");
		grandTotal = factory.createIRI(CARGO.NAMESPACE, "grandTotal");
		grossWeight = factory.createIRI(CARGO.NAMESPACE, "grossWeight");
		grossWeightForRate = factory.createIRI(CARGO.NAMESPACE, "grossWeightForRate");
		groundsForExemption = factory.createIRI(CARGO.NAMESPACE, "groundsForExemption");
		handlingInformation = factory.createIRI(CARGO.NAMESPACE, "handlingInformation");
		hazardClassificationId = factory.createIRI(CARGO.NAMESPACE, "hazardClassificationId");
		height = factory.createIRI(CARGO.NAMESPACE, "height");
		HOUSE = factory.createIRI(CARGO.NAMESPACE, "HOUSE");
		houseWaybills = factory.createIRI(CARGO.NAMESPACE, "houseWaybills");
		hsCode = factory.createIRI(CARGO.NAMESPACE, "hsCode");
		hsCodeForRate = factory.createIRI(CARGO.NAMESPACE, "hsCodeForRate");
		hsCommodityDescription = factory.createIRI(CARGO.NAMESPACE, "hsCommodityDescription");
		hsCommodityName = factory.createIRI(CARGO.NAMESPACE, "hsCommodityName");
		hsType = factory.createIRI(CARGO.NAMESPACE, "hsType");
		HUMIDITY = factory.createIRI(CARGO.NAMESPACE, "HUMIDITY");
		iataCargoAgentCode = factory.createIRI(CARGO.NAMESPACE, "iataCargoAgentCode");
		iataCargoAgentLocationIdentifier = factory.createIRI(CARGO.NAMESPACE, "iataCargoAgentLocationIdentifier");
		INBOUND = factory.createIRI(CARGO.NAMESPACE, "INBOUND");
		includedViaPoints = factory.createIRI(CARGO.NAMESPACE, "includedViaPoints");
		incoterms = factory.createIRI(CARGO.NAMESPACE, "incoterms");
		inPiece = factory.createIRI(CARGO.NAMESPACE, "inPiece");
		Insurance = factory.createIRI(CARGO.NAMESPACE, "Insurance");
		insurance = factory.createIRI(CARGO.NAMESPACE, "insurance");
		insuredAmount = factory.createIRI(CARGO.NAMESPACE, "insuredAmount");
		insuredShipments = factory.createIRI(CARGO.NAMESPACE, "insuredShipments");
		inUnitComposition = factory.createIRI(CARGO.NAMESPACE, "inUnitComposition");
		involvedInActions = factory.createIRI(CARGO.NAMESPACE, "involvedInActions");
		involvedParties = factory.createIRI(CARGO.NAMESPACE, "involvedParties");
		IotDevice = factory.createIRI(CARGO.NAMESPACE, "IotDevice");
		isotopeId = factory.createIRI(CARGO.NAMESPACE, "isotopeId");
		isotopeName = factory.createIRI(CARGO.NAMESPACE, "isotopeName");
		isotopes = factory.createIRI(CARGO.NAMESPACE, "isotopes");
		issuedBy = factory.createIRI(CARGO.NAMESPACE, "issuedBy");
		issuedForPiece = factory.createIRI(CARGO.NAMESPACE, "issuedForPiece");
		issuedForShipment = factory.createIRI(CARGO.NAMESPACE, "issuedForShipment");
		issuedForWaybill = factory.createIRI(CARGO.NAMESPACE, "issuedForWaybill");
		issuedOn = factory.createIRI(CARGO.NAMESPACE, "issuedOn");
		Item = factory.createIRI(CARGO.NAMESPACE, "Item");
		ItemDg = factory.createIRI(CARGO.NAMESPACE, "ItemDg");
		itemQuantity = factory.createIRI(CARGO.NAMESPACE, "itemQuantity");
		jobTitle = factory.createIRI(CARGO.NAMESPACE, "jobTitle");
		knownShipper = factory.createIRI(CARGO.NAMESPACE, "knownShipper");
		lastName = factory.createIRI(CARGO.NAMESPACE, "lastName");
		latestAcceptanceTime = factory.createIRI(CARGO.NAMESPACE, "latestAcceptanceTime");
		latestArrivalTime = factory.createIRI(CARGO.NAMESPACE, "latestArrivalTime");
		latitude = factory.createIRI(CARGO.NAMESPACE, "latitude");
		legacyTemplate = factory.createIRI(CARGO.NAMESPACE, "legacyTemplate");
		legNumber = factory.createIRI(CARGO.NAMESPACE, "legNumber");
		length = factory.createIRI(CARGO.NAMESPACE, "length");
		LIGHT = factory.createIRI(CARGO.NAMESPACE, "LIGHT");
		lineItemNumber = factory.createIRI(CARGO.NAMESPACE, "lineItemNumber");
		LiveAnimalsEpermit = factory.createIRI(CARGO.NAMESPACE, "LiveAnimalsEpermit");
		loadedMaterials = factory.createIRI(CARGO.NAMESPACE, "loadedMaterials");
		loadedPieces = factory.createIRI(CARGO.NAMESPACE, "loadedPieces");
		loadedUnits = factory.createIRI(CARGO.NAMESPACE, "loadedUnits");
		Loading = factory.createIRI(CARGO.NAMESPACE, "Loading");
		LOADING = factory.createIRI(CARGO.NAMESPACE, "LOADING");
		loadingActions = factory.createIRI(CARGO.NAMESPACE, "loadingActions");
		loadingIndicator = factory.createIRI(CARGO.NAMESPACE, "loadingIndicator");
		LoadingMaterial = factory.createIRI(CARGO.NAMESPACE, "LoadingMaterial");
		loadingPositionIdentifier = factory.createIRI(CARGO.NAMESPACE, "loadingPositionIdentifier");
		loadingType = factory.createIRI(CARGO.NAMESPACE, "loadingType");
		LoadingType = factory.createIRI(CARGO.NAMESPACE, "LoadingType");
		LoadingUnit = factory.createIRI(CARGO.NAMESPACE, "LoadingUnit");
		loadingUnit = factory.createIRI(CARGO.NAMESPACE, "loadingUnit");
		LoadType = factory.createIRI(CARGO.NAMESPACE, "LoadType");
		loadType = factory.createIRI(CARGO.NAMESPACE, "loadType");
		Location = factory.createIRI(CARGO.NAMESPACE, "Location");
		locationCodes = factory.createIRI(CARGO.NAMESPACE, "locationCodes");
		locationName = factory.createIRI(CARGO.NAMESPACE, "locationName");
		locationType = factory.createIRI(CARGO.NAMESPACE, "locationType");
		LogisticsAction = factory.createIRI(CARGO.NAMESPACE, "LogisticsAction");
		LogisticsActivity = factory.createIRI(CARGO.NAMESPACE, "LogisticsActivity");
		LogisticsAgent = factory.createIRI(CARGO.NAMESPACE, "LogisticsAgent");
		LogisticsEvent = factory.createIRI(CARGO.NAMESPACE, "LogisticsEvent");
		LogisticsObject = factory.createIRI(CARGO.NAMESPACE, "LogisticsObject");
		LogisticsService = factory.createIRI(CARGO.NAMESPACE, "LogisticsService");
		longitude = factory.createIRI(CARGO.NAMESPACE, "longitude");
		longText = factory.createIRI(CARGO.NAMESPACE, "longText");
		LOOSE = factory.createIRI(CARGO.NAMESPACE, "LOOSE");
		LoosePiece = factory.createIRI(CARGO.NAMESPACE, "LoosePiece");
		lotNumber = factory.createIRI(CARGO.NAMESPACE, "lotNumber");
		lowDispersibleIndicator = factory.createIRI(CARGO.NAMESPACE, "lowDispersibleIndicator");
		MAIN_CARRIAGE = factory.createIRI(CARGO.NAMESPACE, "MAIN_CARRIAGE");
		manufacturer = factory.createIRI(CARGO.NAMESPACE, "manufacturer");
		MASTER = factory.createIRI(CARGO.NAMESPACE, "MASTER");
		masterWaybill = factory.createIRI(CARGO.NAMESPACE, "masterWaybill");
		materialModel = factory.createIRI(CARGO.NAMESPACE, "materialModel");
		materialType = factory.createIRI(CARGO.NAMESPACE, "materialType");
		maximumQuantity = factory.createIRI(CARGO.NAMESPACE, "maximumQuantity");
		maxSegments = factory.createIRI(CARGO.NAMESPACE, "maxSegments");
		maxTemperature = factory.createIRI(CARGO.NAMESPACE, "maxTemperature");
		Measurement = factory.createIRI(CARGO.NAMESPACE, "Measurement");
		measurements = factory.createIRI(CARGO.NAMESPACE, "measurements");
		measurementTimestamp = factory.createIRI(CARGO.NAMESPACE, "measurementTimestamp");
		measurementValue = factory.createIRI(CARGO.NAMESPACE, "measurementValue");
		methodName = factory.createIRI(CARGO.NAMESPACE, "methodName");
		methodVersion = factory.createIRI(CARGO.NAMESPACE, "methodVersion");
		middleName = factory.createIRI(CARGO.NAMESPACE, "middleName");
		minimumQuantity = factory.createIRI(CARGO.NAMESPACE, "minimumQuantity");
		minTemperature = factory.createIRI(CARGO.NAMESPACE, "minTemperature");
		modeCode = factory.createIRI(CARGO.NAMESPACE, "modeCode");
		ModeQualifier = factory.createIRI(CARGO.NAMESPACE, "ModeQualifier");
		modeQualifier = factory.createIRI(CARGO.NAMESPACE, "modeQualifier");
		modularCheckNumber = factory.createIRI(CARGO.NAMESPACE, "modularCheckNumber");
		movementMilestone = factory.createIRI(CARGO.NAMESPACE, "movementMilestone");
		MovementTime = factory.createIRI(CARGO.NAMESPACE, "MovementTime");
		movementTimes = factory.createIRI(CARGO.NAMESPACE, "movementTimes");
		movementTimestamp = factory.createIRI(CARGO.NAMESPACE, "movementTimestamp");
		MovementTimeType = factory.createIRI(CARGO.NAMESPACE, "MovementTimeType");
		movementTimeType = factory.createIRI(CARGO.NAMESPACE, "movementTimeType");
		name = factory.createIRI(CARGO.NAMESPACE, "name");
		nbCorrections = factory.createIRI(CARGO.NAMESPACE, "nbCorrections");
		netWeightMeasure = factory.createIRI(CARGO.NAMESPACE, "netWeightMeasure");
		NONBOOKABLE = factory.createIRI(CARGO.NAMESPACE, "NONBOOKABLE");
		NonHumanActor = factory.createIRI(CARGO.NAMESPACE, "NonHumanActor");
		NOT_BOOKABLE = factory.createIRI(CARGO.NAMESPACE, "NOT_BOOKABLE");
		note = factory.createIRI(CARGO.NAMESPACE, "note");
		numberOfDoors = factory.createIRI(CARGO.NAMESPACE, "numberOfDoors");
		numberOfFittings = factory.createIRI(CARGO.NAMESPACE, "numberOfFittings");
		numberOfNets = factory.createIRI(CARGO.NAMESPACE, "numberOfNets");
		numberOfStraps = factory.createIRI(CARGO.NAMESPACE, "numberOfStraps");
		numericalValue = factory.createIRI(CARGO.NAMESPACE, "numericalValue");
		nvdForCarriage = factory.createIRI(CARGO.NAMESPACE, "nvdForCarriage");
		nvdForCustoms = factory.createIRI(CARGO.NAMESPACE, "nvdForCustoms");
		odlnCode = factory.createIRI(CARGO.NAMESPACE, "odlnCode");
		offerValidFrom = factory.createIRI(CARGO.NAMESPACE, "offerValidFrom");
		offerValidTo = factory.createIRI(CARGO.NAMESPACE, "offerValidTo");
		ofProduct = factory.createIRI(CARGO.NAMESPACE, "ofProduct");
		ofShipment = factory.createIRI(CARGO.NAMESPACE, "ofShipment");
		ON_CARRIAGE = factory.createIRI(CARGO.NAMESPACE, "ON_CARRIAGE");
		ON_REQUEST = factory.createIRI(CARGO.NAMESPACE, "ON_REQUEST");
		onsiteActions = factory.createIRI(CARGO.NAMESPACE, "onsiteActions");
		onTransportMeans = factory.createIRI(CARGO.NAMESPACE, "onTransportMeans");
		operatedTransportMovement = factory.createIRI(CARGO.NAMESPACE, "operatedTransportMovement");
		operatingTransportMeans = factory.createIRI(CARGO.NAMESPACE, "operatingTransportMeans");
		Organization = factory.createIRI(CARGO.NAMESPACE, "Organization");
		originator = factory.createIRI(CARGO.NAMESPACE, "originator");
		originReferencePermitDateTime = factory.createIRI(CARGO.NAMESPACE, "originReferencePermitDateTime");
		originReferencePermitId = factory.createIRI(CARGO.NAMESPACE, "originReferencePermitId");
		originReferencePermitTypeCode = factory.createIRI(CARGO.NAMESPACE, "originReferencePermitTypeCode");
		originTradeCountry = factory.createIRI(CARGO.NAMESPACE, "originTradeCountry");
		otherCharacteristics = factory.createIRI(CARGO.NAMESPACE, "otherCharacteristics");
		OtherCharge = factory.createIRI(CARGO.NAMESPACE, "OtherCharge");
		otherChargeAmount = factory.createIRI(CARGO.NAMESPACE, "otherChargeAmount");
		otherChargeCode = factory.createIRI(CARGO.NAMESPACE, "otherChargeCode");
		otherCharges = factory.createIRI(CARGO.NAMESPACE, "otherCharges");
		otherChargesIndicator = factory.createIRI(CARGO.NAMESPACE, "otherChargesIndicator");
		otherCustomsInformation = factory.createIRI(CARGO.NAMESPACE, "otherCustomsInformation");
		OtherIdentifier = factory.createIRI(CARGO.NAMESPACE, "OtherIdentifier");
		otherIdentifiers = factory.createIRI(CARGO.NAMESPACE, "otherIdentifiers");
		otherIdentifierType = factory.createIRI(CARGO.NAMESPACE, "otherIdentifierType");
		otherRegulatedEntities = factory.createIRI(CARGO.NAMESPACE, "otherRegulatedEntities");
		otherScreeningMethods = factory.createIRI(CARGO.NAMESPACE, "otherScreeningMethods");
		OUTBOUND = factory.createIRI(CARGO.NAMESPACE, "OUTBOUND");
		overpackCriticalitySafetyIndexNumeric = factory.createIRI(CARGO.NAMESPACE, "overpackCriticalitySafetyIndexNumeric");
		overpackIndicator = factory.createIRI(CARGO.NAMESPACE, "overpackIndicator");
		overpackT1 = factory.createIRI(CARGO.NAMESPACE, "overpackT1");
		overpackTypeCode = factory.createIRI(CARGO.NAMESPACE, "overpackTypeCode");
		ownerCode = factory.createIRI(CARGO.NAMESPACE, "ownerCode");
		owningOrganization = factory.createIRI(CARGO.NAMESPACE, "owningOrganization");
		packagedeIdentifier = factory.createIRI(CARGO.NAMESPACE, "packagedeIdentifier");
		packageMarkCoded = factory.createIRI(CARGO.NAMESPACE, "packageMarkCoded");
		packagingDangerLevelCode = factory.createIRI(CARGO.NAMESPACE, "packagingDangerLevelCode");
		packagingType = factory.createIRI(CARGO.NAMESPACE, "packagingType");
		PackagingType = factory.createIRI(CARGO.NAMESPACE, "PackagingType");
		packingInstructionNumber = factory.createIRI(CARGO.NAMESPACE, "packingInstructionNumber");
		PALLET = factory.createIRI(CARGO.NAMESPACE, "PALLET");
		parentOrganization = factory.createIRI(CARGO.NAMESPACE, "parentOrganization");
		partialEventIndicator = factory.createIRI(CARGO.NAMESPACE, "partialEventIndicator");
		partOfIotDevice = factory.createIRI(CARGO.NAMESPACE, "partOfIotDevice");
		Party = factory.createIRI(CARGO.NAMESPACE, "Party");
		partyDetails = factory.createIRI(CARGO.NAMESPACE, "partyDetails");
		partyRole = factory.createIRI(CARGO.NAMESPACE, "partyRole");
		passed = factory.createIRI(CARGO.NAMESPACE, "passed");
		PENDING = factory.createIRI(CARGO.NAMESPACE, "PENDING");
		performedAt = factory.createIRI(CARGO.NAMESPACE, "performedAt");
		permitTypeCode = factory.createIRI(CARGO.NAMESPACE, "permitTypeCode");
		permitTypeOtherDescription = factory.createIRI(CARGO.NAMESPACE, "permitTypeOtherDescription");
		Person = factory.createIRI(CARGO.NAMESPACE, "Person");
		PHONE_NUMBER = factory.createIRI(CARGO.NAMESPACE, "PHONE_NUMBER");
		physicalChemicalForm = factory.createIRI(CARGO.NAMESPACE, "physicalChemicalForm");
		PhysicalLogisticsObject = factory.createIRI(CARGO.NAMESPACE, "PhysicalLogisticsObject");
		Piece = factory.createIRI(CARGO.NAMESPACE, "Piece");
		pieceCountForRate = factory.createIRI(CARGO.NAMESPACE, "pieceCountForRate");
		PieceDg = factory.createIRI(CARGO.NAMESPACE, "PieceDg");
		PieceGroup = factory.createIRI(CARGO.NAMESPACE, "PieceGroup");
		pieceGroupCount = factory.createIRI(CARGO.NAMESPACE, "pieceGroupCount");
		pieceGroupGrossWeight = factory.createIRI(CARGO.NAMESPACE, "pieceGroupGrossWeight");
		pieceGroupId = factory.createIRI(CARGO.NAMESPACE, "pieceGroupId");
		pieceGroups = factory.createIRI(CARGO.NAMESPACE, "pieceGroups");
		pieceHeight = factory.createIRI(CARGO.NAMESPACE, "pieceHeight");
		pieceLength = factory.createIRI(CARGO.NAMESPACE, "pieceLength");
		PieceLiveAnimals = factory.createIRI(CARGO.NAMESPACE, "PieceLiveAnimals");
		pieces = factory.createIRI(CARGO.NAMESPACE, "pieces");
		pieceWeight = factory.createIRI(CARGO.NAMESPACE, "pieceWeight");
		pieceWidth = factory.createIRI(CARGO.NAMESPACE, "pieceWidth");
		PLANNED = factory.createIRI(CARGO.NAMESPACE, "PLANNED");
		postalCode = factory.createIRI(CARGO.NAMESPACE, "postalCode");
		postOfficeBox = factory.createIRI(CARGO.NAMESPACE, "postOfficeBox");
		PRE_CARRIAGE = factory.createIRI(CARGO.NAMESPACE, "PRE_CARRIAGE");
		preferredTransportId = factory.createIRI(CARGO.NAMESPACE, "preferredTransportId");
		prefix = factory.createIRI(CARGO.NAMESPACE, "prefix");
		PRESSURE = factory.createIRI(CARGO.NAMESPACE, "PRESSURE");
		Price = factory.createIRI(CARGO.NAMESPACE, "Price");
		price = factory.createIRI(CARGO.NAMESPACE, "price");
		priceReferenceId = factory.createIRI(CARGO.NAMESPACE, "priceReferenceId");
		priceSpecification = factory.createIRI(CARGO.NAMESPACE, "priceSpecification");
		Product = factory.createIRI(CARGO.NAMESPACE, "Product");
		productCode = factory.createIRI(CARGO.NAMESPACE, "productCode");
		productDescription = factory.createIRI(CARGO.NAMESPACE, "productDescription");
		ProductDg = factory.createIRI(CARGO.NAMESPACE, "ProductDg");
		productionCountry = factory.createIRI(CARGO.NAMESPACE, "productionCountry");
		productionCountryForRate = factory.createIRI(CARGO.NAMESPACE, "productionCountryForRate");
		productionDate = factory.createIRI(CARGO.NAMESPACE, "productionDate");
		properShippingName = factory.createIRI(CARGO.NAMESPACE, "properShippingName");
		PublicAuthority = factory.createIRI(CARGO.NAMESPACE, "PublicAuthority");
		quantity = factory.createIRI(CARGO.NAMESPACE, "quantity");
		quantityAnimals = factory.createIRI(CARGO.NAMESPACE, "quantityAnimals");
		quantityForUnitPrice = factory.createIRI(CARGO.NAMESPACE, "quantityForUnitPrice");
		question = factory.createIRI(CARGO.NAMESPACE, "question");
		Question = factory.createIRI(CARGO.NAMESPACE, "Question");
		questionNumber = factory.createIRI(CARGO.NAMESPACE, "questionNumber");
		questions = factory.createIRI(CARGO.NAMESPACE, "questions");
		questionSection = factory.createIRI(CARGO.NAMESPACE, "questionSection");
		QUEUED = factory.createIRI(CARGO.NAMESPACE, "QUEUED");
		qValueNumeric = factory.createIRI(CARGO.NAMESPACE, "qValueNumeric");
		ranges = factory.createIRI(CARGO.NAMESPACE, "ranges");
		Ranges = factory.createIRI(CARGO.NAMESPACE, "Ranges");
		rateCharge = factory.createIRI(CARGO.NAMESPACE, "rateCharge");
		rateClassCode = factory.createIRI(CARGO.NAMESPACE, "rateClassCode");
		rateClassCodeBasic = factory.createIRI(CARGO.NAMESPACE, "rateClassCodeBasic");
		ratePercentage = factory.createIRI(CARGO.NAMESPACE, "ratePercentage");
		ratings = factory.createIRI(CARGO.NAMESPACE, "ratings");
		Ratings = factory.createIRI(CARGO.NAMESPACE, "Ratings");
		rcp = factory.createIRI(CARGO.NAMESPACE, "rcp");
		reasonsForAdjustments = factory.createIRI(CARGO.NAMESPACE, "reasonsForAdjustments");
		receivedFrom = factory.createIRI(CARGO.NAMESPACE, "receivedFrom");
		recordedGeolocation = factory.createIRI(CARGO.NAMESPACE, "recordedGeolocation");
		recordingActor = factory.createIRI(CARGO.NAMESPACE, "recordingActor");
		recordingOrganization = factory.createIRI(CARGO.NAMESPACE, "recordingOrganization");
		referenceForObjects = factory.createIRI(CARGO.NAMESPACE, "referenceForObjects");
		referredBookingOption = factory.createIRI(CARGO.NAMESPACE, "referredBookingOption");
		regionCode = factory.createIRI(CARGO.NAMESPACE, "regionCode");
		RegulatedEntity = factory.createIRI(CARGO.NAMESPACE, "RegulatedEntity");
		regulatedEntityAcceptor = factory.createIRI(CARGO.NAMESPACE, "regulatedEntityAcceptor");
		regulatedEntityCategory = factory.createIRI(CARGO.NAMESPACE, "regulatedEntityCategory");
		regulatedEntityExpiryDate = factory.createIRI(CARGO.NAMESPACE, "regulatedEntityExpiryDate");
		regulatedEntityIdentifier = factory.createIRI(CARGO.NAMESPACE, "regulatedEntityIdentifier");
		regulatedEntityIssuer = factory.createIRI(CARGO.NAMESPACE, "regulatedEntityIssuer");
		REJECTED = factory.createIRI(CARGO.NAMESPACE, "REJECTED");
		remarks = factory.createIRI(CARGO.NAMESPACE, "remarks");
		remarksText = factory.createIRI(CARGO.NAMESPACE, "remarksText");
		reportableQuantity = factory.createIRI(CARGO.NAMESPACE, "reportableQuantity");
		REQUESTED = factory.createIRI(CARGO.NAMESPACE, "REQUESTED");
		requestMatch = factory.createIRI(CARGO.NAMESPACE, "requestMatch");
		resultOfCheck = factory.createIRI(CARGO.NAMESPACE, "resultOfCheck");
		resultValue = factory.createIRI(CARGO.NAMESPACE, "resultValue");
		salutation = factory.createIRI(CARGO.NAMESPACE, "salutation");
		SCHEDULED = factory.createIRI(CARGO.NAMESPACE, "SCHEDULED");
		screeningMethods = factory.createIRI(CARGO.NAMESPACE, "screeningMethods");
		seal = factory.createIRI(CARGO.NAMESPACE, "seal");
		sealNumber = factory.createIRI(CARGO.NAMESPACE, "sealNumber");
		SecurityDeclaration = factory.createIRI(CARGO.NAMESPACE, "SecurityDeclaration");
		securityDeclarations = factory.createIRI(CARGO.NAMESPACE, "securityDeclarations");
		securityStampId = factory.createIRI(CARGO.NAMESPACE, "securityStampId");
		securityStatus = factory.createIRI(CARGO.NAMESPACE, "securityStatus");
		Sensor = factory.createIRI(CARGO.NAMESPACE, "Sensor");
		sensorType = factory.createIRI(CARGO.NAMESPACE, "sensorType");
		SensorType = factory.createIRI(CARGO.NAMESPACE, "SensorType");
		sequenceNumber = factory.createIRI(CARGO.NAMESPACE, "sequenceNumber");
		serialNumber = factory.createIRI(CARGO.NAMESPACE, "serialNumber");
		servedActivity = factory.createIRI(CARGO.NAMESPACE, "servedActivity");
		servedServices = factory.createIRI(CARGO.NAMESPACE, "servedServices");
		serviceabilityCode = factory.createIRI(CARGO.NAMESPACE, "serviceabilityCode");
		serviceCode = factory.createIRI(CARGO.NAMESPACE, "serviceCode");
		serviceLevelCode = factory.createIRI(CARGO.NAMESPACE, "serviceLevelCode");
		shipment = factory.createIRI(CARGO.NAMESPACE, "shipment");
		Shipment = factory.createIRI(CARGO.NAMESPACE, "Shipment");
		shipperDeclarationText = factory.createIRI(CARGO.NAMESPACE, "shipperDeclarationText");
		shippingInfo = factory.createIRI(CARGO.NAMESPACE, "shippingInfo");
		shippingMarks = factory.createIRI(CARGO.NAMESPACE, "shippingMarks");
		shippingRefNo = factory.createIRI(CARGO.NAMESPACE, "shippingRefNo");
		shortName = factory.createIRI(CARGO.NAMESPACE, "shortName");
		shortText = factory.createIRI(CARGO.NAMESPACE, "shortText");
		signatoryCompany = factory.createIRI(CARGO.NAMESPACE, "signatoryCompany");
		signatoryRole = factory.createIRI(CARGO.NAMESPACE, "signatoryRole");
		signatureDate = factory.createIRI(CARGO.NAMESPACE, "signatureDate");
		signatures = factory.createIRI(CARGO.NAMESPACE, "signatures");
		signatureStatement = factory.createIRI(CARGO.NAMESPACE, "signatureStatement");
		signatureTypeCode = factory.createIRI(CARGO.NAMESPACE, "signatureTypeCode");
		skeletonIndicator = factory.createIRI(CARGO.NAMESPACE, "skeletonIndicator");
		slac = factory.createIRI(CARGO.NAMESPACE, "slac");
		slacForRate = factory.createIRI(CARGO.NAMESPACE, "slacForRate");
		specialConditions = factory.createIRI(CARGO.NAMESPACE, "specialConditions");
		specialFormIndicator = factory.createIRI(CARGO.NAMESPACE, "specialFormIndicator");
		specialHandlingCodes = factory.createIRI(CARGO.NAMESPACE, "specialHandlingCodes");
		specialProvisionId = factory.createIRI(CARGO.NAMESPACE, "specialProvisionId");
		specialServiceRequests = factory.createIRI(CARGO.NAMESPACE, "specialServiceRequests");
		speciesCommonName = factory.createIRI(CARGO.NAMESPACE, "speciesCommonName");
		speciesScientificName = factory.createIRI(CARGO.NAMESPACE, "speciesScientificName");
		specimenDescription = factory.createIRI(CARGO.NAMESPACE, "specimenDescription");
		specimenTypeCode = factory.createIRI(CARGO.NAMESPACE, "specimenTypeCode");
		stackable = factory.createIRI(CARGO.NAMESPACE, "stackable");
		station = factory.createIRI(CARGO.NAMESPACE, "station");
		StationRemarks = factory.createIRI(CARGO.NAMESPACE, "StationRemarks");
		stationRemarks = factory.createIRI(CARGO.NAMESPACE, "stationRemarks");
		statusBookingOption = factory.createIRI(CARGO.NAMESPACE, "statusBookingOption");
		Storage = factory.createIRI(CARGO.NAMESPACE, "Storage");
		storagePlaceIdentifier = factory.createIRI(CARGO.NAMESPACE, "storagePlaceIdentifier");
		STORE_IN = factory.createIRI(CARGO.NAMESPACE, "STORE_IN");
		STORE_OUT = factory.createIRI(CARGO.NAMESPACE, "STORE_OUT");
		storedObjects = factory.createIRI(CARGO.NAMESPACE, "storedObjects");
		Storing = factory.createIRI(CARGO.NAMESPACE, "Storing");
		storingActions = factory.createIRI(CARGO.NAMESPACE, "storingActions");
		storingIdentifier = factory.createIRI(CARGO.NAMESPACE, "storingIdentifier");
		storingType = factory.createIRI(CARGO.NAMESPACE, "storingType");
		StoringType = factory.createIRI(CARGO.NAMESPACE, "StoringType");
		streetAddressLines = factory.createIRI(CARGO.NAMESPACE, "streetAddressLines");
		subjectCode = factory.createIRI(CARGO.NAMESPACE, "subjectCode");
		subLocationOf = factory.createIRI(CARGO.NAMESPACE, "subLocationOf");
		subLocations = factory.createIRI(CARGO.NAMESPACE, "subLocations");
		subOrganization = factory.createIRI(CARGO.NAMESPACE, "subOrganization");
		subTotal = factory.createIRI(CARGO.NAMESPACE, "subTotal");
		supplementaryInfoPrefix = factory.createIRI(CARGO.NAMESPACE, "supplementaryInfoPrefix");
		supplementaryInfoSuffix = factory.createIRI(CARGO.NAMESPACE, "supplementaryInfoSuffix");
		tareWeight = factory.createIRI(CARGO.NAMESPACE, "tareWeight");
		targetCountry = factory.createIRI(CARGO.NAMESPACE, "targetCountry");
		taxDueAgent = factory.createIRI(CARGO.NAMESPACE, "taxDueAgent");
		taxDueAirline = factory.createIRI(CARGO.NAMESPACE, "taxDueAirline");
		technicalName = factory.createIRI(CARGO.NAMESPACE, "technicalName");
		TELEX = factory.createIRI(CARGO.NAMESPACE, "TELEX");
		TemperatureInstructions = factory.createIRI(CARGO.NAMESPACE, "TemperatureInstructions");
		temperatureInstructions = factory.createIRI(CARGO.NAMESPACE, "temperatureInstructions");
		temperatureUnit = factory.createIRI(CARGO.NAMESPACE, "temperatureUnit");
		templatePurpose = factory.createIRI(CARGO.NAMESPACE, "templatePurpose");
		text = factory.createIRI(CARGO.NAMESPACE, "text");
		textualHandlingInstructions = factory.createIRI(CARGO.NAMESPACE, "textualHandlingInstructions");
		textualValue = factory.createIRI(CARGO.NAMESPACE, "textualValue");
		THERMOMETER = factory.createIRI(CARGO.NAMESPACE, "THERMOMETER");
		TILT = factory.createIRI(CARGO.NAMESPACE, "TILT");
		timeOfAvailability = factory.createIRI(CARGO.NAMESPACE, "timeOfAvailability");
		timePreferences = factory.createIRI(CARGO.NAMESPACE, "timePreferences");
		totalDimensions = factory.createIRI(CARGO.NAMESPACE, "totalDimensions");
		totalGrossWeight = factory.createIRI(CARGO.NAMESPACE, "totalGrossWeight");
		totalTransitTime = factory.createIRI(CARGO.NAMESPACE, "totalTransitTime");
		totalVolume = factory.createIRI(CARGO.NAMESPACE, "totalVolume");
		totalVolumetricWeight = factory.createIRI(CARGO.NAMESPACE, "totalVolumetricWeight");
		transactionPurpose = factory.createIRI(CARGO.NAMESPACE, "transactionPurpose");
		transactionPurposeCode = factory.createIRI(CARGO.NAMESPACE, "transactionPurposeCode");
		transportContractId = factory.createIRI(CARGO.NAMESPACE, "transportContractId");
		transportContractTypeCode = factory.createIRI(CARGO.NAMESPACE, "transportContractTypeCode");
		transportIdentifier = factory.createIRI(CARGO.NAMESPACE, "transportIdentifier");
		transportIndexNumeric = factory.createIRI(CARGO.NAMESPACE, "transportIndexNumeric");
		transportLegs = factory.createIRI(CARGO.NAMESPACE, "transportLegs");
		TransportLegs = factory.createIRI(CARGO.NAMESPACE, "TransportLegs");
		TransportMeans = factory.createIRI(CARGO.NAMESPACE, "TransportMeans");
		transportMeansServiceType = factory.createIRI(CARGO.NAMESPACE, "transportMeansServiceType");
		transportMeansType = factory.createIRI(CARGO.NAMESPACE, "transportMeansType");
		TransportMovement = factory.createIRI(CARGO.NAMESPACE, "TransportMovement");
		transportOrganization = factory.createIRI(CARGO.NAMESPACE, "transportOrganization");
		turnable = factory.createIRI(CARGO.NAMESPACE, "turnable");
		typeCode = factory.createIRI(CARGO.NAMESPACE, "typeCode");
		typicalCo2Coefficient = factory.createIRI(CARGO.NAMESPACE, "typicalCo2Coefficient");
		typicalFuelConsumption = factory.createIRI(CARGO.NAMESPACE, "typicalFuelConsumption");
		ULD = factory.createIRI(CARGO.NAMESPACE, "ULD");
		ULDBasicPiece = factory.createIRI(CARGO.NAMESPACE, "ULDBasicPiece");
		uldContourCode = factory.createIRI(CARGO.NAMESPACE, "uldContourCode");
		uldLoadingIndicator = factory.createIRI(CARGO.NAMESPACE, "uldLoadingIndicator");
		uldOwnerCode = factory.createIRI(CARGO.NAMESPACE, "uldOwnerCode");
		uldRateClassType = factory.createIRI(CARGO.NAMESPACE, "uldRateClassType");
		uldSerialNumber = factory.createIRI(CARGO.NAMESPACE, "uldSerialNumber");
		ULDSpecificPiece = factory.createIRI(CARGO.NAMESPACE, "ULDSpecificPiece");
		uldTareWeightForRate = factory.createIRI(CARGO.NAMESPACE, "uldTareWeightForRate");
		uldType = factory.createIRI(CARGO.NAMESPACE, "uldType");
		uldTypeCode = factory.createIRI(CARGO.NAMESPACE, "uldTypeCode");
		uniqueIdentifier = factory.createIRI(CARGO.NAMESPACE, "uniqueIdentifier");
		unit = factory.createIRI(CARGO.NAMESPACE, "unit");
		UNIT_LOAD_DEVICE = factory.createIRI(CARGO.NAMESPACE, "UNIT_LOAD_DEVICE");
		unitBasis = factory.createIRI(CARGO.NAMESPACE, "unitBasis");
		UnitComposition = factory.createIRI(CARGO.NAMESPACE, "UnitComposition");
		unitPrice = factory.createIRI(CARGO.NAMESPACE, "unitPrice");
		UnitsPreference = factory.createIRI(CARGO.NAMESPACE, "UnitsPreference");
		unitsPreference = factory.createIRI(CARGO.NAMESPACE, "unitsPreference");
		UNLOADING = factory.createIRI(CARGO.NAMESPACE, "UNLOADING");
		unNumber = factory.createIRI(CARGO.NAMESPACE, "unNumber");
		UNPLANNED_STOP = factory.createIRI(CARGO.NAMESPACE, "UNPLANNED_STOP");
		updateBookingOptionRequests = factory.createIRI(CARGO.NAMESPACE, "updateBookingOptionRequests");
		upid = factory.createIRI(CARGO.NAMESPACE, "upid");
		usedInCheck = factory.createIRI(CARGO.NAMESPACE, "usedInCheck");
		usedTemplate = factory.createIRI(CARGO.NAMESPACE, "usedTemplate");
		usedToDateQuotaQuantity = factory.createIRI(CARGO.NAMESPACE, "usedToDateQuotaQuantity");
		validFrom = factory.createIRI(CARGO.NAMESPACE, "validFrom");
		validUntil = factory.createIRI(CARGO.NAMESPACE, "validUntil");
		Value = factory.createIRI(CARGO.NAMESPACE, "Value");
		vatIndicator = factory.createIRI(CARGO.NAMESPACE, "vatIndicator");
		vehicleModel = factory.createIRI(CARGO.NAMESPACE, "vehicleModel");
		vehicleRegistration = factory.createIRI(CARGO.NAMESPACE, "vehicleRegistration");
		vehicleSize = factory.createIRI(CARGO.NAMESPACE, "vehicleSize");
		vehicleType = factory.createIRI(CARGO.NAMESPACE, "vehicleType");
		version = factory.createIRI(CARGO.NAMESPACE, "version");
		VIBRATION = factory.createIRI(CARGO.NAMESPACE, "VIBRATION");
		volume = factory.createIRI(CARGO.NAMESPACE, "volume");
		VolumePieceGroup = factory.createIRI(CARGO.NAMESPACE, "VolumePieceGroup");
		VolumetricWeight = factory.createIRI(CARGO.NAMESPACE, "VolumetricWeight");
		volumetricWeight = factory.createIRI(CARGO.NAMESPACE, "volumetricWeight");
		volumetricWeightForRate = factory.createIRI(CARGO.NAMESPACE, "volumetricWeightForRate");
		volumeUnit = factory.createIRI(CARGO.NAMESPACE, "volumeUnit");
		waybill = factory.createIRI(CARGO.NAMESPACE, "waybill");
		Waybill = factory.createIRI(CARGO.NAMESPACE, "Waybill");
		WaybillLineItem = factory.createIRI(CARGO.NAMESPACE, "WaybillLineItem");
		waybillLineItems = factory.createIRI(CARGO.NAMESPACE, "waybillLineItems");
		waybillNumber = factory.createIRI(CARGO.NAMESPACE, "waybillNumber");
		waybillPrefix = factory.createIRI(CARGO.NAMESPACE, "waybillPrefix");
		waybillType = factory.createIRI(CARGO.NAMESPACE, "waybillType");
		WaybillType = factory.createIRI(CARGO.NAMESPACE, "WaybillType");
		WEBSITE = factory.createIRI(CARGO.NAMESPACE, "WEBSITE");
		weight = factory.createIRI(CARGO.NAMESPACE, "weight");
		weightUnit = factory.createIRI(CARGO.NAMESPACE, "weightUnit");
		weightValuationIndicator = factory.createIRI(CARGO.NAMESPACE, "weightValuationIndicator");
		width = factory.createIRI(CARGO.NAMESPACE, "width");
	}

	private CARGO() {
		//static access only
	}

}
