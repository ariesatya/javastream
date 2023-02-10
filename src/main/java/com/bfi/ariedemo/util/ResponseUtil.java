package com.bfi.ariedemo.util;

import com.bfi.ariedemo.dto.GeneralWrapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface ResponseUtil {
  static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse) {
    return wrapOrNotFound(maybeResponse, null);
  }

  static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse, HttpHeaders header) {
    return maybeResponse.map(response -> ResponseEntity.ok().headers(header).body(response))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  static <X> ResponseEntity<GeneralWrapper<X>> wrapOrNotFoundGeneral(Optional<X> maybeResponse) {
    return wrapOrNotFoundGeneral(maybeResponse, null);
  }

  static <X> ResponseEntity<GeneralWrapper<X>> wrapOrNotFoundGeneral(Optional<X> maybeResponse, HttpHeaders header) {
    return maybeResponse
      .map(response -> ResponseEntity.ok().headers(header)
        .body(new GeneralWrapper<X>().success(HttpStatus.OK)))
      .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new GeneralWrapper<X>().notFound(HttpStatus.NOT_FOUND)));
  }
}
