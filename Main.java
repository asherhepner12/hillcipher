import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.lang.Math.sqrt;

/*
REFERENCES (APA):

Worthington, W. (n.d.). Hill cipher project [PDF].
https://sites.math.unt.edu/~tushar/S10Linear2700%20%20Project_files/Worthington%20Paper.pdf

GeeksforGeeks. (2021, July 21). Hill cipher.
https://www.geeksforgeeks.org/dsa/hill-cipher/

GeeksforGeeks. (2025, February 17). Euclidean algorithms (basic and extended).
https://www.geeksforgeeks.org/dsa/euclidean-algorithms-basic-and-extended/

Bikenaga, B. (n.d.). The extended Euclidean algorithm.
https://sites.millersville.edu/bikenaga/number-theory/extended-euclidean-algorithm/extended-euclidean-algorithm.html
*/


//Numbers: 65-90
public class Main {

    public static final int ALPHABETSIZE = 26;
    public static final int ASCIIADJUST = 65;

    public static int[][] createKeyMatrix(String keyString) throws Exception
    {
        int keyMatrixSize = (int) Math.sqrt(keyString.length());
        if (keyMatrixSize * keyMatrixSize != keyString.length())
        {
            throw new Exception("Key string is not the right size for an nxn matrix!");
        }
        else
        {
            int[][] keyMatrix = new int[keyMatrixSize][keyMatrixSize];
            for (int row = 0; row < keyMatrixSize; row++)
            {
                for (int col = 0; col < keyMatrixSize; col++)
                {
                    char c = keyString.charAt(row * keyMatrixSize + col);
                    if ((c >= 'A') && (c <= 'Z'))
                    {
                        keyMatrix[row][col] = c - ASCIIADJUST;
                    }
                    else
                    {
                        throw new Exception("Key string contains invalid characters!");
                    }
                }
            }
            return keyMatrix;
        }
    }

    public static void printMatrix(int[][] matrix)
    {
        System.out.print("| ");
        for (int row = 0; row < matrix.length; row++)
        {
            for (int col = 0; col < matrix[row].length; col++)
                {
                System.out.print(matrix[row][col] + " ");
                }
            System.out.print("| ");
        }
        System.out.println();
    }
    public static void printMatrix(List<List<Integer>> vectorList)
    {
        System.out.print("| ");
        for (int i = 0; i < vectorList.size(); i++)
        {
            for (int j= 0; j < vectorList.get(i).size(); j++)
            {
                System.out.print(vectorList.get(i).get(j) + " ");
            }
            System.out.print("| ");
        }
        System.out.println();
    }

    public static List<List<Integer>> stringToVectorList(String message, List<List<Integer>> vectorList, int size) throws Exception
    {
        if (message.length() % size != 0)
        {

            for(int i = 0; i < message.length() % size; i++)
            {message += "X";}
            System.out.println("Input string after adjusting for vector size: " + message);
        }


        //Turn string into ascii numbers
        int[] asciiMsg = new int[message.length()];
        for(int i = 0; i < message.length(); i++)
        {
            asciiMsg[i] = message.charAt(i) - ASCIIADJUST;
            if (asciiMsg[i] < 0 || asciiMsg[i] > ALPHABETSIZE)
            {
                throw new Exception("Input string contains invalid characters!");
            }
        }

        //create vectors of the ascii numbers
        int msgIndex = 0;
        for (int vectorIndex = 0; vectorIndex < (message.length() / size); vectorIndex++)
        {
            ArrayList<Integer> vector = new ArrayList<Integer>();
            for(int i = 0; i < size; i++)
            {
                vector.add(asciiMsg[msgIndex]);
                msgIndex++;
            }
            vectorList.add(vector);
        }
        return vectorList;
    }

    public static String convertVectorList(List<List<Integer>> vectorList, int[][] matrix)
    {
        List<List<Integer>> encrVectorList = new ArrayList<List<Integer>>();
        int elem = 0;
        for(int a = 0; a < vectorList.size(); a++)
        {
            ArrayList<Integer> encrVector = new ArrayList<Integer>();

            for(int b = 0; b < vectorList.get(a).size(); b++)
            {
                for (int c = 0; c < matrix.length; c++) {
                    elem += (matrix[b][c] * vectorList.get(a).get(c));
                }
                encrVector.add(elem % ALPHABETSIZE);
                elem = 0;
            }
            encrVectorList.add(encrVector);
        }

        System.out.print("Converted vectors: ");
        printMatrix(encrVectorList);


        StringBuilder encryptedStringBuilder = new StringBuilder();

        for (int i = 0; i < vectorList.size(); i++)
        {
            for (int j= 0; j < vectorList.get(i).size(); j++)
            {
                encryptedStringBuilder.append((char) (encrVectorList.get(i).get(j) + ASCIIADJUST));
            }
        }
        String encryptedString = encryptedStringBuilder.toString();
        return encryptedString;
    }

    public static int findDeterminant(int[][] matrix)
    {
        //Turn Array into ArrayList
        List<List<Integer>> matrixList = new ArrayList<List<Integer>>();
        for (int i = 0; i < matrix.length; i++)
        {
            ArrayList<Integer> row = new ArrayList<Integer>();
            for (int j = 0; j < matrix[i].length; j++)
            {row.add(matrix[i][j]);}
            matrixList.add(row);
        }
        //Run the real function
        return findDeterminant(matrixList);
    }
    public static int findDeterminant(List<List<Integer>> matrix)
    {
        //Base case for a 1x1 matrix (finding the adjMatrix)
        if (matrix.size() == 1)
        {return matrix.get(0).get(0);}
        //Base case for 2x2 determinant
        if (matrix.size() == 2) {
            return (matrix.get(0).get(0) * matrix.get(1).get(1)) - (matrix.get(0).get(1) * matrix.get(1).get(0));
        }
        else
        {
            int determinant = 0;
            for(int cofactor = 0; cofactor < matrix.size(); cofactor++)
            {
                //Create a smaller matrix excluding the row/col of the cofactor
                List<List<Integer>> cofactorMatrix = new ArrayList<List<Integer>>();
                for(int row = 1; row < matrix.size(); row++)
                {
                    ArrayList<Integer> rowList = new ArrayList<Integer>();
                    for (int col = 0; col < matrix.size(); col++)
                    {
                        if (col != cofactor)
                        {rowList.add(matrix.get(row).get(col));}
                    }
                    cofactorMatrix.add(rowList);
                }
                //Recursively call the function to break the matrix down into 2x2 matrices
                determinant += (int) (matrix.get(0).get(cofactor) * Math.pow(-1,cofactor) * findDeterminant(cofactorMatrix));
            }
            return determinant;
        }
    }

    //Extended Euclidean Algorithm, derived from: https://www.geeksforgeeks.org/dsa/euclidean-algorithms-basic-and-extended/
    // det * det-1 + 26 * y = 1
    public static int gcd(int a, int b, int[] x, int[] y)
    {
       if (a == 0)
       {
        x[0] = 0;
        y[0] = 1;
        return b;
       }

       int[] x1 = {0}, y1 = {0};
       int num = gcd(b % a, a, x1, y1);

       x[0] = y1[0] - (b / a) * x1[0];
       y[0] = x1[0];
       return num;
    }

    public static int findDeterminantInverse (int det) throws Exception {
        //Check if there is an inverse in the determinant
        //If the length of the alphabet (ALPHABETSIZE) is coprime with the determinant, it exists
        int[] x = {1}, y = {1};
        if (gcd(det, ALPHABETSIZE, x, y) != 1)
        {throw new Exception("Matrix is not invertible mod " + ALPHABETSIZE +"!");}
        else
        {
            //Make it within the modular range
            return (x[0] % ALPHABETSIZE + ALPHABETSIZE) % ALPHABETSIZE;
        }
    }

    public static int[][] findCofactorMatrix (int[][] matrix)
    {
        int[][] cofactorMatrix = new int[matrix.length][matrix[0].length];
        //Use a bunch of gross nested loops to find the cofactor matrix
        //First set is to iterate through each element in matrix
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix.length; col++)
            {
                List<List<Integer>> smallMatrix = new ArrayList<List<Integer>>();
                //Create smaller matrices to find the minor of each element
                for (int smallRow = 0; smallRow < matrix.length; smallRow++)
                {
                    if (smallRow != row)
                    {
                        ArrayList<Integer> smallRowList = new ArrayList<Integer>();
                        for (int smallCol = 0; smallCol < matrix[row].length; smallCol++)
                        {
                            if (smallCol != col)
                            {smallRowList.add(matrix[smallRow][smallCol]);}
                        }
                        smallMatrix.add(smallRowList);
                    }
                }
                //multiply determinant of minor matrix with the sign of the element's placement
                cofactorMatrix [row] [col] = (int) (Math.pow(-1, row+col+2) * findDeterminant(smallMatrix));
            }
        }
        return cofactorMatrix;
    }
    public static int[][] findAdjugateMatrix (int[][] matrix)
    {
        int[][] cofactorMatrix = findCofactorMatrix (matrix);
        int[][] adjMatrix = new int[matrix.length][matrix[0].length];
        //Transpose the cofactor matrix
        for (int row = 0; row < matrix.length; row++)
        {
            for (int col = 0; col < matrix[row].length; col++)
            {
                adjMatrix[col][row] = cofactorMatrix[row][col];
            }
        }
        return adjMatrix;
    }

    public static int[][] invertMatrix (int[][] matrix) throws Exception
    {
        /*
        STEPS:
        1. Find det(A)
            a. Use recursive determinant algorithm
        2. Find mod inverse of det(A)
            a. Use extended Euclidean algorithm
            b. If NO mod inverse exists, ERROR
        3. Find adj(A)
            a. Get all cofactors of A
            b. Transpose the cofactor matrix
        4. Find A-1
            a. Take adj(A) * mod inverse of det(A)
            b. Take that % ALPHABETSIZE
         */
            int det = findDeterminant(matrix);
            System.out.println("Determinant: " + det);
            int detInverse = findDeterminantInverse(det);
            System.out.println("Inverted determinant: " + detInverse);

            int [][] adjMatrix = findAdjugateMatrix(matrix);

            //Take the adjugate matrix modulo alphabetsize
            for (int i = 0; i < adjMatrix.length; i++)
            {
                for (int j = 0; j < adjMatrix[i].length; j++)
                {
                    adjMatrix[i][j] = ((detInverse * adjMatrix[i][j]) % ALPHABETSIZE + ALPHABETSIZE) % ALPHABETSIZE;
                }
            }
;
            return adjMatrix;

    }

    public static void main(String[] args) throws Exception {
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter a string of characters (Capital A-Z ONLY) ");
                System.out.println("that can be square rooted to create an nxn key matrix: ");
                String keyString = scanner.nextLine();
                int[][] matrix = createKeyMatrix(keyString);
                System.out.print("Key matrix: ");
                printMatrix(matrix);
                int[][] invertedMatrix = invertMatrix(matrix);
                System.out.print("Inverted key matrix: ");
                printMatrix(invertedMatrix);
                int choice = 0;
                while (choice != 3) {
                    System.out.println("Enter a number: ");
                    System.out.println("============================");
                    System.out.println("1. Encrypt a String");
                    System.out.println("2. Decrypt a String");
                    System.out.println("3. Exit");
                    System.out.println("============================");
                    try {
                        choice = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        throw new Exception("Input was not a number!");
                    }

                    scanner.nextLine();
                    switch (choice) {
                        case 1:
                            System.out.println("Enter a string of characters to encrypt (Capital A-Z ONLY) : ");
                            //Create string and lengthen it to fit all letters into vectors
                            String message = scanner.nextLine();
                            List<List<Integer>> unencryptedVectorList = new ArrayList<List<Integer>>();
                            stringToVectorList(message, unencryptedVectorList, matrix.length);


                            //Print message as unencrypted vector
                            System.out.print("String as vectors: ");
                            printMatrix(unencryptedVectorList);

                            //Print encrypted vector and its resulting String
                            String encryptedString = convertVectorList(unencryptedVectorList, matrix);
                            System.out.println("ENCRYPTED STRING: " + encryptedString);
                            break;
                        case 2:
                            //Invert the key matrix

                            System.out.println("Enter a string of characters to decrypt (Capital A-Z ONLY) : ");

                            //Decrypt the String
                            String encryptedMessage = scanner.nextLine();
                            List<List<Integer>> encryptedVectorList = new ArrayList<List<Integer>>();
                            stringToVectorList(encryptedMessage, encryptedVectorList, matrix.length);

                            System.out.print("String as vectors: ");
                            printMatrix(encryptedVectorList);

                            String decryptedString = convertVectorList(encryptedVectorList, invertedMatrix);
                            System.out.println("DECRYPTED STRING: " + decryptedString);
                            break;
                        default:
                            choice = 3;
                            System.exit(0);
                    }
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
                System.out.println("RESTARTING PROGRAM.");
                System.out.println("============================");
            }


        }
    }
}