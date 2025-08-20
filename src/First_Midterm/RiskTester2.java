//package First_Midterm;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//class Risk {
//
//    private List<Integer> X;
//    private List<Integer> Y;
//
//    int napad=0;
//    int odbrana=0;
//
//    public Risk() {
//        this.X = new ArrayList<>();
//        this.Y = new ArrayList<>();
//    }
//
//    public Risk(List<Integer> x, List<Integer> y) {
//        X = x;
//        Y = y;
//    }
//
//    public static Risk createPlayers(String line) {
//        String[] parts = line.split(";");
//        String firstPlayer = parts[0]; //5 3 4
//        String secondPlayer = parts[1];
//
//        String[] Xs = firstPlayer.split("\\s+");
//        String[] Ys = secondPlayer.split("\\s+");
//
//        List<Integer> p1 = new ArrayList<>();
//        List<Integer> p2 = new ArrayList<>();
//
//
//        for (String X : Xs) {
//            p1.add(Integer.parseInt(X));
//        }
//
//        for (String Y : Ys) {
//            p2.add(Integer.parseInt(Y));
//        }
//
//        return new Risk(p1, p2);
//    }
//
//    public int checkWinningCondition() {
//
//        if (X.isEmpty() && Y.isEmpty())
//            return 1;
//
//
//
//        Optional<Integer> maxX = X.stream().max(Comparator.naturalOrder());
//
//        Optional<Integer> maxY = Y.stream().max(Comparator.naturalOrder());
//
//
//        if (maxX.get() > maxY.get()) {
//            napad++;
//            this.X.remove(maxX.get());
//            this.Y.remove(maxY.get());
//            return checkWinningCondition();
//        } else {
//            odbrana++;
//        }
//        return 0;
//    }
//
//
//    //5 3 4;2 4 1
//    //da vrati kolku pati pobedil napagjachot
//    public void processAttacksData(InputStream in) {
//        BufferedReader br = new BufferedReader(new InputStreamReader(in));
//
//
//        int n=0, o=0;
//        List<Risk> risk = br.lines().map(Risk::createPlayers).collect(Collectors.toList());
//
//        int winningCounter = 0;
//        for (Risk r : risk) {
//            if (r.checkWinningCondition() == 1) {
//                winningCounter++;
//            }
//        }
//        System.out.println(String.format("%d %d", napad, odbrana));
//    }
//}
//
//public class RiskTester2 {
//    public static void main(String[] args) {
//        Risk risk = new Risk();
//        risk.processAttacksData(System.in);
//    }
//}
