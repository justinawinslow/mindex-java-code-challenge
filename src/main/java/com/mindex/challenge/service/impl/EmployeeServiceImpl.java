package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Getting employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public ReportingStructure generateReportingStructure(String id) {
        LOG.debug("Generating reporting structure for employee id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new RuntimeException("Failed to generate reporting structure for employee id: [" + id + "]");
        }
        return new ReportingStructure(employee, calculateTotalDirectReports(employee));
    }

    private int calculateTotalDirectReports(Employee employee) {
        int numOfDirectReports = 0;

        List<Employee> directReports = employee.getDirectReports();
        if (directReports != null) {
            numOfDirectReports += directReports.size();

            for (Employee directReport : directReports) {
                if (directReport != null) {
                    Employee directReportEmployee = readEmployeeIfNecessary(directReport);
                    numOfDirectReports += calculateTotalDirectReports(directReportEmployee);
                }
            }
        }

        return numOfDirectReports;
    }

    private Employee readEmployeeIfNecessary(Employee employee) {
        return employee.getFirstName() == null ? employeeRepository.findByEmployeeId(employee.getEmployeeId()) : employee;
    }
}
