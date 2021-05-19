package metrics.test;

import java.util.ArrayList;

public class WMCTest {
    private int a, b, c, d, e;

    public void func1(int x) {
        int a, d, tweq, qwd;
        a = 2;
        this.b = 4;
        this.e = 5;
        d = 2;
        if (a > b && b < c) {
            System.out.println("fun 1");
        } else if (a > b && b < c || c > d) {
            if (a > b) {
            } else {
                if (a > b) {
                } else {
                }
            }
        } else {

        }
    }

    public void func2() {
        int k;
        this.b = 6;
        k = 2;
        ArrayList<Integer> arr = new ArrayList<Integer>();
        if (b > 2) {
            for (Integer temp : arr) {

            }
            for (int i = 0; i < arr.size(); i++) {

            }
        } else {
            if (c > e) {
                for (Integer temp : arr) {

                }
                for (int i = 0; i < arr.size(); i++) {

                }
            }
        }

    }

    public void func3() {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        for (Integer temp : arr) {
            for (; ; ) {
                if (a>b) {
                    a = b;
                } else {
                    for (; ; ) {
                        for (; ; ) {
                        }

                    }


                }
                for (; ; ) {
                    for (; ; ) {
                        for (; ; ) {
                            for (; ; ) {
                                if (true) {
                                    a = b;
                                } else {

                                }
                                for (; ; ) {

                                }
                            }
                        }

                    }

                }
            }

        }
    }
}
