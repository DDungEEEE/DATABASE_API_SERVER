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

    USER_NOT_FOUND_ERROR(403, "COMMON_003", "사용자를 찾을 수 없습니다."),

    USER_ALREADY_LOGGED_ERROR(403, "COMMON_004", "이미 사용중인 아이디입니다."),

    USER_NOT_LOGGED_ERROR(403, "COMMON_005", "로그인 중인 사용자가 아닙니다."),

    TOKEN_EXPIRED_ERROR(403, "COMMON_005", "로그인 세션이 만료되었습니다. 다시 로그인해주세요."),

    CLIENT_NOT_FOUND_ERROR(404, "CLIENT_001", "고객 정보를 찾을 수 없습니다"),

    // 사용자 ID 중복
    DUPLICATE_USER_ID_ERROR(400, "USER_001", "이미 존재하는 사용자 아이디입니다."),

    DUPLICATE_PHONE_NUMBER_ERROR(400, "USER_002", "이미 존재하는 휴대폰 번호입니다."),

    DUPLICATE_USER_PHONE_NUMBER_ERROR(400, "USER_002", "이미 존재하는 사용자 번호입니다."),

    //Staff id X
    STAFF_NOT_FOUND_ERROR(404, "STAFF_001", "관리자를 찾을 수 없습니다."),

    PRODUCT_NOT_FOUND_ERROR(404, "PRODUCT_001", "상품을 찾을 수 없습니다."),

    NOTICE_NOT_FOUND_ERROR(404, "NOTICE_001", "공지사항을 찾을 수 없습니다."),

    ORDER_NOT_FOUND_ERROR(404, "ORDER_001", "주문내역을 찾을 수 없습니다."),

    MANUFACTURER_SORT_NOT_FOUND_ERROR(404, "MANUFACTURER_SORT_001", "제품군을 찾을 수 없습니다."),

    MANUFACTURER_NOT_FOUND_ERROR(404, "MANUFACTURER_001", "제조사를 찾을 수 없습니다."),

    FEEDBACK_NOT_FOUND_ERROR(404, "FEEDBACK_001", "피드백을 찾을 수 없습니다."),

    ALERT_NOT_FOUND_ERROR(404, "ALERT_001", "알림을 찾을 수 없습니다."),

    ROLE_NOT_AUTHORIZED(403, "ACCOUNT_001", "접근할 권한이 존재하지 않습니다."),

    ACCESS_DENIED(403, "ACCOUNT_002", "접근이 거부되었습니다."),

    USER_IS_NOT_OWNER(403, "ACCOUNT_003", "User IS NOT OWNER"),

    TOKEN_IS_EXPIRED(403, "ACCOUNT_O02", "TOKEN IS EXPIRED");




    private final int status;
    private final String divisionCode;
    private final String reason;

}
