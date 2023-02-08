package uz.md.shopapp.exceptions.handling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.ErrorData;
import uz.md.shopapp.exceptions.*;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AlreadyExistsException.class})
    public ApiResult<ErrorData> handleAccessDeniedException(Exception ex) {
        return ApiResult.errorResponse(ex.getMessage(), HttpStatus.FORBIDDEN.value());
    }

    @ExceptionHandler({
            AccessKeyInvalidException.class,
            InvalidUserNameOrPasswordException.class,
            NotAllowedException.class,
            NotEnabledException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResult<ErrorData> handleAllException(Exception ex) {
        return ApiResult.errorResponse(ex.getMessage(), HttpStatus.FORBIDDEN.value());
    }

    @ExceptionHandler({IllegalRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<ErrorData> handleIllegalRequest(Exception exception) {
        return ApiResult.errorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResult<ErrorData> handleConflictException(Exception exception) {
        return ApiResult.errorResponse(exception.getMessage(), HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResult<ErrorData> handleNotFoundException(Exception exception) {
        return ApiResult.errorResponse(exception.getMessage(), HttpStatus.NOT_FOUND.value());
    }


}