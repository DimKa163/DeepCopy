import DeepCopy.DeepCopy;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Hello");
        list.add("World");
        Man man = new Man("Dmitry", 24, list);
        Man manClone = DeepCopy.clone(man);
        manClone.getFavoriteBooks().add("World and War");
        System.out.println("The End!");
    }
}
