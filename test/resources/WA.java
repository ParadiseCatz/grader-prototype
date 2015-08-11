import java.util.*;
import java.lang.reflect.Array;
import java.io.*;

class WA {
    StringTokenizer tokenizer;
    BufferedReader in;
    static int NMAX = 100100;

    int A, B;
    int[] pre;

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(System.in));
            initVariable();
            solve();
        } catch (Exception e) {
            System.exit(9000);
        }
    }

    String nextToken() throws IOException {
        while (tokenizer == null || !tokenizer.hasMoreTokens()) {
            String line = in.readLine();
            if (line == null) {
                System.exit(0);
            }
            tokenizer = new StringTokenizer(line);
        }
        return tokenizer.nextToken();
    }

    int nextInt() throws NumberFormatException, IOException {
        return Integer.parseInt(nextToken());
    }

    long nextLong() throws NumberFormatException, IOException {
        return Long.parseLong(nextToken());
    }

    double nextDouble() throws NumberFormatException, IOException {
        return Double.parseDouble(nextToken());
    }

    String nextString() throws IOException {
        return nextToken();
    }

    void initVariable() throws NumberFormatException {
        pre = new int[NMAX];
    }

    void solve() throws NumberFormatException, IOException {
        A = nextInt();
        B = nextInt();
        System.out.println(A + B + 1);
    }

    public static void main(String []args) {
        new WA().run();
    }
}