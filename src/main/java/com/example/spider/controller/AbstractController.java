package com.example.spider.controller;

import com.example.spider.model.DTOs.ErrorDTO;
import com.example.spider.model.exceptions.BadRequestException;
import com.example.spider.model.exceptions.NotFoundException;
import com.example.spider.model.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.time.LocalDateTime;

public abstract class AbstractController {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequest(Exception e){
        return generateErrorDTO(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleUnauthorized(Exception e){
        return generateErrorDTO(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFound(Exception e){
        return generateErrorDTO(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleRest(Exception e){
        e.printStackTrace();
        return generateErrorDTO(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private ErrorDTO generateErrorDTO(final Exception e, final HttpStatus s){
        return ErrorDTO.builder()
                .msg(e.getMessage())
                .time(LocalDateTime.now())
                .status(s.value())
                .build();
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleValidationException(MethodArgumentNotValidException e) {
        final BindingResult bindingResult = e.getBindingResult();
        final String errorMessage = bindingResult.getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .findFirst()
                .orElse("Invalid data");
        return generateErrorDTO(new BadRequestException(errorMessage), HttpStatus.BAD_REQUEST);
    }
    protected  long loggedId(final HttpSession session){
        return (long) session.getAttribute(Util.LOGGED_ID);
    }

    protected boolean validSession(final HttpSession session){
        if(session.getAttribute(Util.LOGGED)==null){
            throw new UnauthorizedException("Invalid session. Login first.");
        }
        return true;
    }
    protected void invalidateSession(final HttpSession session){
        if(!validSession(session)) {
            throw new BadRequestException("Not a valid session") ;
        }
        session.invalidate();
    }

}
