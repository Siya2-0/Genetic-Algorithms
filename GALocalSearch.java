import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.LinkedList;
import java.math.BigInteger;

public class GALocalSearch{

    //Parameters
    Random RandomGeneration;
    int PopulationSize;
    int ChromosomeLength;
    float KnapSackLimit;
    int TournmentSize;
    int NumberOfGeneration=200;
    String Filename;
    int FileLines;//number of items avaiable
    float Data[][];
    long seed;

    public void SetParameters(String FileName, long seed)
    {
        this.Filename=FileName;
        SetFileLines();
        PopulateData();
        SetChromosomeLength();
        SetKnapSackLimit(FileName);
        SetTournmentSize();
        SetPopulationSize();
        //this.seed=System.currentTimeMillis ();
        this.seed=seed;
        RandomGeneration=new Random(seed);
        System.out.println("File :"+ Filename);
        System.out.println("KnapSackLimit :"+ KnapSackLimit);
        System.out.println("Tournment Size :"+ TournmentSize);
        System.out.println("No items in the Sack :"+ FileLines);
        System.out.println("Population Size: "+ PopulationSize);
        System.out.println("Seed value: " + seed);


    }

    public void SetTournmentSize()
    {
        if(FileLines<=5)
            TournmentSize=2;
        else if(FileLines<=15 && FileLines>5)
            TournmentSize=3;
        else if(FileLines>15 && FileLines<=30)
            TournmentSize=4;
        else{
            TournmentSize=(int)0.20*FileLines;
        }
    }
   

    public int CountFileLines(String Filename)
    {
        String FirstLine="";
        try {
            // Open the file
            FileReader fileReader = new FileReader(Filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            FirstLine=bufferedReader.readLine();
            // Read each line until the end of the file

            // Close the file
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        String[] SplitString=FirstLine.split(" ");
        return Integer.parseInt(SplitString[0]);
    }//PASSED TEST


    public float SetKnapSackLimit(String Filename)
    {
        String FirstLine="";
        try {
            // Open the file
            FileReader fileReader = new FileReader(Filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            FirstLine=bufferedReader.readLine();
            // Read each line until the end of the file

            // Close the file
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        String[] SplitString=FirstLine.split(" ");

        KnapSackLimit= Float.parseFloat(SplitString[1]);
        return KnapSackLimit;

    }//PASSED TEST
  
    public void SetFileName(String Filename)
    {
        this.Filename=Filename;
    }

    public void SetFileLines()
    {
        if(Filename !=""){
            FileLines=CountFileLines(Filename);
        }
        else{
            System.out.println("FileName not set");
        }
    }

    public void PopulateData()
    {
        if(Filename != "")
        {
            int row=0;
            if( FileLines !=0)
            {
                Data= new float[FileLines][2];
                try {
                    // Open the file
                    FileReader fileReader = new FileReader(Filename);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
        
                    String line=bufferedReader.readLine();//first line
                    // Read each line until the end of the file
                    while (((line=bufferedReader.readLine()) != null) &&(row<FileLines) ) {
                        // Process each line here, for example, print it
                        String[] values=line.split(" ");
                        //System.out.println(values[0]  +"  "+ values[1]);
                        Data[row][0]=Float.parseFloat(values[0]);
                        Data[row][1]=Float.parseFloat(values[1]);
                        row++;
                    }
        
                    // Close the file
                    bufferedReader.close();
                } catch (IOException e) {
                    System.err.println("Error reading the file: " + e.getMessage());
                }

            }
            else{
                System.out.println("File is empty");
            }

        }
        else{
            System.out.println("Filename not set");
        }
    }

    public void SetPopulationSize()
    {
        if(FileLines<=5)
            PopulationSize=10;
        else if(FileLines<=10 && FileLines>5)
            PopulationSize=13;
        else if(FileLines<=20 && FileLines>10)
            PopulationSize=25;
        else
            PopulationSize=35;
    }

  
    public void SetChromosomeLength()
    {
        ChromosomeLength=CountFileLines(Filename);
    }
    
    public int PrimaryHash()
    {
        return RandomGeneration.nextInt((Data.length));
    }

    public int SecondaryHash()
    {
        
        int save=RandomGeneration.nextInt()%Data.length;
        if(save<0)
            save*=-1;
        while(save==0)
        {
            save=RandomGeneration.nextInt()%Data.length;
            if(save<0)
                save*=-1;
        }

        return save;
    }
    public boolean contains(int[]list, int value)
    {
        
        for(int i=0; i<list.length; i++)
        {
            if(list[i]==value)
            {
                return true;
            }
            else if(list[i]==-999)
            {
                return false;
            }
        }
        return false;
    } 

    public float[][] LocalSearch(float[][]Candidate)
    {
        Random rand= new Random();
        int Remove=rand.nextInt(CountActiveGenes(Candidate));
        int Replacement=rand.nextInt(FileLines);
        float[][]Result=Copy(Candidate);
        int stuck=0;
      
        while(IsContained(Candidate, Replacement) || TotalValue(Candidate)-Candidate[Remove][0]+Data[Replacement][0]>KnapSackLimit)
        {
         
            Replacement=rand.nextInt(FileLines);
            if(stuck>200)
            {
                return Candidate;
            }
            stuck++;
        }
        

        Result[Remove][0]=Data[Replacement][0];
        Result[Remove][1]=Data[Replacement][1];
        

        return Result;
    }
    public float[][] TabuSearch(float[][] Candidate)
    {
        float[][]Solution=Copy(Candidate);
        LinkedList<float [][]> TabuList= new LinkedList<>();
        TabuList.add(Solution);
        for(int i=0; i<35;i++)
        {
            float[][] neighbouring=LocalSearch(Solution);
            
            if(IndexOf(TabuList, neighbouring)==-1)//SOLUTION NOT VISITED BEFORE
            {
                TabuList.add(neighbouring);
              
                
            }
            if(TotalValue(neighbouring)>=TotalValue(Candidate) && TotalWeight(neighbouring)<=KnapSackLimit)
            {
                Solution=Copy(neighbouring);
            }
           
        }
        return FindBestSolution(TabuList);
    }

    public float[][] RandomGenerateOneSolution()
    {
        float RandomSolution[][]=new float[ChromosomeLength][2];
        int[] place= new int[ChromosomeLength];
        for(int i=0; i<ChromosomeLength; i++)
        {
            RandomSolution[i][0]=0;
            RandomSolution[i][1]=0;
            place[i]=-999;
        }
      
        int primaryIndex;
        
        //PopulateData();
        for(int step=0; step<ChromosomeLength; )
        {
           
            primaryIndex=PrimaryHash();
            while( contains(place, primaryIndex) )
            {
                primaryIndex=PrimaryHash();
          
            }
            if(RandomSolution[step][0]==0)//empty
            {
                if((TotalWeight(RandomSolution)+Data[primaryIndex][1])>KnapSackLimit)
                {
                    break;
                }
                RandomSolution[step][0]=Data[primaryIndex][0];
                RandomSolution[step][1]=Data[primaryIndex][1];
                place[step]=primaryIndex;
                step++;
            }
            
            
        }


        return RandomSolution;
    }//PASSED TEST
    public int CountActiveGenes(float[][]data)
    {
        int count=0;
        for(int i=0; i<data.length; i++)
        {
            if(data[i][0] !=0 && data[i][1] != 0)
            {
                count++;
            }
        }
        return count;
    }

    public void PrintData(float[][] data)
    {
        for(int i=0; i<data.length; i++)
        {
            if(data[i][0]!=0 && data[i][1] != 0)
            {
                System.out.println(data[i][0]+ " - "+ data[i][1] );
            }
        }
        System.out.println("Total Value "+ TotalValue(data));
        System.out.println("Total Weight "+ TotalWeight(data));
    }//PASSED TEST
    public LinkedList<float[][]> RandomGeneratePopulation()
    {
        LinkedList<float[][]> Population=new LinkedList<>();
        for(int i=0; i<PopulationSize; i++)
        {
            Population.add(RandomGenerateOneSolution());
        }

        return Population;
    }//PASSED TEST
   
    public float TotalValue(float[][] sack)
    {
        float cost=0;
        for(int i=0; i<sack.length; i++ )
        {
        
            cost+=sack[i][1];
        }
        return cost;
    }
    

    


    public boolean Contains(int[] arr, int element)
    {
        for(int i=0; i<arr.length; i++)
        {
            if(arr[i]==element)
                return true;
        }
        return false;
    }
    //tournment selection
    public float[][] TournmentSeletion(LinkedList<float[][]> Population)
    {
      
        int[] choose= new int[TournmentSize];

        for(int i=0; i<TournmentSize; i++)
        {
            choose[i]=-999;
        }
        int best=-1;
        for(int i=0; i<TournmentSize; i++)
        {
            int r=RandomGeneration.nextInt((Population.size()));
            while(contains(choose, r))
            {
                r=RandomGeneration.nextInt((Population.size()));
             
            }
            choose[i]=r;
        
            if(best==-1)
            {
                best=r;
            }
            else{
                if(TotalValue(Population.get(best)) < TotalValue(Population.get(r)))
                {
                    best=r;
                }

            }

        }
        return Copy(Population.get(best));
    }//PASSED TEST

    public float[][] Copy(float[][]data)
    {
        float[][]rt= new float[data.length][data[0].length];

        for(int i=0; i<rt.length; i++)
        {
            for(int c=0; c<data[0].length; c++)
            {
                rt[i][c]=data[i][c];
            }
        }

        return rt;
    }

    public float TotalWeight(float[][]data)
    {
        float cost=0;
        for(int i=0; i<data.length; i++ )
        {
            cost+=data[i][0];
        }
        return cost;

    }
    
    public int CountDifferentGenes(float[][]small, float[][] large)
    {
        int DiffCount=0;
        for(int s=0; s<CountActiveGenes(small); s++)
        {
            for(int l=0; l<CountActiveGenes(large); l++)
            {
                if(small[s][0]==large[l][0]&& small[s][1]==large[l][1])
                {
                    DiffCount++;
                    break;
                }
              
            }
        }

        return CountActiveGenes(small)-DiffCount;
    }

    public boolean AyiKho(float[][] list, float Weight, float Value )
    {
        for(int i=0; i<CountActiveGenes(list); i++)
        {
            if(list[i][0]==Weight && list[i][1]==Value)
            {
                return false;
            }
        }
        return true;
    }
   
    public LinkedList<float[][]>Crossover(float[][] parent1, float[][] parent2)
    {
       
       
        float[][] offspring1=Copy(parent1);
        float[][] offspring2=Copy(parent2);
        if(CountActiveGenes(offspring1)>CountActiveGenes(offspring2))
        {
            int count=CountDifferentGenes(offspring2, offspring1);

            if(count<3)
            {
                int CountExchances=0;
                for(int p2=0; p2<offspring2.length; p2++)
                {
                    if(AyiKho(parent1, offspring2[p2][0], offspring2[p2][1]))
                    {
                        for(int p1=0; p1<offspring1.length; p1++)
                        {
                            if(AyiKho(parent2, offspring1[p1][0], offspring1[p1][1]))
                            {
                                float temp1, temp2;
                                temp1=offspring1[p1][0];
                                temp2=offspring1[p1][1];

                                offspring1[p1][0]=offspring2[p2][0];
                                offspring1[p1][1]=offspring2[p2][1];

                                offspring2[p2][0]=temp1;
                                offspring2[p2][1]=temp2;
                                CountExchances++;
                                break;
                            }
                        }
                    }
                    if(CountExchances==count)
                        break;
                }

            }
            else{
                int CountCrossOverPoints=RandomGeneration.nextInt(count)+1;
                
                int CountExchances=0;
                for(int p2=0; p2<offspring2.length; p2++)
                {
                    if(AyiKho(parent1, offspring2[p2][0], offspring2[p2][1]))
                    {
                        for(int p1=0; p1<offspring1.length; p1++)
                        {
                            if(AyiKho(parent2, offspring1[p1][0], offspring1[p1][1]))
                            {
                                float temp1, temp2;
                                temp1=offspring1[p1][0];
                                temp2=offspring1[p1][1];

                                offspring1[p1][0]=offspring2[p2][0];
                                offspring1[p1][1]=offspring2[p2][1];

                                offspring2[p2][0]=temp1;
                                offspring2[p2][1]=temp2;
                                CountExchances++;
                                break;
                            }
                        }
                    }
                    if(CountExchances==CountCrossOverPoints)
                        break;
                }
                

            }

        }
        else{
            //
            int count=CountDifferentGenes(offspring1, offspring2);

            if(count<3)
            {
                
                int CountExchances=0;
                for(int p1=0; p1<CountActiveGenes(offspring1); p1++)
                {
                    if(AyiKho(parent2, offspring1[p1][0], offspring1[p1][1]))
                    {
                        for(int p2=0; p2<CountActiveGenes(offspring2); p2++)
                        {
                            if(AyiKho(parent1, offspring2[p2][0], offspring2[p2][1]))
                            {
                                float temp1, temp2;
                                temp1=offspring1[p1][0];
                                temp2=offspring1[p1][1];

                                offspring1[p1][0]=offspring2[p2][0];
                                offspring1[p1][1]=offspring2[p2][1];

                                offspring2[p2][0]=temp1;
                                offspring2[p2][1]=temp2;
                                CountExchances++;
                                break;
                            }
                        }
                    }
                    if(CountExchances==count)
                        break;
                }

            }
            else{
                int CountCrossOverPoints=RandomGeneration.nextInt(count)+1;
                //System.out.println("Cross "+CountCrossOverPoints);
                
                int CountExchances=0;
                for(int p1=0; p1<CountActiveGenes(offspring1); p1++)
                {
                    if(AyiKho(parent2, offspring1[p1][0], offspring1[p1][1])  &&  offspring1[p1][0] !=0 &&  offspring1[p1][1]!=0)
                    {
                        for(int p2=0; p2<CountActiveGenes(offspring2); p2++)
                        {
                            if(AyiKho(parent1, offspring2[p2][0], offspring2[p2][1])  &&  offspring2[p2][0] !=0 &&  offspring2[p2][1]!=0)
                            {
                                float temp1, temp2;
                                temp1=offspring1[p1][0];
                                temp2=offspring1[p1][1];

                                offspring1[p1][0]=offspring2[p2][0];
                                offspring1[p1][1]=offspring2[p2][1];

                                offspring2[p2][0]=temp1;
                                offspring2[p2][1]=temp2;
                                CountExchances++;
                                break;
                            }
                        }
                    }
                    if(CountExchances==CountCrossOverPoints)
                        break;
                }
                

            }

        }

        

        LinkedList<float[][]> offspring= new LinkedList<>();
        if(TotalWeight(offspring1)<=KnapSackLimit)
            offspring.add(offspring1);
        
        if(TotalWeight(offspring2)<=KnapSackLimit)
            offspring.add(offspring2);

        
        if(offspring.size() !=2 &&TotalValue(offspring1)>TotalValue(offspring2))
        {
            offspring.add(offspring1);
            if(offspring.size() !=2)
                offspring.add(offspring2);
        }
        else if(offspring.size() !=2 &&TotalValue(offspring1)<=TotalValue(offspring2))
        {
            offspring.add(offspring2);
            if(offspring.size() !=2)
                offspring.add(offspring1);
        }
        return offspring;
        
    }

   
    public boolean IsContained(float[][] candidate, int ItemIndex)
    {
        for(int i=0; i<candidate.length; i++)
        {
            if(candidate[i][0]==Data[ItemIndex][0] && candidate[i][1]==Data[ItemIndex][1] )
            {
                return true;
            }
        }
        return false;
    }

    public float[][] Mutation(float[][] original)
    {
      
        int MutateIndex=RandomGeneration.nextInt(original.length);
      
        if(CountActiveGenes(original)==FileLines){
            return RandomGenerateOneSolution();
        }

        int SubstitueItem=RandomGeneration.nextInt(FileLines);
        while(IsContained(original, SubstitueItem))
        {
            SubstitueItem=RandomGeneration.nextInt(FileLines);
        }


        float[][]result=Copy(original);
        result[MutateIndex][0]=Data[SubstitueItem][0];
        result[MutateIndex][1]=Data[SubstitueItem][1];
        if(TotalWeight(result)<=KnapSackLimit)
            return result;

        
        return original;
    }

    public int IndexOf(LinkedList<float[][]> population, float[][]find)
    {
        for(int i=0; i<population.size(); i++)
        {
            if(Equality(population.get(i), find))
                return i;
        }
        return -1;
    }

    public boolean Equality(float[][]data1, float[][]data2)
    {
        if(data1.length != data2.length || data1[0].length != data2[0].length )
            return false;

        for(int i=0; i<data1.length; i++)
        {
            if(data1[i][0]!= data2[i][0] && data1[i][1]!= data2[i][1] )
                return false;
        }
        return true;
    }
    public float[][] FindBestSolution(LinkedList<float[][]> population)
    {
        int BestIndex=-1;
        for(int i=0; i<population.size(); i++)
        {
            if(BestIndex==-1 && TotalWeight(population.get(i))<=KnapSackLimit)
            {
                BestIndex=i;
            }
            else if(BestIndex!=-1 && TotalValue(population.get(i))  > TotalValue(population.get(BestIndex)) && TotalWeight(population.get(i))<=KnapSackLimit )
            {
                BestIndex=i;
            }
        }
        if(BestIndex==-1)
            return Copy(population.get(0));
        return Copy(population.get(BestIndex));
    }
    public float[][] GeneticAlgorithm(String FileName , long seed)
    {
       
        SetParameters(FileName, seed);
    
        LinkedList<float[][]> Population=RandomGeneratePopulation();
       
        int exploration=0;
        for(int i=0; i<NumberOfGeneration; i++)
        {
            float[][]parent1=TournmentSeletion(Population);
            float[][]parent2=TournmentSeletion(Population);
            while(Equality(parent1, parent2))
            {
                parent1=TournmentSeletion(Population);
                parent2=TournmentSeletion(Population);
                if(exploration>200)
                {
                    LinkedList<float[][]>Random=RandomGeneratePopulation();
                    for(int r=((int)0.2*Population.size()); r<Population.size(); r++)
                    {
                        Population.set(r,Random.get(r));  
                    }
                    exploration=0;
                    parent1=TournmentSeletion(Population);
                    parent2=TournmentSeletion(Population);

                }
                exploration++;
            }
            LinkedList<float[][]>offspring=Crossover(parent1, parent2);

            offspring.set(0, Mutation(offspring.get(0)));
            offspring.set(1, Mutation(offspring.get(1)));

            offspring.set(0, TabuSearch(offspring.get(0)));
            offspring.set(1, TabuSearch(offspring.get(1)));


            //ned to pick replace strategy steady, generations

            Population.set(IndexOf(Population, parent1), offspring.get(0));
            Population.set(IndexOf(Population, parent2), offspring.get(1));


        }

        return FindBestSolution(Population);
    }


}