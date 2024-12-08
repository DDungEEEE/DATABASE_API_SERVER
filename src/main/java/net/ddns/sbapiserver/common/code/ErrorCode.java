package net.ddns.sbapiserver.common.code;

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

    // 사용자 ID 중복
    DUPLICATE_USER_ID_ERROR(400, "USER_001", "이미 존재하는 사용자 아이디입니다."),

    DUPLICATE_USER_PHONE_NUMBER_ERROR(400, "USER_002", "이미 존재하는 사용자 번호입니다."),

    //Staff id X
    STAFF_NOT_FOUND_ERROR(404, "STAFF_001", "관리자를 찾을 수 없습니다."),

    PRODUCT_NOT_FOUND_ERROR(404, "PRODUCT_001", "상품을 찾을 수 없습니다."),

    NOTICE_NOT_FOUND_ERROR(404, "NOTICE_001", "공지사항을 찾을 수 없습니다."),

    ORDER_NOT_FOUND_ERROR(404, "ORDER_001", "주문내역을 찾을 수 없습니다."),

    MANUFACTURER_NOT_FOUND_ERROR(404, "MANUFACTURER_001", "제조사를 찾을 수 없습니다."),

    ROLE_NOT_AUTHORIZED(403, "ACCOUNT_001", "ROLE IS NOT AUTHORIZED"),

    ACCESS_DENIED(403, "ACCOUNT_002", "접근 권한이 존재하지 않습니다."),

    USER_IS_NOT_OWNER(403, "ACCOUNT_003", "User IS NOT OWNER"),

    TOKEN_IS_EXPIRED(403, "ACCOUNT_O02", "TOKEN IS EXPIRED");




    private final int status;
    private final String divisionCode;
    private final String reason;

}
