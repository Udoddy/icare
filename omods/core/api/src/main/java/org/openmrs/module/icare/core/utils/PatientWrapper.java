package org.openmrs.module.icare.core.utils;

import org.openmrs.*;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.attribute.AttributeType;
import org.openmrs.module.icare.ICareConfig;
import org.openmrs.module.icare.billing.services.insurance.nhif.NHIFConfig;

import javax.naming.ConfigurationException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientWrapper {
	
	Patient patient;
	
	public PatientWrapper(Patient patient) {
		this.patient = patient;
	}
	
	private String getAttribute(String attributeConfig) throws ConfigurationException {
		String attributeValue = null;
		AdministrationService adminService = Context.getService(AdministrationService.class);
		String insuranceAttributeUuid = adminService.getGlobalProperty(attributeConfig);
		if (insuranceAttributeUuid == null) {
			throw new ConfigurationException("Attribute ID is configured. Please set '" + attributeConfig + "'");
		}
		VisitService visitService = Context.getService(VisitService.class);
		List<VisitAttributeType> visitAttributeTypes = visitService.getAllVisitAttributeTypes();
		for (PersonAttribute attribute : this.patient.getAttributes()) {
			PersonAttributeType attributeType = attribute.getAttributeType();
			for (VisitAttributeType visitAttributeType : visitAttributeTypes) {
				if (visitAttributeType.getUuid().equals(attributeType.getUuid())) {
					if (visitAttributeType.getUuid().equals(insuranceAttributeUuid)) { //CASH OR Insurance
						attributeValue = (String) attribute.getValue();
					}
				}
			}
		}
		return attributeValue;
	}
	
	public String getFileNumber() throws ConfigurationException {
		String identifierValue = "";
		AdministrationService adminService = Context.getService(AdministrationService.class);
		String patientFileNumberIdentifier = adminService.getGlobalProperty(NHIFConfig.PATIENT_FILENUMBER_IDENTIFIER);
		if (patientFileNumberIdentifier == null) {
			throw new ConfigurationException("Patient File Number Identifier is not configured. Please check "
			        + NHIFConfig.PATIENT_FILENUMBER_IDENTIFIER + ".");
		}
		for (PatientIdentifier patientIdentifier : this.patient.getIdentifiers()) {
			if (patientIdentifier.getIdentifierType().getUuid().equals(patientFileNumberIdentifier)) {
				identifierValue = patientIdentifier.getIdentifier();
			}
		}
		return identifierValue;
	}
	
	public String getAddress() {
		String address = "";
		if (this.patient.getPersonAddress() != null) {
			if (this.patient.getPersonAddress().getAddress1() != null) {
				address += this.patient.getPersonAddress().getAddress1();
			}
			if (this.patient.getPersonAddress().getCountyDistrict() != null) {
				if (!address.equals("")) {
					address += ", ";
				}
				address += this.patient.getPersonAddress().getCountyDistrict();
			}
			if (this.patient.getPersonAddress().getStateProvince() != null) {
				if (!address.equals("")) {
					address += ", ";
				}
				address += this.patient.getPersonAddress().getStateProvince();
			}
			if (this.patient.getPersonAddress().getCityVillage() != null) {
				if (!address.equals("")) {
					address += ", ";
				}
				address += this.patient.getPersonAddress().getCityVillage();
			}
		}
		return address;
	}
	
	public String getPhoneNumber() throws ConfigurationException {
		String phoneNumber = this.getAttribute(ICareConfig.PHONE_NUMBER_ATTRIBUTE);
		if (phoneNumber != null) {
			return phoneNumber;
		} else {
			return "";
		}
	}
	
	public String getOccupation() throws ConfigurationException {
		String signature = this.getAttribute(ICareConfig.PATIENT_OCCUPATION_ATTRIBUTE);
		if (signature != null) {
			return signature;
		} else {
			return "";
		}
	}
	
	public String getFullName() {
		return this.patient.getPersonName().getFullName();
	}
	
	public Patient getPatient() {
		return this.patient;
	}
	
	public Object toMap() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Map<String, Object> patientMap = new HashMap<>();
		patientMap.put("uuid", patient.getUuid());
		patientMap.put("void", patient.getVoided());

		Map<String, Object> personMap = new HashMap<>();
		personMap.put("uuid", patient.getPerson().getUuid());
		personMap.put("void", patient.getPerson().getVoided());
		personMap.put("age", patient.getPerson().getAge());
		personMap.put("birthdate", dateFormat.format(patient.getPerson().getBirthdate()));
		personMap.put("display", patient.getPerson().getPersonName().getFullName());
		personMap.put("gender", patient.getPerson().getGender());
		personMap.put("birthdateEstimated", patient.getPerson().getBirthdateEstimated());
		personMap.put("dead", patient.getPerson().getDead());
		personMap.put("deathdateEstimated", patient.getPerson().getDeathdateEstimated());
		personMap.put("causeOfDeath", patient.getPerson().getCauseOfDeathNonCoded());

		List<Map<String, Object>> attributesMap = new ArrayList<>();
		for(PersonAttribute attribute:patient.getPerson().getAttributes()){
			Map<String, Object> attributeMap = new HashMap<>();
			attributeMap.put("uuid", attribute.getUuid());
			attributeMap.put("display", attribute.getValue());
			attributesMap.add(attributeMap);
		}
		personMap.put("attributes", attributesMap);

		List<Map<String, Object>> identifiersMap = new ArrayList<>();
		for(PatientIdentifier identifier:patient.getIdentifiers()){
			Map<String, Object> attributeMap = new HashMap<>();
			attributeMap.put("uuid", identifier.getUuid());
			attributeMap.put("identifier", identifier.getIdentifier());
			attributeMap.put("preferred", identifier.getPreferred());
			attributeMap.put("void", identifier.getVoided());

			Map<String, Object> identifierType = new HashMap<>();
			identifierType.put("uuid", identifier.getIdentifierType().getUuid());
			identifierType.put("display", identifier.getIdentifierType().getName());
			attributeMap.put("identifierType", identifierType);
			identifiersMap.add(attributeMap);
		}
		patientMap.put("identifiers", identifiersMap);

		patientMap.put("person", personMap);
		return patientMap;
    }
	
	public CharSequence getEmail() {
		return "";
	}
}