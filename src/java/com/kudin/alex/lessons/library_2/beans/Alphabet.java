
package com.kudin.alex.lessons.library_2.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author ALEKSANDR KUDIN
 * @since May 7, 2018
 */

@ManagedBean
@SessionScoped
public class Alphabet implements Serializable{

    private final List<Character> rusLetters;
    private final List<Character> engLetters;
    
    public Alphabet(){
        rusLetters = Arrays.asList('А','Б','В','Г','Д','Е','Ж','З','И','К','Л','М','Н','О','П','Р','С','Т','У','Ф','Х','Ц','Ч','Ш','Щ','Э','Ю','Я');
        
        engLetters = Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
    }
    
    public List<Character> getRusLetters(){
        return rusLetters;
    }
    
    public List<Character> getEngLetters(){
        return engLetters;
    }
}
