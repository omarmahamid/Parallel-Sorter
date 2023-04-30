package org.parallel.sort;


import java.util.Arrays;

public class ParallelSort{

    private static final int BASE_CASE_SIZE = 10; // Change this as per your requirement

    public static void main(String[] args) {
        int[] arr = {9, 4, 2, 5, 1, 6, 3, 8, 7};
        optimizedBubbleSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    public static void optimizedBubbleSort(int[] arr, int l, int h) {
        if (h - l + 1 <= BASE_CASE_SIZE) {
            for (int i = l; i < h; i++) {
                for (int j = l; j < l + h - i; j++) {
                    if (arr[j] > arr[j + 1]) {
                        swap(arr, j, j + 1);
                    }
                }
            }
        } else {
            int m = (l + h) / 2;
            partition(arr, l, m, m + 1, h);
            Thread t1 = new Thread(() -> optimizedBubbleSort(arr, l, m));
            Thread t2 = new Thread(() -> optimizedBubbleSort(arr, m + 1, h));
            t1.start();
            t2.start();
            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            merge(arr, l, m, m + 1, h);
        }
    }

    public static void partition(int[] arr, int ll, int lh, int rl, int rh) {
        if (lh - ll + 1 <= BASE_CASE_SIZE) {
            for (int i = rl; i <= rh; i++) {
                for (int j = ll; j < lh; j++) {
                    if (arr[j] > arr[j + 1]) {
                        swap(arr, j, j + 1);
                    }
                }
                if (arr[lh] > arr[i]) {
                    swap(arr, lh, i);
                }
            }
        } else {
            int lm = (ll + lh) / 2;
            int rm = (rl + rh) / 2;
            Thread t1 = new Thread(() -> partition(arr, ll, lm, rl, rm));
            Thread t2 = new Thread(() -> partition(arr, lm + 1, lh, rm + 1, rh));
            t1.start();
            t2.start();
            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread t3 = new Thread(() -> merge(arr, ll, lm, rm + 1, rh));
            Thread t4 = new Thread(() -> merge(arr, lm + 1, lh, rl, rm));
            t3.start();
            t4.start();
            try {
                t3.join();
                t4.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void merge(int[] arr, int ll, int lh, int rl, int rh) {
        int[] temp = new int[arr.length];
        int i = ll;
        int j = rl;
        int k = 0;
        while (i <= lh && j <= rh) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }
        while (i <= lh) {
            temp[k++] = arr[i++];
        }
        while (j <= rh) {
            temp[k++] = arr[j++];
        }
        System.arraycopy(temp, 0, arr, ll, k);
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}