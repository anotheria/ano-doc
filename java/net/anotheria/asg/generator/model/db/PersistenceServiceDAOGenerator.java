package net.anotheria.asg.generator.model.db;

import java.util.ArrayList;
import java.util.List;

import net.anotheria.asg.generator.AbstractGenerator;
import net.anotheria.asg.generator.CommentGenerator;
import net.anotheria.asg.generator.Context;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedClass;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.IGenerateable;
import net.anotheria.asg.generator.IGenerator;
import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaListProperty;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.meta.MetaProperty;
import net.anotheria.asg.generator.meta.ModuleParameter;
import net.anotheria.asg.generator.model.DataFacadeGenerator;
import net.anotheria.util.ExecutionTimer;
import net.anotheria.util.StringUtils;

/**
 * This generator generates the DAO for a Document, the daoexceptions, and the rowmapper.
 * @author another
 *
 */
public class PersistenceServiceDAOGenerator extends AbstractGenerator implements IGenerator{
	
	public List<FileEntry> generate(IGenerateable gmodule){
		
		MetaModule mod = (MetaModule)gmodule;
		
		List<FileEntry> ret = new ArrayList<FileEntry>();
		
		ExecutionTimer timer = new ExecutionTimer(mod.getName()+"-DaoGen");
		
		List<MetaDocument> documents = mod.getDocuments();
		for (MetaDocument d: documents){
			
			timer.startExecution(d.getName());
			timer.startExecution(d.getName()+"Exc");
			ret.add(new FileEntry(generateException(d)));
			timer.stopExecution(d.getName()+"Exc");

			timer.startExecution(d.getName()+"NoItemE");
			ret.add(new FileEntry(generateNoItemException(d)));
			timer.stopExecution(d.getName()+"NoItemE");
			
			timer.startExecution(d.getName()+"DAO");
			ret.add(new FileEntry(generateDAO(d)));
			timer.stopExecution(d.getName()+"DAO");
			
			timer.startExecution(d.getName()+"RowMapper");
			ret.add(new FileEntry(generateRowMapper(d)));
			timer.stopExecution(d.getName()+"RowMapper");

			timer.stopExecution(d.getName());
		}
		
		//timer.printExecutionTimesOrderedByCreation();
		return ret;
	}
	
	private String getPackageName(MetaModule module){
		return GeneratorDataRegistry.getInstance().getContext().getPackageName(module)+".service.persistence";
	}
	
	private GeneratedClass generateException(MetaDocument doc){
		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);
		
	    clazz.setTypeComment(CommentGenerator.generateJavaTypeComment(getExceptionName(doc), this));
	    
	    clazz.setPackageName(getPackageName(doc.getParentModule()));
	    
	    clazz.addImport("net.anotheria.db.dao.DAOException");
	    
	    clazz.setName(getExceptionName(doc));
	    clazz.setParent("DAOException");
	    
	    startClassBody();
		appendString("public "+getExceptionName(doc)+"(String message){");
		appendIncreasedStatement("super(message)");
		appendString("}");
		
		emptyline();
		appendString("public "+getExceptionName(doc)+"(){");
		appendIncreasedStatement("super()");
		appendString("}");
		
		return clazz;

	}
	
	private GeneratedClass generateNoItemException(MetaDocument doc){
		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);
		
	    clazz.setTypeComment(CommentGenerator.generateJavaTypeComment(getNoItemExceptionName(doc), this));
	    clazz.setPackageName(getPackageName(doc.getParentModule()));
	    clazz.setName(getNoItemExceptionName(doc));
	    clazz.setParent(getExceptionName(doc));
	    
	    startClassBody();
		appendString("public "+getNoItemExceptionName(doc)+"(String id){");
		appendIncreasedStatement("super("+quote("No item found for id: ")+"+id)");
		appendString("}");
		
		appendString("public "+getNoItemExceptionName(doc)+"(long id){");
		appendIncreasedStatement("this("+quote("")+"+id)");
		appendString("}");

		return clazz; 
	}
	
	private String getAttributeConst(MetaProperty p){
		return "ATT_NAME_"+p.getName().toUpperCase();
	}

	private String getAttributeName(MetaProperty p){
		return p.getName().toLowerCase();
	}

	private GeneratedClass generateRowMapper(MetaDocument doc){
		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);
	    
		Context context = GeneratorDataRegistry.getInstance().getContext();
		
	    clazz.setTypeComment(CommentGenerator.generateJavaTypeComment(getRowMapperName(doc), this));
	    clazz.setPackageName(getPackageName(doc.getParentModule()));
	    
	    clazz.addImport("java.sql.ResultSet");
	    clazz.addImport("java.sql.SQLException");
	    clazz.addImport("net.anotheria.db.dao.RowMapper");
	    clazz.addImport("net.anotheria.db.dao.RowMapperException");
	    clazz.addImport(DataFacadeGenerator.getDocumentImport(context, doc));
	    clazz.addImport(VOGenerator.getDocumentImport(context, doc));
	    
	    clazz.setName(getRowMapperName(doc));
	    clazz.setParent("RowMapper<"+doc.getName()+">");
	    clazz.setGenerateLogger(true);

	    startClassBody();

		openFun("public "+doc.getName()+" map(ResultSet row) throws RowMapperException");

	    openTry();
	    
	    appendStatement("long id = row.getLong(1)");
	    appendStatement(doc.getName()+" ret = new "+VOGenerator.getDocumentImplName(doc)+"(\"\"+id)");
	    for (int i=0; i<doc.getProperties().size(); i++){
	    	generateProperty2DBMapping(doc.getProperties().get(i), i+2);
	    }
	    int ioffset = doc.getProperties().size();
	    
	    for (int i=0; i<doc.getLinks().size(); i++){
	    	generateProperty2DBMapping(doc.getLinks().get(i), i+ioffset+2);
	    }
	    
	    ioffset = doc.getProperties().size()+doc.getLinks().size();
	    generateProperty2DBMappingPrivate(doc, new MetaProperty(VOGenerator.DAO_CREATED, "long"), ioffset+2);
	    generateProperty2DBMappingPrivate(doc, new MetaProperty(VOGenerator.DAO_UPDATED, "long"), ioffset+3);
	    
	    appendStatement("return ret");
	    decreaseIdent();
	    appendString("}catch(SQLException e){");
	    appendIncreasedStatement("log.error(\"map\", e)");
	    appendIncreasedStatement("throw new RowMapperException(e)");
	    appendString("}");
	    
	    append(closeBlock());
	    
	    return clazz;
	}
	
	private void generateProperty2DBMapping(MetaProperty p, int position){
		if (p instanceof MetaListProperty)
			_generateArrayProperty2DBMapping((MetaListProperty)p, position);
		else {
			_generateProperty2DBMapping(p, position);
		}
	}

	private void _generateProperty2DBMapping(MetaProperty p, int position){
		String call = "";
		
		call += "ret.set";
		call += p.getAccesserName();
		call += "(";
		call += "row.";
		call += p.toPropertyGetter();
		call += "("+position+"))";
		
		appendStatement(call);
	}
	
	private void _generateArrayProperty2DBMapping(MetaListProperty p, int position){
		String call = "";
		
		call += "ret.set";
		call += p.getAccesserName();
		call += "(";
		call += "convertToList(";
		call += "(" + p.getContainedProperty().toJavaType() + "[])";
		call += "row.getArray";
		call += "("+position+")";
		call += ".getArray";
		call += "()))";
		
		appendStatement(call);
	}
	
	private void generateProperty2DBMappingPrivate(MetaDocument doc, MetaProperty p, int position){
		String call = "";
		
		call += "(("+VOGenerator.getDocumentImplName(doc)+")ret).set";
		call += p.getAccesserName();
		call += "(";
		call += "row.";
		call += p.toPropertyGetter();
		call += "("+position+"))";
		
		appendStatement(call);
	}

	private void generateDB2PropertyMapping(String variableName, MetaProperty p, int position){
		if (p instanceof MetaListProperty)
			_generateDB2ArrayPropertyMapping(variableName, (MetaListProperty)p, position);
		else {
			_generateDB2PropertyMapping(variableName, p, position);
		}
	}
	
	private void _generateDB2PropertyMapping(String variableName, MetaProperty p, int position){
		String call = "";
		
		call += "ps.";
		call += p.toPropertySetter();
		call += "("+position+", ";
		call += variableName+".";
		call += p.toGetter();
		call += "())";
		
		
		appendStatement(call);
	}
	
	private void _generateDB2ArrayPropertyMapping(String variableName, MetaListProperty p, int position){
		String call = "";
		
		call += "ps.setArray";
		call += "("+position+", ";
		call += "new " + p.getContainedProperty().toJavaObjectType() + "Array(";
		call += variableName+".";
		call += p.toGetter();
		call += "()))";
		
		appendStatement(call);
	}
	
	private String getDB2PropertyCallMapping(String variableName, MetaProperty p, String position){
		if (p instanceof MetaListProperty)
			return _getDB2ArrayPropertyCallMapping(variableName, (MetaListProperty)p, position);
		else {
			return _getDB2PropertyCallMapping(variableName, p, position);
		}
	}
	
	private String _getDB2PropertyCallMapping(String variableName, MetaProperty p, String position){
		String call = "";
		
		call += "ps.";
		call += p.toPropertySetter();
		call += "("+position+", ";
		call += "("+p.toJavaObjectType()+")" + variableName;
		call += ")";
		
		
		return call;
	}
	
	private String _getDB2ArrayPropertyCallMapping(String variableName, MetaListProperty p, String position){
		String call = "//Not implemented";
		
//		call += "ps.setArray";
//		call += "("+position+", ";
//		call += "new " + p.getContainedProperty().toJavaObjectType() + "Array(";
//		call += variableName+".";
//		call += p.toGetter();
//		call += "()))";
		
		return call;
	}

	private GeneratedClass generateDAO(MetaDocument doc){
		
		GeneratedClass clazz = new GeneratedClass();
		startNewJob(clazz);
		
	    List<MetaProperty> properties = new ArrayList<MetaProperty>();
	    properties.addAll(doc.getProperties());
	    properties.addAll(doc.getLinks());
		
	    clazz.setTypeComment(CommentGenerator.generateJavaTypeComment(getDAOName(doc), this));
	    clazz.setPackageName(getPackageName(doc.getParentModule()));

	    boolean moduleDbContextSensitive = doc.getParentModule().isParameterEqual(ModuleParameter.MODULE_DB_CONTEXT_SENSITIVE, "true");
	    
	    clazz.addImport("java.util.List");
	    clazz.addImport("java.util.ArrayList");
	    clazz.addImport("java.util.concurrent.atomic.AtomicLong");	    
	    if (moduleDbContextSensitive){
	    	clazz.addImport("java.util.Map");
	    	clazz.addImport("java.util.HashMap");
	    }
	    
	    clazz.addImport(DataFacadeGenerator.getDocumentImport(GeneratorDataRegistry.getInstance().getContext(), doc));
	    clazz.addImport(VOGenerator.getDocumentImport(GeneratorDataRegistry.getInstance().getContext(), doc));
	    clazz.addImport("net.anotheria.db.dao.DAO");
	    clazz.addImport("net.anotheria.db.dao.DAOException");
	    clazz.addImport("net.anotheria.db.dao.DAOSQLException");
	    clazz.addImport("net.anotheria.db.dao.RowMapper");
	    clazz.addImport("net.anotheria.anodoc.query2.QueryProperty");
	    clazz.addImport("net.anotheria.anodoc.util.context.DBContext");
	    clazz.addImport("net.anotheria.anodoc.util.context.ContextManager");

	    clazz.addImport("java.sql.Connection");
	    clazz.addImport("java.sql.PreparedStatement");
	    clazz.addImport("java.sql.ResultSet");
	    clazz.addImport("java.sql.SQLException");
	    clazz.addImport("java.sql.Statement");
	    clazz.addImport("org.apache.log4j.Logger");
	    
	    for (MetaProperty p : properties){
	    	if(p instanceof MetaListProperty)
	    		clazz.addImport("net.anotheria.db.array." + ((MetaListProperty)p).getContainedProperty().toJavaObjectType() + "Array");
	    }
	    
	    clazz.setName(getDAOName(doc));
	    clazz.addInterface("DAO");

	    startClassBody();
		appendStatement("private static Logger log = Logger.getLogger("+getDAOName(doc)+".class)");
	    
	    //first define constants.
	    String constDecl = "public static final String ";
	    appendStatement(constDecl+"TABNAME = "+quote(getSQLTableName(doc)));
	    emptyline();
	    MetaProperty id = new MetaProperty("id", "string");
	    MetaProperty dao_created = new MetaProperty("dao_created", "long");
	    MetaProperty dao_updated = new MetaProperty("dao_updated", "long");

	    appendStatement(constDecl+getAttributeConst(id)+" = "+quote(getAttributeName(id)));
	    for (MetaProperty p : properties){
		    appendStatement(constDecl+getAttributeConst(p)+" \t = "+quote(getAttributeName(p)));
	    }
	    
	    emptyline();
	    //create sql staments
	    //SQL-CREATE
	    String sqlCreate1 = quote("INSERT INTO ");
	    StringBuilder sqlCreate2 = new StringBuilder(quote(" ("));
	    sqlCreate2.append("+"+getAttributeConst(id));
	    for (MetaProperty p : properties){
	    	sqlCreate2.append("+"+quote(", ")+"+"+getAttributeConst(p));
	    }
	    sqlCreate2.append("+"+quote(", ")+"+"+getAttributeConst(dao_created));
	    StringBuilder sqlCreateEnd = new StringBuilder(") VALUES (");
	    //+2 because of created flag and id.
	    for (int i=0; i<properties.size()+2; i++){
	    	sqlCreateEnd.append("?");
	    	if (i<properties.size()+1)
	    		sqlCreateEnd.append(",");
	    }
	    sqlCreateEnd.append(")");
	    sqlCreate2.append("+").append(quote(sqlCreateEnd));
	    appendStatement(constDecl+" SQL_CREATE_1 \t= "+sqlCreate1);
	    appendStatement(constDecl+" SQL_CREATE_2 \t= "+sqlCreate2);
	    
	    //SQL-UPDATE
	    String sqlUpdate1 = quote("UPDATE ");
	    StringBuilder sqlUpdate2 = new StringBuilder(quote(" SET "));
	    for (MetaProperty p : properties){
	    	sqlUpdate2.append(" + ").append(getAttributeConst(p)).append(" + ").append(quote(" = ?, "));
	    }
    	sqlUpdate2.append(" + ").append(getAttributeConst(dao_updated)).append(" + ").append(quote(" = ?"));
	    sqlUpdate2.append(" + ").append(quote(" WHERE ")).append(" + ").append(getAttributeConst(id)).append(" + ").append(quote(" = ?"));

    	appendStatement(constDecl+" SQL_UPDATE_1 \t= "+sqlUpdate1);
    	appendStatement(constDecl+" SQL_UPDATE_2 \t= "+sqlUpdate2.toString());
	    
	    //SQL-DELETE
	    String sqlDelete1 = quote("DELETE FROM ");
	    String sqlDelete2 = quote(" WHERE ")+" + TABNAME +"+quote(".")+" + "+getAttributeConst(id)+" + "+quote(" = ?");
	    appendStatement(constDecl + " SQL_DELETE_1 \t= "+sqlDelete1);
	    appendStatement(constDecl + " SQL_DELETE_2 \t= "+sqlDelete2);

	    //SQL-READ-ONE
	    
	    StringBuilder allAttrbutes = new StringBuilder("\"");
	    allAttrbutes.append("+").append(getAttributeConst(id));
	    for (MetaProperty p : properties){
	    	allAttrbutes.append("+").append(quote(", ")).append("+").append(getAttributeConst(p));
	    }
	    allAttrbutes.append("+"+quote(", ")+"+"+getAttributeConst(dao_created));
	    allAttrbutes.append("+"+quote(", ")+"+"+getAttributeConst(dao_updated));
	    allAttrbutes.append("+\"");
	    
	    String sqlReadOne1 = quote("SELECT "+allAttrbutes+" FROM ");
	    String sqlReadOne2 = quote(" WHERE ")+" + TABNAME +"+quote(".")+" + "+getAttributeConst(id)+" + "+quote(" = ?");
	    appendStatement(constDecl + " SQL_READ_ONE_1 \t= "+sqlReadOne1);
	    appendStatement(constDecl + " SQL_READ_ONE_2 \t= "+sqlReadOne2);

	    //SQL-READ-ALL
	    String sqlReadAll1 = quote("SELECT "+allAttrbutes+" FROM ");
	    String sqlReadAll2 = quote(" ORDER BY id");
	    appendStatement(constDecl + " SQL_READ_ALL_1 \t= "+sqlReadAll1);
	    appendStatement(constDecl + " SQL_READ_ALL_2 \t= "+sqlReadAll2);
	    
	    //SQL-READ-ALL
	    String sqlReadAllByProperty1 = quote("SELECT "+allAttrbutes+" FROM ");
	    String sqlReadAllByProperty2 = quote(" WHERE ");
	    appendStatement(constDecl + " SQL_READ_ALL_BY_PROPERTY_1 \t= "+sqlReadAllByProperty1);
	    appendStatement(constDecl + " SQL_READ_ALL_BY_PROPERTY_2 \t= "+sqlReadAllByProperty2);

	    emptyline();
	    appendStatement("private RowMapper<"+doc.getName()+"> rowMapper = new "+doc.getName()+"RowMapper()");
	    
	    emptyline();
	    //create impl


	    if (moduleDbContextSensitive){
	    	appendStatement("private Map<String,AtomicLong> lastIds = new HashMap<String,AtomicLong>()");
	    }else{
		    appendStatement("private AtomicLong lastId = new AtomicLong()");
	    }
	    appendStatement("private static final long START_ID = 0");
	    emptyline();
	    
	    //get last id method
	    appendString("private AtomicLong getLastId(Connection con) throws DAOException {");
	    increaseIdent();
	    if (moduleDbContextSensitive){
		    appendStatement("DBContext context = ContextManager.getCallContext().getDbContext()");
		    appendStatement("String tableName = context.getTableNameInContext(TABNAME)");
	    	appendStatement("AtomicLong lastId = lastIds.get(tableName)");
	    	appendString("if (lastId==null){");
	    	increaseIdent();
	    	appendCommentLine("double-checked-locking");
	    	appendString("synchronized(lastIds){");
	    	increaseIdent();
	    	appendStatement("lastId = lastIds.get(tableName)");
	    	appendString("if (lastId==null){");
	    	increaseIdent();
        	appendStatement("long maxId = getMaxId(con, tableName)");
        	appendStatement("lastId = new AtomicLong(maxId == 0 ? START_ID : maxId)");
        	appendStatement("lastIds.put(tableName, lastId)");
        	append(closeBlock());
        	append(closeBlock());
        	append(closeBlock());
        	appendStatement("return lastId");
	    	
	    }else{
	    	appendStatement("return lastId");
	    	
	    }
	    append(closeBlock());
	    emptyline();
	    
	    //get last id method
	    appendString("private void adjustLastId(Connection con, long lastIdValue) throws DAOException {");
	    increaseIdent();
	    if (moduleDbContextSensitive){
	    	appendStatement("throw new RuntimeException(\"Not yet implemented\")");
	    }else{
	    	appendString("if (lastId.get()<lastIdValue)");
	    	appendIncreasedStatement("lastId.set(lastIdValue)");
	    }
	    append(closeBlock());
	    emptyline();

	    
	    //createSQL Method
	    appendString("private String createSQL(String sql1, String sql2){");
	    increaseIdent();
	    if (moduleDbContextSensitive){
		    appendStatement("DBContext context = ContextManager.getCallContext().getDbContext()");
		    appendStatement("StringBuilder sql = new StringBuilder()");
		    appendStatement("sql.append(sql1).append(context.getTableNameInContext(TABNAME)).append(sql2)");
		    appendStatement("return sql.toString()");
	    }else{
		    appendStatement("StringBuilder sql = new StringBuilder()");
		    appendStatement("sql.append(sql1).append(TABNAME).append(sql2)");
		    appendStatement("return sql.toString()");
	    }
	    append(closeBlock());
	    emptyline();
	    
	    String throwsClause = " throws DAOException";
	    String callLog = "";
	    
        //get all XYZ method
        callLog = quote("get"+doc.getMultiple()+"(")+"+con+"+quote(")");
        appendComment("Returns all "+doc.getMultiple()+" objects stored.");
        openFun("public List<"+doc.getName()+">"+" get"+doc.getMultiple()+"(Connection con)"+throwsClause);
        generateFunctionStart("SQL_READ_ALL", callLog, true);
        appendStatement("ResultSet result = ps.executeQuery()");
        appendStatement("ArrayList<"+doc.getName()+"> ret = new ArrayList<"+doc.getName()+">()");
        appendString("while(result.next())");
		appendIncreasedStatement("ret.add(rowMapper.map(result))");
		appendStatement("return  ret");
        generateFunctionEnd(callLog, true);
        append(closeBlock());
        emptyline();
        
        appendComment("Deletes a "+doc.getName()+" object by id.");
        callLog = quote("delete"+doc.getName()+"(")+"+con+"+quote(", ")+"+id+"+quote(")");
        
        openFun("public void delete"+doc.getName()+"(Connection con, String id)"+throwsClause);
        generateFunctionStart("SQL_DELETE", callLog, true);
        appendStatement("ps.setLong(1, Long.parseLong(id))");
        appendStatement("int rows = ps.executeUpdate()");
        appendString("if (rows!=1 && rows!=0){");
        increaseIdent();
        appendStatement("log.warn(\"Deleted more than one row of "+doc.getName()+": \"+id)");
		append(closeBlock());
		generateFunctionEnd(callLog, true);
        append(closeBlock());
        emptyline();
        
        //deleteListXYZ method
        String listDecl = "List<"+doc.getName()+">";
        appendComment("Deletes multiple "+doc.getName()+" objects.");
        callLog = quote("delete"+doc.getMultiple()+"(")+"+con+"+quote(", ")+"+list+"+quote(")");
        
        openFun("public void delete"+doc.getMultiple()+"(Connection con, "+listDecl+" list)"+throwsClause);
        appendStatement("PreparedStatement ps = null");
        appendString("try{");
        increaseIdent();
        appendStatement("con.setAutoCommit(false)");
        appendStatement("ps = con.prepareStatement(createSQL(SQL_DELETE_1, SQL_DELETE_2))");
        appendString("for ("+doc.getName()+" "+doc.getVariableName()+" : list){");
        increaseIdent();
        appendStatement("ps.setLong(1, Long.parseLong("+doc.getVariableName()+".getId()))");
        appendStatement("int rows = ps.executeUpdate()");
        appendString("if (rows!=1 && rows!=0){");
        increaseIdent();
        appendStatement("log.warn(\"Deleted more than one row of "+doc.getName()+": \"+"+doc.getVariableName()+".getId())");
		append(closeBlock());
		append(closeBlock());
		appendStatement("con.commit()");
		generateFunctionEnd(callLog, true);
        append(closeBlock());
        emptyline();
        
        //getXYZ method
        callLog = quote("get"+doc.getName()+"(")+"+con+"+quote(", ")+"+id+"+quote(")");
        appendComment("Returns the "+doc.getName()+" object with the specified id.");
        openFun("public "+doc.getName()+" get"+doc.getName()+"(Connection con, String id)"+throwsClause);
        appendNullCheck("con", "Null arg: con");
        appendNullCheck("id", "Null arg: id");        
        generateFunctionStart("SQL_READ_ONE", callLog, true);
        appendStatement("ps.setLong(1, Long.parseLong(id))");
        appendStatement("ResultSet result = ps.executeQuery()");
        appendString("if (!result.next())");
        appendIncreasedStatement("throw new "+getNoItemExceptionName(doc)+"(id)");
		appendStatement("return rowMapper.map(result)");
        generateFunctionEnd(callLog, true);
        append(closeBlock());
        emptyline();
        
        int ii = 0;
        
        //ImportXYZ method
        callLog = quote("import"+doc.getName()+"(")+"+con+"+quote(", ")+"+"+doc.getVariableName()+"+"+quote(")");
        appendComment("Imports a new "+doc.getName()+" object.\nReturns the imported version.");
        openFun("public "+doc.getName()+" import"+doc.getName()+"(Connection con, "+doc.getName()+" "+doc.getVariableName()+")"+throwsClause);
        generateFunctionStart("SQL_CREATE", callLog, true);
        //appendStatement("long nextId = getLastId(con).incrementAndGet()"));
        appendStatement("ps.setLong(1, Long.parseLong("+doc.getVariableName()+".getId()))");
        for (int i=0; i<properties.size(); i++){
        	generateDB2PropertyMapping(doc.getVariableName(), properties.get(i), i+2);
        	ii = i +2;
        }
        appendCommentLine("set create timestamp");
        appendStatement("ps.setLong("+(ii+1)+", System.currentTimeMillis())");

        appendStatement("int rows = ps.executeUpdate()");
        appendString("if (rows!=1)");
        appendIncreasedStatement("throw new DAOException(\"Create failed, updated rows: \"+rows)");
        
        String copyResVarName = "new"+StringUtils.capitalize(doc.getVariableName());
        String createCopyCall = VOGenerator.getDocumentImplName(doc)+" "+copyResVarName + " = new "+VOGenerator.getDocumentImplName(doc);
        createCopyCall += "("+doc.getVariableName()+".getId())";
        appendStatement(createCopyCall);
        appendStatement(copyResVarName+".copyAttributesFrom("+doc.getVariableName()+")");
        appendStatement("adjustLastId(con, Long.parseLong("+doc.getVariableName()+".getId()))");
        
        appendStatement("return "+copyResVarName);
        generateFunctionEnd(callLog, true);
        
        append(closeBlock());
        emptyline();
        ii = 0;
        
        //createXYZ method
        callLog = quote("create"+doc.getName()+"(")+"+con+"+quote(", ")+"+"+doc.getVariableName()+"+"+quote(")");
        appendComment("Creates a new "+doc.getName()+" object.\nReturns the created version.");
        openFun("public "+doc.getName()+" create"+doc.getName()+"(Connection con, "+doc.getName()+" "+doc.getVariableName()+")"+throwsClause);
        generateFunctionStart("SQL_CREATE", callLog, true);
        appendStatement("long nextId = getLastId(con).incrementAndGet()");
        appendStatement("ps.setLong(1, nextId)");
        for (int i=0; i<properties.size(); i++){
        	generateDB2PropertyMapping(doc.getVariableName(), properties.get(i), i+2);
        	ii = i +2;
        }
        appendCommentLine("set create timestamp");
        appendStatement("ps.setLong("+(ii+1)+", System.currentTimeMillis())");

        appendStatement("int rows = ps.executeUpdate()");
        appendString("if (rows!=1)");
        appendIncreasedStatement("throw new DAOException(\"Create failed, updated rows: \"+rows)");
        
        copyResVarName = "new"+StringUtils.capitalize(doc.getVariableName());
        createCopyCall = VOGenerator.getDocumentImplName(doc)+" "+copyResVarName + " = new "+VOGenerator.getDocumentImplName(doc);
        createCopyCall += "(\"\"+nextId)";
        appendStatement(createCopyCall);
        appendStatement(copyResVarName+".copyAttributesFrom("+doc.getVariableName()+")");
        
        appendStatement("return "+copyResVarName);
        generateFunctionEnd(callLog, true);
        
        append(closeBlock());
        emptyline();


        //createListXYZ method
//        String listDecl = "List<"+doc.getName()+">";
        callLog = quote("create"+doc.getMultiple()+"(")+"+con+"+quote(", ")+"+list+"+quote(")");
        appendComment("Creates multiple new "+doc.getName()+" objects.\nReturns the created versions.");
        openFun("public "+listDecl+" create"+doc.getMultiple()+"(Connection con, "+listDecl+" list)"+throwsClause);
        //append(generateFunctionStart("SQL_CREATE", callLog, true));
        appendStatement("PreparedStatement ps = null");
        appendString("try{");
        increaseIdent();
        appendStatement("con.setAutoCommit(false)");
        appendStatement("ps = con.prepareStatement(createSQL(SQL_CREATE_1, SQL_CREATE_2))");
        appendStatement(listDecl+" ret = new ArrayList<"+doc.getName()+">()");
        appendString("for ("+doc.getName()+" "+doc.getVariableName()+" : list){");
        increaseIdent();
        appendStatement("long nextId = getLastId(con).incrementAndGet()");
        appendStatement("ps.setLong(1, nextId)");
        for (int i=0; i<properties.size(); i++){
        	generateDB2PropertyMapping(doc.getVariableName(), properties.get(i), i+2);
        	ii = i +2;
        }
        appendCommentLine("set create timestamp");
        appendStatement("ps.setLong("+(ii+1)+", System.currentTimeMillis())");

        appendStatement("int rows = ps.executeUpdate()");
        appendString("if (rows!=1)");
        appendIncreasedStatement("throw new DAOException(\"Create failed, updated rows: \"+rows)");
        
        /*String*/ copyResVarName = "new"+StringUtils.capitalize(doc.getVariableName());
        /*String*/ createCopyCall = VOGenerator.getDocumentImplName(doc)+" "+copyResVarName + " = new "+VOGenerator.getDocumentImplName(doc);
        createCopyCall += "(\"\"+nextId)";
        appendStatement(createCopyCall);
        appendStatement(copyResVarName+".copyAttributesFrom("+doc.getVariableName()+")");
        
        appendStatement("ret.add("+copyResVarName+")");
        append(closeBlock());
        appendStatement("con.commit()");
        appendStatement("return ret");
        generateFunctionEnd(callLog, true);
        
        append(closeBlock());
        emptyline();

        //updateXYZ method
        callLog = quote("update"+doc.getName()+"(")+"+con+"+quote(", ")+"+"+doc.getVariableName()+"+"+quote(")");
        appendComment("Updates a "+doc.getName()+" object.\nReturns the updated version.");
        openFun("public "+doc.getName()+" update"+doc.getName()+"(Connection con, "+doc.getName()+" "+doc.getVariableName()+")"+throwsClause);
        generateFunctionStart("SQL_UPDATE", callLog, true);

        for (int i=0; i<properties.size(); i++){
        	generateDB2PropertyMapping(doc.getVariableName(), properties.get(i), i+1);
        	ii = i+1;
        }
        appendCommentLine("set update timestamp");
        appendStatement("ps.setLong("+(ii+1)+", System.currentTimeMillis())");
        appendCommentLine("set id for the where clause");
        appendStatement("ps.setLong("+(ii+2)+", Long.parseLong("+doc.getVariableName()+".getId()))");

        appendStatement("int rows = ps.executeUpdate()");
        appendString("if (rows!=1)");
        appendIncreasedStatement("throw new DAOException(\"Update failed, updated rows: \"+rows)");

        appendStatement("return "+doc.getVariableName());
        generateFunctionEnd(callLog, true);
        append(closeBlock());
        emptyline();
        
        
        //updateListXYZ method
        callLog = quote("update"+doc.getMultiple()+"(")+"+con+"+quote(", ")+"+list+"+quote(")");
        appendComment("Updates multiple new "+doc.getName()+" objects.\nReturns the updated versions.");
        openFun("public "+listDecl+" update"+doc.getMultiple()+"(Connection con, "+listDecl+" list)"+throwsClause);
        appendStatement("PreparedStatement ps = null");
        appendString("try{");
        increaseIdent();
        appendStatement("con.setAutoCommit(false)");
        appendStatement("ps = con.prepareStatement(createSQL(SQL_UPDATE_1, SQL_UPDATE_2))");
        appendStatement(listDecl+" ret = new ArrayList<"+doc.getName()+">()");
        appendString("for ("+doc.getName()+" "+doc.getVariableName()+" : list){");
        increaseIdent();
        for (int i=0; i<properties.size(); i++){
        	generateDB2PropertyMapping(doc.getVariableName(), properties.get(i), i+1);
        	ii = i + 1;
        }
        appendCommentLine("set update timestamp");
        appendStatement("ps.setLong("+(ii+1)+", System.currentTimeMillis())");
        appendCommentLine("set id for the where clause");
        appendStatement("ps.setLong("+(ii+2)+", Long.parseLong("+doc.getVariableName()+".getId()))");

        appendStatement("int rows = ps.executeUpdate()");
        appendString("if (rows!=1)");
        appendIncreasedStatement("throw new DAOException(\"Update failed, updated rows: \"+rows)");
        
        append(closeBlock());
        appendStatement("con.commit()");
        appendStatement("return list");
        generateFunctionEnd(callLog, true);
        
        append(closeBlock());
        emptyline();
        //end updateListXYZ
        
        //get all XYZ byProperty method
        callLog = quote("get"+doc.getMultiple()+"ByProperty(")+"+con+"+quote(",")+"+ properties+"+quote(")");
        appendComment("Returns all "+doc.getMultiple()+" objects stored which matches given properties.");
        openFun("public List<"+doc.getName()+">"+" get"+doc.getMultiple()+"ByProperty(Connection con, List<QueryProperty> properties)"+throwsClause);
        //append(generateFunctionStart("SQL_READ_ALL_BY_PROPERTY", callLog, true));
		appendStatement("PreparedStatement ps = null");
		openTry();
		//TODO Caching fuer generierte SQL Statements
        appendCommentLine("//enable caching of statements one day");
        appendStatement("String SQL = createSQL(SQL_READ_ALL_BY_PROPERTY_1, SQL_READ_ALL_BY_PROPERTY_2)");
        appendStatement("String whereClause = "+quote(""));
        appendString("for (QueryProperty p : properties){");
        increaseIdent();
        appendString("if (whereClause.length()>0)");
        appendIncreasedStatement("whereClause += "+quote(" AND "));
        appendStatement("String statement = p.unprepaireable()? (String) p.getValue(): " + quote("?"));
        appendStatement("whereClause += p.getName()+p.getComparator()+statement");
        append(closeBlock());
        appendStatement("SQL += whereClause");
        //appendStatement("System.out.println(SQL)"));
        appendStatement("ps = con.prepareStatement(SQL)");
        //set properties
		appendStatement("int propertyPosition = 0");
        appendString("for (QueryProperty property: properties){");
        increaseIdent();
        appendString("if(property.unprepaireable())");
        appendIncreasedStatement("continue");
        appendStatement("setProperty(++propertyPosition, ps, property)");
        append(closeBlock());        
        
        appendStatement("ResultSet result = ps.executeQuery()");
        appendStatement("ArrayList<"+doc.getName()+"> ret = new ArrayList<"+doc.getName()+">()");
        appendString("while(result.next())");
		appendIncreasedStatement("ret.add(rowMapper.map(result))");
		appendStatement("return  ret");
        generateFunctionEnd(callLog, true);
        append(closeBlock());
        emptyline();
        
        //setProperty
        openFun("private void setProperty(int position, PreparedStatement ps, QueryProperty property) throws SQLException");
        appendString("if(property.unprepaireable()){");
        increaseIdent();	
        appendStatement("return");
    	append(closeBlock());
        for (MetaProperty p : properties){
        	appendString("if ("+getAttributeConst(p)+".equals(property.getName().toLowerCase())){");
        	increaseIdent();
        	appendStatement(getDB2PropertyCallMapping("property.getValue()", p, "position"));
        	appendStatement("return");
        	append(closeBlock());
        }
        MetaProperty rawId = new MetaProperty("id","long");
    	appendString("if ("+getAttributeConst(id)+".equals(property.getName())){");
    	increaseIdent();
    	appendStatement(getDB2PropertyCallMapping("property.getValue()", rawId, "position"));
    	appendStatement("return");
    	append(closeBlock());
    	appendString("if ("+quote(dao_created.getName())+".equals(property.getName())){");
    	increaseIdent();
    	appendStatement(getDB2PropertyCallMapping("property.getValue()", dao_created, "position"));
    	appendStatement("return");
    	append(closeBlock());
    	appendString("if ("+quote(dao_updated.getName())+".equals(property.getName())){");
    	increaseIdent();
    	appendStatement(getDB2PropertyCallMapping("property.getValue()", dao_updated, "position"));
    	appendStatement("return");
    	append(closeBlock());

        append(closeBlock()); //end setProperty
        
       //special functions
//	        appendComment("Returns all "+doc.getName()+" objects, where property with given name equals object."));
//	        appendStatement("public "+listDecl+" get"+doc.getMultiple()+"ByProperty(String propertyName, Object value)"));
//	        emptyline();
//			appendComment("Returns all "+doc.getName()+" objects, where property with given name equals object, sorted"));
//			appendStatement("public "+listDecl+" get"+doc.getMultiple()+"ByProperty(String propertyName, Object value, SortType sortType)"));
//			emptyline();
//			appendComment("Executes a query"));
//			appendStatement("public QueryResult executeQueryOn"+doc.getMultiple()+"(DocumentQuery query)"));
//			emptyline();
	    
	    
//		appendComment("creates an xml element with all contained data."));
//		appendStatement("public Element exportToXML()"));
//		emptyline();
        
        appendString("/* ---------- SQL --------- ");
        generateSQLCreate(doc, dao_created, dao_updated);
        appendString("   ---------- SQL --------- */");
        
        openFun("public void createStructure(Connection connection) "+throwsClause);
        appendCommentLine("not implemented");
        append(closeBlock());
        emptyline();
        		
        appendString("/* ---------- SQL --------- ");
        generateSQLDelete(doc);
        appendString("   ---------- SQL --------- */");
        openFun("public void deleteStructure(Connection connection) "+throwsClause);
        appendCommentLine("not implemented");
        append(closeBlock());
        emptyline();
        
        openFun("protected void finish(Statement st)");
        append(closeBlock());
        emptyline();

        openFun("private long getMaxId(Connection con, String tableName) "+throwsClause);
        appendStatement("Statement st = null");
        openTry();
        appendStatement("con.setAutoCommit(true)");
        appendStatement("st = con.createStatement()");
    	appendStatement("st.execute(\"SELECT MAX(\"+"+getAttributeConst(id)+"+\") FROM \"+tableName)");
    	appendStatement("ResultSet set = st.getResultSet()");
    	appendStatement("long maxId = 0");
    	appendString("if (set.next())");
    	appendIncreasedStatement("maxId = set.getLong(1)");
    	appendStatement("log.info(\"maxId in table \"+tableName+\" is \"+maxId)");
    	appendStatement("set.close()");
    	appendStatement("st.close()");
    	appendStatement("return maxId");
    	
    	generateFunctionEnd(quote("getMaxId(")+"+con+"+quote(", ")+"+tableName+"+quote(")"), false);
        append(closeBlock());
        emptyline();
        
        //init() method
        openFun("public void init(Connection con) "+throwsClause);
        appendStatement("log.debug(\"Called: init(\"+con+\")\")");
        if (!moduleDbContextSensitive){
        	appendStatement("long maxId = getMaxId(con, TABNAME)");
        	appendStatement("lastId = new AtomicLong(maxId == 0 ? START_ID : maxId)");
        }
    	append(closeBlock());

    	return clazz;
	}
	
	private void generateSQLDelete(MetaDocument doc){
		appendString("DROP TABLE "+getSQLTableName(doc));
	}
	
	private void generateSQLCreate(MetaDocument doc, MetaProperty... additionalProps){
		appendString("CREATE TABLE "+getSQLTableName(doc)+"(");
		appendString("id int8 PRIMARY KEY,");
		for (int i=0; i<doc.getProperties().size(); i++){
			appendString(getSQLPropertyDefinition(doc.getProperties().get(i))+",");
		}
		for (int i=0; i<doc.getLinks().size(); i++){
			appendString(getSQLPropertyDefinition(doc.getLinks().get(i))+",");
		}
		for (int i=0; i<additionalProps.length-1; i++)
			appendString(getSQLPropertyDefinition(additionalProps[i])+",");
		appendString(getSQLPropertyDefinition(additionalProps[additionalProps.length-1]));
		
		appendString(")");
	}
	
	private String getSQLPropertyDefinition(MetaProperty p){
		return getAttributeName(p)+" "+getSQLPropertyType(p);
	}
	
	/**
	 * This method maps MetaProperties Types to SQL DataTypes.
	 * @param p
	 * @return
	 */
	private String getSQLPropertyType(MetaProperty p){
		if (p.getType().equals("string"))
			return "varchar";
		if (p.getType().equals("text"))
			return "varchar";
		if (p.getType().equals("long"))
			return "int8";
		if (p.getType().equals("int"))
			return "int";
		if (p.getType().equals("double"))
			return "double precision";
		if (p.getType().equals("float"))
			return "float4";
		if (p.getType().equals("boolean"))
			return "boolean";
		return "UNKNOWN!";
	}

	private String getSQLTableName(MetaDocument doc){
		return doc.getName().toLowerCase();
	}
	
	private void generateFunctionStart(String SQL_STATEMENT, String callLog, boolean usePreparedSt){
		if (usePreparedSt){
			appendStatement("PreparedStatement ps = null");
			openTry();
			appendStatement("con.setAutoCommit(true)");
			appendStatement("ps = con.prepareStatement(createSQL("+SQL_STATEMENT+"_1, "+SQL_STATEMENT+"_2))");
		}else{
			appendStatement("Statement st = null");
			openTry();
			appendStatement("con.setAutoCommit(true)");
			//ret += writeStatement("ps = con.prepareStatement("+SQL_STATEMENT+")");
		}
	}
	
	private void generateFunctionEnd(String callLog, boolean usePreparedSt){
		decreaseIdent();
		appendString("}catch(SQLException e){");
		increaseIdent();
		appendStatement("log.error("+callLog+", e)");
		appendStatement("throw new DAOSQLException(e)");
		decreaseIdent();
		appendString("}finally{");
		increaseIdent();
		appendStatement("finish("+(usePreparedSt ? "ps": "st")+")");
		append(closeBlock());
	}
	
	
	public static final String getExceptionName(MetaDocument doc){
		return getDAOName(doc)+"Exception";
	}
	
	public static final String getNoItemExceptionName(MetaDocument doc){
		return getDAOName(doc)+"NoItemForIdFoundException";
	}

	public static String getDAOName(MetaDocument doc){
	    return doc.getName()+"DAO";
	}

	public static String getRowMapperName(MetaDocument doc){
	    return doc.getName()+"RowMapper";
	}
}
