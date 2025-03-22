package xa.sh.ecom.ecom.payload.request;

import jakarta.validation.constraints.NotBlank;

public class RefreshTokenReq {
    @NotBlank
    private String refreshToken;

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

}
