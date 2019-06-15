package com.xiaoniang.epub.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class Log {
	static private PrintWriter writer;
	public static void println (String line) {
			writer.println(line);
	}
	public static void println(List<String> list) {
		for (String line : list) {
			writer.println(line);
		}
	}
	public static void print (String string) {
		writer.print(string);
	}
	public static void start() {
		try {
			writer = new PrintWriter(new File("src/com/xiaoniang/epub/resources/log.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("[!] Didn't find the log file");
		}
	}
	public static void end() {
		writer.close();
	}
	public static PrintWriter writer() {
		return writer;
	}
}
