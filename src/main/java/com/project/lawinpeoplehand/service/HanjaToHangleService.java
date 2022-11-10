package com.project.lawinpeoplehand.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


@Service
public class HanjaToHangleService {
	
	private static final char[] HANJA_TO_HANGLE_MAP = new char[2 << 16];

	static {
		ClassPathResource resource = new ClassPathResource("xml/hanjatohangle.xml");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
		try {
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			Document document = documentBuilder.parse(resource.getFile());
			Element root = document.getDocumentElement();
			Element unicodeMap = (Element) root.getElementsByTagName("UnicodeMap").item(0);
			Element code = (Element) unicodeMap.getElementsByTagName("Code").item(0);
			
			StringTokenizer tknizer = new StringTokenizer(code.getTextContent(), ",");
			for(int i = 0; tknizer.hasMoreTokens(); i++) {
				int value = Integer.decode(tknizer.nextToken().trim());
				HANJA_TO_HANGLE_MAP[i] = (char)value;
			}
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	boolean containHanja(String overview) {
		final Pattern pattern = Pattern.compile("[一-龥]");
		Matcher matcher = pattern.matcher(overview);
		
		return matcher.find();
	}
	
	List<HanjaOrHangle> split(String overview) {
		final Pattern pattern = Pattern.compile("[一-龥]+");
		Matcher matcher = pattern.matcher(overview);
		
		List<HanjaOrHangle> result = new ArrayList<>();

		int end = 0;
		while(matcher.find()) {
			String hangle = overview.substring(end, matcher.start());
			if(!hangle.isEmpty()) {
				result.add(new HanjaOrHangle(hangle, false));
			}
			
			String hanja = matcher.group();
			result.add(new HanjaOrHangle(hanja, true));
			
			end = matcher.end();
		}
		
		if(end <= overview.length()) {
			result.add(new HanjaOrHangle(overview.substring(end, overview.length()), false));
		}
		
		return result;
	}
	
	String toHangle(String hanja) throws UnsupportedEncodingException {
		char unicode = 0x0000;
		byte[] hanjaByte = hanja.getBytes("utf8");
		
		for(int i = 0 ; i < hanjaByte.length; ) {
		if( (hanjaByte[i]&0xFF) < 0x80 ) {
			i++;
			continue;
		} else if( (hanjaByte[i]&0xFF) < 0xE0 ) {
			i += 2;
			continue;
		} else if( (hanjaByte[i]&0xFF) < 0xF0 ) {
			unicode = (char)(hanjaByte[i] & 0x0f);
			i++;
			unicode = (char)(unicode << 6);
			unicode = (char)(unicode | (hanjaByte[i] & 0x3f));
			i++;
			unicode = (char)(unicode << 6);
			unicode = (char)(unicode | (hanjaByte[i] & 0x3f));
			i++;
		}
		if(HANJA_TO_HANGLE_MAP[unicode] != unicode) {
				unicode = HANJA_TO_HANGLE_MAP[unicode];
				hanjaByte[i-1] = (byte)((unicode & 0x3f) | 0x80);
				hanjaByte[i-2] = (byte)(((unicode << 2) & 0x3f00 | 0x8000) >> 8);
				hanjaByte[i-3] = (byte)(((unicode << 4) & 0x3f0000 | 0xe00000) >> 16);
				continue;
			}
		}
		return new String(hanjaByte, "UTF-8");
	}
}
