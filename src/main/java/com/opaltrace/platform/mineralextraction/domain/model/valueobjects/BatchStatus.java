package com.opaltrace.platform.mineralextraction.domain.model.valueobjects;

public enum BatchStatus {
    EN_ORIGEN,
    EN_TRANSITO,
    EN_PLANTA,
    PROCESADO,
    CERTIFICADO;

    public boolean canTransitionTo(BatchStatus next) {
        return switch (this) {
            case EN_ORIGEN -> next == EN_TRANSITO;
            case EN_TRANSITO -> next == EN_PLANTA;
            case EN_PLANTA -> next == PROCESADO;
            case PROCESADO -> next == CERTIFICADO;
            case CERTIFICADO -> false;
        };
    }
}
