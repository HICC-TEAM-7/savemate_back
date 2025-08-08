package hicc.budget.demo.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @LoginUser 어노테이션을 생성합니다.
 * 컨트롤러 메서드에서 @LoginUser SessionUser user 와 같이 사용할 수 있습니다.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
}
