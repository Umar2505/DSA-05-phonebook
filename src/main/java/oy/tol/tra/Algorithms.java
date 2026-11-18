package oy.tol.tra;

import java.util.Comparator;
import java.util.function.Predicate;

public final class Algorithms {

   private Algorithms() {
   }

   public static <T extends Comparable<T>> void sort(T [] array) {
      fastSort(array);
   }

   public static <T extends Comparable<T>> void fastSort(T [] array) {
      quickSort(array, 0, array.length - 1);
   }

   public static <T extends Comparable<T>> void quickSort(T [] array, int begin, int end) {
      while (begin < end) {
         int split = partition(array, begin, end);
         if (split - begin < end - split) {
            quickSort(array, begin, split);
            begin = split + 1;
         } else {
            quickSort(array, split + 1, end);
            end = split;
         }
      }
   }

   private static <T extends Comparable<T>> int partition(T [] array, int begin, int end) {
      T pivot = array[begin + (end - begin) / 2];
      int left = begin - 1;
      int right = end + 1;

      while (true) {
         do {
            left++;
         } while (array[left].compareTo(pivot) < 0);

         do {
            right--;
         } while (array[right].compareTo(pivot) > 0);

         if (left >= right) {
            return right;
         }
         swap(array, left, right);
      }
   }

   public static <T extends Comparable<T>> int binarySearch(T aValue, T [] fromArray, int fromIndex, int toIndex) {
      int left = fromIndex;
      int right = toIndex;

      while (left <= right) {
         int middle = left + (right - left) / 2;
         int comparison = aValue.compareTo(fromArray[middle]);
         if (comparison == 0) {
            return middle;
         }
         if (comparison < 0) {
            right = middle - 1;
         } else {
            left = middle + 1;
         }
      }
      return -1;
   }

   public static <T> void reverse(T [] array) {
      for (int left = 0, right = array.length - 1; left < right; left++, right--) {
         swap(array, left, right);
      }
   }

   public static <T> void swap(T [] array, int first, int second) {
      T temporary = array[first];
      array[first] = array[second];
      array[second] = temporary;
   }

   public static <T> int partitionByRule(T [] array, int size, Predicate<T> rule) {
      int left = 0;
      int right = size - 1;

      while (left <= right) {
         while (left < size && !rule.test(array[left])) {
            left++;
         }
         while (right >= 0 && rule.test(array[right])) {
            right--;
         }
         if (left < right) {
            swap(array, left, right);
            left++;
            right--;
         } else {
            break;
         }
      }
      return left;
   }

   public static <T> void sortWithComparator(T [] array, Comparator<T> comparator) {
      quickSortWithComparator(array, 0, array.length - 1, comparator);
   }

   private static <T> void quickSortWithComparator(T [] array, int begin, int end, Comparator<T> comparator) {
      while (begin < end) {
         int split = partitionWithComparator(array, begin, end, comparator);
         if (split - begin < end - split) {
            quickSortWithComparator(array, begin, split, comparator);
            begin = split + 1;
         } else {
            quickSortWithComparator(array, split + 1, end, comparator);
            end = split;
         }
      }
   }

   private static <T> int partitionWithComparator(T [] array, int begin, int end, Comparator<T> comparator) {
      T pivot = array[begin + (end - begin) / 2];
      int left = begin - 1;
      int right = end + 1;

      while (true) {
         do {
            left++;
         } while (comparator.compare(array[left], pivot) < 0);

         do {
            right--;
         } while (comparator.compare(array[right], pivot) > 0);

         if (left >= right) {
            return right;
         }
         swap(array, left, right);
      }
   }
}

