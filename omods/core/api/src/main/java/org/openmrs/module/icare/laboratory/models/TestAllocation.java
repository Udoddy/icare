package org.openmrs.module.icare.laboratory.models;

// Generated Oct 7, 2020 12:48:40 PM by Hibernate Tools 5.2.10.Final

import org.hibernate.annotations.GenericGenerator;
import org.openmrs.BaseOpenmrsData;
import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.Order;

import javax.persistence.*;
import java.util.*;

/**
 * LbTestAllocation generated by hbm2java
 */
@Entity
@Table(name = "lb_test_allocation")
public class TestAllocation extends BaseOpenmrsData implements java.io.Serializable {
	
	@Id
	@GeneratedValue()
	@Column(name = "test_allocation_id", unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_id")
	private Device device;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "sample_id", referencedColumnName = "sample_id", nullable = false),
	        @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false) })
	private SampleOrder sampleOrder;
	
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sample_id")
	private Sample sample;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;*/
	
	@Column(name = "label", length = 65535)
	private String label;
	
	@ManyToOne
	@JoinColumn(name = "container_id")
	private Concept container;
	
	@ManyToOne
	@JoinColumn(name = "concept_id")
	private Concept testConcept;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "testAllocation")
	private List<TestAllocationStatus> testAllocationStatuses = new ArrayList<TestAllocationStatus>(0);
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "testAllocation")
	private List<Result> testAllocationResults = new ArrayList<Result>(0);
	
	public Concept getTestConcept() {
		return this.testConcept;
	}
	
	public void setTestConcept(Concept testConcept) {
		this.testConcept = testConcept;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public SampleOrder getSampleOrder() {
		return sampleOrder;
	}
	
	public Sample getSample() {
		System.out.println("TESTSTSTST");
		return this.sampleOrder.getSample();
	}
	
	public void setSampleOrder(SampleOrder sampleOrder) {
		this.sampleOrder = sampleOrder;
	}
	
	public List<TestAllocationStatus> getTestAllocationStatuses() {
		return testAllocationStatuses;
	}
	
	public void setTestAllocationStatuses(List<TestAllocationStatus> testAllocationStatuses) {
		this.testAllocationStatuses = testAllocationStatuses;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public List<Result> getTestAllocationResults() {
		return testAllocationResults;
	}
	
	public void setTestAllocationResults(List<Result> testAllocationResults) {
		this.testAllocationResults = testAllocationResults;
	}
	
	public Concept getContainer() {
		return container;
	}
	
	public void setContainer(Concept container) {
		this.container = container;
	}
	
	public static TestAllocation fromMap(Map<String, Object> map) {
		Concept containerConcept = new Concept();
		containerConcept.setUuid(((Map<String, Object>) map.get("container")).get("uuid").toString());
		
		Concept testConcept = new Concept();
		if (map.get("concept") != null && ((Map<String, Object>) map.get("concept")).get("uuid") != null) {
			testConcept.setUuid(((Map<String, Object>) map.get("concept")).get("uuid").toString());
		}
		
		Order order = new Order();
		order.setUuid(((Map<String, Object>) map.get("order")).get("uuid").toString());
		
		Sample sample = new Sample();
		sample.setUuid(((Map<String, Object>) map.get("sample")).get("uuid").toString());
		
		SampleOrder sampleOrder = new SampleOrder();
		sampleOrder.setSample(sample);
		sampleOrder.setOrder(order);
		
		TestAllocation testAllocation = new TestAllocation();
		testAllocation.setLabel(map.get("label").toString());
		testAllocation.setContainer(containerConcept);
		testAllocation.setSampleOrder(sampleOrder);
		if (testConcept != null) {
			testAllocation.setTestConcept(testConcept);
		}
		
		return testAllocation;
	}
	
	public Map<String, Object> toMap() {
		Map<String, Object> testAllocationMap = new HashMap<String, Object>();
		testAllocationMap.put("uuid", this.getUuid());
		testAllocationMap.put("label", this.getLabel());
		
		if (this.getContainer() != null) {
			Map<String, Object> containerMap = new HashMap<String, Object>();
			containerMap.put("uuid", this.getContainer().getUuid());
			containerMap.put("display", this.getContainer().getDisplayString());
			testAllocationMap.put("container", containerMap);
		}
		
		if (this.getTestConcept() != null) {
			Map<String, Object> testConceptMap = new HashMap<String, Object>();
			testConceptMap.put("uuid", this.getTestConcept().getUuid());
			testConceptMap.put("display", this.getTestConcept().getDisplayString());
			//			testConceptMap.put("names", this.getTestConcept().getNames());
			//			testConceptMap.put("shortNames", this.getTestConcept().getShortNames());
			testAllocationMap.put("concept", testConceptMap);
			testAllocationMap.put("parameter", testConceptMap);
		}
		
		List<Map<String, Object>> testAllocationStatusMap = new ArrayList<Map<String, Object>>();
		for (TestAllocationStatus status : this.getTestAllocationStatuses()) {
			testAllocationStatusMap.add(status.toMap());
		}
		testAllocationMap.put("statuses", testAllocationStatusMap);
		
		List<Map<String, Object>> resultssMap = new ArrayList<Map<String, Object>>();
		for (Result result : this.getTestAllocationResults()) {
			resultssMap.add(result.toMap());
		}
		testAllocationMap.put("results", resultssMap);
		Map<String, Object> order = new HashMap<String, Object>();
		order.put("uuid", this.sampleOrder.getOrder().getUuid());
		order.put("orderer", this.sampleOrder.getOrder().getOrderer().getName());
		order.put("concept", this.sampleOrder.getOrder().getConcept().getUuid());
		testAllocationMap.put("order", order);
		
		Map<String, Object> sample = new HashMap<String, Object>();
		sample.put("uuid", this.sampleOrder.getSample().getUuid());
		sample.put("label", this.sampleOrder.getSample().getLabel());
		testAllocationMap.put("sample", sample);
		return testAllocationMap;
	}
	
	public Device getDevice() {
		return device;
	}
	
	public void setDevice(Device device) {
		this.device = device;
	}
}
