package mixailche.jsonplaceholder.proxy.test.service;

import lombok.Getter;
import mixailche.jsonplaceholder.proxy.common.data.ProxyRequestDto;
import mixailche.jsonplaceholder.proxy.common.data.SupportedHttpMethod;
import mixailche.jsonplaceholder.proxy.common.service.business.JsonPlaceholderClientService;
import mixailche.jsonplaceholder.proxy.common.service.db.EventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static mixailche.jsonplaceholder.proxy.common.data.SupportedHttpMethod.DELETE;
import static mixailche.jsonplaceholder.proxy.common.data.SupportedHttpMethod.GET;
import static mixailche.jsonplaceholder.proxy.common.data.SupportedHttpMethod.POST;
import static mixailche.jsonplaceholder.proxy.common.data.SupportedHttpMethod.PUT;
import static mixailche.jsonplaceholder.proxy.core.util.HttpUtils.sendStatusCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JsonPlaceholderClientServiceTest {

    private final JsonPlaceholderClientService jsonPlaceholderClientService;

    @Autowired
    public JsonPlaceholderClientServiceTest(JsonPlaceholderClientService jsonPlaceholderClientService) {
        this.jsonPlaceholderClientService = jsonPlaceholderClientService;
    }

    private void mockClient(Answer<?> answer) {
        RestTemplate client = mock();
        when(client.exchange(
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.<Class<?>>any()
        )).thenAnswer(answer);
        ReflectionTestUtils.setField(jsonPlaceholderClientService, "client", client);
    }

    @MockBean
    private EventService eventServiceMock;

    @ParameterizedTest
    @MethodSource("provideSampleRequest")
    public void testRequestSending(ProxyRequestDto request) {
        setAssertingClient(
                URI.create("https://jsonplaceholder.typicode.com").resolve(request.getContextPath()),
                HttpMethod.valueOf(request.getMethod().name()),
                request.getBody()
        );
        try {
            jsonPlaceholderClientService.executeRequest(request);
        } catch (AssertionError e) {
            Assertions.fail(e);
        } catch (Exception ignored) {
            // No operations.
        }
    }

    private void setAssertingClient(URI expectedUri, HttpMethod expectedMethod, String expectedBody) {
        mockClient(invocation -> {
            Assertions.assertEquals(expectedUri, invocation.<URI>getArgument(0));
            Assertions.assertEquals(expectedMethod, invocation.<HttpMethod>getArgument(1));
            Assertions.assertEquals(expectedBody, invocation.<HttpEntity<String>>getArgument(2).getBody());
            return null;
        });
    }

    @ParameterizedTest
    @MethodSource("provideErrorCodes")
    public void testErrorHandling(HttpStatus errorCode) {
        mockClient(invocation -> sendStatusCode(errorCode));
        try {
            jsonPlaceholderClientService.executeRequest(sampleRequest());
        } catch (RestClientException e) {
            if (errorCode.is4xxClientError()) {
                Assertions.fail(e);
            }
        } catch (Exception ignored) {
            // No operations.
        }
    }

    public record RequestKey(SupportedHttpMethod method, String contextPath) {
        public static RequestKey of(SupportedHttpMethod method, String contextPath) {
            return new RequestKey(method, contextPath);
        }
    }

    @ParameterizedTest
    @MethodSource("provideCachingSamples")
    public void testCaching(int expectedRequests, List<RequestKey> requestKeys) {
        Counter counter = new Counter();
        mockClient(invocation -> { counter.inc(); return sendStatusCode(HttpStatus.OK); });
        requestKeys.forEach(key -> {
            try {
                jsonPlaceholderClientService.executeRequest(
                        ProxyRequestDto.builder()
                                .method(key.method())
                                .contextPath(key.contextPath())
                                .headers(HttpHeaders.writableHttpHeaders(HttpHeaders.EMPTY))
                                .build()
                );
            } catch (Exception ignored) {
                // No operations.
            }
        });
        Assertions.assertEquals(expectedRequests, counter.getCount());
    }

    @Getter
    private static class Counter {
        private int count = 0;

        public void inc() {
            count++;
        }
    }

    private static ProxyRequestDto sampleRequest() {
        return ProxyRequestDto.builder()
                .body("sample body")
                .contextPath("/sample/path")
                .method(POST)
                .build();
    }

    private static Stream<ProxyRequestDto> provideSampleRequest() {
        return Stream.of(sampleRequest());
    }

    private static Stream<HttpStatus> provideErrorCodes() {
        return Arrays.stream(HttpStatus.values()).filter(HttpStatusCode::isError);
    }

    private static Stream<Arguments> provideCachingSamples() {
        return Stream.of(
                cacheUsageExample(GET),
                cacheUsageExample(PUT),
                cacheUsageExample(DELETE),
                cacheInvalidationExample(GET, PUT),
                cacheInvalidationExample(GET, POST),
                cacheInvalidationExample(GET, DELETE),
                cacheInvalidationExample(PUT, POST),
                cacheInvalidationExample(PUT, DELETE),
                cacheInvalidationExample(DELETE, PUT),
                cacheInvalidationExample(DELETE, POST),
                cacheMissingExample(10),
                cacheMissingExample(20),
                cacheMissingExample(100)
        );
    }

    private static final Random RANDOM = new Random();

    private static String getRandomPath() {
        return "/random/path/" + RANDOM.nextLong();
    }

    private static Arguments cacheUsageExample(SupportedHttpMethod idempotentMethod) {
        RequestKey cachedRequest = RequestKey.of(idempotentMethod, getRandomPath());
        return Arguments.of(1, List.of(cachedRequest, cachedRequest));
    }

    private static Arguments cacheInvalidationExample(SupportedHttpMethod idempotentMethod,
                                                      SupportedHttpMethod unsafeMethod) {
        String path = getRandomPath();
        return Arguments.of(3, List.of(
                RequestKey.of(idempotentMethod, path),
                RequestKey.of(unsafeMethod, path),
                RequestKey.of(idempotentMethod, path)
        ));
    }

    private static Arguments cacheMissingExample(int count) {
        String path = getRandomPath();
        return Arguments.of(count, IntStream.rangeClosed(1, count)
                .mapToObj(i -> RequestKey.of(GET, path + "/" + i)).toList());
    }

}
