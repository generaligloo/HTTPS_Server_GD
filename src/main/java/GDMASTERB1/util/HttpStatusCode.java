package GDMASTERB1.util;

public enum HttpStatusCode {

    CLIENT_ERROR_400_BAD_REQUEST(400, "Erreur de requête"),
    CLIENT_ERROR_401_METHOD_NOT_ALLOWED(401, "Méthode non autorisé"),
    CLIENT_ERROR_414_BAD_REQUEST(414, "URL trop long"),

    SERVER_ERROR_500_INTERNAL_SERVER_ERROR(500, "Erreur interne au serveur"),
    SERVER_ERROR_501_NOT_IMPLEMENTED(501, "Pas encore implémenté"),
    SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED(505, "Version HTTP non supporté");

    public final int STATUS_CODE;
    public final String MESSAGE;

    HttpStatusCode(int STATUS_CODE, String MESSAGE) {
        this.STATUS_CODE = STATUS_CODE;
        this.MESSAGE = MESSAGE;
    }
}
