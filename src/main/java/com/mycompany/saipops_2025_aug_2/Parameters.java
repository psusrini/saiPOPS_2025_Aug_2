/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops_2025_aug_2;
   
import static com.mycompany.saipops_2025_aug_2.Constants.*;
import ilog.cplex.IloCplex;
import static java.lang.System.exit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author sst119
 */
public class Parameters {
    
    private static final Logger logger ;
    
    
    static {
        logger= Logger.getLogger(Parameters.class.getSimpleName() );
        //logger.setLevel(Level.INFO);
        try {
            FileHandler fileHandler = new FileHandler(Parameters.class.getSimpleName()+ ".log");

            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);

            logger.info("Logging initialized.");
            

        } catch (Exception e) {
            System.err.println(e.getMessage()) ;
            exit(ONE);
        }
        
    }
    
    public static final int  MIP_EMPHASIS = 2 ;
    
    //cplex config related
    public static final int  HEUR_FREQ  = -1 ;    
    public static final int  FILE_STRATEGY= 3;  
    public static final int MAX_THREADS =  System.getProperty("os.name").toLowerCase().contains("win") ? 1 : 32;
     
    public static final int MAX_PERIODS =5;
    public static final int PERIOD_DURATION_IN_SECONDS =60*60; //60 minute periods
     
    public static final HeuristicEnum HEURISTIC_TO_USE = HeuristicEnum.CPLEX;
    public static final Boolean USE_OPTIMISTIC_DIMENSIONING = true;
    
    public static final String PRESOLVED_MIP_FILENAME =              
            System.getProperty("os.name").toLowerCase().contains("win") ?
     
    //  "c:\\temp\\mips\\2club200v.pre.sav":
  //  "c:\\temp\\mips\\LTL8.pre.sav": 
            
   // "c:\\temp\\mips\\bab1.pre.sav":
            
    //   "c:\\temp\\mips\\bab2.pre.sav":
    //   "c:\\temp\\mips\\bab6.pre.sav":
            
    //   "c:\\temp\\mips\\bnatt500.pre.sav":
            
   //    "c:\\temp\\mips\\ds.pre.sav":    
    
    //   "c:\\temp\\mips\\hanoi5.pre.sav":  
            
    //   "c:\\temp\\mips\\ivu52.pre.sav":
            
    //   "c:\\temp\\mips\\neos-beardy.pre.sav":        
    //   "c:\\temp\\mips\\neos-952987.pre.sav":
    //    "c:\\temp\\mips\\neos-954925.pre.sav":  
            
     "c:\\temp\\mips\\opm2-z10-s4.pre.sav":
    //   "c:\\temp\\mips\\opm2-z12-s7.pre.sav":        
    //  "c:\\temp\\mips\\opm2-z12-s8.pre.sav":   
    //    "c:\\temp\\mips\\opm2-z12-s14.pre.sav":   
            
            
    //    "c:\\temp\\mips\\p6b.pre.sav":
    //   "c:\\temp\\mips\\protfold.pre.sav":
            
    //    "c:\\temp\\mips\\queens-30.pre.sav":
            
    //   "c:\\temp\\mips\\rail03.pre.sav":
  // "c:\\temp\\mips\\reblock354.pre.sav":            
    //    "c:\\temp\\mips\\rmine10.pre.sav":        
    //  "c:\\temp\\mips\\rvb-sub.pre.sav":
            
    // "c:\\temp\\mips\\s100.pre.sav":
    //   "c:\\temp\\mips\\s1234.pre.sav":            
     //   "c:\\temp\\mips\\seymour-disj-10.pre.sav":      
    //   "c:\\temp\\mips\\sorrell3.pre.sav":    
    //   "c:\\temp\\mips\\stp3d.pre.sav":
    //   "c:\\temp\\mips\\supportcase10.pre.sav":
            
    // "c:\\temp\\mips\\v150d30-2hopcds.pre.sav":
            
    //   "c:\\temp\\mips\\wnq.pre.sav":
                                                    
    //"C:\\temp\\MIPS\\knapsack.lp":          
     
            
    // MIPs with no objective such as bnatt500        
            
    //Open problems        
     
    "PBO.pre.sav";
    
           
    //for perf variability testing  
    public static final long PERF_VARIABILITY_RANDOM_SEED = 0;
    public static final java.util.Random  PERF_VARIABILITY_RANDOM_GENERATOR =             
            new  java.util.Random  (PERF_VARIABILITY_RANDOM_SEED);   
    
    
    public static void printParameters (){
        logger.info("MIP_EMPHASIS "+ MIP_EMPHASIS);
        logger.info("HEURISTIC_TO_USE "+ HEURISTIC_TO_USE);
        logger.info("PERF_VARIABILITY_RANDOM_SEED "+ PERF_VARIABILITY_RANDOM_SEED);
        logger.info("USE_OPTIMISTIC_DIMENSIONING "+ USE_OPTIMISTIC_DIMENSIONING);
        
                
    }
    
}
