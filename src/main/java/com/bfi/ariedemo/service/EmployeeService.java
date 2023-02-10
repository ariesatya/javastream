package com.bfi.ariedemo.service;

import com.bfi.ariedemo.domain.Employee;
import com.bfi.ariedemo.dto.EmployeeDTO;
import com.bfi.ariedemo.exception.BadRequestException;
import com.bfi.ariedemo.mapper.EmployeeMapper;
import com.bfi.ariedemo.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Employee}.
 */
@Service
@Transactional
@SuppressWarnings("unused")
public class EmployeeService {

  private final Logger log = LoggerFactory.getLogger(EmployeeService.class);

  private final EmployeeRepository employeeRepository;

  private final EmployeeMapper employeeMapper;

  public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
    this.employeeRepository = employeeRepository;
    this.employeeMapper = employeeMapper;
  }

  /**
   * Save a employee.
   *
   * @param employeeDTO the entity to save.
   * @return the persisted entity.
   */
  public EmployeeDTO save(EmployeeDTO employeeDTO) {
    log.debug("Request to save Employee : {}", employeeDTO);
    Employee employee = employeeMapper.toEntity(employeeDTO);
    employee = employeeRepository.save(employee);
    return employeeMapper.toDto(employee);
  }

  /**
   * Update a employee.
   *
   * @param employeeDTO the entity to save.
   * @return the persisted entity.
   */
  public EmployeeDTO update(EmployeeDTO employeeDTO) {
    log.debug("Request to update Employee : {}", employeeDTO);
    Employee employee = employeeMapper.toEntity(employeeDTO);
    employee = employeeRepository.save(employee);
    return employeeMapper.toDto(employee);
  }

  /**
   * Partially update an employee.
   *
   * @param employeeDTO the entity to update partially.
   * @return the persisted entity.
   */
  public EmployeeDTO partialUpdate(EmployeeDTO employeeDTO) {
    log.debug("Request to partially update Employee : {}", employeeDTO);
    return employeeRepository
      .findById(employeeDTO.getId())
      .map(existingEmployee -> {
        employeeMapper.partialUpdate(existingEmployee, employeeDTO);
        return existingEmployee;
      })
      .map(employeeRepository::save)
      .map(employeeMapper::toDto)
      .orElse(null);
  }

  /**
   * Get all the employees.
   *
   * @param status the status employee.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<EmployeeDTO> findAll(Boolean status) {
    log.debug("Request to get all Employees");
    return employeeRepository.findAll()
      .stream()
      .filter(employee -> status.equals(employee.isStatus()))
      .map(employeeMapper::toDto)
      .collect(Collectors.toList());
  }

  /**
   * Get one employee by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */

  @Transactional(readOnly = true)
  public EmployeeDTO findOne(String id) {
    log.debug("Request to get Employee : {}", id);
    return employeeMapper.toDto(getEmployeeById(id));
  }

  /**
   * Delete the employee by id.
   *
   * @param id the id of the entity.
   */
  public void delete(String id) {
    log.debug("Request to delete Employee : {}", id);
    var employee = getEmployeeById(id);
    employee.setStatus(false);
    employeeRepository.save(employee);
  }

  private Employee getEmployeeById(String id) {
    log.debug("Get Employee by id : {}", id);
    return employeeRepository.findById(id)
      .orElseThrow(() -> new BadRequestException("Not Found"));
  }
}
