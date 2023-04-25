package pro.sky.sockswarehouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "These socks are out of stock")
public class SocksNotFoundException extends RuntimeException {
}
