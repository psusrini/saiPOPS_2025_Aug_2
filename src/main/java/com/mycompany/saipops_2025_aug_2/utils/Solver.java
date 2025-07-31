/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops_2025_aug_2.utils;
        
import com.mycompany.saipops_2025_aug_2.constraints.LowerBoundConstraint;
import static com.mycompany.saipops_2025_aug_2.Constants.*;
import static com.mycompany.saipops_2025_aug_2.Parameters.*;
import com.mycompany.saipops_2025_aug_2.callbacks.SAIPOPS_Callback;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import static java.lang.System.exit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet; 
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
 


/**
 *
 * @author sst119
 */
public class Solver {
      
    private static final Logger logger ;
    private    IloCplex cplex;
    
    static {
        logger= Logger.getLogger(Solver.class.getSimpleName() );
        //logger.setLevel(Level.INFO);
        try {
             
            FileHandler fileHandler = new FileHandler(Solver.class.getSimpleName()+ ".log");
 
            fileHandler.setFormatter(new SimpleFormatter());
 
            logger.addHandler(fileHandler);
 
            logger.info("Logging initialized.");
            

        } catch (Exception e) {
            System.err.println(e.getMessage()) ;
            exit(ONE);
        }
        
    }
     
    public    TreeMap<String, Double>  objectiveFunctionMap =null;
    public  TreeMap<String, IloNumVar>  mapOfAllVariablesInTheModel = new TreeMap<String, IloNumVar> ();
    //constraints, smallest first
    public  TreeMap<Integer, HashSet<LowerBoundConstraint>> mapOfAllConstraintsInTheModel = 
            new TreeMap<Integer, HashSet<LowerBoundConstraint>> ();
     
   
    public Solver (  ) throws Exception{
   
        initCplex(); 
        
        for (HashSet<LowerBoundConstraint> cSet : mapOfAllConstraintsInTheModel.values()){
            for (LowerBoundConstraint lbc : cSet){
                lbc.sort(); 
                 
            }           
        }
                
        IloCplex.BranchCallback callback =           
                new SAIPOPS_Callback( 
                    objectiveFunctionMap,
                    mapOfAllVariablesInTheModel,                             
                    mapOfAllConstraintsInTheModel
                );
       
        cplex.use(callback) ;        
        
    }
    
   
    public void solve () throws IloException{
        System.out.println ("Solve invoked ..." );
        for (int periods = ONE; periods <=  MAX_PERIODS ; periods ++){                
            cplex.solve();
            print_statistics (cplex, periods) ;
            
          
            if (cplex.getStatus().equals( IloCplex.Status.Infeasible)) break;
            if (cplex.getStatus().equals( IloCplex.Status.Optimal)) break;
            
                        

        }
        cplex.end();
        System.out.println  ("Solve completed." );
    }
    
    
    private void initCplex ( ) throws Exception{
        cplex = new IloCplex ();
        
        System.out.println ("CPLEX version "+ cplex.getVersion());
        boolean isWindows =   System.getProperty("os.name").toLowerCase().contains("win") ;
        if (!cplex.getVersion().startsWith("22") && ! isWindows){
            System.err.println ("CPLEX version not the latest -- STOP" );
            exit(ONE);
        }
        
        cplex.importModel( PRESOLVED_MIP_FILENAME);
        CplexUtils.setCplexParameters(cplex) ;
      
      
        objectiveFunctionMap = CplexUtils.getObjective(cplex);
                
        for ( IloNumVar var : CplexUtils.getVariables(cplex)){
            mapOfAllVariablesInTheModel.put (var.getName(), var);
        }
        
        List<LowerBoundConstraint> lbcList = CplexUtils.getConstraints(cplex,objectiveFunctionMap );
                
        //arrange by size
        for (LowerBoundConstraint lbc: lbcList){
            int numVars = lbc.getVariableCount();
                        
            HashSet<LowerBoundConstraint> current =  mapOfAllConstraintsInTheModel.get (numVars);
            if (null==current) current = new HashSet<LowerBoundConstraint>();
            current.add (lbc) ;
            mapOfAllConstraintsInTheModel.put (numVars, current);               
        }
                                               
    }
    
       
    private void print_statistics (IloCplex cplex, int hour) throws IloException {
        double bestSoln = BILLION;
        double relativeMipGap = BILLION;
        IloCplex.Status cplexStatus  = cplex.getStatus();
        if (cplexStatus.equals( IloCplex.Status.Feasible)  ||cplexStatus.equals( IloCplex.Status.Optimal) ) {
            bestSoln=cplex.getObjValue();
            //relativeMipGap=  cplex.getMIPRelativeGap();
        }
        logger.info ("" + hour + ","+  bestSoln + ","+  
                cplex.getBestObjValue() + "," + cplex.getNnodesLeft64() +
                "," + cplex.getNnodes64() /*+ "," + relativeMipGap*/ ) ;
    }
    
}
