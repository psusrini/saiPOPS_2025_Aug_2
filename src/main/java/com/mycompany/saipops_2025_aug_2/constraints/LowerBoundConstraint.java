/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops_2025_aug_2.constraints;
        
import static com.mycompany.saipops_2025_aug_2.Constants.*;
import static com.mycompany.saipops_2025_aug_2.Parameters.*;
import com.mycompany.saipops_2025_aug_2.SignificanceEnum;
import static com.mycompany.saipops_2025_aug_2.SignificanceEnum.*;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author sst119
 */
public class LowerBoundConstraint {
    
    private final String constraint_Name ;
    private double lowerBound ;
    
    private List <Triplet> coefficientList  =   new   ArrayList <Triplet>  ();    
                
    private double maxLHS = ZERO;
     
    public LowerBoundConstraint (double lowerBound , String name) {
        this.lowerBound = lowerBound;
        constraint_Name = name;
    }
    
    public String toString (){
        
        String result= "\n-----------------------\n LBC " + constraint_Name + ": ";
        for (Triplet triplet : this.coefficientList) {
            result += triplet.constraintCoefficient + " "+ triplet.varName + (triplet.isFractional ?  "(f)": "" ) +   " + ";
        }
        result+= " : "+ this.lowerBound;
        result += " MAX_LHS "+ this.maxLHS;
        return result ;
    }
    
    
    public void add (Triplet triplet ) {       
               
        boolean cond1= (triplet.objectiveCoeffcient > ZERO && triplet.constraintCoefficient > ZERO);
        boolean cond2= (triplet.objectiveCoeffcient < ZERO && triplet.constraintCoefficient < ZERO);
        
        if (cond1 || cond2   ) {
            triplet.significance = SignificanceEnum.PRIMARY;
        }  else if (triplet.objectiveCoeffcient == DOUBLE_ZERO){
            triplet.significance = SignificanceEnum.NEUTRAL;
        } else triplet.significance = SignificanceEnum.SECONDARY;
        
        this.coefficientList .add (triplet) ;  
        
        if (triplet.constraintCoefficient > ZERO) this.maxLHS += triplet.constraintCoefficient;
         
    }
    
   
    
    
    
    public int getVariableCount () {
        return coefficientList.size();
    }
    
    // copy this constraint into another
    //
    // used by every node in the cplex search tree to get its own copy of every constraint
    //    
    public LowerBoundConstraint getCopy ( ) {
        LowerBoundConstraint twin = new LowerBoundConstraint ( this.lowerBound,this.constraint_Name);
        
        twin.coefficientList .addAll(this.coefficientList);
        twin.maxLHS = this.maxLHS;
       
        return twin;
    }
    
    public Attributes getAttributes (  ) {   
        
        Attributes attr = new Attributes ();
        
        double remainingPrimarySlack =   this.maxLHS - this.lowerBound;
        double remainingSecondarySlack =   this.maxLHS - this.lowerBound;
        double remainingNeutralSlack =   this.maxLHS - this.lowerBound;
        
        int numPrimaryVariablesExamined     = ZERO; 
        int numSecondaryVariablesExamined     = ZERO; 
        int numNeutralVariablesExamined     = ZERO; 
         
        
        for (int index = ZERO; index <coefficientList.size(); index ++ ){   
            
            Triplet triplet = this.coefficientList.get(index);    
            double thisCoeff = triplet.constraintCoefficient;
            double thisCoeffMagnitude =  Math.abs (  thisCoeff );
            double thisObjMagn = Math.abs (   triplet.objectiveCoeffcient );
                           
                                                 
            if (triplet.significance.equals(PRIMARY)  ){
                
                if (triplet.isFractional)    {
                    attr.fractionalPrimaryVariables .add( triplet.varName  );
                }  
                
                numPrimaryVariablesExamined ++;
                remainingPrimarySlack -= thisCoeffMagnitude;
                if (BILLION == attr.primaryDimension){
                    if (remainingPrimarySlack == DOUBLE_ZERO){
                        attr.primaryDimension=numPrimaryVariablesExamined;
                    }else if (remainingPrimarySlack  < DOUBLE_ZERO){
                        attr.primaryDimension=numPrimaryVariablesExamined- ONE;
                    }
                }
                 
            } else if (triplet.significance.equals( SECONDARY)) {                
                //secondary var    
                
                attr.all_SecondaryVariables.add( triplet.varName);
                
            }   else {
                //neutral var
                if (triplet.isFractional){
                    if (thisCoeff> ZERO) {
                        attr.fractional_PositiveNeutralVariables .add( triplet.varName  );
                    } else {
                        attr.fractional_NegativeNeutralVariables   .add( triplet.varName  ); 
                    }
                }  
                
                numNeutralVariablesExamined ++;
                remainingNeutralSlack -= thisCoeffMagnitude;
                if (BILLION == attr.neutralDimension){
                    if (remainingNeutralSlack == DOUBLE_ZERO){
                        attr.neutralDimension=numNeutralVariablesExamined;
                    }else if (remainingNeutralSlack  < DOUBLE_ZERO){
                        attr.neutralDimension=numNeutralVariablesExamined- ONE;
                    }
                } 
                        
            }
        }
                
        return   attr  ;          
    }
     
    public void sort () {
       
        //for pessimistic dimensioning, sort in natural order 
       
        Collections.sort(this.coefficientList);
        if (USE_OPTIMISTIC_DIMENSIONING) Collections.reverse(coefficientList);
        
    }
      
    public LowerBoundConstraint applyKnownFixings (TreeMap<String, Boolean> fixings, TreeSet<String> fractionalVariables) {
        this.applyFixings(fixings,fractionalVariables);      
        return this.coefficientList.size () < TWO ?  null: this;
    }
    
    private void applyFixings  (TreeMap<String, Boolean> fixings, TreeSet<String> fractionalVariables ) {
        
        List <Triplet> updated_coefficientList  =   new   ArrayList <Triplet>  ();   
        
        //walk thru coeff list
        for (Triplet triplet:  coefficientList){
            
            Boolean fixedValue = fixings.get( triplet.varName);
            
            if (fractionalVariables.contains(triplet.varName)){
                triplet.isFractional = true;
            } 
            
            if (null!=fixedValue){
                if (  fixedValue){
                    //1 fixed
                    this.lowerBound -= triplet.constraintCoefficient;
                    if (triplet.constraintCoefficient> ZERO) this.maxLHS -=  triplet.constraintCoefficient;
                }else {
                    // 0 fixed
                    if (triplet.constraintCoefficient> ZERO) this.maxLHS -=  triplet.constraintCoefficient;
                }
                
            }else {
                updated_coefficientList.add (triplet );
            }
        }
        
               
        this.coefficientList= updated_coefficientList;
    }
    
}
