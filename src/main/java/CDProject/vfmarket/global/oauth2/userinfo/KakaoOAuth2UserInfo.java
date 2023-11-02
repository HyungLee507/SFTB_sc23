package CDProject.vfmarket.global.oauth2.userinfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getAge() {
        return null;
    }

    @Override
    public String getMobile() {
        return null;
    }


}
