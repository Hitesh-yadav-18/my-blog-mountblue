package com.company.my.blog.service;

public class CustomMethodsService {
    
    public boolean isValidPageNumbers(int start, int limit) {
        if((start<0 || limit<0) || (start>0 && limit<=0) || (start<0 && limit>0)) {
            return false;
        }
        else if(start>=0 && limit>0) {
            return true;
        }     
        
        return false;
    }
}
