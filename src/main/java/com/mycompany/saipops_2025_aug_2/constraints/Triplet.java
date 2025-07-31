/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops_2025_aug_2.constraints;
        
import static com.mycompany.saipops_2025_aug_2.Constants.*;
import com.mycompany.saipops_2025_aug_2.SignificanceEnum;




 

 



/**
 *
 * @author sst119
 */
public class Triplet implements Comparable<Triplet >  {   
     
    public String varName;
    public double objectiveCoeffcient;
    public double constraintCoefficient;    
    
    //two more properties that are filled in later
    public boolean isFractional = false;
    public SignificanceEnum significance = null;
            
    public Triplet (String varName,Double constraintCoefficient, Double objectiveCoeffcient ) {
        this.varName = varName;
        this.constraintCoefficient =constraintCoefficient;
        this.objectiveCoeffcient =objectiveCoeffcient;
        
    }    
    
    
    
    public int compareTo(Triplet another) {    
        int result = ZERO;
        double val =  Math.abs (another.constraintCoefficient) -  Math.abs(this.constraintCoefficient) ;
        if (val > ZERO) {
            result = -ONE;
        } else if (val < ZERO){
            result = ONE;
        }  
         
        return result;
    }
    
}
