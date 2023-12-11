package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    private final CompensationRepository compensationRepository;
    private final EmployeeService employeeService;

    @Autowired
    public CompensationServiceImpl(CompensationRepository compensationRepository, EmployeeService employeeService) {
        this.compensationRepository = compensationRepository;
        this.employeeService = employeeService;
    }

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);
        if (compensation.getEffectiveDate() == null) {
            compensation.setEffectiveDate(new Date());
        }
        compensationRepository.insert(compensation);
        return compensation;
    }

    @Override
    public Compensation read(String employeeId) {
        LOG.debug("Getting compensation with employee id [{}]", employeeId);
        try {
            Employee employee = employeeService.read(employeeId);
            Compensation compensation = compensationRepository.findByEmployee(employee);

            if (compensation == null) {
                throw new RuntimeException("No compensation found for employee id: [" + employeeId + "]");
            }

            return compensation;
        } catch (RuntimeException ex) {
            LOG.error("Error getting compensation for employee id [" + employeeId + "]", ex);
            throw new RuntimeException("Failed to retrieve compensation for employee id: [" + employeeId + "]");
        }
    }
}
