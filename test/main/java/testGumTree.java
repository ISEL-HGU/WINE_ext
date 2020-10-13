import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.handong.csee.isel.fcminer.fpcollector.gumtree.CodeComparator;
import edu.handong.csee.isel.fcminer.fpcollector.gumtree.Info;
import edu.handong.csee.isel.fcminer.fpcollector.gumtree.PatternWriter;

public class testGumTree {
	@Test
	public void gumTreeCompareTester() {
		ArrayList<Info> infos = new ArrayList<>();
		String mockClass0 = "public class MockClass0{" +
				"/** \r\n" + 
				" * Logs a trace message for newly inserted entries to the stats cache.\r\n" + 
				" */\r\n" + 
				"void traceStatsUpdate(GuidePostsKey key,GuidePostsInfo info){\r\n" + 
				"  if (LOGGER.isTraceEnabled()) {\r\n" + 
				"    LOGGER.trace(\"Updating local TableStats cache (id={}) for {}, size={}bytes\",new Object[]{Objects.hashCode(this),key,info.getEstimatedSize()});\r\n" + 
				"  }\r\n" + 
				"}\r\n" + 
				"}"; 
		int mockStart0 = mockClass0.indexOf("LOGGER.trace(\"Updating local TableStats cache (id={}) for {}, size={}bytes\",new Object[]{Objects.hashCode(this),key,info.getEstimatedSize()});");
		System.out.println(mockStart0);
		int mockEnd0 = mockStart0 + "LOGGER.trace(\"Updating local TableStats cache (id={}) for {}, size={}bytes\",new Object[]{Objects.hashCode(this),key,info.getEstimatedSize()});".length();
		System.out.println(mockEnd0);
		
		String mockClass1 = "public class MockClass1{" +
				"private boolean printOption(Logger console,ParameterDescription param){\r\n" + 
				"  boolean required=param.getParameter().required();\r\n" + 
				"  if (!param.getParameter().hidden()) {\r\n" + 
				"    console.info(\"  {} {}\\n\\t{}{}\",new Object[]{required ? \"*\" : \" \",param.getNames().trim(),param.getDescription(),formatDefault(param)});\r\n" + 
				"  }\r\n" + 
				"  return required;\r\n" + 
				"}\r\n" + 
				"}";
		int mockStart1 = mockClass1.indexOf("console.info(\"  {} {}\\n\\t{}{}\",new Object[]{required ? \"*\" : \" \",param.getNames().trim(),param.getDescription(),formatDefault(param)});\r\n");
		System.out.println(mockStart1);
		int mockEnd1 = mockStart1 + "console.info(\"  {} {}\\n\\t{}{}\",new Object[]{required ? \"*\" : \" \",param.getNames().trim(),param.getDescription(),formatDefault(param)});\r\n".length();
		System.out.println(mockEnd1);
		
		String mockClass2 = "public class MockClass2{" +
				"/** \r\n" + 
				" * {@inheritDoc}\r\n" + 
				" */\r\n" + 
				"@Override public void register(boolean accept,boolean connect,boolean read,boolean write,SelectorListener listener,SelectableChannel channel,RegistrationCallback callback){\r\n" + 
				"  if (IS_DEBUG) {\r\n" + 
				"    LOG.debug(\"registering : {} for accept : {}, connect: {}, read : {}, write : {}, channel : {}\",new Object[]{listener,accept,connect,read,write,channel});\r\n" + 
				"  }\r\n" + 
				"  int ops=0;\r\n" + 
				"  if (accept) {\r\n" + 
				"    ops|=SelectionKey.OP_ACCEPT;\r\n" + 
				"  }\r\n" + 
				"  if (connect) {\r\n" + 
				"    ops|=SelectionKey.OP_CONNECT;\r\n" + 
				"  }\r\n" + 
				"  if (read) {\r\n" + 
				"    ops|=SelectionKey.OP_READ;\r\n" + 
				"  }\r\n" + 
				"  if (write) {\r\n" + 
				"    ops|=SelectionKey.OP_WRITE;\r\n" + 
				"  }\r\n" + 
				"  registrationQueue.add(new Registration(ops,channel,listener,callback));\r\n" + 
				"  wakeup();\r\n" + 
				"}\r\n" + 
				"}";
		int mockStart2 = mockClass2.indexOf("    LOG.debug(\"registering : {} for accept : {}, connect: {}, read : {}, write : {}, channel : {}\",new Object[]{listener,accept,connect,read,write,channel});\r\n");
		System.out.println(mockStart2);
		int mockEnd2 = mockStart2 + "    LOG.debug(\"registering : {} for accept : {}, connect: {}, read : {}, write : {}, channel : {}\",new Object[]{listener,accept,connect,read,write,channel});\r\n".length();
		System.out.println(mockEnd2);
		
		String mockClass3 = "public class MockClass3{" +
				"@Override public void initialize() throws InvalidDatastoreException {\r\n" + 
				"  Configuration conf=new Configuration();\r\n" + 
				"  SequenceFileModelReader.loadModel(this,params,conf);\r\n" + 
				"  for (  String label : getKeys(\"\")) {\r\n" + 
				"    log.info(\"{} {} {} {}\",new Object[]{label,thetaNormalizerPerLabel.get(getLabelID(label)),thetaNormalizer,thetaNormalizerPerLabel.get(getLabelID(label)) / thetaNormalizer});\r\n" + 
				"  }\r\n" + 
				"}\r\n" + 
				"}";
		int mockStart3 = mockClass3.indexOf("    log.info(\"{} {} {} {}\",new Object[]{label,thetaNormalizerPerLabel.get(getLabelID(label)),thetaNormalizer,thetaNormalizerPerLabel.get(getLabelID(label)) / thetaNormalizer});\r\n");
		System.out.println(mockStart3);
		int mockEnd3 = mockStart3 + "    log.info(\"{} {} {} {}\",new Object[]{label,thetaNormalizerPerLabel.get(getLabelID(label)),thetaNormalizer,thetaNormalizerPerLabel.get(getLabelID(label)) / thetaNormalizer});\r\n".length();
		System.out.println(mockEnd3);
		
		String mockClass4 = "public class MockClass4{" +
				"@Override int runCmd(CommandLine cmdLine) throws Exception {\r\n" + 
				"  ZooKeeper zk=null;\r\n" + 
				"  try {\r\n" + 
				"    zk=ZooKeeperClient.newBuilder().connectString(bkConf.getZkServers()).sessionTimeoutMs(bkConf.getZkTimeout()).build();\r\n" + 
				"    BookieSocketAddress bookieId=AuditorElector.getCurrentAuditor(bkConf,zk);\r\n" + 
				"    if (bookieId == null) {\r\n" + 
				"      LOG.info(\"No auditor elected\");\r\n" + 
				"      return -1;\r\n" + 
				"    }\r\n" + 
				"    LOG.info(\"Auditor: {}/{}:{}\",new Object[]{bookieId.getSocketAddress().getAddress().getCanonicalHostName(),bookieId.getSocketAddress().getAddress().getHostAddress(),bookieId.getSocketAddress().getPort()});\r\n" + 
				"  }\r\n" + 
				"  finally {\r\n" + 
				"    if (zk != null) {\r\n" + 
				"      zk.close();\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"  return 0;\r\n" + 
				"}\r\n" + 
				"}";
		int mockStart4 = mockClass4.indexOf("    LOG.info(\"Auditor: {}/{}:{}\",new Object[]{bookieId.getSocketAddress().getAddress().getCanonicalHostName(),bookieId.getSocketAddress().getAddress().getHostAddress(),bookieId.getSocketAddress().getPort()});\r\n");
		int mockEnd4 = mockStart4 + "    LOG.info(\"Auditor: {}/{}:{}\",new Object[]{bookieId.getSocketAddress().getAddress().getCanonicalHostName(),bookieId.getSocketAddress().getAddress().getHostAddress(),bookieId.getSocketAddress().getPort()});\r\n".length();
		System.out.println(mockStart4);
		System.out.println(mockEnd4);
		
		String mockClass5 = "public class MockClass5{" + 
				"/** \r\n" + 
				" * This will produce a List of Hashtables, each hashtable contains the initialization info for a particular resource loader. This Hashtable will be passed in when initializing the the template loader.\r\n" + 
				" */\r\n" + 
				"private void assembleResourceLoaderInitializers(){\r\n" + 
				"  Vector resourceLoaderNames=rsvc.getConfiguration().getVector(RuntimeConstants.RESOURCE_LOADER);\r\n" + 
				"  StringUtils.trimStrings(resourceLoaderNames);\r\n" + 
				"  for (Iterator it=resourceLoaderNames.iterator(); it.hasNext(); ) {\r\n" + 
				"    String loaderName=(String)it.next();\r\n" + 
				"    StringBuffer loaderID=new StringBuffer(loaderName);\r\n" + 
				"    loaderID.append(\".\").append(RuntimeConstants.RESOURCE_LOADER);\r\n" + 
				"    ExtendedProperties loaderConfiguration=rsvc.getConfiguration().subset(loaderID.toString());\r\n" + 
				"    if (loaderConfiguration == null) {\r\n" + 
				"      log.debug(\"ResourceManager : No configuration information found \" + \"for resource loader named '%s' (id is %s). Skipping it...\",loaderName,loaderID);\r\n" + 
				"      continue;\r\n" + 
				"    }\r\n" + 
				"    loaderConfiguration.setProperty(RESOURCE_LOADER_IDENTIFIER,loaderName);\r\n" + 
				"    sourceInitializerList.add(loaderConfiguration);\r\n" + 
				"  }\r\n" + 
				"}\r\n" + 
				"}";
		int mockStart5 = mockClass5.indexOf("      log.debug(\"ResourceManager : No configuration information found \" + \"for resource loader named '%s' (id is %s). Skipping it...\",loaderName,loaderID);\r\n");
		int mockEnd5 = mockStart5 + "      log.debug(\"ResourceManager : No configuration information found \" + \"for resource loader named '%s' (id is %s). Skipping it...\",loaderName,loaderID);\r\n".length();
		System.out.println(mockStart5);
		System.out.println(mockEnd5);
		
		String mockClass6 = "public class MockClass6 {" +
				"@Override public void close(){\r\n" + 
				"  try {\r\n" + 
				"    client.close();\r\n" + 
				"  }\r\n" + 
				" catch (  final Exception e) {\r\n" + 
				"    logger.warn(\"Failure while closing out %s.\",getClass().getSimpleName(),e);\r\n" + 
				"  }\r\n" + 
				"}\r\n" + 
				"}";
		int mockStart6 = mockClass6.indexOf("    logger.warn(\"Failure while closing out %s.\",getClass().getSimpleName(),e);\r\n");
		int mockEnd6 = mockStart6 + "    logger.warn(\"Failure while closing out %s.\",getClass().getSimpleName(),e);\r\n".length();
		System.out.println(mockStart6);
		System.out.println(mockEnd6);
		
		String mockClass7 =  "public class MockClass7{" +
				"/** \r\n" + 
				" * Appends the DDL-Script for creating the given database to an SQL-Script<br> This includes the generation of all tables, views and relations.\r\n" + 
				" * @param db the database to create\r\n" + 
				" * @param script the sql script to which to append the dll command(s)\r\n" + 
				" */\r\n" + 
				"protected void createDatabase(DBDatabase db,DBSQLScript script){\r\n" + 
				"  for (  DBTable dbTable : db.getTables()) {\r\n" + 
				"    createTable(dbTable,script);\r\n" + 
				"  }\r\n" + 
				"  for (  DBRelation dbRelation : db.getRelations()) {\r\n" + 
				"    createRelation(dbRelation,script);\r\n" + 
				"  }\r\n" + 
				"  for (  DBView v : db.getViews()) {\r\n" + 
				"    try {\r\n" + 
				"      createView(v,script);\r\n" + 
				"    }\r\n" + 
				" catch (    NotSupportedException e) {\r\n" + 
				"      log.warn(\"Error creating the view {0}. This view will be ignored.\",v.getName());\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}\r\n" + 
				"}";
		int mockStart7 = mockClass7.indexOf("      log.warn(\"Error creating the view {0}. This view will be ignored.\",v.getName());\r\n");
		int mockEnd7 = mockStart7 + "      log.warn(\"Error creating the view {0}. This view will be ignored.\",v.getName());\r\n".length();
		System.out.println(mockStart7);
		System.out.println(mockEnd7);
		
		String mockClass8 = "public class MockClass8 {" + 
				"/** \r\n" + 
				" * Replace markers #{contextFieldName} by values from  {@link InterpreterContext} fieldswith same name and marker #{user}. If value == null then replace by empty string.\r\n" + 
				" */\r\n" + 
				"private void replaceContextParameters(Properties properties){\r\n" + 
				"  InterpreterContext interpreterContext=InterpreterContext.get();\r\n" + 
				"  if (interpreterContext != null) {\r\n" + 
				"    String markerTemplate=\"#\\\\{%s\\\\}\";\r\n" + 
				"    List<String> skipFields=Arrays.asList(\"paragraphTitle\",\"paragraphId\",\"paragraphText\");\r\n" + 
				"    List typesToProcess=Arrays.asList(String.class,Double.class,Float.class,Short.class,Byte.class,Character.class,Boolean.class,Integer.class,Long.class);\r\n" + 
				"    for (    String key : properties.stringPropertyNames()) {\r\n" + 
				"      String p=properties.getProperty(key);\r\n" + 
				"      if (StringUtils.isNotEmpty(p)) {\r\n" + 
				"        for (        Field field : InterpreterContext.class.getDeclaredFields()) {\r\n" + 
				"          Class clazz=field.getType();\r\n" + 
				"          if (!skipFields.contains(field.getName()) && (typesToProcess.contains(clazz) || clazz.isPrimitive())) {\r\n" + 
				"            Object value=null;\r\n" + 
				"            try {\r\n" + 
				"              value=FieldUtils.readField(field,interpreterContext,true);\r\n" + 
				"            }\r\n" + 
				" catch (            Exception e) {\r\n" + 
				"              logger.error(\"Cannot read value of field {0}\",field.getName());\r\n" + 
				"            }\r\n" + 
				"            p=p.replaceAll(String.format(markerTemplate,field.getName()),value != null ? value.toString() : StringUtils.EMPTY);\r\n" + 
				"          }\r\n" + 
				"        }\r\n" + 
				"        p=p.replaceAll(String.format(markerTemplate,\"user\"),StringUtils.defaultString(userName,StringUtils.EMPTY));\r\n" + 
				"        properties.setProperty(key,p);\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}\r\n" + 
				"}";
		int mockStart8 = mockClass8.indexOf("              logger.error(\"Cannot read value of field {0}\",field.getName());\r\n");
		int mockEnd8 = mockStart8 + "              logger.error(\"Cannot read value of field {0}\",field.getName());\r\n".length();
		System.out.println(mockStart8);
		System.out.println(mockEnd8);
		
//		String mockClass9 = "";
//		int mockStart9 = mockClass9.indexOf("");
//		int mockEnd9 = mockStart9 + 0;
//		System.out.println(mockStart9);
//		System.out.println(mockEnd9);
//		
//		String mockClass10 = "";
//		int mockStart10 = mockClass10.indexOf("");
//		int mockEnd10 = mockStart10 + 0;
//		System.out.println(mockStart10);
//		System.out.println(mockEnd10);
//		
		Info info0 = new Info();
		info0.setMockClass(mockClass0);
		info0.setMockStart(mockStart0);
		info0.setMockEnd(mockEnd0);
		info0.path = "0";
//		Info info1 = new Info();
//		info1.setMockClass(mockClass1);
//		info1.setMockStart(mockStart1);
//		info1.setMockEnd(mockEnd1);
//		info1.path = "1";
//		Info info2 = new Info();
//		info2.setMockClass(mockClass2);
//		info2.setMockStart(mockStart2);
//		info2.setMockEnd(mockEnd2);
//		info2.path = "2";
//		Info info3 = new Info();
//		info3.setMockClass(mockClass3);
//		info3.setMockStart(mockStart3);
//		info3.setMockEnd(mockEnd3);
//		info3.path = "3";
//		Info info4 = new Info();
//		info4.setMockClass(mockClass4);
//		info4.setMockStart(mockStart4);
//		info4.setMockEnd(mockEnd4);
//		info4.path = "4";
		
		Info info5 = new Info();
		info5.setMockClass(mockClass5);
		info5.setMockStart(mockStart5);
		info5.setMockEnd(mockEnd5);
		info5.path = "5";
		Info info6 = new Info();
		info6.setMockClass(mockClass6);
		info6.setMockStart(mockStart6);
		info6.setMockEnd(mockEnd6);
		info6.path = "6";
		Info info7 = new Info();
		info7.setMockClass(mockClass7);
		info7.setMockStart(mockStart7);
		info7.setMockEnd(mockEnd7);
		info7.path = "7";
		Info info8 = new Info();
		info8.setMockClass(mockClass8);
		info8.setMockStart(mockStart8);
		info8.setMockEnd(mockEnd8);
		info8.path = "8";
		
		infos.add(info0);
//		infos.add(info1);
//		infos.add(info2);
//		infos.add(info3);
//		infos.add(info4);
		
		infos.add(info5);
		infos.add(info6);
		infos.add(info7);
		infos.add(info8);
		
		PatternWriter patternWriter = new PatternWriter();
		CodeComparator gumTreeComp = new CodeComparator();
		for(int i = 0 ; i < infos.size(); i ++) {
			gumTreeComp.compare(infos.get(i));
			for(Info info : infos) {
				gumTreeComp.compare(info);  
			}
			patternWriter.writePatterns(gumTreeComp.getPatterns());
			gumTreeComp.clear();
		}
	}
}
