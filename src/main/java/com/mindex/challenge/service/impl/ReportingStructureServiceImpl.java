package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeService employeeService;
    @Override
    public ReportingStructure generate(String employeeId) {
        LOG.debug("Generating reporting structure for employee id [{}]", employeeId);
        Employee employee = employeeService.read(employeeId);
        return new ReportingStructure(employee, findNumOfDirectReports(employee));
    }

    private int findNumOfDirectReports(Employee employee) {
        int numOfDirectReports = 0;
        List<Employee> directReports = employee.getDirectReports();
        if (directReports != null) {
            numOfDirectReports = directReports.size();
            for (Employee directReport : directReports) {
                numOfDirectReports += findNumOfDirectReports(directReport);
            }
        }
        return numOfDirectReports;
    }
}
