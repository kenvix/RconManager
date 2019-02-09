package com.kenvix.rconmanager.ui.base.view;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

public class IconManager <T extends AppCompatActivity> {
    @SuppressWarnings("UseSparseArrays")
    private Map<Integer, ImageView> cache = new HashMap<>();

    private T invoker;
    private static IconManager instance;

    private IconManager(T invoker) {
        this.invoker = invoker;
    }

    @SuppressWarnings("unchecked")
    public static <U extends AppCompatActivity> IconManager initialize(U invoker) {
        if(instance == null) {
            synchronized (IconManager.class) {
                if(instance == null) {
                    instance = new IconManager(invoker);
                }
                return instance;
            }
        }
        return instance;
    }

    @NonNull
    public static IconManager getInstance() {
        if(instance == null)
            throw new IllegalStateException("IconManager has not initialized");
        return instance;
    }

    public ImageView getIcon(int id) {
        if(!cache.containsKey(id)) {
            synchronized (this) {
                if(!cache.containsKey(id)) {
                    ImageView imageView = invoker.findViewById(id);
                    cache.put(id, imageView);
                    return imageView;
                }
                return cache.get(id);
            }
        }
        return cache.get(id);
    }
}
