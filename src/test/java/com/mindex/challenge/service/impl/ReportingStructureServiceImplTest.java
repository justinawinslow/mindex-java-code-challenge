package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String readReportingStructureUrl;
    private Employee testEmployee;

    @Autowired
    private EmployeeRepository employeeRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        readReportingStructureUrl = "http://localhost:" + port + "/reporting/{id}";
        testEmployee = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
    }

    @Test
    public void testCreateReadUpdate() {
        ReportingStructure testReportingStructure = new ReportingStructure(testEmployee, 4);

        // Read checks
        ResponseEntity<ReportingStructure> readReportingStructureResponse = restTemplate.getForEntity(readReportingStructureUrl,
                ReportingStructure.class, testEmployee.getEmployeeId());
        assertEquals(HttpStatus.OK, readReportingStructureResponse.getStatusCode());
        ReportingStructure readReportingStructure = readReportingStructureResponse.getBody();
        assertNotNull(readReportingStructure);
        assertEquals(readReportingStructure.getEmployee().getEmployeeId(), testReportingStructure.getEmployee().getEmployeeId());
        assertEquals(readReportingStructure.getNumberOfReports(), testReportingStructure.getNumberOfReports());
    }

}
