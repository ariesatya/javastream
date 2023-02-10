package com.bfi.ariedemo.controller;

import com.bfi.ariedemo.domain.Employee;
import com.bfi.ariedemo.dto.EmployeeDTO;
import com.bfi.ariedemo.dto.GeneralWrapper;
import com.bfi.ariedemo.exception.BadRequestException;
import com.bfi.ariedemo.repository.EmployeeRepository;
import com.bfi.ariedemo.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

/**
 * REST controller for managing {@link Employee}.
 */
@RestController
@RequestMapping("/api")
@SuppressWarnings("unused")
public class EmployeeResource {

  private final Logger log = LoggerFactory.getLogger(EmployeeResource.class);

  private static final String ENTITY_NAME = "employee";

  private static final String INVALID_ID = "Invalid id";


  private final EmployeeService employeeService;

  private final EmployeeRepository employeeRepository;

  public EmployeeResource(EmployeeService employeeService, EmployeeRepository employeeRepository) {
    this.employeeService = employeeService;
    this.employeeRepository = employeeRepository;
  }

  /**
   * {@code POST  /employees} : Create a new employee.
   *
   * @param employeeDTO the employeeDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeDTO, or with status {@code 400 (Bad Request)} if the employee has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/employees")
  public ResponseEntity<GeneralWrapper<EmployeeDTO>> createEmployee(@RequestBody EmployeeDTO employeeDTO) throws URISyntaxException {
    log.debug("REST request to save Employee : {}", employeeDTO);
    if (employeeDTO.getId() != null) {
      throw new BadRequestException("A new employee cannot already have an ID");
    }
    EmployeeDTO result = employeeService.save(employeeDTO);
    return ResponseEntity
      .created(new URI("/api/employees/" + result.getId()))
      .body(new GeneralWrapper<>(result).success(HttpStatus.CREATED));
  }

  /**
   * {@code PUT  /employees/:id} : Updates an existing employee.
   *
   * @param id          the id of the employeeDTO to save.
   * @param employeeDTO the employeeDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeDTO,
   * or with status {@code 400 (Bad Request)} if the employeeDTO is not valid,
   * or with status {@code 500 (Internal Server Error)} if the employeeDTO couldn't be updated.å
   */
  @PutMapping("/employees/{id}")
  public ResponseEntity<GeneralWrapper<EmployeeDTO>> updateEmployee(
    @PathVariable(value = "id", required = false) final String id,
    @RequestBody EmployeeDTO employeeDTO
  ) {
    log.debug("REST request to update Employee : {}, {}", id, employeeDTO);
    if (employeeDTO.getId() == null) {
      throw new BadRequestException(INVALID_ID);
    }
    if (!Objects.equals(id, employeeDTO.getId())) {
      throw new BadRequestException(INVALID_ID);
    }

    if (!employeeRepository.existsById(id)) {
      throw new BadRequestException("Entity not found");
    }

    EmployeeDTO result = employeeService.update(employeeDTO);
    return ResponseEntity
      .ok()
      .body(new GeneralWrapper<>(result).success());
  }

  /**
   * {@code PATCH  /employees/:id} : Partial updates given fields of an existing employee, field will ignore if it is null
   *
   * @param id          the id of the employeeDTO to save.
   * @param employeeDTO the employeeDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeDTO,
   * or with status {@code 400 (Bad Request)} if the employeeDTO is not valid,
   * or with status {@code 404 (Not Found)} if the employeeDTO is not found,
   * or with status {@code 500 (Internal Server Error)} if the employeeDTO couldn't be updated.
   */
  @PatchMapping(value = "/employees/{id}", consumes = {"application/json", "application/merge-patch+json"})
  public ResponseEntity<GeneralWrapper<EmployeeDTO>> partialUpdateEmployee(
    @PathVariable(value = "id", required = false) final String id,
    @RequestBody EmployeeDTO employeeDTO
  ) {
    log.debug("REST request to partial update Employee partially : {}, {}", id, employeeDTO);
    if (employeeDTO.getId() == null) {
      throw new BadRequestException(INVALID_ID);
    }
    if (!Objects.equals(id, employeeDTO.getId())) {
      throw new BadRequestException(INVALID_ID);
    }
    if (!employeeRepository.existsById(id)) {
      throw new BadRequestException("Entity not found");
    }

    var result = employeeService.partialUpdate(employeeDTO);
    return ResponseEntity.ok(new GeneralWrapper<>(result).success());
  }

  /**
   * {@code GET  /employees} : get all the employees.
   *
   * @param status the status of employee.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employees in body.
   */
  @GetMapping("/employees")
  public ResponseEntity<GeneralWrapper<List<EmployeeDTO>>> getAllEmployees(
    @RequestParam(value = "status", required = false, defaultValue = "true") Boolean status
  ) {
    log.debug("REST request to get a page of Employees");
    List<EmployeeDTO> listEmployee = employeeService.findAll(status);
    return ResponseEntity.ok()
      .body(new GeneralWrapper<>(listEmployee).success());
  }

  /**
   * {@code GET  /employees/:id} : get the "id" employee.
   *
   * @param id the id of the employeeDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/employees/{id}")
  public ResponseEntity<GeneralWrapper<EmployeeDTO>> getEmployee(@PathVariable String id) {
    log.debug("REST request to get Employee : {}", id);
    EmployeeDTO employeeDTO = employeeService.findOne(id);
    return ResponseEntity.ok()
      .body(new GeneralWrapper<>(employeeDTO).success());
  }

  /**
   * {@code DELETE  /employees/:id} : delete the "id" employee.
   *
   * @param id the id of the employeeDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/employees/{id}")
  public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
    log.debug("REST request to delete Employee : {}", id);
    employeeService.delete(id);
    return ResponseEntity
      .noContent()
      .build();
  }
}
