package repository.book;

import model.Book;

import java.util.*;

public class Cache<T extends Book>{

    private List<T> storage;

    public List<T> load(){
        return storage;
    }

    public void save(List<T> entities){
        this.storage = entities;
    }

    public boolean hasResult(){
        return storage != null;
    }

    public void invalidateCache(){
        storage = null;
    }
}