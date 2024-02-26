// Bartosz Bugajski - 5
import java.util.Scanner;

/*
Dla kazdego zestawu w programie dostajemy na wejsciu n liczb w kolejnosci preorder lub postorder a nastepnie w kolejnosci
inorder. Na podstawie tych dwoch zestawow, mamy stworzyc drzewo binarne, a nastepnie wypisac jego wezly w preorder lub
postorder (w zaleznosci od tego, jaka byla dana na wejsciu, mamy wybrac ta brakujaca) oraz w levelorder. Wypisywanie w kolejnosciach
postorder, preorder oraz levelorder bylo opisane na wykladzie, jest to identyczna implementacja. Glowna trudnoscia w zadaniu
jest stworzenie odpowiedniego drzewa na podstawie jego opisu w dwoch roznych porzadkach. W tym celu najpierw wczytuje je do
dwoch tablic o odpowiednim rozmiarze, a nastepnie wywoluje jedna z dwoch metod klasy Tree, ktora "buduje" to drzewo.
 */

class Queue // kolejka potrzebna do implementacji wypisywania drzewa w levelorder
{
    private int maxSize; // maksymalna liczba elementow
    private Node[] elements; // tablica elementow
    private int front; // wskaznik na poczatek kolejki
    private int rear; // wskaznik na koniec kolejki

    public Queue(int size) // konstruktor
    {
        maxSize = size; // ustawiamy liczbe elementow
        elements = new Node[maxSize]; // tworzymy tablice o podanej liczbie elementow
        front = 0; // ustawiamy poczatek na 0
        rear = -1; // ustawiamy koniec na -1
    }

    public void insert(Node n)
    {
        elements[++rear] = n; // dodajemy zadany element na koniec kolejki
    }

    public boolean isEmpty()
    {
        return front > rear; // jesli poczatek jest wiekszy od konca to kolejka jest pusta
    }

    public Node remove()
    {
        return elements[front++]; // usuwamy pierwszy element z kolejki i go zwracamy
    }
}

class Node // wezel drzewa
{
    public int info; // identyfikator wezla
    public Node left; // wezel na lewo
    public Node right; // wezel na prawo

    public Node(int x) // konstruktor
    {
        info = x;
        left = null;
        right = null;
    }

    public void display() // wypisanie pola info oraz spacji
    {
        System.out.printf(info + " ");
    }
}

class Tree
{
    public Node root; // korzen drzewa
    public int index; // aktualny indeks (potrzebny do "budowania" drzewa)

    public Tree() // konstruktor
    {
        root = null;
    }

    public void preorder(Node n) // wypisywanie w porzadku preorder
    {
        if(n != null)
        {
            n.display();
            preorder(n.left);
            preorder(n.right);
        }
    }

    public void postorder(Node n) // wypisywanie w porzadku postorder
    {
        if (n != null)
        {
            postorder(n.left);
            postorder(n.right);
            n.display();
        }
    }

    public void levelorder(Node s) // wypisywanie w porzadku levelorder
    {
        Queue Q = new Queue(Source.treeTops);
        Q.insert(s);
        while(!Q.isEmpty())
        {
            Node u = Q.remove();
            u.display();
            if(u.left != null) Q.insert(u.left);
            if(u.right != null) Q.insert(u.right);
        }
    }

    int search(int[] arr, int x) // szukanie w tablicy elementu o danej wartosci (zwraca indeks tego elementu)
    {
        int i;
        for(i = 0; i < arr.length; i++)
        {
            if(arr[i] == x) return i;
        }
        return i;
    }

    public Node buildFromPre(int[] inorder, int[] preorder, int start, int end) // budowanie drzewa z tablicy w preorder
    {
        if(start > end) return null;
        // pierwszym elementem tablicy preorder jest korzen, potem sa elementy ne lewo od niego i na lewo od kazdego z nich,
        // a nastepnie na prawo od kazdego z nich idac od "najglebszego"

        // idziemy od zera, wiec przy kazdym wejsciu w rekurencje zwiekszamy index
        Node n = new Node(preorder[index++]);

        if (start == end) return n; // koniec rekurencji, zwracamy utworzony wezel

        int nindex = search(inorder, n.info);

        n.left = buildFromPre(inorder, preorder, start, nindex - 1); // tworzymy wezel na lewo od aktualnego
        n.right = buildFromPre(inorder, preorder, nindex + 1, end); // tworzymy wezel na prawo od aktualnego
        return n; // zwracamy korzen
    }

    public Node buildFromPost(int[] inorder, int[] postorder, int start, int end) // budowanie drzewa z tablicy w postorder
    {
        if(start > end) return null;
        // ostatnim elementem tablicy postorder jest nasz korzen, potem sa elementy na prawo od niego i na prawo od kazdego
        // z tych elementow, a nastepnie na lewo od kazdego z tych elementow idac od "najglebszego"

        // w tym przypadku idziemy od konca, wiec przy kazdym wejsciu w rekurencje zmniejszamy index
        Node n = new Node(postorder[index--]);

        if (start == end) return n; // koniec rekurencji, zwracamy utworzony wezel

        int nindex = search(inorder, n.info); // szukamy w tablicy inorder indeksu elementu z wartoscia info


        n.right = buildFromPost(inorder, postorder, nindex + 1, end); // tworzymy wezel na prawo od aktualnego
        n.left = buildFromPost(inorder, postorder, start, nindex - 1); // tworzymy wezel na lewo od aktualnego
        return n; // zwracamy korzen
    }
}

public class Source
{
    public static Scanner sc = new Scanner(System.in);
    public static int treeTops; // liczba wezlow (wierzcholkow) drzewa
    public static String order; // porzadek wejscia (PREORDER lub POSTORDER)
    public static void main(String[] args)
    {
        int sets = sc.nextInt(); // liczba zestawow
        for(int i = 0; i < sets; i++)
        {
            treeTops = sc.nextInt(); // wczytanie liczby wezlow
            order = sc.next(); // wczytanie porzadku wejsciowego
            Tree t1 = new Tree(); // utworzenie drzewa

            int[] nodeInfos = new int[treeTops]; // tablica w porzadku preorder lub postorder
            for(int j = 0; j < treeTops; j++)
            {
                nodeInfos[j] = sc.nextInt();
            }

            sc.next();
            int[] nodeInfosInorder = new int[treeTops]; // tablica w porzadku inorder
            for(int j = 0; j < treeTops; j++)
            {
                nodeInfosInorder[j] = sc.nextInt();
            }

            System.out.println("ZESTAW " + (i+1));
            if(order.compareTo("PREORDER") == 0)
            {
                // jesli porzadek to preorder to ustawiamy index na 0, a nastepnie wywolujemy funkcje budowania drzewa
                // z tablicy w porzadku preorder
                t1.index = 0;
                t1.root = t1.buildFromPre(nodeInfosInorder, nodeInfos, 0, treeTops-1);

                // wypisujemy utworzone drzewo w porzadku postorder
                System.out.println("POSTORDER");
                t1.postorder(t1.root);
            }
            else
            {
                // jesli porzadek to postorder to ustawiamy index na liczbe wezlow - 1, a nastepnie wywolujemy funkcje
                // budowania drzewa z tablicy w porzadku postorder
                t1.index = treeTops - 1;
                t1.root = t1.buildFromPost(nodeInfosInorder, nodeInfos, 0, treeTops-1);

                // wypisujemy utworzone drzewo w porzadku preorder
                System.out.println("PREORDER");
                t1.preorder(t1.root);
            }
            System.out.println();

            // wypisujemy utworzone drzewo w porzadku levelorder
            System.out.println("LEVELORDER");
            t1.levelorder(t1.root);
            System.out.println();
        }
    }
}

/*
INPUT:
5
19
PREORDER
1 2 4 7 10 11 5 8 12 15 16 3 6 9 13 17 14 18 19
INORDER
10 7 11 4 2 5 8 15 12 16 1 3 6 17 13 9 18 19 14
19
POSTORDER
10 11 7 4 15 16 12 8 5 2 17 13 19 18 14 9 6 3 1
INORDER
10 7 11 4 2 5 8 15 12 16 1 3 6 17 13 9 18 19 14
1
POSTORDER
1
INORDER
1
5
PREORDER
1 2 3 4 5
INORDER
1 2 3 4 5
5
PREORDER
1 2 3 4 5
INORDER
5 4 3 2 1

OUTPUT:
ZESTAW 1
POSTORDER
10 11 7 4 15 16 12 8 5 2 17 13 19 18 14 9 6 3 1
LEVELORDER
1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19
ZESTAW 2
PREORDER
1 2 4 7 10 11 5 8 12 15 16 3 6 9 13 17 14 18 19
LEVELORDER
1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19
ZESTAW 3
PREORDER
1
LEVELORDER
1
ZESTAW 4
POSTORDER
5 4 3 2 1
LEVELORDER
1 2 3 4 5
ZESTAW 5
POSTORDER
5 4 3 2 1
LEVELORDER
1 2 3 4 5
 */