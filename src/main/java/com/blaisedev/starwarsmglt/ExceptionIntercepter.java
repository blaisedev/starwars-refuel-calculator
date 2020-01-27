//package com.blaisedev.starwarsmglt;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//@ControllerAdvice
//public class ExceptionIntercepter extends ResponseEntityExceptionHandler {
//
//    @Override
//    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        List<String> errorList = ex
//                .getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(fieldError -> fieldError.getDefaultMessage())
//                .collect(Collectors.toList());
//        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errorList);
//        return new ResponseEntity(apiError,status);
//    }
//}
