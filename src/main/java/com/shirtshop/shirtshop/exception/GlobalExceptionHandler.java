package com.shirtshop.shirtshop.exception;


import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;


@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) {
        ProblemDetail errorDetail = null;

        // TODO send this stack trace to an observability tool
        exception.printStackTrace();

        if (exception instanceof BadCredentialsException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
            errorDetail.setProperty("description", "The username or password is incorrect");

            return errorDetail;
        }

        if (exception instanceof AccountStatusException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "The account is locked");
        }

        if (exception instanceof AccessDeniedException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "You are not authorized to access this resource");
        }

        if (exception instanceof SignatureException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "The JWT signature is invalid");
        }

        if (exception instanceof ExpiredJwtException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "The JWT token has expired");
        }

        if (errorDetail == null) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage());
            errorDetail.setProperty("description", "Unknown internal server error.");
        }

        return errorDetail;
    }
	
	// Custom Exception Handler Area Starts
	
	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ErrorDetails> productNotFound(ProductNotFoundException pnf,WebRequest wr){
		ErrorDetails err = new ErrorDetails(LocalDateTime.now(),pnf.getMessage(),wr.getDescription(false));
		return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<ErrorDetails> categoryNotFound(CategoryNotFoundException cnf, WebRequest wr){
		ErrorDetails err = new ErrorDetails(LocalDateTime.now(),cnf.getMessage(), wr.getDescription(false));
		
		return new ResponseEntity<ErrorDetails>(err,HttpStatus.NO_CONTENT);
		
		
	}
	
	
	@ExceptionHandler(SellerException.class)
	public ResponseEntity<ErrorDetails> sellerExceptionHandler(SellerException slre, WebRequest wr){
		ErrorDetails err = new ErrorDetails(LocalDateTime.now(), slre.getMessage(), wr.getDescription(false));
		return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(SellerNotFoundException.class)
	public ResponseEntity<ErrorDetails> sellerNotFoundExceptionHandler(SellerNotFoundException snfe, WebRequest wr){
		ErrorDetails err = new ErrorDetails(LocalDateTime.now(), snfe.getMessage(), wr.getDescription(false));
		return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<ErrorDetails> customerNotFoundExceptionHandler(CustomerNotFoundException cnfe, WebRequest wr){
		ErrorDetails err = new ErrorDetails(LocalDateTime.now(), cnfe.getMessage(), wr.getDescription(false));
		return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(CustomerException.class)
	public ResponseEntity<ErrorDetails> customerExceptionHandler(CustomerException ce, WebRequest wr){
		ErrorDetails err = new ErrorDetails(LocalDateTime.now(), ce.getMessage(), wr.getDescription(false));
		return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
	}


	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ErrorDetails> noHandlerFoundExceptionHandler(NoHandlerFoundException nhfe, WebRequest wr){
		ErrorDetails err = new ErrorDetails(LocalDateTime.now(), nhfe.getMessage(), wr.getDescription(false));
		return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
	}
	
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDetails> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException manv, WebRequest wr){
		String message = manv.getBindingResult().getFieldError().getDefaultMessage();
		ErrorDetails err = new ErrorDetails(LocalDateTime.now(), message, wr.getDescription(false));
		return new ResponseEntity<ErrorDetails>(err, HttpStatus.FORBIDDEN);
	}
	
	
//	
//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<ErrorDetails> exceptionHandler(Exception e, WebRequest wr){
//		ErrorDetails err = new ErrorDetails(LocalDateTime.now(), e.getMessage(), wr.getDescription(false));
//		return new ResponseEntity<ErrorDetails>(err, HttpStatus.BAD_REQUEST);
//	}
//	
}
