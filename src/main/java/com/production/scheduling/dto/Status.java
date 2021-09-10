package com.production.scheduling.dto;

public enum Status {

    WAITING,
    IN_PROGRESS,
    COMPLETED,
    CANCELED{
        @Override
        public Status next() {
            return null;
        }
    };

    public Status next() {
        return values()[ordinal() + 1];
    }

    public Status previous() {
        return values()[ordinal() - 1];
    }
}
