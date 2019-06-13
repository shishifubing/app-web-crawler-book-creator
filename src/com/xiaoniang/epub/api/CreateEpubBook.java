package com.xiaoniang.epub.api;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xiaoniang.epub.resources.LinkPairs;

public class CreateEpubBook {

	public static void main(String[] args) {
		/*
		 * String path = "output"+File.separator; new File(path).mkdir(); for (String
		 * url : LinkPairs.links().keySet()) { try {
		 * System.out.println("[Creation - start] url: "+url); EpubBook epubBook = new
		 * EpubBook(path, url); epubBook.create(new int[] { 0 });
		 * System.out.println("[Creation - end]"); } catch (IOException e) {
		 * System.out.println("[!] Couldn't create the Epub book. url: "+ url);
		 * e.printStackTrace(); } }/
		 **/
		String string = "<p><em><strong>text";
		Matcher matcher = Pattern.compile("<([^\\s>]+)(\\s*[^>]*)>").matcher(string);
		while (matcher.find()) {
			if (!string.substring(matcher.start()).matches(matcher.group(0) + "[^<]*</" + matcher.group(1) + ">")) {
				Matcher endMatcher = Pattern.compile("(((([^<].)|(.[^/])|([^</])))*)((</[^>]+>)*)").matcher(string);
				endMatcher.find();
				string = endMatcher.group(1) + "</" + matcher.group(1) + ">" + endMatcher.group(7);
				System.out.println("group 1: "+endMatcher.group(1));
				System.out.println("start: "+matcher.group(1));
				System.out.println("end: "+endMatcher.group(7));
				System.out.println("string: "+string);
			}
		}
		System.out.println(string);
	}
}