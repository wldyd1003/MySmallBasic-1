package com.coducation.smallbasic;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import com.coducation.smallbasic.codegen.GenJava;

public class MySmallBasicMain {

	public static void main(String[] args) throws IOException {
		LinkedList<String> runOption = new LinkedList<>(Arrays.asList(args));
		boolean guiOption = false;
		String Filename = null;
		
		// -gui filname
		if(runOption.contains("-gui")){
			guiOption = true;
			Filename = runOption.get(Collections.binarySearch(runOption, "-gui")+1);
		}
		
		//run
		if(!guiOption)
		{
			Scanner scan = new Scanner(System.in);

			String option;

			String mrsbFile = Config.getMostRecentSmallBasicFile();
			boolean debug = Config.getDebug();

			System.out.print("Enter your SmallBasic file name ");
			System.out.flush();
			if (mrsbFile != null) {
				System.out.print("[" + mrsbFile + "] ");
			} else
				mrsbFile = "";
			if (debug)
				System.out.print("*debug mode* ");

			System.out.print(": ");

			String aLine = scan.nextLine();
			StringTokenizer st = new StringTokenizer(aLine);

			Filename = mrsbFile;
			if (st.hasMoreTokens() != false) {
				option = st.nextToken();
				if (option.equalsIgnoreCase("debug") == false && option.equalsIgnoreCase("normal") == false) {
					Filename = option;
					Config.putMostRecentSmallBasicFile(Filename);
				} else if ("debug".equalsIgnoreCase(option)) {
					debug = true;
					Config.putDebug(debug);
				} else if ("normal".equalsIgnoreCase(option)) {
					debug = false;
					Config.putDebug(debug);
				} else if (st.hasMoreTokens()) {
					option = st.nextToken();
					if ("debug".equalsIgnoreCase(option)) {
						debug = true;
						Config.putDebug(debug);
					} else if ("normal".equalsIgnoreCase(option)) {
						debug = false;
						Config.putDebug(debug);
					}
				}
			}

			System.out.println("Test File : " + Filename);
			System.out.println("Debug Mode : " + debug);
			System.out.println("-------------------------------");
		}

		try {
			FileReader fr = new FileReader(Filename);
			LexerAnalyzer Lexing = new LexerAnalyzer(fr);
			Parser Parsing = new Parser(Lexing);
	
			// Parser Test Routine.
			Nonterminal stack = Parsing.Parsing();
			if (stack.getTree() instanceof BlockStmt) {
//				PrettyPrinter printer = new PrettyPrinter((BlockStmt) stack.getTree());
//				printer.prettyPrint();
//	
				HashMap<String, Stmt> map = new Continuous().transform((BlockStmt) stack.getTree());
				
//				Set<Map.Entry<String, Stmt>> set = map.entrySet();
//				for (Map.Entry<String, Stmt> entry : set) {
//					String key = entry.getKey();
//					Stmt stmt = entry.getValue();
//					System.out.println(key + ":");
//					new PrettyPrinter(stmt).prettyPrint();
//				}
//				System.out.println();
				
//				String[] s = {Filename};
//				GenJava g = new GenJava(new BasicBlockEnv(map), args);
//				g.codeGen(s);
	
//				System.out.println("Execution");
				try {
					new Eval(new BasicBlockEnv(map)).eval(args);
				} catch (InterpretException exn) {
					if (exn.getProgramEnd() == false)
						throw exn;
				}
			} else {
				System.err.println("Tree is not BlockStmt.");
			}
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		System.out.println("Press Enter to continue...");
		new Scanner(System.in).nextLine();

	}
}
