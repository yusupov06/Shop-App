package uz.md.shopapp.exceptions.handling;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uz.md.shopapp.dtos.ApiResult;
import uz.md.shopapp.dtos.ErrorData;
import uz.md.shopapp.exceptions.*;

import java.util.Arrays;


@ControllerAdvice
@RequiredArgsConstructor
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler({AlreadyExistsException.class})
    public ApiResult<ErrorData> handleAccessDeniedException(Exception ex) {
        return ApiResult.errorResponse(Arrays.toString(ex.getStackTrace()),
                messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale()),
                HttpStatus.FORBIDDEN.value());
    }

    @ExceptionHandler({
            AccessKeyInvalidException.class,
            InvalidUserNameOrPasswordException.class,
            NotAllowedException.class,
            NotEnabledException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResult<ErrorData> handleAllException(Exception ex) {
        return ApiResult.errorResponse(Arrays.toString(ex.getStackTrace()),
                messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale()),
                HttpStatus.FORBIDDEN.value());
    }

    @ExceptionHandler({IllegalRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<ErrorData> handleIllegalRequest(Exception exception) {
        return ApiResult.errorResponse(Arrays.toString(exception.getStackTrace()),
                messageSource.getMessage(exception.getMessage(), null, LocaleContextHolder.getLocale()),
                HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResult<ErrorData> handleConflictException(Exception exception) {
        return ApiResult.errorResponse(Arrays.toString(exception.getStackTrace()),
                messageSource.getMessage(exception.getMessage(), null, LocaleContextHolder.getLocale()),
                HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResult<ErrorData> handleNotFoundException(Exception exception) {
        return ApiResult.errorResponse(Arrays.toString(exception.getStackTrace()),
                messageSource.getMessage(exception.getMessage(), null, LocaleContextHolder.getLocale()),
                HttpStatus.NOT_FOUND.value());
    }


}