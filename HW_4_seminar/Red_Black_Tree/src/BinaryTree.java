import java.util.ArrayList;
import java.util.List;

public class BinaryTree<T extends Comparable<T>> { // проверяет, есть ли у дженерика метод Comparable, если есть, то допускает
// если тип данных "T" включает Comparable метод типа данных "T", тогда мы можем его использовать - здесь это необходимо для сравнения
    private Node root; // всегда начинается с корня

    public boolean add(T value) {
        if (root == null) {
            root = new Node(value, Color.black);
            return true;
        }
        return addNode(root, value);
    }

    private boolean addNode(Node currentNode, T value) {
        if(currentNode.value.compareTo(value) == 0) { // проверяем на уникальность добавления ноды
            return false; // если нода не уникальна, выходим из метода со значением false
        }
        if (currentNode.value.compareTo(value) > 0) { // currentNode.value > value, если текущая нода больше значения новой ноды, идем в лево
            if (currentNode.left != null) { // проверяем, если левый ребенок существует, то
                boolean result = addNode(currentNode.left, value); // запускаем рекурсию уже с левым ребенком, результатом которой будет добавление ноды
                currentNode.left = rebalanced(currentNode.left); // После этого запускаем балансировку на добавленную ноду
                return result; // возвращаем result и выходим из метода
            } else { // если же левого ребенка не существует, то
                currentNode.left = new Node(value, Color.red); // создаем новую ноду, присваиваем ей значение и красный цвет
                return true;  // выходим из метода и возвращаем true
            }
        } else { // иначе, если текущая нода меньше значения новой ноды, то идем в право
            if (currentNode.right != null) { // так же проверяем, существует ли правый ребенок
                boolean result = addNode(currentNode.right, value); // Так же для правой стороны. Result используем для того, чтобы
                currentNode.right = rebalanced(currentNode.right); // можно было провести ребалансировку
                return result; // и сбалансированный результат вернуть
            } else { // иначе, если правого ребенка нет, то
                currentNode.right = new Node(value, Color.red); // создаем ноду и присваиваем значение и красный цвет
                return true; // выходим из цикла и возвращаем true
            }
        }
    }



    public boolean contains(T value) { // содержится ли такое значение в дереве
        Node currentNode = root; // всегда начинаем с root
        while (currentNode != null) { // запускаем цикл, пока нода существует
            if (currentNode.value.compareTo(value) == 0) { // если значение текущей ноды равно искомому значению
                return true; // то выходим из цикла и говорим true - такое значение содержится в дереве
            }
            if (currentNode.value.compareTo(value) > 0) { // если значение текущей ноды больше искомого значения, то
                currentNode = currentNode.left; // меняем текущую ноду на левого ребенка, и продолжаем цикл заново
            } else { // иначе, если текущее значение ноды меньше искомого значения, то
                currentNode = currentNode.right; // текущую ноду меняем на правого ребенка и продолжаем цикл заново
            }
        }
        return false; // если мы проверили все значения нод и не нашли совпадения, то выводим false и выходим из цикла
    }

    public boolean remove(T value) {
        if (!contains(value)) { // если удаляемого значения нет в дереве, то
            return false; // выходим из метода со статусом false
        }
        Node deleteNode = root; // так же указываем начальную ноду, заменяет currentNode
        while (deleteNode != null){ // пока нода существует
            if (deleteNode.value.compareTo(value) == 0) { // если мы нашли нужную ноду
                if (deleteNode.right == null && deleteNode.left == null) { // если у удоляемой ноды нет правого и левого ребенка, то
                    deleteNode = null; // ноду просто удаляем == null
                } else if (deleteNode.right == null) { // если нет правого ребенка
                    deleteNode.left.color = deleteNode.color; // то цвет левого ребенка должен быть таким же, как в удаляемой ноде
                    deleteNode.value = deleteNode.left.value; // значение левой ноды переносим в удаляемую ноду
                    deleteNode.right = deleteNode.left.right; // и меняем связь удаляемой ноды с правым ребенком на связь левого ребенка удаляемой ноды с его правым ребенком
                    deleteNode.left = deleteNode.left.left; // за тем, меняем связь удаляемой ноды с левым ребенком на связь левого ребенка удаляемой ноды с его левым ребенком
                    deleteNode = rebalanced(deleteNode);
                } else { // если нет левого ребенка
                    Node replaceNode = deleteNode.right; // создаем ноду и присваиваем значение правой ноды
                    Node prereplace = replaceNode; // так же создаем еще одну ноду и присваиваем значение предыдущей созданной ноды
                    while (replaceNode.left != null){ // идем максимально в лево, и каждый раз
                        prereplace = replaceNode; // переприсваиваем значения нод
                        replaceNode = replaceNode.left; // и ищем последнего левого ребенка
                    }
                    deleteNode.value = replaceNode.value; // в значение удаляемой ноды присваиваем значение последнего левого ребенка
                    if (replaceNode == prereplace) { // используем if для удаления последнего левого ребенка, когда родитель равен ребенку
                        deleteNode.right = null; // так как он переходит на место удаляемой ноды, если детей нет
                    } else {
                        replaceNode.left = null; // а левого ребенка удаляем, = null, если дети есть
                    }
                    deleteNode = rebalanced(deleteNode);
                    return true; // и возвращаем true
                }
                return true; // сработал первый if, вышли из метода
            }
            if (deleteNode.value.compareTo(value) > 0) { // начинаем перебирать детей, если текущее значение больше искомого, то
                deleteNode = deleteNode.left; // идем в лево
            } else { // иначе
                deleteNode = deleteNode.right; // идем в право
            }
        }
        return false; // если цикл закончился, выдаем false
    }

    private Node rebalanced (Node node) {
        Node result = node; // создаем ноду равной текущей ноде (заданной)
        boolean needRebalance; // переменная для понимания, что нужна ребалансировке
        do {
            needRebalance = false; // Заходим в цикл и балансировку ставим false. Если не один if не сработает - выйдем из цикла
            if (result.right != null && result.right.color == Color.red && // проверяем, если есть правая нода и её цвет красный
                    (result.left == null || result.left.color == Color.black)) { // и, нет левой ноды или она черного цвета
                needRebalance = true; // то нам нужна ребалансировка, малый правы поворот, отправляем снова в цикл
                result = rightSwap(result); // делаем балансировку, для этого мы возвращали ноду при свайпах
            }
            if (result.left != null && result.left.color == Color.red && // проверяем, если есть левая нода и она красного цвета и,
                    result.left.left != null && result.left.left.color == Color.red) { // существует левый ребенок левой ноды и его цвет красный
                needRebalance = true; // то нужна ребалансировка, отправляем снова в цикл
                result = leftSwap(result); // делаем левый малый поворот
            }
            if (result.left != null && result.left.color == Color.red // если существует левая нода и она красного цвета и
                    && result.right != null && result.right.color == Color.red) {// существует правая нода и её цвет красный
                needRebalance = true; // То нужна ребалансировка цвета, отправляем снова в цикл
                colorSwap(result); // меняем цвета
            }
        } while (needRebalance); // делаем ребалансировку пока она нужна (true)
        return result;
    }
    private void colorSwap(Node node) { // метод корректировки цвета нод
        node.right.color = Color.black; // говорим что правый ребенок имеет черный цвет
        node.left.color = Color.black; // левый ребенок имеет черный цвет
        node.color = Color.red; // и родитель имеет красный цвет
    }

    private Node leftSwap (Node node){ // малый левый поворот
        Node left = node.left; // создаем левую ноду, которая равна левому ребенку
        Node between = left.right; // промежуточная нода сейчас равна правому ребенку левого ребенка
        left.right = node; // меняем местами, родителя ставим на место правого ребенка левого ребенка
        node.left = between; // меняем связь, теперь левый ребенок ноды ссылается на промежуточную ноду, а не на левого ребенка
        left.color = node.color; // цвет должен остаться тем же, что был у родителя
        node.color = Color.red; // нода, которую мы перемещаем всегда будет красной
        return left; // делаем на главенствующую ноду для дальнейшей балансировки
    }

    private Node rightSwap (Node node){ // противоположенный левому малому повороту
        Node right = node.right;
        Node between = right.left;
        right.left = node;
        node.right = between;
        right.color = node.color;
        node.color = Color.red;
        return right;
    }
    private class Node {
        private T value;
        private Node left;
        private Node right;
        private Color color;

        public Node() { // создаем конструктор ноды
            value = null;
            left = null;
            right = null;
            color = null;
        }

        public Node(T value) {
            this.value = value;
            left = null;
            right = null;
            color = null;
        }

        public Node(T value, Color color) {
            this.value = value;
            left = null;
            right = null;
            this.color = color;
        }
    }
    private class PrintNode {
        Node node;
        String str;
        int depth;

        public PrintNode() {
            node = null;
            str = " ";
            depth = 0;
        }

        public PrintNode(Node node) {
            depth = 0;
            this.node = node;
            str = node.value.toString();
        }
    }

    private int nodeCount(Node node, int count){
        if (node != null) {
            count++;
            return count + nodeCount(node.left, 0) + nodeCount(node.right, 0);
        }
        return count;
    }

    public int maxDepth() { //+
        return maxDepth2(0, root);
    }

    private int maxDepth2(int depth, Node node) { //+
        depth++;
        int left = depth;
        int right = depth;
        if (node.left != null) {
            left = maxDepth2(depth, node.left);
        }
        if (node.right != null) {
            right = maxDepth2(depth, node.right);
        }
        return left > right ? left : right;
    }

    public void print() { //+
        int maxDepth = maxDepth() + 3;
        int nodeCount = nodeCount(root, 0);
        int width = 50;
        int height = nodeCount * 6;
        List<List<PrintNode>> list = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            ArrayList<PrintNode> row = new ArrayList<>();
            for (int j = 0; j < width; j++){
                row.add(new PrintNode());
            }
            list.add(row);
        }
        list.get(height / 2).set(0, new PrintNode(root)); // выставляем root в центр
        list.get(height / 2).get(0).depth = 0; // и ставим глубину = 0
        for (int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++) {
                PrintNode currentNode = list.get(i).get(j);
                if (currentNode.node != null){ // как только мы находим ноду
                    currentNode.str = currentNode.node.value.toString(); // сразу в строку записываем переведенное значение ноды
                    if (currentNode.node.left != null) { // смотрим, у этой ноды есть левый ребенок, если да, то
                        int in = i + (maxDepth / (int) Math.pow(2, currentNode.depth)); // высчитывается то, куда поставится (номер строки)
                        int jn = j + 3; // координаты того, куда поставится наш ребенок (номер столбца)
                        printLines(list, i, j, in, jn); // печатаются линии от текущего положения до нужных координат
                        list.get(in).get(jn).node = currentNode.node.left; // в то место, куда мы ставим ноду, мы записываем эту самую ноду
                        list.get(in).get(jn).depth = list.get(i).get(j).depth + 1; // и высчитываем глубину, т.е. в глубину текущей ноды мы записываем глубину предыдущей ноды +1
                    }
                    if(currentNode.node.right != null) {
                        int in = i - (maxDepth / (int) Math.pow(2, currentNode.depth));
                        int jn = j + 3;
                        printLines(list, i, j, in, jn);
                        list.get(in).get(jn).node = currentNode.node.right;
                        list.get(in).get(jn).depth = list.get(i).get(j).depth + 1;
                    }
                }
            }
        }
        for (int i = 0; i < height; i++) { // чистка пустых строк
            boolean flag = true;
            for (int j = 0; j < width; j++) {
                if (list.get(i).get(j).str != " ") {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                list.remove(i);
                i--;
                height--;
            }
        }

        for (var row : list) {
            for (var item : row) {
                if (item.node != null && item.node.color == Color.red) {
                    System.out.print("\u001B[31m" + item.str + " " + "\u001B[0m");
                } else {
                    System.out.print(item.str + " ");
                }
            }
            System.out.println();
        }
    }

    private void printLines(List<List<PrintNode>> list, int i, int j, int i2, int j2) { //+
        // i и j - это текущий индекс (место), i2 и j2 - это до куда нужно провести линию
        if (i2 > i) {
            while (i < i2) { // если текущая высота меньше, чем до которой нужно дойти
                i++; // фиксируем шаг
                list.get(i).get(j).str = "|"; // рисуем палочку
            }
            list.get(i).get(j).str = "\\"; // если дошли, рисуем палки и идем в сторону
            while (j < j2){
                j++;
                list.get(i).get(j).str = "-";
            }
        } else {
            while (i > i2) {
                i--;
                list.get(i).get(j).str = "|";
            }
            list.get(i).get(j).str = "/";
            while (j < j2) {
                j++;
                list.get(i).get(j).str = "-";
            }
        }
    }

}

enum Color {
    black, red;
}

