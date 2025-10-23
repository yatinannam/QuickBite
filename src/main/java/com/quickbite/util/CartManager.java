package com.quickbite.util;

import com.quickbite.controllers.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<MenuItem> cartItems = new ArrayList<>();

    private CartManager() {
    }

    public static CartManager getInstance() {
        if (instance == null)
            instance = new CartManager();
        return instance;
    }

    public void addItem(MenuItem item) {
        cartItems.add(item);
    }

    public List<MenuItem> getItems() {
        return cartItems;
    }

    public void clear() {
        cartItems.clear();
    }

    public double getTotal() {
        return cartItems.stream().mapToDouble(MenuItem::getPrice).sum();
    }

    public void removeItem(MenuItem item) {
        cartItems.removeIf(i -> i.getName().equals(item.getName()));
    }

    public void increaseQuantity(MenuItem item) {
        item.setQuantity(item.getQuantity() + 1);
    }

    public void decreaseQuantity(MenuItem item) {
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
        }
    }
}
