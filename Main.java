import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        
        String File="f10_l-d_kp_20_879";
        //for(int i=0; i<23; i++){
            long seed= System.currentTimeMillis ();
            Simulation(File,seed);
            Simulation2(File,seed);
      //  }
         
        
      
    }

    public static void Simulation2(String File,long seed)
    {
        System.out.println("");
        System.out.println("Genetic Algorithms With Local Search");
        long startTime = System.nanoTime();
        GALocalSearch one= new GALocalSearch();
        float[][] data=one.GeneticAlgorithm(File,  seed);
        one.PrintData(data);
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Execution time in seconds: " + ((float)totalTime/1000000000));
        //AppendResultToFile("GALocal.txt", one.TotalValue(data));

    }
    public static void AppendResultToFile(String Filename, float Value)
    {
        try {
            // Open the file in append mode
            FileWriter fileWriter = new FileWriter(Filename, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Append the number to a new line
            bufferedWriter.write(Float.toString(Value));
            bufferedWriter.newLine();

            // Close the writer
            bufferedWriter.close();
           // System.out.println("Number appended successfully to " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void Simulation(String File,long seed)
    {
        System.out.println("");
        System.out.println("Genetic Algorithms");
        long startTime = System.nanoTime(); 
        GA one= new GA();
        float[][] data=one.GeneticAlgorithm(File, seed);
        one.PrintData(data);
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Execution time in seconds: " + ((float)totalTime/1000000000));
        //AppendResultToFile("GA.txt", one.TotalValue(data));

    }

    public static void  TestFunction(String FileName)
    {
        GA one= new GA();
        one.SetFileName(FileName);
        one.SetFileLines();
        one.SetKnapSackLimit(FileName);
        one.SetChromosomeLength();
        System.out.println("FileLines: " + one.CountFileLines(FileName)); 
        System.out.println("KnapSackLimit: " + one.SetKnapSackLimit(FileName)); 
        System.out.println("Chromosome: " + one.ChromosomeLength); 
        one.PopulateData();
        float[][]data=one.Data;
        System.out.println(FileName + "  data");
       // one.PrintData(data);
        LinkedList<float [][]> population=one.RandomGeneratePopulation();
        for(int i=0; i<population.size(); i++ )
        {
            System.out.println("Solution "+ i);
            one.PrintData(population.get(i));
        }
        System.out.println("Tournment");
        float [][]data2=one.TournmentSeletion(population);
        float [][]data3=one.TournmentSeletion(population);
        System.out.println("Parent1");
        one.PrintData(data2);
        System.out.println("Parent2");
        one.PrintData(data3);

        System.out.println("CrossOver");
        LinkedList<float[][]> listt=one.Crossover(data2, data3);
        for(int i=0; i<listt.size(); i++ )
        {
            System.out.println("Solution "+ i);
            one.PrintData(listt.get(i));
        }

        System.out.println("Mutation");
        one.PrintData(data2);
        System.out.print("After Mutation");
        one.PrintData(one.Mutation(data2));
        System.out.println("Index");
        float[][] copy=one.Copy( listt.get(0));
        System.out.println(one.IndexOf(listt,copy ));
        System.out.println("best");
        float [][]d=one.FindBestSolution(listt);
        one.PrintData(d);
    }
    
}
