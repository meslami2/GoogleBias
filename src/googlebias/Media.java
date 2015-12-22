/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googlebias;

/**
 *
 * @author Motahhare
 */
public class Media {
    String domain;
    String name;
    String type;
    double score;
    
    public Media (String domain, String name, String type, double score){
        this.domain = domain;
        this.name = name;
        this.type = type;
        this.score = score;
                
    }
    
    @Override
    public String toString(){
        return domain + "_" + name + "_" + type + "_" + score;
    }
}
