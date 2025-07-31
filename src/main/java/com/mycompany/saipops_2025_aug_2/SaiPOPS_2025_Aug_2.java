/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.saipops_2025_aug_2;

import static com.mycompany.saipops_2025_aug_2.Constants.ONE;
import static com.mycompany.saipops_2025_aug_2.Parameters.printParameters;
import com.mycompany.saipops_2025_aug_2.utils.Solver;
import ilog.cplex.IloCplex;
import static java.lang.System.exit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author sst119
 */
public class SaiPOPS_2025_Aug_2 {

    private static final Logger logger ;
    private static  IloCplex cplex;
    
    static {
        logger= Logger.getLogger(SaiPOPS_2025_Aug_2.class.getSimpleName() );
        //logger.setLevel(Level.INFO);
        try {
            FileHandler fileHandler = new FileHandler(SaiPOPS_2025_Aug_2.class.getSimpleName()+ ".log");

            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);

            logger.info("Logging initialized.");
            

        } catch (Exception e) {
            System.err.println(e.getMessage()) ;
            exit(ONE);
        }
        
    }

    public static void main(String[] args) throws Exception {
        
        printParameters();
        
        Solver solver = new Solver ( ) ;
        
        solver.solve ( );
        logger.info("Test Completed Successfully!");
        
        //candidates are fractional primary vars in the lowest primary dimension
        //pick the ones that do not appear as secondary in secondary dim 1  -- for TED find dominating 0 triggers
        //next time break is objective magnitude
        //next tie break is frequency in lowest primary dimension
        //next tie break is random        
        
    }
}
