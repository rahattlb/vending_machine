import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AppRunner {
    private final UniversalArray<Product> products = new UniversalArrayImpl<>();
    private final PaymentMethod cardAcceptor;
    private final PaymentMethod coinAcceptor;
    private PaymentMethod paymentMethod;
    private int enteredMoney = 0;
    private static boolean isExit = false;

    public AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        cardAcceptor = new CardAcceptor(0);
        coinAcceptor = new CoinAcceptor(100);
    }
    public void run() {
        AppRunner app = new AppRunner();
        paymentMethod = choosePayment();
        while (!isExit) {
            app.startSimulation(paymentMethod);
        }
    }

    private void startSimulation(PaymentMethod paymentMethod) {
        print("В автомате доступны:");
        showProducts(products);
        print("Сумма: " + paymentMethod.getAmount());
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts(paymentMethod).toArray());
        chooseAction(allowProducts, paymentMethod);
    }
    private UniversalArray<Product> getAllowedProducts(PaymentMethod paymentMethod) {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (paymentMethod.getAmount() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }
    private void chooseAction(UniversalArray<Product> products, PaymentMethod paymentMethod) {
        print(" a - Пополнить баланс");
        showActions(products);
        print(" h - Выйти");
        String action = strFromConsole().substring(0, 1);
        if ("a".equalsIgnoreCase(action)) {
            print("Вы пополнили баланс на " + enteredMoney);
            paymentMethod.setAmount(paymentMethod.getAmount() + enteredMoney);
            return;
        }
        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    paymentMethod.setAmount(paymentMethod.getAmount() - products.get(i).getPrice());
                    print("Вы купили " + products.get(i).getName());
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            if ("h".equalsIgnoreCase(action)) {
                isExit = true;
            } else {
                print("Недопустимая буква. Попрбуйте еще раз.");
                chooseAction(products, paymentMethod);
            }
        }
    }
    
    private PaymentMethod choosePayment() {
        print("Выберите способ оплаты:\n1.Оплата по карте\n2.Оплата монетами");
        try {
            int method = intFromConsole();
            if(method == 1) {
                paymentMethod = cardPayment();
            }else if(method == 2) {
                paymentMethod = coinPayment();
            }
        }catch (InputMismatchException e) {
            print("Недопустимое значение!");
            choosePayment();
        }
        return paymentMethod;
    }
    private PaymentMethod coinPayment() {
        try {
            print("Укажите сумму: ");
            enteredMoney = intFromConsole();
            coinAcceptor.setAmount(enteredMoney);
        }catch (IllegalArgumentException | InputMismatchException e) {
            print("Недопустимое значение. Попрбуйте еще раз.");
            coinPayment();
        }
        return coinAcceptor;
    }
    private PaymentMethod cardPayment() {
        try {
            print("Введите номер карты: ");
            int value = Integer.parseInt(strFromConsole().trim());
            cardAcceptor.setCardNumber(strFromConsole().trim());
        }catch (IllegalArgumentException | InputMismatchException e) {
            print("Недопустимое значение. Попрбуйте еще раз.");
            cardPayment();
        }
        try {
            print("Введите пароль от карты: ");
            cardAcceptor.setPassword(intFromConsole());
        }catch (IllegalArgumentException | InputMismatchException e) {
            print("Недопустимое значение. Попрбуйте еще раз.");
            cardPayment();
        }
        try {
            print("Укажите сумму: ");
            enteredMoney = intFromConsole();
            cardAcceptor.setAmount(enteredMoney);
        }catch (IllegalArgumentException | InputMismatchException e) {
            print("Недопустимое значение. Попрбуйте еще раз.");
            cardPayment();
        }
        return cardAcceptor;
    }


    private String strFromConsole() {
        return new Scanner(System.in).nextLine();
    }
    private int intFromConsole() {
        return new Scanner(System.in).nextInt();
    }
    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }
    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }
    private void print(String msg) {
        System.out.println(msg);
    }
}
