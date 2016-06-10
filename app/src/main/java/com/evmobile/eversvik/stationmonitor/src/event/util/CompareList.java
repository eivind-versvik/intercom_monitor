package com.evmobile.eversvik.stationmonitor.src.event.util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by eversvik on 08.04.2016.
 */
public class CompareList<X, T extends Comparable<T>>
{
    HashMap<X, T> changes = new HashMap<>();
    ArrayList<T> list = new ArrayList<>();

    public CompareList(ArrayList<T> list)
    {
        this.list = list;
    }

    public boolean hasChanged(X id, T item)
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

    public boolean evaluate(X id, T item)
    {
        if(!hasChanged(id, item))
            return false;

        for(int i = 0;  i < list.size(); i++)
        {
            T litem = list.get(i);
            if(litem.compareTo(item) == 0)
                return true;
        }

        return false;
    }
}