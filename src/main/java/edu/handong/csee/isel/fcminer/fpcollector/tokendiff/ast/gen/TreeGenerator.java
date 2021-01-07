package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.atteo.classindex.IndexSubclasses;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.Info;

@IndexSubclasses
public abstract class TreeGenerator {

    protected abstract Info generate(Reader r, Info info) throws IOException;
    
    public Info generateFromReader(Reader r, Info info) throws IOException {
        Info updatedInfo = generate(r, info);
        
      //the case when the violating line is not in a method but in static block or something.
        if(updatedInfo.getCtx().getRoot() == null) {
        	return updatedInfo;
        }
        
        updatedInfo.getCtx().validate();
        return updatedInfo;
    }
    
    public Info generateFromInfo(Info info) throws IOException {    	
        return generateFromReader(new StringReader(info.getSrc()), info);
    }
}
