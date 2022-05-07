import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 * @Author: Feng Jixuan
 * @Date: 2022-04-2022-04-28
 * @Description: source
 * @version=1.0
 */
public class main {
    static double[][] distance;
    static double[] p;

    public static void main(String[] args) throws IOException {
        String fileName = null;
        for (String arg : args) {
            if (arg.startsWith("-file=")) {
                fileName = arg.substring(6);
            }
        }
        String data_location = "D:\\1学习\\SRTP结题\\source\\data\\";
        distance = readDist(data_location + fileName + ".dis");
        int n = distance.length;
        int depot = 0;
        int[] tour = readTour(data_location + fileName + ".tour", n, depot);
        p = new double[n + 1];
        Random rd = new Random();
        for (int i = 0; i < n + 1; i++) p[i] = rd.nextDouble()*0.2+0.6;
        long startTime = System.currentTimeMillis();
        p[0] = 1;
        p[n] = 1;
        double cost = 0;
        cost = calCost(tour);
        double currentCost = cost, bestCost = cost,initialCost=cost;
        int[] initialTour=Arrays.copyOf(tour,tour.length);

        int[] bestTour = Arrays.copyOf(tour, tour.length);
        int[] currentTour = Arrays.copyOf(tour, tour.length);
        ;
        int T = 10000, t = 1, Maxiter = 1000;
        int i, j;
        double apCurrent, apNeigh;
        double k=1;


        while (k<n/4) {
//            System.out.print("k="+k+"  cost1="+currentCost);
            for (int r = 1; r <= 40000-10*k; r++) {

                i = rd.nextInt(n - 1) + 1;
                j = rd.nextInt(n - 1) + 1;
                while (i == j) {
                    j = rd.nextInt(n - 1) + 1;
                }
                if (i > j) {
                    int f = i;
                    i = j;
                    j = f;
                }
                int[] neighbor;
                boolean f=rd.nextBoolean();
                if (f){
                     neighbor = twoOpt(currentTour, i, j);
                }else{
                     neighbor = oneShift(currentTour, i, j);
                }
//                neighbor = twoOpt(currentTour, i, j);
                apCurrent = ApCost(currentTour, i, j, (int)k);
                apNeigh = ApCost(neighbor, i, j, (int)k);
//                                System.out.println(k+" apcu="+apCurrent+"  apnei="+apNeigh);

                if (apCurrent - apNeigh > 0) {
                    currentTour = Arrays.copyOf(neighbor, neighbor.length);
                    currentCost = currentCost - apCurrent + apNeigh;
                }
            }
            currentCost = calCost(currentTour);
            //            System.out.println(Arrays.toString(currentTour));
            if (currentCost < bestCost) {
                bestCost = currentCost;
                bestTour = Arrays.copyOf(currentTour, currentTour.length);

            }
//            System.out.println(" cost2="+currentCost);
            k = k +3;
        }
        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间：" + (endTime - startTime)*1.0/1000 + "s");
        System.out.println("Initial tour cost = " + initialCost);
        System.out.println("Initial tour : " + Arrays.toString(initialTour));
        System.out.println("best cost =" + bestCost);
        System.out.println("best tour : " + Arrays.toString(bestTour));
        System.out.println("-------------------------------------");


//


    }

    static int[] oneShift(int[] tour, int i, int j) {
        int[] sol = new int[tour.length];
        for (int r = 0; r < tour.length; r++) {
            sol[r] = tour[r];
        }
        int r = sol[i];
        sol[i] = sol[j];
        sol[j] = r;
        return sol;
    }

    static int[] twoOpt(int[] tour, int i, int j) {
        int[] sol = new int[tour.length];
        for (int r = 0; r < tour.length; r++) {
            if (r >= i && r <= j) {
                sol[r] = tour[j - (r - i)];
            } else {
                sol[r] = tour[r];
            }
        }
        return sol;
    }

    static double ApCost(int[] tour, int i, int j, int k) {
        int l1, l2, l3, l4;
        double c1, c2;
        l1 = i - k >= 0 ? i - k : 0;
        l2 = i - 1;
        l3 = i;
        l4 = i + k - 1 <= j ? i + k - 1 : j;
//        if (k>98) System.out.println("ij"+i+" "+j);
//        if (k>98) System.out.println("apcost "+l1+" "+l2+" "+l3+" "+l4+" "+i);
        c1 = pairCost(tour, l1, l2, l3, l4);
        l1 = j - k + 1 >= i ? j - k + 1 : i;
        l2 = j;
        l3 = j + 1;
        l4 = j + k <= tour.length - 1 ? j + k : tour.length - 1;

//        if (k>98) System.out.println("apcost "+l1+" "+l2+" "+l3+" "+l4+" "+j);
        c2 = pairCost(tour, l1, l2, l3, l4);
        return c1 + c2;

    }

    static double pairCost(int[] tour, int l1, int l2, int l3, int l4) {
        double cost = 0;
        double t1 = 1, t2 = 1;
        for (int i = l1; i <= l2; i++) {
            for (int j = l3; j <= l4; j++) {
                if (i == l1 && j == l3) {
                    for (int k = l1 + 1; k <= l3 - 1; k++) {
                        t1 = t1 * (1 - p[tour[k]]);
                    }
                    t1 = t1 * p[tour[l1]] * p[tour[l3]];

                    t2 = t1;
                } else if (j == l3) {
                    t1 = t2 / p[tour[i - 1]] / (1 - p[tour[i]]) * p[tour[i]];
                    t2 = t1;
                } else {
                    t1 = t1 / p[tour[j - 1]] * (1 - p[tour[j - 1]]) * p[tour[j]];
                }
                //k截断
//                if (i == l1) {
//                    cost += t1 / p[tour[i]] * distance[tour[i]][tour[j]];
//                }
                cost += t1 * distance[tour[i]][tour[j]];
            }
        }
        return cost;
    }

    static int[] readTour(String fileName, int n, int depot) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = null;
        int[] tour = new int[n + 1];
        int count = 1;
        while ((line = br.readLine()) != null) {
            if (count >= 7 && count <= 6 + n) {
                tour[count - 7] = Integer.parseInt(line.strip()) - 1;
            }
            count++;
        }
//        System.out.println(Arrays.toString(tour));
        int index = 0;
        for (int i = 0; i < n; i++) {
            if (tour[i] == depot) {
                index = i;
                break;
            }
        }
        int[] a = {0, index, 0};
        int[] b = {index - 1, n - 1, n - 1};
        for (int i = 0; i < 3; i++) {
            for (; a[i] < b[i]; a[i]++, b[i]--) {
                int t = tour[a[i]];
                tour[a[i]] = tour[b[i]];
                tour[b[i]] = t;
            }
        }
        tour[n] = depot;
        return tour;
    }

    static double calCost(int[] tour) {
        int n = tour.length;
        double cost = 0;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                double t = 1;
                for (int k = i + 1; k < j; k++) {
                    t = t * (1 - p[tour[k]]);
                }
                cost = cost + t * p[tour[i]] * p[tour[j]] * distance[tour[i]][tour[j]];
            }
        }
        return cost;
    }

    static double[][] readDist(String fileName) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = null;
        String[] sp = null;
        line = br.readLine();
        int n = Integer.parseInt(line.strip());
        double[][] distance = new double[n][n];
        int c = 0;
        while ((line = br.readLine()) != null) {
            sp = line.strip().split(" ");
            for (int i = 0; i < n; i++) {
                distance[c][i] = Double.parseDouble(sp[i]);
            }
            c++;
        }
        return distance;

    }
}
