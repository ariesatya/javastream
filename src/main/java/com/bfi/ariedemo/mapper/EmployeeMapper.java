package com.bfi.ariedemo.mapper;


import com.bfi.ariedemo.domain.Employee;
import com.bfi.ariedemo.dto.EmployeeDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring")
@SuppressWarnings("unused")
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {

  @Mapping(target = "status", constant = "true")
  Employee toEntity(EmployeeDTO s);

  @Mapping(source = "salary", target = "level", qualifiedByName = "leveling")
  EmployeeDTO toDto(Employee s);

  @Named("leveling")
  default String getLevelingName(Double salary) {
    if (salary < 5000) {
      return "Bronze";
    } else if (salary < 10000) {
      return "Silver";
    } else if (salary < 15000) {
      return "Gold";
    } else {
      return "Platinum";
    }
  }
}
