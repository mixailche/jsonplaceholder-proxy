package mixailche.jsonplaceholder.proxy.common.service.business;

public interface AuthenticationService {

    String enter(String login, String password);

    String register(String login, String password);

}
