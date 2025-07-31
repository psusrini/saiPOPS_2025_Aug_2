/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops_2025_aug_2.heuristics;
    
import static com.mycompany.saipops_2025_aug_2.Constants.*;
import static com.mycompany.saipops_2025_aug_2.HeuristicEnum.*;
import static com.mycompany.saipops_2025_aug_2.Parameters.*; 
import com.mycompany.saipops_2025_aug_2.constraints.*; 
import static com.mycompany.saipops_2025_aug_2.utils.MathUtils.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author sst119
 * 
 * we want a large number of 0 fixes on both sides of the branch
 * 
 * 
 */
public   class SaiPOPS_Heuristic {
    //
    private  Set<Attributes> attributes;
    private  TreeMap<String, Double>  objectiveFunctionMap;
    
    private int lowestKnownNeutralDimension = BILLION;
    private  TreeMap<String, Double>  positiveNeutralVariableFrequencyMap   = new  TreeMap<String, Double>  ();
    private  TreeMap<String, Double>  negativeNeutralVariableFrequencyMap   = new  TreeMap<String, Double>  ();
               
    private int lowestKnownPrimaryDimension = BILLION;
    private  TreeMap<String , TreeSet<String>>  primaryVariablesAtLowestDim   = new  TreeMap<String , TreeSet<String>>   ();
    private  TreeMap<String, Double>  primaryVariable_FrequencyMap = new  TreeMap<String, Double>  ();
     
    
    
    public SaiPOPS_Heuristic (  Set<Attributes> attributes ,     
            TreeMap<String, Double>  objectiveFunctionMap  ){
         
        this. objectiveFunctionMap = objectiveFunctionMap;
        this.attributes = attributes;    
        
        // populate the maps  
        for (Attributes attr: attributes){
            
            if (attr.hasFractionalNeutralVariables()){
                //  neutral vars present
                if (lowestKnownNeutralDimension > attr.neutralDimension){
                    lowestKnownNeutralDimension = attr.neutralDimension;
                    positiveNeutralVariableFrequencyMap.clear();
                    negativeNeutralVariableFrequencyMap.clear();
                    for (String var : attr.fractional_PositiveNeutralVariables){
                        positiveNeutralVariableFrequencyMap.put(var, DOUBLE_ONE);
                    }
                    for (String var: attr.fractional_NegativeNeutralVariables){
                        negativeNeutralVariableFrequencyMap.put(var, DOUBLE_ONE);
                    }
                }else if (lowestKnownNeutralDimension == attr.neutralDimension){
                    
                    for (String var : attr.fractional_PositiveNeutralVariables){
                        Double current = positiveNeutralVariableFrequencyMap.get(var);
                        if (null==current) current= DOUBLE_ZERO;
                        positiveNeutralVariableFrequencyMap.put(var,current+ ONE);
                    }
                    for (String var: attr.fractional_NegativeNeutralVariables){
                        Double current = negativeNeutralVariableFrequencyMap.get(var);
                        if (null==current) current= DOUBLE_ZERO;
                        negativeNeutralVariableFrequencyMap.put(var, current +ONE);
                    }
                }
            } 
            
                        
            if (attr.hasFractionalPrimaryVariables()){
                if (lowestKnownPrimaryDimension > attr.primaryDimension){
                    lowestKnownPrimaryDimension = attr.primaryDimension;
                    primaryVariablesAtLowestDim.clear();
                    
                    for (String var: attr.fractionalPrimaryVariables){
                        TreeSet<String> current = new TreeSet<String> ();
                        current.addAll(attr.all_SecondaryVariables );
                        primaryVariablesAtLowestDim.put ( var, current ) ;     
                          
                    }     
                    
                    this.primaryVariable_FrequencyMap.clear();
                    for (String var: attr.fractionalPrimaryVariables){
                        primaryVariable_FrequencyMap.put (var, DOUBLE_ONE);
                    }
                    
                }else if (lowestKnownPrimaryDimension == attr.primaryDimension){
                    for (String var: attr.fractionalPrimaryVariables){
                        TreeSet<String> current = primaryVariablesAtLowestDim.get(var);
                        if (null == current)current = new TreeSet<String> ();
                        current.addAll(attr.all_SecondaryVariables );
                        primaryVariablesAtLowestDim.put ( var,  current ) ; 
                        
                        Double currentScore= primaryVariable_FrequencyMap .get ( var);
                        if (null==currentScore)currentScore=DOUBLE_ZERO;
                        primaryVariable_FrequencyMap .put ( var, currentScore + ONE);
                    }                                     
                }
                
                              
            }  
            
          
        }//for all attrs
  
    }//end heuristic method
    
    public String getBranchingVariable() {
        TreeSet<String>  candidates  ;
        
        if ( primaryVariablesAtLowestDim.isEmpty()){            
            //Only neutral vars (no primary). Use MOM_S on neutral vars         
            candidates = getMOMS ( ) ;
        } else  candidates =   getPOPS()  ;
        
        //random tiebreak        
        String[] candidateArray = candidates.toArray(new String[ZERO]);        
        return candidateArray[ PERF_VARIABILITY_RANDOM_GENERATOR.nextInt(candidates.size())];
    }
    
  

    private   TreeSet<String>  getMOMS (){
        TreeSet<String>  candidates = new TreeSet<String>();
        candidates.addAll( this.positiveNeutralVariableFrequencyMap.keySet());
        candidates.addAll( this.negativeNeutralVariableFrequencyMap.keySet());
        candidates =  getMaxiMinFrequency(candidates,positiveNeutralVariableFrequencyMap  , negativeNeutralVariableFrequencyMap );
        return  candidates;
    }
  
    private TreeSet<String> getPOPS() {
        
        //candidates are fractional primary vars at lowest dim
        TreeSet<String> candidates = new TreeSet<String> ();
        candidates.addAll( primaryVariablesAtLowestDim.keySet());
        
        //find apex primary vars  
        if (this.lowestKnownPrimaryDimension==ONE){
            TreeSet<String> apex =  getApex (candidates   );
            if (!apex.isEmpty()   ){
                candidates = apex;
            } 
        }
        
        candidates = getMaxObjMagn (candidates, this.objectiveFunctionMap) ;
         
        //return candidates with highest score
        return getMaxiMinFrequency (candidates, this.primaryVariable_FrequencyMap, new  TreeMap<String, Double>  ()) ;
        
    }
     
    private TreeSet<String> getApex (TreeSet<String> candidates   ){
        TreeSet<String> apex = new TreeSet<String> ();
        apex.addAll( candidates);

        for (TreeSet<String> secondarySet : this.primaryVariablesAtLowestDim.values())        {
            apex.removeAll( secondarySet);
        }
        
        return apex;
    }
    
}
