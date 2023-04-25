package pro.sky.sockswarehouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "This number of socks is out of stock")
public class NoNumberOfSocksException extends RuntimeException {
}
