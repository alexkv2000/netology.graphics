package ru.netology.graphics;

class A {
    public char getA() {
        return 'A';
    }
}

class B extends A {
    public char getB() {
        return 'B';
    }
}

public class ss {
    public static void main(String[] args) {
        A a = new B();
        System.out.println(a.getA());
    }
}
