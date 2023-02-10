package com.bfi.ariedemo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralWrapper<T> {

  private HttpStatus status;

  private String message;

  private T data;

  public GeneralWrapper(T data) {
    this.data = data;
  }

  public GeneralWrapper<T> success() {
    return this.success(HttpStatus.OK);
  }

  public GeneralWrapper<T> success(HttpStatus httpStatus) {
    this.status = httpStatus;
    this.message = "SUCCESS";
    return this;
  }

  public GeneralWrapper<T> fail(HttpStatus httpStatus, String message) {
    this.status = httpStatus;
    this.message = message;
    return this;
  }
  public GeneralWrapper<T> notFound(HttpStatus httpStatus) {
    this.status = httpStatus;
    this.message = "failed";
    return this;
  }
}
