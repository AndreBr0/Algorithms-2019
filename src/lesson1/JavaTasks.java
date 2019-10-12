package lesson1;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;

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

    private static class Address implements Comparable<Address> {

        private String streetName;
        private Integer streetNumber;

        public String getStreetName() {
            return streetName;
        }

        public void setStreetName(String streetName) {
            this.streetName = streetName;
        }

        public Integer getStreetNumber() {
            return streetNumber;
        }

        public void setStreetNumber(Integer streetNumber) {
            this.streetNumber = streetNumber;
        }

        private static final Comparator<Address> COMPARATOR = Comparator
                .comparing(Address::getStreetName)
                .thenComparingInt(Address::getStreetNumber);

        @Override
        public int compareTo(@NotNull Address o) {
            return COMPARATOR.compare(this, o);
        }

        @Override
        public String toString() {
            return streetName + " " + streetNumber;
        }
    }


    private static class Person implements Comparable<Person> {

        private String personName;
        private String personSurname;

        public String getPersonName() {
            return personName;
        }

        public void setPersonName(String personName) {
            this.personName = personName;
        }

        public String getPersonSurname() {
            return personSurname;
        }

        public void setPersonSurname(String personSurname) {
            this.personSurname = personSurname;
        }

        private static final Comparator<Person> COMPARATOR = Comparator
                .comparing(Person::getPersonSurname).thenComparing(Person::getPersonName);

        @Override
        public int compareTo(@NotNull Person o) {
            return COMPARATOR.compare(this, o);
        }

        @Override
        public String toString() {
            return personSurname + " " + personName;
        }
    }

    static public void sortAddresses(String inputName, String outputName) throws IOException {
        TreeMap<Address, TreeSet<Person>> map = new TreeMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(inputName)))) {
            String str = br.readLine();
            while (str != null) {
                Address address = new Address();
                String[] array = str.split(" ");
                address.setStreetName(array[3]);
                address.setStreetNumber(Integer.parseInt(array[4]));
                Person person = new Person();
                person.setPersonSurname(array[0]);
                person.setPersonName(array[1]);
                map.putIfAbsent(address, new TreeSet<>());
                map.get(address).add(person);
                str = br.readLine();
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputName)))) {
            for (Map.Entry<Address, TreeSet<Person>> entry : map.entrySet()) {
                bw.write(entry.getKey().toString());
                bw.write(" - ");
                boolean firstWasFlag = false;
                for (Person person : entry.getValue()) {
                    if (firstWasFlag) {
                        bw.write(", ");
                    }
                    bw.write(person.toString());
                    firstWasFlag = true;
                }
                bw.newLine();
            }
        }
    }
    /*
    В map храним улицы и имена людей, ресурсоёмкость O(n)
    Добавляем улицы и людей в treeset, трудоёмкость O(nlgn)
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
    public static void sortTemperatures(String inputName, String outputName) throws IOException {
        int positiveInterval = 5001;
        int negativeInterval = 2730;
        int[] counter = new int[negativeInterval + positiveInterval];
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
                if (negative) {
                    counter[integer + positiveInterval - 1] += 1;
                } else {
                    counter[integer] += 1;
                }
                str = br.readLine();
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputName))) {
            for (int i = counter.length - 1; i >= positiveInterval; i--) {
                int integer = i - positiveInterval + 1;
                StringBuilder sb = new StringBuilder();
                String string = sb.append("-").append(integer / 10).append(".").append(integer % 10).toString();
                for (int j = 0; j < counter[i]; j++) {
                    bw.write(string);
                    bw.newLine();
                }
            }
            for (int i = 0; i < positiveInterval; i++) {
                StringBuilder sb = new StringBuilder();
                String string = sb.append(i / 10).append(".").append(i % 10).toString();
                for (int j = 0; j < counter[i]; j++) {
                    bw.write(string);
                    bw.newLine();
                }
            }
        }
    }
    /*
    Ресурсоёмкость O(1)
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
