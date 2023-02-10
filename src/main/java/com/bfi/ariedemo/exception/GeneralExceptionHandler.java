package com.bfi.ariedemo.exception;


import com.bfi.ariedemo.dto.GeneralWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;

@RequiredArgsConstructor
@RestControllerAdvice
@SuppressWarnings("unused")
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

  private static final String UNEXPECTED_ERROR = "Unexpected error";

  /**
   * Handle general error message.
   *
   * @param ex exception to be handled
   * @return response entity with error message
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<GeneralWrapper<Object>> general(Exception ex) {
    printError(ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(new GeneralWrapper<>().fail(HttpStatus.INTERNAL_SERVER_ERROR, UNEXPECTED_ERROR));
  }

  /**
   * Handle badrequest error message.
   *
   * @param ex exception to be handled
   * @return response entity with error message
   */
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<GeneralWrapper<Object>> badRequest(BadRequestException ex) {
    printError(ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(new GeneralWrapper<>().fail(HttpStatus.BAD_REQUEST, ex.getMessage()));
  }

  /**
   * Handle validation message.
   *
   * @param ex exception to be handled
   * @return response entity with error message
   */
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<GeneralWrapper<Object>> validation(ValidationException ex) {
    printError(ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(new GeneralWrapper<>().fail(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()));
  }


  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<GeneralWrapper<Object>> duplicate(DataIntegrityViolationException ex) {
    printError(ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(new GeneralWrapper<>().fail(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()));
  }

  /**
   * A single place to customize the response body of all validation types.
   * {@link ResponseEntity} from the given body, headers, and status.
   *
   * @param ex      the MethodArgumentNotValidException
   * @param headers the headers for the response
   * @param status  the response status
   * @param request the current request
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers, HttpStatus status, WebRequest request) {

    printError(ex);
    return ResponseEntity.status(status).body(new GeneralWrapper<>().fail(HttpStatus.BAD_REQUEST, ex.getMessage()));

  }

  /**
   * A single place to customize the response body of all exception types.
   * {@link ResponseEntity} from the given body, headers, and status.
   *
   * @param ex      the exception
   * @param body    the body for the response
   * @param headers the headers for the response
   * @param status  the response status
   * @param request the current request
   */
  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                           HttpStatus status, WebRequest request) {
    printError(ex);
    super.handleExceptionInternal(ex, body, headers, status, request);
    return ResponseEntity.status(status).headers(headers)
      .body(new GeneralWrapper<>().fail(status, ex.getMessage()));
  }
  private void printError(Exception ex) {
    logger.error("Error::{}", ex);
  }
}
