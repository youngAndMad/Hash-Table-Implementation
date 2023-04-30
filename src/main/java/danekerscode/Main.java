package danekerscode;


import java.util.HashMap;

import static java.util.UUID.randomUUID;

public class Main {

    static Integer STUDENT_ID = 1;
    static Integer MY_TESTING_CLASS_ID_COUNTER = 1;
    record MyTestingClass(Integer id, String code) {
        @Override
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + this.id.hashCode();
            result = result * PRIME + this.code.hashCode();
            return result;
        }
    }

    record Student(Integer id, String name, String surname) { }

    private static Student createRandomStudent() {
        return new Student(
                ++STUDENT_ID,
                randomString(6),
                randomString(6)
        );
    }

    static String randomString(int length) {
        return randomUUID().toString().substring(0, length);
    }

    static MyTestingClass createMyTestingClass() {
        return new MyTestingClass(
                ++MY_TESTING_CLASS_ID_COUNTER,
                randomString(4)
        );
    }

    public static void main(String[] args) {
        MyHashTable<MyTestingClass, Student> myHashTable = new MyHashTable<>();
        for (int i = 0; i < 10_000; i++) {
            myHashTable.put(createMyTestingClass(), createRandomStudent());
        }
        System.out.println(myHashTable);
        System.out.println("--------------------------------------------------------------------------------------------");
        myHashTable.printAmountOfBucketsInEachList();
    }

}