package mixailche.jsonplaceholder.proxy.test.service;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.SneakyThrows;
import mixailche.jsonplaceholder.proxy.common.data.AccessLevel;
import mixailche.jsonplaceholder.proxy.common.data.ContentType;
import mixailche.jsonplaceholder.proxy.common.data.UserAccessDetails;
import mixailche.jsonplaceholder.proxy.common.service.business.JwtService;
import mixailche.jsonplaceholder.proxy.test.configuration.UnitTestConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class JwtServiceTest {

    private final JwtService jwtService;

    @Autowired
    private JwtServiceTest(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    private static final int THREADS = 10;

    @Test
    @SneakyThrows
    public void testValid() {
        List<Boolean> results = new CopyOnWriteArrayList<>(Collections.nCopies(THREADS, false));
        List<Thread> threads = IntStream.range(0, THREADS)
                .mapToObj(n -> new Thread(() -> runNthQuery(n, results)))
                .toList();
        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }
        results.forEach(Assertions::assertTrue);
    }

    private void runNthQuery(int n, List<Boolean> output) {
        UserAccessDetails accessDetails = provideRandomUserAccessDetails();
        String token = jwtService.createToken(accessDetails);
        output.set(n, accessDetails.equals(jwtService.getAccessDetails(token)));
    }

    @Test
    public void testInvalid() {
        String token = jwtService.createToken(provideRandomUserAccessDetails());
        Assertions.assertThrows(JWTDecodeException.class,
                () -> jwtService.getAccessDetails(token + token));
    }

    @Test
    @SneakyThrows
    public void testExpirationTime() {
        String token = jwtService.createToken(provideRandomUserAccessDetails());
        Thread.sleep(2 * 1000 * UnitTestConfiguration.REDUCED_TOKEN_LIFETIME);
        Assertions.assertThrows(TokenExpiredException.class, () -> jwtService.getAccessDetails(token));
    }

    private static final Random RANDOM = new Random();

    private static UserAccessDetails provideRandomUserAccessDetails() {
        return UserAccessDetails.builder()
                .userId(RANDOM.nextLong())
                .permissions(randomPermissions())
                .build();
    }

    private static Map<ContentType, AccessLevel> randomPermissions() {
        return Arrays.stream(ContentType.values())
                .collect(Collectors.toMap(Function.identity(), JwtServiceTest::randomAccessLevel));
    }

    private static AccessLevel randomAccessLevel(ContentType ignored) {
        return AccessLevel.values()[RANDOM.nextInt(AccessLevel.values().length)];
    }

}
