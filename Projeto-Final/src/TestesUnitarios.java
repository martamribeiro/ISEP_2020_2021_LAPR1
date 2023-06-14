import org.la4j.Matrix;
import org.la4j.decomposition.EigenDecompositor;
import org.la4j.matrix.dense.Basic2DMatrix;

public class TestesUnitarios {

    private static ProgramaFinal testesUni = new ProgramaFinal();


    public static void main(String [] args){

        escreverTeste();

    }

    public static void escreverTeste(){

        boolean confirma = true;

        if( confirma != testeMaiorValorProprio ()) System.out.println("testeMarioValorProprio -NOT OK");
        else System.out.println("testeMarioValorProprio -OK");

        if(confirma != testeVetorProprio()) System.out.println("testeVetorProprio -NOT OK");
        else System.out.println("testeVetorProprio -OK");

        if(confirma != testeVetorGeraçao()) System.out.println("testeVetorGeraçao -NOT OK");
        else System.out.println("testeVetorGeraçao -OK");

        if(confirma != testeMatrizLeslie()) System.out.println("testeMatrizLeslie -NOT OK");
        else System.out.println("testeMatrizLeslie -OK");

        if(confirma != testeMatrizQuadrada()) System.out.println("testeMatrizQuadrada -NOT OK");
        else System.out.println("testeMatrizQuadrada -OK");

        if(confirma != testeMatrizT()) System.out.println("testeMatrizT -NOT OK");
        else System.out.println("testeMatrizT -OK");

        if(confirma != testeNormalizaçao())System.out.println("testeNormalizaçao -NOT OK");
        else System.out.println("testeNormalizaçao -OK");

        if (confirma != testePopulaçaoGeraçao()) System.out.println("testePopulaçaoGeraçao -NOT OK");
        else System.out.println("testePopulaçaoGeraçao -OK");
    }





    //  ----------------------------------------------------------------//
    //------------------------------TESTES UNI-------------------------//
    //----------------------------------------------------------------//

    public static boolean testeMaiorValorProprio (){
        double[][] a = {{1,0},{0,1}};
        double maiorValorProprio;
        double ExpectedMaiorValorProprio = 1;

        double [][] matriz = testesUni.valoresProprios(a);
        maiorValorProprio = testesUni.maiorValor(matriz);

        double[][] a1 = {{0,1,3},{0.5,0,0},{0,0.5,0}};
        int maiorValorProprio1;
        double ExpectedMaiorValorProprio1 = 1089;

        double [][] matriz1 = testesUni.valoresProprios(a1);
        maiorValorProprio1 = (int) (testesUni.maiorValor(matriz1)*1000);



        double[][] a2 = {{0,3,3.17,0.39},{0.11,0,0,0},{0,0.29,0,0},{0,0,0.33,0}};
        int maiorValorProprio2;
        double ExpectedMaiorValorProprio2 = 6956;

        double [][] matriz2 = testesUni.valoresProprios(a2);
        maiorValorProprio2 = (int) (testesUni.maiorValor(matriz2)*10000);




        if (maiorValorProprio == ExpectedMaiorValorProprio && maiorValorProprio1 == ExpectedMaiorValorProprio1 && maiorValorProprio2 == ExpectedMaiorValorProprio2){
            return true;
        }else{
            return false;
        }


    }

    public static boolean testeVetorProprio(){
        double [][] a = {{1,0},{0,1}};
        double [] expectecVetorProprio =  {100,0};
        double [] per = testesUni.vetorProprio(a,0);

        int cont=0;

        for (int l=0; l<expectecVetorProprio.length; l++){
            if (expectecVetorProprio[l]==per[l]){
                cont +=1;
            }
        }


        double [][] a1 = {{0,1,3},{0.5,0,0},{0,0.5,0}};
        double [] expectecVetorProprio1 =  {5991, 2748, 1260};
        double [] per1 =  (testesUni.vetorProprio(a1,0));

        for (int i =0; i<per1.length; i++){
            per1[i]= (int) (per1[i]*100);
        }


        int cont1=0;

        for (int l=0; l<expectecVetorProprio1.length; l++){
            if (expectecVetorProprio1[l]==per1[l]){
                cont1 +=1;
            }
        }


        double [][] a2 = {{0,3,3.17,0.39},{0.11,0,0,0},{0,0.29,0,0},{0,0,0.33,0}};
        double [] expectecVetorProprio2 =  {7965, 1259, 525 ,249};
        double [] per2 =  (testesUni.vetorProprio(a2,0));

        for (int i =0; i<per2.length; i++){
            per2[i]= (int) (per2[i]*100);
        }


        int cont2=0;

        for (int l=0; l<expectecVetorProprio2.length; l++){
            if (expectecVetorProprio2[l]==per2[l]){
                cont2 +=1;
            }
        }


        if (cont==2 && cont1==3 && cont2==4){
            return true;
        }
        return false;
    }


    public static boolean testeVetorGeraçao(){
        boolean [] bu = new boolean[1];

        String [] linhas = {"x00=500, x01=300, x02=200","f0=0, f1=1, f2=3","s0=0.5, s1=0.5"};
        int [] Expectedgeraçao= {500,300,200};
        int tamanho = 3;

        int [] geraçao = testesUni.A(tamanho,linhas,bu);

        int cont=0;

        for (int l=0; l<Expectedgeraçao.length; l++){
            if(Expectedgeraçao[l] == geraçao[l]) {
                cont += 1;
            }

        }

        String [] linhas1 = {"x00=700, x01=9","f0=0, f1=1.11","s0=0.1"};
        int [] Expectedgeraçao1= {700,9};
        int tamanho1 = 2;

        int [] geraçao1 = testesUni.A(tamanho1,linhas1,bu);

        int cont1=0;

        for (int l=0; l<Expectedgeraçao1.length; l++){
            if(Expectedgeraçao1[l] == geraçao1[l]) {
                cont1 += 1;
            }

        }

        String [] linhas2 = {"x00=7, x01=14, x02=21, x03=28","f0=0, f1=1.11, f2=0.4, f3=","s0=0.1, s1=0.5, s2=0.3"};
        int [] Expectedgeraçao2= {7,14,21,28};
        int tamanho2 = 4;

        int [] geraçao2 = testesUni.A(tamanho2,linhas2,bu);

        int cont2=0;

        for (int l=0; l<Expectedgeraçao2.length; l++){
            if(Expectedgeraçao2[l] == geraçao2[l]) {
                cont2 += 1;
            }

        }



        if (cont == Expectedgeraçao.length && cont1 == Expectedgeraçao1.length && cont2 == Expectedgeraçao2.length){
            return true;
        }else {
            return false;
        }
    }

    public static boolean testeMatrizLeslie() {
        String[] linhas = {"x00=500, x01=300, x02=200", "f0=0, f1=1, f2=3", "s0=0.5, s1=0.5"};
        double[][] ExpectedLeslie = {{0, 1, 3}, {0.5, 0, 0}, {0, 0.5, 0}};
        int tamanho = 3;
        double[][] Leslie = new double[3][3];

        testesUni.Leslie(linhas, Leslie, tamanho);

        int cont =0;

        for (int l=0; l<ExpectedLeslie.length; l++){
            for(int c=0; c<ExpectedLeslie.length; c++){
                if(ExpectedLeslie[l][c] == Leslie[l][c]) {
                    cont += 1;
                }
            }
        }


        String[] linhas1 = {"x00=40, x01=20", "f0=0.5, f1=1.9", "s0=0.11"};
        double[][] ExpectedLeslie1 = {{0.5,1.9}, {0.11, 0}};
        int tamanho1 = 2;
        double[][] Leslie1 = new double[2][2];

        testesUni.Leslie(linhas1, Leslie1, tamanho1);

        int cont1 =0;

        for (int l=0; l<ExpectedLeslie1.length; l++){
            for(int c=0; c<ExpectedLeslie1.length; c++){
                if(ExpectedLeslie1[l][c] == Leslie1[l][c]) {
                    cont1 += 1;
                }
            }
        }

        String[] linhas2 = {"x00=40, x01=20, x02=50, x03=90", "f0=0.5, f1=1.9, f2=3, f3=0.1", "s0=0.11, s1=0.4, s2=0.8"};
        double[][] ExpectedLeslie2 = {{0.5,1.9,3,0.1}, {0.11,0,0,0},{0,0.4,0,0},{0,0,0.8,0}};
        int tamanho2 = 4;
        double[][] Leslie2 = new double[4][4];

        testesUni.Leslie(linhas2, Leslie2, tamanho2);

        int cont2 =0;

        for (int l=0; l<ExpectedLeslie2.length; l++){
            for(int c=0; c<ExpectedLeslie2.length; c++){
                if(ExpectedLeslie2[l][c] == Leslie2[l][c]) {
                    cont2 += 1;
                }
            }
        }

        if (cont == Math.pow(ExpectedLeslie.length,2) && cont1 == Math.pow(Leslie1.length,2) && cont2 == Math.pow(Leslie2.length,2)) {
            return true;
        } else{
            return false;
        }
    }

    public static boolean testeMatrizT (){

        int geraçaoP=0;
        double [][] matriz = {{0, 1, 3}, {0.5, 0, 0}, {0, 0.5, 0}};
        double [][] matrizB = {{0.5,1.5,0},{0,0.5,1.5},{0.25,0,0}};
        double [][] matrizC = {{0.75,0.5,1.5},{0.25,0.75,0},{0,0.25,0.75}};

        testesUni.matrizT(matriz,matrizB,geraçaoP);

        int cont=0;

        for (int l=0; l<matrizB.length; l++){
            for(int c=0; c<matrizB.length; c++){
                if(matrizB[l][c] == matrizC[l][c]) {
                    cont += 1;
                }
            }
        }

        double [][] matriz1 = {{1,2},{3,4}};
        double [][] matrizD = {{7,10},{15,22}};
        double [][] matrizE = {{37,54},{81,118}};

        testesUni.matrizT(matriz1,matrizD,geraçaoP);

        int cont1=0;

        for (int l=0; l<matrizD.length; l++){
            for(int c=0; c<matrizD.length; c++){
                if(matrizD[l][c] == matrizE[l][c]) {
                    cont1 += 1;
                }
            }
        }

        double [][] matriz2 = {{1,2,3,4},{1,2,3,4},{4,3,2,1},{4,3,2,1}};
        double [][] matrizF = {{31,27,23,19},{31,27,23,19},{19,23,27,31},{19,23,27,31}};
        double [][] matrizG = {{226,242,258,274},{226,242,258,274},{274,258,242,226},{274,258,242,226}};

        testesUni.matrizT(matriz2,matrizF,geraçaoP);

        int cont2=0;

        for (int l=0; l<matrizF.length; l++){
            for(int c=0; c<matrizF.length; c++){
                if(matrizF[l][c] == matrizG[l][c]) {
                    cont2 += 1;
                }
            }
        }




        if (cont==Math.pow(matrizB.length,2) && cont1==Math.pow(matrizD.length,2) && cont2==Math.pow(matrizF.length,2)){
            return true;
        }else {
            return false;
        }
    }

    public static boolean testePopulaçaoGeraçao(){

        int nrgera;
        double [][] matriz = {{2,2},{2,2}};
        double [][] matriz1 = {{0,3.5,1.5,0.39},{0.4,0,0,0},{0,0.6,0,0},{0,0,0.5,0}};
        int[] geraçao = new int[2];
        double [] outputExpected = new double[100];
        outputExpected[0] = 200;outputExpected[1] = 100;
        geraçao[0] = 20;
        geraçao[1] = 30;

        int [] geraçao1 = new int[4];
        geraçao1[0] = 5; geraçao1[1] = 20; geraçao1[2] = 15; geraçao1[3] = 20;

        double cont = 0;
        double cont1 = 0;
        double somatorio = 0;
        double [] popPorClasse = new double[matriz.length];

        for(int i = 0; i < matriz.length; i++){
            for (int x = 0; x < matriz.length; x++)
                somatorio = somatorio + matriz[i][x] * geraçao[x];

            popPorClasse[i] = somatorio;
            somatorio = 0;
        }

        for (int i = 0; i < geraçao.length; i++)
            cont = cont + popPorClasse[i];

        for(int i = 0; i < matriz.length; i++){
            for (int x = 0; x < matriz.length; x++)
                somatorio = somatorio + matriz[i][x] * geraçao1[x];

            popPorClasse[i] = somatorio;
            somatorio = 0;
        }

        for (int i = 0; i < geraçao.length; i++)
            cont1 = cont1 + popPorClasse[i];

        if(cont == outputExpected[0] && cont1 == outputExpected[1])
            return true;
        else
            return false;
    }

    public static boolean testeNormalizaçao(){
        double [] outputExpected = new double[7];

        outputExpected[0] = 0.14285714285714285; outputExpected[1] = 0.2857142857142857; outputExpected[2] = 0.5714285714285714;
        outputExpected[3] = 0.09009009009009009; outputExpected[4] = 0.36036036036036034; outputExpected[5] = 0.5405405405405406; outputExpected[6] = 0.009009009009009009;

        int [] x = {5,10,20};
        int [] x1 = {500,2000,3000,50};

        double[] norma = new double[x.length];
        double [] norma1 = new double[x1.length];
        double conta = 0;

        for (int i = 0; i < x.length; i++) {
            conta = conta + x[i];
        }
        for (int i = 0; i < x.length; i++)
            norma[i] =x[i] / conta;

        conta = 0;

        for (int i = 0; i < x1.length; i++) {
            conta = conta + x1[i];
        }
        for (int i = 0; i < x1.length; i++)
            norma1[i] =x1[i] / conta;


        if(norma[0] == outputExpected[0] && norma[1] == outputExpected[1] && norma[2] == outputExpected[2] && norma1[0] == outputExpected[3] && norma1[1] == outputExpected[4] && norma1[2] == outputExpected[5])
        {
            return true;
        }
        else
            return false;

    }

    public static boolean testeMatrizQuadrada(){

        double [][] matrizB = new double[3][3];
        double [][] matriz = {{0, 1, 3}, {0.5, 0, 0}, {0, 0.5, 0}};

        testesUni.matrizquadrada(matriz,matrizB);
        double [][] ExpectedMatrizB = {{0.5,1.5,0},{0,0.5,1.5},{0.25,0,0}};

        int cont=0;

        for (int l=0; l<ExpectedMatrizB.length; l++){
            for(int c=0; c<ExpectedMatrizB.length; c++){
                if(ExpectedMatrizB[l][c] == matrizB[l][c]) {
                    cont += 1;
                }
            }
        }
        int cont1=0;

        double[][]matriz1= {{1,2},{3,4}};
        double[][] matrizC = new double[2][2];
        double [][] ExpectedMatrizC = {{7,10},{15,22}};
        testesUni.matrizquadrada(matriz1,matrizC);

        for (int l=0; l<ExpectedMatrizC.length; l++){
            for(int c=0; c<ExpectedMatrizC.length; c++){
                if(ExpectedMatrizC[l][c] == matrizC[l][c]) {
                    cont1 += 1;
                }
            }
        }

        int cont2=0;
        double[][]matriz2= {{1,2,3,4},{1,2,3,4},{4,3,2,1},{4,3,2,1}};
        double[][] matrizD = new double[4][4];
        double [][] ExpectedMatrizD = {{31,27,23,19},{31,27,23,19},{19,23,27,31},{19,23,27,31}};
        testesUni.matrizquadrada(matriz2,matrizD);

        for (int l=0; l<ExpectedMatrizD.length; l++){
            for(int c=0; c<ExpectedMatrizD.length; c++){
                if(ExpectedMatrizD[l][c] == matrizD[l][c]) {
                    cont2 += 1;
                }
            }
        }



        if (cont == Math.pow(ExpectedMatrizB.length,2) && cont1 == Math.pow(ExpectedMatrizC.length,2) && cont2 == Math.pow(ExpectedMatrizD.length,2)) {
            return true;
        } else{
            return false;
        }
    }


}
