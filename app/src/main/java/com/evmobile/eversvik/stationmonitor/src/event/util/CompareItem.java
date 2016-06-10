package com.evmobile.eversvik.stationmonitor.src.event.util;

import java.util.HashMap;

/**
 * Created by eversvik on 09.04.2016.
 */
public class CompareItem<X, T extends Comparable<T>>
{
    HashMap<X, T> changes = new HashMap<>();
    T item;

    public CompareItem(T item)
    {
        this.item = item;
    }

    private boolean hasChanged(X id, T item)
    {
        T old = changes.get(id);
        if(old == null) {
            // Has changed - can't find old
            changes.put(id, item);
            return true;
        }

        if(old.compareTo(item) == 0) {
            // Is equal - has not changed
            changes.put(id, item);
            return false;
        }
        else
        {
            // Is not equal - thus has changed
            changes.put(id, item);
            return true;
        }
    }

    public boolean evaluate(X id, T item) {
        return hasChanged(id, item) && compare(item);
    }

    public boolean compare(T item)
    {
        return this.item.compareTo(item) == 0;
    }
}
