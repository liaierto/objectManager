package com.liaier.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class TableXmlAnalytic {
	private static Log log = LogFactory.getLog(TableXmlAnalytic.class);
    public static Document parseDocument(String pXmlPath){
        SAXReader pReader = new SAXReader();
        Document doc = null;
        try {
            doc = pReader.read(new File(pXmlPath));
        } catch (DocumentException e) {
            
            log.error(e);
        }
        return doc;
    }
    
    public static List<StringBuffer> ParseXmlData(String pFileName){
    	List<StringBuffer> sqls = new ArrayList();
        Document doc = parseDocument(pFileName+".xml");
        Element root = doc.getRootElement();
        List<Element> pElementList = root.elements();
        for(Element pElement:pElementList){
        	StringBuffer sql = new StringBuffer();
        	sql.append("create table ");
        	sql.append(pElement.attributeValue("name")).append(" (");
        	List<Element> childementList = pElement.elements();
        	String key = "";
        	for(Element childelement:childementList){
        		String field = childelement.attributeValue("name");
        		sql.append(field).append(" ");
        		sql.append(childelement.attributeValue("type")).append("(");
        		sql.append(childelement.attributeValue("len")).append(") ");
        		String isKey = childelement.attributeValue("iskey");
        		if("yes".equals(isKey)){
        			sql.append(childelement.attributeValue("aut"));
        			key = field;
        		}
        		sql.append(",");
        	}
        	sql.append(" primary key("+key+"))");
        	sqls.add(sql);
        }
        
        return sqls;
    }
    
//    public static void main (String args[]){
//        Map pMap = testParseXmlData("WebRoot/AppHome/TABLEINFOTEST.xml");
//        
//        for (Iterator itr = pMap.keySet().iterator();itr.hasNext();){
//            String name = (String) itr.next();
//            String value= (String) pMap.get(name);
//            System.out.println(name+" is value "+value);
//        }
//    }
}
