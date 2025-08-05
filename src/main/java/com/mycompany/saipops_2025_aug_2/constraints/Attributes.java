/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops_2025_aug_2.constraints;
    
import static com.mycompany.saipops_2025_aug_2.Constants.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author sst119
 */
public class Attributes {
    
    public int primaryDimension = BILLION; 
    public TreeSet<String   >  fractionalPrimaryVariables    = new TreeSet<String   > ();
    
    public int secondaryDimension = BILLION; 
    public TreeSet<String   >  fractionalSecondaryVariables    = new TreeSet<String   > ();
      
    public int neutralDimension = BILLION;    
    public TreeSet<String>  fractional_PositiveNeutralVariables = new TreeSet<String>();
    public TreeSet<String>  fractional_NegativeNeutralVariables = new TreeSet<String>();
 
    public boolean hasFractionalVariables () {
        return fractionalPrimaryVariables.size() +  fractionalSecondaryVariables.size()  + 
                fractional_PositiveNeutralVariables .size() +
                fractional_NegativeNeutralVariables.size() > ZERO;
    } 
    
    public boolean hasFractionalPrimaryVariables () {
        return fractionalPrimaryVariables.size()   > ZERO;
    }
    
    public boolean hasFractionalSecondaryVariables () {
        return this.fractionalSecondaryVariables.size()   > ZERO;
    }
   
    public boolean hasFractionalNeutralVariables () {
        return fractional_PositiveNeutralVariables.size() +   fractional_NegativeNeutralVariables.size()> ZERO;
    }
    
}
