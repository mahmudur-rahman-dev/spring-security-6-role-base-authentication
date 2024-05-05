package global.digital.signage.util.Constants;

public class AppConstant {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String[] API_ENDPOINTS_WHITELIST = {
            "/api/v1/auth/login",
            "/api/v1/auth/refresh-token"
    };

    public static final String SUPER_ORGANIZATION_ID = "HEAD";

    public static final String DEFAULT_ORGANIZATION_NAME = "없음";
    public static final String DEFAULT_ORGANIZATION_ID = "-";
}
