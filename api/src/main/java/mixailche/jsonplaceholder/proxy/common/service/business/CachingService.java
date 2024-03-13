package mixailche.jsonplaceholder.proxy.common.service.business;

import java.util.Optional;

public interface CachingService<K, V> {

    void update(K key, V value);

    Optional<V> get(K key);

}
