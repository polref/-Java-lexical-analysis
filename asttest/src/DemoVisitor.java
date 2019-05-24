import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class DemoVisitor extends ASTVisitor {
	public  static  String Name="";
	public boolean visit(FieldDeclaration node) {
		for (Object obj : node.fragments()) {
			VariableDeclarationFragment v = (VariableDeclarationFragment) obj;
			System.out.println("FieldDeclaration - Field name: " + v.getName());
		}

		return true;
	}

	public boolean visit(MethodInvocation node) {
		System.out.println("MethodInvocation - Invocation method name: "+node.getName());// 测试每个方法里所调用的方法
		System.out.println("MethodInvocation - Invocation method way: "+node.getExpression());// 输出调用方法的对象，例如commandline.createArgument().setValue("-root_dir"); 总共有三个调用commandline.createArgument()，commandline，null
		String data=node.getExpression().toString()+" "+node.getName().toString()+" ";
		writeFile.writeAPIslnvocation(data,Name);
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean visit(MethodDeclaration node) {
		System.out.println("MethodDeclaration - Method name: " + node.getName());// 得到方法名
		//System.out.println("MethodDeclaration - the character length of the method is:" + node.getLength());// 节点的长度，不过是以字符长度来计算的，不是以行数来计算的
		//System.out.println("MethodDeclaration - Parameter list of Method:\t" + node.parameters());// 得到方法的参数列表
		//System.out.println("MethodDeclaration - Return Value of Method:\t" + node.getReturnType2());// 得到方法的返回值
		String name= writeFile.filename.substring(0,writeFile.filename.length()-5)+"#"+node.getName().toString();
		Name=name;
		writeFile.writeReturnValue(node.getReturnType2(),name);
		writeFile.writeParameterList(node.parameters(),name);
		System.out.println("\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
		Block b = node.getBody();
		//System.out.println(b.statements());
		List<Statement> list = b.statements();
		String variabletype="";
		String variablename="";
		for(int i = 0;i < list.size();i++){
			if(list.get(i).getClass().getSimpleName().equals("IfStatement")){
				System.out.print(list.get(i));
				System.out.println("it a IF statement:");
				IfStatement ifs = (IfStatement) list.get(i);
				System.out.println("get expression: "+ifs.getExpression());
				System.out.println("get then statement: "+ifs.getThenStatement());
				System.out.println("get else statement: "+ifs.getElseStatement());
			}else if(list.get(i).getClass().getSimpleName().equals("WhileStatement")){
				System.out.print(list.get(i));
				System.out.println("it a WHILE statement");
				WhileStatement ifs = (WhileStatement) list.get(i);
				System.out.println("get expression: "+ifs.getExpression());
				System.out.println("get body: "+ifs.getBody());
			}else if(list.get(i).getClass().getSimpleName().equals("ForStatement")){
				System.out.print(list.get(i));
				System.out.println("it a FOR statement");
				ForStatement ifs = (ForStatement) list.get(i);
				System.out.println("get expression: "+ifs.getExpression());
				System.out.println("get body: "+ifs.getBody());
			}else if(list.get(i).getClass().getSimpleName().equals("VariableDeclarationStatement")){
				System.out.print(list.get(i));
				System.out.println("it a VariableDeclarationStatement");
				VariableDeclarationStatement ifs = (VariableDeclarationStatement) list.get(i);
				System.out.println("get Type: "+ ifs.getType());
				variabletype+=ifs.getType().toString()+" ";
				VariableDeclarationFragment vdf = (VariableDeclarationFragment) ifs.fragments().get(0);
				System.out.println("get variable name: "+ vdf.getName());
				variablename+=vdf.getName().toString();
				System.out.println("get variable value: "+ vdf.getInitializer());
			}else if(list.get(i).getClass().getSimpleName().equals("ExpressionStatement")){
				System.out.print(list.get(i));
				System.out.println("it a ExpressionStatement");
				ExpressionStatement ifs = (ExpressionStatement) list.get(i);
				Expression ex = ifs.getExpression();
				if(ex.getClass().getSimpleName().equals("Assignment")){
					Assignment as = (Assignment)ex;
					System.out.println("get LeftHandSide: "+ as.getLeftHandSide());
					System.out.println("get RightHandSide: "+ as.getRightHandSide());
					System.out.println("get Operator: "+ as.getOperator());
				}else if(ex.getClass().getSimpleName().equals("MethodInvocation")){
					MethodInvocation mi = (MethodInvocation)ex;
					System.out.println("get Name: "+ mi.getName());
					System.out.println("get Arguments: "+ mi.arguments());
					System.out.println("get Expression: "+ mi.getExpression());
					System.out.println("get Operators: "+ mi.properties());
				}
			}else if(list.get(i).getClass().getSimpleName().equals("ExpressionStatement")){
				
			}else{
				System.out.print(list.get(i).getClass().getSimpleName());
			}
		}

		//获取注释;
		System.out.println("annotation: "+node.getJavadoc());
		
		
		
		System.out.println("\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
		writeFile.writevariableType(variabletype,name);
		writeFile.writeVariableName(variablename,name);
		
		
		
		return true;
	}
	public boolean visit(TypeDeclaration node) {
		System.out.println("TypeDeclaration - Class name: " + node.getName());
		return true;
	}

	public boolean visit(BodyDeclaration node) {
		System.out.println("Body:\t" + node.getFlags());
		return true;
	}
}