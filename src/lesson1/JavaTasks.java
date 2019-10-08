package lesson1;

import kotlin.NotImplementedError;

import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     *
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     *
     * Пример:
     *
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     *
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     *
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortTimes(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Сортировка адресов
     *
     * Средняя
     *
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     *
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     *
     * Людей в городе может быть до миллиона.
     *
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     *
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortAddresses(String inputName, String outputName) {
        Map<String, ArrayList<String>> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(inputName)))) {
            String str = br.readLine();
            while (str != null) {
                StringBuilder sb = new StringBuilder();
                String[] array = str.split(" - ");
                map.putIfAbsent(array[1], new ArrayList<>());
                map.get(array[1]).add(array[0]);
                str = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, ArrayList<Integer>> homeNumbers = new HashMap<>();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputName)))) {
            for (String str : map.keySet()) {
                String[] temp = str.split(" ");
                Integer homeNumber = Integer.parseInt(temp[1]);
                homeNumbers.putIfAbsent(temp[0], new ArrayList<>());
                homeNumbers.get(temp[0]).add(homeNumber);
            }
            String[] sortedStreets = homeNumbers.keySet().toArray(new String[0]);
            Arrays.sort(sortedStreets);
            for (String str : sortedStreets) {
                Integer[] sortedHomeNumbers = homeNumbers.get(str).toArray(new Integer[0]);
                Arrays.sort(sortedHomeNumbers);
                for (Integer number : sortedHomeNumbers) {
                    StringBuilder streetWithNumber = new StringBuilder();
                    streetWithNumber.append(str);
                    streetWithNumber.append(" ");
                    streetWithNumber.append(number.toString());
                    ArrayList<String> list = map.get(streetWithNumber.toString());
                    Collections.sort(list);
                    bw.write(streetWithNumber.toString());
                    bw.write(" - ");
                    bw.write(list.get(0));
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < list.size(); i++) {
                        sb.append(", ");
                        sb.append(list.get(i));
                    }
                    bw.write(sb.toString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
В map храним все улицы с номерами и имена людей, в homeNumbers ещё улицы и номера в формате Integer,
ресурсоёмкость O(n)
Трудоёмкость, как у сортировки массива O(nlgn)
 */

    /**
     * Сортировка температур
     *
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     *
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     *
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     *
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */
    static public void sortTemperatures(String inputName, String outputName) {
        List<Integer> temperatures = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(inputName)))) {
            String str = br.readLine();
            while (str != null) {
                boolean negative = false;
                if (str.charAt(0) == '-') {
                    negative = true;
                    str = str.substring(1);
                }
                String[] array = str.split("\\.");
                Integer integer = Integer.parseInt(array[0]) * 10 + Integer.parseInt(array[1]);
                if (negative) integer = - integer;
                temperatures.add(integer);
                str = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer[] sortedArray = countingSort(temperatures.toArray(new Integer[0]), -2730, 5000);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputName))) {
            for (Integer integer : sortedArray) {
                if (integer < 0) bw.write("-");
                bw.write(Math.abs(integer) / 10 + "." + Math.abs(integer) % 10 + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Integer[] countingSort(Integer[] elements, int lowLimit, int highLimit) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int i = lowLimit; i <= highLimit; i++) {
            count.put(i, 0);
        }
        for (Integer element : elements) {
            count.put(element, count.get(element) + 1);
        }
        for (int j = lowLimit + 1; j <= highLimit; j++) {
            count.put(j, count.get(j - 1) + count.get(j));
        }
        Integer[] out = new Integer[elements.length];
        for (int j = elements.length - 1; j >= 0; j--) {
            out[count.get(elements[j]) - 1] = elements[j];
            count.put(elements[j], count.get(elements[j]) - 1);
        }
        return out;
    }

    /*
    Ресурсоёмкость O(n), из-за хранения массива
    Трудоёмкость O(n), из-за сортировки подсчётом
     */
    /**
     * Сортировка последовательности
     *
     * Средняя
     * (Задача взята с сайта acmp.ru)
     *
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     *
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     *
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     *
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */
    static public void sortSequence(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Соединить два отсортированных массива в один
     *
     * Простая
     *
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     *
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     *
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        throw new NotImplementedError();
    }
}
