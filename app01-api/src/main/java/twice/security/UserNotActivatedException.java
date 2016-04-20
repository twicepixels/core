package twice.security;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is throw in case of a not activated user trying to authenticate.
 */
class UserNotActivatedException extends AuthenticationException {

    UserNotActivatedException(String message) {
        super(message);
    }

}