package com.ssg.itbody.enums;

public enum MembershipGrade {
    MEMBER("회원"),
    TRAINER("트레이너");

    private final String value;

    MembershipGrade(String value) {
        this.value = value;
    }

    public String getMembershipGrade() {
        return value;
    }
}

