package lesson3;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
/* Большинство алгоритмов я брал из книги "Алгоритмы построение и анализ" за авторством:
Томаса Кормена, Чарльза Лейзерсона, Рональда Ривеста и Клиффорда Штайна */

// Attention: comparable supported but comparator is not
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;

        Node<T> left = null;

        Node<T> right = null;

        // переменная родителя

        Node<T> parent = null;

// Функция для нахождения минимального элемента в дереве

        Node(T value) { this.value = value;
        }


        Node<T> minimum() {
            Node<T> current = this;
            while (current.left != null) {
                current = current.left;
            }
            return current;
        }

    }


    private Node<T> root = null;

    private int size = 0;

// Подкорректировал под родителя
    @Override
    public boolean add(T t) {
        Node<T> closest = findNear(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        }
        else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
            newNode.parent = closest;
        }
        else {
            assert closest.right == null;
            closest.right = newNode;
            newNode.parent = closest;
        }
        size++;
        return true;
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    public int height() {
        return height(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */
    @Override
    public boolean remove(Object o) {
        T value = (T) o;
        Node<T> node = find(value);
        return delete(node);
    }
    /*
    Память: O(1)
    Сложность: O(lgn)
    */

// Функция удаления Nod'а которая удаляет в дереве переданный объект

    private boolean delete(Node<T> node) {
        if (node == null) return false;
        if (node.left == null) {
            transplant(node, node.right);
        } else if (node.right == null) {
            transplant(node, node.left);
        } else {
            Node<T> y = node.right.minimum();
            if (y.parent != node) {
                transplant(y, y.right);
                y.right = node.right;
                y.right.parent = y;
            }
            transplant(node, y);
            y.left = node.left;
            y.left.parent = y;
        }
        size--;
        return true;
    }

    // Функция трансплант, которая заменяет поддерево,являющееся дочернем по отношению к поддереву, другим родителем.

    private void transplant(Node<T> to, Node<T> from) {
        if (to.parent == null) {
            root = from;
        } else if (to.equals(to.parent.left)) {
            to.parent.left = from;
        } else {
            to.parent.right = from;
        }
        if (from != null) {
            from.parent = to.parent;
        }
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }
//Поиск следущего элемента в дереве

    private Node<T> findNext(Node<T> xpos) {
        if (root == null) return null;
        if (xpos == null) return root.minimum();
        if (xpos.right != null) {
            return xpos.right.minimum();
        }
        Node<T> ypos = xpos.parent;
        while (ypos != null && xpos == ypos.right) {
            xpos = ypos;
            ypos = ypos.parent;
        }
        return ypos;
    }

    //Нахождение определенного объекта в дереве поменял и добавил поиск ближайшего объекта

    private Node<T> find(T value) {
        return find(root, value);
    }

    private Node<T> find(Node<T> node, T value) {
        while (node != null && !value.equals(node.value)) {
            if (value.compareTo(node.value) < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return node;
    }

    private Node<T> findNear(T value) {
        if (root == null) return null;
        return findNear(root, value);
    }

    private Node<T> findNear(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return findNear(start.left, value);
        }
        else {
            if (start.right == null) return start;
            return findNear(start.right, value);
        }
    }

    public class BinaryTreeIterator implements Iterator<T> {

        private Node<T> next;
        private Node<T> now = null;

        private BinaryTreeIterator() {
            next = findNext(null);
        }

        /**
         * Проверка наличия следующего элемента
         * Средняя
         */
        @Override
        public boolean hasNext() {
            return next != null;

        }
        //   Память: O(1)
        //   Сложность: O(1)

        /**
         * Поиск следующего элемента
         * Средняя
         */
        @Override
        public T next() {
            now = next;
            if (now == null) throw new NoSuchElementException();
            next = findNext(now);
            return now.value;
        }
        //   Память: O(1)
        //   Сложность: O(lgn)

        /**
         * Удаление следующего элемента
         * Сложная
         */
        @Override
        public void remove() {
            if (now == null) throw new IllegalStateException();
            BinaryTree.this.delete(now);
            now = null;
        }
    }
    //     Память: O(1)
    //     Сложность: O(lgn)

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    @Override
    public int size() {
        return size;
    }


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        // TODO
        throw new NotImplementedError();
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }
}
