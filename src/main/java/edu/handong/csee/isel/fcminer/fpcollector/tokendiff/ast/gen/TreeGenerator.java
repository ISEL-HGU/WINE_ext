package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.atteo.classindex.IndexSubclasses;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.ProcessedData;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.RawData;

@IndexSubclasses
public abstract class TreeGenerator {

    protected abstract ProcessedData generate(Reader r, RawData rawData) throws IOException;
    
    public ProcessedData generateFromReader(Reader r, RawData rawData) throws IOException {
        ProcessedData pData = generate(r, rawData);
        
      //the case when the violating line is not in a method but in static block or something.
        if(pData == null || pData.getCtx().getRoot() == null) {
        	return pData;
        }
        
        pData.getCtx().validate();
        return pData;
    }
    
    public ProcessedData generateFromInfo(RawData rawData, String src) throws IOException {
        return generateFromReader(new StringReader(src), rawData);
    }
}
