package hicc.budget.demo.config.auth;

import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import hicc.budget.demo.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    
    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());
        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    @Nullable
    public Object resolveArgument(@NonNull MethodParameter parameter, 
                                 @Nullable ModelAndViewContainer mavContainer,
                                 @NonNull NativeWebRequest webRequest,
                                 @Nullable WebDataBinderFactory binderFactory) {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
