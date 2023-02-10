package com.bfi.ariedemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bfi.ariedemo.domain.Employee} entity.
 */
@Data
@Builder
@Schema(description = "The Employee entity.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeDTO implements Serializable {

  private String id;

  /**
   * The firstname attribute.
   */
  @Schema(description = "The firstname attribute.")
  @NotEmpty
  private String firstName;

  private String lastName;

  @NotEmpty
  private String email;

  private String phoneNumber;

  private Double salary;
  private boolean status;

  private String level;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EmployeeDTO)) {
      return false;
    }

    EmployeeDTO employeeDTO = (EmployeeDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, employeeDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
  @Override
  public String toString() {
    return "EmployeeDTO{" +
      "id=" + getId() +
      ", firstName='" + getFirstName() + "'" +
      ", lastName='" + getLastName() + "'" +
      ", email='" + getEmail() + "'" +
      ", phoneNumber='" + getPhoneNumber() + "'" +
      ", salary=" + getSalary() +
      "}";
  }
}
