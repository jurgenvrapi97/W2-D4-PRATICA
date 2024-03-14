package org.example;

import com.github.javafaker.Faker;
import com.mifmif.common.regex.Main;
import org.example.esential.Customer;
import org.example.esential.Order;
import org.example.esential.Product;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

class main {

    public static void main(String[] args) {

        Faker faker = new Faker(Locale.ITALY);

        Supplier<Long> randomIdSupplier = () -> {
            Random random = new Random();
            return random.nextLong();
        };

        Supplier<Customer> userSupplier = () -> {
            return new Customer(randomIdSupplier.get(), faker.funnyName().name(),1);
        };

        Supplier<Product> productSupplier = () ->{
          return new Product(randomIdSupplier.get(), faker.commerce().productName(), faker.commerce().department(), faker.number().randomDouble(2, 1, 100));
        };

        List<Customer> clienti = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            clienti.add(userSupplier.get());
        }




        List<Product> prodotti = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            prodotti.add(productSupplier.get());
        }

        List<Order> ordini = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LocalDate orderDate = LocalDate.now().minusDays(faker.number().numberBetween(0, 30));
            LocalDate deliveryDate = orderDate.plusDays(faker.number().numberBetween(1, 7));


            List<Product> prodottiOrdine = new ArrayList<>();
            int numProducts = faker.number().numberBetween(1, 5);
            for (int j = 0; j < numProducts; j++) {
                prodottiOrdine.add(productSupplier.get());
            }

            ordini.add(new Order(randomIdSupplier.get(), "ordinato", orderDate, deliveryDate, prodottiOrdine, clienti.get(i)));
        }


        // ESERCIZIO 1


        Map<Customer, List<Order>> ordersByCustomer = ordini.stream()
                .collect(Collectors.groupingBy(Order::getCustomer));

        ordersByCustomer.forEach((customer, orders) -> {
            System.out.println("Cliente: " + customer);
            System.out.println("Ordini: " + orders);
        });


        Map<Customer, Double> totalSalesByCustomer = ordini.stream()
                .collect(Collectors.groupingBy(Order::getCustomer,
                        Collectors.summingDouble(order -> order.getProducts().stream()
                                .mapToDouble(Product::getPrice)
                                .sum())));


        System.out.println("------------------------------------------------------------------------------------");
        totalSalesByCustomer.forEach((customer, totalSales) -> {
            System.out.println("Cliente: " + customer);
            System.out.println("Totale vendite: " + totalSales);
        });


        // ESERCIZIO 3

        double averageOrderAmount = ordini.stream()
                .collect(Collectors.averagingDouble(order -> order.getProducts().stream()
                        .mapToDouble(Product::getPrice)
                        .sum()));

        System.out.println("------------------------------------------------------------------------------------------");

        System.out.println("Importo medio degli ordini: " + averageOrderAmount);
     }

}
