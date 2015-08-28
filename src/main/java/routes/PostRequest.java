package routes;

import models.requests.*;

public class PostRequest {
    private final AutoLoginRequest autoLoginRequest = new AutoLoginRequest();
    private final ContributionRequest contributionRequest = new ContributionRequest();
    private final UserRequest userRequest = new UserRequest();
    private final NGUserRequest ngUserRequest = new NGUserRequest();
    private final NGWordRequest ngWordRequest = new NGWordRequest();

    protected AutoLoginRequest autoLoginRequest() {
        return autoLoginRequest;
    }

    protected ContributionRequest contributionRequest() {
        return contributionRequest;
    }

    protected UserRequest userRequest() {
        return userRequest;
    }

    protected NGUserRequest ngUserRequest() {
        return ngUserRequest;
    }

    protected NGWordRequest ngWordRequest() {
        return ngWordRequest;
    }
}
