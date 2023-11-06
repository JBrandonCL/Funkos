    package dev.jbcl.Cache;

    import dev.jbcl.Model.Funkos;


    import java.time.LocalDateTime;
    import java.util.LinkedHashMap;
    import java.util.Map;
    public class FunkoCacheImpl implements FunkoCache {
        private final int MaxSize;
        private final Map<Integer, Funkos> CACHE;

        public FunkoCacheImpl(int Max_Size){
            this.MaxSize = Max_Size;
            this.CACHE = new LinkedHashMap<Integer, Funkos>(this.MaxSize, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<Integer, Funkos> eldest) {
                    return size() > MaxSize;
                }
            };

        }

        @Override
        public void put(Integer key, Funkos value) {
            CACHE.put(key, value);
        }

        @Override
        public Funkos get(Integer key) {
            return CACHE.get(key);
        }

        @Override
        public void remove(Integer key) {
            CACHE.remove(key);
        }

        @Override
        public void clear() {
            CACHE.entrySet().removeIf(entry -> {
                boolean shouldRemove = entry.getValue().getUpdated_at().plusMinutes(10).isBefore(LocalDateTime.now());
                return shouldRemove;
            });
        }


    }