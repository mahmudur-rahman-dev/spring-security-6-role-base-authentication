package global.digital.signage.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonResponse {
    // Common
    OK(101, "Operation successfully executed"),
    FAILED(102, "Operation failed"),
    NOT_PERMITTED(103, "You are not permitted to execute this operation"),
    NOT_FOUND(104, "Resource not found to execute this operation"),

    // Role. Start from 201
    ROLE_NAME_NOT_PERMITTED(201, "You cannot use the name. Please use another name"),
    ROLE_USER_EXIST(202, "User exist with this role"),
    ROLE_DEFAULT_UPDATE_NOT_PERMITTED(203, "You cannot update default role"),

    // User. Start from 221
    DUPLICATE_EMAIL(221, "An user is already registered with this email. Please use another email");

    private final int returnCode;
    private final String message;

}