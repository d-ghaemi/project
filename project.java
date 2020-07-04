
import java.util.ArrayList;
import java.util.Scanner;

public class project {

    static ArrayList<node> N=new ArrayList<node>(0);
    static ArrayList<resistor> R=new ArrayList<resistor>(0);
    static ArrayList<currentSource> CS=new ArrayList<currentSource>(0);
    static ArrayList<voltageSource> VS=new ArrayList<voltageSource>(0);
    static ArrayList<capacitor> C=new ArrayList<capacitor>(0);

    public static int searchNode(String s){
        for(int i=0;i<N.size();i++){
            if(N.get(i).name.equals(s)){
                return i;
            }
        }
        return -1;
    }


    public static class node{
        String name;
        double volt[];
        node(String s){
            name=s;
        }
    }
    abstract public static class branch{
        node n1, n2;
        double I[];
    }
    public static class resistor extends branch {
        String NameR;
        double R;
        resistor(String s){
            NameR=s.substring(0,s.indexOf(" "));
            s=s.substring(s.indexOf(" ")+1);
            node n=new node(s.substring(0, s.indexOf(" ")));
            int i=searchNode(s.substring(0,s.indexOf(" ")));
            if(i==-1){
                N.add(n);
                n1=n;
            }
            else{
                n1=N.get(i);
            }
            s=s.substring(s.indexOf(" ")+1);
            n=new node(s.substring(0, s.indexOf(" ")));
            i=searchNode(s.substring(0,s.indexOf(" ")));
            if(i==-1){
                N.add(n);
                n2=n;
            }
            else{
                n2=N.get(i);
            }
            s=s.substring(s.indexOf(" ")+1);
            s=s.replaceAll("k", "000");
            s=s.replaceAll("M", "000000");
            s=s.replaceAll("G", "000000000");
            if(s.indexOf("m")!=-1){
                R=0.001;
                s=s.replaceAll("m","");
            }
            if(s.indexOf("u")!=-1){
                R=0.000001;
                s=s.replaceAll("u", "");
            }
            else if(s.indexOf("n")!=-1){
                R=0.000000001;
                s=s.replaceAll("n", "");
            }
            else if(s.indexOf("p")!=-1){
                R=0.000000000001;
                s=s.replaceAll("p", "");
            }
            else {
                R=1;
            }
            R*=Double.parseDouble(s);
        }
        void calc_I(int t){
            I[t]=(n1.volt[t]-n2.volt[t])/R;
        }
        void calc_v1(int t){
            n1.volt[t]=n2.volt[t]+I[t]*R;
        }
        void calc_v2(int t){
            n2.volt[t]=n1.volt[t]-I[t]*R;
        }
    }
    public class capacitor extends branch {
        String NameC;
        double C, Q[]={}, Q0[]={};
        void calc_QwithV(double dT, int t){
            Q0[t]=Q[t];
            Q[t]=C*(n1.volt[t]-n2.volt[t]);
            I[t]=(Q[t]-Q0[t])/dT;
        }
        void calc_Q1withI(double dT, int t){
            Q0[t]=Q[t];
            Q[t]=Q0[t]+d*I;
            n1.volt=(Q-Q0)/C+n2.volt;
        }
        void calc_Q2withI(double dt){
            Q0=Q;
            Q=Q0+dt*I;
            n2.volt=n1.volt-(Q-Q0)/C;
        }
    }
    public class inductor extends branch {
        String NameL;
        double L, I0=0;
        void calcI(double dt){
            I0=I;
            I=I0+dt*(n1.volt-n2.volt)/L;
        }
        void calcV1(double dt){
            n1.volt=L*(I-I0)/dt+n2.volt;
        }
        void calcV2(double dt){
            n2.volt=n1.volt-L*(I-I0)/dt;
        }
    }
    public static class voltageSource extends branch {
        String NameV;
        double V, freq;
        voltageSource(String s){
            NameV=s.substring(0,s.indexOf(" "));
            s=s.substring(s.indexOf(" ")+1);
            node n=new node(s.substring(0, s.indexOf(" ")));
            int i=searchNode(s.substring(0,s.indexOf(" ")));
            if(i==-1){
                N.add(n);
                n1=n;
            }
            else{
                n1=N.get(i);
            }
            s=s.substring(s.indexOf(" ")+1);
            n=new node(s.substring(0, s.indexOf(" ")));
            i=searchNode(s.substring(0,s.indexOf(" ")));
            if(i==-1){
                N.add(n);
                n2=n;
            }
            else{
                n2=N.get(i);
            }
            s=s.substring(s.indexOf(" ")+1);
            s=s.replaceAll("k", "000");
            s=s.replaceAll("M", "000000");
            s=s.replaceAll("G", "000000000");
            if(s.indexOf("u")!=-1){
                V=0.001;
                s=s.replaceAll("u", "");
            }
            else if(s.indexOf("n")!=-1){
                V=0.000001;
                s=s.replaceAll("u", "");
            }
            else if(s.indexOf("p")!=-1){
                V=0.000000001;
                s=s.replaceAll("u", "");
            }
            else {
                V=1;
            }
            V*=Double.parseDouble(s.substring(0, s.indexOf(" ")));
        }
        void calcV1(){
            n1.volt=V+n2.volt;
        }
        void calcV2(){
            n2.volt=n1.volt-V;
        }
    }
    public static class currentSource extends branch {
        String NameI;
        double freq;
        currentSource(String s){
            NameI=s.substring(0,s.indexOf(" "));
            s=s.substring(s.indexOf(" ")+1);
            node n=new node(s.substring(0, s.indexOf(" ")));
            int i=searchNode(s.substring(0,s.indexOf(" ")));
            if(i==-1){
                N.add(n);
                n1=n;
            }
            else{
                n1=N.get(i);
            }
            s=s.substring(s.indexOf(" ")+1);
            n=new node(s.substring(0, s.indexOf(" ")));
            i=searchNode(s.substring(0,s.indexOf(" ")));
            if(i==-1){
                N.add(n);
                n2=n;
            }
            else{
                n2=N.get(i);
            }
            s=s.substring(s.indexOf(" ")+1);
            s=s.replaceAll("k", "000");
            s=s.replaceAll("M", "000000");
            s=s.replaceAll("G", "000000000");
            if(s.indexOf("u")!=-1){
                I=0.001;
                s=s.replaceAll("u", "");
            }
            else if(s.indexOf("n")!=-1){
                I=0.000001;
                s=s.replaceAll("u", "");
            }
            else if(s.indexOf("p")!=-1){
                I=0.000000001;
                s=s.replaceAll("u", "");
            }
            else {
                I=1;
            }
            I*=Double.parseDouble(s.substring(0, s.indexOf(" ")));
        }
    }
    public class diode extends branch {
        String NameD;
        void checdiod(){
            if(I<0){
                I=0;
            }
            else if(n1.volt-n2.volt>0){
                n1.volt=n2.volt;
            }
        }
    }

    static class Gauss_Jordan_Elimination {
        private static final double EPSILON = 1e-8;
        private static int N;
        private static double[][] a;
        public Gauss_Jordan_Elimination(double[][] A, double[] b) {
            N = b.length;
            a = new double[N][N+N+1];
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    a[i][j] = A[i][j];
            for (int i = 0; i < N; i++)
                a[i][N+i] = 1.0;
            for (int i = 0; i < N; i++)
                a[i][N+N] = b[i];
            show();
            solve();
            assert check(A, b);
        }
        private void solve() {
            for (int p = 0; p < N; p++) {
                int max = p;
                for (int i = p+1; i < N; i++) {
                    if (Math.abs(a[i][p]) > Math.abs(a[max][p])) {
                        max = i;
                    }
                }
                swap(p, max);
                if (Math.abs(a[p][p]) <= EPSILON) {
                    continue;
                }
                pivot(p, p);
            }
        }
        private void swap(int row1, int row2) {
            double[] temp = a[row1];
            a[row1] = a[row2];
            a[row2] = temp;
        }
        private void pivot(int p, int q) {
            for (int i = 0; i < N; i++) {
                double alpha = a[i][q] / a[p][q];
                for (int j = 0; j <= N+N; j++) {
                    if (i != p && j != q) a[i][j] -= alpha * a[p][j];
                }
            }
            for (int i = 0; i < N; i++)
                if (i != p) a[i][q] = 0.0;
            for (int j = 0; j <= N+N; j++)
                if (j != q) a[p][j] /= a[p][q];
            a[p][q] = 1.0;
        }
        public double[] primal() {
            double[] x = new double[N];
            for (int i = 0; i < N; i++) {
                if (Math.abs(a[i][i]) > EPSILON)
                    x[i] = a[i][N+N] / a[i][i];
                else if (Math.abs(a[i][N+N]) > EPSILON)
                    return null;
            }
            return x;
        }
        public double[] dual() {
            double[] y = new double[N];
            for (int i = 0; i < N; i++) {
                if ( (Math.abs(a[i][i]) <= EPSILON) && (Math.abs(a[i][N+N]) > EPSILON) ) {
                    for (int j = 0; j < N; j++)
                        y[j] = a[i][N+j];
                    return y;
                }
            }
            return null;
        }
        public boolean isFeasible() {
            return primal() != null;
        }
        private static void show() {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    System.out.print(a[i][j]+"  ");
                }
                System.out.print("|");
                for (int j = N; j < N+N; j++) {
                    System.out.print(a[i][j]+"  ");
                }
                System.out.println("| "+a[i][N+N]);
            }
            System.out.println();
        }
        private boolean check(double[][] A, double[] b) {
            if (isFeasible()) {
                double[] x = primal();
                for (int i = 0; i < N; i++) {
                    double sum = 0.0;
                    for (int j = 0; j < N; j++) {
                        sum += A[i][j] * x[j];
                    }
                    if (Math.abs(sum - b[i]) > EPSILON) {
                        System.out.println("not feasible");
                        System.out.println(i+" = "+b[i]+", sum = "+sum+"\n");
                        return false;
                    }
                }
                return true;
            }
            else {
                double[] y = dual();
                for (int j = 0; j < N; j++) {
                    double sum = 0.0;
                    for (int i = 0; i < N; i++) {
                        sum += A[i][j] * y[i];
                    }
                    if (Math.abs(sum) > EPSILON) {
                        System.out.println("invalid certificate of infeasibility");
                        System.out.println("sum = "+sum+"\n");
                        return false;
                    }
                }
                double sum = 0.0;
                for (int i = 0; i < N; i++) {
                    sum += y[i] * b[i];
                }
                if (Math.abs(sum) < EPSILON) {
                    System.out.println("invalid certificate of infeasibility");
                    System.out.println("yb  = "+sum+"\n");
                    return false;
                }
                return true;
            }
        }
        public static void test(double[][] A, double[] b) {
            Gauss_Jordan_Elimination gaussian = new Gauss_Jordan_Elimination(A, b);
            if (gaussian.isFeasible()) {
                System.out.println("Solution to Ax = b");
                double[] x = gaussian.primal();
                for (int i = 0; i < x.length; i++) {
                    System.out.println(" "+x[i]+"\n");
                }
            }
            else {
                System.out.println("Certificate of infeasibility");

                double[] y = gaussian.dual();
                for (int j = 0; j < y.length; j++) {
                    System.out.println(" "+y[j]+"\n");
                }

            }
            show();
            System.out.println();
        }
    }
    //KCL soloution
    static double calcGii(int i){
        double gii=0;
        for(int k=0;k<R.size();k++){
            if(R.get(k).n2.name.equals(N.get(i).name)||R.get(k).n1.name.equals(N.get(i).name)){
                if(!R.get(k).n2.name.equals(R.get(k).n1.name))
                    gii+=1.0/(R.get(k).R);
            }
        }
        return gii;
    }
    static double calcGij(int i, int j){
        double gij=0;
        for(int k=0;k<R.size();k++){
            if(R.get(k).n2.name.equals(N.get(i).name)&&R.get(k).n1.name.equals(N.get(j).name)){
                gij-=1.0/(R.get(k).R);
            }
            else if(R.get(k).n1.name.equals(N.get(i).name)&&R.get(k).n2.name.equals(N.get(j).name)){
                gij-=1.0/(R.get(k).R);
            }
        }
        return gij;
    }
    static double calcJi(int i){
        double ji=0;
        for(int k=0;k<CS.size();k++){
            if(CS.get(k).n2.name.equals(N.get(i).name)){
                ji+=CS.get(k).I;
            }
            if(CS.get(k).n1.name.equals(N.get(i).name)){
                ji-=CS.get(k).I;
            }
        }
        return ji;
    }


    public static void replaceVS(int i){
        String n2VName=new String(VS.get(i).n2.name);
        String n1VName=new String(VS.get(i).n1.name);
        int j=searchNode(n2VName);
        for(int k=0;k<R.size();k++){
            if(R.get(k).n1.name.equals(n2VName)){
                currentSource csV=new currentSource("IV"+Integer.toString(k)+" "+n1VName+" "+R.get(k).n2.name+" "+Double.toString(VS.get(i).V/R.get(k).R)+" 0 0 0");
                System.out.println("IV"+Integer.toString(i)+Integer.toString(k)+" "+n1VName+" "+R.get(k).n2.name+" "+Double.toString(VS.get(i).V/R.get(k).R)+" 0 0 0");
                CS.add(csV);
            }
            else if(R.get(k).n2.name.equals(n2VName)){
                currentSource csV=new currentSource("IV"+Integer.toString(i)+Integer.toString(k)+" "+n1VName+" "+R.get(k).n1.name+" "+Double.toString(VS.get(i).V/R.get(k).R)+" 0 0 0");
                CS.add(csV);
            }
        }
        for (int k=0;k<R.size();k++){
            if(R.get(k).n1.name.equals(n2VName)){
                R.get(k).n1.name=n1VName;
            }
            else if(R.get(k).n2.name.equals(n2VName)){
                R.get(k).n2.name=n1VName;
            }
        }
        N.remove(j);

    }



    public static void main(String[] args) {
        resistor r;
        capacitor c;
        currentSource cs;
        voltageSource vs;
        Scanner sc=new Scanner(System.in);
        String s=sc.nextLine();
        s=s.trim();
        s=s.replaceAll("( )+", " ");

        while (!s.equals(".end")){
            if(s.charAt(0)=='R'){
                r=new resistor(s);
                R.add(r);
            }
            else if(s.charAt(0)=='I'){
                cs=new currentSource(s);
                CS.add(cs);
            }
            else if(s.charAt(0)=='V'){
                vs=new voltageSource(s);
                VS.add(vs);
            }
            else if(s.charAt(0)=='C') {
                //c=new capacitor(s);
                // C.add(c);
            }

            s=sc.nextLine();
            s=s.trim();
            s=s.replaceAll("( )+", " ");
        }


        for(int i=0;i<VS.size();i++) {
            replaceVS(i);
        }

        if(CS.size()>0){
            int n = N.size()-1;
            double [][]mat = new double[n][n];
            double []constants = new double[n];
            for(int i=0; i<n; i++) {
                for(int j=0; j<n; j++) {
                    if(i==j){
                        mat[i][j] = calcGii(i);
                    }
                    else{
                        mat[i][j] = calcGij(i, j);
                    }
                }
                constants[i] = calcJi(i);
            }
            Gauss_Jordan_Elimination.test(mat, constants);
        }


    }
}
