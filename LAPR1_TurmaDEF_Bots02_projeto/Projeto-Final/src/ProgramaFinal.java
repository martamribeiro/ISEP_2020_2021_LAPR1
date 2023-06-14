import org.la4j.Matrix;
import org.la4j.decomposition.EigenDecompositor;
import org.la4j.matrix.dense.Basic2DMatrix;
import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class ProgramaFinal {

//==============================================================
//                    VARIÁVEIS GLOBAIS
//==============================================================

    static final int valorFinal = 5000;

//==============================================================
//                           MAIN
//==============================================================

    public static void main(String[] args) throws IOException, InterruptedException {
        Locale.setDefault(Locale.US);
        int pos;
        boolean bool = false;
        Scanner ler = new Scanner(System.in);
        int tempo;
        int nrespe;
        String namefile = "";
        String name = "";
        double nata;
        double morta;
        double[][] Leslie = null;
        int[] x = null;
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        if (((args.length==0 || (args[0].equals("-n")) && args.length==2))){
            if (args.length==0) {
                System.out.println("Que tipo de espécie pretende estudar ?");
                namefile = ler.next();
                do{
                    System.out.println("Quantos intervalos de tempo deseja ? (No máximo 200)");
                    tempo = ler.nextInt();
                }
                while(tempo > 200 || tempo <= 0);
                x = new int[tempo ];
                double[] itens = new double[tempo];
                double[] itens2 = new double[tempo ];
                Leslie = new double[tempo][tempo];
                for (int i= 0; i <= tempo - 1  ; i++) {
                    System.out.println("Quantas espécies há no tempo " + i  + " ?");
                    nrespe = ler.nextInt();
                    System.out.println("Qual é a taxa de natalidade no tempo " + i + " ?");
                    nata = ler.nextDouble();
                    System.out.println("Qual é a taxa de sobrevivencia no tempo " + i + " ?");
                    morta = ler.nextDouble();
                    x[i] = nrespe;
                    itens[i] = nata;
                    itens2[i] = morta;
                }
                for (int coluna = 0; coluna <= tempo - 1; coluna++) {
                    int l = 0;
                    Leslie[l][coluna] = itens[coluna];
                }
                int l = 1;
                int c = 0;
                do {
                    Leslie[l][c] = itens2[c];
                    l++;
                    c++;
                } while (c <= tempo - 1 && l <= tempo - 1);
            } else if (args[0].equals("-n")) {
                File Ficheiro = new File(args[1]);
                bool = Ficheiro.exists();
                while (bool == false) {
                    System.out.println("Ficheiro nao existe");
                    System.exit(0);
                }
                boolean b = false, c=false;
                boolean [] bu = new boolean[1];
                Scanner ler_ficheiro = new Scanner(Ficheiro);
                String [] linhas = new String [3];
                b = lerFich(ler_ficheiro,linhas);
                if(b){
                    System.out.println("O seu ficheiro tem um problema");
                    System.exit(0);
                }else {
                    int tamanho = linhas[0].split(", ").length;
                    x = A(tamanho, linhas, bu);
                    Leslie = new double[tamanho][tamanho];
                    c = Leslie(linhas, Leslie, tamanho);
                    ler_ficheiro.close();
                }
                if (c || bu[0]){
                    System.out.println("O seu ficheiro tem um problema");
                    System.exit(0);
                }
                namefile = Ficheiro.getName();
                namefile = namefile.substring(0, namefile.lastIndexOf("."));
            }
            int geraçaoi=0;
            int geraçao;
            System.out.println("Até que geração pretende estimar ?");
            geraçaoi = ler.nextInt();
            while (geraçaoi < 0) {
                System.out.println("Valor de geração incorreto, tente outra vez");
                geraçaoi = ler.nextInt();
            }
            geraçao = geraçaoi + 1;
            String type = "png";
            String answer = "";
            String filename = "png";
            System.out.println("Pretende gerar gráficos ? (sim/nao)");
            answer = ler.next();
            if (!answer.equals("sim") && !answer.equals("nao")) {
                System.out.println("Resposta incorreta, introduza sim ou nao");
                answer = ler.next();
            }
            double[] popTotalAno = new double[valorFinal];
            double[][] matrizB = new double[Leslie.length][Leslie.length];
            for (int i = 0; i < x.length; i++) {
                popTotalAno[0] = popTotalAno[0] + x[i];
            }
            double[] inicionorma = new double[x.length];
            double[][] normalizaçao = new double[geraçao][x.length];
            double[][] nNormal = new double [geraçao][x.length];
            inicionorma = normalizacaoInicial(geraçao,x);
            if (geraçao>1) {
                popTotalAno[1] = populaçaoGeraçao(nNormal, normalizaçao, 1, Leslie, x);
                matrizquadrada(Leslie, matrizB);
            }
            if (geraçao>2) {
                popTotalAno[2] = populaçaoGeraçao(nNormal, normalizaçao, 2, matrizB, x);
            }
            for (int i = 3; i < geraçao; i++) {
                matrizT(Leslie, matrizB, geraçao);
                popTotalAno[i] = populaçaoGeraçao(nNormal,normalizaçao,i,matrizB, x);
            }
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            pos = Menu();
            while(pos != 0){
                if (pos == 1){
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    escreverVariaçaoMenu(popTotalAno, geraçao, pos);
                    if (answer.equals("sim")) {
                        saveG(gnuplot(geraçao, popTotalAno, filename, type, pos, nNormal, x.length, x, normalizaçao, inicionorma, true, namefile),type, filename,geraçao, popTotalAno, pos, nNormal, x.length, x, normalizaçao, inicionorma, true, namefile);
                    }
                    System.out.println();
                    System.out.println("Clique 0 para voltar ao menu");
                    String valor = ler.next();
                    while (!valor.equals("0")){
                        System.out.println("Clique 0 para voltar ao menu");
                        valor = ler.next();
                    }
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                }
                else if (pos == 2){
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    escreverVariaçaoMenu(popTotalAno,geraçao,pos);
                    if (answer.equals("sim")) {
                        saveG(gnuplot(geraçao, popTotalAno, filename, type, pos, nNormal, x.length, x, normalizaçao, inicionorma, true, namefile),type, filename,geraçao, popTotalAno, pos, nNormal, x.length, x, normalizaçao, inicionorma, true, namefile);
                    }
                    System.out.println();
                    System.out.println("Clique 0 para voltar ao menu");
                    String valor = ler.next();
                    while (!valor.equals("0")){
                        System.out.println("Clique 0 para voltar ao menu");
                        valor = ler.next();
                    }
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                }
                else if (pos==3){
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    System.out.println("Distribuiçao nao Normalizada");
                    System.out.println("Geração inicial:");
                    for(int i = 0; i < x.length; i++){
                        System.out.print("Classe" +(i+1)+ " = ");
                        System.out.print( x[i]);
                        System.out.println();
                    }
                    System.out.println();
                    for(int i = 1; i < geraçao; i++){
                        System.out.println("Geração "+(i)+":");
                        for(int f = 0; f < x.length; f++){
                            System.out.print("Classe"+(f+1)+ " = ");
                            System.out.printf("%.2f" , nNormal[i][f]);
                            System.out.println();
                        }
                        System.out.println();
                    }
                    if (answer.equals("sim")) {
                        saveG(gnuplot(geraçao, popTotalAno, filename, type, pos, nNormal, x.length, x, normalizaçao, inicionorma, true, namefile),type, filename,geraçao, popTotalAno, pos, nNormal, x.length, x, normalizaçao, inicionorma, true, namefile);
                    }
                    System.out.println();
                    System.out.println("Clique 0 para voltar ao menu");
                    String valor = ler.next();
                    while (!valor.equals("0")){
                        System.out.println("Clique 0 para voltar ao menu");
                        valor = ler.next();
                    }
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                }
                else if (pos==4){
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    System.out.println("Distribuiçao Normalizada");
                    System.out.println("Geração inicial:");
                    for(int i = 0; i < x.length; i++){
                        System.out.print("Classe" +(i+1)+ " = ");
                        System.out.printf("%.2f" , inicionorma[i]*100);
                        System.out.print("%");
                        System.out.println();
                    }
                    System.out.println();
                    for(int i = 1; i < geraçao; i++){
                        System.out.println("Normalização para geração "+(i)+":");
                        for(int f = 0; f < x.length; f++){
                            System.out.print("Classe"+(f+1)+ " = ");
                            System.out.printf("%.2f" , normalizaçao[i][f]*100);
                            System.out.print("%");
                            System.out.println();
                        }
                        System.out.println();
                    }
                    if (answer.equals("sim")) {
                        saveG(gnuplot(geraçao, popTotalAno, filename, type, pos, nNormal, x.length, x, normalizaçao, inicionorma, true, namefile),type, filename,geraçao, popTotalAno, pos, nNormal, x.length, x, normalizaçao, inicionorma, true, namefile);
                    }
                    System.out.println();
                    System.out.println("Clique 0 para voltar ao menu");
                    String valor = ler.next();
                    while (!valor.equals("0")){
                        System.out.println("Clique 0 para voltar ao menu");
                        valor = ler.next();
                    }
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                }
                else if (pos==5){
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    double[][] valoresP = valoresProprios(Leslie);
                    double max = maiorValor(valoresP);
                    int col = colunaMaiorValor(valoresP);
                    double [] per = vetorProprio(Leslie, col);
                    assimtotico(max,per);
                    System.out.println();
                    System.out.println("Clique 0 para voltar ao menu");
                    String valor = ler.next();
                    while (!valor.equals("0")){
                        System.out.println("Clique 0 para voltar ao menu");
                        valor = ler.next();
                    }
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                }
                else if(pos == 7){
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    String texto;
                    int resp;
                    System.out.println("1.Deseja inserir a matriz manualmente ?   2.Deseja ler um ficheiro ?");
                    resp = ler.nextInt();
                    while((resp != 2) && (resp != 1)){
                        System.out.println("Valor Incorreto");
                        System.out.println("1.Deseja inserir a matriz manualmente ?   2.Deseja ler um ficheiro ?");
                        resp = ler.nextInt();
                    }
                    geraçaoi = 0;
                    if(resp == 2) {
                        System.out.println("Digite o nome do ficheiro (formato txt)");
                        texto = ler.next();
                        File Ficheiro = new File(texto);
                        boolean bool1 = Ficheiro.exists();
                        while (bool1 == false) {
                            System.out.println("Ficheiro nao existe");
                            System.out.println("Introduza um novo ficheiro (formato txt)");
                            texto=ler.next();
                            Ficheiro = new File(texto);
                            bool1 = Ficheiro.exists();
                        }
                        boolean b = false, c=false;
                        boolean [] bu = new boolean[1];
                        int ct=0;
                        do{
                            if (ct>0) {
                                System.out.println("O seu ficheiro tem um erro, por favor introduza um novo ficheiro (formato txt)");
                                texto=ler.next();
                                Ficheiro = new File(texto);
                                bool1 = Ficheiro.exists();
                                while (bool1 == false) {
                                    System.out.println("Ficheiro nao existe");
                                    System.out.println("Introduza um novo ficheiro (formato txt)");
                                    texto=ler.next();
                                    Ficheiro = new File(texto);
                                    bool1 = Ficheiro.exists();
                                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                                }
                            }
                            Scanner ler_ficheiro = new Scanner(Ficheiro);
                            String[] linhas = new String[3];
                            b = lerFich(ler_ficheiro, linhas);
                            if (!b) {
                                int tamanho = linhas[0].split(", ").length;
                                x = A(tamanho, linhas, bu);
                                Leslie = new double[tamanho][tamanho];
                                c = Leslie(linhas, Leslie, tamanho);
                                ler_ficheiro.close();
                            }
                            ct+=1;
                            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                        }while(b || c || bu[0]);
                        namefile = Ficheiro.getName();
                        namefile = namefile.substring(0, namefile.lastIndexOf("."));
                        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    } else{
                        System.out.println("Que tipo de espécie pretende estudar ?");
                        namefile = ler.next();
                        do{
                            System.out.println("Quantos intervalos de tempo deseja ? (No máximo 200)");
                            tempo = ler.nextInt();
                        }
                        while(tempo > 200 || tempo <= 0);
                        x = new int[tempo ];
                        double[] itens = new double[tempo];
                        double[] itens2 = new double[tempo ];
                        Leslie = new double[tempo ][tempo ];
                        for (int i= 0; i <= tempo - 1  ; i++) {
                            System.out.println("Quantas especies há no tempo " + i  + " ?");
                            nrespe = ler.nextInt();
                            System.out.println("Qual é a taxa de natalidade no tempo " + i + " ?");
                            nata = ler.nextDouble();
                            System.out.println("Qual é a taxa de sobrevivencia no tempo " + i + " ?");
                            morta = ler.nextDouble();
                            x[i] = nrespe;
                            itens[i] = nata;
                            itens2[i] = morta;
                        }
                        for (int coluna = 0; coluna <= tempo - 1; coluna++) {
                            int l = 0;
                            Leslie[l][coluna] = itens[coluna];
                        }
                        int l = 1;
                        int c = 0;
                        do {
                            Leslie[l][c] = itens2[c];
                            l++;
                            c++;
                        } while (c <= tempo - 1 && l <= tempo - 1);
                    }
                    System.out.println("Até que geração pretende estimar ? ");
                    geraçaoi = ler.nextInt();
                    while (geraçaoi < 0) {
                        System.out.println("Valor de geração incorreto, tente outra vez");
                        geraçaoi = ler.nextInt();
                    }
                    geraçao = geraçaoi + 1;
                    popTotalAno = new double[valorFinal];
                    matrizB = new double[Leslie.length][Leslie.length];
                    for (int i = 0; i < x.length; i++) {
                        popTotalAno[0] = popTotalAno[0] + x[i];
                    }
                    inicionorma = new double[x.length];
                    normalizaçao = new double[geraçao][x.length];
                    nNormal = new double[geraçao][x.length];
                    inicionorma = normalizacaoInicial(geraçao, x);
                    if (geraçao > 1) {
                        popTotalAno[1] = populaçaoGeraçao(nNormal, normalizaçao, 1, Leslie, x);
                        matrizquadrada(Leslie, matrizB);
                    }
                    if (geraçao > 2) {
                        popTotalAno[2] = populaçaoGeraçao(nNormal, normalizaçao, 2, matrizB, x);
                    }
                    for (int i = 3; i < geraçao; i++) {
                        matrizT(Leslie, matrizB, geraçao);
                        popTotalAno[i] = populaçaoGeraçao(nNormal, normalizaçao, i, matrizB, x);
                    }
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                }else if (pos==6){
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    escreverMatrizes2(Leslie);
                    System.out.println();
                    System.out.println("Clique 0 para voltar ao menu");
                    String valor = ler.next();
                    while (!valor.equals("0")){
                        System.out.println("Clique 0 para voltar ao menu");
                        valor = ler.next();
                    }
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                }
                pos = Menu();
            }
        }else if ((args[0].equals("-t") || args[0].equals("-g") || args[0].equals("-e") || args[0].equals("-v")  || args[0].equals("-r")) && args.length<10)  {
            int [] som= new int[7];
            for (int c=0; c<args.length; c++){
                if (args[c].equals("-t")){
                    som[0]+=1;
                }else if (args[c].equals("-g")){
                    som[1]+=1;
                }else if (args[c].equals("-r")){
                    som[2]+=1;
                }else if (args[c].equals("-e")){
                    som[3]+=1;
                }else if (args[c].equals("-v")){
                    som[4]+=1;
                }else if (args[c].matches("^[0-9]*$")) {
                    som[5]+=1;
                }else{
                    som[6]+=1;
                }
            }
            if (som[0]==1 && som[1]==1 && som[2]<2 && som[3]<2 && som[4]<2 && som[5]==2 && som[6]==2) {
                int geraçao = 0;
                for (int k = 0; k < args.length - 2; k++) {
                    if (args[k].equals("-t")) {
                        if (args[k + 1].matches("^[0-9]*$")) {
                            geraçao = Integer.parseInt(args[k + 1]) + 1;
                        } else {
                            System.out.println("Comando Incorreto");
                            System.exit(0);
                        }
                    }
                }
                if (geraçao < 1) {
                    System.out.println("Comando Incorreto");
                } else {
                    boolean resposta1 = false;
                    boolean resposta2 = false;
                    boolean resposta3 = false;
                    File Ficheiro = new File(args[args.length - 2]);
                    bool = Ficheiro.exists();
                    if (bool == false) {
                        System.out.println("Ficheiro nao existe");
                    } else {
                        Scanner ler_ficheiro = new Scanner(Ficheiro);
                        FileWriter myWriter = new FileWriter(args[args.length - 1]);
                        boolean b = false, c=false;
                        boolean [] bu = new boolean[1];
                        String[] linhas = new String[3];
                        b = lerFich(ler_ficheiro, linhas);
                        if (b) {
                            System.out.println("O seu ficheiro tem um problema");
                            System.exit(0);
                        }else {
                            int tamanho = linhas[0].split(", ").length;
                            x = A(tamanho, linhas, bu);
                            Leslie = new double[tamanho][tamanho];
                            c = Leslie(linhas, Leslie, tamanho);
                            ler_ficheiro.close();
                        }
                        if (c || bu[0]){
                            System.out.println("O seu ficheiro tem um problema");
                            System.exit(0);
                        }
                        namefile = Ficheiro.getName();
                        namefile = namefile.substring(0, namefile.lastIndexOf("."));
                        escreverMatrizes(Leslie, x, myWriter);
                        double[] popTotalAno = new double[valorFinal];
                        double[][] matrizB = new double[Leslie.length][Leslie.length];
                        for (int i = 0; i < x.length; i++) {
                            popTotalAno[0] = popTotalAno[0] + x[i];
                        }
                        double[] inicionorma = new double[x.length];
                        double[][] normalizaçao = new double[geraçao][x.length];
                        double[][] nNormal = new double[geraçao][x.length];
                        inicionorma = normalizacaoInicial(geraçao, x);
                        if (geraçao > 1) {
                            popTotalAno[1] = populaçaoGeraçao(nNormal, normalizaçao, 1, Leslie, x);
                            matrizquadrada(Leslie, matrizB);
                        }
                        if (geraçao > 2) {
                            popTotalAno[2] = populaçaoGeraçao(nNormal, normalizaçao, 2, matrizB, x);
                        }
                        for (int i = 3; i < geraçao; i++) {
                            matrizT(Leslie, matrizB, geraçao);
                            popTotalAno[i] = populaçaoGeraçao(nNormal, normalizaçao, i, matrizB, x);
                        }
                        double[][] valoresP = valoresProprios(Leslie);
                        double max = maiorValor(valoresP);
                        int col = colunaMaiorValor(valoresP);
                        double[] per = vetorProprio(Leslie, col);
                        String filename = "";
                        String type = "";
                        boolean bull1=false;
                        for (int i = 0; i < args.length - 2; i++) {
                            if (args[i].equals("-g")) {
                                String tipoDeFich = args[i + 1];
                                if (tipoDeFich.equals("1")) {
                                    filename = "png";
                                    type = "png";
                                } else if (tipoDeFich.equals("2")) {
                                    filename = "txt";
                                    type = "dumb";
                                } else if (tipoDeFich.equals("3")) {
                                    filename = "eps";
                                    type = "postscript";
                                }else{
                                    bull1=true;
                                }
                            }
                        }
                        if (!bull1) {
                            for (int i = 0; i < args.length - 2; i++) {
                                if (args[i].equals("-e")) {
                                    resposta1 = true;
                                } else if (args[i].equals("-v")) {
                                    gnuplot(geraçao, popTotalAno, filename, type, 1, nNormal, x.length, x, normalizaçao, inicionorma, false, namefile);
                                    resposta2 = true;
                                } else if (args[i].equals("-r")) {
                                    gnuplot(geraçao, popTotalAno, filename, type, 2, nNormal, x.length, x, normalizaçao, inicionorma, false, namefile);
                                    resposta3 = true;
                                }
                            }
                            gnuplot(geraçao, popTotalAno, filename, type, 3, nNormal, x.length, x, normalizaçao, inicionorma, false, namefile);
                            gnuplot(geraçao, popTotalAno, filename, type, 4, nNormal, x.length, x, normalizaçao, inicionorma, false, namefile);
                            escreverNormal(nNormal, myWriter, geraçao, inicionorma, normalizaçao, x);
                            if (resposta2 || resposta3) {
                                escreverVarFich(popTotalAno, geraçao, resposta2, resposta3, myWriter);
                            }
                            if (resposta1) {
                                assimtoticoFich(max, per, myWriter);
                            }
                            myWriter.close();
                            System.out.println("O seu programa foi executado com sucesso");
                        }else{
                            System.out.println("Comando Incorreto");
                        }
                    }
                }
            }else{
                System.out.println("Comando Incorreto");
            }
        }else{
            System.out.println("Comando Incorreto");
        }
    }

//==============================================================
//                          METODOS
//==============================================================

    //MÉTODO lerFich
    public static boolean lerFich (Scanner ler_ficheiro, String [] linhas ){
        String str;
        String str1;
        while (ler_ficheiro.hasNext()) {
            str = ler_ficheiro.next();
            str1 = str.substring(0,1);
            if(str1.equals("x")){
                linhas[0] = str + ler_ficheiro.nextLine();
            }else if (str1.equals("f")){
                linhas[1] = str + ler_ficheiro.nextLine();
            }else if (str1.equals("s")){
                linhas[2] = str + ler_ficheiro.nextLine();
            }else{
                return true;
            }
        }
        if (linhas[0]==null || linhas[1] == null || linhas[2]==null){
            return true;
        }
        return false;
    }

//==============================================================

    //MÉTODO A
    public static int[] A(int tamanho, String [] linhas, boolean[] bu) {
        int i = 0;
        char ch;
        int f=0;
        String str;
        int a = 0;
        int a2 = 0;
        int conta1 =0;
        String[] itens = linhas[0].split(", ");
        int[] array = new int[tamanho];
        int[] array2 = new int[tamanho];
        for (i=0; i< itens.length; i++){
            do {
                ch = itens[i].charAt(f);
                if(ch == 'x'){
                    a = f - 1;
                    if(f == 0)
                        a++;
                }
                f+=1;
            }while (ch != '=');
            a2 = f-1-a;
            array2[i] = Integer.parseInt(itens[i].substring(++a,a2));
            for(int conta = 0; conta < i; conta++){
                if( array2[conta] >= array2[conta+1] || array2[conta] >= array2[i] || array2[conta] != conta || array2[i]!=i ){
                    conta1++;
                }
            }
            if(conta1 >  0){
                bu[0]=true;
            }else{
                bu[0]=false;
            }
            str = itens[i].substring(f);
            array [i] = Integer.parseInt(str);
            f=0;
        }
        return array;
    }

//==============================================================

    //MÉTODO Leslie
    public static boolean Leslie(String [] linhas, double[][] Leslie, int tamanho) {
        String[] itens = linhas[1].split(", ");
        String[] itens2 =linhas[2].split(", ");
        if (itens.length<=itens2.length || itens.length!=tamanho || itens2.length<(itens.length-1)){
            return true;
        }
        char ch;
        int f=0;
        String str;
        int a = 0;
        int a2 = 0;
        int conta1 = 0;
        int[] array = new int[itens.length];
        int[] array2 = new int[itens2.length];
        for (int coluna = 0; coluna <= tamanho - 1; coluna++) { //s taxa de sobrevivencia
            int l=0;
            do {
                ch = itens[coluna].charAt(f);
                if(ch == 'f'){
                    a = f-1;
                    if(f == 0)
                        a++; }
                f+=1;
            }while (ch != '=');
            a2 = f-1-a;
            array[coluna] = Integer.parseInt(itens[coluna].substring(++a,a2));
            for(int i = 0; i < coluna; i++){
                if(array[i] >= array[i+1] || array[i] >= array[coluna] || array[i] != i || array[coluna]!=coluna )
                    conta1++; }
            if(conta1 >  0){
                return true;
            }
            str = itens[coluna].substring(f);
            Leslie[l][coluna]  = Double.parseDouble(str);
            f=0;
        }
        f=0;
        int l = 1;  //f taxa de mortalidade
        int c = 0;
        do {
            do {
                ch = itens2[c].charAt(f);
                if(ch == 's'){
                    a = f-1;
                    if(f==0)
                        a++;
                }
                f+=1;
            }while (ch != '=');
            a2 = f-1-a;
            array2[c] = Integer.parseInt(itens2[c].substring(++a,a2));
            for(int i = 0; i < c; i ++){
                if(array2[i] >= array2[i+1] || array2[i] >= array2[c] || array2[i] != i || array2[c]!=c)
                    conta1++;
            }
            if(conta1 > 0){
                return true;
            }
            str = itens2[c].substring(f);
            Leslie[l][c] = Double.parseDouble(str);
            f=0;
            l++;
            c++;
        } while (c <= tamanho - 1 && l <= tamanho - 1);
        return false;
    }

//==============================================================

    //MÉTODO matrizquadrada
    public static void matrizquadrada(double[][] matriz, double[][] matrizB) {
        double somatorio = 0;
        for (int x = 0; x < matriz.length; x++) {
            for (int z = 0; z < matriz.length; z++) {
                for (int i = 0; i < matriz.length; i++) {
                    somatorio = somatorio + matriz[x][i] * matriz[i][z];
                }
                matrizB[x][z] = somatorio;
                somatorio = 0;
            }
            somatorio = 0;
        }
    }

//==============================================================

    //MÉTODO matrizT
    public static void matrizT(double[][] matriz, double[][] matrizB, int geraçaoP) {
        double somatorio = 0;
        double[][] matrizC = new double[matrizB.length][matrizB.length];
        for (int x = 0; x < matriz.length; x++) {
            for (int z = 0; z < matriz.length; z++) {
                for (int i = 0; i < matriz.length; i++) {
                    somatorio = somatorio + matrizB[x][i] * matriz[i][z];
                }
                matrizC[x][z] = somatorio;
                somatorio = 0;
            }
            somatorio = 0;
        }
        for (int l = 0; l < matrizB.length; l++) {
            for (int c = 0; c < matrizB.length; c++) {
                matrizB[l][c] = matrizC[l][c];
            }
        }
    }

//==============================================================

    //MÉTODO normalizacaoInicial
    public static double[] normalizacaoInicial(int geracao, int[] x) {
        double[] norma = new double[x.length];
        double conta = 0;
        for (int i = 0; i < x.length; i++) {
            conta = conta + x[i];
        }
        for (int i = 0; i < x.length; i++)
            norma[i] =x[i] / conta;
        return norma;
    }

//==============================================================

    //MÉTODO populaçaoGeraçao
    public static double populaçaoGeraçao(double[][] nNormal, double [][] normalizacao, int nrgera, double[][] matriz, int[] geraçao) {
        double cont = 0;
        double somatorio = 0;
        double[] popPorClasse = new double[matriz.length];
        for (int i = 0; i < matriz.length; i++) {
            for (int x = 0; x < matriz.length; x++) {
                somatorio = somatorio + matriz[i][x] * geraçao[x];
            }
            popPorClasse[i] = somatorio;
            somatorio = 0;
        }
        for (int i = 0; i < geraçao.length; i++) {
            cont = cont + popPorClasse[i];
        }
        for(int i=0; i<geraçao.length; i++){
            nNormal[nrgera][i]=popPorClasse[i];
        }
        for (int i = 0; i < geraçao.length; i++){
            normalizacao[nrgera][i] = popPorClasse[i] / cont;
        }
        return cont;
    }

//==============================================================

    //MÉTODO escreverMatrizes
    public static void escreverMatrizes (double[][] Leslie, int [] x, FileWriter myWriter) throws IOException {
        myWriter.write("Matriz Leslie: ");
        myWriter.write("\n");
        for(int i=0; i< Leslie.length; i++){
            for(int f=0; f< Leslie.length-1; f++){
                myWriter.write(String.valueOf(Leslie[i][f]));
                myWriter.write(" , ");
            }
            myWriter.write(String.valueOf(Leslie[i][Leslie.length-1]));
            myWriter.write("\n");
        }
        myWriter.write("\n");
    }

//==============================================================

    //MÉTODO escreverMatrizes2
    public static void escreverMatrizes2 (double [][] Leslie){
        System.out.println("Matriz de Leslie: ");
        for(int i=0; i< Leslie.length; i++){
            for(int l=0; l< Leslie.length-1; l++){
                System.out.print(Leslie[i][l]);
                System.out.print(" , ");
            }
            System.out.println(Leslie[i][Leslie.length-1]);
            System.out.println();
        }
        System.out.println();
    }

//==============================================================

    //MÉTODO escreverVariaçaoMenu
    public static void escreverVariaçaoMenu(double[] popTotalAno, int geraçao, int pos)  {
        double varDaPop;
        if (pos == 1) {
            System.out.println("Dimensao da Populaçao: ");
            for (int i = 0; i < geraçao; i++) {
                System.out.print("Geraçao " + (i) + " ");
                System.out.print("- ");
                System.out.printf("%.2f", popTotalAno[i]);
                System.out.printf(" elementos");
                System.out.println();
            }
            System.out.println();
        }
        if (pos == 2) {
            System.out.println("Variaçao da populaçao");
            for (int i = 0; i < geraçao-1; i++) {
                varDaPop = popTotalAno[i + 1] / popTotalAno[i];
                System.out.print("Geraçao " + (i) + " para " + (i + 1) + " = ");
                System.out.printf("%.2f", varDaPop);
                System.out.print("%");
                System.out.println();
            }
            System.out.println();
        }
    }

//==============================================================

    //MÉTODO escreverVarFich
    public static void escreverVarFich (double[] popTotalAno, int geraçao, boolean r1, boolean r2, FileWriter myWriter) throws IOException {
        double varDaPop;
        if(r2) {
            myWriter.write("Variaçao da Populaçao: \n");
            for (int i = 0; i < geraçao-1; i++) {
                varDaPop = popTotalAno[i + 1] / popTotalAno[i];
                myWriter.write("Geração ");
                String num1 = String.format(String.valueOf(i));
                myWriter.write(num1);
                myWriter.write(" para ");
                String num2 = String.format(String.valueOf(i + 1));
                myWriter.write(num2);
                myWriter.write(" = ");
                String num = String.format(String.valueOf(varDaPop));
                BigDecimal bd = new BigDecimal(num).setScale(2, RoundingMode.HALF_UP);
                String num3 = String.format(String.valueOf(bd));
                myWriter.write(num3);
                myWriter.write("%\n");
            }
            myWriter.write("\n");
        }
        if (r1) {
            myWriter.write("Dimensao da Populaçao: \n");
            for (int i = 0; i < geraçao; i++) {
                myWriter.write("Geração ");
                String num1 = String.format(String.valueOf(i));
                myWriter.write(num1);
                myWriter.write(" - ");
                String num = String.format(String.valueOf(popTotalAno[i]));
                BigDecimal bd = new BigDecimal(num).setScale(2, RoundingMode.HALF_UP);
                String num3 = String.format(String.valueOf(bd));
                myWriter.write(num3);
                myWriter.write(" elementos\n");
            }
            myWriter.write("\n");
        }
    }

//==============================================================

    //MÉTODO escreverNormal
    public static void escreverNormal (double [][] nNormal,FileWriter myWriter, int geraçao, double[] inicionorma, double[][] normalizaçao, int []x) throws IOException {
        myWriter.write("Distribuiçao nao normalizada:\n");
        myWriter.write("Geração inicial:\n");
        for(int i = 0; i < x.length; i++){
            myWriter.write("Classe ");
            String num1 = String.format(String.valueOf(i+1));
            myWriter.write(num1);
            myWriter.write(" = ");
            String num = String.format((String.valueOf(x[i])));
            BigDecimal bd = new BigDecimal(num).setScale(2, RoundingMode.HALF_UP);
            String num3 = String.format(String.valueOf(bd));
            myWriter.write(num3);
            myWriter.write("\n");
        }
        myWriter.write("\n");
        for(int i = 1; i < geraçao; i++) {
            myWriter.write("Geração ");
            String num1 = String.format(String.valueOf(i));
            myWriter.write(num1);
            myWriter.write(":\n");
            for (int f = 0; f < x.length; f++) {
                myWriter.write("Classe ");
                String num2 = String.format(String.valueOf(f + 1));
                myWriter.write(num2);
                myWriter.write(" = ");
                String num = String.format(String.valueOf(nNormal[i][f]));
                BigDecimal bd = new BigDecimal(num).setScale(2, RoundingMode.HALF_UP);
                String num3 = String.format(String.valueOf(bd));
                myWriter.write(num3);
                myWriter.write("\n");
            }
            myWriter.write("\n");
        }
        myWriter.write("Distribuiçao Normalizada:\n");
        myWriter.write("Geração inicial:\n");
        for(int i = 0; i < x.length; i++){
            myWriter.write("Classe ");
            String num1 = String.format(String.valueOf(i+1));
            myWriter.write(num1);
            myWriter.write(" = ");
            String num = String.format((String.valueOf(inicionorma[i]*100)));
            BigDecimal bd = new BigDecimal(num).setScale(2, RoundingMode.HALF_UP);
            String num3 = String.format(String.valueOf(bd));
            myWriter.write(num3);
            myWriter.write("%");
            myWriter.write("\n");
        }
        myWriter.write("\n");
        for(int i = 1; i < geraçao; i++) {
            myWriter.write("Geração ");
            String num1 = String.format(String.valueOf(i));
            myWriter.write(num1);
            myWriter.write(":\n");
            for (int f = 0; f < x.length; f++) {
                myWriter.write("Classe ");
                String num2 = String.format(String.valueOf(f + 1));
                myWriter.write(num2);
                myWriter.write(" = ");
                String num = String.format(String.valueOf(normalizaçao[i][f]*100));
                BigDecimal bd = new BigDecimal(num).setScale(2, RoundingMode.HALF_UP);
                String num3 = String.format(String.valueOf(bd));
                myWriter.write(num3);
                myWriter.write("%");
                myWriter.write("\n");
            }
            myWriter.write("\n");
        }
    }

//==============================================================

    //MÉTODO valoresProprios
    public static double[][] valoresProprios(double[][] a) { //a= array da matriz de Leslie
        Matrix b = new Basic2DMatrix(a);
        EigenDecompositor eigenD = new EigenDecompositor(b);
        Matrix[] mattD = eigenD.decompose();
        double matB[][] = mattD[1].toDenseMatrix().toArray(); //transformar a matriz em array
        return matB;
    }

//==============================================================

    //MÉTODO colunaMaiorValor
    public static int colunaMaiorValor(double[][] a) { //a= array da matriz dos valores proprios
        double max = 0;
        int x = 0;
        for (int l=0 ; l<a.length ; l++) {
            for (int c=0 ; c<a.length ; c++) {
                if (Math.abs(a[l][c]) > max) {
                    max = Math.abs(a[l][c]);
                    x=c;
                }
            }
        }
        return x;
    }

//==============================================================

    //MÉTODO maiorValor
    public static double maiorValor(double[][] a) { //a= array da matriz dos valores proprios
        double nAbs =0;
        double max = 0;
        for (int l = 0; l < a.length; l++) {
            for (int c = 0; c < a.length; c++) {
                if (Math.abs(a[l][c]) > max) {
                    nAbs = a[l][c];
                    max = Math.abs(a[l][c]);
                }
            }
        }
        return nAbs;
    }

//==============================================================

    //MÉTODO assimtotico
    public static void assimtotico(double a, double[]per) { //a= max
        System.out.println("Comportamento Assimtotico: ");
        System.out.print("O valor próprio é ");
        System.out.printf("%.4f",a);
        System.out.println();
        System.out.print("Vetor próprio associado: ");
        System.out.print("(");
        for(int l=0; l< per.length-1; l++) {
            System.out.printf("%.2f",per[l]);
            System.out.print(" , ");
        }
        System.out.printf("%.2f",per[per.length-1]);
        System.out.print(")");
    }

//==============================================================

    //MÉTODO assimtoticoFich
    public static void assimtoticoFich (double a, double[]per, FileWriter myWriter) throws IOException {
        myWriter.write("Comportamento Assimtotico: \n");
        myWriter.write("O valor próprio é ");
        String num = String.format(String.valueOf(a));
        BigDecimal bd = new BigDecimal(num).setScale(4, RoundingMode.HALF_UP);
        String num3 = String.format(String.valueOf(bd));
        myWriter.write(num3);
        myWriter.write("\n");
        myWriter.write("Vetor próprio associado: ");
        myWriter.write("(");
        for (int l = 0; l < per.length-1; l++) {
            String num2 = String.format(String.valueOf(per[l]));
            BigDecimal bd1 = new BigDecimal(num2).setScale(2, RoundingMode.HALF_UP);
            String num4 = String.format(String.valueOf(bd1));
            myWriter.write(num4);
            myWriter.write(" , ");
        }
        String num2 = String.format(String.valueOf(per[per.length-1]));
        BigDecimal bd1 = new BigDecimal(num2).setScale(2, RoundingMode.HALF_UP);
        String num4 = String.format(String.valueOf(bd1));
        myWriter.write(num4);
        myWriter.write(")");
        myWriter.write("\n");
    }

//==============================================================

    //MÉTODO vetorProprio
    public static double [] vetorProprio(double[][] a, int b) { //a= Leslie ; b= col
        double soma = 0;
        int l;
        Matrix vp = new Basic2DMatrix(a);
        EigenDecompositor eigenD=new EigenDecompositor(vp);
        Matrix [] mattD= eigenD.decompose();
        double matA [][]= mattD[0].toDenseMatrix().toArray(); //transformar a matriz em array
        double per[] =new double [matA.length];
        for (l=0 ; l<matA.length ; l++) {
            soma = soma + matA[l][b];
        }
        for (l=0 ; l<matA.length ; l++) {
            per [l] = (matA[l][b] / soma) * 100 ;
        }
        return per;
    }

//==============================================================

    //MÉTODO Menu
    public static int Menu(){
        int pos;
        Scanner ler = new Scanner(System.in);
        System.out.println("\n\n\nBem-vindo ao programa! Pressione 0 para sair.");
        System.out.println("1.Pretende calcular a dimensao da populaçao em todas as geraçoes estimadas ?\n2.Pretende calcular a variaçao da população ao longo dos anos ?\n3.Pretende calcular a distribuição da população não normalizada ?\n4.Pretende calcular a distribuição normalizada da população ?\n5.Pretende analisar o comportamento assimptótico ?\n6.Deseja visualizar a sua matriz ?\n7.Deseja inserir uma nova matriz ?");
        pos = ler.nextInt();
        return pos;
    }

//==============================================================

    //MÉTODO gnuplot
    public static String gnuplot(int geraçao, double[] popTotalAno, String filename, String type, int pos, double[][] nNormal, int tamanho, int[] x, double[][] normalizaçao, double[] inicionorma, boolean Ask, String namefile) throws IOException {
        DateTimeFormatter yha= DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        String date = yha.format(now);
        String lp ="";
        String name = "";
        String size = "";
        int fix = 0;
        for(int i=1;i<=4;i++){
            File gnuplot = new File("gnuplot"+i+".gp");
            if (Ask){
                gnuplot.deleteOnExit();
            }
        }
        if (filename.equals("png")){
            size="840, 600";
            fix= -4;
        }else if (filename.equals("txt")){
            size="130, 40";
            fix= -0;
        }else if (filename.equals("eps")){
            size="11, 7";
            fix= -4;
        }
        if (geraçao<5){
            lp="linespoints";
        }else{
            lp="line";
        }
        if (pos == 1) {
            String p="";
            File create = new File(namefile+"_DimPopulação_"+date+""+"."+filename);
            create.createNewFile();
            for(int i=0;i <= geraçao-1;i++){
                p=p+i+" "+popTotalAno[i]+"\n";
            }
            FileWriter fwgpT = new FileWriter("gnuplot1.gp");
            PrintWriter pwgpT = new PrintWriter(fwgpT);
            pwgpT.println("cd '"+System.getProperty("user.dir")+"'");
            pwgpT.println("set terminal "+type+" size "+size);
            if (filename.equals("eps")){
                pwgpT.println("set color");
            }
            pwgpT.println("set output \""+namefile+"_DimPopulação_"+date+""+"."+filename+"\"");
            pwgpT.println("set xlabel \"Geração\"");
            pwgpT.println("set ylabel \"População\"");
            pwgpT.println("set key top left box");
            pwgpT.println("set key font \",12\"");
            pwgpT.println("set key width 0");
            pwgpT.println("set key height 1");
            pwgpT.println("set border 3");
            pwgpT.println("set grid");
            pwgpT.println("plot \"-\" title \"População\" with "+lp+" lc 2");
            pwgpT.println(p);
            pwgpT.println("e");
            pwgpT.println("set output");
            pwgpT.close();
            Runtime rt = Runtime.getRuntime();
            rt.exec("gnuplot gnuplot1.gp");
            name = namefile+"_DimPopulação_"+date+""+".";
        }else if (pos == 2) {
            if (geraçao>1) {
                String v = "";
                File create = new File(namefile + "_TaxaVariação_" + date + "" + "." + filename);
                create.createNewFile();
                for (int i = 0; i <= geraçao - 2; i++) {
                    v = v + i + " " + popTotalAno[i + 1] / popTotalAno[i] + "\n";
                }
                FileWriter fwgpV = new FileWriter("gnuplot2.gp");
                PrintWriter pwgpV = new PrintWriter(fwgpV);
                pwgpV.println("cd '" + System.getProperty("user.dir") + "'");
                pwgpV.println("set terminal " + type + " size " + size);
                if (filename.equals("eps")) {
                    pwgpV.println("set color");
                }
                pwgpV.println("set output \"" + namefile + "_TaxaVariação_" + date + "" + "." + filename + "\"");
                pwgpV.println("set xlabel \"Geração\"");
                pwgpV.println("set ylabel \"Taxa de variação\"");
                pwgpV.println("set key top right box");
                pwgpV.println("set key width 0");
                pwgpV.println("set key font \",12\"");
                pwgpV.println("set key height 1");
                pwgpV.println("set border 3");
                pwgpV.println("set grid");
                pwgpV.println("plot \"-\" title \"Variação da população\" with " + lp + " lc 7");
                pwgpV.println(v);
                pwgpV.println("e");
                pwgpV.println("set output");
                pwgpV.close();
                Runtime rt3 = Runtime.getRuntime();
                rt3.exec("gnuplot gnuplot2.gp");
                name = namefile + "_TaxaVariação_" + date + "" + ".";
            }
        }else if (pos == 3) {
            File create = new File(namefile+"_DistribuiçãoPopulação_"+date+""+"."+filename);
            create.createNewFile();
            String nc="";
            int n=0;
            for(int i = 1; i <= tamanho; i++){
                nc=nc +"0 "+x[n]+"\n";
                for(int j=2;j<= geraçao;j++){
                    nc=nc + (j-1) + " " + nNormal[j-1][n]+ "\n";
                }
                n++;
                nc=nc+"e\n";
            }
            String plot= "plot";
            FileWriter fwgpNN = new FileWriter("gnuplot3.gp");
            PrintWriter pwgpNN = new PrintWriter(fwgpNN);
            pwgpNN.println("cd '"+System.getProperty("user.dir")+"'");
            pwgpNN.println("set terminal "+type+" size "+size);
            if (filename.equals("eps")){
                pwgpNN.println("set color");
            }
            pwgpNN.println("set output \""+namefile+"_DistribuiçãoPopulação_"+date+""+"."+filename+"\"");
            pwgpNN.println("set xlabel \"Geração\"");
            pwgpNN.println("set ylabel \"População\"");
            pwgpNN.println("set key top center box");
            pwgpNN.println("set key width 0");
            pwgpNN.println("set key spacing 1");
            pwgpNN.println("set key font \",12\"");
            pwgpNN.println("set key height 1");
            pwgpNN.println("set border 3");
            pwgpNN.println("set grid");
            for(int i = 1; i <= tamanho; i++) {
                plot = plot + "\"-\" title \"População da classe" + i + "\" with "+lp+" lc " + i;
                if (i<=tamanho-1) {
                    plot = plot + ",";
                }
            }
            if (tamanho>25){
                pwgpNN.println("unset key");
            }
            pwgpNN.println(plot);
            pwgpNN.println(nc);
            pwgpNN.println("set output");
            pwgpNN.close();
            Runtime rt2 = Runtime.getRuntime();
            rt2.exec("gnuplot gnuplot3.gp");
            name = namefile+"_DistribuiçãoPopulação_"+date+""+".";
        }else if (pos == 4){
            File create = new File(namefile+"_DistribuiçãoNormalizada_"+date+""+"."+filename);
            create.createNewFile();
            String c="";
            int n=0;
            for(int i = 1; i <= tamanho; i++){
                c= c + "0 " + (inicionorma[n]*100) +"\n";
                for(int j=2;j<= geraçao;j++){
                    c=c + (j-1) + " " + normalizaçao[j-1][n]*100 +"\n";
                }
                n++;
                c=c+"e\n";
            }
            String plot= "plot";
            FileWriter fwgpN = new FileWriter("gnuplot4.gp");
            PrintWriter pwgpN = new PrintWriter(fwgpN);
            pwgpN.println("cd '"+System.getProperty("user.dir")+"'");
            pwgpN.println("set terminal "+type+" size "+size);
            if (filename.equals("eps")){
                pwgpN.println("set color");
            }
            pwgpN.println("set output \""+namefile+"_DistribuiçãoNormalizada_"+date+""+"."+filename+"\"");
            pwgpN.println("set xlabel \"Geração\"");
            pwgpN.println("set ylabel \"Taxa de população\"");
            pwgpN.println("set key top right");
            pwgpN.println("set key width "+fix);
            pwgpN.println("set key spacing 2");
            pwgpN.println("set key outside");
            pwgpN.println("set key font \",12\"");
            pwgpN.println("set key height 1");
            pwgpN.println("set border 3");
            pwgpN.println("set grid");
            for(int i = 1; i <= tamanho; i++){
                plot= plot + "\"-\" title \"Normalização da classe"+i+"\" with "+lp+" lc "+ i;
                if (i<=tamanho-1) {
                    plot = plot + ",";
                }
            }
            if (tamanho>25){
                pwgpN.println("unset key");
            }
            pwgpN.println(plot);
            pwgpN.println(c);
            pwgpN.println("set output");
            pwgpN.close();
            Runtime rt2 = Runtime.getRuntime();
            rt2.exec("gnuplot gnuplot4.gp");
            name = namefile+"_DistribuiçãoNormalizada_"+date+""+".";
        }
        return name;
    }

//==============================================================

    //MÉTODO saveG
    public static void saveG(String name,String filename,String type,int geraçao,double[] popTotalAno,int pos,double[][] nNormal,int tamanho,int[] x,double[][] normalizaçao,double[] inicionorma,boolean Ask,String namefile) throws IOException {
        if (pos == 2){
            if(geraçao == 1){
                System.out.println("Ficheiro impossível de ser criado!");
                return;
            }
        }
        Scanner ler = new Scanner(System.in);
        File grafico = new File(name + filename);
        Desktop d = Desktop.getDesktop();
        d.open(new File(name + filename));
        System.out.println("Deseja guardar o gráfico? (sim/nao)");
        String resposta = ler.next();
        if (!resposta.equals("sim") && !resposta.equals("nao")) {
            System.out.println("Resposta incorreta, introduza sim ou nao");
            resposta = ler.next();
        }
        if(resposta.equals("nao")) {
            grafico.delete();
        }else if (resposta.equals("sim")) {
            System.out.println("Em que tipo de Ficheiro quer salvar o gráfico? (png/ eps/ txt)");
            filename = ler.next();
            while (!filename.equals("png") && !filename.equals("eps") && !filename.equals("txt")) {
                System.out.println("Resposta incorreta, introduza png, eps ou txt");
                filename = ler.next();
            }
            switch (filename) {
                case "png":
                    break;
                case "txt":
                    type = "dumb";
                    grafico.delete();
                    gnuplot(geraçao, popTotalAno, filename, type, pos, nNormal, x.length, x, normalizaçao, inicionorma, true, namefile);
                    break;
                case "eps":
                    type = "postscript";
                    grafico.delete();
                    gnuplot(geraçao, popTotalAno, filename, type, pos, nNormal, x.length, x, normalizaçao, inicionorma, true, namefile);
                    break;
            }
        }
    }
}