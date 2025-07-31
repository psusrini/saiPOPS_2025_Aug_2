/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops_2025_aug_2.utils;

import static com.mycompany.saipops_2025_aug_2.Constants.DOUBLE_ZERO;
import static com.mycompany.saipops_2025_aug_2.Constants.ONE;
import static com.mycompany.saipops_2025_aug_2.Constants.ZERO;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author sst119
 */
public class MathUtils {
    
    public MathUtils (){
        
    }
    
    public static     double objSum (TreeSet<String> varSet,  TreeMap<String, Double>  objectiveFunctionMap){
        double sum = ZERO;
        for (String var: varSet){
            sum += Math.abs (objectiveFunctionMap.get( var));
        }
        return sum;
    }
    
    public static    TreeSet<String>  getMaxObjMagn (TreeSet<String> candidates ,  TreeMap<String, Double>  objectiveFunctionMap){
        TreeSet<String >  winners = new  TreeSet<String>();
        double bestKnownObjMagn = -ONE;
        
        for (String var : candidates ){
            Double objval =   objectiveFunctionMap.get( var);
            
            double thisObjMagn = Math.abs ( objval);
            if (thisObjMagn > bestKnownObjMagn){
                bestKnownObjMagn = thisObjMagn;
                winners.clear();
            }
            if (bestKnownObjMagn ==thisObjMagn){
                winners.add (var );
            }
        }
        
        return winners ;
    }
    
    public static   TreeSet<String> getMaxiMinFrequency(TreeSet<String>  candidates,  TreeMap<String, Double>  mapOne,  TreeMap<String, Double>  mapTwo ) {
        TreeSet<String >  winners = new  TreeSet<String>();
        
        double smallestKnownFreq = DOUBLE_ZERO; 
        double largestKnownFreq = DOUBLE_ZERO;
        
        //find var whose smaller freq is as large as possible
        
        
        for (String candidate:  candidates){
            Double pFreq = mapOne.get (candidate );
            if (null == pFreq) pFreq = DOUBLE_ZERO;
            Double nFreq = mapTwo.get (candidate );
            if (null == nFreq) nFreq = DOUBLE_ZERO;
            
            pFreq= Math.abs (pFreq );
            nFreq= Math.abs (nFreq);
            
            double smaller = Math.min (pFreq,  nFreq);
            double larger = Math.max (pFreq,  nFreq);
            
            if ( (smaller >smallestKnownFreq ) || (smaller == smallestKnownFreq && largestKnownFreq<larger  )) {
                winners.clear();
                smallestKnownFreq =smaller;
                largestKnownFreq= larger;
                winners.add (candidate );
            }  else if (smaller == smallestKnownFreq && largestKnownFreq==larger  ) {
                winners.add (candidate );
            }
              
        }
               
        return winners;
    }   
    
}
