package ohsoontac.serverapi.domain.user.exception;


import ohsoontac.serverapi.global.error.exception.ErrorCode1;
import ohsoontac.serverapi.global.error.exception.OhSoonTacException;

public class UserNotFoundException extends OhSoonTacException {


    public static final OhSoonTacException EXCEPTION = new UserNotFoundException();

    public UserNotFoundException() {
        super(ErrorCode1.USER_NOT_FOUND);
    }
}
