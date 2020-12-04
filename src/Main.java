import DeepCopy.DeepCopy;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Hello");
        list.add("World");
        Man man = new Man("Dmitry", 24, list);
        Man man1 = new Man("Dmitry", 24, list);
        boolean result = man.equals(man);
        Man manClone = DeepCopy.clone(man);
        boolean d = man.equals(man);
        manClone.getFavoriteBooks().add("World and War");
        System.out.println("Hello World!");
    }
}
