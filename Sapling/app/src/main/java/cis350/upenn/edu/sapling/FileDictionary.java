package cis350.upenn.edu.sapling;

import java.util.Set;

// @author: amenarde

public interface FileDictionary<S, T> {
    public boolean put(S s, T t);
    public T get(S s);
    public boolean has(S s);
    public T remove(S s);
    public Set<S> getKeySet();
    public Set<T> getValueSet();
}
