package CDProject.vfmarket.payment.enums;

public enum IamportApiUrl {

    GET_TOKEN_URL("https://api.iamport.kr/users/getToken"),
    CANCEL_URL("https://api.iamport.kr/payments/cancel");
    private String url;

    public String getURL() {
        return url;
    }

    IamportApiUrl(String url) {
        this.url = url;
    }
}
