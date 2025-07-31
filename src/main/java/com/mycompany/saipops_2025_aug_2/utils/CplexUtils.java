/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops_2025_aug_2.utils;
         
import com.mycompany.saipops_2025_aug_2.constraints.Triplet;
import com.mycompany.saipops_2025_aug_2.constraints.LowerBoundConstraint;
import static com.mycompany.saipops_2025_aug_2.Constants.*;
import static com.mycompany.saipops_2025_aug_2.Parameters.*; 
import ilog.concert.IloException;
import ilog.concert.IloLPMatrix;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloLinearNumExprIterator;
import ilog.concert.IloNumVar;
import ilog.concert.IloObjective;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author sst119
 */
public class CplexUtils {
    
    public static void setCplexParameters (IloCplex cplex) throws IloException {
        
        cplex.setParam( IloCplex.Param.TimeLimit,  PERIOD_DURATION_IN_SECONDS);
         
        cplex.setParam( IloCplex.Param.Threads, MAX_THREADS);
        
        cplex.setParam( IloCplex.Param.MIP.Strategy.File, FILE_STRATEGY);
        
        cplex.setParam( IloCplex.Param.Emphasis.MIP,  MIP_EMPHASIS);
       
        cplex.setParam( IloCplex.Param.MIP.Strategy.HeuristicFreq , HEUR_FREQ);
        
    }
     
    
    public static List<LowerBoundConstraint> 
        getConstraints (IloCplex cplex, 
                TreeMap<String, Double> objectiveCoeffsMap) 
                throws IloException{
         
        List<LowerBoundConstraint> result = new ArrayList<LowerBoundConstraint>( );
        
        IloLPMatrix lpMatrix = (IloLPMatrix)cplex.LPMatrixIterator().next();        
        final int NUM_CONSTRAINTS = lpMatrix.getNrows();
        
        int[][] ind = new int[ NUM_CONSTRAINTS][];
        double[][] val = new double[ NUM_CONSTRAINTS][];
        
        double[] lb = new double[NUM_CONSTRAINTS] ;
        double[] ub = new double[NUM_CONSTRAINTS] ;
        
        lpMatrix.getRows(ZERO,   NUM_CONSTRAINTS, lb, ub, ind, val);
        
        IloRange[] ranges = lpMatrix.getRanges() ;        
        
        //build up each constraint 
        for (int index=ZERO; index < NUM_CONSTRAINTS ; index ++ ){
            
            String thisConstraintname = ranges[index].getName();
          
            final int NUM_VARS_IN_CONSTRAINT =  ind[index].length;
                      
            boolean isUpperBound = Math.abs(ub[index])< BILLION ;
            boolean isLowerBound = Math.abs(lb[index])<BILLION ;
            boolean isEquality = ub[index]==lb[index];
            
            if  (isEquality)  {
                LowerBoundConstraint lbcUP =new LowerBoundConstraint(lb[index], thisConstraintname + "_UP");
                LowerBoundConstraint lbcDOWN =new LowerBoundConstraint(-ub[index], thisConstraintname + "_Down" );
                                 
                for (  int varIndex = ZERO;varIndex< NUM_VARS_IN_CONSTRAINT;   varIndex ++ ){
                    String var = lpMatrix.getNumVar(ind[index][varIndex]).getName() ;
                    Double coeff = val[index][varIndex];
                    double ObjectiveCoeff = objectiveCoeffsMap.get(var) ;
                    lbcUP.add(new Triplet(var,  coeff,  ObjectiveCoeff  )) ;
                    lbcDOWN.add(new Triplet(var, -coeff, ObjectiveCoeff));    
                    
                }
                
                result.add(lbcUP);
                result.add(lbcDOWN);
                
            }else {
                
                //not an equailty constraint
                LowerBoundConstraint lbc =new LowerBoundConstraint
                    ((isUpperBound && ! isLowerBound )? -ub[index] : lb[index], thisConstraintname);
                 
                for (  int varIndex = ZERO;varIndex< NUM_VARS_IN_CONSTRAINT;   varIndex ++ ){
                    String var = lpMatrix.getNumVar(ind[index][varIndex]).getName() ;
                    Double coeff = val[index][varIndex];
                    lbc.add(new Triplet(var, (isUpperBound && ! isLowerBound )? -coeff: coeff, objectiveCoeffsMap.get(var))) ;     
                                        
                }
                
                result.add(lbc) ; 
                
            }
            
        }
     
        return result;
               
    }
    
    
    public static TreeMap<String, Double> getObjective (IloCplex cplex) throws IloException {
        
        TreeMap<String, Double>  objectiveMap = new TreeMap<String, Double>();
        
        IloObjective  obj = cplex.getObjective();
       
        IloLinearNumExpr expr = (IloLinearNumExpr) obj.getExpr();
                 
        IloLinearNumExprIterator iter = expr.linearIterator();
        while (iter.hasNext()) {
           IloNumVar var = iter.nextNumVar();
           double val = iter.getValue();
                     
           objectiveMap.put(var.getName(),   val   );
           //note - 0 val is put if var is missing from the objective           
        }
        
        return  objectiveMap ;
        
         
    }
        
    public static List<IloNumVar> getVariables (IloCplex cplex) throws IloException{
        List<IloNumVar> result = new ArrayList<IloNumVar>();
        IloLPMatrix lpMatrix = (IloLPMatrix)cplex.LPMatrixIterator().next();
        IloNumVar[] variables  =lpMatrix.getNumVars();
        for (IloNumVar var :variables){
            result.add(var ) ;
        }
        return result;
    }
    
    
}
