package model;

import java.util.List;
import java.util.stream.Collectors;

import services.Store;

public class Customer extends Person {
    private Store store; 

    public Customer(String id, String username, String password,String name, Store store) {
        super(id, username, password,name, "Customer"); 
        this.store = store; 
    }

    public List<Product> searchProductsByName(String name) {
        return store.searchByName(name);
    }

    public List<Book> searchBooksByAuthor(String author) {
        List<Product> products = store.searchByAuthor(author); 
        return products.stream()
                       .filter(product -> product instanceof Book)
                       .map(product -> (Book) product)
                       .collect(Collectors.toList());
    }

    public List<Book> searchBooksByPublicationYear(int year) {
        return store.searchByPublicationYear(year); 
    }
}
