package net.ddns.sbapiserver.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //서버가 처리할 방법을 모름
    INTERNAL_SERVER_ERROR(500, "COMMON-001", "SERVER ERROR"),

    //권한이 없음
    FORBIDDEN_ERROR(403, "COMMON_002", "FOR BIDDEN"),

    CLIENT_NOT_FOUND_ERROR(404, "CLIENT_001", "사용자를 찾을 수 없습니다"),

    // Client의 id 중복
    DUPLICATE_CLIENT_ERROR(400, "CLIENT_002", "CLIENT IS DUPLICATE"),

    //Staff id X
    STAFF_NOT_FOUND_ERROR(404, "STAFF_001", "관리자를 찾을 수 없습니다."),

    // Staff의 아이디 중복
    DUPLICATE_STAFF_ERROR(400, "STAFF_002", "STAFF IS DUPLICATE"),

    PRODUCT_NOT_FOUND_ERROR(404, "PRODUCT_001", "상품을 찾을 수 없습니다."),

    NOTICE_NOT_FOUND_ERROR(404, "NOTICE_001", "공지사항을 찾을 수 없습니다."),

    ROLE_NOT_AUTHORIZED(403, "ACCOUNT_001", "ROLE IS NOT AUTHORIZED"),

    TOKEN_IS_EXPIRED(403, "ACCOUNT_O02", "TOKEN IS EXPIRED");




    private final int status;
    private final String divisionCode;
    private final String reason;

}
