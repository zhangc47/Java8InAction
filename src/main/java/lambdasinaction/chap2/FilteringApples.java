package lambdasinaction.chap2;

import java.util.*;

/**
 * 行为参数化与值参数化, p30
 */
public class FilteringApples {

    public static void main(String... args) {

        List<Apple> inventory = Arrays.asList(
            new Apple(80, "green"),
            new Apple(155, "green"),
            new Apple(120, "red"));

        // 1. 区分苹果的颜色
        // [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
        List<Apple> greenApples = filterApplesByColor(inventory, "green");
        System.out.println(greenApples);

        // [Apple{color='red', weight=120}]
        List<Apple> redApples = filterApplesByColor(inventory, "red");
        System.out.println(redApples);

        // 2.区分苹果的重量(filterApplesByWeight)
        // 但是可以发现, 这个和1很相似.打破了DRY（ Don’t Repeat Yourself，不要重复自己）的软件工程原则
        // [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
        List<Apple> greenApples2 = filter(inventory, new AppleColorPredicate());
        System.out.println(greenApples2);

        // [Apple{color='green', weight=155}]
        List<Apple> heavyApples = filter(inventory, new AppleWeightPredicate());
        System.out.println(heavyApples);

        // []
        List<Apple> redAndHeavyApples = filter(inventory, new AppleRedAndHeavyPredicate());
        System.out.println(redAndHeavyApples);

        // [Apple{color='red', weight=120}]
        // 匿名类 -> 编译器提示 "replace with lambda"
        List<Apple> redApples2 = filter(inventory, new ApplePredicate() {
            @Override
            public boolean test(Apple a) {
                return a.getColor().equals("red");
            }
        });

        // lambda
        List<Apple> redApples3 = filter(inventory, (Apple apple) -> "red".equals(apple.getColor()));

        System.out.println(redApples2);


        // p31 7th尝试: 将list类型抽象化, lambda中写boolean test(T t)
        List<Apple> filter = filter(inventory, (Apple apple) -> "red".equals(apple.getColor()));

        List<Integer> integerList = Arrays.asList(1, 2);
        List<Integer> filter1 = filter(integerList, (Integer i) -> i % 2 == 0);


    }

    public interface Predicate<T> {
        boolean test(T t);
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for (T t : list) {
            if (p.test(t)) {
                result.add(t);
            }
        }
        return result;
    }


    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if ("green".equals(apple.getColor())) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterApplesByColor(List<Apple> inventory, String color) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getColor().equals(color)) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getWeight() > weight) {
                result.add(apple);
            }
        }
        return result;
    }

    /**
     * 接收行为为参数
     */
    public static List<Apple> filter(List<Apple> inventory, ApplePredicate p) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (p.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }



    interface ApplePredicate {
        public boolean test(Apple a);
    }

    static class AppleWeightPredicate implements ApplePredicate {
        @Override
        public boolean test(Apple apple) {
            return apple.getWeight() > 150;
        }
    }

    static class AppleColorPredicate implements ApplePredicate {
        @Override
        public boolean test(Apple apple) {
            return "green".equals(apple.getColor());
        }
    }

    static class AppleRedAndHeavyPredicate implements ApplePredicate {
        @Override
        public boolean test(Apple apple) {
            return "red".equals(apple.getColor())
                && apple.getWeight() > 150;
        }
    }

    /**
     * class Apple
     */
    public static class Apple {
        private int weight = 0;
        private String color = "";

        public Apple(int weight, String color) {
            this.weight = weight;
            this.color = color;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return "Apple{" +
                "color='" + color + '\'' +
                ", weight=" + weight +
                '}';
        }
    }
}